package com.example.scalc.Model;

public class Jornada {

    private int id;
    private int id_ticket;
    private String fecha;
    private int cant_pedidos;
    private double cant_horas;

    public Jornada(int id, int id_ticket, String fecha, int cant_pedidos, double cant_horas) {
        this.id = id;
        this.id_ticket = id_ticket;
        this.fecha = fecha;
        this.cant_pedidos = cant_pedidos;
        this.cant_horas = cant_horas;
    }

    // Getters y Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_ticket() {
        return id_ticket;
    }

    public void setId_ticket(int id_ticket) {
        this.id_ticket = id_ticket;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getCant_pedidos() {
        return cant_pedidos;
    }

    public void setCant_pedidos(int cant_pedidos) {
        this.cant_pedidos = cant_pedidos;
    }

    public double getCant_horas() {
        return cant_horas;
    }

    public void setCant_horas(double cant_horas) {
        this.cant_horas = cant_horas;
    }
}
