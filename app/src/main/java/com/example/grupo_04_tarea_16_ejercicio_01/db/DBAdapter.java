package com.example.grupo_04_tarea_16_ejercicio_01.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.grupo_04_tarea_16_ejercicio_01.model.Experiencia;
import com.example.grupo_04_tarea_16_ejercicio_01.model.Usuario;

import java.util.ArrayList;

public class DBAdapter {

    // --- INFORMACIÓN DE LA BASE DE DATOS ---
    public static final String DATABASE_NAME = "TURISMO_DB";
    public static final int DATABASE_VERSION = 1;

    // --- TABLA USUARIOS ---
    public static final String TABLE_USUARIOS = "usuarios";
    public static final String COL_USER_ID = "id";
    public static final String COL_USER_NOMBRE = "nombre";
    public static final String COL_USER_CORREO = "correo";
    public static final String COL_USER_PASSWORD = "password";
    public static final String COL_USER_GRUPO = "grupo";

    // --- TABLA EXPERIENCIAS ---
    public static final String TABLE_EXPERIENCIAS = "experiencias";
    public static final String COL_EXP_ID = "id";
    public static final String COL_EXP_USER_ID = "id_usuario"; // FK
    public static final String COL_EXP_TITULO = "titulo";
    public static final String COL_EXP_DESC = "descripcion";
    public static final String COL_EXP_FOTO = "foto_path";
    public static final String COL_EXP_LAT = "latitud";        // Para Google Maps
    public static final String COL_EXP_LNG = "longitud";       // Para Google Maps
    public static final String COL_EXP_SYNC = "sincronizado";  // 0 o 1

    // --- SENTENCIAS SQL DE CREACIÓN ---
    private static final String CREATE_TABLE_USUARIOS = "CREATE TABLE " + TABLE_USUARIOS + " (" +
            COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_USER_NOMBRE + " TEXT NOT NULL, " +
            COL_USER_CORREO + " TEXT NOT NULL UNIQUE, " +
            COL_USER_PASSWORD + " TEXT NOT NULL, " +
            COL_USER_GRUPO + " TEXT);";

    private static final String CREATE_TABLE_EXPERIENCIAS = "CREATE TABLE " + TABLE_EXPERIENCIAS + " (" +
            COL_EXP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_EXP_USER_ID + " INTEGER NOT NULL, " +
            COL_EXP_TITULO + " TEXT, " +
            COL_EXP_DESC + " TEXT, " +
            COL_EXP_FOTO + " TEXT, " +
            COL_EXP_LAT + " REAL, " +
            COL_EXP_LNG + " REAL, " +
            COL_EXP_SYNC + " INTEGER DEFAULT 0);";

