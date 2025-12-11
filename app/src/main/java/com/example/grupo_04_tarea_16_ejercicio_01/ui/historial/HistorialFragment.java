package com.example.grupo_04_tarea_16_ejercicio_01.ui.historial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_04_tarea_16_ejercicio_01.adapter.ExperienciaAdapter;
import com.example.grupo_04_tarea_16_ejercicio_01.R;
import com.example.grupo_04_tarea_16_ejercicio_01.model.Experiencia;

import java.util.ArrayList;
import java.util.List;

public class HistorialFragment extends Fragment {

    private RecyclerView recyclerView;
    private ExperienciaAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_historial, container, false);

        recyclerView = root.findViewById(R.id.recyclerViewHistorial);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Experiencia> listaFalsa = new ArrayList<>();
        listaFalsa.add(new Experiencia("Plaza de Armas", "Muy bonito", "10/12/2025", true));
        listaFalsa.add(new Experiencia("Mirador Grau", "Vista increíble", "11/12/2025", false));
        listaFalsa.add(new Experiencia("Iglesia Huaura", "Antigua", "11/12/2025", true));

        adapter = new ExperienciaAdapter(listaFalsa);
        recyclerView.setAdapter(adapter);

        return root;
    }
}