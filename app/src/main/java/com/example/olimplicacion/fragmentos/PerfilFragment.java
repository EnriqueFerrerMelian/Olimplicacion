package com.example.olimplicacion.fragmentos;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.olimplicacion.MainActivity;
import com.example.olimplicacion.MenuPrincipal;
import com.example.olimplicacion.R;
import com.example.olimplicacion.clases.AppHelper;
import com.example.olimplicacion.databinding.FragmentPerfilBinding;
import com.example.olimplicacion.databinding.MyBottomSheetCambiarPasswordBinding;

public class PerfilFragment extends Fragment {
private static FragmentPerfilBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        AppHelper.cambiarToolbarText("Perfil");
        AppHelper.cargaPerfil(binding);
        binding.modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottonSheetCambiar(binding.nombre, binding);
            }
        });
        return binding.getRoot();
    }
    public void showBottonSheetCambiar(TextView nombreHeader, FragmentPerfilBinding binding){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.my_bottom_sheet_cambiar_password);
        Button aceptar = dialog.findViewById(R.id.aceptar);
        Button cancel = dialog.findViewById(R.id.cancel);
        EditText nombre = dialog.findViewById(R.id.nombre);
        EditText passAntiguo = dialog.findViewById(R.id.passAntiguo);
        EditText passNuevo = dialog.findViewById(R.id.passNuevo);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppHelper.cambiarDatos(nombre,passAntiguo,passNuevo, getContext());
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    public static FragmentPerfilBinding getPerfilBinding(){
        return binding;
    }
}