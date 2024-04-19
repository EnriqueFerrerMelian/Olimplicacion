package com.example.olimplicacion.clases;

import com.example.olimplicacion.rutinas.Rutina;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Usuario {
    int id;
    String nombre, clave;
    Map<String, Object> rutinas;
    Map<String, Object> peso;

    public Usuario(int id, String nombre,String clave, Map<String, Object> rutinas,  Map<String, Object>  peso) {
        this.id = id;
        this.nombre = nombre;
        this.clave = clave;
        this.rutinas = rutinas;
        this.peso = peso;

    }
    public Usuario(){
        this.rutinas = new HashMap<>();
        this.peso = new HashMap<>();
    }

    public Map<String, Object> getPeso() {
        return peso;
    }

    public void setPeso(Map<String, Object> peso) {
        this.peso = peso;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Map<String, Object> getRutinas() {
        return rutinas;
    }

    public void setRutinas(Map<String, Object> rutinas) {
        this.rutinas = rutinas;
    }

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

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", clave='" + clave + '\'' +
                ", rutinas=" + rutinas +
                ", peso=" + peso +
                '}';
    }
}