package com.example.grupo_04_tarea_16_ejercicio_01.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.grupo_04_tarea_16_ejercicio_01.Modelo.Experiencia; // Tu Modelo

import java.util.ArrayList;

public class ExperienciaAdapter extends ArrayAdapter<Experiencia> {
    private Context context;
    private ArrayList<Experiencia> experiencias;

    public ExperienciaAdapter(Context context, ArrayList<Experiencia> experiencias) {
        super(context, 0, experiencias);
        this.context = context;
        this.experiencias = experiencias;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. Inflado de la vista (Igual que en AutobusAdapter)
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        // 2. Obtener el objeto actual
        Experiencia experiencia = experiencias.get(position);

        // 3. Referenciar los TextViews del layout por defecto de Android
        TextView text1 = convertView.findViewById(android.R.id.text1);
        TextView text2 = convertView.findViewById(android.R.id.text2);

        // 4. Lógica Visual (TEXT 1: Título Principal)
        text1.setText(experiencia.getTitulo());

        // 5. Lógica Visual (TEXT 2: Detalles y Estado)
        // Usamos emojis para indicar el estado, similar a tu ejemplo de autobus
        String estadoInfo = "";

        // Verificamos si ya se subió a la nube (Sincronizado = 1)
        if (experiencia.getSincronizado() == 1) {
            estadoInfo += "✅ Subido ";  // Ya está en el servidor
            text1.setTextColor(Color.BLACK); // Color normal
        } else {
            estadoInfo += "☁️ Pendiente "; // Falta internet/subir
            text1.setTextColor(Color.RED); // Resaltar título en rojo si falta subir
        }

        // Verificamos si tiene foto guardada (Si la ruta no está vacía)
        if (experiencia.getFotoPath() != null && !experiencia.getFotoPath().isEmpty()) {
            estadoInfo += "| 📸 Foto ";
        }

        // Mostramos Coordenadas o Descripción
        String coordenadas = "📍 " + experiencia.getLatitud() + ", " + experiencia.getLongitud();

        // Juntamos todo en la segunda línea
        text2.setText(estadoInfo + "\n" + experiencia.getDescripcion());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}