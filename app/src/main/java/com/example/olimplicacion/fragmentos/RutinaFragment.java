package com.example.olimplicacion.fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.olimplicacion.R;
import com.example.olimplicacion.clases.Rutina;
import com.example.olimplicacion.clases.RutinaFbAdapter;
import com.example.olimplicacion.databinding.FragmentRutinaBinding;
import com.example.olimplicacion.fragmentosDetalle.DetallesRutinaFragment;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

/**
 * CONTENDRÁ UN RECYCLERVIEW DE RECYCLERVIEWS DE EJERCICIOS
 * AL CREAR UNA RUTINA SE EJECUTA EL FRAGMENTO 'CREACIONRUTINAFRAGMENT'
 * AL SELECCIONAR UNA RUTINA SE EJECUTA 'DETALLESRUTINAFRAGMENT
 * AL SELECCIONAR 'MODIFICAR' EN 'DETALLESRUTINAFRAGMENT' SE EJECUTA DE NUEVO 'CREACIONRUTINAFRAGMENT'
 */

public class RutinaFragment extends Fragment implements RutinaFbAdapter.ItemClickListener {
    //recyclerView
    private RutinaFbAdapter rutinaFbAdapter;
    private RecyclerView recyclerView;

    static FragmentRutinaBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRutinaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseRecyclerOptions<Rutina> options =
                new FirebaseRecyclerOptions.Builder<Rutina>()
                        .setQuery(FirebaseDatabase
                                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                                .getReference()
                                .child("rutinas"), Rutina.class)
                        .build();

        rutinaFbAdapter = new RutinaFbAdapter(options, this::onItemClick);
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(rutinaFbAdapter);

        //Abre el fragmento donde se creará una rutina nueva.
        binding.anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println("Subiendo prueba");
                //subirPrueba();
                reemplazarFragmento(new CreacionRutinaFragment());
            }
        });
    }

    /**
     * Reemplaza el fragmento en el contenedor 'fragmentContainerView' por el pasado por
     * parámetro
     * @param fragmento
     */
    public void reemplazarFragmento(Fragment fragmento){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragmento).addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        rutinaFbAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        rutinaFbAdapter.stopListening();
    }

    @Override
    public void onItemClick(Rutina rutina) {
        Fragment fragment = DetallesRutinaFragment.newInstance(rutina);
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment, "nota").addToBackStack(null);
        fragmentTransaction.commit();
    }

/*    public void cargarPrueba(){
        DatabaseReference ref = FirebaseDatabase.getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("rutinas");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {//dataSnapshot son todos los usuarios
                for (DataSnapshot rut: dataSnapshot.getChildren()) {//
                    Rutina rutina = new Rutina();
                    rutina.setId(rut.child("id").getValue().toString());
                    rutina.setNombre(rut.child("nombre").getValue().toString());
                    if(rut.child("img").getValue()!=null){
                        //rutina.setImg(Uri.parse(rut.child("img").getValue().toString()));
                    }
                    if(MainActivity.getUsuario().getRutinas().contains(rutina.getId())){
                        dataArrayList.add(rutina);
                    }
                }
                rutinaAdapter = new RutinaAdapter(dataArrayList, RutinaFragment.this);
                recyclerView.setAdapter(rutinaAdapter);
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }*/

/*    *//**
     * Este método obtiene las rutinas guardadas en la base de datos de Firebase y se comparan con las rutinas del usuario.
     * Las rutinas que coincidan serán cargadas en el RecyclerView.
     *//*
    public void cargarRutina(){
        DatabaseReference ref = FirebaseDatabase.getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("rutinas");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {//dataSnapshot son todos los usuarios
                for (DataSnapshot rut: dataSnapshot.getChildren()) {//
                    Rutina rutina = new Rutina();
                    rutina.setId(rut.child("id").getValue().toString());
                    rutina.setNombre(rut.child("nombre").getValue().toString());
                    if(rut.child("img").getValue()!=null){
                        //rutina.setImg(Uri.parse(rut.child("img").getValue().toString()));
                    }
                    if(MainActivity.getUsuario().getRutinas().contains(rutina.getId())){
                        dataArrayList.add(rutina);
                    }
                }
                rutinaAdapter = new RutinaAdapter(dataArrayList, RutinaFragment.this);
                recyclerView.setAdapter(rutinaAdapter);
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }*/

  /*  public void subirPrueba(){
        Map<String , Object> rutina = new HashMap<>();
        rutina.put("nombre", "nombre Rutina");
        rutina.put("id", "22");
        rutina.put("img", "no img");
        List<String> dias = new ArrayList<>();
        dias.add("l");dias.add("m");
        rutina.put("dias", dias);
        List<Ejercicio> ejercicios = new ArrayList<>();

        rutina.put("ejercicios", ejercicios);

        FirebaseDatabase
                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("rutinas")
                .push()
                .setValue(rutina);
    }*/
}