package com.example.olimplicacion.baseDeDatos;

import androidx.recyclerview.widget.RecyclerView;

import com.example.olimplicacion.MainActivity;
import com.example.olimplicacion.clases.Rutina;
import com.example.olimplicacion.clases.RutinaAdapter;
import com.example.olimplicacion.fragmentos.RutinaFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * SUSCEPTIBLE DE SER BORRADA
 */
public class RutinaDAO {
//ATRIBUTOS
private DatabaseReference ref;
//CONSTRUCTOR

    public RutinaDAO() {
        ref = FirebaseDatabase.getInstance().getReference("rutinas");
    }

//GETTERS Y SETTERS

    public DatabaseReference getRef() {
        return ref;
    }

    public void setRef(DatabaseReference ref) {
        this.ref = ref;
    }


}
