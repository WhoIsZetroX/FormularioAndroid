package com.example.sebas_pc.formularioandroid.view;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class NewPostActivity extends AppCompatActivity implements View.OnClickListener {

    //static final String DATEPICKERYEAR = "DATEPICKERYEAR";
    //static final String DATEPICKERMONTH = "DATEPICKERMONTH";
    //static final String DATEPICKERDAY = "DATEPICKERDAY";
    TextView tv, tv2;
    //public String inici = DATEPICKERYEAR + "/" + DATEPICKERMONTH + "/" + DATEPICKERDAY;
    //public String fi = DATEPICKERYEAR + "/" + DATEPICKERMONTH + "/" + DATEPICKERDAY;
    LinearLayout linearLayout;
    String photoPath;
    String cameraPhotoPath;
    private static final int PERMISSION_REQUEST_CODE = 1;

    String photoName;
    boolean photoConf=false;

    Button bfecha, bfecha2;
    EditText efecha, efecha2;
    private  int dia,mes,ano,hora,minutos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        findViews();

        bfecha = (Button)findViewById(R.id.bfecha);
        efecha = (EditText) findViewById(R.id.efecha);

        bfecha2 = (Button)findViewById(R.id.bfecha2);
        efecha2 = (EditText)findViewById(R.id.efecha2);

        bfecha.setOnClickListener(this);
        bfecha2.setOnClickListener(this);
        efecha.setOnClickListener(this);
        efecha2.setOnClickListener(this);

        final String formId = FirebaseDatabase.getInstance().getReference().child("formularios").push().getKey();

        linearLayout = findViewById(R.id.ll);

        findViewById(R.id.button_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    //If your app has access to the device’s storage, then print the following message to Android Studio’s Logcat//
                    Log.e("permission", "Permission already granted.");
                    dispatchTakePictureIntent();
                } else {
                    //If your app doesn’t have permission to access external storage, then call requestPermission//
                    requestPermission();
                }
            }
        });

        //Obtenemos la hora en tiempo real
        final String fecha = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a").format(Calendar.getInstance().getTime());
        //Obtenemos el fabricante y modelo del movil ademas de su ANDROID_ID
        String deviceName = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        String androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        //Los mostramos
        final String lala = deviceName + " " + androidId;


        //Listener para hacer la foto
        findViewById(R.id.button_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dispatchTakePictureIntent();
            }
        });

        //Una vez clickemos al boton de enviar hará esto
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dhForm = fecha;
                String id_movil = lala;
                String dni = ((EditText) findViewById(R.id.et_persondni)).getText().toString();
                String nombre = ((EditText) findViewById(R.id.et_personName)).getText().toString();
                String apellidos = ((EditText) findViewById(R.id.et_personlastName)).getText().toString();
                String inici = ((EditText) findViewById(R.id.efecha)).getText().toString();;
                String fi = ((EditText) findViewById(R.id.efecha2)).getText().toString();
                String hores = ((EditText) findViewById(R.id.hores)).getText().toString();
                String destinatari = ((Spinner) findViewById(R.id.dest)).getSelectedItem().toString();
                String area = ((Spinner) findViewById(R.id.area)).getSelectedItem().toString();
                String ambit = ((Spinner) findViewById(R.id.ambit)).getSelectedItem().toString();
                String familiar = ((EditText) findViewById(R.id.familiar)).getText().toString();
                String tipus = ((Spinner) findViewById(R.id.tipus)).getSelectedItem().toString();
                String tipusF = ((Spinner) findViewById(R.id.finalT)).getSelectedItem().toString();
                String observaciones = ((EditText) findViewById(R.id.editTextObs)).getText().toString();

                if (TextUtils.isEmpty(dni)){
                    ((EditText) findViewById(R.id.et_persondni)).setError("El DNI es obligatori.");
                    ((EditText) findViewById(R.id.et_persondni)).requestFocus();
                    return;
                }

                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

                /*
                try {
                    Date date1 = format.parse(inici);
                    Date date2 = format.parse(fi);
                    if (date1.compareTo(date2) <= 0) {

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                */

                /*
                if (TextUtils.isEmpty(name)){
                    nombre.setError("El nom es obligatori.");
                    nombre.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(lastName)){
                    apellido.setError("El cognom es obligatori.");
                    apellido.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(hores)){
                    horas.setError("El num d'hores es obligatori.");
                    horas.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(dest)){
                    destino.setError("El destinatari es obligatori.");
                    destino.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(familiar)){
                    family.setError("El num d'hores es obligatori.");
                    family.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(descripcio)){
                    descripcion.setError("La descripció es obligatoria.");
                    descripcion.requestFocus();
                    return;
                }
                */

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
                                    formulario.AQimg = downloadUrl.toString();
                                    FirebaseDatabase.getInstance().getReference().child("formularios").child(formId).setValue(formulario);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    formulario.AQimg = null;
                                    FirebaseDatabase.getInstance().getReference().child("formularios").child(formId).setValue(formulario);                                }
                            });
                    //formulario.img = "imagenes/"+photoName;

                }else{
                    formulario.AQimg = "null";
                    FirebaseDatabase.getInstance().getReference().child("formularios").child(formId).setValue(formulario);
                }
                //String formId = FirebaseDatabase.getInstance().getReference().child("formularios").push().getKey();


                //Variables para cada uno de los elementos del envio del mensaje


                //Variables para cada uno de los elementos del envio del mensaje
                //Variables de tipo String para escritura
                String dni_m = ((EditText) findViewById(R.id.et_persondni)).getText().toString();
                String name_m = ((EditText) findViewById(R.id.et_personName)).getText().toString();
                String lastName_m = ((EditText) findViewById(R.id.et_personlastName)).getText().toString();
                String inici_m = ((EditText) findViewById(R.id.efecha)).getText().toString();
                String fi_m = ((EditText) findViewById(R.id.efecha2)).getText().toString();
                String hores_m = ((EditText) findViewById(R.id.hores)).getText().toString();
                String dest_m = ((Spinner) findViewById(R.id.dest)).getSelectedItem().toString();
                String area_m = ((Spinner) findViewById(R.id.area)).getSelectedItem().toString();
                String ambit_m = ((Spinner) findViewById(R.id.ambit)).getSelectedItem().toString();
                String familiar_m = ((EditText) findViewById(R.id.familiar)).getText().toString();
                String tipus_m = ((Spinner) findViewById(R.id.tipus)).getSelectedItem().toString();
                String finalT_m = ((Spinner) findViewById(R.id.finalT)).getSelectedItem().toString();
                String observacions_m = ((EditText) findViewById(R.id.editTextObs)).getText().toString();
                //String imagenmostrar   = your_message.getText().toString();


                //Hacemos un intento llamado sendEmail para el envio de datos
                Intent sendEmail = new Intent(android.content.Intent.ACTION_SEND);

                String subject = ("Solicitud Formulario: "+fecha);

                if (ambit.equals("Familiar")){
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
                                    + "Observacions: " + observacions_m);
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
                                    + "Observacions: " + observacions_m);

                    if (photoConf) {
                        Uri file = Uri.fromFile(new File(cameraPhotoPath));
                        sendEmail.putExtra(Intent.EXTRA_STREAM, file);
                    }
                    sendEmail.setType("image/png");

                    startActivity(Intent.createChooser(sendEmail, "Enviando mail..."));
                }

            }
        });

        //Esto sera la parte de los Spinner, se abrirar unos al tiempo que otros se pondra invisibles
        Spinner desti = findViewById(R.id.dest);
        String[] itemsDesti = new String[]{"RRHH", "Cap de Departament"};
        ArrayAdapter<String> adapterDesti = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsDesti);
        desti.setAdapter(adapterDesti);

        Spinner area = findViewById(R.id.area);
        String[] itemsArea = new String[]{"Jardinería", "Neteja", "Ooilf"};
        ArrayAdapter<String> adapterArea = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsArea);
        area.setAdapter(adapterArea);

        //Esto sera la parte de los Spinner, se abrirar unos al tiempo que otros se pondra invisibles
        Spinner ambit = findViewById(R.id.ambit);
        String[] itemsA = new String[]{"Personal", "Familiar"};
        ArrayAdapter<String> adapterA = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsA);
        ambit.setAdapter(adapterA);

        final EditText familiar = findViewById(R.id.familiar);
        familiar.setVisibility(View.INVISIBLE);
        final TextView familiartv = findViewById(R.id.familiartv);
        familiartv.setVisibility(View.INVISIBLE);

        final Spinner tipus = findViewById(R.id.tipus);
        String[] itemsP = new String[]{"1 jornada", "Superior a una jornada"};
        final ArrayAdapter<String> adapterTP = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsP);
        String[] itemsF = new String[]{"En mateixa localitat", "Fora localitat"};
        final ArrayAdapter<String> adapterTF = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsF);

        final Spinner finalT = findViewById(R.id.finalT);
        String[] itemsFinal1 = new String[]{"Asistencia medica", "Curs formació"};
        final ArrayAdapter<String> adapterFinal1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsFinal1);
        String[] itemsFinal2 = new String[]{"Matrimoni", "Permis paternitat"};
        final ArrayAdapter<String> adapterFinal2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsFinal2);
        String[] itemsFinal3 = new String[]{"Defuncio", "Intervenció quirurgica"};
        final ArrayAdapter<String> adapterFinal3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsFinal3);
        String[] itemsFinal4 = new String[]{"Defuncio", "Intervenció quirurgica"};
        final ArrayAdapter<String> adapterFinal4 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsFinal4);

        ambit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position==0) {
                    familiartv.setVisibility(View.INVISIBLE);
                    familiar.setVisibility(View.INVISIBLE);
                    tipus.setAdapter(adapterTP);
                    tipus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            if (position==0) {
                                finalT.setAdapter(adapterFinal1);
                            } else if (position==1) {
                                finalT.setAdapter(adapterFinal2);
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                        }
                    });
                } else if (position==1) {
                    familiartv.setVisibility(View.VISIBLE);
                    familiar.setVisibility(View.VISIBLE);
                    tipus.setAdapter(adapterTF);
                    tipus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            if (position==0) {
                                finalT.setAdapter(adapterFinal3);
                            } else if (position==1) {
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
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        //Para poner la fecha de IniciData y FinalData en forma de seleccion

        //Picker 1

        // API >= 26
        //datePicker.updateDate(2000, 12, 31);
        //datePicker.setOnDateChangedListener(this);

        /*
           datePicker.init(
                getPreferences(MODE_PRIVATE).getInt(DATEPICKERYEAR, datePicker.getYear()),
                getPreferences(MODE_PRIVATE).getInt(DATEPICKERMONTH, datePicker.getMonth()),
                getPreferences(MODE_PRIVATE).getInt(DATEPICKERDAY, datePicker.getDayOfMonth()),
                this);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);

        //Picker2
        datePicker2= (DatePicker) findViewById(R.id.datePicker2);
        // API >= 26
        //datePicker.updateDate(2000, 12, 31);
        //datePicker.setOnDateChangedListener(this);
        datePicker2.init(
                getPreferences(MODE_PRIVATE).getInt(DATEPICKERYEAR, datePicker.getYear()),
                getPreferences(MODE_PRIVATE).getInt(DATEPICKERMONTH, datePicker.getMonth()),
                getPreferences(MODE_PRIVATE).getInt(DATEPICKERDAY, datePicker.getDayOfMonth()),
                this);
        */
    }


    /*
    //Metodo para el datepicker
    @Override
    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
        int id = datePicker.getId();
        switch(id){
            case R.id.datePicker:
                getPreferences(MODE_PRIVATE).edit()
                        .putInt(DATEPICKERYEAR, year)
                        .putInt(DATEPICKERMONTH, month)
                        .putInt(DATEPICKERDAY, day)
                        .apply();
                break;
        }
    }
    */

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
                                startActivity(new Intent(NewPostActivity.this, SignInActivity.class));
                                finish();
                            }
                        });
        }
        return true;
    }

    @Override
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

    private boolean checkPermission() {
        //Check for READ_EXTERNAL_STORAGE access, using ContextCompat.checkSelfPermission()//
        int result = ContextCompat.checkSelfPermission(NewPostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(NewPostActivity.this, Manifest.permission.CAMERA);

        //If the app does have this permission, then return true//
        if (result == PackageManager.PERMISSION_GRANTED || result2 == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            //If the app doesn’t have this permission, then return false//
            return false;
        }
    }

    void dispatchTakePictureIntent(){
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
        photoConf=true;
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


    void setPicture(){
        if(photoPath != null && !photoPath.isEmpty()) {
            LinearLayout view_instance = findViewById(R.id.ll);
            ViewGroup.LayoutParams params=view_instance.getLayoutParams();
            params.height=1500;
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


    void findViews() {
        //datePicker = findViewById(R.id.datePicker);
        //iniciData = findViewById(R.id.iniciData);

    }

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
                    efecha.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                }
            }
                    ,dia,mes,ano);
            datePickerDialog.show();
        } else if (v == bfecha2 || v == efecha2) {
            Calendar calendar = Calendar.getInstance();
            int dia = calendar.get(Calendar.YEAR);
            int mes = calendar.get(Calendar.MONTH);
            int ano = calendar.get(Calendar.DATE);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    efecha2.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                }
            }
                    ,dia,mes,ano);
            datePickerDialog.show();
        }

    }
}
