package com.example.olimplicacion.clases;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.olimplicacion.MainActivity;
import com.example.olimplicacion.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.LinkedList;
import java.util.List;


public class EjercicioFbAdapter extends FirebaseRecyclerAdapter<Ejercicio, EjercicioAdapter.ViewHolder> {
    private EjercicioAdapter.ViewHolder.ItemClickListener clickListener;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public EjercicioFbAdapter(@NonNull FirebaseRecyclerOptions<Ejercicio> options,  EjercicioAdapter.ViewHolder.ItemClickListener clickListener) {
        super(options);
        this.clickListener = clickListener;
    }

    /**
     * Extraerá la información de los objetos y las insertará en la vista si el id del ejercicio está contenido en
     * la lista de ejercicios del usuario.
     * Para seleccionar las que pertenecen al usuario logeado, se crea una lista de integros que contendrán los
     * ejercicios que pertenecen a ese usuario.
     * Luego se comparan los ejercicios descargados con los de la lista y se cargan en el recyclerView los que coincidan.
     * @param holder
     * @param position
     * @param model the model object containing the data that should be used to populate the view.
     */
    @Override
    protected void onBindViewHolder(@NonNull EjercicioAdapter.ViewHolder holder, int position, @NonNull Ejercicio model) {
        holder.nombre.setText(model.getNombre());
        Glide.with(holder.imagen.getContext())
                .load(model.getImg())
                .placeholder(R.drawable.baseline_add_24)//si no hay imagen carga una por defecto
                .circleCrop()
                .error(R.drawable.baseline_add_24)//si ocurre algún error se verá por defecto
                .into(holder.imagen);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(model);
            }
        });
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
    public interface ItemClickListener{
        public void onItemClick(Ejercicio ejercicio);
    }
}
