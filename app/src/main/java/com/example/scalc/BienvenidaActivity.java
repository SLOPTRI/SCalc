package com.example.scalc;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.example.scalc.SQL.AdminSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class BienvenidaActivity extends AppCompatActivity {

    private Button btnEmpezar;
    private EditText etNombreUser, etPrecioHoraUser, etPrecioPedidoUser;
    private AdminSQLiteOpenHelper admin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        btnEmpezar = findViewById(R.id.btnEmpezar);
        etNombreUser = findViewById(R.id.etNombreUser);
        etPrecioHoraUser = findViewById(R.id.etPrecioHoraUser);
        etPrecioPedidoUser = findViewById(R.id.etPrecioPedidoUser);

        verificarPermisos();

        btnEmpezar.setOnClickListener(v -> {
            registrarUsuarioAbreMain();
        });
    }

    public void abreMain(){
        Intent intent = new Intent(BienvenidaActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void registrarUsuarioAbreMain(){
        admin = new AdminSQLiteOpenHelper(this);

        String nombreUser = etNombreUser.getText().toString();
        String precioHoraUser = etPrecioHoraUser.getText().toString();
        String precioPedidoUser = etPrecioPedidoUser.getText().toString();

        if(nombreUser.isEmpty()){
            etNombreUser.setError("El nombre es obligatorio");
            etNombreUser.requestFocus();
        } else if(precioHoraUser.isEmpty()){
            etPrecioHoraUser.setError("El precio por hora es obligatorio");
            etPrecioHoraUser.requestFocus();
        } else if(precioPedidoUser.isEmpty()){
            etPrecioPedidoUser.setError("El precio por pedido es obligatorio");
            etPrecioPedidoUser.requestFocus();
        } else{
            admin.insertarUser(nombreUser, Double.parseDouble(precioHoraUser), Double.parseDouble(precioPedidoUser));
            abreMain();
        }

    }

    private void verificarPermisos() {
        int versionAndroid = Build.VERSION.SDK_INT;
        List<String> permisosFaltantes = new ArrayList<>();

        // 1. Permiso de Notificaciones (Solo necesario en Android 13+)
        if (versionAndroid >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permisosFaltantes.add(Manifest.permission.POST_NOTIFICATIONS);
            }
        }

        // 2. Permisos de Bluetooth (Solo necesario en Android 12+)
        if (versionAndroid >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                permisosFaltantes.add(Manifest.permission.BLUETOOTH_CONNECT);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                permisosFaltantes.add(Manifest.permission.BLUETOOTH_SCAN);
            }
        }

        // 3. Si falta algún permiso, pedirlos todos de golpe
        if (!permisosFaltantes.isEmpty()) {
            ActivityCompat.requestPermissions(this, permisosFaltantes.toArray(new String[0]), 100);
        }
    }

}