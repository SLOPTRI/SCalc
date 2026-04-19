package com.example.scalc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scalc.Model.Usuario;
import com.example.scalc.SQL.AdminSQLiteOpenHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

public class ConfiguracionActivity extends AppCompatActivity {

    private Button btnGuardarConfig;
    private EditText etConfHora, etConfPedido, etConfNombre;
    private AdminSQLiteOpenHelper admin;
    private String modalidad;
    private RadioButton rbAmbos, rbSoloHora, rbSoloPedido;
    private RadioGroup rgModalidad;
    private TextInputLayout tilConfHora, tilConfPedido, tilConfNombre;
    private CheckBox cbModalidad;

    // Variables para guardar el estado original (El "Antes")
    private String modalidadOriginal;
    private double tarifaHoraOriginal;
    private double tarifaPedidoOriginal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        admin = new AdminSQLiteOpenHelper(this);

        // Inicialización de las vistas
        btnGuardarConfig = findViewById(R.id.btnGuardarConfig);
        etConfHora = findViewById(R.id.etConfHora);
        etConfPedido = findViewById(R.id.etConfPedido);
        etConfNombre = findViewById(R.id.etConfNombre);
        rbAmbos = findViewById(R.id.rbAmbos);
        rbSoloHora = findViewById(R.id.rbSoloHora);
        rbSoloPedido = findViewById(R.id.rbSoloPedido);
        tilConfHora = findViewById(R.id.tilConfHora);
        tilConfPedido = findViewById(R.id.tilConfPedido);
        tilConfNombre = findViewById(R.id.tilConfNombre);
        cbModalidad = findViewById(R.id.cbModalidad);
        rgModalidad = findViewById(R.id.rgModalidad);

        // Obtenemos los datos actuales del usuario y guardamos el "Antes"
        Usuario user = admin.getDatosUsuario();
        modalidadOriginal = user.getModalidad();
        tarifaHoraOriginal = user.getTarifa_hora();
        tarifaPedidoOriginal = user.getTarifa_pedido();

        // Asignamos la modalidad para trabajar con ella en la pantalla
        modalidad = user.getModalidad();

        // Configuracion inicial de las vistas (Bloqueado por defecto)
        actualizarAccesibilidad(false);
        cbModalidad.setOnCheckedChangeListener((buttonView, isChecked) -> {
            actualizarAccesibilidad(isChecked);
        });

        // Listeners de los RadioButtons
        rbAmbos.setOnClickListener(v -> {
            modalidad = "HoraPedido";
            tilConfHora.setVisibility(View.VISIBLE);
            tilConfPedido.setVisibility(View.VISIBLE);
        });
        rbSoloHora.setOnClickListener(v -> {
            modalidad = "Hora";
            tilConfHora.setVisibility(View.VISIBLE);
            tilConfPedido.setVisibility(View.GONE);
        });
        rbSoloPedido.setOnClickListener(v -> {
            modalidad = "Pedido";
            tilConfPedido.setVisibility(View.VISIBLE);
            tilConfHora.setVisibility(View.GONE);
        });

        // Rellenar los campos con los datos de la Base de Datos
        etConfHora.setText(String.valueOf(user.getTarifa_hora()));
        etConfPedido.setText(String.valueOf(user.getTarifa_pedido()));
        etConfNombre.setText(user.getNombre());

        if(modalidad.equals("HoraPedido")){
            rbAmbos.setChecked(true);
        } else if(modalidad.equals("Hora")){
            rbSoloHora.setChecked(true);
            tilConfPedido.setVisibility(View.GONE);
        } else if(modalidad.equals("Pedido")){
            rbSoloPedido.setChecked(true);
            tilConfHora.setVisibility(View.GONE);
        }

