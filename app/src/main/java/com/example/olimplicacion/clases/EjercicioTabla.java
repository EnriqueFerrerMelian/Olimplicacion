package com.example.olimplicacion.clases;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

//contiene los datos de la tabla "ejercicio" que usará la bd
public class EjercicioTabla {
    //ATRIBUTOS
    public static final String TABLE_NAME = "ejercicios";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_MUSCULOS = "musculos";
    public static final String COLUMN_DESC = "desc";
    public static final String COLUMN_IMG = "img";

    //CONSTRUCTOR
    static public void onCreate(SQLiteDatabase db) { //creará la TABLA, no un registro
        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE " + EjercicioTabla.TABLE_NAME + "(");
        sb.append(COLUMN_ID + " integer primary key autoincrement, ");
        sb.append(COLUMN_NOMBRE + " integer, ");
        sb.append(COLUMN_MUSCULOS + " integer, ");
        sb.append(COLUMN_DESC + " integer, ");
        sb.append(COLUMN_IMG + " integer);");

        try{
            db.execSQL(sb.toString());
        }catch(SQLException ex){
            ex.getStackTrace();
        }
    }

    //GETTERS Y SETTERS
    //borrará la tabla y la creará con los datos nuevos
    static public void onUpgrade(SQLiteDatabase db, int i, int i1){
        try {
            db.execSQL("DROP TABLE IF EXISTS " + EjercicioTabla.TABLE_NAME);
        }catch (SQLException ex){
            ex.getStackTrace();
        }
    }


}
