package com.example.geoprofesor_v2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FormActivity extends AppCompatActivity {

    //declaracion de variables
    private EditText edtUsuario, edtPassword;
    private Button btnRegistrar;
    private ProgressDialog progressDialog;

    //variable de firebase
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        edtUsuario = (EditText) findViewById(R.id.edtuser);
        edtPassword = (EditText) findViewById(R.id.edtpass);
        btnRegistrar = (Button) findViewById(R.id.button);

        //context = this
        progressDialog = new ProgressDialog(this);

        // paso 2 firebase inicializando el objeto
        firebaseAuth = FirebaseAuth.getInstance();


    }

    private void usuarioregistrado() {
        String email = edtUsuario.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        final Intent intent = new Intent(this,LoginActivity.class);

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Falta ingresar el usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Falta ingresar la contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Cargando los datos al servidor");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password) // Crear nuevos usuarios en firebase
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(FormActivity.this, "Usuario registrado con exito", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();

                            startActivity(intent);

                        } else {
                            Toast.makeText(FormActivity.this, "Error al cargar al servidor, intentelo de nuevo", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    }
                });
    }

    public void onClickButtonRegistrar(View v) {
        if (v == btnRegistrar) {
            usuarioregistrado();
        }


    }

}