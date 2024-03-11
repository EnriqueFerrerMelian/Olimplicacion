package com.example.olimplicacion.fragmentos;

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

import com.example.olimplicacion.MenuPrincipal;
import com.example.olimplicacion.R;
import com.example.olimplicacion.clases.Ejercicio;
import com.example.olimplicacion.clases.EjercicioAdapter;
import com.example.olimplicacion.databinding.FragmentEjercicioBinding;
import com.example.olimplicacion.databinding.FragmentRutinaBinding;

import java.util.ArrayList;

/**
 * ESTE DEBERIA SER EL FRAGMENTO AL QUE SE ACCEDA AL INICIAR SESIÓN
 * CONTENDRÁ UN RECYCLERVIEW DE RECYCLERVIEWS DE EJERCICIOS
 */

public class RutinaFragment extends Fragment {
    MenuPrincipal menuPrincipal;
    //recyclerView
    private RecyclerView recyclerView;//lista del xml
    private EjercicioAdapter ejercicioAdapter;//adaptador
    static ArrayList<Ejercicio> dataArrayList = new ArrayList<>();
    //recyclerView fin

    static FragmentRutinaBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRutinaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        menuPrincipal = new MenuPrincipal();
        dataArrayList = new ArrayList<>();
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ejercicioAdapter = new EjercicioAdapter(dataArrayList);
        recyclerView.setAdapter(ejercicioAdapter);
        dataArrayList.add(new Ejercicio((int)12, "Rutina prueba", "Musculos prueba", "Descripcion" , null));
        binding.anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reemplazarFragmento(new CreacionRutinaFragment());
            }
        });
    }
    public void reemplazarFragmento(Fragment fragmento){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragmento).addToBackStack(null);
        fragmentTransaction.commit();
    }

}