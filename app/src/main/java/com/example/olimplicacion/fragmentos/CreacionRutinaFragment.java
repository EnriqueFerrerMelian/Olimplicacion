package com.example.olimplicacion.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.olimplicacion.R;
import com.example.olimplicacion.clases.Ejercicio;
import com.example.olimplicacion.clases.EjercicioAdapter;
import com.example.olimplicacion.databinding.FragmentCreacionRutinaBinding;

import java.util.ArrayList;

/**
 * Desde aquí se manejará la creación de rutinas
 */
public class CreacionRutinaFragment extends Fragment  implements EjercicioAdapter.ViewHolder.ItemClickListener{

    //recyclerView
    private RecyclerView recyclerView;//lista del xml
    private EjercicioAdapter ejercicioAdapter;//adaptador
    static ArrayList<Ejercicio> dataArrayList = new ArrayList<>();
    //recyclerView fin
    static FragmentCreacionRutinaBinding binding;

    static Context context;//para usar toast en métodos státicos
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreacionRutinaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataArrayList = new ArrayList<>();
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ejercicioAdapter = new EjercicioAdapter(dataArrayList, this);
        recyclerView.setAdapter(ejercicioAdapter);
        dataArrayList.add(new Ejercicio((int)12, "Ejercicio prueba", "Musculos prueba", "Descripcion" , null));

        /**
         * abre el fragmento EjercicioListaFragment para seleccionar un ejercicio a añadir
         */
        binding.anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reemplazarFragmento(new ListaEjerciciosFragment());
            }
        });
        binding.aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });
    }

    /**
     * añade un ejercicio a la lista
     * @param ejercicio
     */
    public static void addToList(Ejercicio ejercicio){
        dataArrayList.add(ejercicio);
        Toast.makeText(context, "Ejercicio añadido.", Toast.LENGTH_LONG);
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
            if(dataArrayList.get(i).getName()==ejercicio.getName()){
                return true;
            }
        }
        return false;
    }

    public void reemplazarFragmento(Fragment fragmento){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragmento).addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onItemClick(Ejercicio ejercicio) {

    }
}