package com.example.scalc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scalc.SQL.AdminSQLiteOpenHelper;
import com.google.android.material.textfield.TextInputLayout;


public class BienvenidaActivity extends AppCompatActivity {

    private Button btnEmpezar;
    private EditText etNombreUser, etPrecioHoraUser, etPrecioPedidoUser;
    private AdminSQLiteOpenHelper admin;
    private RadioGroup rgModalidad;
    private RadioButton rbAmbos, rbSoloHora, rbSoloPedido;
    private TextInputLayout tilPrecioHoraUser, tilPrecioPedidoUser;
    public boolean[] modalidad;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        // Inicialización de las vistas
        btnEmpezar = findViewById(R.id.btnEmpezar);
        etNombreUser = findViewById(R.id.etNombreUser);
        etPrecioHoraUser = findViewById(R.id.etPrecioHoraUser);
        etPrecioPedidoUser = findViewById(R.id.etPrecioPedidoUser);
        rgModalidad = findViewById(R.id.rgModalidad);
        rbAmbos = findViewById(R.id.rbAmbos);
        rbSoloHora = findViewById(R.id.rbSoloHora);
        rbSoloPedido = findViewById(R.id.rbSoloPedido);
        tilPrecioHoraUser = findViewById(R.id.tilPrecioHoraUser);
        tilPrecioPedidoUser = findViewById(R.id.tilPrecioPedidoUser);
        tilPrecioHoraUser.setVisibility(View.GONE);
        tilPrecioPedidoUser.setVisibility(View.GONE);

        rgModalidad.clearCheck();
        modalidad = new boolean[]{true,false,false};

        // Configuracion de las vistas

        btnEmpezar.setOnClickListener(v -> {
            registrarUsuarioAbreMain();
        });

        rbAmbos.setOnClickListener(v ->{
            tilPrecioHoraUser.setVisibility(View.VISIBLE);
            tilPrecioPedidoUser.setVisibility(View.VISIBLE);
            modalidad[0] = true;
            modalidad[1] = false;
            modalidad[2] = false;
        });

        rbSoloHora.setOnClickListener(v ->{
            tilPrecioHoraUser.setVisibility(View.VISIBLE);
            tilPrecioPedidoUser.setVisibility(View.GONE);
            modalidad[0] = false;
            modalidad[1] = true;
            modalidad[2] = false;
            etPrecioPedidoUser.setText("");
        });

        rbSoloPedido.setOnClickListener(v ->{
            tilPrecioPedidoUser.setVisibility(View.VISIBLE);
            tilPrecioHoraUser.setVisibility(View.GONE);
            modalidad[0] = false;
            modalidad[1] = false;
            modalidad[2] = true;
            etPrecioHoraUser.setText("");
        });

    }

    /**
     * Función que abre la actividad principal.
    * */
    public void abreMain(){
        Intent intent = new Intent(BienvenidaActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Función que registra un usuario en la DB y abre la actividad principal.
     */
    public void registrarUsuarioAbreMain() {
        String nombreUser = etNombreUser.getText().toString().trim();
        String precioHoraUser = etPrecioHoraUser.getText().toString().trim();
        String precioPedidoUser = etPrecioPedidoUser.getText().toString().trim();

        // 1. Validar el Nombre (Siempre obligatorio)
        if (nombreUser.isEmpty()) {
            etNombreUser.setError("El nombre es obligatorio");
            etNombreUser.requestFocus();
            return; // Cortamos la ejecución aquí si hay error
        }

        // 2. Validar Tarifa Hora (Obligatoria si es Ambos[0] o Solo Hora[1])
        if ((modalidad[0] || modalidad[1]) && precioHoraUser.isEmpty()) {
            etPrecioHoraUser.setError("El precio por hora es necesario");
            etPrecioHoraUser.requestFocus();
            return;
        }

        // 3. Validar Tarifa Pedido (Obligatoria si es Ambos[0] o Solo Pedido[2])
        if ((modalidad[0] || modalidad[2]) && precioPedidoUser.isEmpty()) {
            etPrecioPedidoUser.setError("El precio por pedido es necesario");
            etPrecioPedidoUser.requestFocus();
            return;
        }

        // 4. Si pasamos todas las validaciones, preparamos los datos
        String modalidadString = "";
        double tarifaHora = 0.0;
        double tarifaPedido = 0.0;

        if (modalidad[0]) {
            modalidadString = "HoraPedido";
            tarifaHora = Double.parseDouble(precioHoraUser);
            tarifaPedido = Double.parseDouble(precioPedidoUser);
        } else if (modalidad[1]) {
            modalidadString = "Hora";
            tarifaHora = Double.parseDouble(precioHoraUser);
            tarifaPedido = 0.0; // Lo forzamos a 0 por seguridad
        } else if (modalidad[2]) {
            modalidadString = "Pedido";
            tarifaHora = 0.0; // Lo forzamos a 0 por seguridad
            tarifaPedido = Double.parseDouble(precioPedidoUser);
        }

        // 5. Guardar en Base de Datos
        admin = new AdminSQLiteOpenHelper(this);
        admin.insertarUser(nombreUser, modalidadString, tarifaHora, tarifaPedido);

        // 6. Navegar a la pantalla principal
        abreMain();
    }

}
