package com.example.olimplicacion.actividades;

import android.graphics.Color;
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
import com.example.olimplicacion.clases.Actividad;
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
        if(model.getImg1()!=null){
            Glide.with(holder.imagen.getContext())
                    .load(model.getImg1())
                    .placeholder(R.drawable.baseline_add_242)//si no hay imagen carga una por defecto
                    .error(R.drawable.baseline_add_242)//si ocurre algún error se verá por defecto
                    .into(holder.imagen);
        }
        for (int i = 0; i < model.getDias().size(); i++) {
            if(model.getDias().get(i).equals("l")){
                holder.lunes.setTextColor(Color.rgb(255, 127, 39));
            }
            if(model.getDias().get(i).equals("m")){
                holder.martes.setTextColor(Color.rgb(255, 127, 39));
            }
            if(model.getDias().get(i).equals("x")){
                holder.miercoles.setTextColor(Color.rgb(255, 127, 39));
            }
            if(model.getDias().get(i).equals("j")){
                holder.jueves.setTextColor(Color.rgb(255, 127, 39));
            }
            if(model.getDias().get(i).equals("v")){
                holder.viernes.setTextColor(Color.rgb(255, 127, 39));
            }
            if(model.getDias().get(i).equals("s")){
                holder.sabado.setTextColor(Color.rgb(255, 127, 39));
            }
            if(model.getDias().get(i).equals("d")){
                holder.domingo.setTextColor(Color.rgb(255, 127, 39));
            }
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
        TextView nombre, descripcion, lunes, martes, miercoles, jueves, viernes, sabado, domingo;
        ImageView imagen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreActividad);
            descripcion = itemView.findViewById(R.id.descripcionActividad);
            imagen = itemView.findViewById(R.id.imgActividad);
            lunes = itemView.findViewById(R.id.lunes);
            martes = itemView.findViewById(R.id.martes);
            miercoles = itemView.findViewById(R.id.miercoles);
            jueves = itemView.findViewById(R.id.jueves);
            viernes = itemView.findViewById(R.id.viernes);
            sabado = itemView.findViewById(R.id.sabado);
            domingo = itemView.findViewById(R.id.domingo);
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
