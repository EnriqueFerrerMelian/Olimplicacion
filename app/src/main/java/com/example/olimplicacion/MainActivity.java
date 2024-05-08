package com.example.olimplicacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.olimplicacion.actividades.Actividad;
import com.example.olimplicacion.clases.AppHelper;
import com.example.olimplicacion.clases.Avance;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * NOTA: Al registrarse en el gimnasio, el administrador dará una clave generada de forma aleatoria
 * con la que el usuario podrá entrar en la aplicación. En el apartado 'Configuración de perfil' se podrá cambiar.
 * Esto significa que no hace falta un fragmento de 'Registro'.
 */

public class MainActivity extends AppCompatActivity {
    private static Usuario usuario = new Usuario();
    private static Peso peso = new Peso();
    private static Avance avance = new Avance();
    private static List<Actividad> actividades = new ArrayList<>();


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
                        if(data.child("peso").getValue(Peso.class)==null){
                            peso = data.child("peso").getValue(Peso.class);
                        }

                        if(data.child("avance").getValue(Avance.class)!=null){
                            avance = data.child("avance").getValue(Avance.class);
                        }
                        if(data.child("actividades").getValue()!=null){
                            for (DataSnapshot data2: data.child("actividades").getChildren()) {
                                Actividad actividad = data2.getValue(Actividad.class);
                                System.out.println(actividad.getFecha());
                                actividades.add(actividad);
                            }
                        }
                        confirmado=true;
                        //ejecuto el fragmento 'MenuPrincipal'
                        irAMenuPrincipal();

                        //pongo a null los campos de texto
                        binding.nombre.setText("");
                        binding.clave.setText("");
                    }
                }
                if(!confirmado){
                    AppHelper.escribirToast("No estás registrado", MainActivity.this);
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
    public static Avance getAvance() {return avance;}

    public static void setUsuario(Usuario usuarioOB){
        usuario = usuarioOB;
    }
    public static void setPeso(Peso pesoOB){
        peso = pesoOB;
    }
    public static void setAvance(Avance avanceOB){
        avance = avanceOB;
    }

    public static List<Actividad> getActividades() {
        return actividades;
    }

    public static void setActividades(List<Actividad> actividades) {
        MainActivity.actividades = actividades;
        System.out.println("Actualizando" + MainActivity.actividades);
    }

    private static void validarFechaActividad(){
        //Obtengo la fecha de hoy
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        String fecha = date.getYear() +"/"+ date.getMonth()+"/"+ date.getDate()+"";
        String[] fechas1 = fecha.split("/");
        int a1 = Integer.valueOf(fechas1[0]),b1 = Integer.valueOf(fechas1[1]),c1 = Integer.valueOf(fechas1[2]);
        for (int i = 0; i < actividades.size(); i++) {
            String[] fechas2 = actividades.get(i).getFecha().split("/");
            int a2 = Integer.valueOf(fechas2[0]),b2 = Integer.valueOf(fechas2[1]),c2 = Integer.valueOf(fechas2[2]);
            if(a1>a2 || (a1==a2 && b1>b2) || (a1==a2 && b1==b2 && (c1-c2>4))){
                AppHelper.eliminarReserva(actividades.remove(i));
            }
        }
    }
}