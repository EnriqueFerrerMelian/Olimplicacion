package com.example.olimplicacion.fragmentos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.olimplicacion.R;
import com.example.olimplicacion.baseDeDatos.FirebaseHelper;
import com.example.olimplicacion.clases.Ejercicio;
import com.example.olimplicacion.clases.EjercicioAdapter;
import com.example.olimplicacion.clases.EjercicioFbAdapter;
import com.example.olimplicacion.databinding.FragmentListaEjerciciosBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaEjerciciosFragment extends Fragment implements EjercicioAdapter.ViewHolder.ItemClickListener {
    //recyclerView
    private EjercicioFbAdapter ejercicioFbAdapter;
    private RecyclerView recyclerView;//lista del xml
    private EjercicioAdapter ejercicioAdapter;//adaptador
    static ArrayList<Ejercicio> dataArrayList;
    private int numId;
    //recyclerView fin
    private FirebaseHelper fbHelper;
    static FragmentListaEjerciciosBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListaEjerciciosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseRecyclerOptions<Ejercicio> options =
                new FirebaseRecyclerOptions.Builder<Ejercicio>()
                        .setQuery(FirebaseDatabase
                                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                                .getReference()
                                .child("ejercicios"), Ejercicio.class)
                        .build();
        ejercicioFbAdapter = new EjercicioFbAdapter(options, this);

        //fbHelper = new FirebaseHelper("ejercicios");
        dataArrayList = new ArrayList<>();
        numId = dataArrayList.size();

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ejercicioAdapter = new EjercicioAdapter(dataArrayList, this);

        recyclerView.setAdapter(ejercicioFbAdapter);
        //cargarDesdeFb();
        //cargarDesdeFb2();
        //para añadir el ejercicio solo tengo que hacer un método en el fragmento anterior y ejecutarlo en un listener
        binding.volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onItemClick(Ejercicio ejercicio) {
        Fragment fragment = DetalleActividad01Fragment.newInstance(ejercicio.getNombre(), ejercicio.getMusculos(), ejercicio.getDescripcion());
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment, "nota").addToBackStack(null);//si no funciona cambiar fragment por getParentFragment()
        fragmentTransaction.commit();
    }


    public void cargarImagen() throws IOException {
        fbHelper.setStorageReference("apertura_con_mancuernas.png");
        File localFile = File.createTempFile("tempFile", ".png");
        fbHelper.getStorageReference().getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    }
                });
    }


    public void cargarDesdeFb(){
        Ejercicio ejercicio = new Ejercicio();


        fbHelper.getCollectionReference().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    System.out.println("Cargando ejercicios");
                    System.out.println("Imagen: " + ejercicio.getImg());
                    ejercicio.setId(Integer.parseInt(documentSnapshot.get("id").toString()));
                    ejercicio.setNombre(documentSnapshot.get("nombre").toString());
                    ejercicio.setCategoria(documentSnapshot.get("categoria").toString());
                    ejercicio.setDescripcion(documentSnapshot.get("descripcion").toString());
                    ejercicio.setMusculos(documentSnapshot.get("nombre").toString());
                    System.out.println("insertando ejercicio en lista");
                    dataArrayList.add(ejercicio);
                    System.out.println(ejercicio.getNombre() +" añadido");
                }
                ejercicioAdapter = new EjercicioAdapter(dataArrayList, ListaEjerciciosFragment.this);
                recyclerView.setAdapter(ejercicioAdapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Algo ha salido mal: " + e);
            }
        });
        System.out.println("saliendo de método cargarDesdeFb");
    }

    @Override
    public void onStart() {
        super.onStart();
        ejercicioFbAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        ejercicioFbAdapter.stopListening();
    }
}