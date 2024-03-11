package com.example.olimplicacion.fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.olimplicacion.DetailedActivity01;
import com.example.olimplicacion.clases.Ejercicio;
import com.example.olimplicacion.R;
import com.example.olimplicacion.databinding.FragmentEjercicioListaBinding;

import java.util.ArrayList;

public class EjercicioListaFragment extends Fragment {
    FragmentEjercicioListaBinding binding;
    ArrayList<Ejercicio> dataArrayList = new ArrayList<>();
    Ejercicio ejercicio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("07- creando binding en lista de ejercicios");
        binding = FragmentEjercicioListaBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("08- creando listas con datos");



        System.out.println("09- cargando la listView");
        //rellenamos el ListView
        /*listAdapter = new ListAdapter(getContext(), dataArrayList);
        binding.listView.setAdapter(listAdapter);
        binding.listView.setClickable(true);
        //al seleccionar un objeto, mandamos los datos de ese objeto al activity detalles para que los muestre.
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), DetailedActivity01.class);

                intent.putExtra("name", nameList[i]);
                intent.putExtra("musculos", musculosList[i]);
                intent.putExtra("desc", descList[i]);
                intent.putExtra("img", imgList[i]);

                startActivity(intent);
            }
        });*/
    }

}