package com.example.olimplicacion.clases;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.olimplicacion.R;
import com.example.olimplicacion.baseDeDatos.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;
public class EjercicioAdapter extends RecyclerView.Adapter<EjercicioAdapter.ViewHolder>{
    FirebaseHelper fbHelper;
    private List<Ejercicio> dataArrayList = new ArrayList<Ejercicio>();
    private ViewHolder.ItemClickListener clickListener;

    public EjercicioAdapter(List<Ejercicio> dataArrayList, ViewHolder.ItemClickListener clickListener) {
        this.dataArrayList = dataArrayList;
        this.clickListener = clickListener;

    }

    public void setListaFiltrada(List<Ejercicio> listaFiltrada){
        this.dataArrayList = listaFiltrada;
        notifyDataSetChanged();//esto es para un buscador
    }
    @NonNull
    @Override
    public EjercicioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ejercicio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bind(dataArrayList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(dataArrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{

        Context context = itemView.getContext();
        FirebaseHelper fbHelper;
        //FirebaseHelper fh;
        TextView nombre;
        ImageView imagen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fbHelper = new FirebaseHelper("ejercicios");
            nombre = itemView.findViewById(R.id.listName01);
            imagen = itemView.findViewById(R.id.listImage01);
        }

        public void bind(Ejercicio ejercicio) {
            nombre.setText(ejercicio.getNombre());
            //imagen.setImageBitmap(ejercicio.getImage());
            System.out.println("Imagen: " + ejercicio.getImg());
        }
        public interface ItemClickListener{
            public void onItemClick(Ejercicio ejercicio);
        }
    }
}
