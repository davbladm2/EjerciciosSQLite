package com.example.dm2.ejerciciossqlite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void pulsado(View v) {
        Intent intent;

        if(v.getId() == R.id.buttonEjercicio1){
            intent=new Intent(MainActivity.this,Actividad1.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.buttonEjercicio2){
            intent=new Intent(MainActivity.this,Actividad2.class);
            startActivity(intent);
        }
    }
}
