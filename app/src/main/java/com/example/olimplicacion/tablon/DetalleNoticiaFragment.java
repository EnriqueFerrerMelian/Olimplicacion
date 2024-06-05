package com.example.olimplicacion.tablon;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.olimplicacion.MenuPrincipal;
import com.example.olimplicacion.clases.AppHelper;
import com.example.olimplicacion.clases.Noticia;
import com.example.olimplicacion.databinding.FragmentDetalleNoticiaBinding;

public class DetalleNoticiaFragment extends Fragment {
    FragmentDetalleNoticiaBinding binding;
    private static Noticia noticia;
    public DetalleNoticiaFragment() {
        // Required empty public constructor
    }
    public static DetalleNoticiaFragment newInstance(Noticia noticiaF) {
        DetalleNoticiaFragment fragment = new DetalleNoticiaFragment();
        noticia = noticiaF;
        AppHelper.cambiarToolbarText(noticia.getTitulo());
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetalleNoticiaBinding.inflate(inflater, container, false);
        ((MenuPrincipal) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppHelper.cargarNoticia(binding, getContext(), noticia);
        return binding.getRoot();
    }
}