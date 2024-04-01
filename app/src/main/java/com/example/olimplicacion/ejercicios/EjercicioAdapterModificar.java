package com.example.olimplicacion.ejercicios;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.olimplicacion.R;
import com.example.olimplicacion.ejercicios.Ejercicio;
import com.example.olimplicacion.rutinas.CreacionRutinaFragment;
import com.example.olimplicacion.rutinas.Rutina;

import java.util.ArrayList;
import java.util.List;
public class EjercicioAdapterModificar extends RecyclerView.Adapter<EjercicioAdapterModificar.ViewHolder>{
    private static List<Ejercicio> dataArrayList = new ArrayList<Ejercicio>();
    private ViewHolder.ItemClickListener clickListener;
    private static Rutina rutina;


    public EjercicioAdapterModificar(List<Ejercicio> dataArrayList, ViewHolder.ItemClickListener clickListener, Rutina rutina) {
        this.dataArrayList = dataArrayList;
        this.clickListener = clickListener;
        this.rutina = rutina;
    }

    public void setListaFiltrada(List<Ejercicio> listaFiltrada){
        this.dataArrayList = listaFiltrada;
        notifyDataSetChanged();//esto es para un buscador
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ejercicio_modificar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bind(dataArrayList.get(position));
        holder.idEjercicio = dataArrayList.get(position).getId();
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
        TextView nombre;ImageView imagen;ImageButton borrar;int idEjercicio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.listName01);
            imagen = itemView.findViewById(R.id.listImage01);
            borrar = itemView.findViewById(R.id.borrar);
            borrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CreacionRutinaFragment.eliminarEjercicio(idEjercicio);
                    //eliminarEjercicio(rutina.getId(), idEjercicio);
                }
            });
        }

        public void bind(Ejercicio ejercicio) {
            nombre.setText(ejercicio.getNombre());
            Glide.with(imagen.getContext())
                    .load(ejercicio.getImg())
                    .placeholder(R.drawable.baseline_add_242)//si no hay imagen carga una por defecto
                    .circleCrop()
                    .error(R.drawable.baseline_add_242)//si ocurre algún error se verá por defecto
                    .into(imagen);

        }


        /*public void eliminarEjercicio(String idRutina, int idEjercicio) {
            System.out.println("eliminarEjercicio()");
            DatabaseReference ref = FirebaseDatabase.getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                    .getReference("rutinas");
            //en la lista de rutinas, busco la que tenga el id pasado por parámetro
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot rut : dataSnapshot.getChildren()) {//
                        //si la rutina que hemos pasado está en la lista
                        if (rut.child("id").getValue().equals(idRutina)) {
                            //obtenemos su nombre en la base de datos
                            String codigoRutina = rut.getKey();

                            //creamos la referencia a los ejercicios de la rutina con ese nombre
                            DatabaseReference ejercicios = ref.child(codigoRutina+"/ejercicios");
                            ejercicios.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot rut : dataSnapshot.getChildren()) {
                                        if (Integer.parseInt(rut.child("id").getValue().toString())==idEjercicio) {

                                            System.out.println(ejercicios.child(rut.getKey()));
                                            System.out.println("Borrando ejercicio en la rutina");
                                            for (int i = 0; i < rutina.getEjercicios().size(); i++) {
                                                if(rutina.getEjercicios().get(i).getId()==idEjercicio){
                                                    //rutina.getEjercicios().remove(i);
                                                    System.out.println("rutina.getEjercicios().remove(i): " + rutina.getEjercicios().get(i));
                                                }
                                            }
                                            System.out.println("Borrando ejercicio en la base de datos");
                                            System.out.println("ejercicios.child(rut.getKey()).removeValue(): " + ejercicios.child(rut.getKey()));
                                            //ejercicios.child(rut.getKey()).removeValue();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    System.out.println("No se encuentre o hay un error");
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    System.out.println("No se encuentre o hay un error");
                }
            });

        }*/

        public interface ItemClickListener{
            public void onItemClick(Ejercicio ejercicio);
        }
    }

}
