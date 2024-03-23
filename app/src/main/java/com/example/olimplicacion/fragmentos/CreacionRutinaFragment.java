package com.example.olimplicacion.fragmentos;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.olimplicacion.DetailedActivity01;
import com.example.olimplicacion.MainActivity;
import com.example.olimplicacion.MenuPrincipal;
import com.example.olimplicacion.R;
import com.example.olimplicacion.clases.Ejercicio;
import com.example.olimplicacion.clases.EjercicioAdapter;
import com.example.olimplicacion.clases.Rutina;
import com.example.olimplicacion.databinding.FragmentCreacionRutinaBinding;
import com.example.olimplicacion.fragmentosDetalle.DetallesRutinaFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

/**
 * Desde aquí se manejará la creación de rutinas
 * EN FASE DE CONSTRUCCION
 */
public class CreacionRutinaFragment extends Fragment  implements EjercicioAdapter.ViewHolder.ItemClickListener{

    //firebase Satorage
    private StorageReference storageReference;
    private static Uri imgUriFb = Uri.parse(" ");//contiendrá el link de la imagen en el Store de firebase
    //firebase Satorage

    //recyclerView
    private RecyclerView recyclerView;//lista del xml
    private EjercicioAdapter ejercicioAdapter;//adaptador
    private static List<Ejercicio> dataArrayList = new ArrayList<>();
    //recyclerView fin

    //obtencion de imágenes

    private static Uri imgUri = Uri.parse(" ");//contiene las imagenes de galeria y camara durante su administración
    private ActivityResultLauncher<Intent> camaraLauncher;
    private ActivityResultLauncher<Intent> galeriaLauncher;
    //obtencion de imágenes

    private static FragmentCreacionRutinaBinding binding;
    private static Rutina rutina;

