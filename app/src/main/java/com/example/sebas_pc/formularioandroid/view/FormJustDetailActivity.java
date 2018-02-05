package com.example.sebas_pc.formularioandroid.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.example.sebas_pc.formularioandroid.R;
import com.example.sebas_pc.formularioandroid.model.Formulario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FormJustDetailActivity extends AppCompatActivity {

    TextView tvContent, tvContent2;
    public String form;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_just_detail);

        tvContent = findViewById(R.id.tvContent);
        tvContent2 = findViewById(R.id.tvContent2);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            form = (String) bundle.get("formId");
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final String formId = form;
        mDatabase.child("formularios_justificados").child(formId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        Formulario formulario = dataSnapshot.getValue(Formulario.class);
                        tvContent.setText(formulario.toString());
                        tvContent2.setClickable(true);
                        tvContent2.setMovementMethod(LinkMovementMethod.getInstance());
                        tvContent2.setText(Html.fromHtml("<a href='" + formulario.AQimg + "'> IMAGEN </a>"));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("tag", "getUser:onCancelled", databaseError.toException());
                    }
                });
    }
}
