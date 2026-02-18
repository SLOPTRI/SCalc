package com.example.scalc;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.example.scalc.SQL.AdminSQLiteOpenHelper;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class BienvenidaActivity extends AppCompatActivity {

    private Button btnEmpezar;
    private EditText etNombreUser, etPrecioHoraUser, etPrecioPedidoUser;
    private AdminSQLiteOpenHelper admin;
    private RadioButton rbAmbos, rbSoloHora, rbSoloPedido;
    private TextInputLayout tilPrecioHoraUser, tilPrecioPedidoUser;
    public boolean[] modalidad;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        btnEmpezar = findViewById(R.id.btnEmpezar);
        etNombreUser = findViewById(R.id.etNombreUser);
        etPrecioHoraUser = findViewById(R.id.etPrecioHoraUser);
        etPrecioPedidoUser = findViewById(R.id.etPrecioPedidoUser);
        rbAmbos = findViewById(R.id.rbAmbos);
        rbSoloHora = findViewById(R.id.rbSoloHora);
        rbSoloPedido = findViewById(R.id.rbSoloPedido);
        tilPrecioHoraUser = findViewById(R.id.tilPrecioHoraUser);
        tilPrecioPedidoUser = findViewById(R.id.tilPrecioPedidoUser);
        tilPrecioHoraUser.setVisibility(View.GONE);
        tilPrecioPedidoUser.setVisibility(View.GONE);

        modalidad = new boolean[]{false,false,false};

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
        });

        rbSoloPedido.setOnClickListener(v ->{
            tilPrecioPedidoUser.setVisibility(View.VISIBLE);
            tilPrecioHoraUser.setVisibility(View.GONE);
            modalidad[0] = false;
            modalidad[1] = false;
            modalidad[2] = true;
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
        String modalidadString = "";

        if(nombreUser.isEmpty()){
            etNombreUser.setError("El nombre es obligatorio");
            etNombreUser.requestFocus();
        } else if(precioHoraUser.isEmpty() && modalidad[1]){
            etPrecioHoraUser.setError("El precio por hora es necesario");
            etPrecioHoraUser.requestFocus();
        } else if(precioPedidoUser.isEmpty() && modalidad[2]){
            etPrecioPedidoUser.setError("El precio por pedido es necesario");
            etPrecioPedidoUser.requestFocus();
        } else{
            if(modalidad[0]){
                modalidadString = "HoraPedido";
            } else if(modalidad[1]){
                modalidadString = "Hora";
                precioPedidoUser = "0";
            } else if (modalidad[2]){
                modalidadString = "Pedido";
                precioHoraUser = "0";
            }

            admin.insertarUser(nombreUser, modalidadString, Double.parseDouble(precioHoraUser), Double.parseDouble(precioPedidoUser));
            abreMain();
        }

    }

}