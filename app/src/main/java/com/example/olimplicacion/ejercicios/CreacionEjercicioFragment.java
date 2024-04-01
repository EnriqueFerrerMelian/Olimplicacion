package com.example.olimplicacion.ejercicios;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.bumptech.glide.Glide;
import com.example.olimplicacion.R;
import com.example.olimplicacion.databinding.FragmentCreacionEjercicioBinding;
import com.example.olimplicacion.rutinas.CreacionRutinaFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreacionEjercicioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreacionEjercicioFragment extends Fragment {

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
    static FragmentCreacionEjercicioBinding binding;

    public CreacionEjercicioFragment() { }
    public static CreacionEjercicioFragment newInstance(Ejercicio ejercicio) {
        CreacionEjercicioFragment fragment = new CreacionEjercicioFragment();
        Bundle args = new Bundle();
        args.putString(NOMBRE, ejercicio.getNombre());
        args.putString(MUSCULOS, ejercicio.getMusculos());
        args.putString(DESCRIPCION, ejercicio.getDescripcion());
        args.putString(IMAGEN, String.valueOf(ejercicio.getImg()));
        args.putInt(ID, ejercicio.getId());
        args.putString(CATEGORIA, ejercicio.getCategoria());

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            nombre = getArguments().getString(NOMBRE);
            categoria = getArguments().getString(CATEGORIA);
            musculos = getArguments().getString(MUSCULOS);
            descripcion = getArguments().getString(DESCRIPCION);
            imagen = getArguments().getString(IMAGEN);
            id = getArguments().getInt(ID);
        }
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
        binding.categoria.setText(categoria);
        binding.detailName.setText(nombre);
        binding.detailMusculos.setText(musculos);
        binding.detailDesc.setText(descripcion);
        Glide.with(getContext())
                .load(Uri.parse(imagen))
                .placeholder(R.drawable.baseline_add_242)//si no hay imagen carga una por defecto
                .error(R.drawable.baseline_add_242)//si ocurre algún error se verá por defecto
                .into(binding.detailImage);

        binding.agnadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String peso = binding.numberPeso.getValue() + binding.numberPesoDecimal.getValue()+"";
                String repeticionesYseries = binding.numberRepeticiones.getValue() +" x "+ binding.numberVeces.getValue();
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

    public void showBottonSheet(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.my_bottom_sheet_ejercicio);
        LinearLayout galeriaLayout = dialog.findViewById(R.id.galeriaLayout);
        LinearLayout camaraLayout = dialog.findViewById(R.id.camaraLayout);
        Button aceptar = dialog.findViewById(R.id.aceptar);
        //inicialización de numberPicker**************************
        NumberPicker numberPeso = dialog.findViewById(R.id.numberPeso);
        numberPeso.setMinValue(1);numberPeso.setMaxValue(100);
        NumberPicker numberPesoDecimal = dialog.findViewById(R.id.numberPesoDecimal);
        numberPesoDecimal.setMinValue(0);numberPesoDecimal.setMaxValue(5);
        NumberPicker repeticiones = dialog.findViewById(R.id.repeticiones);
        repeticiones.setMinValue(1);repeticiones.setMaxValue(30);
        NumberPicker series = dialog.findViewById(R.id.series);
        series.setMinValue(1);series.setMaxValue(100);
        //inicialización de numberPicker**********************fin*
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Metodo de aceptar aquí");
                //metodoprueba
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}