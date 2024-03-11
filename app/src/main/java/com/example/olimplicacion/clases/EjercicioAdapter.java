package com.example.olimplicacion.clases;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.olimplicacion.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;
public class EjercicioAdapter extends RecyclerView.Adapter<EjercicioAdapter.ViewHolder>{
    private List<Ejercicio> dataArrayList = new ArrayList<>();

    public EjercicioAdapter(List<Ejercicio> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    public void setListaFiltrada(List<Ejercicio> listaFiltrada){
        this.dataArrayList = listaFiltrada;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public EjercicioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ejercicio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(dataArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        Context context = itemView.getContext();
        //FirebaseHelper fh;
        TextView nombre;
        ShapeableImageView imagen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //fh = new FirebaseHelper("ejercicios");
            nombre = itemView.findViewById(R.id.listName01);
            imagen = itemView.findViewById(R.id.listImage01);


        }

        public void bind(Ejercicio ejercicio) {
            nombre.setText(ejercicio.getName());
            imagen.setImageBitmap(ejercicio.getImage());
        }
    }
}
