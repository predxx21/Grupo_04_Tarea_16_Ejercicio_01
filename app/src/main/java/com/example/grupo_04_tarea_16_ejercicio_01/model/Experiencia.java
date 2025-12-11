package com.example.grupo_04_tarea_16_ejercicio_01.model;

public class Experiencia {
    private String titulo;
    private String descripcion;
    private String fecha;
    private boolean sincronizado;

    public Experiencia(String titulo, String descripcion, String fecha, boolean sincronizado) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.sincronizado = sincronizado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public boolean isSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(boolean sincronizado) {
        this.sincronizado = sincronizado;
    }
}
