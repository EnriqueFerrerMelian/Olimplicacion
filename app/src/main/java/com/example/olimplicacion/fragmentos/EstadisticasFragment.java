package com.example.olimplicacion.fragmentos;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.olimplicacion.MainActivity;
import com.example.olimplicacion.R;
import com.example.olimplicacion.clases.Avance;
import com.example.olimplicacion.clases.Peso;
import com.example.olimplicacion.clases.Usuario;
import com.example.olimplicacion.databinding.FragmentEstadisticasBinding;
import com.example.olimplicacion.ejercicios.Ejercicio;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstadisticasFragment extends Fragment {
    private static FragmentEstadisticasBinding binding;
    private static Peso pesoOB = MainActivity.getPeso();
    private static Avance avanceOB = MainActivity.getAvance();
    private static Entry pesoSeleccionado = new Entry();
    private static BarEntry progresoSeleccionado = new BarEntry(0,0);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEstadisticasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if(pesoOB==null){
            pesoOB = new Peso();
        }else{
            binding.textoInfo.setVisibility(View.GONE);
        }
        if(avanceOB==null){
            avanceOB = new Avance();
        }
        configurarChartPeso();
        configurarChartAvance();
        binding.aniadirPeso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottonSheetPeso();
            }
        });

        /**
         * selecciona un dato del chart y lo guarda en una variable global 'pesoSeleccionado'
         */
        binding.lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                System.out.println(e.getX());
                pesoSeleccionado = e;
            }
            @Override
            public void onNothingSelected() {}
        });
        binding.barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                System.out.println("Progreso: "+ e.getX());
                progresoSeleccionado = (BarEntry) e;
            }

            @Override
            public void onNothingSelected() {}
        });
        binding.eliminarPeso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.lineChart.getLineData().getDataSetCount()>0){
                    int index = binding.lineChart.getLineData().getDataSetByIndex(0).getEntryIndex(pesoSeleccionado);
                    pesoOB.getDatosPeso().remove(index);
                    pesoOB.getFecha().remove(index);
                    actualizarPeso(pesoOB);
                }else{
                    escribirToast("Debe haber al menos un registro");
                }
            }
        });
        binding.eliminarAvance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.lineChart.getLineData().getDataSetCount()>0){
                    int index = binding.barChart.getBarData().getDataSetByIndex(0).getEntryIndex(progresoSeleccionado);
                    avanceOB.getEjerciciosNombres().remove(index);
                    avanceOB.getPesos().remove(index);
                    actualizarAvance(avanceOB);
                }else{
                    escribirToast("Debe haber al menos un registro");
                }
            }
        });
    }
    /**
     * Abre un cuadro de diálogo con el que se podrá introducir un peso en el registro
     */
    public void showBottonSheetPeso(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.my_bottom_sheet_add_peso);
        Button add = dialog.findViewById(R.id.add);
        //inicialización de numberPicker**************************
        NumberPicker numberPesoOb = dialog.findViewById(R.id.numberPesoOb);
        numberPesoOb.setMinValue(0);numberPesoOb.setMaxValue(150);
        NumberPicker numberPesoDecimalOb = dialog.findViewById(R.id.numberPesoDecimalOb);
        numberPesoDecimalOb.setMinValue(0);numberPesoDecimalOb.setMaxValue(9);
        if(pesoOB.getObjetivo()!=null){
            String[] objetivo = pesoOB.getObjetivo().split("\\.");
            numberPesoOb.setValue(Integer.valueOf(objetivo[0]));
            numberPesoDecimalOb.setValue(Integer.valueOf(objetivo[1]));
        }
        NumberPicker numberPeso = dialog.findViewById(R.id.numberPeso);
        numberPeso.setMinValue(1);numberPeso.setMaxValue(150);
        NumberPicker numberPesoDecimal = dialog.findViewById(R.id.numberPesoDecimal);
        numberPesoDecimal.setMinValue(0);numberPesoDecimal.setMaxValue(9);
        if(pesoOB.getDatosPeso().size()>0){
            String[] objetivo = pesoOB.getDatosPeso().get(pesoOB.getDatosPeso().size()-1).get("y").split("\\.");
            numberPeso.setValue(Integer.valueOf(objetivo[0]));
            numberPesoDecimal.setValue(Integer.valueOf(objetivo[1]));
        }
        //inicialización de numberPicker**********************fin*
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logica del realtime database
                String pesoOut = numberPeso.getValue() +"." + numberPesoDecimal.getValue();
                String objetivoOut = numberPesoOb.getValue() +"." + numberPesoDecimalOb.getValue();
                actualizarPeso(addDatos(pesoOut, objetivoOut));
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    /**
     * Actualiza los datos del peso, que luego se pasarán a la base de datos.
     * @param peso Peso seleccionado, transformado en string.
     * @param objetivo Objetivo seleccionado, transformado en string.
     * @return Objeto Peso con los datos actualizados
     */
    public Peso addDatos(String peso, String objetivo){
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
     * Actualiza los datos del peso del usuario en Firebase.
     * @param peso
     */
    public void actualizarPeso(Peso peso){
        DatabaseReference ref = FirebaseDatabase
                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("usuarios/"+ MainActivity.getUsuario().getId()+"/peso");
        ref.setValue(peso).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                actualizarUsuario();
            }
        });
    }

    /**
     * Actualiza los objetos usuario y peso de la aplicación con los de FireBase.
     * Cuando se actualizan se vuelve a cargar el gráfico
     */
    public static void actualizarUsuario(){
        DatabaseReference ref = FirebaseDatabase.getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("usuarios/"+MainActivity.getUsuario().getId());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MainActivity.setUsuario(dataSnapshot.getValue(Usuario.class));
                MainActivity.setPeso(dataSnapshot.child("peso").getValue(Peso.class));
                configurarChartPeso();
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
    public static void actualizarAvance(Avance avance){
        System.out.println("actualizarAvance()");
        DatabaseReference ref = FirebaseDatabase
                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("usuarios/"+ MainActivity.getUsuario().getId()+"/avance");
        ref.setValue(avance).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                actualizarUsuario();
                configurarChartAvance();
            }
        });
    }

    /**
     * Configura la apariencia por defecto del chart y carga los datos del objeto Peso
     */
    public static void configurarChartPeso(){
        System.out.println("configurarLineChart()");
        //configurar descripción
        Description description = new Description();
        description.setText("Seguimiento de peso");
        description.setTextSize(14f);
        description.setPosition(360f, 25f);

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

        binding.lineChart.setDescription(description);
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

        //si se ha seleccionado una marca de objetivo
        if(pesoOB.getObjetivo()!=null){
            YAxis leftAxis = binding.lineChart.getAxisLeft();
            LimitLine ll = new LimitLine(Float.valueOf(pesoOB.getObjetivo()), "Objetivo");
            ll.setLineColor(Color.rgb(255,135,0));
            ll.setLineWidth(3f);
            ll.setTextColor(Color.BLACK);
            ll.setTextSize(10f);
            leftAxis.addLimitLine(ll);
        }

        //inserción de entradas
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < pesoOB.getDatosPeso().size(); i++) {
            entries.add(new Entry(Float.valueOf(i),Float.valueOf(pesoOB.getDatosPeso().get(i).get("y")) ));
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "Peso");
        lineDataSet.setColor(Color.GREEN);
        lineDataSet.setValueTextSize(15);
        lineDataSet.setLineWidth(3);
        LineData lineData = new LineData(lineDataSet);
        //>>>****INSERCIÓN DE DATOS*****FIN

        binding.lineChart.moveViewToX(entries.size()-1);
        binding.lineChart.setData(lineData);
        binding.lineChart.invalidate();
    }
    public static void configurarChartAvance(){
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


        binding.barChart.setDescription(description);
        binding.barChart.canScrollHorizontally(1);
        binding.barChart.setVisibleXRangeMaximum(5f);
        binding.barChart.setNoDataText("No se ha guardado ningún dato.");

        //****inserción de datos********
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
        //****inserción de datos*****fin

        binding.barChart.moveViewToX(entries.size()-1);
        binding.barChart.setData(barData);
        binding.barChart.invalidate();
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
    public void escribirToast(String texto){
        Toast.makeText(getContext(), texto, Toast.LENGTH_LONG).show();
    }


}