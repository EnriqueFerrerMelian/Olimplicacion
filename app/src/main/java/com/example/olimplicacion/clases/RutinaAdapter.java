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

public class RutinaAdapter  extends RecyclerView.Adapter<RutinaAdapter.ViewHolder>{
    private List<Rutina> dataArrayList = new ArrayList<Rutina>();
    private RutinaAdapter.ViewHolder.ItemClickListener clickListener;

    public RutinaAdapter(List<Rutina> dataArrayList, RutinaAdapter.ViewHolder.ItemClickListener clickListener) {
        this.dataArrayList = dataArrayList;
        this.clickListener = clickListener;

    }

    public void setListaFiltrada(List<Rutina> listaFiltrada){
        this.dataArrayList = listaFiltrada;
        //notifyDataSetChanged();//esto es para un buscador
    }
    @NonNull
    @Override
    public RutinaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rutina, parent, false);
        return new RutinaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RutinaAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
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

        TextView nombre;
        ImageView imagen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.rutinaNombre);
            imagen = itemView.findViewById(R.id.rutinaImg);
        }

        public void bind(Rutina rutina) {
            nombre.setText(rutina.getNombre());
            //imagen.setImageBitmap(ejercicio.getImage());
        }
        public interface ItemClickListener{
            public void onItemClick(Rutina rutina);
        }
    }
}
