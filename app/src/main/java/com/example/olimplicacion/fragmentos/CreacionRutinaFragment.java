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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.olimplicacion.MainActivity;
import com.example.olimplicacion.R;
import com.example.olimplicacion.clases.Ejercicio;
import com.example.olimplicacion.clases.EjercicioAdapter;
import com.example.olimplicacion.clases.EjercicioAdapterModificar;
import com.example.olimplicacion.clases.Rutina;
import com.example.olimplicacion.databinding.FragmentCreacionRutinaBinding;
import com.example.olimplicacion.fragmentosDetalle.DetalleEjercicioFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

/**
 * Desde aquí se manejará la creación de rutinas
 * EN FASE DE CONSTRUCCION
 */
public class CreacionRutinaFragment extends Fragment implements EjercicioAdapter.ViewHolder.ItemClickListener {

    //firebase Satorage ******************************
    private StorageReference storageReference;
    private static Uri imgUriFb = Uri.parse(" ");//contiendrá el link de la imagen en el Store de firebase
    //firebase Satorage ***************************fin

    //recyclerView ******************************
    private static RecyclerView recyclerView;
    private static EjercicioAdapterModificar ejercicioAdapterModificar;
    private static List<Ejercicio> dataArrayList = new ArrayList<>();
    //recyclerView ***************************fin

    //obtencion de imágenes ******************************
    private static Uri imgUri = Uri.parse(" ");//contiene las imagenes de galeria y camara durante su administración
    private ActivityResultLauncher<Intent> camaraLauncher;
    private ActivityResultLauncher<Intent> galeriaLauncher;
    //obtencion de imágenes ***************************fin

    //Variables globales
    private static FragmentCreacionRutinaBinding binding;
    private static Rutina rutina;
    private boolean confirmacionImg = false;
    private static int index = 0;
    private static int controlErrores;//por implementar

    public CreacionRutinaFragment() {
        // Required empty public constructor
    }

