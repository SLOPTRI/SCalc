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

        // Handler para esperar 2000 milisegundos (2 segundos)
        new Handler().postDelayed(() -> verificarUsuarioYRedirigir(), 2000);
    }

    /**
    * Función que verifica si hay un usuario en la base de datos.
    * Si no hay, redirige a la actividad de bienvenida.
    * Si hay, redirige a la actividad principal.
    **/
    private void verificarUsuarioYRedirigir() {
        // Abrimos conexión a la base de datos (solo lectura)
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        // Consultamos si hay filas en la tabla USUARIO
        Cursor cursor = db.rawQuery("SELECT id FROM " + AdminSQLiteOpenHelper.TABLA_USUARIO, null);

        Intent intent;

        if (cursor.moveToFirst()) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, BienvenidaActivity.class);
        }

        cursor.close();
        db.close();
        startActivity(intent);
        finish();
    }
}