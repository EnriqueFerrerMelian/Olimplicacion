package com.example.olimplicacion.calendario;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.olimplicacion.clases.AppHelper;
import com.example.olimplicacion.clases.Evento;
import com.example.olimplicacion.databinding.FragmentCalendarioBinding;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnNavigationButtonClickedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


/**
 * Mostrará un calendario con la imagen de la rutina que toca ese día
 * debajo por defecto una breve descripción de la rutina más cercana
 */
public class CalendarioFragment extends Fragment implements OnNavigationButtonClickedListener {
    private FragmentCalendarioBinding binding;
    private CustomCalendar customCalendar;
    private static ObjetoAdapter eventoAdapter;
    private static List<Evento> eventos = new ArrayList<>();
    private static RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCalendarioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        customCalendar = binding.customCalendar;
        AppHelper.cargarCalendario(customCalendar, getContext(), this);
        //recycler
        recyclerView = binding.recyclerViewRutina;
        eventoAdapter = new ObjetoAdapter(eventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(eventoAdapter);
        //despliega en la lista los eventos del día actual, si los hay
        Calendar calendar = Calendar.getInstance();
        AppHelper.todayIsTheDay(calendar);
    }


    @Override
    public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
        AppHelper.updateArr(newMonth);
        Map<Integer, Object>[] arr = AppHelper.getArr();
        return arr;
    }
    public static void setRecyclerView(List<Evento> eventoList){
        eventoAdapter = new ObjetoAdapter(eventoList);
        recyclerView.setAdapter(eventoAdapter);
    }
}