/*
package com.example.olimplicacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.olimplicacion.clases.Ejercicio;
import com.example.olimplicacion.databinding.ActivityDetailed01Binding;
//import com.example.olimplicacion.fragmentos.EjercicioFragment;

public class DetailedActivity01 extends AppCompatActivity {

    //ATRIBUTOS
    ActivityDetailed01Binding binding;
    //EjercicioFragment ejercicioFragment;
    int nombre;
    int musculos;
    int desc;
    int img;

    //METODOS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailed01Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //declaracion de variables
       // ejercicioFragment = new EjercicioFragment();
        nombre = 0;musculos = 0;desc = 0;img = 0;
        Intent intent = this.getIntent();

        //codigo
        if(intent!=null){
            img = intent.getIntExtra("img", R.drawable.apertura_con_mancuernas);

            binding.detailName.setText(nombre);
            binding.detailDesc.setText(desc);
            binding.detailMusculos.setText(musculos);
            binding.detailImage.setImageResource(img);
        }

        binding.numberPeso.setMinValue(1);binding.numberPeso.setMaxValue(100);
        binding.numberPesoDecimal.setMinValue(0);binding.numberPesoDecimal.setMaxValue(5);
        binding.numberRepeticiones.setMinValue(1);binding.numberRepeticiones.setMaxValue(30);
        binding.numberVeces.setMinValue(1);binding.numberVeces.setMaxValue(100);


        //listeners
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                */
/*if(nombre>0){
                    String peso = binding.numberPeso.getValue() + "." +binding.numberPesoDecimal.getValue() + " Kg";
                    System.out.println("Peso: "+ peso);

                    String veces = binding.numberRepeticiones.getValue() + " X " + binding.numberVeces.getValue();
                    System.out.println("veces: "+ veces);
                    Ejercicio ejercicio = new Ejercicio();
                    ejercicio.setName(nombre);ejercicio.setDesc(desc);
                    ejercicio.setMusculos(musculos);ejercicio.setImage(img);
                    if(!EjercicioFragment.tieneEjercicio(ejercicio)){
                        EjercicioFragment.addToList(ejercicio, peso, veces);
                        Toast.makeText(DetailedActivity01.this, "Ejercicio añadido.",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(DetailedActivity01.this, ListaEjerciciosActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(DetailedActivity01.this, "Ya estába en la lista de ejercicios.",Toast.LENGTH_LONG).show();
                    }
                }*//*

            }
        });

    }
}*/
