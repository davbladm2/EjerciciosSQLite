package com.example.dm2.ejerciciossqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactosSQLiteHelper extends SQLiteOpenHelper{

    //Sentencia para crear la tabla de Contactos
    String sqlCreate="CREATE TABLE contactos (idContacto INTEGER PRIMARY KEY, nombre TEXT, telefono TEXT)";

    public ContactosSQLiteHelper(Context contexto, String nombre, SQLiteDatabase.CursorFactory factory, int version){
        super(contexto,nombre,factory,version);
    }

    public void onCreate(SQLiteDatabase db){
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
    }

    public void onUpgrade(SQLiteDatabase db, int i, int i1){
        //Se elimina la versión anterior e la tabla
        db.execSQL("DROP TABLE IF EXISTS contactos");
        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }
}
