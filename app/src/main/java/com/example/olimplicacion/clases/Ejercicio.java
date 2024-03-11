package com.example.olimplicacion.clases;

import android.graphics.Bitmap;

import java.util.LinkedList;

public class Ejercicio {
    //ATRIBUTOS
    long id;
    String name, musculos, desc, peso, repeticiones;
    Bitmap image;


    //CONSTRUCTOR
    public Ejercicio(long id, String name, String musculos, String desc, Bitmap image) {
        this.id = id;
        this.name = name;
        this.musculos = musculos;
        this.desc = desc;
        this.image = image;
    }

    public Ejercicio(){

    }

    //GETTERS Y SETTERS


    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(String repeticiones) {
        this.repeticiones = repeticiones;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMusculos() {
        return musculos;
    }

    public void setMusculos(String musculos) {
        this.musculos = musculos;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Ejercicio{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", musculos='" + musculos + '\'' +
                ", desc='" + desc + '\'' +
                ", peso='" + peso + '\'' +
                ", repeticiones='" + repeticiones + '\'' +
                ", image=" + image +
                '}';
    }
}
