package com.example.olimplicacion.clases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Con esta clase se administrará el seguimiento del peso corporal
 */
public class Peso {
//ATRIBUTOS
List<Map<String, String>> datosPeso;
List<String> fecha;
String objetivo;
//CONSTRUCTOR
    public Peso(){
        this.datosPeso = new ArrayList<>();
        this.fecha = new ArrayList<>();
    }
    public Peso(List<Map<String, String>>  datosPeso, List<String> fecha, String objetivo){
        this.datosPeso = datosPeso;
        this.fecha = fecha;
        this.objetivo = objetivo;
    }

//GETTERS Y SETTERS

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public List<String> getFecha() {
        return fecha;
    }

    public void setFecha(List<String> fecha) {
        this.fecha = fecha;
    }

    public List<Map<String, String>>  getDatosPeso() {
        return datosPeso;
    }

    public void setDatosPeso(List<Map<String, String>>  datosPeso) {
        this.datosPeso = datosPeso;
    }

    @Override
    public String toString() {
        return "Peso{" +
                "datosPeso=" + datosPeso +
                ", fecha='" + fecha + '\'' +
                ", objetivo=" + objetivo +
                '}';
    }
}
