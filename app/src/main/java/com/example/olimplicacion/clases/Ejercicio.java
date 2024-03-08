package com.example.olimplicacion.clases;

import java.util.LinkedList;

public class Ejercicio {
    //ATRIBUTOS
    private long id;
    private int  name, musculos, desc, image;


    //CONSTRUCTOR
    public Ejercicio(long id, int name, int musculos, int desc, int image) {
        this.id = id;
        this.name = name;
        this.musculos = musculos;
        this.desc = desc;
        this.image = image;
    }

    public Ejercicio(){

    }

    //GETTERS Y SETTERS



    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public int getName() {
        return name;
    }
    public void setName(int name) {
        this.name = name;
    }
    public int getMusculos() {
        return musculos;
    }
    public void setMusculos(int musculos) {
        this.musculos = musculos;
    }
    public int getDesc() {
        return desc;
    }
    public void setDesc(int desc) {
        this.desc = desc;
    }
    public int getImage() {
        return image;
    }
    public void setImage(int image) {
        this.image = image;
    }
}
