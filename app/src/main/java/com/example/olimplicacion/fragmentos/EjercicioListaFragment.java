package com.example.olimplicacion.fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.olimplicacion.DetailedActivity01;
import com.example.olimplicacion.adaptadores.ListAdapter;
import com.example.olimplicacion.clases.Ejercicio;
import com.example.olimplicacion.R;
import com.example.olimplicacion.databinding.FragmentEjercicioListaBinding;
import com.example.olimplicacion.db.DataBaseManager;

import java.util.ArrayList;

public class EjercicioListaFragment extends Fragment {
    FragmentEjercicioListaBinding binding;
    ListAdapter listAdapter;
    ArrayList<Ejercicio> dataArrayList = new ArrayList<>();
    Ejercicio ejercicio;
    DataBaseManager dbm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("07- creando binding en lista de ejercicios");
        binding = FragmentEjercicioListaBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbm = new DataBaseManager(getContext());
        System.out.println("08- creando listas con datos");
        //rellenamos listas con los datos
        int [] nameList = {R.string.goblet_squat,R.string.press_arnold, R.string.levantamiento_a_dos_tiempos, R.string.renegade_row, R.string.paseo_del_granjero, R.string.apertura_con_mancuernas, R.string.remo_inclinado, R.string.uppercut_con_mancuernas, R.string.peso_muerto_con_mancuernas_a_dos_brazos_con_piernas_rigidas, R.string.kickbacks_con_mancuernas, R.string.swing_con_un_brazo, R.string.press_de_banca_con_mancuernas, R.string.press_de_hombros_con_mancuernas, R.string.curl_martillo_de_biceps_cruzado, R.string.goblet_squat, R.string.remo_vertical_con_mancuernas, R.string.step_ups, R.string.spider_curl_con_mancuernas, R.string.elevacion_con_mancuernas, R.string.aperturas_con_mancuernas_con_brazos_flexionados, R.string.zancadas_con_mancuernas, R.string.levantamiento_de_peso_muerto_con_mancuernas_a_una_pierna, R.string.elevacion_de_hombros_con_una_sola_mancuerna,R.string.elevacion_de_pantorrillas_con_mancuernas, R.string.hollow_body_skullcrushers, R.string.sentadilla_lateral_con_mancuernas, R.string.peso_muerto_rumano_con_mancuernas, R.string.fila_renegada_con_mancuernas};
        int [] musculosList = {R.string.goblet_squatM,R.string.press_arnoldM, R.string.levantamiento_a_dos_tiemposM, R.string.renegade_rowM, R.string.paseo_del_granjeroM, R.string.apertura_con_mancuernasM, R.string.remo_inclinadoM, R.string.uppercut_con_mancuernasM, R.string.peso_muerto_con_mancuernas_a_dos_brazos_con_piernas_rigidasM, R.string.kickbacks_con_mancuernasM, R.string.swing_con_un_brazoM, R.string.press_de_banca_con_mancuernasM, R.string.press_de_hombros_con_mancuernasM, R.string.curl_martillo_de_biceps_cruzadoM, R.string.goblet_squatM, R.string.remo_vertical_con_mancuernasM, R.string.step_upsM, R.string.spider_curl_con_mancuernasM, R.string.elevacion_con_mancuernasM, R.string.aperturas_con_mancuernas_con_brazos_flexionadosM, R.string.zancadas_con_mancuernasM, R.string.levantamiento_de_peso_muerto_con_mancuernas_a_una_piernaM, R.string.elevacion_de_hombros_con_una_sola_mancuernaM,R.string.elevacion_de_pantorrillas_con_mancuernasM, R.string.hollow_body_skullcrushersM, R.string.sentadilla_lateral_con_mancuernasM, R.string.peso_muerto_rumano_con_mancuernasM, R.string.fila_renegada_con_mancuernasM};
        int [] descList = {R.string.goblet_squatC,R.string.press_arnoldC, R.string.levantamiento_a_dos_tiemposC, R.string.renegade_rowC, R.string.paseo_del_granjeroC, R.string.apertura_con_mancuernasC, R.string.remo_inclinadoC, R.string.uppercut_con_mancuernasC, R.string.peso_muerto_con_mancuernas_a_dos_brazos_con_piernas_rigidasC, R.string.kickbacks_con_mancuernasC, R.string.swing_con_un_brazoC, R.string.press_de_banca_con_mancuernasC, R.string.press_de_hombros_con_mancuernasC, R.string.curl_martillo_de_biceps_cruzadoC, R.string.goblet_squatC, R.string.remo_vertical_con_mancuernasC, R.string.step_upsC, R.string.spider_curl_con_mancuernasC, R.string.elevacion_con_mancuernasC, R.string.aperturas_con_mancuernas_con_brazos_flexionadosC, R.string.zancadas_con_mancuernasC, R.string.levantamiento_de_peso_muerto_con_mancuernas_a_una_piernaC, R.string.elevacion_de_hombros_con_una_sola_mancuernaC,R.string.elevacion_de_pantorrillas_con_mancuernasC, R.string.hollow_body_skullcrushersC, R.string.sentadilla_lateral_con_mancuernasC, R.string.peso_muerto_rumano_con_mancuernasC, R.string.fila_renegada_con_mancuernasC};
        int[] imgList = {R.drawable.goblet_squat, R.drawable.press_arnold, R.drawable.levantamiento_a_dos_tiempos,R.drawable.renegade_row, R.drawable.paseo_del_granjero, R.drawable.apertura_con_mancuernas, R.drawable.remo_inclinado, R.drawable.uppercut_con_mancuernas, R.drawable.peso_muerto_con_mancuernas_a_dos_brazos_con_piernas_rigidas, R.drawable.kickbacks_con_mancuernas, R.drawable.swing_con_un_brazo, R.drawable.press_de_banca_con_mancuernas, R.drawable.press_de_hombros_con_mancuernas, R.drawable.curl_martillo_de_biceps_cruzado, R.drawable.goblet_squat, R.drawable.remo_vertical_con_mancuernas, R.drawable.step_ups, R.drawable.spider_curl_con_mancuernas, R.drawable.elevacion_con_mancuernas, R.drawable.apertura_con_mancuernas, R.drawable.zancadas_con_mancuernas, R.drawable.levantamiento_de_peso_muerto_con_mancuernas_a_una_pierna, R.drawable.elevacion_de_hombros_con_una_sola_mancuerna, R.drawable.elevacion_de_pantorrillas_con_mancuernas,R.drawable.hollow_body_skullcrushers, R.drawable.sentadilla_lateral_con_mancuernas, R.drawable.peso_muerto_rumano_con_mancuernas, R.drawable.peso_muerto_rumano_con_mancuernas};

        System.out.println("08- cargando la lista de ejercicios con los datos de las listas");
        for(int i =0; i<imgList.length;i++){
            //creamos objetos "ejercicio" para rellenar el ListView
            Ejercicio ejercicio = new Ejercicio();
            ejercicio.setName(nameList[i]);ejercicio.setMusculos(musculosList[i]);
            ejercicio.setDesc(descList[i]);ejercicio.setImage(imgList[i]);

            //añado el ejercicio a la lista de ejercicios
            dataArrayList.add(ejercicio);
        }


        System.out.println("09- cargando la listView");
        //rellenamos el ListView
        listAdapter = new ListAdapter(getContext(), dataArrayList);
        binding.listView.setAdapter(listAdapter);
        binding.listView.setClickable(true);
        //al seleccionar un objeto, mandamos los datos de ese objeto al activity detalles para que los muestre.
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), DetailedActivity01.class);

                intent.putExtra("name", nameList[i]);
                intent.putExtra("musculos", musculosList[i]);
                intent.putExtra("desc", descList[i]);
                intent.putExtra("img", imgList[i]);

                startActivity(intent);
            }
        });
    }

}