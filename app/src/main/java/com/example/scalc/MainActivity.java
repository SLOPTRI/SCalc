package com.example.scalc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.scalc.SQL.AdminSQLiteOpenHelper;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    private AdminSQLiteOpenHelper admin;
    private Button btnNuevaJornada, btnHistorial, btnConfiguracion;
    private TextView tvSaludo, tvMesActual, tvSalarioMes, tvTotalPedidos, tvTotalHoras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        admin = new AdminSQLiteOpenHelper(this);

        btnNuevaJornada = findViewById(R.id.btnNuevaJornada);
        btnHistorial = findViewById(R.id.btnHistorial);
        btnConfiguracion = findViewById(R.id.btnConfiguracion);

        tvSaludo = findViewById(R.id.tvSaludo);
        tvSaludo.setText("Hola, "+ admin.getDatosUsuario().getNombre());

        tvMesActual = findViewById(R.id.tvMesActual);
        tvMesActual.setText(admin.selectorMes(LocalDate.now().getMonthValue()) + " " + LocalDate.now().getYear());

        tvSalarioMes = findViewById(R.id.tvSalarioMes);

        tvTotalPedidos = findViewById(R.id.tvTotalPedidos);
        tvTotalHoras = findViewById(R.id.tvTotalHoras);


        btnNuevaJornada.setOnClickListener(v -> {
            abreNuevaJornada();
        });

        btnHistorial.setOnClickListener(v -> {
            abreHistorial();
        });

        btnConfiguracion.setOnClickListener(v -> {
            abreConfiguracion();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvSaludo.setText("Hola, "+ admin.getDatosUsuario().getNombre());
    }

    public void abreHistorial(){
        Intent intent  = new Intent(MainActivity.this, HistorialActivity.class);
        startActivity(intent);
    }

    public void abreConfiguracion(){
        Intent intent  = new Intent(MainActivity.this, ConfiguracionActivity.class);
        startActivity(intent);
    }

    public void abreNuevaJornada(){
        Intent intent  = new Intent(MainActivity.this, NuevaJornadaActivity.class);
        startActivity(intent);
    }
}