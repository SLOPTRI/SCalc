package com.example.scalc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.scalc.Model.Ticket;
import com.example.scalc.Model.Usuario;
import com.example.scalc.SQL.AdminSQLiteOpenHelper;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    private AdminSQLiteOpenHelper admin;
    private Button btnNuevaJornada, btnHistorial, btnConfiguracion;
    private TextView tvSaludo, tvMesActual, tvSalarioMes, tvTotalPedidos, tvTotalHoras;
    private CardView cardResumen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Inicializamos DB y Vistas
        admin = new AdminSQLiteOpenHelper(this);

        btnNuevaJornada = findViewById(R.id.btnNuevaJornada);
        btnHistorial = findViewById(R.id.btnHistorial);
        btnConfiguracion = findViewById(R.id.btnConfiguracion);

        tvSaludo = findViewById(R.id.tvSaludo);
        tvMesActual = findViewById(R.id.tvMesActual);
        tvSalarioMes = findViewById(R.id.tvSalarioMes);
        tvTotalPedidos = findViewById(R.id.tvTotalPedidos);
        tvTotalHoras = findViewById(R.id.tvTotalHoras);

        cardResumen = findViewById(R.id.cardResumen);

        // 2. Configurar Botones
        cardResumen.setOnClickListener(v -> abreTicketActual());
        btnNuevaJornada.setOnClickListener(v -> abreNuevaJornada());
        btnHistorial.setOnClickListener(v -> abreHistorial());
        btnConfiguracion.setOnClickListener(v -> abreConfiguracion());

        cargarDatosEnPantalla();
    }

    @Override
    protected void onResume() {
        super.onResume();

        admin.forzarRecalcularTicket();

        cargarDatosEnPantalla();
    }

    /**
     * Método único para actualizar la UI con los datos de la BD.
     */
    private void cargarDatosEnPantalla() {
        // A. Cargar Usuario
        Usuario usuario = admin.getDatosUsuario();
        if (usuario != null) {
            tvSaludo.setText("Hola, " + usuario.getNombre());
        }

        // B. Cargar Fecha Actual
        String nombreMes = admin.selectorMes(LocalDate.now().getMonthValue());
        tvMesActual.setText(nombreMes + " " + LocalDate.now().getYear());

        // C. Cargar Ticket Actual
        Ticket ticketActual = admin.getTicketActual();

        if (ticketActual != null) {
            // CASO 1: SI HAY DATOS ESTE MES

            tvSalarioMes.setText(String.format("%.2f €", ticketActual.getSalario_total()));
            tvTotalPedidos.setText(String.valueOf(ticketActual.getTotal_pedidos()));
            tvTotalHoras.setText(String.format("%.2f h", ticketActual.getTotal_horas()));
        } else {
            // CASO 2: NO HAY DATOS (Es un mes nuevo o primera vez)

            tvSalarioMes.setText("0.00 €");
            tvTotalPedidos.setText("0");
            tvTotalHoras.setText("0.00 h");
        }
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

    public void abreTicketActual(){
        Intent intent  = new Intent(MainActivity.this, DetalleTicketActivity.class);
        Ticket ticketActual = admin.getTicketActual();
        intent.putExtra("ID_TICKET_SELECCIONADO", ticketActual.getId());

        startActivity(intent);
    }
}