    public CreacionRutinaFragment() {
        // Required empty public constructor
    }
    public static CreacionRutinaFragment newInstance(Rutina rutinaF) {
        System.out.println("Se pasa la rutina");
        CreacionRutinaFragment fragment = new CreacionRutinaFragment();
        rutina = rutinaF;
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreacionRutinaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //si el fragmento se ejecuta pasándole un objeto Rutina, este cargará los datos en el fragmento.
        if(rutina!=null){
            dataArrayList=rutina.getEjercicios();
            Glide.with(getContext())
                    .load(rutina.getImg())
                    .placeholder(R.drawable.baseline_add_24)//si no hay imagen carga una por defecto
                    .circleCrop()
                    .error(R.drawable.baseline_add_24)//si ocurre algún error se verá por defecto
                    .into(binding.editarImagen);
            //inserto el nombre
            binding.nombreDeRutina.setText(rutina.getNombre());
            //cambio el color de los días seleccionados
            if(rutina.getDias().contains("l")){
                binding.lunes.setChecked(true);
            }if(rutina.getDias().contains("m")){
                binding.martes.setChecked(true);
            }if(rutina.getDias().contains("x")){
                binding.miercoles.setChecked(true);
            }if(rutina.getDias().contains("j")){
                binding.jueves.setChecked(true);
            }if(rutina.getDias().contains("v")){
                binding.viernes.setChecked(true);
            }if(rutina.getDias().contains("s")){
                binding.sabado.setChecked(true);
            }if(rutina.getDias().contains("d")){
                binding.domingo.setChecked(true);
            }
        }
        super.onViewCreated(view, savedInstanceState);
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ejercicioAdapter = new EjercicioAdapter(dataArrayList, this);
        recyclerView.setAdapter(ejercicioAdapter);
        cameraLauncher();// Inicializar el ActivityResultLauncher de la camara
        galleryLauncher();// Inicializar el ActivityResultLauncher, de la galeria

        /**
         * abre el fragmento EjercicioListaFragment para seleccionar un ejercicio a añadir
         */
        binding.editarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottonSheet();
            }
        });

        //al añadir ejercicio no se suma a la lista
        binding.anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reemplazarFragmento(new ListaEjerciciosFragment());
            }
        });
        binding.aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarRutina();
                reemplazarFragmento(new RutinaFragment());
            }
        });
        binding.cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rutina = null;
                dataArrayList = new ArrayList<>();
                getParentFragmentManager().popBackStack();
            }
        });
    }


    public void reemplazarFragmento(Fragment fragmento){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragmento).addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onItemClick(Ejercicio ejercicio) {}

    public void showBottonSheet(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.my_botton_sheet);
        LinearLayout galeriaLayout = dialog.findViewById(R.id.galeriaLayout);
        LinearLayout camaraLayout = dialog.findViewById(R.id.camaraLayout);
        Button aceptar = dialog.findViewById(R.id.aceptar);
        galeriaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galeriaLauncher.launch(intent);
                dialog.dismiss();
            }
        });

        camaraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) { // Si no tiene permiso
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 225); // Solicitar permiso
                } else {
                    // Crear un Intent para abrir la cámara
                    Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Iniciar el ActivityResultLauncher para la cámara
                    camaraLauncher.launch(camaraIntent);
                    dialog.dismiss();
                }
            }
        });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Metodo de aceptar aquí");
                //metodoprueba
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    public void cameraLauncher(){
        //obtendra la imagen como Bitmap por defecto
        camaraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        //capturamos imagen
                        Intent data = result.getData();
                        if (data != null) {
                            // Obtener la imagen como un bitmap
                            Bundle extras = data.getExtras();
                            if (extras != null) {

                                Bitmap bitmap = (Bitmap) extras.get("data");
                                Glide.with(getContext())
                                        .load(bitmap)
                                        .placeholder(R.drawable.iconogris)//si no hay imagen carga una por defecto
                                        .circleCrop()
                                        .error(R.drawable.iconogris)//si ocurre algún error se verá por defecto
                                        .into(binding.editarImagen);
                                imgUri = getImageUri(getContext(), bitmap);
                            } else {
                                // Si no hay datos extras, utilizar la Uri para cargar la imagen
                            }
                        }
                    }
                });
    }
    public void galleryLauncher(){
        //obtendra la imagen como Uri por defecto
        galeriaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        //capturamos imagen
                        Intent data = result.getData();

                        if (data != null) {
                            // Obtener la imagen como un bitmap
                            Bundle extras = data.getExtras();
                            if (extras != null) {
                                Bitmap bitmap = (Bitmap) extras.get("data");
                            } else {
                                // Si no hay datos extras, utilizar la Uri para cargar la imagen
                                        Glide.with(getContext())
                                        .load(data.getData())
                                        .placeholder(R.drawable.iconogris)//si no hay imagen carga una por defecto
                                        .circleCrop()
                                        .error(R.drawable.iconogris)//si ocurre algún error se verá por defecto
                                        .into(binding.editarImagen);
                                imgUri = data.getData();
                            }
                        }
                    }
                }
        );
    }

    /**
     * Este método guarda los datos de la rutina nueva creada por el usuario. Dentro de esa rutina, una lista de
     * ejercicios en los que se incluyen dos datos más: 'pesos' y 'repeticiones y veces'.
     * @param fecha
     */
    public void guardarRutinaEnRealtime(String fecha){
        //*******************************************************
        Map<String , Object> rutina = new HashMap<>();
        rutina.put("nombre", binding.nombreDeRutina.getText().toString());
        rutina.put("id", MainActivity.getUsuario().getId()+"_"+fecha);
        rutina.put("img", imgUriFb.toString());
        List<String> dias = new ArrayList<>();
        if(binding.lunes.isChecked()){
            dias.add("l");
        }if(binding.martes.isChecked()){
            dias.add("m");
        }if(binding.miercoles.isChecked()){
            dias.add("x");
        }if(binding.jueves.isChecked()){
            dias.add("j");
        }if(binding.viernes.isChecked()){
            dias.add("v");
        }if(binding.sabado.isChecked()){
            dias.add("s");
        }if(binding.domingo.isChecked()){
            dias.add("d");
        }
        rutina.put("dias", dias);
        List<Ejercicio> ejercicios = dataArrayList;
        rutina.put("ejercicios", ejercicios);

        FirebaseDatabase
                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("rutinas")
                .push()
                .setValue(rutina);
        //*******************************************************
        //Actualizo el usuario con la nueva rutina
        FirebaseDatabase
                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("usuarios/"
                        +MainActivity.getUsuario().getId()+"/rutinas")
                .push()
                .setValue(MainActivity.getUsuario().getId()+"_"+fecha);
    }

    /**
     * Este método guarda una imegen en formato Uri al Store de firebase
     */
    public void guardarRutina(){
        //guardo la fecha de hoy
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        String fecha = date.toString();

        //creamos una referencia en el Store
        storageReference = FirebaseStorage.getInstance().getReference("usuario_"+MainActivity.getUsuario().getId()
                +";fecha_"+fecha);
        //subimos la imagen y recojo el link
        storageReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //si no hay problemas durante el proceso obtenemos la uri que usa el store para guardar la imagen
                Task<Uri>uriTask= taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri urlimagen = uriTask.getResult();
                imgUriFb = urlimagen;
                guardarRutinaEnRealtime(fecha);
            }
        });
    }

    public static void addToDataList(Ejercicio ejercicio, Context contexto){
        boolean join = true;
        for (Ejercicio dato: dataArrayList) {
            if(dato.getId()== ejercicio.getId()){
                join=false;
            }
        }
        if(join){
            dataArrayList.add(ejercicio);
        }else{
            Toast.makeText(contexto, "Ya estába en la lista de ejercicios.",Toast.LENGTH_LONG).show();
        }
    }
    /**
     * Este método convierte una imagen Bitmap a tipo Uri
     * @param inContext
     * @param inImage
     * @return
     */
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        //After delete that image from our image gallery, it starts returning null.
        //added a date with the title and description of bitmap
        Date fecha = new Date();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title"+fecha.toString(), null);
        return Uri.parse(path);
    }
}