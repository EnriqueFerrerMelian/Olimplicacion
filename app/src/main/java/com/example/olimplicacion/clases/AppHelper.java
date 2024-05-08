package com.example.olimplicacion.clases;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.olimplicacion.MainActivity;
import com.example.olimplicacion.R;
import com.example.olimplicacion.actividades.Actividad;
import com.example.olimplicacion.databinding.FragmentDetallesActividadBinding;
import com.example.olimplicacion.databinding.FragmentEstadisticasBinding;
import com.example.olimplicacion.fragmentos.EstadisticasFragment;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.github.mikephil.charting.data.BarData;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AppHelper {
    private static Uri imgUriFb = Uri.parse(" ");


    /**
     * Inserta un toast con el texto deseado.
     * @param texto Texto introducido por el usuario
     * @param context Contexto del fragmento o actividad donde debe visualizarse
     */
    public static void escribirToast(String texto, Context context){
        Toast.makeText(context, texto, Toast.LENGTH_LONG).show();
    }
    /**
     * Actualiza los objetos usuario y peso de la aplicación con los de FireBase.
     * Cuando se actualizan se vuelve a cargar el gráfico
     */
    public static void actualizarApp(){
        DatabaseReference ref = FirebaseDatabase
                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("usuarios/"+MainActivity.getUsuario().getId());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MainActivity.setUsuario(dataSnapshot.getValue(Usuario.class));

                Peso peso = dataSnapshot.child("peso")
                        .getValue(Peso.class)==null ? new Peso() : dataSnapshot.child("peso").getValue(Peso.class);
                MainActivity.setPeso(peso);

                Avance avance = dataSnapshot.child("avance")
                        .getValue(Avance.class)==null ? new Avance() : dataSnapshot.child("avance").getValue(Avance.class);
                MainActivity.setAvance(avance);

                List<Actividad> actividades = new ArrayList<>();
                if(dataSnapshot.child("actividades").getValue()!=null){
                    for (DataSnapshot data: dataSnapshot.child("actividades").getChildren()) {
                        Actividad actividad = data.getValue(Actividad.class);
                        actividades.add(actividad);
                    }
                }
                MainActivity.setActividades(actividades);
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
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
    public static Peso addDatos(String peso, String objetivo){
        //Obtengo la fecha de hoy
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        String fecha = date.getDate() +"/"+ date.getMonth();

        //creo un mapa que guarde los ejes
        Map<String, String> datosPeso = new HashMap<>();
        datosPeso.put("x", String.valueOf(MainActivity.getPeso().getDatosPeso().size()+1));
        datosPeso.put("y", peso);

        MainActivity.getPeso().getFecha().add(fecha);
        MainActivity.getPeso().getDatosPeso().add(datosPeso);
        MainActivity.getPeso().setObjetivo(objetivo);
        return MainActivity.getPeso();
    }

    /**
     * Configura la apariencia por defecto del chart y carga los datos del objeto Peso
     */
    public static void configurarChartPeso(FragmentEstadisticasBinding binding){
        float maxView = 0;float minView = 0;

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
        final List<String> fechas = MainActivity.getPeso().getFecha();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(fechas));

        //inserción de entradas
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < MainActivity.getPeso().getDatosPeso().size(); i++) {
            entries.add(new Entry((float) i,Float.parseFloat(Objects.requireNonNull(MainActivity.getPeso().getDatosPeso().get(i).get("y")))));
            if(Float.parseFloat(Objects.requireNonNull(MainActivity.getPeso().getDatosPeso().get(i).get("y")))>maxView){
                maxView = Float.parseFloat(Objects.requireNonNull(MainActivity.getPeso().getDatosPeso().get(i).get("y")));
            }
            if(Float.parseFloat(Objects.requireNonNull(MainActivity.getPeso().getDatosPeso().get(i).get("y")))<minView){
                minView = Float.parseFloat(Objects.requireNonNull(MainActivity.getPeso().getDatosPeso().get(i).get("y")));
            }

        }
        LineDataSet lineDataSet = new LineDataSet(entries, "Peso");
        lineDataSet.setColor(Color.GREEN);
        lineDataSet.setValueTextSize(15);
        lineDataSet.setLineWidth(3);
        LineData lineData = new LineData(lineDataSet);
        if(MainActivity.getPeso().getDatosPeso().size()>0){
            binding.ultimoPeso.setText(MainActivity.getPeso().getDatosPeso().get(MainActivity.getPeso().getDatosPeso().size()-1).get("y") + " Kgs");
        }
        //>>>****INSERCIÓN DE DATOS*****FIN

        //si se ha seleccionado una marca de objetivo
        if(MainActivity.getPeso().getObjetivo()!=null){
            float leyendVal =Float.parseFloat(MainActivity.getPeso().getObjetivo());
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
        String ultimoValorIntroducido = MainActivity.getAvance().getEjerciciosNombres()
                .isEmpty() ? " " : MainActivity.getAvance()
                .getEjerciciosNombres().get(MainActivity.getAvance().getEjerciciosNombres().size()-1);
        binding.ultimoProgreso.setText(ultimoValorIntroducido);

        //>>>****INSERCIÓN DE DATOS*****fin

        binding.barChart.setDescription(description);
        binding.barChart.canScrollHorizontally(1);

        binding.barChart.setNoDataText("No se ha guardado ningún dato.");
        binding.barChart.moveViewToX(entries.size()-1);
        binding.barChart.setData(barData);
        binding.barChart.invalidate();
        binding.barChart.setVisibleXRangeMaximum(5);
    }

    public static void actualizarAvance(Avance avance){
        DatabaseReference ref = FirebaseDatabase
                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("usuarios/"+ MainActivity.getUsuario().getId()+"/avance");
        ref.setValue(avance).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                actualizarApp();
                configurarChartAvance(EstadisticasFragment.getBinding());
            }
        });
    }
    /**
     * Actualiza los datos del peso del usuario en Firebase.
     * @param peso
     */
    public static void actualizarPeso(Peso peso){
        DatabaseReference ref = FirebaseDatabase
                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("usuarios/"+ MainActivity.getUsuario().getId()+"/peso");
        ref.setValue(peso).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                actualizarApp();
                configurarChartPeso(EstadisticasFragment.getBinding());
            }
        });
    }
    /**
     * Recoge una lista de nombres, los separa en un array por segmentos e introduce el primer
     * segmento en una lista nueva
     * @param nombres Lista de nombres introducida
     * @return Lista de la primera palabra de cada nombre.
     */
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

    // ACTIVIDADES**********************************ACTIVIDADES**********************************
    public static void cargarActividad(FragmentDetallesActividadBinding binding,Context context, Actividad actividad){
        if (actividad.getDias().contains("l")) {
            binding.lunes.setTextColor(Color.rgb(255, 127, 39));
        }
        if (actividad.getDias().contains("m")) {
            binding.martes.setTextColor(Color.rgb(255, 127, 39));
        }
        if (actividad.getDias().contains("x")) {
            binding.miercoles.setTextColor(Color.rgb(255, 127, 39));
        }
        if (actividad.getDias().contains("j")) {
            binding.jueves.setTextColor(Color.rgb(255, 127, 39));
        }
        if (actividad.getDias().contains("v")) {
            binding.viernes.setTextColor(Color.rgb(255, 127, 39));
        }
        if (actividad.getDias().contains("s")) {
            binding.sabado.setTextColor(Color.rgb(255, 127, 39));
        }
        if (actividad.getDias().contains("d")) {
            binding.domingo.setTextColor(Color.rgb(255, 127, 39));
        }
        Glide.with(context)
                .load(actividad.getImg2())
                .placeholder(R.drawable.baseline_add_242)//si no hay imagen carga una por defecto
                .error(R.drawable.logo)//si ocurre algún error se verá por defecto
                .fitCenter()
                .override(1000)
                .into(binding.imagenActividad);
        binding.nombreActividad.setText(actividad.getNombre());
        binding.descripcionActividad.append(actividad.getDescripcion());
        binding.profesorActividad.append(actividad.getProfesor());
        binding.horariosActividad.append(actividad.getHorario());
        binding.vacantesActividad.append(actividad.getVacantes());
        binding.precioActividad.append(actividad.getPrecio());
    }

    public static void reservarActividad(Actividad actividad){
        //si hay vacantes
        //Obtengo la fecha de hoy
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        String fecha = date.getYear() +"/"+ date.getMonth()+"/"+ date.getDate()+"";
        int vacantes = Integer.parseInt(actividad.getVacantes())-1;
        actividad.setVacantes(String.valueOf(vacantes));
        actividad.setFecha(fecha);
        DatabaseReference ref = FirebaseDatabase
                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("usuarios/"+ MainActivity.getUsuario().getId()+"/actividades/"+actividad.getNombre());
        ref.setValue(actividad).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                actualizarVacante(actividad);
            }
        });
    }
    public static void eliminarReserva(Actividad actividad){
        int vacantes = Integer.parseInt(actividad.getVacantes())+1;
        actividad.setVacantes(String.valueOf(vacantes));
        DatabaseReference ref = FirebaseDatabase
                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("usuarios/"+ MainActivity.getUsuario().getId()+"/actividades/"+actividad.getNombre());
        ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                actualizarVacante(actividad);
            }
        });
    }
    public static void actualizarVacante(Actividad actividad){
        DatabaseReference ref1 = FirebaseDatabase
                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("actividades/"+actividad.getNombre());
        ref1.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                DatabaseReference ref2 = FirebaseDatabase
                        .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                        .getReference("actividades/"+actividad.getNombre());
                ref2.setValue(actividad).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        actualizarApp();
                    }
                });
            }
        });


    }

    // ACTIVIDADES**********************************ACTIVIDADES**********************************
    /**
     * get uri to drawable or any other resource type if u wish
     * @param context - context
     * @return - uri
     */
    public static void hotFixImagen(Context context, int drawable){//int corresponde a un drawable
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), drawable);
        Uri imgUri = getImageUri(context, bm);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(String.valueOf(drawable));
        storageReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //si no hay problemas durante el proceso obtenemos el link de la imagen en el Store
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlimagen = uriTask.getResult();
                imgUriFb = urlimagen;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e);
            }
        });
    }

    public static void hotFixAvtividad(){
        Actividad actividad = new Actividad("Esgrima", "20.00€",
                " es un deporte de combate en el que se enfrentan dos contrincantes debidamente protegidos que deben intentar tocarse con un arma blanca, en función de la cual se diferencian tres modalidades: sable, espada y florete.",
                "10", "Cirano de Vergerac", "De 8:30 a 9:30");
        actividad.setImg1("https://firebasestorage.googleapis.com/v0/b/olimplicacion-3ba86.appspot.com/o/2131165449?alt=media&token=212d3b2f-e1f8-4876-b1ee-42008c1cda5a");
        actividad.setImg2("https://firebasestorage.googleapis.com/v0/b/olimplicacion-3ba86.appspot.com/o/2131165448?alt=media&token=2db03e43-bd07-4aaf-bf29-f9360660280a");
        List<String> dias = new ArrayList<>();dias.add("m");dias.add("j");
        actividad.setDias(dias);
        Map<String, Object> actividadMapa = new HashMap<>();
        actividadMapa.put("esgrima", actividad);
        DatabaseReference ref = FirebaseDatabase
                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("actividades");
        ref.updateChildren(actividadMapa).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Actividad actividad = new Actividad("Kárate", "15.00€",
                        " es un arte marcial tradicional basada en algunos estilos de las artes marciales chinas y en otras disciplinas provenientes de Okinawa. A la persona que lo practica se la llama karateca.",
                        "15", "Andrés", "De 6:30 a 7:30");
                actividad.setImg1("https://firebasestorage.googleapis.com/v0/b/olimplicacion-3ba86.appspot.com/o/2131165450?alt=media&token=6cfbbc04-9029-41bc-987b-59e08fc92cd6");
                actividad.setImg2("https://firebasestorage.googleapis.com/v0/b/olimplicacion-3ba86.appspot.com/o/2131165452?alt=media&token=89be4d90-e92b-4a7f-abcc-9c975517b76a");
                List<String> dias = new ArrayList<>();dias.add("m");dias.add("j");dias.add("s");
                actividad.setDias(dias);
                Map<String, Object> actividadMapa = new HashMap<>();
                actividadMapa.put("karate", actividad);
                ref.updateChildren(actividadMapa).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Actividad actividad = new Actividad("Zumba", "10.00€",
                                " es una disciplina enfocada por una parte a mantener un cuerpo saludable y por otra a desarrollar, fortalecer y dar flexibilidad al cuerpo mediante movimientos de baile combinados con una serie de rutinas aeróbicas.",
                                "15", "Señora Profesora", "De 7:30 a 8:30");
                        actividad.setImg1("https://firebasestorage.googleapis.com/v0/b/olimplicacion-3ba86.appspot.com/o/2131165449?alt=media&token=212d3b2f-e1f8-4876-b1ee-42008c1cda5a");
                        actividad.setImg2("https://firebasestorage.googleapis.com/v0/b/olimplicacion-3ba86.appspot.com/o/2131165448?alt=media&token=2db03e43-bd07-4aaf-bf29-f9360660280a");
                        List<String> dias = new ArrayList<>();dias.add("l");dias.add("x");dias.add("v");
                        actividad.setDias(dias);
                        Map<String, Object> actividadMapa = new HashMap<>();
                        actividadMapa.put("zumba", actividad);
                        ref.updateChildren(actividadMapa);
                    }
                });
            }
        });
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
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        Date fecha = new Date();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title" + fecha.toString(), null);
        return Uri.parse(path);
    }
}
