package com.example.scalc.Model;

import java.time.LocalDate;

public class Ticket {

    private int id;
    private Usuario usuario;
    private String mes;
    private int anio;
    private int total_pedidos;
    private double total_horas;
    private double salario_total;
    private int estado;
    private double tarifa_hora;
    private double tarifa_pedido;
    private String modalidad;

    // Constructor

    public Ticket(int id, String mes, Usuario usuario) {
        this.id = id;
        this.mes = mes;
        this.usuario = usuario;
        this.anio = LocalDate.now().getYear();
        this.total_pedidos = 0;
        this.total_horas = 0;
        this.salario_total = 0;
        this.estado = estado;
        this.tarifa_hora = usuario.getTarifa_hora();
        this.tarifa_pedido = usuario.getTarifa_pedido();
        this.modalidad = usuario.getModalidad();
    }

    public Ticket(){}

    // Getters y Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getusuario() {
        return usuario;
    }

    public void setusuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getTotal_pedidos() {
        return total_pedidos;
    }

    public void setTotal_pedidos(int total_pedidos) {
        this.total_pedidos = total_pedidos;
    }

    public double getTotal_horas() {
        return total_horas;
    }

    public void setTotal_horas(double total_horas) {
        this.total_horas = total_horas;
    }

    public double getSalario_total() {
        return salario_total;
    }

    public void setSalario_total(double salario_total) {
        this.salario_total = salario_total;
    }


    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
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

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    // Se asigna el salario solo si el ticket esta activo, de lo contrario no se modifica.
    public void calcularSalarioTotal() {
        double salario_horas = this.total_horas * this.tarifa_hora;
        double salario_pedidos = this.total_pedidos * this.tarifa_pedido;

        this.salario_total = salario_horas + salario_pedidos;
    }
}
