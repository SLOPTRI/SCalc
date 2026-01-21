package com.example.scalc;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.scalc.SQL.AdminSQLiteOpenHelper;

import java.time.LocalDate;

public class NuevaJornadaActivity extends AppCompatActivity {

    private AdminSQLiteOpenHelper admin;
    private Button btnGuardarJornada;
    private EditText etFecha, etPedidos, etHoras;
    private CheckBox cbFecha;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_jornada);
        admin = new AdminSQLiteOpenHelper(this);

        btnGuardarJornada = findViewById(R.id.btnGuardarJornada);
        etFecha = findViewById(R.id.etFecha);
        etPedidos = findViewById(R.id.etPedidos);
        etHoras = findViewById(R.id.etHoras);
        cbFecha = findViewById(R.id.cbFecha);
        etFecha.setEnabled(false);


        cbFecha.setOnClickListener(v -> {
            if(cbFecha.isChecked()){
                etFecha.setEnabled(true);
            }else{
                etFecha.setEnabled(false);
            }
        });


        etFecha.setText(LocalDate.now().toString());

        btnGuardarJornada.setOnClickListener(v -> {
            guardaJornada();
        });
    }


    public void guardaJornada(){

        String fechaString = etFecha.getText().toString();
        String nPedidos = etPedidos.getText().toString();
        String nHoras = etHoras.getText().toString();

        if(fechaString.isEmpty()){
            etFecha.setError("Campo requerido");
            return;
        }
        if(nPedidos.isEmpty()){
            etPedidos.setError("Campo requerido, indicar 0 si es necesario");
            return;
        }
        if(nHoras.isEmpty() || Integer.parseInt(nHoras) < 1){
            etHoras.setError("Campo requerido, no puede ser menor a 1");
            return;
        }

        int numeroPedidos = Integer.parseInt(nPedidos);
        double numeroHoras = Double.parseDouble(nHoras);

        admin.insertarJornada(fechaString, numeroPedidos, numeroHoras);
        finish();
    }
}