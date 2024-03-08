package com.example.olimplicacion.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseManager {
//ATRIBUTOS
Context mContext;
SQLiteDatabase db;
DataBaseHelper dbOpenHelper;
EjerciciosDAO ejerciciosDAO;//se añade para que la db tenga acceso a los métodos de CRUD
//CONSTRUCTOR
public DataBaseManager(Context context){
    this.mContext = context;
    dbOpenHelper = new DataBaseHelper(mContext);

    //getWritableDatabase()
    //Si la db no se ha creado llamará al método onCreate
    //Si la versión es superior llamará al onUpgrade
    db = dbOpenHelper.getWritableDatabase();
    ejerciciosDAO = new EjerciciosDAO(db);
}
//GETTERS Y SETTERS


    public EjerciciosDAO getEjerciciosDAO() {
        return ejerciciosDAO;
    }
}
