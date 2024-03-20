package com.example.olimplicacion.clases;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.olimplicacion.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.imageview.ShapeableImageView;

public class RutinaFbAdapter  extends FirebaseRecyclerAdapter<Rutina, RutinaFbAdapter.ViewHolder> {
    private ItemClickListener clickListener;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RutinaFbAdapter(@NonNull FirebaseRecyclerOptions<Rutina> options, ItemClickListener clickListener) {
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
    protected void onBindViewHolder(@NonNull RutinaFbAdapter.ViewHolder holder, int position, @NonNull Rutina model) {
        holder.nombre.setText(model.getNombre());
        if(model.getImg()!=null){
            Glide.with(holder.imagen.getContext())
                    .load(model.getImg())
                    .placeholder(R.drawable.baseline_add_24)//si no hay imagen carga una por defecto
                    .circleCrop()
                    .error(R.drawable.baseline_add_24)//si ocurre algún error se verá por defecto
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
    public RutinaFbAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rutina, parent, false);
        return new RutinaFbAdapter.ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //ShapeableImageView imagen;
        TextView nombre, lunes, martes, miercoles, jueves, viernes, sabado, domingo;
        ImageView imagen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.rutinaImg);
            nombre = itemView.findViewById(R.id.rutinaNombre);
            lunes = itemView.findViewById(R.id.lunes);
            martes = itemView.findViewById(R.id.martes);
            miercoles = itemView.findViewById(R.id.miercoles);
            jueves = itemView.findViewById(R.id.jueves);
            viernes = itemView.findViewById(R.id.viernes);
            sabado = itemView.findViewById(R.id.sabado);
            domingo = itemView.findViewById(R.id.domingo);
        }
    }
    public interface ItemClickListener{
        public void onItemClick(Rutina rutina);
    }
}
