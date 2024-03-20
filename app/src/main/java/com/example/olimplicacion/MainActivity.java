package com.example.olimplicacion;

import static java.util.Arrays.asList;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.olimplicacion.clases.Rutina;
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
    private static Usuario usuario = new Usuario();
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
                if(binding.nombre.getText().length()>0 && binding.clave.getText().length()>0){
                    obtenesUsuario(binding.nombre.getText().toString(), binding.clave.getText().toString());
                }
            }
        });
    }

    /**
     * Inicia un intent al activity MenuPrincipal.
     */
    public void openActivity2(){
        Intent intent = new Intent(this, MenuPrincipal.class);
        startActivity(intent);
    }

    /**
     * NOTA: INCLUIR LISTA DE EJERCICIOS EN RUTINA DE DATABASE
     * Obtiene un usuario de la base de datos a partir de nombre y clave pasados por parámetro.
     * Crea una referencia a FirebaseDatabase que conectará con la RealTime Databade de Firebase.
     * Se apllicará un listener a esa referencia, que ejecutará el siguiente código cuando se acceda a ella:
     * Se crea una lista de rutinas.
     * Se crea un bucle for each, donde se comprobará por cada objeto de la base de datos (usuarios) si contienen
     * el nombre y clave pasados por parámetros. Si concuerda, se pasarán los datos a un objeto Usuario que se usará más adelante.
     * Se obtienen sus rutinas en la lista creada anteriormente.
     * Se ejecuta la siguiente actividad (MenúPrincipal) y se ponen las áreas de texto a null.
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
                        for (DataSnapshot rutine: user.child("rutinas").getChildren()) {
                            rutinas.add(rutine.getValue().toString());
                        }
                        usuario.setRutinas(rutinas);
                        openActivity2();
                        binding.nombre.setText("");
                        binding.clave.setText("");
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