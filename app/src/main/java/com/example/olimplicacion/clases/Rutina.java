package com.example.olimplicacion.clases;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.List;

public class Rutina {
//ATRIBUTOS
String nombre, id;
Uri img;
List<String> dias;
List<Ejercicio> ejercicios;

//CONSTRUCTOR

    public Rutina(String id, String nombre, Uri img, List<Ejercicio> ejercicios, List<String> dias) {
        this.id = id;
        this.nombre = nombre;
        this.img = img;
        this.ejercicios = ejercicios;
        this.dias = dias;
    }

    public Rutina(){}
//GETTERS Y SETTERS


    public List<String> getDias() {
        return dias;
    }

    public void setDias(List<String> dias) {
        this.dias = dias;
    }

    public List<Ejercicio> getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(List<Ejercicio> ejercicios) {
        this.ejercicios = ejercicios;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Uri getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = Uri.parse(img);
    }

    @Override
    public String toString() {
        return "Rutina{" +
                "nombre='" + nombre + '\'' +
                ", id='" + id + '\'' +
                ", img=" + img +
                ", ejercicios=" + ejercicios +
                '}';
    }
}
