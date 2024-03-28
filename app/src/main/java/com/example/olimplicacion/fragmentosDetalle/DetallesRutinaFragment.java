package com.example.olimplicacion.fragmentosDetalle;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.olimplicacion.R;
import com.example.olimplicacion.clases.Ejercicio;
import com.example.olimplicacion.clases.EjercicioAdapter;
import com.example.olimplicacion.clases.EjercicioFbAdapter;
import com.example.olimplicacion.clases.Rutina;
import com.example.olimplicacion.databinding.FragmentDetallesRutinaBinding;
import com.example.olimplicacion.fragmentos.CreacionRutinaFragment;

import java.util.List;

/**
 * Este fragmento mostrará los detalles de la rutina seleccionara en el fragmento RutinaFragment.
 * Se debe incluir un fragmento similar pero en el que se puedan añadir ejercicios, con un boton añadir ejercicio.
 * al seleccionar un ejercicio en este fragmento, debe abrirse un fragmento de ejercicio NUEVO, en el que no se
 * puedan cambiar los detalles, pero con un botón modificar. Es decir, crear un nuevo fragmento de Ejercicio para modificar
 */

public class DetallesRutinaFragment extends Fragment implements EjercicioFbAdapter.ItemClickListener {
    private static Rutina rutina;
    FragmentDetallesRutinaBinding binding;
    private static RecyclerView recyclerView;
    private EjercicioAdapter ejercicioAdapter;
    private static List<Ejercicio> dataArrayList;


    public DetallesRutinaFragment() {
        // Required empty public constructor
    }
    public static DetallesRutinaFragment newInstance(Rutina rutinaF) {
        DetallesRutinaFragment fragment = new DetallesRutinaFragment();
        rutina = rutinaF;
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetallesRutinaBinding.inflate(inflater, container, false);
        //codigo
        //inserto los datos en el fragmento *************
        //cargo el recyclerview *******************
        dataArrayList = rutina.getEjercicios();
        ejercicioAdapter = new EjercicioAdapter(dataArrayList, this::onItemClick);
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(ejercicioAdapter);
        //cargo el recyclerview ****************fin

        Glide.with(getContext())
                .load(rutina.getImg())
                .placeholder(R.drawable.baseline_add_24)//si no hay imagen carga una por defecto
                .circleCrop()
                .error(R.drawable.baseline_add_24)//si ocurre algún error se verá por defecto
                .into(binding.imagen);
        binding.nombreDeRutina.setText(rutina.getNombre());
        if(rutina.getDias().contains("l")){
            binding.lunes.setTextColor(Color.rgb(255, 127, 39));
        }if(rutina.getDias().contains("m")){
            binding.martes.setTextColor(Color.rgb(255, 127, 39));
        }if(rutina.getDias().contains("x")){
            binding.miercoles.setTextColor(Color.rgb(255, 127, 39));
        }if(rutina.getDias().contains("j")){
            binding.jueves.setTextColor(Color.rgb(255, 127, 39));
        }if(rutina.getDias().contains("v")){
            binding.viernes.setTextColor(Color.rgb(255, 127, 39));
        }if(rutina.getDias().contains("s")){
            binding.sabado.setTextColor(Color.rgb(255, 127, 39));
        }if(rutina.getDias().contains("d")){
            binding.domingo.setTextColor(Color.rgb(255, 127, 39));
        }
        //inserto los datos en el fragmento **********fin

        binding.modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificarRutina(rutina);
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

    @Override
    public void onItemClick(Ejercicio ejercicio) {
        Fragment fragment = DetalleEjercicioFragment.newInstance(ejercicio);
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment, "nota").addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void modificarRutina(Rutina rutina){
        Fragment fragment = CreacionRutinaFragment.newInstance(rutina);
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment, "nota").addToBackStack(null);
        fragmentTransaction.commit();
    }
}