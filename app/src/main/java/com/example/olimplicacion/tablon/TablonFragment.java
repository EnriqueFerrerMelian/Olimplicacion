package com.example.olimplicacion.tablon;

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

import com.example.olimplicacion.MainActivity;
import com.example.olimplicacion.R;
import com.example.olimplicacion.clases.AppHelper;
import com.example.olimplicacion.clases.Noticia;
import com.example.olimplicacion.clases.Rutina;
import com.example.olimplicacion.databinding.FragmentTablonBinding;
import com.example.olimplicacion.rutinas.DetallesRutinaFragment;
import com.example.olimplicacion.rutinas.RutinaFbAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TablonFragment extends Fragment  implements NoticiaFbAdapter.ItemClickListener {
    private static FragmentTablonBinding binding;
    //recyclerView **********
    private NoticiaFbAdapter noticiaFbAdapter;
    private RecyclerView recyclerView;
    //recyclerView *******fin
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTablonBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppHelper.cambiarToolbarText("Tablón de anuncios");
        //Obtengo la fecha de hoy
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        FirebaseRecyclerOptions<Noticia> options =
                new FirebaseRecyclerOptions.Builder<Noticia>()
                        .setQuery(FirebaseDatabase.getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                                .getReference("noticias"), Noticia.class)
                        .build();
        noticiaFbAdapter = new NoticiaFbAdapter(options, this::onItemClick);
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(noticiaFbAdapter);
    }
    @Override
    public void onItemClick(Noticia noticia) {
        Fragment fragment = DetalleNoticiaFragment.newInstance(noticia);
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment, "nota").addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Estos métodos llaman a la base de datos de Firebase al iniciar y cerrar el fragmento.
     */
    @Override
    public void onStart() {
        super.onStart();
        noticiaFbAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        noticiaFbAdapter.stopListening();
    }
}