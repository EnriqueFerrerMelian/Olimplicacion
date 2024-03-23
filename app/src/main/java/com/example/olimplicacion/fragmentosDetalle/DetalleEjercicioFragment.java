package com.example.olimplicacion.fragmentosDetalle;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.olimplicacion.R;
import com.example.olimplicacion.clases.Ejercicio;
import com.example.olimplicacion.databinding.FragmentDetalleEjercicioBinding;
import com.example.olimplicacion.fragmentos.CreacionRutinaFragment;

/**
 * Muestra los detalles del ejercicio seleccionado
 * desde ListaEjerciciosFragment
 */
public class DetalleEjercicioFragment extends Fragment {
    //ATRIBUTOS
    private static final String NOMBRE = "nombre";
    private static final String ID = "id";
    private static final String IMAGEN = "imagen";
    private static final String MUSCULOS = "musculos";
    private static final String DESCRIPCION = "descripcion";
    private static final String CATEGORIA = "categoria";
    String nombre;
    String imagen;
    String musculos;
    String descripcion;
    String categoria;
    int id;
    static FragmentDetalleEjercicioBinding binding;

    public static DetalleEjercicioFragment newInstance(Ejercicio ejercicio) {
        DetalleEjercicioFragment fragment = new DetalleEjercicioFragment();
        Bundle args = new Bundle();
        args.putString(NOMBRE, ejercicio.getNombre());
        args.putString(MUSCULOS, ejercicio.getMusculos());
        args.putString(DESCRIPCION, ejercicio.getDescripcion());
        args.putString(IMAGEN, ejercicio.getImg());
        args.putInt(ID, ejercicio.getId());
        args.putString(CATEGORIA, ejercicio.getCategoria());

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
            imagen = getArguments().getString(IMAGEN);
            id = getArguments().getInt(ID);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetalleEjercicioBinding.inflate(inflater, container, false);
        //codigo
        //inicialización de numberPicker**************************
        binding.numberPeso.setMinValue(1);binding.numberPeso.setMaxValue(100);
        binding.numberPesoDecimal.setMinValue(0);binding.numberPesoDecimal.setMaxValue(5);
        binding.numberRepeticiones.setMinValue(1);binding.numberRepeticiones.setMaxValue(30);
        binding.numberVeces.setMinValue(1);binding.numberVeces.setMaxValue(100);
        //inicialización de numberPicker**********************fin*

        binding.detailName.setText(nombre);
        binding.detailMusculos.setText(musculos);
        binding.detailDesc.setText(descripcion);
        Glide.with(getContext())
                .load(Uri.parse(imagen))
                .placeholder(R.drawable.baseline_add_24)//si no hay imagen carga una por defecto
                .circleCrop()
                .error(R.drawable.baseline_add_24)//si ocurre algún error se verá por defecto
                .into(binding.detailImage);

        binding.agnadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String peso = binding.numberPeso.getValue() + binding.numberPesoDecimal.getValue()+"";
                String repeticionesYseries = binding.numberRepeticiones.getValue() +";"+ binding.numberVeces.getValue();
                Ejercicio ejercicio = new Ejercicio(id, nombre, musculos, descripcion, categoria, imagen);
                ejercicio.setPeso(peso);ejercicio.setRepecitionesYseries(repeticionesYseries);
                CreacionRutinaFragment.addToDataList(ejercicio, getContext());
                getParentFragmentManager().popBackStack();
            }
        });
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