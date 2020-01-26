package com.example.cloudfirestore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class Adapter extends FirestoreRecyclerAdapter<Ciudad, Holder> implements View.OnClickListener, View.OnLongClickListener {
    private View.OnClickListener clickListener;
    private View.OnLongClickListener longClickListener;

    Adapter(@NonNull FirestoreRecyclerOptions<Ciudad> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull Ciudad model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.holder, viewGroup, false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new Holder(view);
    }

    void setOnClickListener(View.OnClickListener listener) {
        this.clickListener = listener;
    }

    void setOnLongClickListener(View.OnLongClickListener listener) {
        this.longClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (clickListener != null) clickListener.onClick(v);
    }

    @Override
    public boolean onLongClick(View v) {
        if (longClickListener != null) {
            longClickListener.onLongClick(v);
        }
        return true;
    }
}
