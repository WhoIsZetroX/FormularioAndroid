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

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  {
    FirebaseRecyclerAdapter mAdapter;
    List<Formulario> formularioList = new ArrayList<>();
    public String deviceName;
    public String id;
    public String idTel;
    public int num =1;
    boolean forConf=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        num=1;
        deviceName = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
        id=android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        idTel = deviceName+" "+id;

        Query postsQuery = FirebaseDatabase.getInstance().getReference().child("formularios_noJustificados");

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Formulario>()
                .setQuery(postsQuery, Formulario.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Formulario, PostViewHolder>(options) {
            @Override
            public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new PostViewHolder(inflater.inflate(R.layout.item_post, viewGroup, false));
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

                // Así se mostraría la imagen
                //Glide.with(MainActivity.this).load(post.img).into(viewHolder.ivContent);

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, FormViewActivity.class);
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
                                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                                finish();
                            }
                        });
        }
        return true;
    }
}
