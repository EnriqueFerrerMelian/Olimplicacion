package com.example.olimplicacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.olimplicacion.databinding.ActivityMenuPrincipalBinding;
import com.example.olimplicacion.fragmentos.ActividadesFragment;
import com.example.olimplicacion.fragmentos.CalendarioFragment;
import com.example.olimplicacion.fragmentos.EstadisticasFragment;
import com.example.olimplicacion.fragmentos.PerfilFragment;
import com.example.olimplicacion.fragmentos.RutinaFragment;

public class MenuPrincipal extends AppCompatActivity {

    //ATRIBUTOS
    private static Fragment fragmentoDesechable = null;

    //METODOS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMenuPrincipalBinding binding;
        binding = ActivityMenuPrincipalBinding.inflate(getLayoutInflater());//carga la vista xml al cargar la aplicación
        setContentView(binding.getRoot());

        //declaracion de variables
        setSupportActionBar(binding.toolbar);

        //codigo
        reemplazarFragmento(new RutinaFragment());

        //listeners
        binding.bottomnav.setOnItemSelectedListener(item ->{
            if(item.getItemId()==R.id.ejercicio){
                reemplazarFragmento(new RutinaFragment());
            }
            if(item.getItemId()==R.id.estadisticas){
                reemplazarFragmento(new EstadisticasFragment());
            }
            if(item.getItemId()==R.id.actividades){
                reemplazarFragmento(new ActividadesFragment());
            }
            if(item.getItemId()==R.id.calendario){
                reemplazarFragmento(new CalendarioFragment());
            }
            return true;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.perfil){
            reemplazarFragmento(new PerfilFragment());
            Toast.makeText(this, "Estas en la opción Perfil", Toast.LENGTH_LONG).show();
        }
        if(id == R.id.configuracion){
            Toast.makeText(this, "Estas en la opción Configuracion", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    /**
     * Reemplaza el fragmento en el contenedor 'fragmentContainerView' por el pasado por
     * parámetro
     * @param fragmento
     */
    public void reemplazarFragmento(Fragment fragmento){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(fragmentoDesechable!=null){
            fragmentTransaction.remove(fragmentoDesechable);
            System.out.println(fragmentoDesechable + "eliminado");
        }
        fragmentTransaction.replace(R.id.fragmentContainerView, fragmento);
        fragmentoDesechable = fragmento;
        fragmentTransaction.commit();
    }
}