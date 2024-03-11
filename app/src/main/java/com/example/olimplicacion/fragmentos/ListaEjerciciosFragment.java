package com.example.olimplicacion.fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.olimplicacion.clases.Ejercicio;
import com.example.olimplicacion.clases.EjercicioAdapter;
import com.example.olimplicacion.databinding.FragmentListaEjerciciosBinding;

import java.util.ArrayList;

public class ListaEjerciciosFragment extends Fragment {
    //recyclerView
    private RecyclerView recyclerView;//lista del xml
    private EjercicioAdapter ejercicioAdapter;//adaptador
    static ArrayList<Ejercicio> dataArrayList = new ArrayList<>();
    //recyclerView fin
    static FragmentListaEjerciciosBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListaEjerciciosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataArrayList = new ArrayList<>();
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ejercicioAdapter = new EjercicioAdapter(dataArrayList);
        recyclerView.setAdapter(ejercicioAdapter);
        dataArrayList.add(new Ejercicio((int)12, "Ejercicio prueba", "Musculos prueba", "Descripcion" , null));

        binding.volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

    }
}