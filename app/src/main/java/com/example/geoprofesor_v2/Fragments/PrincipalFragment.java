package com.example.geoprofesor_v2.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.geoprofesor_v2.Objetos.Referencias;
import com.example.geoprofesor_v2.R;
import com.example.geoprofesor_v2.Servicio.MyServices;

import java.sql.Ref;


public class PrincipalFragment extends Fragment implements View.OnClickListener {

    Button btnfragComenzar, btnfragTerminar;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_principal, container, false);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentActivity view = getActivity();

        btnfragComenzar = (Button) view.findViewById(R.id.btnComenzar);
        btnfragComenzar.setOnClickListener(this);

        btnfragTerminar = (Button) view.findViewById(R.id.btnTerminnar);
        btnfragTerminar.setOnClickListener(this);

        sharedPreferences = view.getSharedPreferences("Preference", Context.MODE_PRIVATE);

        if (sharedPreferences.contains("id")) {
            Referencias.RUTA = Referencias.OBJETO_REFERENCE + sharedPreferences.getString("id", "");
            Referencias.IdSave = sharedPreferences.getString("id", "");
        }

        if (sharedPreferences.contains("usuario")) {
            Referencias.NombreSave = sharedPreferences.getString("usuario", "");
        }

        if (sharedPreferences.contains("carrera")) {
            Referencias.CarreraSave = sharedPreferences.getString("carrera", "");
        }

    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {

            case R.id.btnComenzar:
                intent = new Intent(getContext(), MyServices.class);
                getActivity().startService(intent);
                break;

            case R.id.btnTerminnar:
                intent = new Intent(getContext(), MyServices.class);


                getActivity().stopService(intent);
                break;
        }

    } //fin del switch


}