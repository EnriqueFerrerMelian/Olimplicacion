package com.example.olimplicacion.actividades;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.olimplicacion.R;
import com.example.olimplicacion.clases.AppHelper;
import com.example.olimplicacion.databinding.FragmentDetallesActividadBinding;
import com.example.olimplicacion.ejercicios.EjercicioFbAdapter;

/**
 * Este fragmento mostrará los detalles de la actividad seleccionada en el fragmento ActividadesFragment.
 */
public class DetallesActividadFragment extends Fragment{
    private static Actividad actividad;
    private FragmentDetallesActividadBinding binding;

    public DetallesActividadFragment() {
        // Required empty public constructor
    }
    public static DetallesActividadFragment newInstance(Actividad actividadF) {
        DetallesActividadFragment fragment = new DetallesActividadFragment();
        actividad = actividadF;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetallesActividadBinding.inflate(inflater, container, false);
        AppHelper.cargarActividad(binding, getContext(), actividad);

        binding.volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });
        return binding.getRoot();
    }
}