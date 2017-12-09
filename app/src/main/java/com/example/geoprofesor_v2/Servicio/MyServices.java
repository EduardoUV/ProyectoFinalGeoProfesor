package com.example.geoprofesor_v2.Servicio;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;
import com.example.geoprofesor_v2.Objetos.Maestros;
import com.example.geoprofesor_v2.Objetos.Referencias;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

// EN EL TOQUE DEL BOTON PARA VERIFICAR

public class MyServices extends Service implements OnMapReadyCallback {

    public static LocationManager locationmanager;
    public static Location location;
    public static LocationListener locationlistener;

    double lat = 0.0;
    double lng = 0.0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        miUbicacion();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FirebaseCordenadas(0.0, 0.0);
        locationmanager.removeUpdates(locationlistener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    }

    public void actualizarUbicacion(Location location) {

        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            float resultado[] = new float[1];

            Location.distanceBetween(lat, lng, 19.1654923, -96.1142007,  resultado);
            // 19.1654923, -96.1142007 (FIUV)
            // 19.160229, -96.110318 (USBI)

            if (resultado[0] < 83) {
                //COMIENZA A MANDAR LOS DATOS DE SU UBICACION

                FirebaseCordenadas(lat, lng);
                Toast.makeText(this, "Dentro: " + resultado[0], Toast.LENGTH_LONG).show();
            } else {

                FirebaseCordenadas(0.0, 0.0);
                Toast.makeText(this, "Fuera: " + resultado[0], Toast.LENGTH_LONG).show();
            }
        }
    }

    public static void FirebaseCordenadas(double lat, double lng) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference FIUV = database.getReference(Referencias.PRINCIPAL_REFERENCE);
        Maestros m = new Maestros();

        // AQUI DEBE RECIBIR LO QUE SE GUARDA DE FORMA LOCAL CON EL PREFERENCE RECUERDA!!!!
        m.id = Referencias.IdSave;
        m.Nombre = Referencias.NombreSave;
        m.Carrera = Referencias.CarreraSave;
        m.Latitud = lat;
        m.Longitud = lng;

        Map<String, Object> maestro = new HashMap<>();

        maestro.put(Referencias.RUTA, m); // aqui ingresa el dato guardado

        FIUV.updateChildren(maestro);

    }

    LocationListener locListener = new LocationListener() {


        @Override
        public void onLocationChanged(Location location) {
            actualizarUbicacion(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getBaseContext(), "Activado el GPS", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getBaseContext(), "Desactivado el GPS", Toast.LENGTH_SHORT).show();
        }
    };


    // VALIDANDO SOLO EL SERVICIO
    public void miUbicacion() {
        // TAMBIEN EN EL LA CLASE SERVICIO ONCOMMAND

        locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        location = locationmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        actualizarUbicacion(location);
        locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, locListener);  // el tiempo esta en milisegundos 1.5 minutos =  90,000 milisegunos
        locationlistener = locListener;

    }


}