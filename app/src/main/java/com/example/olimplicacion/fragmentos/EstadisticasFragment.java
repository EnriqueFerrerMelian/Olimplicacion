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
import android.widget.TextView;

import com.example.olimplicacion.MainActivity;
import com.example.olimplicacion.R;
import com.example.olimplicacion.clases.AppHelper;
import com.example.olimplicacion.clases.Avance;
import com.example.olimplicacion.clases.Peso;
import com.example.olimplicacion.databinding.FragmentEstadisticasBinding;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

public class EstadisticasFragment extends Fragment {
    private static FragmentEstadisticasBinding binding;
   // private static Peso pesoOB = MainActivity.getPeso();
    //private static Avance avanceOB = MainActivity.getAvance();
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
        if(MainActivity.getPeso()==null){
            MainActivity.setPeso(new Peso());
        }else{
            binding.textoInfo.setVisibility(View.GONE);
        }
        if(MainActivity.getAvance()==null){
            MainActivity.setAvance(new Avance());
        }
        AppHelper.configurarChartPeso(binding);
        AppHelper.configurarChartAvance(binding);

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
                pesoSeleccionado = e;
            }
            @Override
            public void onNothingSelected() {}
        });
        binding.barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                progresoSeleccionado = (BarEntry) e;
            }

            @Override
            public void onNothingSelected() {}
        });
        binding.eliminarPeso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEliminarElemento("¿Desehas eliminar ese registro?", true);
            }
        });
        binding.eliminarAvance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEliminarElemento("¿Desehas eliminar ese progreso?", false);
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
        Button cancel = dialog.findViewById(R.id.cancel);
        //inicialización de numberPicker**************************
        NumberPicker numberPesoOb = dialog.findViewById(R.id.numberPesoOb);
        numberPesoOb.setMinValue(0);numberPesoOb.setMaxValue(150);
        NumberPicker numberPesoDecimalOb = dialog.findViewById(R.id.numberPesoDecimalOb);
        numberPesoDecimalOb.setMinValue(0);numberPesoDecimalOb.setMaxValue(9);
        if(MainActivity.getPeso().getObjetivo()!=null){
            String[] objetivo = MainActivity.getPeso().getObjetivo().split("\\.");
            numberPesoOb.setValue(Integer.valueOf(objetivo[0]));
            numberPesoDecimalOb.setValue(Integer.valueOf(objetivo[1]));
        }
        NumberPicker numberPeso = dialog.findViewById(R.id.numberPeso);
        numberPeso.setMinValue(1);numberPeso.setMaxValue(150);
        NumberPicker numberPesoDecimal = dialog.findViewById(R.id.numberPesoDecimal);
        numberPesoDecimal.setMinValue(0);numberPesoDecimal.setMaxValue(9);
        if(MainActivity.getPeso().getDatosPeso().size()>0){
            String[] objetivo = MainActivity.getPeso().getDatosPeso().get(MainActivity.getPeso().getDatosPeso().size()-1).get("y").split("\\.");
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
                AppHelper.actualizarPeso(AppHelper.addDatos(pesoOut, objetivoOut));
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    /**
     * Abre un cuadro de diálogo de confirmación para confirmar si se elimina el elemento seleccionado.
     * En función del elemento eliminado (peso o progreso), se introduce un texto adecuado y true para
     * eliminar un peso seleccionado o false para un progreso seleccionado.
     * @param textoInput Texto que se mostrará en el cuadro de diálogo
     * @param esPeso True o false para peso seleccionado o progreso respectivamente.
     */
    public void showEliminarElemento(String textoInput, boolean esPeso) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.my_bottom_sheet_sobrescribir_rutina);
        TextView texto = dialog.findViewById(R.id.textView2);
        texto.setText(textoInput);
        Button si = dialog.findViewById(R.id.si);
        Button no = dialog.findViewById(R.id.no);
        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(esPeso){
                    if(binding.lineChart.getLineData().getDataSets().get(0).getEntryCount()>1){
                        System.out.println("Borrando");
                        int index = binding.lineChart.getLineData().getDataSetByIndex(0).getEntryIndex(pesoSeleccionado);
                        MainActivity.getPeso().getDatosPeso().remove(index);
                        MainActivity.getPeso().getFecha().remove(index);
                        AppHelper.actualizarPeso(MainActivity.getPeso());
                    }else{
                        AppHelper.escribirToast("Debe haber al menos un registro", getContext());
                    }
                }else{
                    if(binding.barChart.getBarData().getDataSets().get(0).getEntryCount()>0){
                        int index = binding.barChart.getBarData().getDataSetByIndex(0).getEntryIndex(progresoSeleccionado);
                        MainActivity.getAvance().getEjerciciosNombres().remove(index);
                        MainActivity.getAvance().getPesos().remove(index);
                        AppHelper.actualizarAvance(MainActivity.getAvance());
                    }
                }
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    /**
     * @return El objeto binding del fragmento.
     */
    public static FragmentEstadisticasBinding getBinding(){
        return EstadisticasFragment.binding;
    }

    /**
     * Actualiza los datos del peso, que luego se pasarán a la base de datos.
     *   Peso seleccionado, transformado en string.
     *    seleccionado, transformado en string.
     * @return Objeto Peso con los datos actualizados
     */
    /*public Peso addDatos(String peso, String objetivo){
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
    }*/

    /**
     * Actualiza los datos del peso del usuario en Firebase.
     * @param peso
     *//*
    public void actualizarPeso(Peso peso){
        DatabaseReference ref = FirebaseDatabase
                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("usuarios/"+ MainActivity.getUsuario().getId()+"/peso");
        ref.setValue(peso).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                AppHelper.actualizarApp();
            }
        });
    }*/

    /**
     * Actualiza los objetos usuario y peso de la aplicación con los de FireBase.
     * Cuando se actualizan se vuelve a cargar el gráfico
     *//*
    public static void actualizarUsuario(){
        DatabaseReference ref = FirebaseDatabase.getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("usuarios/"+MainActivity.getUsuario().getId());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MainActivity.setUsuario(dataSnapshot.getValue(Usuario.class));
                MainActivity.setPeso(dataSnapshot.child("peso").getValue(Peso.class));
                AppHelper.configurarChartPeso(binding);
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }*/
    /*public static void actualizarAvance(Avance avance){
        System.out.println("actualizarAvance()");
        DatabaseReference ref = FirebaseDatabase
                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("usuarios/"+ MainActivity.getUsuario().getId()+"/avance");
        ref.setValue(avance).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                actualizarUsuario();
                AppHelper.configurarChartAvance(binding);
            }
        });
    }*/
}