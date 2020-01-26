package com.example.cloudfirestore;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText usuario, pass;
    private Button crear, entrar;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuario = findViewById(R.id.editUsuario);
        pass = findViewById(R.id.editPass);
        crear = findViewById(R.id.buttonCrear);
        entrar = findViewById(R.id.buttonEntrar);

        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(MainActivity.this, user.getEmail() + " logeado", Toast.LENGTH_LONG).show();
                    auth = firebaseAuth;
                }
            }
        };

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.createUserWithEmailAndPassword(usuario.getText().toString(), pass.getText().toString())
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Usuario creado", Toast.LENGTH_LONG).show();
                                    iniciarAplicacion(task.getResult().getUser().getEmail().split("@")[0]);
                                } else {
                                    Toast.makeText(MainActivity.this, "Problemas al crear el usuario", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signInWithEmailAndPassword(usuario.getText().toString(), pass.getText().toString())
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    iniciarAplicacion(task.getResult().getUser().getEmail().split("@")[0]);
                                } else {
                                    Toast.makeText(MainActivity.this, "Error de autenticación", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

    }

    private void iniciarAplicacion(String user) {
        Intent intent = new Intent(this, MainApplication.class);
        intent.putExtra("usuario", user);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (auth != null) {
            auth.removeAuthStateListener(authListener);
            auth.signOut();
        }
    }
}