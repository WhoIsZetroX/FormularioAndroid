package com.example.sebas_pc.formularioandroid.view;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.sebas_pc.formularioandroid.R;
import com.example.sebas_pc.formularioandroid.model.Formulario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FormNullDetailActivity extends AppCompatActivity {

    // Creamos las variables
    TextView tvContent, tvContent2;
    public String form;
    private DatabaseReference mDatabase;
    Intent intent;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_null_detail);

        // Vinculamos las variables con los objetos de la app con los findViewById
        findViews();

        // Pillamos los datos que pasamos de la anterior activity que en este caso es la key del formulario
        intent = getIntent();
        bundle = intent.getExtras();

        // Si los datos que recoge de la anterior activity no son nulos se lo asignaremos a la variable "form"
        if (bundle != null) {
            // Si el contenido no es nulo pillamos la id
            form = (String) bundle.get("formId");
        }

        // Creamos una variable final con la key
        final String formId = form;

        // Hacemos una consulta a la base de datos y hacemos que pille el formulario con la key que
        // pasamos pero lo hacemos que solo pille 1 vez el dato.
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("formularios_anulados").child(formId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Almacenamos los datos del formulario de la base en una variable de tipo
                        // formulario y añadimos los datos en un textView
                        Formulario formulario = dataSnapshot.getValue(Formulario.class);
                        tvContent.setText(formulario.toString());

                        // Esta parte es otro textView para poder poner un link que te redireccione
                        // a la imagen pero haciendo que se vea en el navegador
                        tvContent2.setClickable(true);
                        tvContent2.setMovementMethod(LinkMovementMethod.getInstance());
                        tvContent2.setText(Html.fromHtml("<a href='" + formulario.ARimg + "'> IMAGEN </a>"));

                        // Si queremos mostrarla en ese momento dentro de la app sería:
                        // Así se mostraría la imagen
                        //Glide.with(MainActivity.this).load(post.img).into(viewHolder.ivContent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // En el caso de que se produzca algún error subiendo los archivos entonces
                        // dará error y firebase tiene un metodo para que si no se ha podido subir
                        // ahora entonces se lo guardará y se subirá cuando pueda
                        Log.w("tag", "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    // Metodo para relacionar variables con los objetos/items de la app
    void findViews() {
        // TextView de todos los datos del formulario
        tvContent = findViewById(R.id.tvContent);
        // TextView del link de la imagen
        tvContent2 = findViewById(R.id.tvContent2);
    }
}
