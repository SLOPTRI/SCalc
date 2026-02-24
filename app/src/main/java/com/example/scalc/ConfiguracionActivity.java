package com.example.scalc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scalc.SQL.AdminSQLiteOpenHelper;
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

        // Modalidad actual del usuario.
        modalidad = admin.getDatosUsuario().getModalidad();

        // Configuracion de las vistas.
        actualizarAccesibilidad(false);
        cbModalidad.setOnCheckedChangeListener((buttonView, isChecked) -> {
            actualizarAccesibilidad(isChecked);
        });

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


        etConfHora.setText(String.valueOf(admin.getDatosUsuario().getTarifa_hora()));
        etConfPedido.setText(String.valueOf(admin.getDatosUsuario().getTarifa_pedido()));
        etConfNombre.setText(admin.getDatosUsuario().getNombre());
        if(modalidad.equals("HoraPedido")){
            rbAmbos.setChecked(true);
        } else if(modalidad.equals("Hora")){
            rbSoloHora.setChecked(true);
            tilConfPedido.setVisibility(View.GONE);
        } else if(modalidad.equals("Pedido")){
            rbSoloPedido.setChecked(true);
            tilConfHora.setVisibility(View.GONE);
        }

        btnGuardarConfig.setOnClickListener(v -> {
            actualizaUsuario();
        });
    }

    /**
    * Función que actualiza un usuario en la DB.
    */
    public void actualizaUsuario(){
        admin = new AdminSQLiteOpenHelper(this);

        String nombre = etConfNombre.getText().toString();
        String horaStr = etConfHora.getText().toString();
        String pedidoStr = etConfPedido.getText().toString();
        String modalidad = this.modalidad;

        admin.actualizarUser(nombre, modalidad, Double.parseDouble(horaStr), Double.parseDouble(pedidoStr));

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