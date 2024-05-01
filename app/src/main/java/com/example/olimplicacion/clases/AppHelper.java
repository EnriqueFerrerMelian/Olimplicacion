package com.example.olimplicacion.clases;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import com.example.olimplicacion.MainActivity;
import com.example.olimplicacion.databinding.FragmentEstadisticasBinding;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AppHelper {
//ATRIBUTOS

//CONSTRUCTOR

//GETTERS Y SETTERS
    public static void escribirToast(String texto, Context context){
        Toast.makeText(context, texto, Toast.LENGTH_LONG).show();
    }

    // ESTADÍSTICAS**********************************ESTADÍSTICAS**********************************
    //TODOS LOS MÉTODOS DEL FRAGMENTO ESTADISTICAS AQUÍ
    /**
     * Actualiza los datos del peso, que luego se pasarán
     * a la base de datos.
     * @param peso Peso seleccionado, transformado en string.
     * @param objetivo Objetivo seleccionado, transformado en string.
     * @return Objeto Peso con los datos actualizados
     */
    public static Peso addDatos(String peso, String objetivo, Peso pesoOB){
        //Obtengo la fecha de hoy
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        String fecha = date.getDate() +"/"+ date.getMonth();

        //creo un mapa que guarde los ejes
        Map<String, String> datosPeso = new HashMap<>();
        datosPeso.put("x", String.valueOf(pesoOB.getDatosPeso().size()+1));
        datosPeso.put("y", peso);

        pesoOB.getFecha().add(fecha);
        pesoOB.getDatosPeso().add(datosPeso);
        pesoOB.setObjetivo(objetivo);
        return pesoOB;
    }

    /**
     * Configura la apariencia por defecto del chart y carga los datos del objeto Peso
     */
    public static void configurarChartPeso(FragmentEstadisticasBinding binding, Peso pesoOB){
        float maxView = 0;float minView = 0;
        System.out.println("configurarLineChart()");

        //borrando bordes
        binding.lineChart.setDrawBorders(false);
        binding.lineChart.getAxisRight().setDrawLabels(false);
        binding.lineChart.getAxisRight().setDrawGridLines(false);
        binding.lineChart.getAxisLeft().setDrawLabels(false);
        binding.lineChart.getAxisLeft().setDrawGridLines(false);
        binding.lineChart.getXAxis().setDrawGridLines(false);
        binding.lineChart.setDrawGridBackground(false);

        //leyends
        Legend l = binding.lineChart.getLegend();
        l.setEnabled(true);
        l.setTextSize(15);
        l.setForm(Legend.LegendForm.LINE);
        LegendEntry[] legendEntry = new LegendEntry[2];
        LegendEntry lEntry1 = new LegendEntry();
        lEntry1.formColor = Color.GREEN;
        lEntry1.label = "Peso";
        legendEntry[0] = lEntry1;
        LegendEntry lEntry2 = new LegendEntry();
        lEntry2.formColor = Color.rgb(255,135,0);
        lEntry2.label = "Objetivo";
        legendEntry[1] = lEntry2;
        l.setCustom(legendEntry);

        binding.lineChart.canScrollHorizontally(1);
        binding.lineChart.setVisibleXRangeMaximum(7f);
        binding.lineChart.setNoDataText("No se ha guardado ningún dato.");

        //>>>****INSERCIÓN DE DATOS********

        //insertando fechas en eje X
        XAxis xAxis = binding.lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        final List<String> fechas = pesoOB.getFecha();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(fechas));

        //inserción de entradas
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < pesoOB.getDatosPeso().size(); i++) {
            entries.add(new Entry((float) i,Float.parseFloat(Objects.requireNonNull(pesoOB.getDatosPeso().get(i).get("y")))));
            if(Float.parseFloat(Objects.requireNonNull(pesoOB.getDatosPeso().get(i).get("y")))>maxView){
                maxView = Float.parseFloat(Objects.requireNonNull(pesoOB.getDatosPeso().get(i).get("y")));
            }
            if(Float.parseFloat(Objects.requireNonNull(pesoOB.getDatosPeso().get(i).get("y")))<minView){
                minView = Float.parseFloat(Objects.requireNonNull(pesoOB.getDatosPeso().get(i).get("y")));
            }

        }
        LineDataSet lineDataSet = new LineDataSet(entries, "Peso");
        lineDataSet.setColor(Color.GREEN);
        lineDataSet.setValueTextSize(15);
        lineDataSet.setLineWidth(3);
        LineData lineData = new LineData(lineDataSet);
        if(pesoOB.getDatosPeso().size()>0){
            binding.ultimoPeso.setText(pesoOB.getDatosPeso().get(pesoOB.getDatosPeso().size()-1).get("y") + " Kgs");
        }
        //>>>****INSERCIÓN DE DATOS*****FIN

        //si se ha seleccionado una marca de objetivo
        if(pesoOB.getObjetivo()!=null){
            float leyendVal =Float.parseFloat(pesoOB.getObjetivo());
            YAxis yAxis = binding.lineChart.getAxisLeft();
            LimitLine ll = new LimitLine(leyendVal, "Objetivo");
            ll.setLineColor(Color.rgb(255,135,0));
            ll.setLineWidth(3f);
            ll.setTextColor(Color.BLACK);
            ll.setTextSize(10f);
            yAxis.addLimitLine(ll);
            float valorMaximoVisible = (maxView<leyendVal)? leyendVal : maxView;
            float valorMinimoVisible = (minView>leyendVal)? leyendVal : minView;
            yAxis.setAxisMaximum(valorMaximoVisible);
            yAxis.setAxisMinimum(valorMinimoVisible);
        }
        binding.lineChart.moveViewToX(entries.size()-1);
        binding.lineChart.setData(lineData);
        binding.lineChart.invalidate();
    }

    public static void configurarChartAvance(FragmentEstadisticasBinding binding){
        System.out.println("configurarBarChart()");
        //configurar descripción
        Description description = new Description();
        description.setText("Progreso");
        description.setTextSize(14f);
        description.setPosition(175f, 25f);

        //borrando bordes
        binding.barChart.setDrawBorders(false);
        binding.barChart.getAxisRight().setDrawLabels(false);
        binding.barChart.getAxisRight().setDrawGridLines(false);
        binding.barChart.getAxisLeft().setDrawLabels(false);
        binding.barChart.getAxisLeft().setDrawGridLines(false);
        binding.barChart.getXAxis().setDrawGridLines(false);
        binding.barChart.setDrawGridBackground(false);

        //leyends
        Legend l = binding.barChart.getLegend();
        l.setEnabled(true);
        l.setTextSize(15);
        l.setForm(Legend.LegendForm.LINE);
        LegendEntry[] legendEntry = new LegendEntry[1];
        LegendEntry lEntry1 = new LegendEntry();
        lEntry1.formColor = Color.RED;
        lEntry1.label = "Kgs";
        legendEntry[0] = lEntry1;
        l.setCustom(legendEntry);

        //>>>****INSERCIÓN DE DATOS********
        //insertando nombres en eje X
        XAxis xAxis = binding.barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(recortarNombres(MainActivity.getAvance().getEjerciciosNombres())));

        //inserción de entradas
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < MainActivity.getAvance().getPesos().size(); i++) {//pesos de los ejercicios, array de Strings
            entries.add(new BarEntry(Float.valueOf(i),Float.valueOf(MainActivity.getAvance().getPesos().get(i))));
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Avance");
        barDataSet.setColor(Color.RED);
        barDataSet.setValueTextSize(15);
        BarData barData = new BarData(barDataSet);
        binding.ultimoProgreso
                .setText(MainActivity.getAvance()
                        .getEjerciciosNombres()
                        .get(MainActivity.getAvance()
                                .getEjerciciosNombres().size()-1));
        //>>>****INSERCIÓN DE DATOS*****fin

        binding.barChart.setDescription(description);
        binding.barChart.canScrollHorizontally(1);

        binding.barChart.setNoDataText("No se ha guardado ningún dato.");
        binding.barChart.moveViewToX(entries.size()-1);
        binding.barChart.setData(barData);
        binding.barChart.invalidate();
        binding.barChart.setVisibleXRangeMaximum(5);
    }
    public static List<String> recortarNombres(List<String> nombres){
        List<String> nombresCortos = new ArrayList<>();
        for (int i = 0; i < nombres.size(); i++) {
            if(nombres.get(i).split(" ").length>0){
                String[] arrayNombres = nombres.get(i).split(" ");
                nombresCortos.add(arrayNombres[0]);
            }
        }
        return nombresCortos;
    }
    // ESTADÍSTICAS**********************************ESTADÍSTICAS**********************************
    public void hotFixAvtividad(){
        Actividad actividad = new Actividad("Kárate", "22.50€", "Artes marciales", "15", "Andrés", "De 6 a 7:30");
        Map<String, Object> actividadMapa = new HashMap<>();
        actividadMapa.put("Kárate", actividad);
        DatabaseReference ref = FirebaseDatabase
                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("actividades");
        ref.setValue(actividadMapa);
    }
    public static void hotFixPesos(){
        Map<String, String> datos = new HashMap<>(); datos.put("x", "0.0");datos.put("y", "0.0");
        List<Map<String, String>> datosPeso = new ArrayList<>();
        datosPeso.add(datos);
        List<String> fechas = new ArrayList<>();fechas.add("1/12");
        Peso peso = new Peso(datosPeso, fechas, "0.0");
        DatabaseReference ref = FirebaseDatabase
                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("usuarios/"+ MainActivity.getUsuario().getId()+"/peso");
        ref.setValue(peso);
    }

}
