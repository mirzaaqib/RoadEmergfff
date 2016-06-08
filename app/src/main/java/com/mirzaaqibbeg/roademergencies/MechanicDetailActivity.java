package com.mirzaaqibbeg.roademergencies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mirzaaqibbeg.roademergencies.DataStructure.Mechanic;

import org.w3c.dom.Text;

public class MechanicDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Mechanic mechanic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_detail);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        mechanic= (Mechanic) bundle.getSerializable(Fields.MECHANIC);

        ImageView profile;
        TextView tName= (TextView) findViewById(R.id.mName);
        TextView tDescription= (TextView) findViewById(R.id.mdescription);
        TextView tDistance= (TextView) findViewById(R.id.distance);
        TextView tPhone= (TextView) findViewById(R.id.phoneNumber);

        tName.setText(mechanic.getName());
        tDescription.setText(mechanic.getDescription());
        tDistance.setText(mechanic.getDistance());
        tPhone.setText(mechanic.getNumber());

        SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        LatLng latLng = new LatLng(Double.valueOf(mechanic.getLatitude()), Double.valueOf(mechanic.getLongitude()));
//        LatLng latLng = new LatLng(22.75, 75.88);
        mMap.addMarker(new MarkerOptions().position(latLng).title(mechanic.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}
