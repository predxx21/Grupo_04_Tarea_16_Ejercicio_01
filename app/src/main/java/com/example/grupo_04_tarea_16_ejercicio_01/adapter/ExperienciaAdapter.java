package com.example.grupo_04_tarea_16_ejercicio_01.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_04_tarea_16_ejercicio_01.R;
import com.example.grupo_04_tarea_16_ejercicio_01.model.Experiencia;

import java.util.List;

public class ExperienciaAdapter extends RecyclerView.Adapter<ExperienciaAdapter.ViewHolder> {

    private List<Experiencia> listaExperiencias;

    public ExperienciaAdapter(List<Experiencia> listaExperiencias) {
        this.listaExperiencias = listaExperiencias;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_experiencia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Experiencia item = listaExperiencias.get(position);

        holder.txtTitulo.setText(item.getTitulo());
        holder.txtFecha.setText(item.getFecha());

        if (item.isSincronizado()) {
            holder.imgSync.setImageResource(android.R.drawable.checkbox_on_background);
            holder.imgSync.setColorFilter(Color.parseColor("#43A047"));
        } else {
            holder.imgSync.setImageResource(android.R.drawable.stat_notify_sync);
            holder.imgSync.setColorFilter(Color.parseColor("#E53935"));
        }

    }

    @Override
    public int getItemCount() {
        return listaExperiencias.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtFecha;
        ImageView imgFoto, imgSync;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTituloItem);
            txtFecha = itemView.findViewById(R.id.txtFechaItem);
            imgFoto = itemView.findViewById(R.id.imgFotoMini);
            imgSync = itemView.findViewById(R.id.imgSyncStatus);
        }
    }
}
