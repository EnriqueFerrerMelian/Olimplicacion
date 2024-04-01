package com.example.olimplicacion.ejercicios;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.olimplicacion.R;
import com.example.olimplicacion.databinding.FragmentDetalleEjercicioBinding;

/**
 * Muestra los detalles del ejercicio seleccionado
 * desde 'ListaEjerciciosFragment' o 'DetallesRutinaFragment'
 */
public class DetalleEjercicioFragment extends Fragment {
    private static Ejercicio ejercicio;
    private static FragmentDetalleEjercicioBinding binding;

    public static DetalleEjercicioFragment newInstance(Ejercicio ejercicioF) {
        DetalleEjercicioFragment fragment = new DetalleEjercicioFragment();
        ejercicio = ejercicioF;
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetalleEjercicioBinding.inflate(inflater, container, false);
        //codigo
        binding.categoria.setText(ejercicio.getCategoria());
        binding.detailName.setText(ejercicio.getNombre());
        binding.detailMusculos.setText(ejercicio.getMusculos());
        binding.detailDesc.setText(ejercicio.getDescripcion());
        Glide.with(getContext())
                .load(Uri.parse(ejercicio.getImg()))
                .placeholder(R.drawable.baseline_add_242)//si no hay imagen carga una por defecto
                .error(R.drawable.baseline_add_242)//si ocurre algún error se verá por defecto
                .into(binding.detailImage);
        binding.numPeso.setText(ejercicio.getPeso());
        binding.numRepeticiones.setText(ejercicio.getRepecitionesYseries());
        binding.volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });
        //codigo fin
        return binding.getRoot();
    }
}