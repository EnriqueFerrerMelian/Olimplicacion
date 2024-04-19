package com.example.olimplicacion.ejercicios;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.olimplicacion.R;
import com.example.olimplicacion.ejercicios.CreacionEjercicioFragment;
import com.example.olimplicacion.ejercicios.Ejercicio;
import com.example.olimplicacion.ejercicios.EjercicioFbAdapter;
import com.example.olimplicacion.databinding.FragmentListaEjerciciosBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class ListaEjerciciosFragment extends Fragment implements EjercicioFbAdapter.ItemClickListener {
    private EjercicioFbAdapter ejercicioFbAdapter;
    private RecyclerView recyclerView;
    static FragmentListaEjerciciosBinding binding;
    private static String valorSpinner = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListaEjerciciosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //cargo la lista
        cargarRecycler();
        instanciarSpinner();

        binding.categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    cargarRecycler();
                }
                if(i==1){
                    valorSpinner = "mancuernas";
                    cargarDesdeSpiner(valorSpinner);
                }
                if(i==2){
                    valorSpinner = "kettlebells";
                    cargarDesdeSpiner(valorSpinner);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });
    }

    /**
     * Este método configura el spinner
     */
    public void instanciarSpinner(){
        List<String> listaCategorias = new ArrayList<>();
        listaCategorias.add("todos");
        listaCategorias.add("mancuernas");
        listaCategorias.add("kettlebells");
        ArrayAdapter<String> spinnerAdapter= new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listaCategorias);
        spinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        binding.categoria.setAdapter(spinnerAdapter);
    }

    /**
     * Este método carga el recyclerView con todos los ejercicios de la base de datos
     */
    public void cargarRecycler(){
        FirebaseRecyclerOptions<Ejercicio> options =
                new FirebaseRecyclerOptions.Builder<Ejercicio>()
                        .setQuery(FirebaseDatabase
                                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                                .getReference()
                                .child("ejercicios"), Ejercicio.class)
                        .build();

        ejercicioFbAdapter = new EjercicioFbAdapter(options, this::onItemClick);
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(ejercicioFbAdapter);
        ejercicioFbAdapter.startListening();
    }

    /**
     * Este método carga los ejercicios en el recyclerView según la categoría
     * @param valor
     */
    public void cargarDesdeSpiner(String valor){
        System.out.println("Generando opciones 2");
        FirebaseRecyclerOptions<Ejercicio> options =
                new FirebaseRecyclerOptions.Builder<Ejercicio>()
                        .setQuery(FirebaseDatabase
                                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                                .getReference()
                                .child("ejercicios").orderByChild("categoria").equalTo(valor), Ejercicio.class)
                        .build();
        System.out.println("Instanciando adaptador 2 ");
        ejercicioFbAdapter = new EjercicioFbAdapter(options, this::onItemClick);
        System.out.println("Insertando adaptador adaptador 2 ");
        recyclerView.setAdapter(ejercicioFbAdapter);
        ejercicioFbAdapter.startListening();
    }
    @Override
    public void onItemClick(Ejercicio ejercicio) {
        Fragment fragment = CreacionEjercicioFragment.newInstance(ejercicio);
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment, "nota").addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        ejercicioFbAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        ejercicioFbAdapter.stopListening();
    }
}