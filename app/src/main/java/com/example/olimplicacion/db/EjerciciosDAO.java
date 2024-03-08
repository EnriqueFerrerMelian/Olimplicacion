package com.example.olimplicacion.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.olimplicacion.clases.Ejercicio;
import com.example.olimplicacion.clases.EjercicioTabla;

import java.util.ArrayList;

//Data Access Object
//los archivos creados se guardan en la memoria interna del movil
//Device Explorer: data > data > com.example.nombreApp > files
public class EjerciciosDAO {
//ATRIBUTOS
    Context mContext;
private SQLiteDatabase db;
static final int READ_BLOCK_SIZE = 100;

//CONSTRUCTOR

    public EjerciciosDAO(SQLiteDatabase db) {
        this.db = db;
    }
    final String TAG = "demo";

//GETTERS Y SETTERS

    //CRUD (CREATE-READ-UPDATE-DELETE)
    //crear
    //leer
    //actualizar
    //borrar

    //guardar un ejercicio en la bd.
    public long addEjrecicio(Ejercicio ej){
        ContentValues valores = new ContentValues();
        valores.put(EjercicioTabla.COLUMN_NOMBRE, ej.getName());
        valores.put(EjercicioTabla.COLUMN_MUSCULOS, ej.getMusculos());
        valores.put(EjercicioTabla.COLUMN_DESC, ej.getDesc());
        valores.put(EjercicioTabla.COLUMN_IMG, ej.getImage());
        return db.insert(EjercicioTabla.TABLE_NAME, null, valores);//devolverá -1 si ha ocurrido un error
    }

    //modificar el ejercicio en la db
    public boolean updateEjrecicio(Ejercicio ej){
        ContentValues valores = new ContentValues();
        valores.put(EjercicioTabla.COLUMN_NOMBRE, ej.getName());
        valores.put(EjercicioTabla.COLUMN_MUSCULOS, ej.getMusculos());
        valores.put(EjercicioTabla.COLUMN_DESC, ej.getDesc());
        valores.put(EjercicioTabla.COLUMN_IMG, ej.getImage());
        return db.update(EjercicioTabla.TABLE_NAME, valores, EjercicioTabla.COLUMN_ID + "= ?", new String[]{String.valueOf(ej.getId())}) > 0;
        //devolverá true si se ha actualizado algun registro
    }

    //borrar el ejercicio de la db
    public boolean deleteEjrecicio(Ejercicio ej){
        return db.delete(EjercicioTabla.TABLE_NAME, EjercicioTabla.COLUMN_ID + "= ?", new String[]{String.valueOf(ej.getId())}) > 0;
        //devolverá true si se ha borrado algun registro
    }


    public Ejercicio getEjrecicio(long id){
        Ejercicio ejercicio = null;

        Cursor cursor = db.query(EjercicioTabla.TABLE_NAME, new String[]{EjercicioTabla.COLUMN_ID,
            EjercicioTabla.COLUMN_NOMBRE, EjercicioTabla.COLUMN_MUSCULOS, EjercicioTabla.COLUMN_ID,
            EjercicioTabla.COLUMN_IMG}, EjercicioTabla.COLUMN_ID +"= ?", new String[]{String.valueOf(id)}, null, null, null);
        if(cursor.moveToFirst()){
            ejercicio = crearEjercicioConCursor(cursor);
        }
        return ejercicio; // devolverá un ejercicio por su id
    }

    //devuelve un array con todos los ejercicios
    public ArrayList<Ejercicio> getAll(){
        ArrayList<Ejercicio> ejercicios = new ArrayList<>();
        //en este caso configuramos un cursos sin clausula "where" ni parámetros
        Cursor cursor = db.query(EjercicioTabla.TABLE_NAME, new String[]{EjercicioTabla.COLUMN_ID,
                EjercicioTabla.COLUMN_NOMBRE, EjercicioTabla.COLUMN_MUSCULOS, EjercicioTabla.COLUMN_DESC,
                EjercicioTabla.COLUMN_IMG}, null, null, null, null, null);

        //cursor.moveToFirst();
        while(cursor.moveToNext()){
            Ejercicio ejercicio = crearEjercicioConCursor(cursor);
            ejercicios.add(ejercicio);
        }
        return ejercicios;
    }

    private Ejercicio crearEjercicioConCursor(Cursor cursor){
        Ejercicio ejercicio = new Ejercicio();
        ejercicio.setId(cursor.getLong(0));
        ejercicio.setName(cursor.getInt(1));
        ejercicio.setMusculos(cursor.getInt(2));
        ejercicio.setDesc(cursor.getInt(3));
        ejercicio.setImage(cursor.getInt(4));

        return ejercicio;
    }

    /*public void escribirEjercicio(Ejercicio ej, Context context) {
        //los archivos creados se guardan en la memoria interna del movil
        //Device Explorer: data > data > com.example.nombreApp > files
        try {
            File archivo = new File(ej.getName() + ".txt");
            FileOutputStream fos = context.openFileOutput(archivo.getName(), Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.append("\r\n");
            osw.write(ej.getName() + "\n");
            osw.write(ej.getMusculos() + "\n");
            osw.write(ej.getDesc() + "\n");
            osw.write(ej.getImage() + "\n");
            osw.flush();//borra el contenido del buffer
            osw.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }
    public void leerEjercicio(Ejercicio ej, Context context) {
        LinkedList<String> atributos = new LinkedList<String>();
        try {
            File archivo = new File("/");
            File[] archivos = archivo.listFiles();
            for(int i = 0; i<archivos.length; i++){
                InputStreamReader isr = new InputStreamReader(context.openFileInput(archivo.getName()));
                BufferedReader bfr = new BufferedReader(isr);
                String linea = "";
                while((linea=bfr.readLine())!=null){
                    atributos.add(linea);
                }
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
    }*/
}
