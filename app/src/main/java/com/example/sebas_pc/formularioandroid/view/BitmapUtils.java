package com.example.sebas_pc.formularioandroid.view;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.File;
import java.io.IOException;

/**
 * Clase para poder rotar la imagen cuando rotas el dispositivo.
 **/
public class BitmapUtils {
    public static Bitmap rotate(String path){
        // Inicializamos la variable
        int rotate = -1;
        try {
            //Pillamos la orientación del dispositivo
            int exifOrientation = new ExifInterface(new File(path).getAbsolutePath())
                    .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            // Usamos la condicion para definir que dependiendo de donde esté rotado el movil se rote la imagen
            if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)       rotate = 90;
            else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) rotate = 180;
            else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) rotate = 270;
        } catch (IOException e) {}
        Bitmap bitmap = BitmapFactory.decodeFile(path, new BitmapFactory.Options());
        if(rotate != -1) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotate);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }
}
