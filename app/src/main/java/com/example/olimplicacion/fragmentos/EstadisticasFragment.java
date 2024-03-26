package com.example.olimplicacion.fragmentos;

import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.olimplicacion.R;
import com.example.olimplicacion.databinding.FragmentEstadisticasBinding;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EstadisticasFragment extends Fragment {
    FragmentEstadisticasBinding binding;
    private String fecha;
    private List<String> xValuesMes;
    private List<String> xValuesSemana;
    private List<String> xValuesDia;
    private List<Entry> entries1;
    LineDataSet lineDataSet1;
    LineData lineData;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEstadisticasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //declaracion de variables
        Date fechaHoy = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM", Locale.getDefault());
        String formattedDate = df.format(fechaHoy);
        fecha = formattedDate;
        System.out.println("Fecha de hoy:"+fecha);
        Description description = new Description();
        //xValuesMes = Arrays.asList(inicio);//obtendrá los meses
        /*xValuesSemana = Arrays.asList("Enero", "Febrero", "Marzo", "Abril", "MaMayo", "Junio");//nombre de valores en eje X
        xValuesDia = Arrays.asList("Enero", "Febrero", "Marzo", "Abril", "MaMayo", "Junio");//nombre de valores en eje X*/
        xValuesDia = new ArrayList<>();

        XAxis xAxis = binding.chart.getXAxis();
        YAxis yAxis = binding.chart.getAxisLeft();

        //codigo
        description.setText("Seguimiento de peso");
        description.setPosition(250f, 15f);
        binding.chart.setDescription(description);
        binding.chart.getAxisRight().setDrawLabels(false);

        //configuración de eje horizontal
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//posicion de valores del eje X
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValuesDia));
        //xAxis.setLabelCount(10);//divisiones que puede llegar a tomar como max en eje X
        xAxis.setGranularity(1f);

        //Configuración del eje vertical
        /*yAxis.setAxisMaximum(0f);//valor mínimo de Y
        yAxis.setAxisMaximum(100f);//valor máximo de Y DEBE SER EL PESO INICIAL INTRODUCIDO POR EL USUARIO*/
        //yAxis.setAxisLineWidth(4f);//grosor de linea de valores
        yAxis.setDrawLabels(false);
        //yAxis.setAxisLineColor(Color.rgb(255,111,0));//color de línea
        //yAxis.setLabelCount(10);//divide el máximo de valores entre el valor establecido

        entries1 = new ArrayList<>();

        lineDataSet1 = new LineDataSet(entries1, "Peso");
        lineDataSet1.setColor(Color.GREEN);

        lineData = new LineData(lineDataSet1);
        binding.chart.setData(lineData);
        binding.chart.invalidate();

        //listeners
        binding.anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottonDialog();
            }
        });
    }

    private void showBottonDialog(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        //creamos la vista
        View view = LayoutInflater.from(getContext()).inflate(R.layout.my_bottom_sheet_ejercicio, null);

        //configuracin de objetos con binding
        String[] data = {",0", ",1", ",2", ",3", ",4", ",5", ",6", ",7", ",8", ",9"};

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    //recoge los datos de peso del bottonsheetlayout.
    public void recogerData(float valorPeso){
        //inserta la fecha de hoy en la lista de fechas
        xValuesDia.add(fecha);
        //insertamos una entrada con el nuevo peso
        entries1.add(new Entry(entries1.size(),valorPeso));
        System.out.println("Valor de entries1:" + entries1.get(entries1.size()-1));
        lineDataSet1 = new LineDataSet(entries1, "Peso");
    }
}