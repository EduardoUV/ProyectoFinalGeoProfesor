package com.example.geoprofesor_v2.Fragments;

import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geoprofesor_v2.Objetos.Maestros;
import com.example.geoprofesor_v2.Objetos.Referencias;
import com.example.geoprofesor_v2.R; // para meter entre carpetas
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FormFragment extends Fragment implements View.OnClickListener {

    ProgressDialog progressDialog;
    TextView txtcarrera, txtFragId, txtFragUsuario, txtFragCarrera;
    Button btnfragingreso, btnfragcancelar;
    EditText txtnombre;
    SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentActivity view = getActivity();

        txtnombre = (EditText) view.findViewById(R.id.txtnombre);
        txtcarrera = (EditText) view.findViewById(R.id.txtcarrera);

        txtFragId = (TextView) view.findViewById(R.id.txtfragId);
        txtFragUsuario = (TextView) view.findViewById(R.id.txtfragUsuario);
        txtFragCarrera = (TextView) view.findViewById(R.id.txtfragCarrera);

        btnfragingreso = (Button) view.findViewById(R.id.btnfragingreso);
        btnfragingreso.setOnClickListener(this);

        btnfragcancelar = (Button) view.findViewById(R.id.btnfragcancelar);
        btnfragcancelar.setOnClickListener(this);

        sharedPreferences = view.getSharedPreferences("Preference", Context.MODE_PRIVATE);

        if (sharedPreferences.contains("id")) {
            txtFragId.setText(sharedPreferences.getString("id", ""));
        }

        if (sharedPreferences.contains("usuario")) {
            txtFragUsuario.setText(sharedPreferences.getString("usuario", ""));
        }

        if(sharedPreferences.contains("carrera")){
            txtFragCarrera.setText(sharedPreferences.getString("carrera", ""));
        }

        progressDialog = new ProgressDialog(getContext());


    }

    public String MAC(){
        WifiManager manager = (WifiManager) getActivity().getSystemService(getContext().WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();
        return address;
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnfragingreso:

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference FIUV = database.getReference(Referencias.PRINCIPAL_REFERENCE);

                Maestros m = new Maestros();

                //// actualizacion

                m.id = MAC();//idrandom();          // recibe numero random 10 (el objeto)
                m.Nombre = txtnombre.getText().toString();
                m.Carrera = txtcarrera.getText().toString();

                //POR SER GLOBALES Y LLAMARLOS CUANDO SE ACUTLICE LAS CORDENADAS
                Referencias.IdSave = String.valueOf(MAC()); //String.valueOf(m.id); // recibe el mismo numero random 10 (el sub-objeto dentro del servicio)
                Referencias.NombreSave = txtnombre.getText().toString();
                Referencias.CarreraSave = txtcarrera.getText().toString();

                //Referencias.IDSAVE = Referencias.OBJETO_REFERENCE + idrandom(); //DATOS10  recibe el DATOS + numero random 10

                m.Latitud = 0.0;
                m.Longitud = 0.0;

                Map<String, Object> maestro = new HashMap<>();

                maestro.put(Referencias.OBJETO_REFERENCE + m.id, m);

                SaveIdLocal();

                //maestro.put("DATOS10",m); Aqui especificas sobre quien vas a realizar modificaciones

                VerificarTexto(maestro, FIUV);
                break;

            case R.id.btnfragcancelar:

                txtnombre.setText("");
                txtcarrera.setText("");
                break;

        }
    } // fin del switch

    public void SaveIdLocal() {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("id", Referencias.IdSave);
        editor.putString("usuario", Referencias.NombreSave);
        editor.putString("carrera", Referencias.CarreraSave);
        editor.apply();

        txtFragId.setText(Referencias.IdSave);
        txtFragUsuario.setText(Referencias.NombreSave);
        txtFragCarrera.setText(Referencias.CarreraSave);

    }

    public void VerificarTexto(Map<String, Object> maestro, DatabaseReference FIUV) {

        String nombre = txtnombre.getText().toString().trim();
        String apellido = txtcarrera.getText().toString().trim();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido)) {
            Toast.makeText(getContext(), "Verifica el formulario y su llenado", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Cargando datos...");
        progressDialog.show();

        FIUV.updateChildren(maestro);

        txtnombre.setText("");
        txtcarrera.setText("");

        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                progressDialog.dismiss();
            }
        }.start();
    }




}