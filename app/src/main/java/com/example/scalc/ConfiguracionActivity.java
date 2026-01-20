package com.example.scalc;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scalc.SQL.AdminSQLiteOpenHelper;

public class ConfiguracionActivity extends AppCompatActivity {

    private Button btnGuardarConfig;
    private EditText etConfHora, etConfPedido, etConfNombre;
    private AdminSQLiteOpenHelper admin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        admin = new AdminSQLiteOpenHelper(this);

        btnGuardarConfig = findViewById(R.id.btnGuardarConfig);
        etConfHora = findViewById(R.id.etConfHora);
        etConfPedido = findViewById(R.id.etConfPedido);
        etConfNombre = findViewById(R.id.etConfNombre);

        etConfHora.setText(String.valueOf(admin.getDatosUsuario().getTarifa_hora()));
        etConfPedido.setText(String.valueOf(admin.getDatosUsuario().getTarifa_pedido()));
        etConfNombre.setText(admin.getDatosUsuario().getNombre());


        btnGuardarConfig.setOnClickListener(v -> {
            actualizaUsuario();
        });
    }


    public void actualizaUsuario(){
        admin = new AdminSQLiteOpenHelper(this);

        String nombre = etConfNombre.getText().toString();
        String horaStr = etConfHora.getText().toString();
        String pedidoStr = etConfPedido.getText().toString();

        admin.actualizarUser(nombre, Double.parseDouble(horaStr), Double.parseDouble(pedidoStr));

        finish();
    }
}