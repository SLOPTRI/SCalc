package com.example.scalc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.scalc.SQL.AdminSQLiteOpenHelper;

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
            abreMain();
        }

    }

}