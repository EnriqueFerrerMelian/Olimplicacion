package com.example.olimplicacion;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.olimplicacion.actividades.ActividadesFragment;
import com.example.olimplicacion.databinding.ActivityMenuPrincipalBinding;
import com.example.olimplicacion.calendario.CalendarioFragment;
import com.example.olimplicacion.fragmentos.EstadisticasFragment;
import com.example.olimplicacion.fragmentos.PerfilFragment;
import com.example.olimplicacion.rutinas.RutinaFragment;
import com.example.olimplicacion.tablon.TablonFragment;

public class MenuPrincipal extends AppCompatActivity {
    private static Fragment fragmentoDesechable = null;//se usa para controlar la transacción entre fragmentos
    private static ActivityMenuPrincipalBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuPrincipalBinding.inflate(getLayoutInflater());//carga la vista xml al cargar la aplicación
        setContentView(binding.getRoot());
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
        if(item.getItemId()==R.id.perfil){
            reemplazarFragmento(new PerfilFragment());
        }
        if(item.getItemId()==R.id.tablon){
            reemplazarFragmento(new TablonFragment());
        }
        if(item.getItemId()==16908332){
            getSupportFragmentManager().popBackStack();
        }
        //return true;
        return super.onOptionsItemSelected(item);
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
        }
        fragmentTransaction.replace(R.id.fragmentContainerView, fragmento);
        fragmentoDesechable = fragmento;
        fragmentTransaction.commit();
    }

    public static ActivityMenuPrincipalBinding getBinding(){
        return binding;
    }
}