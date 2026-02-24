package com.example.scalc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scalc.Adapter.TicketAdapter;
import com.example.scalc.Model.Ticket;
import com.example.scalc.R;
import com.example.scalc.SQL.AdminSQLiteOpenHelper;

import java.util.List;

public class HistorialActivity extends AppCompatActivity {

    RecyclerView rvHistorial;
    AdminSQLiteOpenHelper admin;
    TicketAdapter adapter;
    Button btnEstadisticas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        // Inicialización de las vistas y Base de Datos
        rvHistorial = findViewById(R.id.rvHistorial);
        admin = new AdminSQLiteOpenHelper(this);

        // Configuración del RecyclerView
        rvHistorial.setLayoutManager(new LinearLayoutManager(this));

        // Obtener datos
        List<Ticket> listaDeTickets = admin.getTickets();

        // Crear y asignar adaptador
        adapter = new TicketAdapter(listaDeTickets);
        rvHistorial.setAdapter(adapter);

        // Configurar botón de estadísticas
        btnEstadisticas = findViewById(R.id.btnEstadisticas);
        btnEstadisticas.setOnClickListener(v -> {
            abreEstadisticas();
        });
    }

    /**
    * Función que abre la actividad de estadísticas.
    */
    public void abreEstadisticas() {
        Intent intent = new Intent(HistorialActivity.this, EstadisticasActivity.class);
        startActivity(intent);
    }
}