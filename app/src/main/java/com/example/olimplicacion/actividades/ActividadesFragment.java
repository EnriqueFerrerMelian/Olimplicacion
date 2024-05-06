package com.example.olimplicacion.actividades;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.olimplicacion.MainActivity;
import com.example.olimplicacion.R;
import com.example.olimplicacion.clases.AppHelper;
import com.example.olimplicacion.databinding.FragmentActividadesBinding;
import com.example.olimplicacion.rutinas.Rutina;
import com.example.olimplicacion.rutinas.RutinaFbAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ActividadesFragment extends Fragment  implements ActividadFbAdapter.ItemClickListener{
    //recyclerView **********
    private ActividadFbAdapter actividadFbAdapter;//adaptador
    private RecyclerView recyclerView;
    //recyclerView *******fin

    //variables globales
    private FragmentActividadesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentActividadesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //AppHelper.hotFixAvtividad();
        //AppHelper.hotFixImagen(getContext(), R.drawable.zumba);
        FirebaseRecyclerOptions<Actividad> options =
                new FirebaseRecyclerOptions.Builder<Actividad>()
                        .setQuery(FirebaseDatabase.getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                                .getReference("actividades"), Actividad.class)
                        .build();
        actividadFbAdapter = new ActividadFbAdapter(options, this::onItemClick);
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(actividadFbAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        actividadFbAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        actividadFbAdapter.stopListening();
    }
    /**
     * Este método se pasará a rutinaFbAdapter. De manera que cuando se haga click en una rutina se ejecute el
     * fragmento DetallesRutinaFragmento
     * @param actividad
     */
    @Override
    public void onItemClick(Actividad actividad) {
        Fragment fragment = DetallesActividadFragment.newInstance(actividad);
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment, "nota").addToBackStack(null);
        fragmentTransaction.commit();
    }
}