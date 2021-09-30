package com.evs.foodexp.commonPkg.common.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.evs.foodexp.R;
import com.evs.foodexp.utils.Global;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class AddYourLoaction extends AppCompatActivity implements OnMapReadyCallback {//, LocationListener

    private GoogleMap mMap;
    String TAG = "places Api";
    AutocompleteSupportFragment place_autocomplete_pickup_loc;
    //TextView txtInfo;
    String Str_pickLocET = "";
    SharedPreferences prefs;
    String lat = "", longs = "", address = "";
    Button btn_done;
    Location mylocation;
    private FusedLocationProviderClient mFusedLocationClient;


    //    AIzaSyB-jW6b8tu2vqcrO5x17PM2FY0iP81YQus
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_your_loaction);
        //txtInfo = (TextView) findViewById(R.id.txtInfo);
        if (!Global.CheckGpsLocation(AddYourLoaction.this)) {
            Global.showGPSDisabledAlertToUser(AddYourLoaction.this);
        }
        prefs = PreferenceManager.getDefaultSharedPreferences(AddYourLoaction.this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btn_done = findViewById(R.id.btn_done);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        place_autocomplete_pickup_loc = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_pickup_loc);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getResources().getString(R.string.location_key));
        }
        place_autocomplete_pickup_loc.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,
                Place.Field.LAT_LNG, Place.Field.ADDRESS));

        ((EditText) place_autocomplete_pickup_loc.getView().findViewById(R.id.places_autocomplete_search_input)).setBackground(getResources().getDrawable(R.drawable.adjustmentstyle2));
        ((ImageButton) place_autocomplete_pickup_loc.getView().findViewById(R.id.places_autocomplete_search_button)).setVisibility(View.GONE);
        ((ImageButton) place_autocomplete_pickup_loc.getView().findViewById(R.id.places_autocomplete_clear_button)).setVisibility(View.GONE);
        ((EditText) place_autocomplete_pickup_loc.getView().findViewById(R.id.places_autocomplete_search_input)).setTextSize(16.0f);
        ((EditText) place_autocomplete_pickup_loc.getView().findViewById(R.id.places_autocomplete_search_input)).setText(address);
        //((ImageButton) place_autocomplete_pickup_loc.getView().findViewById(R.id.place_autocomplete_clear_button)).setImageResource(R.drawable.xblack);
        ((ImageButton) place_autocomplete_pickup_loc.getView().findViewById(R.id.places_autocomplete_clear_button)).setBackgroundColor(Color.WHITE);
        place_autocomplete_pickup_loc.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                mMap.clear();
                ((ImageButton) place_autocomplete_pickup_loc.getView().findViewById(R.id.places_autocomplete_clear_button)).setVisibility(View.GONE);

                Log.i(TAG, "Place: " + place.getName());
                Str_pickLocET = place.getAddress();
//                Log.d("searched location", Str_pickLocET);
//                Log.d("searched location", String.valueOf(place.getLatLng()));
                // pickLocET.setText(Str_pickLocET);
                lat = String.valueOf(place.getLatLng().latitude);
                longs = String.valueOf(place.getLatLng().longitude);
                address = String.valueOf(place.getAddress());

                Marker marker=  mMap.addMarker(new MarkerOptions().position(place.getLatLng())
                        .title(String.valueOf(place.getName()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_blue)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
                marker.showInfoWindow();


            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        place_autocomplete_pickup_loc.getView().findViewById(R.id.places_autocomplete_clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Place: " + "");
                Str_pickLocET = "";
                place_autocomplete_pickup_loc.setText(Str_pickLocET);
                Log.d("searched location", Str_pickLocET);

                view.setVisibility(View.GONE);
            }
        });
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address.length() != 0) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("address", address);
                    returnIntent.putExtra("lat", lat);
                    returnIntent.putExtra("long", longs);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    Toast.makeText(AddYourLoaction.this, "Please Select Address", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mMap != null) {
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    // txtInfo.setText(marker.getTitle().toString() + " Lat:" + marker.getPosition().latitude + " Long:" + marker.getPosition().longitude);
                    return false;
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Global.CheckGpsLocation(AddYourLoaction.this)) {
            Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                    .withListener(new MultiplePermissionsListener() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                // do you work now
                                mFusedLocationClient.getLastLocation().addOnSuccessListener(AddYourLoaction.this, new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        // Got last known location. In some rare situations this can be null.
                                        if (location != null) {
                                            // Logic to handle location object
                                            mylocation = location;

                                            if (!Global.validatename(address)) {
                                                address = getAddress(mylocation);
                                                lat = String.valueOf(mylocation.getLatitude());
                                                longs = String.valueOf(mylocation.getLongitude());
//                                                MarkerOptions markerOptions=new MarkerOptions();
//                                                markerOptions.position(new LatLng(mylocation.getLatitude(), mylocation.getLongitude()))
//                                                        .title(address)
//                                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.location164));

//                                                mMap.addMarker(markerOptions);
                                                Marker marker = mMap.addMarker(new MarkerOptions()
                                                        .position(new LatLng(mylocation.getLatitude(), mylocation.getLongitude()))
                                                        .title(address)
                                                        .icon(BitmapDescriptorFactory
                                                                .fromResource(R.drawable.location_blue)));

                                                marker.showInfoWindow();
                                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mylocation.getLatitude(), mylocation.getLongitude()), 15));
                                            }
                                        }

                                    }
                                });
                            }

                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                // permission is denied permenantly, navigate user to app settings
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
//                                startActivityForResult(intent, 101);
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        } else {
            Global.showGPSDisabledAlertToUser(AddYourLoaction.this);
        }
    }

    private String getAddress(Location latlang) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        String address = "";
        try {
            addresses = geocoder.getFromLocation(latlang.getLatitude(), latlang.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5


            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }





}

