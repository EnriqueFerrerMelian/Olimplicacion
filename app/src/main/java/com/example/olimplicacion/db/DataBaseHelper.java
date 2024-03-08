package com.example.olimplicacion.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.olimplicacion.clases.EjercicioTabla;

public class DataBaseHelper extends SQLiteOpenHelper {
    final static String DATABASE_NAME = "olimpia.db";
    final static int DATABASE_VERSION = 1;

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

//ATRIBUTOS

//CONSTRUCTOR

    //GETTERS Y SETTERS
    @Override //se llamará este método si la bd no se ha creado con anterioridad
    public void onCreate(SQLiteDatabase db) {
        EjercicioTabla.onCreate(db); //creará la tabla "ejercicios" al crearse la db
        //la inserción de los datos de la tabla debería ir aquí
        //talvez obtener los objetos de una lista?
        //a partir de aqui, investigar como leer de un archivo para crear los ejercicios
        /*https://www.youtube.com/watch?v=uBw9QSWSIww
        49:19*/

    }

    @Override  //se llama cuando la nueva versión de la bd (i1) es mayor que la antigua versión (i)
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("Drop table "+EjercicioTabla.TABLE_NAME);
        onCreate(db);
    }
}