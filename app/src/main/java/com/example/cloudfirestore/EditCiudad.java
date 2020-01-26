package com.example.cloudfirestore;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.TaskCompletionSource;

import java.io.ByteArrayOutputStream;

public class EditCiudad extends AppCompatActivity {

    Ciudad c;
    private EditText editCiudad, editPais;
    private ImageView imagen;
    private Button aceptar;
    private String key;
    private int COD_ELEGIR_IMAGEN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ciudad);
        key = (String) getIntent().getExtras().get("key");
        c = getIntent().getParcelableExtra("ciudad");

        editCiudad = findViewById(R.id.editCiudad);
        editPais = findViewById(R.id.editPais);
        imagen = findViewById(R.id.imageView);
        aceptar = findViewById(R.id.buttonAceptar);

        if (c.getImagen() != null && !c.getImagen().equals("")) {
            imagen.setImageBitmap(StringToBitMap(c.getImagen()));
        }
        editPais.setText(c.getPais());
        editCiudad.setText(c.getCiudad());

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                if (intent.resolveActivity(EditCiudad.this.getPackageManager()) != null) {
                    startActivityForResult(intent, COD_ELEGIR_IMAGEN);
                }
            }
        });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.setCiudad(editCiudad.getText().toString());
                c.setPais(editPais.getText().toString());
                Intent i = new Intent();
                i.putExtra("ciudad", c);
                i.putExtra("key", key);
                setResult(RESULT_OK, i);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COD_ELEGIR_IMAGEN && resultCode == RESULT_OK) {
            Uri rutaImagen = data.getData();
            c.setImagen(BitMapToString(bitmapFromUri(rutaImagen, getApplicationContext())));
            imagen.setImageBitmap(bitmapFromUri(rutaImagen, getApplicationContext()));
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Se ha cancelado la operación", Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap bitmapFromUri(Uri uri, Context context) {
        ImageView imageViewTemp = new ImageView(context);
        imageViewTemp.setImageURI(uri);
        BitmapDrawable d = (BitmapDrawable) imageViewTemp.getDrawable();
        return d.getBitmap();
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 2, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap convertirBytesBitmap(byte[] bytes) {
        try {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public byte[] convertirImagenBytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