    // --- VARIABLES DE GESTIÓN ---
    private Context context;
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public DBAdapter(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    // --- CLASE INTERNA DBHelper ---
    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_USUARIOS);
            db.execSQL(CREATE_TABLE_EXPERIENCIAS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPERIENCIAS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
            onCreate(db);
        }
    }

    // --- MÉTODOS DE APERTURA Y CIERRE ---
    public DBAdapter open() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    // ====================================================================
    //                       MÉTODOS PARA USUARIOS (Auth)
    // ====================================================================

    public long registrarUsuario(Usuario usuario) {
        ContentValues values = new ContentValues();
        values.put(COL_USER_NOMBRE, usuario.getNombre());
        values.put(COL_USER_CORREO, usuario.getCorreo());
        values.put(COL_USER_PASSWORD, usuario.getPassword());
        values.put(COL_USER_GRUPO, usuario.getGrupo());
        return db.insert(TABLE_USUARIOS, null, values);
    }

    // Retorna el Usuario completo si el login es correcto, o null si falla
    public Usuario validarLogin(String correo, String password) {
        Usuario usuario = null;
        Cursor cursor = db.query(TABLE_USUARIOS, null,
                COL_USER_CORREO + "=? AND " + COL_USER_PASSWORD + "=?",
                new String[]{correo, password}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            usuario = new Usuario();
            usuario.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)));
            usuario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_NOMBRE)));
            usuario.setCorreo(cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_CORREO)));
            usuario.setGrupo(cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_GRUPO)));
            cursor.close();
        }
        return usuario;
    }

    // ====================================================================
    //                   MÉTODOS PARA EXPERIENCIAS (Mapa/Fotos)
    // ====================================================================

    public long insertarExperiencia(Experiencia experiencia) {
        ContentValues values = new ContentValues();
        values.put(COL_EXP_USER_ID, experiencia.getIdUsuario());
        values.put(COL_EXP_TITULO, experiencia.getTitulo());
        values.put(COL_EXP_DESC, experiencia.getDescripcion());
        values.put(COL_EXP_FOTO, experiencia.getFotoPath());
        values.put(COL_EXP_LAT, experiencia.getLatitud());   // Dato CLAVE para el Mapa
        values.put(COL_EXP_LNG, experiencia.getLongitud());  // Dato CLAVE para el Mapa
        values.put(COL_EXP_SYNC, 0); // Por defecto no sincronizado

        return db.insert(TABLE_EXPERIENCIAS, null, values);
    }

    // Obtener TODAS las experiencias de UN usuario (Para el Historial y el Mapa)
    public ArrayList<Experiencia> getExperienciasPorUsuario(int idUsuario) {
        ArrayList<Experiencia> lista = new ArrayList<>();
        String selection = COL_EXP_USER_ID + "=?";
        String[] selectionArgs = {String.valueOf(idUsuario)};

        Cursor cursor = db.query(TABLE_EXPERIENCIAS, null, selection, selectionArgs, null, null, COL_EXP_ID + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Experiencia exp = new Experiencia();
                exp.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_EXP_ID)));
                exp.setIdUsuario(cursor.getInt(cursor.getColumnIndexOrThrow(COL_EXP_USER_ID)));
                exp.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow(COL_EXP_TITULO)));
                exp.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(COL_EXP_DESC)));
                exp.setFotoPath(cursor.getString(cursor.getColumnIndexOrThrow(COL_EXP_FOTO)));
                exp.setLatitud(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_EXP_LAT)));
                exp.setLongitud(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_EXP_LNG)));
                exp.setSincronizado(cursor.getInt(cursor.getColumnIndexOrThrow(COL_EXP_SYNC)));
                lista.add(exp);
            } while (cursor.moveToNext());
        }
        if (cursor != null) cursor.close();
        return lista;
    }

    // Actualizar estado de sincronización (Para la funcionalidad "Offline")
    public void cambiarEstadoSync(int idExperiencia, int nuevoEstado) {
        ContentValues values = new ContentValues();
        values.put(COL_EXP_SYNC, nuevoEstado);
        db.update(TABLE_EXPERIENCIAS, values, COL_EXP_ID + "=?", new String[]{String.valueOf(idExperiencia)});
    }

    public Usuario getUsuarioPorId(int id) {
        Usuario usuario = null;
        Cursor cursor = db.query(TABLE_USUARIOS, null,
                COL_USER_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            usuario = new Usuario();
            usuario.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)));
            usuario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_NOMBRE)));
            usuario.setCorreo(cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_CORREO)));
            usuario.setGrupo(cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_GRUPO)));
            // No obtenemos la contraseña por seguridad
            cursor.close();
        }
        return usuario;
    }

    // Actualizar usuario
    public boolean actualizarUsuario(Usuario usuario) {
        ContentValues values = new ContentValues();
        values.put(COL_USER_NOMBRE, usuario.getNombre());
        values.put(COL_USER_CORREO, usuario.getCorreo());
        values.put(COL_USER_GRUPO, usuario.getGrupo());

        // Solo actualizar contraseña si no está vacía
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            values.put(COL_USER_PASSWORD, usuario.getPassword());
        }

        String whereClause = COL_USER_ID + "=?";
        String[] whereArgs = {String.valueOf(usuario.getId())};

        int rowsAffected = db.update(TABLE_USUARIOS, values, whereClause, whereArgs);
        return rowsAffected > 0;
    }

    // Obtener usuario por email (para PerfilFragment)
    public Usuario getUsuarioPorEmail(String email) {
        Usuario usuario = null;
        Cursor cursor = db.query(TABLE_USUARIOS, null,
                COL_USER_CORREO + "=?",
                new String[]{email}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            usuario = new Usuario();
            usuario.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)));
            usuario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_NOMBRE)));
            usuario.setCorreo(cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_CORREO)));
            usuario.setGrupo(cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_GRUPO)));
            cursor.close();
        }
        return usuario;
    }


}