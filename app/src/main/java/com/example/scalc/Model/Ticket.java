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
    private String estado;

    public Ticket(int id, String mes, Usuario usuario) {
        this.id = id;
        this.mes = mes;
        this.usuario = usuario;
        this.anio = LocalDate.now().getYear();
        this.total_pedidos = 0;
        this.total_horas = 0;
        this.salario_total = 0;
        this.estado = "activo";
    }

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


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Se asigna el salario solo si el ticket esta activo, de lo contrario no se modifica.
    public void calcularSalarioTotal() {
        if(this.estado.equals("activo")){
            double salario_horas = this.total_horas * this.usuario.getTarifa_hora();
            double salario_pedidos = this.total_pedidos * this.usuario.getTarifa_pedido();

            this.salario_total = salario_horas + salario_pedidos;
        }
    }
}
