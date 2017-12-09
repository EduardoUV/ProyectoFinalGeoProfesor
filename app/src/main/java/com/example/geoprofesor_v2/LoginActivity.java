package com.example.geoprofesor_v2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    // Agregamos variables
    private EditText edtUsuario;
    private EditText edtPassword;
    private Button btningreso;
    private TextView txtregistro;

    //objeto Progressdialog
    private ProgressDialog progressDialog;

    //Objeto FirebaseAuth
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsuario = (EditText) findViewById(R.id.txtuser);
        edtPassword = (EditText) findViewById(R.id.txtpass);
        btningreso = (Button) findViewById(R.id.btningreso);
        txtregistro = (TextView) findViewById(R.id.txtregistro);

        //context = this
        progressDialog = new ProgressDialog(this);

        // paso 2 firebase inicializando el objeto
        firebaseAuth = FirebaseAuth.getInstance();


        if(firebaseAuth.getCurrentUser()!= null){
        }


    }


    public void login(){
        String email = edtUsuario.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();


        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Falta ingresar el usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Falta ingresar la contraseña", Toast.LENGTH_SHORT).show();
            return;
        }


        progressDialog.setMessage("Accediendo a su cuenta...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password) // logearse con usuarios ya creados
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            //iniciamos el perfil
                            startActivity(new Intent(getApplicationContext(),PerfilActivity.class));
                        }
                    }
                });
    }


    public void onClick(View v) {

        //Boton de ingreso
        if (v == btningreso) {
            login();
        }

        //Abre activity FormActivity para registro
        if (v == txtregistro) {

            Intent intent = new Intent(this, FormActivity.class);
            startActivity(intent);
        }
    }



}