package com.example.olimplicacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.olimplicacion.clases.Usuario;
import com.example.olimplicacion.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static Usuario usuario;
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
                    obtenesUsuario(binding.nombre.getText().toString(), binding.clave.getText().toString());
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

    /**
     * Obtiene un usuario de la base de datos a partir de nombre y clave pasados por parámetro.
     * @param nombre
     * @param clave
     */
    public void obtenesUsuario(String nombre, String clave){
        DatabaseReference ref = FirebaseDatabase.getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("usuarios");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {//dataSnapshot son todos los usuarios
                List<String> rutinas = new ArrayList<>();
                for (DataSnapshot user: dataSnapshot.getChildren()) {//
                    if(user.child("nombre").getValue().equals(nombre) && user.child("clave").getValue().equals(clave)){
                        usuario.setId(user.child("id").getValue().toString());
                        usuario.setNombre(user.child("nombre").getValue().toString());
                        usuario.setClave(user.child("clave").getValue().toString());
                        for (DataSnapshot rutina:user.child("rutinas").getChildren()) {
                            rutinas.add(rutina.getValue().toString());
                        }
                        usuario.setRutinas(rutinas);
                        System.out.println(usuario);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public static Usuario getUsuario(){
        return usuario;
    }
}