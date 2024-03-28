package com.example.olimplicacion.clases;


import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.olimplicacion.MainActivity;
import com.example.olimplicacion.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class EjercicioAdapter extends RecyclerView.Adapter<EjercicioAdapter.ViewHolder>{
    private static List<Ejercicio> dataArrayList = new ArrayList<Ejercicio>();
    private ViewHolder.ItemClickListener clickListener;
    private static Rutina rutina;


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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        //declaración de variables
        TextView nombre;ImageView imagen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.listName01);
            imagen = itemView.findViewById(R.id.listImage01);
        }

        public void bind(Ejercicio ejercicio) {
            nombre.setText(ejercicio.getNombre());
            Glide.with(imagen.getContext())
                    .load(ejercicio.getImg())
                    .placeholder(R.drawable.baseline_add_24)//si no hay imagen carga una por defecto
                    .circleCrop()
                    .error(R.drawable.baseline_add_24)//si ocurre algún error se verá por defecto
                    .into(imagen);

        }

        public interface ItemClickListener{
            public void onItemClick(Ejercicio ejercicio);
        }
    }

}
