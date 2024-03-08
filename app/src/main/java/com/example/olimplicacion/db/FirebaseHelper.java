package com.example.olimplicacion.db;


import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class FirebaseHelper {
//ATRIBUTOS
    private static final FirebaseStorage fs = FirebaseStorage.getInstance();//sustituye StorageReference
    private DocumentReference documento;//documentReference;
    private static CollectionReference coleccion;//collectionReference;
    private static final FirebaseFirestore DB = FirebaseFirestore.getInstance();
//CONSTRUCTOR

    public FirebaseHelper(String collectionReference) {
        this.documento = FirebaseFirestore.getInstance().document("");
        this.coleccion = DB.collection(collectionReference);
    }

//GETTERS Y SETTERS
    public FirebaseStorage getFbStorage(){
        return fs;
    }
    public DocumentReference getDocumentReference() {
        return documento;
    }

    public void setDocumentReference(String documentReference) {
        this.documento = FirebaseFirestore.getInstance().document(documentReference);
    }

    public CollectionReference getCollectionReference() {
        return coleccion;
    }

    public void setCollectionReference(String collectionReference) {
        this.coleccion = DB.collection(collectionReference);
    }

    /*public void subirPublicacion(Publicacion publicacion) {
        coleccion.add(publicacion).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                System.out.println(publicacion.getTitulo() + " - subido");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("No subido");
            }
        });
    }
    public void addUser(Usuario usuario){
        //averiguar el tamaño de la coleccion
        coleccion.add(usuario).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                System.out.println(usuario.getNombre()+ " - subido");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("No subido");
            }
        });
    }

    public List<Publicacion> getPublicaciones() {
        List<Publicacion> publicaciones = new ArrayList<>();
        System.out.println("Cargando coleccion");
        // Create a new user with a first, middle, and last name
        this.coleccion.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    //creamos el objeto publicacin
                    Publicacion publicacion = new Publicacion();
                    //le añadimos los datos uno a uno
                    publicacion.setTitulo(documentSnapshot.get("titulo").toString());
                    publicacion.setComentario(documentSnapshot.get("comentario").toString());
                    publicacion.setId(Integer.parseInt(documentSnapshot.get("id").toString()));
                    publicacion.setUsuario(Integer.parseInt(documentSnapshot.get("usuario").toString()));
                    publicacion.setLike(Boolean.parseBoolean(documentSnapshot.get("isLike").toString()));
                    publicaciones.add(publicacion);
                }
            }
        });
        return publicaciones;
    }*/

}
