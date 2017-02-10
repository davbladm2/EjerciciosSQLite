package com.example.dm2.ejerciciossqlite;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Actividad1 extends AppCompatActivity {

    private ContactosSQLiteHelper dbCon;
    private SQLiteDatabase db;
    private EditText editTextCodigo,editTextNombre,editTextTelefono;
    private String codigo,nombre,telefono;
    private TextView textViewContenido;
    private int idContactoConsulta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad1);
        setTitle("Agenda de Contactos");

        //Abrir la base de datos CONTACTOS en modo de escritura
        dbCon=new ContactosSQLiteHelper(this,"DBContactos",null,1);
        db=dbCon.getWritableDatabase();

        editTextCodigo=(EditText) findViewById(R.id.editTextCodigo);
        editTextNombre=(EditText) findViewById(R.id.editTextNombre);
        editTextTelefono=(EditText) findViewById(R.id.editTextTelefono);
        textViewContenido=(TextView) findViewById(R.id.textViewContenido);

        Cursor c=db.rawQuery("SELECT idContacto, nombre, telefono FROM contactos",null);
        rellenarTextViewContenido(c);
    }

    public void pulsados(View v){
        codigo=editTextCodigo.getText().toString();
        nombre=editTextNombre.getText().toString();
        telefono=editTextTelefono.getText().toString();

        if(v.getId() == R.id.buttonInsertar){
            //Si hemos abierto correctamente la base de datos
            if(db!=null){
                if(codigo.equals("") || nombre.equals("") || telefono.equals("")){
                    Toast toast1 =Toast.makeText(getApplicationContext(),"Debes rellenar los tres campos", Toast.LENGTH_SHORT);
                    toast1.show();
                }else{
                    int id=Integer.parseInt(codigo);
                    //Insertar los datos en la tabla contactos
                    db.execSQL("INSERT INTO contactos (idContacto, nombre, telefono) VALUES ("+id+",'"+nombre+"','"+telefono+"')");
                    Toast toast1 =Toast.makeText(getApplicationContext(),"Se ha añadido correctamente el contacto "+nombre, Toast.LENGTH_SHORT);
                    toast1.show();
                    editTextCodigo.setText("");
                    editTextNombre.setText("");
                    editTextTelefono.setText("");
                    finish();
                    startActivity(getIntent());
                }
            }
        }

        //Actualizar datos en la BBDD
        if(v.getId() == R.id.buttonActualizar){
            if(codigo.equals("") || nombre.equals("") || telefono.equals("")){
                Toast toast1 =Toast.makeText(getApplicationContext(),"Primero tienes que hacer una consulta", Toast.LENGTH_SHORT);
                toast1.show();
            }else{
                String nombre=editTextNombre.getText().toString();
                String telefono=editTextTelefono.getText().toString();
                db.execSQL("UPDATE contactos SET nombre='"+nombre+"', telefono='"+telefono+"' WHERE idContacto="+idContactoConsulta);
                Toast toast1 =Toast.makeText(getApplicationContext(),"Se ha actualizado correctamente", Toast.LENGTH_SHORT);
                toast1.show();
                Cursor c2=db.rawQuery("SELECT idContacto, nombre, telefono FROM contactos WHERE idContacto="+idContactoConsulta,null);
                rellenarTextViewContenido(c2);
            }
        }

        //Eliminar datos en la BBDD
        if(v.getId() == R.id.buttonEliminar){
            if(codigo.isEmpty() && nombre.isEmpty() && telefono.isEmpty()){
                Toast toast1 =Toast.makeText(getApplicationContext(),"Debes introducir un codigo para poder eliminar.", Toast.LENGTH_SHORT);
                toast1.show();
            }else if(!codigo.isEmpty()){
                int id=Integer.parseInt(editTextCodigo.getText().toString());
                db.execSQL("DELETE FROM contactos WHERE idContacto="+id);
                Toast toast1 =Toast.makeText(getApplicationContext(),"Se ha eliminado correctamente el contacto.", Toast.LENGTH_SHORT);
                toast1.show();
                finish();
                startActivity(getIntent());
            }
        }

        //Consultar datos en la BBDD
        if(v.getId() == R.id.buttonConsultar){
            Cursor c1=null;
            Boolean entrada=false;
            if(!codigo.isEmpty() && nombre.isEmpty() && telefono.isEmpty()){
                int id=Integer.parseInt(editTextCodigo.getText().toString());
                c1=db.rawQuery("SELECT idContacto, nombre, telefono FROM contactos WHERE idContacto="+id,null);
                entrada=true;
            }else if(codigo.isEmpty() && !nombre.isEmpty() && telefono.isEmpty()){
                String nombre=editTextNombre.getText().toString();
                c1=db.rawQuery("SELECT idContacto, nombre, telefono FROM contactos WHERE nombre='"+nombre+"'",null);
                entrada=true;
            }else if(codigo.isEmpty() && nombre.isEmpty() && !telefono.isEmpty()){
                String telefono=editTextTelefono.getText().toString();
                c1=db.rawQuery("SELECT idContacto, nombre, telefono FROM contactos WHERE telefono='"+telefono+"'",null);
                entrada=true;
            }else{
                Toast toast1 =Toast.makeText(getApplicationContext(),"Debes rellenar un campo para hacer la consulta", Toast.LENGTH_SHORT);
                toast1.show();
            }
            if(entrada==true){
                if(!c1.moveToFirst()){
                    Toast toast1 =Toast.makeText(getApplicationContext(),"No existe el contacto", Toast.LENGTH_SHORT);
                    toast1.show();
                }else {
                    rellenarTextViewContenido(c1);
                    if (c1.moveToFirst()) {
                        do {
                            int codigo = c1.getInt(0);
                            String nombre = c1.getString(1);
                            String telefono = c1.getString(2);
                            idContactoConsulta = codigo;
                            editTextCodigo.setText(Integer.toString(codigo));
                            editTextTelefono.setText(telefono);
                            editTextNombre.setText(nombre);
                        } while (c1.moveToNext());
                    }
                }
            }
        }
    }

    public void rellenarTextViewContenido(Cursor c){
        textViewContenido.setText("");
        if(c.moveToFirst()){
            do{
                int codigo=c.getInt(0);
                String nombre=c.getString(1);
                String telefono=c.getString(2);
                textViewContenido.setText(textViewContenido.getText()+"\n"+codigo+" - "+nombre+" - "+telefono);
            }while (c.moveToNext());
        }
    }
}
