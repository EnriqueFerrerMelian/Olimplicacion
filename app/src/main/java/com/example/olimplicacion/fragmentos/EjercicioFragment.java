package com.example.olimplicacion.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.olimplicacion.clases.Ejercicio;
import com.example.olimplicacion.clases.EjercicioAdapter;
import com.example.olimplicacion.databinding.FragmentEjercicioBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * este fragmento debería cargar desde la base de datos la lista de ejercicios seleccionada
 * desde el fragmento RutinaFragment
 */
public class EjercicioFragment extends Fragment  implements EjercicioAdapter.ViewHolder.ItemClickListener{
    //recyclerView
    private RecyclerView recyclerView;//lista del xml
    private EjercicioAdapter ejercicioAdapter;//adaptador
    static ArrayList<Ejercicio> dataArrayList = new ArrayList<>();
    //recyclerView fin
    static FragmentEjercicioBinding binding;
    static ArrayList<String> pesoLista = new ArrayList<>();
    static ArrayList<String> vecesLista = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEjercicioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //declaracion de variables
        dataArrayList = new ArrayList<>();
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ejercicioAdapter = new EjercicioAdapter(dataArrayList, this);
        recyclerView.setAdapter(ejercicioAdapter);
        dataArrayList.add(new Ejercicio(1, "nombre1", "Musculos1", "Desc1", "Cat1", null));

        /**
         * abre el fragmento EjercicioListaFragment para seleccionar un ejercicio a añadir
         */
        binding.anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    /**
     * añade un ejercicio a la lista
     * @param ejercicio
     * @param peso
     * @param veces
     */
    public static void addToList(Ejercicio ejercicio, String peso, String veces){
        dataArrayList.add(ejercicio);
        pesoLista.add(peso);
        vecesLista.add(veces);

        //se añade a la base de datos
        //dbm.getEjerciciosDAO().addEjrecicio(ejercicio);
    }

    /**
     * comprueba si el ejercicio ya está en la lista
     * @param ejercicio
     * @return
     */
    public static boolean tieneEjercicio(Ejercicio ejercicio) {
        for (int i = 0; i < dataArrayList.size(); i++) {
            if(dataArrayList.get(i).getNombre()==ejercicio.getNombre()){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onItemClick(Ejercicio ejercicio) {

    }
}