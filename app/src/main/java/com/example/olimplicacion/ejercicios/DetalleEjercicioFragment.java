package com.example.olimplicacion.ejercicios;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;

import com.bumptech.glide.Glide;
import com.example.olimplicacion.MainActivity;
import com.example.olimplicacion.R;
import com.example.olimplicacion.clases.Avance;
import com.example.olimplicacion.clases.Usuario;
import com.example.olimplicacion.databinding.FragmentDetalleEjercicioBinding;
import com.example.olimplicacion.rutinas.DetallesRutinaFragment;
import com.example.olimplicacion.rutinas.Rutina;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Muestra los detalles del ejercicio seleccionado
 * desde 'ListaEjerciciosFragment' o 'DetallesRutinaFragment'
 */
public class DetalleEjercicioFragment extends Fragment {
    private static Ejercicio ejercicio;
    private static FragmentDetalleEjercicioBinding binding;

    public static DetalleEjercicioFragment newInstance(Ejercicio ejercicioF) {
        DetalleEjercicioFragment fragment = new DetalleEjercicioFragment();
        ejercicio = ejercicioF;
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetalleEjercicioBinding.inflate(inflater, container, false);
        System.out.println("Ejercicio id: "+ ejercicio.getId());
        //codigo
        binding.categoria.setText(ejercicio.getCategoria());
        binding.detailName.setText(ejercicio.getNombre());
        binding.detailMusculos.setText(ejercicio.getMusculos());
        binding.detailDesc.setText(ejercicio.getDescripcion());
        Glide.with(getContext())
                .load(Uri.parse(ejercicio.getImg()))
                .placeholder(R.drawable.baseline_add_242)//si no hay imagen carga una por defecto
                .error(R.drawable.baseline_add_242)//si ocurre algún error se verá por defecto
                .into(binding.detailImage);
        binding.numPeso.setText(ejercicio.getPeso());
        binding.numRepeticiones.setText(ejercicio.getRepecitionesYseries());

        binding.modBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottonSheet();
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

    public void showBottonSheet(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.my_bottom_sheet_ejercicio);
        Button aceptar = dialog.findViewById(R.id.aceptar);
        //inicialización de numberPicker**************************
        NumberPicker numberPeso = dialog.findViewById(R.id.numberPeso);
        numberPeso.setMinValue(1);numberPeso.setMaxValue(100);
        String[] setPeso = binding.numPeso.getText().toString().split("\\.");
        numberPeso.setValue(Integer.valueOf(setPeso[0]));
        NumberPicker numberPesoDecimal = dialog.findViewById(R.id.numberPesoDecimal);
        numberPesoDecimal.setMinValue(0);numberPesoDecimal.setMaxValue(9);
        numberPesoDecimal.setValue(Integer.valueOf(setPeso[1]));
        String setRepSer1 = binding.numRepeticiones.getText().toString().replace(" ","");
        String [] setRepSer2= setRepSer1.split("x");
        NumberPicker repeticiones = dialog.findViewById(R.id.repeticiones);
        repeticiones.setMinValue(1);repeticiones.setMaxValue(30);
        repeticiones.setValue(Integer.valueOf(setRepSer2[0]));
        NumberPicker series = dialog.findViewById(R.id.series);
        series.setMinValue(1);series.setMaxValue(100);
        series.setValue(Integer.valueOf(setRepSer2[1]));
        //inicialización de numberPicker**********************fin*
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.numPeso.setText(numberPeso.getValue() +"."+ numberPesoDecimal.getValue());
                binding.numRepeticiones.setText(repeticiones.getValue() + " x " + series.getValue());
                updateEjercicio(DetallesRutinaFragment.getRutina());
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    /**
     * Actualiza el ejercicio en la base de datos. Obtiene la posición del ejercicio en la lista de
     * ejercicios de la rutina seleccionada. Guarda en el ejercicio seleccionado el nuevo peso y
     * repeticiones. Con el index seleccionamos el ejercicio a actualizar en la base de datos
     * añadiendolo a la referencia.
     * @param rutina seleccionada.
     */
    public void updateEjercicio(Rutina rutina) {
        System.out.println("updateEjercicio()");
        int index = 0;
        for (int i = 0; i < rutina.getEjercicios().size(); i++) {
            if(rutina.getEjercicios().get(i).getId()== ejercicio.getId()){
                index = i;
            }
        }
        ejercicio.setPeso(binding.numPeso.getText().toString());
        ejercicio.setRepecitionesYseries(binding.numRepeticiones.getText().toString());
        System.out.println(ejercicio);
        DatabaseReference ref = FirebaseDatabase
                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("usuarios/"+MainActivity.getUsuario().getId()+"/rutinas/"+
                        rutina.getNombre()+"/ejercicios/"+index);
        ref.setValue(ejercicio).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                actualizarAvance();
            }
        });
    }
    /**
     * Compara el nombre del ejercicio actualizado con la lista de nombres de ejercicios del objeto 'avance'.
     * Si encuentra una coincidencia, elimina esa coincidencia del objeto avance, y guarda en nue
     */
    public static void actualizarAvance(){
        System.out.println("actualizarAvance()");
        int index = 0;
        boolean bandera = false;
        if(MainActivity.getAvance().getEjerciciosNombres().size()>0) {
            for (int i = 0; i < MainActivity.getAvance().getEjerciciosNombres().size(); i++) {
                if (MainActivity.getAvance().getEjerciciosNombres().get(i).equals(ejercicio.getNombre())) {
                    index = i;
                    bandera = true;
                }
            }
            if(bandera){
                MainActivity.getAvance().getPesos().set(index, ejercicio.getPeso());
            }else{
                MainActivity.getAvance().getEjerciciosNombres().add(ejercicio.getNombre());
                MainActivity.getAvance().getPesos().add(ejercicio.getPeso());
            }
        }else{
            MainActivity.getAvance().getEjerciciosNombres().add(ejercicio.getNombre());
            MainActivity.getAvance().getPesos().add(ejercicio.getPeso());
        }
        DatabaseReference ref = FirebaseDatabase
                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("usuarios/" + MainActivity.getUsuario().getId() + "/avance");
        ref.setValue(MainActivity.getAvance()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                actualizarUsuario();
            }
        });
    }
    public static void actualizarUsuario(){
        System.out.println("Actualizando usuario");
        DatabaseReference ref = FirebaseDatabase
                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("usuarios/"+MainActivity.getUsuario().getId());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {//dataSnapshot son todos los usuarios
                MainActivity.setUsuario(dataSnapshot.getValue(Usuario.class));
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}