package com.example.olimplicacion.fragmentos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.olimplicacion.DetailedActivity02;
import com.example.olimplicacion.MenuPrincipal;
import com.example.olimplicacion.R;
import com.example.olimplicacion.databinding.DetallesEjercicioModificarBinding;


public class DetallesEjercicioModificar extends Fragment {
    DetallesEjercicioModificarBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DetallesEjercicioModificarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //declaracion de variables
        binding.numberPeso.setMinValue(1);binding.numberPeso.setMaxValue(100);
        binding.numberPesoDecimal.setMinValue(0);binding.numberPesoDecimal.setMaxValue(5);
        binding.numberRepeticiones.setMinValue(1);binding.numberRepeticiones.setMaxValue(30);
        binding.numberVeces.setMinValue(1);binding.numberVeces.setMaxValue(100);


        //Listeners
        binding.acepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //recoger los datos de los numpick y mandarlos al fragmento de datos
                String peso = binding.numberPeso.getValue() + "." +binding.numberPesoDecimal.getValue() + " Kg";
                System.out.println("Peso: "+ peso);
                String veces = binding.numberRepeticiones.getValue() + " X " + binding.numberVeces.getValue();
                System.out.println("veces: "+ veces);
                Bundle b = new Bundle();
                b.putString("peso", peso);
                b.putString("veces", veces);
                //DetallesEjercicioDatos nextFrag= new DetallesEjercicioDatos();
               // nextFrag.setArguments(b);
                /*getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();*/
            }
        });
        binding.canBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MenuPrincipal.class);
                startActivity(intent);
            }
        });
    }
}