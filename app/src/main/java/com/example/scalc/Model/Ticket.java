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

    public Ticket(int id, String mes, int anio, int total_pedidos, double total_horas, double salario_total, int estado, Usuario usuario) {
        this.id = id;
        this.mes = mes;
        this.anio = anio;
        this.total_pedidos = total_pedidos;
        this.total_horas = total_horas;
        this.salario_total = salario_total;
        this.estado = estado;
        this.usuario = usuario;

        if (usuario != null) {
            this.tarifa_hora = usuario.getTarifa_hora();
            this.tarifa_pedido = usuario.getTarifa_pedido();
            this.modalidad = usuario.getModalidad();
        }
    }

    public Ticket(){}

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMes() {
        return mes;
    }

    public void setModalidad(String modalidad) { this.modalidad = modalidad; }
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

    // Se asigna el salario solo si el ticket esta activo, de lo contrario no se modifica.
    public void calcularSalarioTotal() {
        double salario_horas = this.total_horas * this.tarifa_hora;
        double salario_pedidos = this.total_pedidos * this.tarifa_pedido;

        this.salario_total = salario_horas + salario_pedidos;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", usuario=" + usuario +
                ", mes='" + mes + '\'' +
                ", anio=" + anio +
                ", total_pedidos=" + total_pedidos +
                ", total_horas=" + total_horas +
                ", salario_total=" + salario_total +
                ", estado=" + estado +
                ", tarifa_hora=" + tarifa_hora +
                ", tarifa_pedido=" + tarifa_pedido +
                ", modalidad='" + modalidad + '\'' +
                '}';
    }
}
