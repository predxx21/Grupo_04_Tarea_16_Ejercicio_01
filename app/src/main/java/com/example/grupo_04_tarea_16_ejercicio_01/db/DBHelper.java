package com.example.grupo_04_tarea_16_ejercicio_01.db;

import android.content.Context;

import com.example.grupo_04_tarea_16_ejercicio_01.Modelo.Experiencia;
import com.example.grupo_04_tarea_16_ejercicio_01.Modelo.Usuario;

import java.util.ArrayList;

public class DBHelper {
    private DBAdapter dbAdapter;

    public DBHelper(Context context){
        dbAdapter = new DBAdapter(context);
    }

    // ==========================================
    //            GESTIÓN DE USUARIOS
    // ==========================================

    public void Registrar_Usuario(Usuario usuario) {
        dbAdapter.open();
        dbAdapter.registrarUsuario(usuario);
        dbAdapter.close();
    }

    public Usuario Validar_Login(String correo, String password) {
        dbAdapter.open();
        Usuario usuario = dbAdapter.validarLogin(correo, password);
        dbAdapter.close();
        return usuario;
    }

    // ==========================================
    //          GESTIÓN DE EXPERIENCIAS
    // ==========================================

    public void Insertar_Experiencia(Experiencia experiencia) {
        dbAdapter.open();
        dbAdapter.insertarExperiencia(experiencia);
        dbAdapter.close();
    }

    // Este es el que usará el INTEGRANTE 6 para poner los PINES en el MAPA
    // y el INTEGRANTE 4 para mostrar la lista en el HISTORIAL
    public ArrayList<Experiencia> Obtener_Experiencias_Por_Usuario(int idUsuario) {
        dbAdapter.open();
        ArrayList<Experiencia> lista = dbAdapter.getExperienciasPorUsuario(idUsuario);
        dbAdapter.close();
        return lista;
    }

    // Para la sincronización (Si deciden agregar el botón de "Ya subió")
    public void Actualizar_Estado_Sync(int idExperiencia, int nuevoEstado) {
        dbAdapter.open();
        dbAdapter.cambiarEstadoSync(idExperiencia, nuevoEstado);
        dbAdapter.close();
    }
}