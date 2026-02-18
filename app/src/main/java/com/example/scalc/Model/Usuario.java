package com.example.scalc.Model;

public class Usuario {

    private int id;
    private String nombre;
    private String modalidad;
    private double tarifa_hora;
    private double tarifa_pedido;

    public Usuario(int id, String nombre,String modalidad, double tarifa_hora, double tarifa_pedido) {
        this.id = id;
        this.nombre = nombre;
        this.modalidad = modalidad;
        this.tarifa_hora = tarifa_hora;
        this.tarifa_pedido = tarifa_pedido;
    }

    // Getters y Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public double getTarifa_hora() {
        return tarifa_hora;
    }

    public void setTarifa_hora(double tarifa_hora) {
        this.tarifa_hora = tarifa_hora;
    }

    public double getTarifa_pedido() {
        return tarifa_pedido;
    }

    public void setTarifa_pedido(double tarifa_pedido) {
        this.tarifa_pedido = tarifa_pedido;
    }

}
