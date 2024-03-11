package com.example.olimplicacion.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.olimplicacion.R;

/**
 * Mostrará un calendario con la imagen de la rutina que toca ese día
 * debajo por defecto una breve descripción de la rutina más cercana
 */
public class CalendarioFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendario, container, false);
    }
}