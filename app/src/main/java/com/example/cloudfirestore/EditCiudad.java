package com.example.cloudfirestore;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class EditCiudad extends AppCompatActivity {

    private final String DEFAULT_IMAGE = "/default.jpg";

    private Ciudad c;
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

        if (c.getImagen() == null || c.getImagen().equals("")) {
            StorageReference reference = FirebaseStorage.getInstance().getReference(DEFAULT_IMAGE);
            reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Picasso.get().load(task.getResult()).into(imagen);
                }
            });
        } else {
            StorageReference reference = FirebaseStorage.getInstance().getReference(c.getImagen());
            reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Picasso.get().load(task.getResult()).into(imagen);
                }
            });
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
            c.setImagen(rutaImagen.toString());
            imagen.setImageURI(rutaImagen);

        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Se ha cancelado la operación", Toast.LENGTH_SHORT).show();
        }
    }
}
