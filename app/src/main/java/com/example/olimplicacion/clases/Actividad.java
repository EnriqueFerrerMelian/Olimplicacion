package com.example.olimplicacion.clases;

public class Actividad {
    String nombre, precio, descripcion, vacantes, profesor, horario, img;

    public Actividad(){}

    public Actividad(String nombre, String precio, String descripcion, String vacantes, String profesor, String horario) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.vacantes = vacantes;
        this.profesor = profesor;
        this.horario = horario;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getVacantes() {
        return vacantes;
    }

    public void setVacantes(String vacantes) {
        this.vacantes = vacantes;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    @Override
    public String toString() {
        return "Actividad{" +
                "nombre='" + nombre + '\'' +
                ", precio='" + precio + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", vacantes='" + vacantes + '\'' +
                ", profesor='" + profesor + '\'' +
                ", horario='" + horario + '\'' +
                '}';
    }
}
