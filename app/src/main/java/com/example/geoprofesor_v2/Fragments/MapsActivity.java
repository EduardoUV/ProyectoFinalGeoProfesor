package com.example.geoprofesor_v2.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geoprofesor_v2.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marcador;
    double lat = 0.0;
    double lng = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if (status == ConnectionResult.SUCCESS) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity) getApplicationContext(), 10);
        }
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        miUbicacion();
    }






    private void agregarMarcador(double lat, double lng) {

        LatLng coordenadas = new LatLng(lat, lng);

        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 18);
        if (marcador != null) marcador.remove();
        marcador = mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("Posicion actual")
                .alpha(100)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
        mMap.animateCamera(miUbicacion);
    }






    private void actualizarUbicacion(Location location) {

        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            float resultado[] = new float[1];

            Location.distanceBetween(lat, lng, 19.1654923, -96.1142007, resultado);

            Circle circle = mMap.addCircle(new CircleOptions()
                    .center(new LatLng(19.1654923, -96.1142007))
                    .radius(80)
                    .strokeColor(Color.RED));

            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(lat, lng), new LatLng(19.1654923, -96.1142007))
                    .width(5)
                    .color(Color.GREEN));

            Log.v("Goooooooool","khvhjvhhv");

            if (resultado[0] < 80) {
                Toast.makeText(this, "Dentro: " + resultado[0], Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Fuera: " + resultado[0], Toast.LENGTH_LONG).show();
            }

            agregarMarcador(lat, lng);


        }
    }


    LocationListener locListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            miUbicacion();
            actualizarUbicacion(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(MapsActivity.this, "Activado el GPS", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(MapsActivity.this, "Desactivado el GPS", Toast.LENGTH_SHORT).show();
        }
    };


    // VALIDANDO SOLO EL SERVICIO

    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        // TAMBIEN EN EL LA CLASE SERVICIO ONCOMMAND

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualizarUbicacion(location);

        // esto va en el ONCOMMAND
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, locListener); // el tiempo esta en milisegundos 1 minutos =  60,000 milisegunos

        // locationManager.removeUpdates(LocListener); <--- onStop del servicio

    }

}
