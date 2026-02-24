package com.example.scalc;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scalc.Adapter.JornadaAdapter;
import com.example.scalc.Model.Jornada;
import com.example.scalc.Model.Usuario;
import com.example.scalc.SQL.AdminSQLiteOpenHelper;

import java.util.List;

public class DetalleTicketActivity extends AppCompatActivity {

    private RecyclerView rvJornadas;
    private AdminSQLiteOpenHelper admin;
    private JornadaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_ticket);

        // Inicialización de las vistas
        rvJornadas = findViewById(R.id.rvJornadas);
        rvJornadas.setLayoutManager(new LinearLayoutManager(this));

        //Inicialización de la Base de Datos
        admin = new AdminSQLiteOpenHelper(this);

        // Recibir el ID del ticket (Enviado desde el Historial)
        int idTicketRecibido = getIntent().getIntExtra("ID_TICKET_SELECCIONADO", -1);

        if (idTicketRecibido != -1) {
            cargarJornadas(idTicketRecibido);
        } else {
            Toast.makeText(this, "Error al cargar el ticket", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
    * Función que carga las jornadas de un ticket.
    * @param idTicket: ID del ticket.
    */
    private void cargarJornadas(int idTicket) {
        // A. Consultamos la lista de jornadas de este ticket
        List<Jornada> listaJornadas = admin.getJornadas(idTicket);

        // B. Necesitamos el usuario para saber sus precios (Tarifa hora/pedido)
        Usuario miUsuario = admin.getDatosUsuario();

        // C. Verificamos que haya datos antes de pintar
        if (listaJornadas != null && !listaJornadas.isEmpty() && miUsuario != null) {

            // D. Creamos el adaptador pasando LISTA + USUARIO
            adapter = new JornadaAdapter(listaJornadas, miUsuario);

            // E. Asignamos el adaptador al RecyclerView
            rvJornadas.setAdapter(adapter);

        } else {
            Toast.makeText(this, "No hay jornadas registradas en este ticket", Toast.LENGTH_LONG).show();
        }
    }
}