package com.example.dm2.ejerciciossqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Actividad2 extends AppCompatActivity {

    private LibrosSQLiteHelper dbCon;
    private SQLiteDatabase db;
    private EditText editTextIsbn,editTextTitulo,editTextGenero,editTextNumPaginas;
    private TextView textViewContenido;
    private String isbn,titulo,genero,numPaginas;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad2);
        setTitle("BBDD de Libros");

        //Abrir la base de datos CONTACTOS en modo de escritura
        dbCon=new LibrosSQLiteHelper(this,"DBLibros",null,1);
        db=dbCon.getWritableDatabase();

        editTextIsbn=(EditText) findViewById(R.id.editTextIsbn);
        editTextTitulo=(EditText) findViewById(R.id.editTextTitulo);
        editTextGenero=(EditText) findViewById(R.id.editTextGenero);
        editTextNumPaginas=(EditText) findViewById(R.id.editTextNumPaginas);
        textViewContenido=(TextView) findViewById(R.id.textViewContenido);

        Cursor c=db.rawQuery("SELECT isbn, titulo, genero, numPaginas FROM libros",null);
        rellenarTextViewContenido(c);
    }

    public void pulsados(View v){
        isbn=editTextIsbn.getText().toString();
        titulo=editTextTitulo.getText().toString();
        genero=editTextGenero.getText().toString();
        numPaginas=editTextNumPaginas.getText().toString();

        if(v.getId() == R.id.buttonInsertar){
            //Si hemos abierto correctamente la base de datos
            if(db!=null){
                if(isbn.equals("") || titulo.equals("") || genero.equals("") || numPaginas.equals("")){
                    Toast toast1 =Toast.makeText(getApplicationContext(),"Debes rellenar los cuatro campos", Toast.LENGTH_SHORT);
                    toast1.show();
                }else{
                    int numP=Integer.parseInt(numPaginas);
                    //Insertar los datos en la tabla contactos
                    db.execSQL("INSERT INTO libros (isbn, titulo, genero, numPaginas) VALUES ('"+isbn+"','"+titulo+"','"+genero+"',"+numP+")");
                    Toast toast1 =Toast.makeText(getApplicationContext(),"Se ha añadido correctamente el libro "+titulo, Toast.LENGTH_SHORT);
                    toast1.show();
                    editTextIsbn.setText("");
                    editTextGenero.setText("");
                    editTextTitulo.setText("");
                    editTextNumPaginas.setText("");
                    finish();
                    startActivity(getIntent());
                }
            }
        }

        //Actualizar datos en la BBDD
        if(v.getId() == R.id.buttonActualizar){
            if(isbn.equals("") || titulo.equals("") || genero.equals("") || numPaginas.equals("")){
                Toast toast1 =Toast.makeText(getApplicationContext(),"Primero tienes que hacer una consulta", Toast.LENGTH_SHORT);
                toast1.show();
            }else{
                String titulo2=editTextTitulo.getText().toString();
                String genero2=editTextGenero.getText().toString();
                String isbn2=editTextIsbn.getText().toString();
                db.execSQL("UPDATE libros SET  titulo='"+titulo2+"', genero='"+genero2+"' WHERE isbn='"+isbn2+"'");
                Toast toast1 =Toast.makeText(getApplicationContext(),"Se ha actualizado correctamente", Toast.LENGTH_SHORT);
                toast1.show();
                Cursor c2=db.rawQuery("SELECT isbn, titulo, genero, nuMPaginas  FROM libros WHERE isbn='"+isbn2+"'",null);
                rellenarTextViewContenido(c2);
            }
        }

        //Eliminar datos en la BBDD
        if(v.getId() == R.id.buttonEliminar){
            if(isbn.isEmpty() && titulo.isEmpty() && genero.isEmpty() && numPaginas.isEmpty()){
                Toast toast1 =Toast.makeText(getApplicationContext(),"Debes introducir el isbn para poder eliminar.", Toast.LENGTH_SHORT);
                toast1.show();
            }else if(!isbn.isEmpty()){
                String isbn=editTextIsbn.getText().toString();
                db.execSQL("DELETE FROM libros WHERE isbn='"+isbn+"'");
                Toast toast1 =Toast.makeText(getApplicationContext(),"Se ha eliminado correctamente el libro.", Toast.LENGTH_SHORT);
                toast1.show();
                finish();
                startActivity(getIntent());
            }
        }

        //Consultar datos en la BBDD
        if(v.getId() == R.id.buttonConsultar){
            Cursor c1=null;
            Boolean entrada=false;
            if(!isbn.isEmpty() && titulo.isEmpty() && genero.isEmpty() && numPaginas.isEmpty()){
                String isbn=editTextIsbn.getText().toString();
                c1=db.rawQuery("SELECT isbn, titulo, genero, numPaginas FROM libros WHERE isbn="+isbn,null);
                entrada=true;
            }else if(isbn.isEmpty() && !titulo.isEmpty() && genero.isEmpty() && numPaginas.isEmpty()){
                String titulo=editTextTitulo.getText().toString();
                c1=db.rawQuery("SELECT isbn, titulo, genero, numPaginas FROM libros WHERE titulo='"+titulo+"'",null);
                entrada=true;
            }else if(isbn.isEmpty() && titulo.isEmpty() && !genero.isEmpty() && numPaginas.isEmpty()){
                String genero=editTextGenero.getText().toString();
                c1=db.rawQuery("SELECT isbn, titulo, genero, numPaginas FROM libros WHERE genero='"+genero+"'",null);
                entrada=true;
            }else if(isbn.isEmpty() && titulo.isEmpty() && !genero.isEmpty() && numPaginas.isEmpty()){
                int numP=Integer.parseInt(editTextNumPaginas.getText().toString());
                c1=db.rawQuery("SELECT isbn, titulo, genero, numPaginas FROM libros WHERE genero='"+numPaginas+"'",null);
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
                            String isbn=c1.getString(0);
                            String titulo=c1.getString(1);
                            String genero=c1.getString(2);
                            int numPag=c1.getInt(3);
                            editTextIsbn.setText(isbn);
                            editTextTitulo.setText(titulo);
                            editTextGenero.setText(genero);
                            editTextNumPaginas.setText(Integer.toString(numPag));
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
                String isbn=c.getString(0);
                String titulo=c.getString(1);
                String genero=c.getString(2);
                int numPag=c.getInt(3);
                textViewContenido.setText(textViewContenido.getText()+"\n"+isbn+" - "+titulo+" - "+genero+" - "+numPag);
            }while (c.moveToNext());
        }
    }
}
