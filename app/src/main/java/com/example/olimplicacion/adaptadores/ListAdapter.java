/*
package com.example.olimplicacion.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.olimplicacion.clases.Ejercicio;
import com.example.olimplicacion.R;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Ejercicio>{
    public ListAdapter(@NonNull Context context, ArrayList<Ejercicio> dataArrayList) {
        super(context, R.layout.list_item01, dataArrayList);
    }
//ATRIBUTOS

//CONSTRUCTOR

//GETTERS Y SETTERS

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        Ejercicio ejercicio = getItem(position);

        if(view ==null){ //si la vista recogida es null
            //se genera la siguiente
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item01, parent, false);
        }

        //se obtendrán los objetos de esa vista
        ImageView listImage = view.findViewById(R.id.listImage01);
        TextView listName = view.findViewById(R.id.listName01);

        //se cambiará su contenido por los del objeto ejercicio obtenido
        listImage.setImageResource(ejercicio.getImage());
        listName.setText(ejercicio.getName());

        return view;
    }
}
*/
