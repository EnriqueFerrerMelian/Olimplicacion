package com.example.olimplicacion.clases;

import android.graphics.Bitmap;

public class Rutina {
//ATRIBUTOS
int id;
String nombre;
Bitmap img;
//CONSTRUCTOR

    public Rutina(int id, String nombre, Bitmap img) {
        this.id = id;
        this.nombre = nombre;
        this.img = img;
    }

    public Rutina(){}
//GETTERS Y SETTERS


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Rutina{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", img=" + img +
                '}';
    }
}
