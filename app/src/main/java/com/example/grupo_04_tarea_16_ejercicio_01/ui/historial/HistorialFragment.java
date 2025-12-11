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
        Experiencia exp1 = new Experiencia();
        exp1.setTitulo("Plaza de Armas");
        exp1.setDescripcion("Muy bonito centro histórico");
        exp1.setSincronizado(1);
        exp1.setFotoPath("");
        listaFalsa.add(exp1);

        Experiencia exp2 = new Experiencia();
        exp2.setTitulo("Mirador Grau");
        exp2.setDescripcion("Vista increíble al mar");
        exp2.setSincronizado(0);
        exp2.setFotoPath("");
        listaFalsa.add(exp2);

        Experiencia exp3 = new Experiencia();
        exp3.setTitulo("Iglesia Huaura");
        exp3.setDescripcion("Balcón histórico");
        exp3.setSincronizado(1);
        exp3.setFotoPath("");
        listaFalsa.add(exp3);
        adapter = new ExperienciaAdapter(listaFalsa);
        recyclerView.setAdapter(adapter);

        return root;
    }
}