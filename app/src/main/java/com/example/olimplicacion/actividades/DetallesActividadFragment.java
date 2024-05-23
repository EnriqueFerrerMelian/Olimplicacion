package com.example.olimplicacion.actividades;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.olimplicacion.MainActivity;
import com.example.olimplicacion.MenuPrincipal;
import com.example.olimplicacion.clases.Actividad;
import com.example.olimplicacion.clases.AppHelper;
import com.example.olimplicacion.databinding.FragmentDetallesActividadBinding;

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
        AppHelper.cambiarToolbarText(actividad.getNombre());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetallesActividadBinding.inflate(inflater, container, false);
        ((MenuPrincipal) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppHelper.cargarActividad(binding, getContext(), actividad);
        //si la actividad no está reservada
        boolean esta = false;
        for (int i = 0; i < MainActivity.getActividadesOBs().size(); i++) {
            if(MainActivity.getActividadesOBs().get(i).getNombre().equals(actividad.getNombre())){
                esta = true;
            }
        }
        if (!esta) {
            //se activa el onclick listener
            binding.reservar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Integer.valueOf(actividad.getVacantes())>1) {
                        AppHelper.reservarActividad(actividad);
                        getParentFragmentManager().popBackStack();
                    }else{
                        System.out.println("No quedan vacantes para esa actividad");
                        AppHelper.escribirToast("No quedan vacantes para esa actividad", getContext());
                    }
                }
            });
            binding.eliminarReserva.setText("");
            binding.eliminarReserva.setTextColor(Color.BLACK);
            binding.eliminarReserva.setBackgroundColor(Color.TRANSPARENT);
        } else {
            binding.eliminarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                    AppHelper.eliminarReserva(actividad);
                    getParentFragmentManager().popBackStack();
                }
            });
            //si no el botoón cambia de aspecto
            binding.reservar.setText("Ya reservado");
            binding.reservar.setTextColor(Color.BLACK);
            binding.reservar.setBackgroundColor(Color.TRANSPARENT);
        }
        return binding.getRoot();
    }
}