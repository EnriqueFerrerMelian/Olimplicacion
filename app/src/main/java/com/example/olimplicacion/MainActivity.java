package com.example.olimplicacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.olimplicacion.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;

    ActivityMainBinding binding;
    Button entrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //listeners
        binding.boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.nombre.getText().toString().equals("admin") && binding.clave.getText().toString().equals("admin")){
                    openActivity2();
                    binding.nombre.setText("");
                    binding.clave.setText("");
                }
            }
        });
    }


    public void openActivity2(){
        Intent intent = new Intent(this, MenuPrincipal.class);
        startActivity(intent);
    }
}