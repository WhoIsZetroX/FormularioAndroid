package com.example.sebas_pc.formularioandroid.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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

public class FormViewActivity extends AppCompatActivity {
    Formulario formulari = new Formulario();
    TextView tvContent, tvContent2;
    public String form;
    private DatabaseReference mDatabase;
    FloatingActionButton fab1, fab2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_view);
        tvContent = findViewById(R.id.tvContent);
        tvContent2 = findViewById(R.id.tvContent2);
        fab1 = findViewById(R.id.justificar);
        fab2 = findViewById(R.id.eliminar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null)
        {
            form =(String) bundle.get("formId");
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final String formId = form;
        mDatabase.child("formularios").child(formId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        Formulario formulario = dataSnapshot.getValue(Formulario.class);
                        tvContent.setText(formulario.toString());
                        if (formulario.AQimg.equals("null")) {
                            tvContent2.setText(" ");

                            if (formulario.ALfamiliar == null) {
                                formulari.AAdhForm = formulario.AAdhForm;
                                formulari.ABid_movil = formulario.ABid_movil;
                                formulari.ACdNI = formulario.ACdNI;
                                formulari.ADnombre = formulario.ADnombre;
                                formulari.AEapellidos = formulario.AEapellidos;
                                formulari.AFinici = formulario.AFinici;
                                formulari.AGfi = formulario.AGfi;
                                formulari.AHhores = formulario.AHhores;
                                formulari.AIdestinatari = formulario.AIdestinatari;
                                formulari.AJarea = formulario.AJarea;
                                formulari.AKambit = formulario.AKambit;
                                formulari.AMtipus = formulario.AMtipus;
                                formulari.ANtipusF = formulario.ANtipusF;
                                formulari.AOobservaciones = formulario.AOobservaciones;

                            } else {

                                formulari.AAdhForm = formulario.AAdhForm;
                                formulari.ABid_movil = formulario.ABid_movil;
                                formulari.ACdNI = formulario.ACdNI;
                                formulari.ADnombre = formulario.ADnombre;
                                formulari.AEapellidos = formulario.AEapellidos;
                                formulari.AFinici = formulario.AFinici;
                                formulari.AGfi = formulario.AGfi;
                                formulari.AHhores = formulario.AHhores;
                                formulari.AIdestinatari = formulario.AIdestinatari;
                                formulari.AJarea = formulario.AJarea;
                                formulari.AKambit = formulario.AKambit;
                                formulari.ALfamiliar = formulario.ALfamiliar;
                                formulari.AMtipus = formulario.AMtipus;
                                formulari.ANtipusF = formulario.ANtipusF;
                                formulari.AOobservaciones = formulario.AOobservaciones;
                            }


                        }else {
                            tvContent2.setClickable(true);
                            tvContent2.setMovementMethod(LinkMovementMethod.getInstance());
                            tvContent2.setText(Html.fromHtml("<a href='" + formulario.AQimg + "'> IMAGEN </a>"));

                            if (formulario.ALfamiliar == null) {
                                formulari.AAdhForm = formulario.AAdhForm;
                                formulari.ABid_movil = formulario.ABid_movil;
                                formulari.ACdNI = formulario.ACdNI;
                                formulari.ADnombre = formulario.ADnombre;
                                formulari.AEapellidos = formulario.AEapellidos;
                                formulari.AFinici = formulario.AFinici;
                                formulari.AGfi = formulario.AGfi;
                                formulari.AHhores = formulario.AHhores;
                                formulari.AIdestinatari = formulario.AIdestinatari;
                                formulari.AJarea = formulario.AJarea;
                                formulari.AKambit = formulario.AKambit;
                                formulari.AMtipus = formulario.AMtipus;
                                formulari.ANtipusF = formulario.ANtipusF;
                                formulari.AOobservaciones = formulario.AOobservaciones;
                                formulari.AQimg = formulario.AQimg;

                            } else {

                                formulari.AAdhForm = formulario.AAdhForm;
                                formulari.ABid_movil = formulario.ABid_movil;
                                formulari.ACdNI = formulario.ACdNI;
                                formulari.ADnombre = formulario.ADnombre;
                                formulari.AEapellidos = formulario.AEapellidos;
                                formulari.AFinici = formulario.AFinici;
                                formulari.AGfi = formulario.AGfi;
                                formulari.AHhores = formulario.AHhores;
                                formulari.AIdestinatari = formulario.AIdestinatari;
                                formulari.AJarea = formulario.AJarea;
                                formulari.AKambit = formulario.AKambit;
                                formulari.ALfamiliar = formulario.ALfamiliar;
                                formulari.AMtipus = formulario.AMtipus;
                                formulari.ANtipusF = formulario.ANtipusF;
                                formulari.AOobservaciones = formulario.AOobservaciones;
                                formulari.AQimg = formulario.AQimg;

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("tag", "getUser:onCancelled", databaseError.toException());
                    }
                });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormViewActivity.this, JustFormActivity.class);
                intent.putExtra("formId", formId);
                startActivity(intent);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Desea anular este formulario?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                final String formId = FirebaseDatabase.getInstance().getReference().child("formularios_anulados").push().getKey();
                                FirebaseDatabase.getInstance().getReference().child("formularios_anulados").child(formId).setValue(formulari);
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child(formId).removeValue();
                                startActivity(new Intent(FormViewActivity.this, MainActivity.class));
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                // Create the AlertDialog object and return it
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}
