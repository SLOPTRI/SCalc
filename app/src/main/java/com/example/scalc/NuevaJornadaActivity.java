package com.example.scalc;

import android.content.Context;
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
        etFecha.setText(LocalDate.now().toString());


        cbFecha.setOnClickListener(v -> {
            revidaEstadoCheckBox();
        });

        btnGuardarJornada.setOnClickListener(v -> {
            guardaJornada();
        });
    }

    public void revidaEstadoCheckBox(){
        if(cbFecha.isChecked()){
            etFecha.setEnabled(true);
        }else{
            etFecha.setEnabled(false);
        }
    }
    public void guardaJornada(){

        String fechaString = etFecha.getText().toString();
        String nPedidos = etPedidos.getText().toString();
        String nHoras = etHoras.getText().toString();
        Double horas = Double.parseDouble(nHoras);

        if(fechaString.isEmpty()){
            etFecha.setError("Campo requerido");
            return;
        }
        if(nPedidos.isEmpty()){
            etPedidos.setError("Campo requerido, indicar 0 si es necesario");
            return;
        }
        if(nHoras.isEmpty() || horas < 1.0){
            etHoras.setError("Campo requerido, no puede ser menor a 1");
            return;
        }

        int numeroPedidos = Integer.parseInt(nPedidos);

        admin.insertarJornada(fechaString, numeroPedidos, horas);

        lanzarNotificacionExito();

        finish();
    }

    private void lanzarNotificacionExito() {
        String CHANNEL_ID = "canal_scalc_jornadas";
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);

        // 1. Crear el canal (Obligatorio para Android 8.0+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            android.app.NotificationChannel channel = new android.app.NotificationChannel(
                    CHANNEL_ID,
                    "Notificaciones SCalc",
                    android.app.NotificationManager.IMPORTANCE_HIGH // Importancia Alta para que suene
            );
            channel.setDescription("Avisos al guardar jornadas");
            notificationManager.createNotificationChannel(channel);
        }

        // 2. Construir la notificación
        androidx.core.app.NotificationCompat.Builder builder = new androidx.core.app.NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_upload_done) // Icono seguro del sistema
                .setContentTitle("✅ Jornada Guardada")
                .setContentText("Los datos se han registrado correctamente.")
                .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH) // Prioridad alta
                .setAutoCancel(true);

        // 3. Lanzar la notificación (con protección anti-crash)
        try {
            notificationManager.notify(101, builder.build());
        } catch (SecurityException e) {
            // Si falta el permiso en Android 13, mostramos un Toast como respaldo
            android.widget.Toast.makeText(this, "Notificación bloqueada por falta de permisos", android.widget.Toast.LENGTH_LONG).show();
        }
    }
}