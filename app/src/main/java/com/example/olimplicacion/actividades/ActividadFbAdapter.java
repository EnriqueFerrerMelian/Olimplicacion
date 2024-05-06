package com.example.olimplicacion.actividades;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.olimplicacion.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ActividadFbAdapter extends FirebaseRecyclerAdapter<Actividad, ActividadFbAdapter.ViewHolder> {
    private ItemClickListener clickListener;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ActividadFbAdapter(@NonNull FirebaseRecyclerOptions<Actividad> options, ItemClickListener clickListener) {
        super(options);
        this.clickListener = clickListener;
    }
    @Override
    protected void onBindViewHolder(@NonNull ActividadFbAdapter.ViewHolder holder, int position, @NonNull Actividad model) {
        holder.nombre.setText(model.getNombre());
        holder.descripcion.setText(model.getDescripcion());
        if(model.getImg()!=null){
            Glide.with(holder.imagen.getContext())
                    .load(model.getImg())
                    .placeholder(R.drawable.baseline_add_242)//si no hay imagen carga una por defecto
                    .error(R.drawable.baseline_add_242)//si ocurre algún error se verá por defecto
                    .into(holder.imagen);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(model);
            }
        });
    }

    @NonNull
    @Override
    public ActividadFbAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actividad, parent, false);
        return new ActividadFbAdapter.ViewHolder(view);
    }
    class ViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {
        TextView nombre,descripcion;
        ImageView imagen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreActividad);
            descripcion = itemView.findViewById(R.id.descripcionActividad);
            imagen = itemView.findViewById(R.id.imgActividad);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            return false;
        }
    }



    public interface ItemClickListener{
        public void onItemClick(Actividad actividad);
    }
}
