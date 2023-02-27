package com.Fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonParser;
import com.location.FetchURL;
import com.location.TaskLoadedCallback;
import com.meghlaxshapplications.travelapp.MapsFragment;
import com.meghlaxshapplications.travelapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Context mContext;
    private GoogleMap mMap;
    private Polyline polyline;
    private MarkerOptions place1;
    private MarkerOptions place2;
    private String url;
    private FusedLocationProviderClient fusedLocationProviderClient;
    double currentLat = 0 , currentlog = 0;
    private LatLng placelatlong;
    private double lat , longi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);



        getSupportActionBar().hide();

        mContext = MapActivity.this;



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getcurrentLocation();


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;

        MapStyleOptions mapStyleOptions=MapStyleOptions.loadRawResourceStyle(this,R.raw.maps);
        googleMap.setMapStyle(mapStyleOptions);

        lat = 26.906937762280283;
        longi = 70.89576122275103;



         placelatlong = new LatLng(26.906937762280283,70.89576122275103);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(placelatlong,10));

        getnearbyhotels();












    }

    private void getnearbyhotels() {
      //  int i = "Hotel";

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" + "?location=" + lat + "," + longi + "&radius=8000" +
                "&types=" + "hotel" + "&sensor=true" + "&key=" + "AIzaSyB27Ma2ymN62i7d6sPGEBTJvBfzeuSbfsg";

        new PlaceTask().execute(url);

    }

    private void getcurrentLocation() {

        @SuppressLint("MissingPermission") Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location !=null){

                    currentLat = location.getLatitude();
                    currentlog = location.getLongitude();




                }

            }
        });

    }


    private class PlaceTask extends AsyncTask<String, Integer,String> {


        @Override
        protected String doInBackground(String... strings) {

            String data = null;
            try {
                 data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(String s) {

            new ParserTask().execute(s);
            super.onPostExecute(s);
        }
    }

    private String downloadUrl(String string) throws IOException {
        URL url = new URL(string);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream stream = connection.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        StringBuilder builder = new StringBuilder();

        String line = "";

        while ((line = reader.readLine())!=null){

            builder.append(line);

        }

        String data = builder.toString();
        reader.close();

        return data;



    }

    private class ParserTask extends AsyncTask<String,Integer,List<HashMap<String , String>>> {


        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {

            JsonParserp jsonParserp = new JsonParserp();
            List<HashMap<String ,String >> mapList = null;

            JSONObject object = null;

            try {
                 object = new JSONObject(strings[0]);

                 mapList = jsonParserp.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            mMap.clear();

            for (int i=0; i<hashMaps.size(); i++){

                HashMap<String , String> hashMaplist = hashMaps.get(i);

                double lat = Double.parseDouble(hashMaplist.get("lat"));
                double lng = Double.parseDouble(hashMaplist.get("lng"));
                 String name = hashMaplist.get("name");

                 LatLng latLng = new LatLng(lat,lng);

                 MarkerOptions options = new MarkerOptions();

                 options.position(latLng);
                 options.title(name);
                 mMap.addMarker(options);


            }
        }
    }


}




