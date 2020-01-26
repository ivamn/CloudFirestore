package com.example.cloudfirestore;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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
    }
}