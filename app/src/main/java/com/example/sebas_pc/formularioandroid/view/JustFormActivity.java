package com.example.sebas_pc.formularioandroid.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sebas_pc.formularioandroid.R;
import com.example.sebas_pc.formularioandroid.model.Formulario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JustFormActivity extends AppCompatActivity {

    TextView tvContent, tvContent2;
    public String form;
    private DatabaseReference mDatabase;
    String photoPath;
    String cameraPhotoPath;
    String photoName;
    boolean photoConf = false;
    Button camera;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_just_form);
        tvContent = findViewById(R.id.tvContent);
        tvContent2 = findViewById(R.id.tvContent2);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            form = (String) bundle.get("formId");
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
                        } else {
                            tvContent2.setClickable(true);
                            tvContent2.setMovementMethod(LinkMovementMethod.getInstance());
                            tvContent2.setText(Html.fromHtml("<a href='" + formulario.AQimg + "'> IMAGEN </a>"));
                            // ...
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("tag", "getUser:onCancelled", databaseError.toException());
                    }
                });
        camera = findViewById(R.id.button_cameraF);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });


        imageView = (ImageView) findViewById(R.id.image_view);


    }

    void dispatchTakePictureIntent() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        dir.mkdir();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File photoFile = null;

        try {
            photoFile = File.createTempFile("JPEG_" + timeStamp + "_", ".jpg", dir);
            photoName = photoFile.getName();
            cameraPhotoPath = photoFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (photoFile != null) {
            Uri photoUri = FileProvider.getUriForFile(this,
                    "com.example.sebas_pc.formularioandroid.fileprovider",
                    photoFile);
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(takePictureIntent, 1301);
        }
        photoConf = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1301) {
            if (resultCode == RESULT_OK) {
                // acción a tomar cuando se ha tomado la foto
                photoPath = cameraPhotoPath;
            }
        }
    }


    void setPicture() {
        if (photoPath != null && !photoPath.isEmpty()) {
            LinearLayout view_instance = findViewById(R.id.linLay);
            ViewGroup.LayoutParams params = view_instance.getLayoutParams();
            params.height = 1500;
            view_instance.setLayoutParams(params);
            Bitmap bitmap = BitmapUtils.rotate(photoPath);
            imageView.setImageBitmap(bitmap);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("PHOTOPATH", photoPath);
        outState.putString("CAMERAPHOTOPATH", cameraPhotoPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        photoPath = savedInstanceState.getString("PHOTOPATH", "");
        cameraPhotoPath = savedInstanceState.getString("CAMERAPHOTOPATH", "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setPicture();
    }




}
