package com.example.scalc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;

import com.example.scalc.SQL.AdminSQLiteOpenHelper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Ocultar la barra superior (Action Bar) para que se vea pantalla completa
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Usamos un Handler para esperar 2000 milisegundos (2 segundos)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Aquí dentro se ejecuta el código pasados los 2 segundos
                verificarUsuarioYRedirigir();
            }
        }, 2000);
    }

    private void verificarUsuarioYRedirigir() {
        // 1. Abrimos conexión a la base de datos (solo lectura)
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        // 2. Consultamos si hay filas en la tabla USUARIO
        // "SELECT id FROM usuario" -> Es una consulta ligera
        Cursor cursor = db.rawQuery("SELECT id FROM " + AdminSQLiteOpenHelper.TABLA_USUARIO, null);

        Intent intent;

        // 3. El semáforo:
        if (cursor.moveToFirst()) {
            // SÍ hay datos (el cursor pudo moverse a la primera fila)
            // Significa que el usuario ya se registró antes -> Vamos al Dashboard
            intent = new Intent(SplashActivity.this, MainActivity.class);
        } else {
            // NO hay datos (el cursor está vacío)
            // Es un usuario nuevo -> Vamos al Registro
            intent = new Intent(SplashActivity.this, BienvenidaActivity.class);
        }

        // Cerramos cursor y base de datos para liberar memoria
        cursor.close();
        db.close();

        // 4. Iniciamos la actividad decidida
        startActivity(intent);

        // 5. IMPORTANTE: Matamos la SplashActivity
        // Si no hacemos esto, cuando el usuario le de al botón "Atrás", volvería a ver el logo cargando.
        finish();
    }
}