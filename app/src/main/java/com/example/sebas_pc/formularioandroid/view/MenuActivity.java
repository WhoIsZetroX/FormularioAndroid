package com.example.sebas_pc.formularioandroid.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.sebas_pc.formularioandroid.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MenuActivity extends AppCompatActivity {

    //Menú
    Button btnConsultar, btnEsribir, btnConsJustificados, btnConsAnulados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnConsultar=findViewById(R.id.btnC);
        btnEsribir=findViewById(R.id.btnE);
        btnConsJustificados = findViewById(R.id.btnG);
        btnConsAnulados = findViewById(R.id.btnH);

        //Botón para consultar los formularios redactados
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, MainActivity.class));
            }
        });

        //Botón para redactar un formulario
        btnEsribir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, NewPostActivity.class));
            }
        });

        //Botón para consultar los formularios justificados
        btnConsJustificados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, FormViewJustActivity.class));
            }
        });

        //Botón para consultar los formularios anulados
        btnConsAnulados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, FormNullActivity.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_entry, menu);
        return true;
    }

    //Metodo para cerrar la sesion
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id)
        {
            case R.id.logout:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(MenuActivity.this, SignInActivity.class));
                                finish();
                            }
                        });
        }
        return true;
    }
}
