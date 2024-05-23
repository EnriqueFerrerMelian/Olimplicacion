package com.example.olimplicacion.clases;

import java.util.HashMap;
import java.util.Map;

public class Usuario {
    int id;
    String nombre, clave, usuario;
    Map<String, Object> rutinas;
    Map<String, Object> peso;
    Map<String, Object> avance;

    public Usuario(int id, String nombre,String usuario, String clave, Map<String, Object> rutinas,  Map<String, Object>  peso) {
        this.id = id;
        this.nombre = nombre;
        this.clave = clave;
        this.rutinas = rutinas;
        this.peso = peso;
        this.usuario = usuario;
    }
    public Usuario(){
        this.rutinas = new HashMap<>();
        this.peso = new HashMap<>();
    }

    public String getUsuario() {
        return usuario;
    }

    public Map<String, Object> getAvance() {
        return avance;
    }

    public void setAvance(Map<String, Object> avance) {
        this.avance = avance;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
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