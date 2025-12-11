package com.example.grupo_04_tarea_16_ejercicio_01.model;

public class Experiencia {

    private int id;
    private int idUsuario;      // Foreign Key: Para saber quién tomó la foto
    private String titulo;
    private String descripcion; // Opcional, si quieren añadir detalle
    private String fotoPath;    // Guardamos la RUTA del archivo (ej: "/sdcard/DCIM/foto1.jpg")
    private double latitud;     // Necesario para Google Maps (ej: -12.046374)
    private double longitud;    // Necesario para Google Maps (ej: -77.042793)
    private int sincronizado;   // 0 = Pendiente (Nube roja), 1 = Subido (Nube verde)

    public Experiencia() {
    }

    public Experiencia(int idUsuario, String titulo, String descripcion, String fotoPath, double latitud, double longitud, int sincronizado) {
        this.idUsuario = idUsuario;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fotoPath = fotoPath;
        this.latitud = latitud;
        this.longitud = longitud;
        this.sincronizado = sincronizado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public void setSincronizado(int estado) {
        this.sincronizado = estado;
    }
    public int getSincronizado() {
        return this.sincronizado;
    }

    public boolean isSincronizado() {
        return this.sincronizado == 1;
    }
}
