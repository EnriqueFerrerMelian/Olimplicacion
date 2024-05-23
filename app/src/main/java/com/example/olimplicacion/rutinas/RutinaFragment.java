package com.example.olimplicacion.rutinas;

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

import com.example.olimplicacion.MainActivity;
import com.example.olimplicacion.MenuPrincipal;
import com.example.olimplicacion.R;
import com.example.olimplicacion.clases.AppHelper;
import com.example.olimplicacion.clases.Rutina;
import com.example.olimplicacion.databinding.FragmentRutinaBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

/**
 * CONTENDRÁ UN RECYCLERVIEW DE RECYCLERVIEWS DE EJERCICIOS
 * AL CREAR UNA RUTINA SE EJECUTA EL FRAGMENTO 'CREACIONRUTINAFRAGMENT'
 * AL SELECCIONAR UNA RUTINA SE EJECUTA 'DETALLESRUTINAFRAGMENT
 * AL SELECCIONAR 'MODIFICAR' EN 'DETALLESRUTINAFRAGMENT' SE EJECUTA DE NUEVO 'CREACIONRUTINAFRAGMENT'
 */

public class RutinaFragment extends Fragment implements RutinaFbAdapter.ItemClickListener {
    private static FragmentRutinaBinding binding;
    //recyclerView **********
    private RutinaFbAdapter rutinaFbAdapter;
    private RecyclerView recyclerView;
    //recyclerView *******fin


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentRutinaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MenuPrincipal) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        AppHelper.cambiarToolbarText("Rutinas");
        FirebaseRecyclerOptions<Rutina> options =
                new FirebaseRecyclerOptions.Builder<Rutina>()
                        .setQuery(FirebaseDatabase.getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                                .getReference("usuarios/"+ MainActivity.getUsuarioOB().getId()+"/rutinas"), Rutina.class)
                        .build();
        rutinaFbAdapter = new RutinaFbAdapter(options, this::onItemClick);
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(rutinaFbAdapter);

        //Abre el fragmento donde se creará una rutina nueva.
        binding.anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    /**
     * Este método se pasará a rutinaFbAdapter. De manera que cuando se haga click en una rutina se ejecute el
     * fragmento DetallesRutinaFragmento
     * @param rutina
     */
    @Override
    public void onItemClick(Rutina rutina) {
        Fragment fragment = DetallesRutinaFragment.newInstance(rutina);
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment, "nota").addToBackStack(null);
        fragmentTransaction.commit();
    }


}