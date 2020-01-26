package com.example.cloudfirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import io.grpc.Context;

public class MainApplication extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private RecyclerView recycler;
    private Adapter adapter;
    private FloatingActionButton fab;
    private int COD_EDITAR = 2;
    private int COD_ADD = 3;
    private Uri u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_application);
        fab = findViewById(R.id.fab);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        recycler = findViewById(R.id.recycler);
        cargarDatos();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainApplication.this, AddCiudad.class);
                startActivityForResult(i, COD_ADD);
            }
        });
    }

    private void cargarDatos(){
        CollectionReference ref = db.collection("ciudades");
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    cargarRecycler(task.getResult().getQuery());
                } else {
                    Toast.makeText(MainApplication.this, "Fallo al leer datos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void cargarRecycler(Query q){
        FirestoreRecyclerOptions<Ciudad> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Ciudad>()
                .setQuery(q, Ciudad.class).build();
        adapter = new Adapter(firestoreRecyclerOptions);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarCiudad(recycler.getChildAdapterPosition(v));
            }
        });
        adapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                eliminarCiudad(recycler.getChildAdapterPosition(v));
                return false;
            }
        });
        adapter.startListening();
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void eliminarCiudad(int i) {
        String key = adapter.getSnapshots().getSnapshot(i).getId();
        db.collection("ciudades").document(key).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainApplication.this, "Se ha elimnado el registro", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainApplication.this, "Fallo al eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void editarCiudad(int item) {
        Intent i = new Intent(this, EditCiudad.class);
        Ciudad c = adapter.getItem(item);
        i.putExtra("ciudad", c);
        i.putExtra("key", adapter.getSnapshots().getSnapshot(item).getId());
        startActivityForResult(i, COD_EDITAR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COD_EDITAR && resultCode == RESULT_OK) {
            Ciudad c = (Ciudad) data.getExtras().get("ciudad");
            String key = (String) data.getExtras().get("key");
            uploadImage(c.getImagen());
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("ciudad", c.getCiudad());
            hashMap.put("pais", c.getPais());
            hashMap.put("imagen", c.getImagen());
            db.collection("ciudades").document(key).update(hashMap);
        } else if (requestCode == COD_ADD & resultCode == RESULT_OK) {
            Ciudad c = (Ciudad) data.getExtras().get("ciudad");
            db.collection("ciudades").document(c.getCiudad()).set(c);
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Se ha cancelado la operación", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage(String path){
        Uri uri = Uri.parse(path);
        StorageReference refSubida =storage.getReference().child("images/" + uri.getLastPathSegment());
        refSubida.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainApplication.this, "Se ha subido la imagen", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainApplication.this, "No se ha podido subir la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
