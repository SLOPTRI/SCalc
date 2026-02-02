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

    public void enviarReporte(android.view.View view) {
        android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"soporte@scalc.com"});
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Fallo en la App");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "Hola, he encontrado un error...");

        try {
            startActivity(android.content.Intent.createChooser(intent, "Enviar email..."));
        } catch (Exception e) {
            android.widget.Toast.makeText(this, "No tienes app de correo.", android.widget.Toast.LENGTH_SHORT).show();
        }
    }

    // REQUISITO 2: Bluetooth (Hardware)
    @android.annotation.SuppressLint("MissingPermission")
    public void probarBluetooth(android.view.View view) {
        android.bluetooth.BluetoothAdapter bt = android.bluetooth.BluetoothAdapter.getDefaultAdapter();

        if (bt == null) {
            android.widget.Toast.makeText(this, "Este móvil no tiene Bluetooth", android.widget.Toast.LENGTH_SHORT).show();
        } else if (!bt.isEnabled()) {
            // Pide activar Bluetooth
            startActivity(new android.content.Intent(android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE));
        } else {
            // Simula la conexión
            android.widget.Toast.makeText(this, "✅ Bluetooth activo. Buscando dispositivos...", android.widget.Toast.LENGTH_LONG).show();
        }
    }
}