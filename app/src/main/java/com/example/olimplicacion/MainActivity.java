package com.example.olimplicacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.olimplicacion.clases.Peso;
import com.example.olimplicacion.clases.Usuario;
import com.example.olimplicacion.databinding.ActivityMainBinding;
import com.example.olimplicacion.rutinas.Rutina;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

/**
 * NOTA: Al registrarse en el gimnasio, el administrador dará una clave generada de forma aleatoria
 * con la que el usuario podrá entrar en la aplicación. En el apartado 'Configuración de perfil' se podrá cambiar.
 * Esto significa que no hace falta un fragmento de 'Registro'.
 */

public class MainActivity extends AppCompatActivity {
    private static Usuario usuario = new Usuario();
    private static Peso peso = new Peso();
    ActivityMainBinding binding;

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
                    verificarUsuario(binding.nombre.getText().toString(), binding.clave.getText().toString());
                }else{
                    //si no se rellena algún campo saltará una notificación
                    if(binding.nombre.getText().length()<1){
                        binding.nombre.setError("No puede estar vacío.");
                    }else{
                        binding.clave.setError("No puede estar vacío.");
                    }
                }
            }
        });
    }

    /**
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
    public void verificarUsuario(String nombre, String clave){
        DatabaseReference ref = FirebaseDatabase.getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("usuarios");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {//dataSnapshot son todos los usuarios
                boolean confirmado = false;
                for (DataSnapshot data: dataSnapshot.getChildren()) {//
                    if(data.child("nombre").getValue().equals(nombre) && data.child("clave").getValue().equals(clave)){
                        //recojo los datos del usuario en un objeto Usuario
                        usuario = data.getValue(Usuario.class);
                        peso = data.child("peso").getValue(Peso.class);
                        confirmado=true;
                        //ejecuto el fragmento 'MenuPrincipal'
                        irAMenuPrincipal();

                        //pongo a null los campos de texto
                        binding.nombre.setText("");
                        binding.clave.setText("");
                    }
                }
                if(!confirmado){
                    escribirToast("No estás registrado");
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
    /**
     * Inicia un intent al activity MenuPrincipal.
     */
    public void irAMenuPrincipal(){
        Intent intent = new Intent(this, MenuPrincipal.class);
        startActivity(intent);
    }

    /**
     * Este método se usará para obtener datos del usuario actual a lo largo de la aplicación
     * @return
     */
    public static Usuario getUsuario(){
        return usuario;
    }
    public static Peso getPeso(){
        return peso;
    }

    public static void setUsuario(Usuario usuarioFb){
        usuario = usuarioFb;
    }
    public static void setPeso(Peso pesoFb){
        peso = pesoFb;
    }
    public void escribirToast(String texto){
        Toast.makeText(MainActivity.this, texto, Toast.LENGTH_LONG).show();
    }
}