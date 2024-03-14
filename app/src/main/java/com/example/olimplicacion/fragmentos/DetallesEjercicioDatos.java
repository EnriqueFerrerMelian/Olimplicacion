/*
package com.example.olimplicacion.fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.olimplicacion.DetailedActivity02;
import com.example.olimplicacion.R;
import com.example.olimplicacion.databinding.DetallesEjercicioDatosBinding;
import com.example.olimplicacion.databinding.FragmentEjercicioBinding;


public class DetallesEjercicioDatos extends Fragment {
    DetallesEjercicioDatosBinding binding;
    String peso, veces;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DetallesEjercicioDatosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //declaracion de variables
        Bundle b = getArguments();

        //codigo
        addDatos(b.getString("peso"), b.getString("veces"));

        binding.modBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetallesEjercicioModificar nextFrag= new DetallesEjercicioModificar();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
    public void addDatos(String dato1, String dato2){
        binding.numPeso.setText(dato1);
        binding.numRepeticiones.setText(dato2);
    }
}*/
