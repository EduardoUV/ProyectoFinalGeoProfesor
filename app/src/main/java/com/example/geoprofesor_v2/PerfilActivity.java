package com.example.geoprofesor_v2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.geoprofesor_v2.Fragments.AyudaFragment;
import com.example.geoprofesor_v2.Fragments.FormFragment;
import com.example.geoprofesor_v2.Fragments.ObjetivoFragment;
import com.example.geoprofesor_v2.Fragments.PrincipalFragment;

public class PerfilActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    protected void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_perfil);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        remplace(new PrincipalFragment()); // Se infla en el container automaticamente al pasar del login

        // si existe un toolbar se le da soporte sino no.
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.navview);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Principal:
                        remplace(new PrincipalFragment());
                        break;

                    case R.id.Objetivo:
                        remplace(new ObjetivoFragment());
                        break;

                    case R.id.Ingresa_tus_datos:
                        remplace(new FormFragment());
                        break;

                    case R.id.Ayuda:
                        remplace(new AyudaFragment());
                        break;
                }

                return false;

            }

        });

    }


    // nos permite ejecutar la llamada al NavigationView por medio del Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    // permite el cambio o "transaccion" del nuevo fragment seleccionado en el navigation o menu desplegable"
    public void remplace(Fragment f) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.Container, f);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();

    }


}