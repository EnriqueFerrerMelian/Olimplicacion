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

import com.example.olimplicacion.MainActivity;
import com.example.olimplicacion.actividades.Actividad;
import com.example.olimplicacion.actividades.ActividadAdapter;
import com.example.olimplicacion.clases.AppHelper;
import com.example.olimplicacion.databinding.FragmentCalendarioBinding;
import com.example.olimplicacion.rutinas.Rutina;
import com.example.olimplicacion.rutinas.RutinaAdapter;
import com.example.olimplicacion.rutinas.RutinaFbAdapter;

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
    private static RutinaAdapter rutinaAdapter;
    private static ActividadAdapter actividadAdapter;
    private static List<Rutina> rutinas = new ArrayList<>();
    private static List<Actividad> actividades = new ArrayList<>();
    private static RecyclerView recyclerViewRutina;
    private static RecyclerView recyclerViewActividad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCalendarioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        customCalendar = binding.customCalendar;
        AppHelper.cargarCalendario(customCalendar, getContext(), this);
        //recycler para rutinas
        recyclerViewRutina = binding.recyclerViewRutina;
        rutinaAdapter = new RutinaAdapter(rutinas);
        recyclerViewRutina.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewRutina.setAdapter(rutinaAdapter);
        //recycler para actividades
        recyclerViewActividad = binding.recyclerViewActividad;
        actividadAdapter = new ActividadAdapter(actividades);
        recyclerViewActividad.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewActividad.setAdapter(actividadAdapter);

    }


    @Override
    public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
        AppHelper.updateArr(newMonth);
        Map<Integer, Object>[] arr = AppHelper.getArr();
        return arr;
    }
    public static List<Rutina> getRutinaSelected(){
        return rutinas;
    }
    public static void setRecyclerView(List<Rutina> rutinaList, List<Actividad> actividadList){
        rutinaAdapter = new RutinaAdapter(rutinaList);
        recyclerViewRutina.setAdapter(rutinaAdapter);
        actividadAdapter = new ActividadAdapter(actividadList);
        recyclerViewActividad.setAdapter(actividadAdapter);
    }
}