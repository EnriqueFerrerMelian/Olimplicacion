package com.example.olimplicacion.rutinas;

import com.example.olimplicacion.ejercicios.Ejercicio;

import java.util.List;

public class Rutina {
//ATRIBUTOS
String nombre, id;
    String img;
List<String> dias;
List<Ejercicio> ejercicios;

//CONSTRUCTOR

    public Rutina(String id, String nombre, String img, List<Ejercicio> ejercicios, List<String> dias) {
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Rutina{" +
                "nombre='" + nombre + '\'' +
                ", id='" + id + '\'' +
                ", img='" + img + '\'' +
                ", dias=" + dias +
                ", ejercicios=" + ejercicios +
                '}';
    }
}
