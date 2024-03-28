/*
package com.example.olimplicacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.olimplicacion.databinding.ActivityDetailed02Binding;
//import com.example.olimplicacion.fragmentos.EjercicioFragment;

public class DetailedActivity02 extends AppCompatActivity {

    //ATRIBUTOS
    ActivityDetailed02Binding binding;
    //EjercicioFragment ejercicioFragment;
    int nombre,musculos,desc, img;
    String  peso, veces;

    //METODOS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailed02Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //declaracion de variables
        //ejercicioFragment = new EjercicioFragment();
        nombre = 0;musculos = 0;desc = 0;img = 0;
        Intent intent = this.getIntent();


        //codigo
        if(intent!=null){
            img = intent.getIntExtra("img", R.drawable.apertura_con_mancuernas);
            peso = intent.getStringExtra("peso");
            veces = intent.getStringExtra("veces");

            binding.detailName.setText(nombre);
            binding.detailDesc.setText(desc);
            binding.detailMusculos.setText(musculos);
            binding.detailImage.setImageResource(img);
        }
        //DetallesEjercicioDatos detallesEjercicioDatos = new DetallesEjercicioDatos();
        //reemplazarFragmento(detallesEjercicioDatos, peso, veces);



        //listeners
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailedActivity02.this, MenuPrincipal.class);
                startActivity(intent);
            }
        });
    }
    private void reemplazarFragmento(Fragment fragmento, String peso, String veces){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragmento);
        Bundle b = new Bundle();
        b.putString("peso", peso);
        b.putString("veces", veces);
        fragmento.setArguments(b);
        fragmentTransaction.commit();
    }
}*/
