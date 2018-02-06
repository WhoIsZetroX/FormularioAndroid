package com.example.sebas_pc.formularioandroid.view;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sebas_pc.formularioandroid.R;
import com.example.sebas_pc.formularioandroid.model.Formulario;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewPostActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_REQUEST_CODE = 1;
    String photoPath;
    String cameraPhotoPath;
    String deviceName, androidId, formId, dhForm, idMovil;
    String photoName;
    boolean photoConf = false;
    TelephonyManager tm;
    TextView efecha, efecha2, familiartv;
    EditText familiar;
    Button bfecha, bfecha2, save;
    ImageButton camera;
    LinearLayout linearLayout;
    Spinner desti, area, ambit, tipus, finalT;
    boolean check;

    String[] itemsP;
    ArrayAdapter<String> adapterTP;
    String[] itemsF;
    ArrayAdapter<String> adapterTF;
    String[] itemsFinal1;
    ArrayAdapter<String> adapterFinal1;
    String[] itemsFinal2;
    ArrayAdapter<String> adapterFinal2;
    String[] itemsFinal3;
    ArrayAdapter<String> adapterFinal3;
    String[] itemsFinal4;
    ArrayAdapter<String> adapterFinal4;
    //Esto sera la parte de los Spinner, se abrirar unos al tiempo que otros se pondra invisibles
    String[] itemsDesti;
    ArrayAdapter<String> adapterDesti;
    String[] itemsArea;
    ArrayAdapter<String> adapterArea;
    //Esto sera la parte de los Spinner, se abrirar unos al tiempo que otros se pondra invisibles
    String[] itemsA;
    ArrayAdapter<String> adapterA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        findViews();
        setOnClicks();

        itemsP = new String[]{"1 jornada", "Superior a una jornada"};
        adapterTP = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsP);
        itemsF = new String[]{"En mateixa localitat", "Fora localitat"};
        adapterTF = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsF);
        itemsFinal1 = new String[]{"Asistencia medica", "Curs formació"};
        adapterFinal1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsFinal1);
        itemsFinal2 = new String[]{"Matrimoni", "Permis paternitat"};
        adapterFinal2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsFinal2);
        itemsFinal3 = new String[]{"Defuncio", "Intervenció quirurgica"};
        adapterFinal3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsFinal3);
        itemsFinal4 = new String[]{"Defuncio", "Intervenció quirurgica"};
        adapterFinal4 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsFinal4);
        itemsDesti = new String[]{"RRHH", "Cap de Departament"};
        adapterDesti = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsDesti);
        itemsArea = new String[]{"Jardinería", "Neteja", "Ooilf"};
        adapterArea = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsArea);
        //Esto sera la parte de los Spinner, se abrirar unos al tiempo que otros se pondra invisibles
        itemsA = new String[]{"Personal", "Familiar"};
        adapterA = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsA);

        //Obtenemos la id del formulario que le pondremos al subirlo
        formId = FirebaseDatabase.getInstance().getReference().child("formularios_noJustificados").push().getKey();
        //Obtenemos la hora en tiempo real
        dhForm = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a").format(Calendar.getInstance().getTime());
        //Obtenemos el fabricante y modelo del movil ademas de su ANDROID_ID
        deviceName = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
        tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        //Los unimos
        idMovil = deviceName + " " + androidId;

        desti.setAdapter(adapterDesti);
        area.setAdapter(adapterArea);
        ambit.setAdapter(adapterA);

        familiar.setVisibility(View.INVISIBLE);
        familiartv.setVisibility(View.INVISIBLE);

        ambit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    selectPersonal();
                } else if (position == 1) {
                    selectFamiliar();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entry, menu);
        return true;
    }

    //Metodo para cerrar la sesion
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.logout:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(NewPostActivity.this, SignInActivity.class));
                                finish();
                            }
                        });
        }
        return true;
    }

    @Override
    // nos dice si nos acepta o declina el permiso
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(NewPostActivity.this,
                            "Permission accepted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(NewPostActivity.this,
                            "Permission denied", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }
