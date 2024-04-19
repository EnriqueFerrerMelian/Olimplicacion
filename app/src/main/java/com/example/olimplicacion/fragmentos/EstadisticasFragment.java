package com.example.olimplicacion.fragmentos;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.olimplicacion.MainActivity;
import com.example.olimplicacion.R;
import com.example.olimplicacion.clases.Peso;
import com.example.olimplicacion.clases.Usuario;
import com.example.olimplicacion.databinding.FragmentEstadisticasBinding;
import com.example.olimplicacion.ejercicios.Ejercicio;
import com.example.olimplicacion.rutinas.DetallesRutinaFragment;
import com.example.olimplicacion.rutinas.Rutina;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EstadisticasFragment extends Fragment {
    private static FragmentEstadisticasBinding binding;
    private static Usuario usuario = MainActivity.getUsuario();
    private static Peso pesoOB = MainActivity.getPeso();

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

        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        String fecha = String.valueOf(date.getDate()) +" / "+ String.valueOf(date.getMonth());
        System.out.println("GetDate: " + fecha);
        cargarChart();
        binding.anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottonSheet();
            }
        });
    }
    public void showBottonSheet(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.my_bottom_sheet_add_peso);
        Button add = dialog.findViewById(R.id.add);
        //inicialización de numberPicker**************************
        NumberPicker numberPeso = dialog.findViewById(R.id.numberPeso);
        numberPeso.setMinValue(1);numberPeso.setMaxValue(100);
        NumberPicker numberPesoDecimal = dialog.findViewById(R.id.numberPesoDecimal);
        numberPesoDecimal.setMinValue(0);numberPesoDecimal.setMaxValue(9);
        //inicialización de numberPicker**********************fin*
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logica del realtime database
                String pesoOut = numberPeso.getValue() +"." + numberPesoDecimal.getValue();
                actualizarPeso(addPeso(pesoOut));
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }


    public Peso addPeso(String peso){
        Map<String, String> datosPeso = new HashMap<>();
        datosPeso.put("x", String.valueOf(pesoOB.getDatosPeso().size()));
        datosPeso.put("y", peso);
        pesoOB.getDatosPeso().add(datosPeso);
        System.out.println(pesoOB.getDatosPeso().size());
        return pesoOB;
    }

    /**
     * Actualiza la lista de pesos en el Firebase.
     * @param peso
     */
    public void actualizarPeso(Peso peso){
        //guardo la fecha de hoy
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        String fecha = date.getDate() +"/"+ date.getMonth() +"/"+ date.getYear();

        peso.getFecha().add(fecha);
        peso.setObjetivo("75");

        DatabaseReference ref = FirebaseDatabase
                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("usuarios/"+ MainActivity.getUsuario().getId()+"/peso");
        ref.setValue(peso).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                actualizarUsuario();
                cargarChart();
            }
        });
    }
    public void escribirToast(String texto){
        Toast.makeText(getContext(), texto, Toast.LENGTH_LONG).show();
    }
    public static void actualizarUsuario(){
        System.out.println("Actualizando usuario");
        DatabaseReference ref = FirebaseDatabase.getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("usuarios/"+MainActivity.getUsuario().getId());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MainActivity.setUsuario(dataSnapshot.getValue(Usuario.class));
                MainActivity.setPeso(dataSnapshot.child("peso").getValue(Peso.class));
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    /**
     * Configura el chart y carga sus contenidos. Obtiene los datos de un objeto usuario.
     */
    public void cargarChart(){
        System.out.println("Cargando chart: método");
        //leyenda
        String legendName[] = {"Objetivo", "Peso"};
        //configurar descripción
        Description description = new Description();
        description.setText("Seguimiento de peso");
        description.setTextSize(14f);
        description.setPosition(360f, 25f);
        binding.lineChart.setDescription(description);
        binding.lineChart.setDrawBorders(false);
        //estilando chart
        binding.lineChart.getAxisRight().setDrawLabels(false);
        binding.lineChart.getAxisRight().setDrawGridLines(false);
        binding.lineChart.getAxisLeft().setDrawLabels(false);
        binding.lineChart.getAxisLeft().setDrawGridLines(false);
        binding.lineChart.getXAxis().setDrawGridLines(false);
        binding.lineChart.setDrawGridBackground(false);
        //insertando fechas en eje X
        XAxis xAxis = binding.lineChart.getXAxis();
        binding.lineChart.getXAxis().setDrawLabels(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        List<String> xValues = new ArrayList<>();
        xValues = pesoOB.getFecha();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
        //linea de objetivo
        YAxis leftAxis = binding.lineChart.getAxisLeft();
        //si se ha seleccionado una marca de objetivo
        if(pesoOB.getObjetivo()!=null){
            LimitLine ll = new LimitLine(Float.valueOf(pesoOB.getObjetivo()), "Objetivo");
            ll.setLineColor(Color.rgb(255,135,0));
            ll.setLineWidth(3f);
            ll.setTextColor(Color.BLACK);
            ll.setTextSize(10f);
            leftAxis.addLimitLine(ll);
        }
        //inserción de datos
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < pesoOB.getDatosPeso().size(); i++) {
            entries.add(new Entry(Float.valueOf(pesoOB.getDatosPeso().get(i).get("x")),Float.valueOf(pesoOB.getDatosPeso().get(i).get("y")) ));
        }
        //configurando los datos visibles en el chart
        LineDataSet lineDataSet = new LineDataSet(entries, "Peso");
        lineDataSet.setColor(Color.GREEN);
        lineDataSet.setValueTextSize(15);
        lineDataSet.setLineWidth(3);
        LineData lineData = new LineData(lineDataSet);
        binding.lineChart.setData(lineData);
        //leyends CREAR AL AÑADIR DATOS
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
        binding.lineChart.setVisibleXRangeMaximum(7f);
        binding.lineChart.moveViewToX(entries.size()-1);
        binding.lineChart.setNoDataText("No se ha guardado ningún dato.");
        binding.lineChart.invalidate();
    }

}