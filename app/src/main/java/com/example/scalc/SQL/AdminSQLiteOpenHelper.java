package com.example.scalc.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.scalc.Model.Jornada;
import com.example.scalc.Model.Ticket;
import com.example.scalc.Model.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    // 1. Nombre de la BD y su versión
    private static final String DATABASE_NAME = "scalc_database.db";
    private static final int DATABASE_VERSION = 7;

    // 2. Nombres de las Tablas
    public static final String TABLA_USUARIO = "usuario";
    public static final String TABLA_TICKET = "ticket";
    public static final String TABLA_JORNADA = "jornada";

    // 3. Sentencias SQL para CREAR las tablas
    // Tabla USUARIO
    private static final String CREAR_TABLA_USUARIO = "CREATE TABLE " + TABLA_USUARIO + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nombre TEXT, " +
            "tarifa_hora REAL, " +
            "tarifa_pedido REAL)";

    // Tabla TICKET
    private static final String CREAR_TABLA_TICKET = "CREATE TABLE " + TABLA_TICKET + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "id_usuario INTEGER, " +
            "mes TEXT, " +
            "anio INTEGER, " +
            "total_pedidos INTEGER, " +
            "total_horas REAL, " +
            "salario_total REAL, " +
            "estado TEXT, " +
            "FOREIGN KEY(id_usuario) REFERENCES " + TABLA_USUARIO + "(id))";

    // Tabla JORNADA
    private static final String CREAR_TABLA_JORNADA = "CREATE TABLE " + TABLA_JORNADA + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "id_ticket INTEGER, " +
            "fecha TEXT, " +
            "cant_pedidos INTEGER, " +
            "cant_horas REAL, " +
            "FOREIGN KEY(id_ticket) REFERENCES " + TABLA_TICKET + "(id))";

    public AdminSQLiteOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Este método se ejecuta UNA sola vez al instalar la app
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Ejecutamos las órdenes SQL
        db.execSQL(CREAR_TABLA_USUARIO);
        db.execSQL(CREAR_TABLA_TICKET);
        db.execSQL(CREAR_TABLA_JORNADA);
    }

    // Este método se ejecuta si cambias la version de la BD
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_JORNADA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_TICKET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_USUARIO);
        onCreate(db);
    }

    public void insertarUser(String nombre, double tarifa_hora, double tarifa_pedido){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("nombre", nombre);
        values.put("tarifa_hora", tarifa_hora);
        values.put("tarifa_pedido", tarifa_pedido);
        db.insert(TABLA_USUARIO, null, values);
        db.close();
    }

    public Usuario getDatosUsuario(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLA_USUARIO, null);

        Usuario usuario = null;

        if (cursor.moveToFirst()) {
            usuario = new Usuario(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getDouble(3)
            );
        }

        cursor.close();

        return usuario;
    }

    public void actualizarUser(String nombre, double tarifa_hora, double tarifa_pedido){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("nombre", nombre);
        values.put("tarifa_hora", tarifa_hora);
        values.put("tarifa_pedido", tarifa_pedido);

        db.update(TABLA_USUARIO, values, "id = 1", null);
        db.close();
    }

    public void insertarJornada(String fechaString,int numeroPedidos, double numeroHoras){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Obtener ticket actual
        Ticket ticket = getTicketActual();

        if(ticket == null){
            ticket = insertarTicket();
        }

        values.put("id_ticket", ticket.getId());
        values.put("fecha", fechaString);
        values.put("cant_pedidos", numeroPedidos);
        values.put("cant_horas", numeroHoras);

        db.insert(TABLA_JORNADA, null, values);
        db.close();

        asignarHorasPedidosTicket(ticket);
    }

    public Ticket getTicketAnioMes(int anioBuscado, String mesBuscado){
        List<Ticket> tickets = getTickets();

        if (tickets == null) return null;

        for (Ticket ticket : tickets) {

            if(ticket != null && ticket.getMes() != null && mesBuscado != null) {

                if(ticket.getAnio() == anioBuscado && ticket.getMes().equals(mesBuscado)){
                    return ticket;
                }
            }
        }
        return null;
    }

    public Ticket insertarTicket(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        int mesInt = LocalDate.now().getMonthValue();
        String mes = selectorMes(mesInt);

        Ticket ticket = new Ticket(0, mes, getDatosUsuario());
        ticket.setAnio(LocalDate.now().getYear());
        ticket.setEstado("activo");
        ticket.setTotal_pedidos(0);
        ticket.setTotal_horas(0);
        ticket.setSalario_total(0);

        values.put("id_usuario", 1);
        values.put("anio", LocalDate.now().getYear());
        values.put("mes", mes);
        values.put("estado", "activo");
        values.put("salario_total", 0);
        values.put("total_horas", 0);
        values.put("total_pedidos", 0);


        long id = db.insert(TABLA_TICKET, null, values);
        ticket.setId((int) id);
        db.close();

        return ticket;
    }

    public List<Ticket> getTickets() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLA_TICKET + " WHERE id_usuario = 1", null);
        List<Ticket> tickets = new ArrayList<>();

        if(cursor.moveToFirst()){

            do {

                Log.d("cursorPosiciondd", String.valueOf(cursor.getPosition()));
                Ticket newTicket = new Ticket(cursor.getInt(0), cursor.getString(2), getDatosUsuario());
                newTicket.setAnio(cursor.getInt(3));
                newTicket.setTotal_pedidos(cursor.getInt(4));
                newTicket.setTotal_horas(cursor.getDouble(5));
                newTicket.setSalario_total(cursor.getDouble(6));
                newTicket.setEstado(cursor.getString(7));

                tickets.add(newTicket);

            }while (cursor.moveToNext());
        }
        cursor.close();
        return tickets;
    }

    public String selectorMes(int n){
        switch (n){
            case 1:
                return "Enero";
            case 2:
                return "Febrero";
            case 3:
                return "Marzo";
            case 4:
                return "Abril";
            case 5:
                return "Mayo";
            case 6:
                return "Junio";
            case 7:
                return "Julio";
            case 8:
                return "Agosto";
            case 9:
                return "Septiembre";
            case 10:
                return "Octubre";
            case 11:
                return "Noviembre";
            case 12:
                return "Diciembre";
            default:
                return "";
        }
    }

    public List<Jornada> getJornadas(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLA_JORNADA + " WHERE id_ticket = " + id + " ORDER BY id DESC", null);
        List<Jornada> jornadas = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{

                Jornada newJornada = new Jornada(cursor.getInt(0), id, cursor.getString(2), cursor.getInt(3),
                        cursor.getDouble(4));

                jornadas.add(newJornada);

            } while ( cursor.moveToNext() );
        }

        return jornadas;
    }

    public void asignarHorasPedidosTicket(Ticket ticket) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        double totalHoras = 0.0;
        int totalPedidos = 0;

        for (Jornada jornada : getJornadas(ticket.getId())) {
            totalHoras += jornada.getCant_horas();
            totalPedidos += jornada.getCant_pedidos();
        }

        ticket.setTotal_horas(totalHoras);
        ticket.setTotal_pedidos(totalPedidos);
        ticket.calcularSalarioTotal();

        Usuario usr = getDatosUsuario();

        values.put("total_horas", ticket.getTotal_horas());
        values.put("total_pedidos", ticket.getTotal_pedidos());
        values.put("salario_total", ticket.getSalario_total());

        db.update(TABLA_TICKET, values, "id = " + ticket.getId(), null);
        db.close();
    }

    public Ticket getTicketActual(){
        return getTicketAnioMes(LocalDate.now().getYear(), selectorMes(LocalDate.now().getMonthValue()));
    }

    public void forzarRecalcularTicket(){

        Ticket ticket = getTicketActual();

        if(ticket != null){
            asignarHorasPedidosTicket(ticket);
        }

    }

}