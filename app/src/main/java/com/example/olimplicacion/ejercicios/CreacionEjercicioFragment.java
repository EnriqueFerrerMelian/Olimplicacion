package com.example.olimplicacion.ejercicios;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.olimplicacion.MenuPrincipal;
import com.example.olimplicacion.R;
import com.example.olimplicacion.clases.Ejercicio;
import com.example.olimplicacion.databinding.FragmentCreacionEjercicioBinding;
import com.example.olimplicacion.rutinas.CreacionRutinaFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreacionEjercicioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreacionEjercicioFragment extends Fragment {
    private static Ejercicio ejercicio;
    static FragmentCreacionEjercicioBinding binding;

    public CreacionEjercicioFragment() { }
    public static CreacionEjercicioFragment newInstance(Ejercicio ejercicioF) {
        CreacionEjercicioFragment fragment = new CreacionEjercicioFragment();
        Bundle args = new Bundle();
        ejercicio = ejercicioF;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MenuPrincipal) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreacionEjercicioBinding.inflate(inflater, container, false);
        //codigo
        //inicialización de numberPicker**************************
        binding.numberPeso.setMinValue(1);binding.numberPeso.setMaxValue(100);
        binding.numberPesoDecimal.setMinValue(0);binding.numberPesoDecimal.setMaxValue(5);
        binding.numberRepeticiones.setMinValue(1);binding.numberRepeticiones.setMaxValue(30);
        binding.numberVeces.setMinValue(1);binding.numberVeces.setMaxValue(100);
        //inicialización de numberPicker**********************fin*
        binding.categoria.setText(ejercicio.getCategoria());
        binding.detailName.setText(ejercicio.getNombre());
        binding.detailMusculos.setText(ejercicio.getMusculos());
        binding.detailDesc.setText(ejercicio.getDescripcion());
        Glide.with(getContext())
                .load(ejercicio.getImg())
                .placeholder(R.drawable.baseline_add_242)//si no hay imagen carga una por defecto
                .error(R.drawable.baseline_add_242)//si ocurre algún error se verá por defecto
                .into(binding.detailImage);

        binding.agnadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String peso = binding.numberPeso.getValue() +"." + binding.numberPesoDecimal.getValue();
                String repeticionesYseries = binding.numberRepeticiones.getValue() +" x "+ binding.numberVeces.getValue();
                ejercicio.setPeso(peso);ejercicio.setRepecitionesYseries(repeticionesYseries);
                CreacionRutinaFragment.addToDataList(ejercicio, getContext());
                getParentFragmentManager().popBackStack();
            }
        });
        //codigo fin
        return binding.getRoot();
    }
}