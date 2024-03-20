package com.example.olimplicacion.fragmentosDetalle;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.olimplicacion.R;
import com.example.olimplicacion.clases.Ejercicio;

import java.util.List;

public class DetallesRutinaDatos extends Fragment {
    private static final String NOMBRE = "  nombre";
    private static final String IMG = "imagen";


    private String nombre;
    private String img;
    private RecyclerView recyclerView;
    private List<Ejercicio> dataArrayList;

    public DetallesRutinaDatos() {
        // Required empty public constructor
    }
    public static DetallesRutinaDatos newInstance(String nombre) {
        DetallesRutinaDatos fragment = new DetallesRutinaDatos();
        Bundle args = new Bundle();
        args.putString(NOMBRE, nombre);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            nombre = getArguments().getString(NOMBRE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detalles_rutina_datos, container, false);
    }
}