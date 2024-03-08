package com.example.olimplicacion.fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.olimplicacion.DetailedActivity02;
import com.example.olimplicacion.adaptadores.ListAdapter;
import com.example.olimplicacion.clases.Ejercicio;
import com.example.olimplicacion.ListaEjerciciosActivity;
import com.example.olimplicacion.databinding.FragmentEjercicioBinding;
import com.example.olimplicacion.db.DataBaseManager;

import java.util.ArrayList;

public class EjercicioFragment extends Fragment {
    static FragmentEjercicioBinding binding;
    static ListAdapter listAdapter;
    static ArrayList<Ejercicio> dataArrayList = new ArrayList<>();
    static ArrayList<String> pesoLista = new ArrayList<>();
    static ArrayList<String> vecesLista = new ArrayList<>();
    DataBaseManager dbm;
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
        dbm = new DataBaseManager(getContext());
        listAdapter = new ListAdapter(getContext(), dataArrayList);
        binding.listView.setAdapter(listAdapter);

        //listeners
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), DetailedActivity02.class);

                intent.putExtra("name", dataArrayList.get(i).getName());
                intent.putExtra("musculos", dataArrayList.get(i).getMusculos());
                intent.putExtra("desc", dataArrayList.get(i).getDesc());
                intent.putExtra("img", dataArrayList.get(i).getImage());
                intent.putExtra("peso", pesoLista.get(i));
                intent.putExtra("veces", vecesLista.get(i));

                startActivity(intent);
            }
        });
        binding.anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ListaEjerciciosActivity.class);
                startActivity(intent);
            }
        });
    }

    public static void addToList(Ejercicio ejercicio, String peso, String veces){
        dataArrayList.add(ejercicio);
        pesoLista.add(peso);
        vecesLista.add(veces);
        Toast.makeText(listAdapter.getContext(), "Ejercicio añadido.", Toast.LENGTH_LONG);
        //se añade a la base de datos
        //dbm.getEjerciciosDAO().addEjrecicio(ejercicio);
    }

    public static boolean tieneEjercicio(Ejercicio ejercicio) {
        for (int i = 0; i < dataArrayList.size(); i++) {
            if(dataArrayList.get(i).getName()==ejercicio.getName()){
                return true;
            }
        }
        return false;
    }
}