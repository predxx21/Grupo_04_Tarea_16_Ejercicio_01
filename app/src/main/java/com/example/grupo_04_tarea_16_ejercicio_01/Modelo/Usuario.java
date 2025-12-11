package com.example.grupo_04_tarea_16_ejercicio_01.Modelo;

public class Usuario {
    private int id;
    private String nombre;
    private String correo;
    private String password;
    private String grupo;

    public Usuario() {
    }

    public Usuario(String nombre, String correo, String password, String grupo) {
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.grupo = grupo;
    }

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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
}
