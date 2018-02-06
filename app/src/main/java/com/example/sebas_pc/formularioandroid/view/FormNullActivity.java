package com.example.sebas_pc.formularioandroid.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.sebas_pc.formularioandroid.R;
import com.example.sebas_pc.formularioandroid.model.Formulario;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aleix on 01/02/2018.
 */

public class FormNullActivity extends AppCompatActivity {

    // Creamos las variables
    FirebaseRecyclerAdapter mAdapter;
    public String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_null);

        // Creamos la consulta para la base de datos. en este caso mostraremos
        // TODOS los formularios anulados
        Query postsQuery = FirebaseDatabase.getInstance().getReference().child("formularios_anulados");

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Formulario>()
                .setQuery(postsQuery, Formulario.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Formulario, PostViewHolder>(options) {
            @Override
            public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new PostViewHolder(inflater.inflate(R.layout.item_post, viewGroup, false));
                // Le decimos que use la clase "PostViewHolder" como referencia para poner los items
                // Y que use el xml de "item_post" para saber como colocarse
            }

            @Override
            protected void onBindViewHolder(final PostViewHolder viewHolder, int position, final Formulario form) {
                // Obtener la "key" del post:
                //final DatabaseReference postRef = getRef(position);
                //final String postKey = postRef.getKey();
                //if(post.id_movil.equals(idTel)) {
                //final String postKey = getRef(position).getKey();
                //if (FirebaseDatabase.getInstance().getReference().child("formularios").child(postKey).child("id_movil").equals(idTel)) {

                final String postKey = getRef(position).getKey();

                viewHolder.tvContent.setText(form.toString2());

                //Mostrar imagen
                //Glide.with(MainActivity.this).load(post.img).into(viewHolder.ivContent);

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(FormNullActivity.this, FormNullDetailActivity.class);
                        intent.putExtra("formId", postKey);
                        startActivity(intent);
                    }
                });
            }
        };

        RecyclerView recycler = findViewById(R.id.recyclerview);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(mAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    // Estos son los 3 puntitos que salen arriba a la deerecha que sirven para cerrar sesión,
    // con este metodo lo creamos
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entry, menu);
        return true;
    }

    //Metodo donde colocamos lo que aparecerá lo que nosotros queramos que salga en los 3 puntitos,
    // en este caso solo hemos puesto para cerrar la sesion
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.logout:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(FormNullActivity.this, SignInActivity.class));
                                finish();
                            }
                        });
        }
        return true;
    }

}

