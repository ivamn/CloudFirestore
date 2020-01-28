package com.example.cloudfirestore;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

public class Holder extends RecyclerView.ViewHolder {
    private TextView textoCiudad, textPais;
    private ImageView imageView;

    public Holder(View v) {
        super(v);
        textoCiudad = v.findViewById(R.id.textCiudad);
        textPais = v.findViewById(R.id.textPais);
        imageView = v.findViewById(R.id.imageView);
    }

    public void bind(Ciudad item) {
        textoCiudad.setText(item.getCiudad());
        textPais.setText(item.getPais());
        StorageReference reference = FirebaseStorage.getInstance().getReference(item.getImagen());
        reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri uri = task.getResult();
                Picasso.get().load(task.getResult()).into(imageView);
            }
        });
    }
}