    public static CreacionRutinaFragment newInstance(Rutina rutinaF) {
        CreacionRutinaFragment fragment = new CreacionRutinaFragment();
        if (rutinaF != null) {
            rutina = rutinaF;
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreacionRutinaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (rutina != null) {
            cargarRutina(rutina);
        }
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ejercicioAdapterModificar = new EjercicioAdapterModificar(dataArrayList, this::onItemClick, rutina);
        recyclerView.setAdapter(ejercicioAdapterModificar);
        cameraLauncher();// Inicializar el ActivityResultLauncher de la camara
        galleryLauncher();// Inicializar el ActivityResultLauncher, de la galeria

        //si el fragmento se ejecuta pasándole un objeto Rutina, este cargará los datos en el fragmento.


        binding.editarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottonSheet();
            }
        });

        /**
         * abre el fragmento EjercicioListaFragment para seleccionar un ejercicio a añadir
         */
        binding.anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reemplazarFragmento(new ListaEjerciciosFragment());
            }
        });


        binding.aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarImagenRutina();
                //si se ha pasado una rutina por parámetro se aliminará de la base de datos
                reemplazarFragmento(new RutinaFragment());
            }
        });
        binding.cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //elimino la rutina y los ejercicios asociados
                rutina = null;
                dataArrayList = new ArrayList<>();
                getParentFragmentManager().popBackStack();
            }
        });
    }

    /**
     * Reemplaza el fragmento en el contenedor 'fragmentContainerView' por el pasado por
     * parámetro
     *
     * @param fragmento
     */
    public void reemplazarFragmento(Fragment fragmento) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragmento).addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onItemClick(Ejercicio ejercicio) {
        Fragment fragment = DetalleEjercicioFragment.newInstance(ejercicio);
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment, "nota").addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Abre un cuadro de diálogo desde donde se seleccionará añadir una imagen desde la galería
     * o desde la cámara
     */
    public void showBottonSheet() {
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

    public void cameraLauncher() {
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
                                confirmacionImg = true;
                            } else {
                                // Si no hay datos extras, utilizar la Uri para cargar la imagen
                            }
                        }
                    }
                });
    }

    public void galleryLauncher() {
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
                                confirmacionImg = true;
                            }
                        }
                    }
                }
        );
    }

    /**
     * Este método guarda los datos de la rutina nueva creada por el usuario. Dentro de esa rutina, una lista de
     * ejercicios en los que se incluyen dos datos más: 'pesos' y 'repeticiones y veces'.
     */
    public void guardarRutinaEnRealtime(String fecha) {
        Map<String, Object> rutinaMap = new HashMap<>();
        rutinaMap.put("nombre", binding.nombreDeRutina.getText().toString());
        rutinaMap.put("id", MainActivity.getUsuario().getId() + "_" + fecha);
        if(confirmacionImg){
            rutinaMap.put("img", imgUriFb.toString());
        }else{
            rutinaMap.put("img", rutina.getImg());
        }
        List<String> dias = new ArrayList<>();
        if (binding.lunes.isChecked()) {
            dias.add("l");
        }
        if (binding.martes.isChecked()) {
            dias.add("m");
        }
        if (binding.miercoles.isChecked()) {
            dias.add("x");
        }
        if (binding.jueves.isChecked()) {
            dias.add("j");
        }
        if (binding.viernes.isChecked()) {
            dias.add("v");
        }
        if (binding.sabado.isChecked()) {
            dias.add("s");
        }
        if (binding.domingo.isChecked()) {
            dias.add("d");
        }
        rutinaMap.put("dias", dias);
        List<Ejercicio> ejerciciosLista = dataArrayList;
        rutinaMap.put("ejercicios", ejerciciosLista);

        FirebaseDatabase
                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference().child("rutinas")
                .push()
                .setValue(rutinaMap);
        //*******************************************************
        //Actualizo el usuario con la nueva rutina
        FirebaseDatabase
                .getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("usuarios/"
                        + MainActivity.getUsuario().getId() + "/rutinas")
                .push()
                .setValue(MainActivity.getUsuario().getId() + "_" + fecha);
        if (rutina != null) {
            eliminarRutina(rutina.getId());
        }else{
            dataArrayList = new ArrayList<>();
            rutina = null;
        }
    }

    /**
     * Este método guarda una imegen en formato Uri al Store de firebase
     */
    public void guardarImagenRutina() {
        //guardo la fecha de hoy
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        String fecha = date.toString();
        if (rutina == null) {
            //creamos una referencia en el Store que será el nombre de la imagen
            storageReference = FirebaseStorage.getInstance().getReference("usuario_" + MainActivity.getUsuario().getId()
                    + ";fecha_" + fecha);
            //subimos la imagen y recojo el link

            storageReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //si no hay problemas durante el proceso obtenemos la uri que usa el store para guardar la imagen
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;
                    Uri urlimagen = uriTask.getResult();
                    imgUriFb = urlimagen;
                    guardarRutinaEnRealtime(fecha);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("La base de datos no estaba preparada.");
                }
            });
        }else{
            if(confirmacionImg){
                //creamos una referencia en el Store que será el nombre de la imagen
                storageReference = FirebaseStorage.getInstance().getReference("usuario_" + MainActivity.getUsuario().getId()
                        + ";fecha_" + fecha);
                //subimos la imagen y recojo el link

                storageReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //si no hay problemas durante el proceso obtenemos la uri que usa el store para guardar la imagen
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        Uri urlimagen = uriTask.getResult();
                        imgUriFb = urlimagen;
                        guardarRutinaEnRealtime(fecha);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("La base de datos no estaba preparada.");
                    }
                });
            }else{
                guardarRutinaEnRealtime(fecha);
            }
        }
    }

    /**
     * Elimina la rutina con el id pasado por parámetro tanto de la lista de rutinas como de
     * la lista de rutinas que pertenecen al usuario conectado.
     * @param id
     */
    public void eliminarRutina(String id) {
        //creo una referencia a las rutinas
        DatabaseReference ref = FirebaseDatabase.getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("rutinas");
        //en la lista de rutinas, busco la que tenga el id pasado por parámetro
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot rut : dataSnapshot.getChildren()) {//
                    if (rut.child("id").getValue().equals(id)) {
                        ref.child(rut.getKey()).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("No se encuentre o hay un error");
            }
        });
        DatabaseReference ref2 = FirebaseDatabase.getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("usuarios/" + MainActivity.getUsuario().getId() + "/rutinas");

        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot rut : dataSnapshot.getChildren()) {//
                    if (rut.getValue().equals(id)) {
                        ref2.child(rut.getKey()).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("No se encuentra o hay un error");
            }
        });
        dataArrayList = new ArrayList<>();
        rutina = null;
    }

    /**
     * Al seleccionar un ejercicio de la lista, se usará este método para añadirlo al dataArrayList.
     * Si el ejercicio ya estaba en la lista no se añadirá
     * @param ejercicio
     * @param contexto
     */
    public static void addToDataList(Ejercicio ejercicio, Context contexto) {
        boolean join = true;
        for (Ejercicio dato : dataArrayList) {
            if (dato.getId() == ejercicio.getId()) {
                join = false;
            }
        }
        if (join) {
            dataArrayList.add(ejercicio);
        } else {
            Toast.makeText(contexto, "Ya estába en la lista de ejercicios.", Toast.LENGTH_LONG).show();
        }
    }

    public static void removeFromDataList(Ejercicio ejercicio, Context contexto){
        boolean join = true;
        for (Ejercicio dato : dataArrayList) {
            if (dato.getId() == ejercicio.getId()) {
                join = false;
            }
        }
        if (join) {
            dataArrayList.remove(ejercicio);
        } else {
            Toast.makeText(contexto, "No estába en la lista de ejercicios.", Toast.LENGTH_LONG).show();
        }
    }
    /**
     * Este método convierte una imagen Bitmap a tipo Uri
     *
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
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title" + fecha.toString(), null);
        return Uri.parse(path);
    }

    /**
     * Si el fragmento se abre mara modificar una rutina ya existente, el objeto global rutina
     * guardará la información de la rutina seleccionada y se cargarán sus datos en este fragmento.
     *
     * @param rutina
     */
    public void cargarRutina(Rutina rutina) {
        dataArrayList = rutina.getEjercicios();
        Glide.with(getContext())
                .load(rutina.getImg())
                .placeholder(R.drawable.baseline_add_24)//si no hay imagen carga una por defecto
                .error(R.drawable.baseline_add_24)//si ocurre algún error se verá por defecto
                .into(binding.editarImagen);
        //inserto el nombre
        binding.nombreDeRutina.setText(rutina.getNombre());
        //cambio el color de los días seleccionados
        if (rutina.getDias().contains("l")) {
            binding.lunes.setChecked(true);
        }
        if (rutina.getDias().contains("m")) {
            binding.martes.setChecked(true);
        }
        if (rutina.getDias().contains("x")) {
            binding.miercoles.setChecked(true);
        }
        if (rutina.getDias().contains("j")) {
            binding.jueves.setChecked(true);
        }
        if (rutina.getDias().contains("v")) {
            binding.viernes.setChecked(true);
        }
        if (rutina.getDias().contains("s")) {
            binding.sabado.setChecked(true);
        }
        if (rutina.getDias().contains("d")) {
            binding.domingo.setChecked(true);
        }
    }
    public static void eliminarEjercicio(int idEjercicio) {
        System.out.println("eliminarEjercicio()");
        DatabaseReference ref = FirebaseDatabase.getInstance("https://olimplicacion-3ba86-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("rutinas");
        //en la lista de rutinas, busco la que tenga el id pasado por parámetro
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot rut : dataSnapshot.getChildren()) {//
                    //si la rutina que hemos pasado está en la lista
                    if (rut.child("id").getValue().equals(rutina.getId())) {
                        //obtenemos su nombre en la base de datos
                        String codigoRutina = rut.getKey();

                        //creamos la referencia a los ejercicios de la rutina con ese nombre
                        DatabaseReference ejercicios = ref.child(codigoRutina+"/ejercicios");
                        ejercicios.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot rut : dataSnapshot.getChildren()) {
                                    if (Integer.parseInt(rut.child("id").getValue().toString())==idEjercicio) {
                                        System.out.println(ejercicios.child(rut.getKey()));
                                        System.out.println("Borrando ejercicio en la rutina");
                                        boolean encontrado = false;

                                        for (int i = 0; i < dataArrayList.size(); i++) {
                                            if(dataArrayList.get(i).getId()==idEjercicio){
                                                encontrado = true;
                                                index = i;
                                            }
                                        }
                                        if(encontrado){
                                            System.out.println("Encontrado, borrando " + dataArrayList.get(index));
                                            dataArrayList.remove(index);
                                            recyclerView.setAdapter(ejercicioAdapterModificar);
                                        }
                                        System.out.println("Borrando ejercicio en la base de datos");
                                        System.out.println("ejercicios.child(rut.getKey()).removeValue(): " + ejercicios.child(rut.getKey()));
                                        //ejercicios.child(rut.getKey()).removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                System.out.println("No se encuentre o hay un error");
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("No se encuentre o hay un error");
            }
        });
    }
}