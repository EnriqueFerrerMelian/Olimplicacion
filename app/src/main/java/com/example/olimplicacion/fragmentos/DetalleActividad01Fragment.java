package com.example.olimplicacion.fragmentos;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.olimplicacion.R;
import com.example.olimplicacion.databinding.ActivityDetailed01Binding;
import com.example.olimplicacion.databinding.FragmentDetalleActividad01Binding;
import com.example.olimplicacion.databinding.FragmentListaEjerciciosBinding;

/**
 * Muestra los detalles del ejercicio seleccionado
 * desde ListaEjerciciosFragment
 */
public class DetalleActividad01Fragment extends Fragment {
    //ATRIBUTOS
    private static final String NOMBRE = "  nombre";
    private static final String IMAGEN = "imagen";
    private static final String MUSCULOS = "musculos";
    private static final String DESCRIPCION = "descripcion";
    String nombre;
    Bitmap imagen;
    String musculos;
    String descripcion;
    static FragmentDetalleActividad01Binding binding;

    public static DetalleActividad01Fragment newInstance(String nombre) {
        DetalleActividad01Fragment fragment = new DetalleActividad01Fragment();
        Bundle args = new Bundle();
        args.putString(NOMBRE, nombre);
        args.putString(MUSCULOS, nombre);
        args.putString(DESCRIPCION, nombre);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            nombre = getArguments().getString(NOMBRE);
            musculos = getArguments().getString(MUSCULOS);
            descripcion = getArguments().getString(DESCRIPCION);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetalleActividad01Binding.inflate(inflater, container, false);

        binding.detailName.setText(nombre);
        binding.detailMusculos.setText(musculos);
        binding.detailDesc.setText(descripcion);
        binding.volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });
        return binding.getRoot();
    }



}