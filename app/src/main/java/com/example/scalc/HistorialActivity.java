package com.example.scalc;

import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial); // Asegúrate que este es tu layout

        // 1. Inicializar vistas y DB
        rvHistorial = findViewById(R.id.rvHistorial);
        admin = new AdminSQLiteOpenHelper(this);

        // 2. Configurar RecyclerView
        rvHistorial.setLayoutManager(new LinearLayoutManager(this));

        // 3. Obtener datos
        List<Ticket> listaDeTickets = admin.getTickets();

        // 4. Crear y asignar adaptador
        adapter = new TicketAdapter(listaDeTickets);
        rvHistorial.setAdapter(adapter);
    }
}