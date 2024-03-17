package com.example.olimplicacion.clases;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    String id, nombre, clave;
    List<Integer> rutinas;

    public Usuario(String id, String nombre,String clave, List<Integer> rutinas) {
        this.id = id;
        this.nombre = nombre;
        this.clave = clave;
        this.rutinas = rutinas;
    }
    public Usuario(){}
    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public List<Integer> getRutinas() {
        return rutinas;
    }

    public void setRutinas(List<Integer> rutinas) {
        this.rutinas = rutinas;
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

    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", clave='" + clave + '\'' +
                ", rutinas=" + rutinas +
                '}';
    }
}