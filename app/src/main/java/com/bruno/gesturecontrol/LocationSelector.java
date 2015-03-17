package com.bruno.gesturecontrol;

import android.annotation.TargetApi;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class LocationSelector extends ActionBarActivity {

    private TextView nameLabel;
    private EditText nameEdit;
    private Button saveButton;
    private GoogleMap map;

    private CameraUpdate originCam;
    private CameraUpdate originZoom;
    private Location originLocation;

    private String NAME = null;
    private double LATITUDE = 0.0;
    private double LONGITUDE = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selector);

        nameLabel = (TextView) findViewById(R.id.nameLabel);
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setEnabled(false);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GestureActivation.class);
                intent.putExtra("latitude", LATITUDE);
                intent.putExtra("longitude", LONGITUDE);
                intent.putExtra("name", NAME);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setMyLocationEnabled(true);

        if (map.isMyLocationEnabled() && map.getMyLocation() != null) {
            originLocation = new Location(map.getMyLocation());
            originCam = CameraUpdateFactory.newLatLng(new LatLng(originLocation.getLatitude(), originLocation.getLongitude()));
            map.moveCamera(originCam);
        }

        originZoom = CameraUpdateFactory.zoomTo(7);
        map.animateCamera(originZoom);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                if (nameEdit.getText().toString().isEmpty()) {
                    map.clear();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.name_first_message), Toast.LENGTH_SHORT).show();
                    saveButton.setEnabled(false);
                } else {
                    map.clear();
                    markMap(nameEdit.getText().toString(), point.latitude, point.longitude);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location_selector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void markMap(String name, double latitude, double longitude) {
        //Add the marks on the map
        Geocoder geocoder = new Geocoder(LocationSelector.this, Locale.getDefault());
        String address = "";
        try {
            List<Address> addLines;
            addLines = geocoder.getFromLocation(latitude, longitude, 1);
            if (addLines.isEmpty()) {
                address = getResources().getString(R.string.unknown_address);
            }
            else {
                for (int i = 0; i < addLines.get(0).getMaxAddressLineIndex(); i++) {
                    address = address + addLines.get(0).getAddressLine(i) + ", ";
                }
                address = address + addLines.get(0).getCountryCode();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), address, Toast.LENGTH_LONG).show();

        map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(name)
                .snippet(address));

        NAME = name;
        LATITUDE = latitude;
        LONGITUDE = longitude;
        saveButton.setEnabled(true);
    }
}
