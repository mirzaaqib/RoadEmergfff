package com.mirzaaqibbeg.roademergencies;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;
import com.mirzaaqibbeg.roademergencies.DataStructure.Mechanic;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MedcareActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient googleApiClient;
    private Mechanic mechanic;
    private LocationListener listener;
    private LocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medcare);


        googleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        googleApiClient.connect();

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	   Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	   if (location != null) {
		  Fields.LATITUDE = location.getLatitude();
		  Fields.LONGITUDE = location.getLongitude();
	   }
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Fields.LATITUDE = latitude;
                Fields.LONGITUDE = longitude;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        String locationProvider = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (manager.isProviderEnabled(locationProvider)) {
            manager.requestLocationUpdates(locationProvider, 1, 1, listener);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage("GPS not enable!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    });
            builder.create().show();
        }


        List<Integer> filters = new ArrayList<>();
        filters.add(Place.TYPE_HOSPITAL);
        filters.add(Place.TYPE_PHARMACY);
//        filters.add(Place.TYPE_ESTABLISHMENT);
        PlaceFilter filter = new PlaceFilter(filters, false, null, null);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(googleApiClient, filter);

        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(final PlaceLikelihoodBuffer placeLikelihoods) {
                ArrayList<PlaceLikelihood> arrayList = new ArrayList<>();
                for (PlaceLikelihood placeLikel : placeLikelihoods) {
                    arrayList.add(placeLikel);
                }
                ListView view = (ListView) findViewById(R.id.listMedCare);
                view.setAdapter(new Medadapter(arrayList));
                view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MedcareActivity.this, MechanicDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Fields.MECHANIC, mechanic);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        });

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "connected", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

        Toast.makeText(this, "connected", Toast.LENGTH_LONG).show();
    }

    class Medadapter extends BaseAdapter {


        ArrayList<PlaceLikelihood> placeLike;


        public Medadapter(ArrayList<PlaceLikelihood> arrayList) {
            this.placeLike = arrayList;
        }

        @Override
        public int getCount() {
            return placeLike.size();
        }

        @Override
        public Object getItem(int position) {
            return placeLike.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getApplicationContext());
                v = vi.inflate(R.layout.view_list_medcare, null);
            }
            TextView cName = (TextView) v.findViewById(R.id.cname);
            TextView cAddress = (TextView) v.findViewById(R.id.caddress);
            TextView cPhone = (TextView) v.findViewById(R.id.cphone);
            PlaceLikelihood likelihood = placeLike.get(position);
            final Place place = likelihood.getPlace();


            cPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = "tel:" + place.getPhoneNumber();
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
                }
            });
            mechanic = new Mechanic();
            mechanic.setName(String.valueOf(place.getName().toString()));
            mechanic.setM_id(place.getId());
            mechanic.setLatitude(String.valueOf(place.getLatLng().latitude));
            mechanic.setLongitude(String.valueOf(place.getLatLng().longitude));
            mechanic.setNumber(String.valueOf(place.getPhoneNumber()));
            mechanic.setType(String.valueOf(place.getPlaceTypes()));
            mechanic.setAddress(String.valueOf(place.getAddress()));
            mechanic.setDescription(String.valueOf(place.getAddress()));
            mechanic.setEmail_id(String.valueOf(place.getWebsiteUri()));


            cName.setText(place.getName());
            cAddress.setText(place.getAddress());
            cPhone.setText(place.getPhoneNumber());
            return v;
        }
    }
}