// con este método se revisan los permisos
    private boolean checkPermission() {
        //Check for READ_EXTERNAL_STORAGE access, using ContextCompat.checkSelfPermission()//
        int result = ContextCompat.checkSelfPermission(NewPostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(NewPostActivity.this, Manifest.permission.CAMERA);

        //si la app tiene este permiso, retorna true
        if (result == PackageManager.PERMISSION_GRANTED || result2 == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            //si la app no tiene este permiso, retorna falso
            return false;
        }
    }

    // con este metodo se hacen las fotos cuando le das al botón
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

    // con este metodo se hace la foto
    void setPicture() {
        if (photoPath != null && !photoPath.isEmpty()) {
            LinearLayout view_instance = findViewById(R.id.ll);
            ViewGroup.LayoutParams params = view_instance.getLayoutParams();
            params.height = 1500;
            view_instance.setLayoutParams(params);
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
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


    //metodo donde guardamos todos los findviews
    void findViews() {
        //datePicker = findViewById(R.id.datePicker);
        //iniciData = findViewById(R.id.iniciData);
        bfecha = findViewById(R.id.bfecha);
        efecha = findViewById(R.id.efecha);
        bfecha2 = findViewById(R.id.bfecha2);
        efecha2 = findViewById(R.id.efecha2);
        camera = findViewById(R.id.button_camera);
        save = findViewById(R.id.btn_save);
        linearLayout = findViewById(R.id.ll);
        desti = findViewById(R.id.dest);
        area = findViewById(R.id.area);
        ambit = findViewById(R.id.ambit);
        familiar = findViewById(R.id.familiar);
        familiartv = findViewById(R.id.familiartv);
        tipus = findViewById(R.id.tipus);
        finalT = findViewById(R.id.finalT);
    }

    //metodo donde tenemos todos los onclickslistener
    public void setOnClicks() {
        bfecha.setOnClickListener(this);
        bfecha2.setOnClickListener(this);
        efecha.setOnClickListener(this);
        efecha2.setOnClickListener(this);
        camera.setOnClickListener(this);
        save.setOnClickListener(this);
    }



    //
    @Override
    public void onClick(View v) {
        if (v == bfecha || v == efecha) {
            Calendar calendar = Calendar.getInstance();
            int dia = calendar.get(Calendar.YEAR);
            int mes = calendar.get(Calendar.MONTH);
            int ano = calendar.get(Calendar.DATE);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    efecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                }
            }
                    , dia, mes, ano);
            datePickerDialog.show();
        } else if (v == bfecha2 || v == efecha2) {
            Calendar calendar = Calendar.getInstance();
            int dia = calendar.get(Calendar.YEAR);
            int mes = calendar.get(Calendar.MONTH);
            int ano = calendar.get(Calendar.DATE);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    efecha2.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                }
            }
                    , dia, mes, ano);
            datePickerDialog.show();
        } else if (v == camera) {
            if (checkPermission()) {
                //If your app has access to the device’s storage, then print the following message to Android Studio’s Logcat//
                Log.e("permission", "Permission already granted.");
                dispatchTakePictureIntent();
            } else {
                //If your app doesn’t have permission to access external storage, then call requestPermission//
                requestPermission();
            }
        } else if (v == save) {
            enviarForm();
        }

    }

    //este metodo define los campos que envia el formulario
    public void enviarForm() {
        String dhForm = this.dhForm;
        String id_movil = idMovil;
        String dni = ((EditText) findViewById(R.id.et_persondni)).getText().toString();
        String nombre = ((EditText) findViewById(R.id.et_personName)).getText().toString();
        String apellidos = ((EditText) findViewById(R.id.et_personlastName)).getText().toString();
        String inici = ((TextView) findViewById(R.id.efecha)).getText().toString();
        String fi = ((TextView) findViewById(R.id.efecha2)).getText().toString();
        String hores = ((EditText) findViewById(R.id.hores)).getText().toString();
        String destinatari = ((Spinner) findViewById(R.id.dest)).getSelectedItem().toString();
        String area = ((Spinner) findViewById(R.id.area)).getSelectedItem().toString();
        String ambit = ((Spinner) findViewById(R.id.ambit)).getSelectedItem().toString();
        String familiar = ((EditText) findViewById(R.id.familiar)).getText().toString();
        String tipus = ((Spinner) findViewById(R.id.tipus)).getSelectedItem().toString();
        String tipusF = ((Spinner) findViewById(R.id.finalT)).getSelectedItem().toString();
        String observaciones = ((EditText) findViewById(R.id.editTextObs)).getText().toString();
        final boolean check = ((CheckBox) findViewById(R.id.checkBox)).isChecked();

        //si no pones dni, salta el siguiente error
        if (TextUtils.isEmpty(dni)) {
            ((EditText) findViewById(R.id.et_persondni)).setError("El DNI es obligatori.");
            ((EditText) findViewById(R.id.et_persondni)).requestFocus();
            return;
            //si no pones nombre, salta el siguiente error
        } else if (TextUtils.isEmpty(nombre)) {
            ((EditText) findViewById(R.id.et_personName)).setError("El nom es obligatori.");
            ((EditText) findViewById(R.id.et_personName)).requestFocus();
            return;
            //si no pones apellido, salta el siguiente error
        } else if (TextUtils.isEmpty(apellidos)) {
            ((EditText) findViewById(R.id.et_personlastName)).setError("El cognom es obligatori.");
            ((EditText) findViewById(R.id.et_personlastName)).requestFocus();
            return;
            //si no pones horas, salta el siguiente error
        } else if (TextUtils.isEmpty(hores)) {
            ((EditText) findViewById(R.id.hores)).setError("El num d'hores es obligatori.");
            ((EditText) findViewById(R.id.hores)).requestFocus();
            return;
            //si no pones fecha, salta el siguiente error
        } else if (TextUtils.isEmpty(inici)) {
            ((TextView) findViewById(R.id.efecha)).setError("La fecha es obligatoria.");
            ((TextView) findViewById(R.id.efecha)).requestFocus();
            Toast.makeText(NewPostActivity.this,
                    "La fecha es obligatoria", Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(fi)) {
            ((TextView) findViewById(R.id.efecha2)).setError("La fecha es obligatoria.");
            ((TextView) findViewById(R.id.efecha2)).requestFocus();
            Toast.makeText(NewPostActivity.this,
                    "La fecha es obligatoria", Toast.LENGTH_LONG).show();
            return;
        }

        ///////////
        final Formulario formulario = new Formulario();
        formulario.AAdhForm = dhForm;
        formulario.ABid_movil = id_movil;
        formulario.ACdNI = dni;
        formulario.ADnombre = nombre;
        formulario.AEapellidos = apellidos;
        formulario.AFinici = inici;
        formulario.AGfi = fi;
        formulario.AHhores = hores;
        formulario.AIdestinatari = destinatari;
        formulario.AJarea = area;
        formulario.AKambit = ambit;
        formulario.ALfamiliar = familiar;
        formulario.AMtipus = tipus;
        formulario.ANtipusF = tipusF;
        formulario.AOobservaciones = observaciones;
        formulario.AQcheck = check;

//si hay foto, que la envie
        if (photoConf) {
            StorageReference mStorageRef;

            mStorageRef = FirebaseStorage.getInstance().getReference();

            Uri file = Uri.fromFile(new File(cameraPhotoPath));
            StorageReference riversRef = mStorageRef.child("imagenes/" + photoName);

            riversRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            formulario.ARimg = downloadUrl.toString();

                            if (check == false){
                                return;
                            }
                            // si hay foto, se envia a formularios justificados
                            else if (check == true){
                                FirebaseDatabase.getInstance().getReference().child("formularios_justificados").child(formId).setValue(formulario);
                            // si no hay foto, se envia a formularios no justificados
                            } else {
                                FirebaseDatabase.getInstance().getReference().child("formularios_noJustificados").child(formId).setValue(formulario);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            formulario.ARimg = null;

                            if (check == false){
                                return;
                            }

                            else if (check == true){
                                FirebaseDatabase.getInstance().getReference().child("formularios_justificados").child(formId).setValue(formulario);

                            } else {
                                FirebaseDatabase.getInstance().getReference().child("formularios_noJustificados").child(formId).setValue(formulario);
                            }
                        }
                    });
            //formulario.img = "imagenes/"+photoName;

        } else {
            formulario.ARimg = "null";
            if (check==false){
                FirebaseDatabase.getInstance().getReference().child("formularios_noJustificados").child(formId).setValue(formulario);
            }
            else if (check == true){
                return;
            }
        }
        //String formId = FirebaseDatabase.getInstance().getReference().child("formularios").push().getKey();

        //Variables para cada uno de los elementos del envio del mensaje
        //Variables de tipo String para escritura
        String dni_m = ((EditText) findViewById(R.id.et_persondni)).getText().toString();
        String name_m = ((EditText) findViewById(R.id.et_personName)).getText().toString();
        String lastName_m = ((EditText) findViewById(R.id.et_personlastName)).getText().toString();
        String inici_m = ((TextView) findViewById(R.id.efecha)).getText().toString();
        String fi_m = ((TextView) findViewById(R.id.efecha2)).getText().toString();
        String hores_m = ((EditText) findViewById(R.id.hores)).getText().toString();
        String dest_m = ((Spinner) findViewById(R.id.dest)).getSelectedItem().toString();
        String area_m = ((Spinner) findViewById(R.id.area)).getSelectedItem().toString();
        String ambit_m = ((Spinner) findViewById(R.id.ambit)).getSelectedItem().toString();
        String familiar_m = ((EditText) findViewById(R.id.familiar)).getText().toString();
        String tipus_m = ((Spinner) findViewById(R.id.tipus)).getSelectedItem().toString();
        String finalT_m = ((Spinner) findViewById(R.id.finalT)).getSelectedItem().toString();
        String observacions_m = ((EditText) findViewById(R.id.editTextObs)).getText().toString();
        boolean check_m = ((CheckBox) findViewById(R.id.checkBox)).isChecked();
        //String imagenmostrar   = your_message.getText().toString();


        //Hacemos un intento llamado sendEmail para el envio de datos
        Intent sendEmail = new Intent(android.content.Intent.ACTION_SEND);

        String subject = ("Solicitud Formulario: " + this.dhForm);
    //si el ambito es familiar se envian estos datos
        if (ambit.equals("Familiar")) {
            // Se llena con datos
            sendEmail.setType("plain/text");
            sendEmail.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"ftallers@fundaciotallers.org"});
            sendEmail.putExtra(Intent.EXTRA_SUBJECT, subject);
            sendEmail.putExtra(android.content.Intent.EXTRA_TEXT,
                    "Dni: " + dni_m + '\n'
                            + "Nom: " + name_m + '\n'
                            + "Cognom: " + lastName_m + '\n'
                            + "Inici: " + inici_m + '\n'
                            + "Fi: " + fi_m + '\n'
                            + "Hores: " + hores_m + '\n'
                            + "Destinatari: " + dest_m + '\n'
                            + "Área: " + area_m + '\n'
                            + "Ambit: " + ambit_m + '\n'
                            + "Familiar: " + familiar_m + '\n'
                            + "Duració: " + tipus_m + '\n'
                            + "Motiu: " + finalT_m + '\n'
                            + "Observacions: " + observacions_m + '\n'
                            + "Justificado: " + check_m);

            if (photoConf) {
                Uri file = Uri.fromFile(new File(cameraPhotoPath));
                sendEmail.putExtra(Intent.EXTRA_STREAM, file);
            }
            sendEmail.setType("image/png");


            startActivity(Intent.createChooser(sendEmail, "Enviando mail..."));

        } else {
            // Se llena con datos
            sendEmail.setType("plain/text");
            sendEmail.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"ftallers@fundaciotallers.org"});
            sendEmail.putExtra(Intent.EXTRA_SUBJECT, subject);
            sendEmail.putExtra(android.content.Intent.EXTRA_TEXT,
                    "Dni: " + dni + '\n'
                            + "Nom: " + name_m + '\n'
                            + "Cognom: " + lastName_m + '\n'
                            + "Inici: " + inici_m + '\n'
                            + "Fi: " + fi_m + '\n'
                            + "Hores: " + hores_m + '\n'
                            + "Destinatari: " + dest_m + '\n'
                            + "Área: " + area_m + '\n'
                            + "Ambit: " + ambit_m + '\n'
                            + "Duració: " + tipus_m + '\n'
                            + "Motiu: " + finalT_m + '\n'
                            + "Observacions: " + observacions_m + '\n'
                            + "Justificado: " + check_m);
            if (photoConf) {
                Uri file = Uri.fromFile(new File(cameraPhotoPath));
                sendEmail.putExtra(Intent.EXTRA_STREAM, file);
            }
            sendEmail.setType("image/png");

            startActivity(Intent.createChooser(sendEmail, "Enviando mail..."));
        }
    }

    public void selectPersonal() {
        familiartv.setVisibility(View.INVISIBLE);
        familiar.setVisibility(View.INVISIBLE);
        tipus.setAdapter(adapterTP);
        tipus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    finalT.setAdapter(adapterFinal1);
                } else if (position == 1) {
                    finalT.setAdapter(adapterFinal2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    public void selectFamiliar() {
        familiartv.setVisibility(View.VISIBLE);
        familiar.setVisibility(View.VISIBLE);
        tipus.setAdapter(adapterTF);
        tipus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    finalT.setAdapter(adapterFinal3);
                } else if (position == 1) {
                    finalT.setAdapter(adapterFinal4);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

}