        // Listener del botón Guardar
        btnGuardarConfig.setOnClickListener(v -> {
            actualizaUsuario();
        });
    }

    /**
     * Función que valida los datos y comprueba si hay que mostrar alerta.
     */
    public void actualizaUsuario() {
        String nombre = etConfNombre.getText().toString().trim();
        String horaStr = etConfHora.getText().toString().trim();
        String pedidoStr = etConfPedido.getText().toString().trim();

        // 1. Validaciones para evitar Crasheos
        if (nombre.isEmpty()) {
            etConfNombre.setError("El nombre no puede estar vacío");
            etConfNombre.requestFocus();
            return;
        }
        if ((modalidad.equals("HoraPedido") || modalidad.equals("Hora")) && horaStr.isEmpty()) {
            etConfHora.setError("Introduce la tarifa por hora");
            etConfHora.requestFocus();
            return;
        }
        if ((modalidad.equals("HoraPedido") || modalidad.equals("Pedido")) && pedidoStr.isEmpty()) {
            etConfPedido.setError("Introduce la tarifa por pedido");
            etConfPedido.requestFocus();
            return;
        }

        // 2. Preparar los datos numéricos de forma segura
        double tarifaHoraFinal = 0.0;
        double tarifaPedidoFinal = 0.0;

        if (modalidad.equals("HoraPedido")) {
            tarifaHoraFinal = Double.parseDouble(horaStr);
            tarifaPedidoFinal = Double.parseDouble(pedidoStr);
        } else if (modalidad.equals("Hora")) {
            tarifaHoraFinal = Double.parseDouble(horaStr);
        } else if (modalidad.equals("Pedido")) {
            tarifaPedidoFinal = Double.parseDouble(pedidoStr);
        }

        // 3. Comprobar si ha cambiado la modalidad de cobro
        if (!modalidad.equals(modalidadOriginal)) {
            mostrarAlertaCambios(nombre, tarifaHoraFinal, tarifaPedidoFinal);
        } else {
            ejecutarGuardado(nombre, modalidad, tarifaHoraFinal, tarifaPedidoFinal);
        }
    }

    /**
     * Muestra una alerta Material Design detallando el antes y el después de las tarifas.
     */
    private void mostrarAlertaCambios(String nombreFinal, double horaFinal, double pedidoFinal) {

        String mensaje = "Vas a cambiar tu modalidad de cobro.\n\n" +
                "TUS VALORES ANTERIORES:\n" +
                "• Tarifa Hora: " + tarifaHoraOriginal + " €\n" +
                "• Tarifa Pedido: " + tarifaPedidoOriginal + " €\n\n" +
                "TUS NUEVOS VALORES:\n" +
                "• Tarifa Hora: " + horaFinal + " €\n" +
                "• Tarifa Pedido: " + pedidoFinal + " €\n\n" +
                "Los nuevos tickets usarán estos valores. ¿Estás seguro de guardar los cambios?";

        new MaterialAlertDialogBuilder(this)
                .setTitle("Confirmar Cambios")
                .setMessage(mensaje)
                .setPositiveButton("Sí, guardar", (dialog, which) -> {
                    ejecutarGuardado(nombreFinal, modalidad, horaFinal, pedidoFinal);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    /**
     * Ejecuta la actualización en SQLite y cierra la ventana.
     */
    private void ejecutarGuardado(String nombre, String mod, double hora, double pedido) {
        admin.actualizarUser(nombre, mod, hora, pedido);
        android.widget.Toast.makeText(this, "Ajustes guardados correctamente", android.widget.Toast.LENGTH_SHORT).show();

        finish();
    }

    /**
     * Función que actualiza la accesibilidad de las vistas.
     * @param habilitado: Booleano que indica si se debe habilitar o no.
     */
    private void actualizarAccesibilidad(boolean habilitado) {
        rgModalidad.setEnabled(habilitado);
        rbAmbos.setEnabled(habilitado);
        rbSoloHora.setEnabled(habilitado);
        rbSoloPedido.setEnabled(habilitado);

        tilConfHora.setEnabled(habilitado);
        tilConfPedido.setEnabled(habilitado);
        etConfHora.setEnabled(habilitado);
        etConfPedido.setEnabled(habilitado);
        etConfNombre.setEnabled(habilitado);
        btnGuardarConfig.setEnabled(habilitado);

        float alfa = habilitado ? 1.0f : 0.5f;
        rgModalidad.setAlpha(alfa);
        tilConfHora.setAlpha(alfa);
        tilConfPedido.setAlpha(alfa);
        tilConfNombre.setAlpha(alfa);

        int iconoRes = habilitado ? R.drawable.ic_lock_open : R.drawable.ic_lock;

        cbModalidad.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconoRes, 0);
    }
}