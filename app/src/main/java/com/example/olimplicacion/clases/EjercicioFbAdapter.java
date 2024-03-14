package com.example.olimplicacion.clases;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.olimplicacion.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.imageview.ShapeableImageView;


public class EjercicioFbAdapter extends FirebaseRecyclerAdapter<Ejercicio, EjercicioAdapter.ViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public EjercicioFbAdapter(@NonNull FirebaseRecyclerOptions<Ejercicio> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull EjercicioAdapter.ViewHolder holder, int position, @NonNull Ejercicio model) {
        holder.nombre.setText(model.getNombre());
        System.out.println(model);
        Glide.with(holder.imagen.getContext())
                .load(model.getImg())
                .placeholder(R.drawable.baseline_add_24)//si no hay imagen carga una por defecto
                .circleCrop()
                .error(R.drawable.baseline_add_24)//si ocurre algún error se verá por defecto
                .into(holder.imagen);
        System.out.println("Reyenando lista");
    }

    @NonNull
    @Override
    public EjercicioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ejercicio, parent, false);
        return new EjercicioAdapter.ViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView imagen;
        TextView nombre;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.listImage01);
            nombre = itemView.findViewById(R.id.listName01);
        }
    }
}
