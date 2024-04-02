package com.example.olimplicacion.rutinas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.olimplicacion.MainActivity;
import com.example.olimplicacion.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        Context context = itemView.getContext();
        TextView nombre;
        TextView lunes, martes, miercoles, jueves, viernes, sabado, domingo;
        ImageView imagen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.rutinaNombre);
            imagen = itemView.findViewById(R.id.rutinaImg);
        }
        public interface ItemClickListener{
            public void onItemClick(Rutina rutina);
        }
        public void bind(Rutina rutina){
            nombre.setText(rutina.getNombre());
            Glide.with(context)
                    .load(rutina.getImg())
                    .placeholder(R.drawable.baseline_add_242)//si no hay imagen carga una por defecto
                    .circleCrop()
                    .error(R.drawable.baseline_add_242)//si ocurre algún error se verá por defecto
                    .into(imagen);

        }
    }
    /*public void eliminarRutina(String id) {
        System.out.println("eliminarRutina()");
        DatabaseReference ref = FirebaseDatabase.getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("rutinas");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot rut : dataSnapshot.getChildren()) {//
                    if (rut.child("id").getValue().equals(id)) {
                        ref.child(rut.getKey()).removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("No se encuentre o hay un error");
            }
        });
        DatabaseReference ref2 = FirebaseDatabase.getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("usuarios/" + MainActivity.getUsuario().getId() + "/rutinas");
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot rut : dataSnapshot.getChildren()) {//
                    if (rut.getValue().equals(id)) {
                        ref2.child(rut.getKey()).removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("No se encuentra o hay un error");
            }
        });
    }*/
}
