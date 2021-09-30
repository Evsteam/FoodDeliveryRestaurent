package com.evs.foodexp.driverPkg.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import  androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import androidx.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.commonPkg.viewModel.RestorentViewModel;
import com.evs.foodexp.location_services.DirectionsJSONParser;
import com.evs.foodexp.models.Comment;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.ImageFilePath;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;
import com.evs.foodexp.utils.Utills;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class DriverFoodInProgressActivity extends AppCompatActivity implements OnMapReadyCallback,
        AuthMsgListener {

    private GoogleMap mMap;
    Button btn_deliverd;
    boolean isGPSEnabled = false;
    public static double longitude = 0.0;
    public static double latitude = 0.0;
    int PERMISSION_ALL = 1;
    String address = "", imageUploade = "", orderStatus = "", landMark, name, number, dropuserlat, dropuserlong;
    TextView tv_Address, tv_title, tv_landMark, tv_timer;
//    String  pickupuserlat, pickUpaddress, pickUplandMark, pickupuserlong;
    SharedPreferences prefs;

    ImageView img_call;
    Comment comment;
    private FirebaseAuth firebaseAuth;
    DatabaseReference mDatabaseReference, reference_check, reference;
    String BookingId = "", selectedImagePath = "";
    //    String swap_value="2+1";
    ChildEventListener mChildEventListener;
    boolean timercheck = false;
    double dist = 0.0;
    LoaderProgress progress;
    RestorentViewModel viewModel;
    SharedPreferences prefes;
    boolean lineDrow = false;
    boolean lineDrow2 = false;

    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;
    FusedLocationProviderClient mFusedLocationProviderClient;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    boolean mapLoaded = false;
    Marker carMarker;
    float bearing;
    static MoveThread moveThread;
    static Handler handler;
    Location oldLocation;
    private static int SELECT_FILE = 11, REQUEST_CAMERA = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_food_in_progress);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        Utills.StatusBarColour(DriverFoodInProgressActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("On The Way");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        handler = new Handler();
        prefs = PreferenceManager.getDefaultSharedPreferences(DriverFoodInProgressActivity.this);
        btn_deliverd = (Button) findViewById(R.id.btn_deliverd);
        tv_Address = findViewById(R.id.tv_Address);
        img_call = findViewById(R.id.img_call);
        tv_title = findViewById(R.id.tv_title);
        tv_landMark = findViewById(R.id.tv_landMark);
        tv_timer = findViewById(R.id.tv_timer);
        tv_timer.setVisibility(View.GONE);
        SupportMapFragment mapFragmentUser = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.orderInProgressDriver);
        mapFragmentUser.getMapAsync(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            BookingId = bundle.getString("bookingId");
            address = bundle.getString("address");
            landMark = bundle.getString("landMark");
            name = bundle.getString("name");
            number = bundle.getString("number");
//            pickupuserlat = bundle.getString("pickupuserlat");
//            pickupuserlong = bundle.getString("pickupuserlong");
            dropuserlat = bundle.getString("dropuserlat");
            dropuserlong = bundle.getString("dropuserlong");
//            pickUplandMark = bundle.getString("pickUplandMark");
//            pickUpaddress = bundle.getString("pickUpaddress");
            imageUploade = bundle.getString("imageUploade");
            orderStatus = bundle.getString("orderStatus");
        }
        prefes = PreferenceManager.getDefaultSharedPreferences(DriverFoodInProgressActivity.this);
        progress = new LoaderProgress(DriverFoodInProgressActivity.this);
        viewModel = new ViewModelProvider(DriverFoodInProgressActivity.this).get(RestorentViewModel.class);
        tv_title.setText(name);
        tv_landMark.setVisibility(View.GONE);
        tv_Address.setText(address);

        img_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);
            }
        });


        timercheck = true;
//        Timer t = new Timer();
//        t.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                if (timercheck) {
//                    distance(Double.parseDouble(pickupuserlat), Double.parseDouble(pickupuserlong), latitude, longitude);
//                    if (dist < 0.05) {
//                        timercheck = false;
//                        tv_Address.setText(address);
//                        tv_landMark.setText(landMark);
//                        tv_landMark.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//        }, 0, 5000);

        btn_deliverd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(orderStatus.equalsIgnoreCase("1")){
                    if(Global.validatename(imageUploade)){
                        viewModel.excuteUpdateStatus(BookingId,
                        SessionManager.get_user_id(prefes), "2", "",DriverFoodInProgressActivity.this);
                    }else selectImage();
                }else {
                    selectImage();

                }


            }
        });


    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        CameraUpdate point = CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(dropuserlat)
                , Double.parseDouble(dropuserlong)), 8);
        mMap.moveCamera(point);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mapLoaded = true;
                mMap.getUiSettings().setAllGesturesEnabled(false);
                mMap.getUiSettings().setZoomControlsEnabled(false);
            }
        });


    }


    private boolean checkLocation() {
        int courcelocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int fineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (courcelocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (fineLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_ALL);
            return false;
        }
        return true;
    }


    private void SendLocation(String lat, String longs, final String bookingId, String address,
                              final String driverId) {
        comment = new Comment(lat, longs, bookingId, address, driverId);

        mDatabaseReference.child("liveTracking").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("value  ", "" + bookingId + "@" + driverId);
                Log.e("has child  ", "" + dataSnapshot.hasChild(bookingId + "@" + driverId));
                String key = mDatabaseReference.child("liveTracking").child(bookingId + "@" + driverId).push().getKey();
                Map<String, Object> postValues = comment.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(("/liveTracking/" + bookingId + "@" + driverId + "/") + key, postValues);
                mDatabaseReference.updateChildren(childUpdates);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Global.GpsEnable(DriverFoodInProgressActivity.this)) {
            if (checkLocation()) {
                createLocationRequest();
            }
        } else Global.showGPSDisabledAlertToUser(DriverFoodInProgressActivity.this);

    }


    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case 1: {
                createLocationRequest();
                return;
            }
            default:
                finish();
        }
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + Global.api_key;

        // Output format
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while in url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialog(DriverFoodInProgressActivity.this, message);
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        progress.dismiss();
        if (msgResponce.getStatus().equalsIgnoreCase("slipUploaded")) {
            imageUploade = "slipUploaded";
            btn_deliverd.setText("Confrim and Pickup");
        } else if (msgResponce.getStatus().equalsIgnoreCase("Confirm")) {
            btn_deliverd.setText("Mark as Delivered");
            orderStatus="2";
        } else {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(DriverFoodInProgressActivity.this);
            alertbox.setTitle(getResources().getString(R.string.app_name));
            alertbox.setMessage(msgResponce.getMsg());
            alertbox.setPositiveButton(getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(DriverFoodInProgressActivity.this, DriverHomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
            alertbox.show();

            stopLocationUpdates();

        }
    }

    class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }


        class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

            @Override
            protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

                JSONObject jObject;
                List<List<HashMap<String, String>>> routes = null;

                try {
                    jObject = new JSONObject(jsonData[0]);
                    DirectionsJSONParser parser = new DirectionsJSONParser();

                    // Starts parsing data
                    routes = parser.parse(jObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return routes;
            }

            @Override
            protected void onPostExecute(List<List<HashMap<String, String>>> result) {

                try {
                    ArrayList<LatLng> points = null;
                    PolylineOptions lineOptions = null;
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Traversing through all the routes
                    for (int i = 0; i < result.size(); i++) {
                        points = new ArrayList<LatLng>();
                        lineOptions = new PolylineOptions();

                        // Fetching i-th route
                        List<HashMap<String, String>> path = result.get(i);

                        // Fetching all the points in i-th route
                        for (int j = 0; j < path.size(); j++) {
                            HashMap<String, String> point = path.get(j);

                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);
                            points.add(position);
                        }
                        // Adding all the points in the route to LineOptions
                        lineOptions.addAll(points);
                        lineOptions.width(5);
                        lineOptions.color(Color.BLUE);
                    }
                    // Drawing polyline in the Google Map for the i-th route
                    mMap.addPolyline(lineOptions);
                } catch (Exception e) {

                }
            }
        }
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(1500);
        mLocationRequest.setSmallestDisplacement(50);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...

                startLocationUpdates();
            }
        });
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(DriverFoodInProgressActivity.this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }


    private void startLocationUpdates() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
//                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
//                    mMap.clear();
                if (locationResult.getLocations() != null) {
                    updateMarker(locationResult.getLocations().get(locationResult.getLocations().size()-1));


                        SendLocation("" + locationResult.getLocations().get(locationResult.getLocations().size()-1).getLatitude(), "" + locationResult.getLocations().get(locationResult.getLocations().size()-1).getLongitude(),
                                BookingId, address, SessionManager.get_user_id(prefs));


                    latitude = locationResult.getLocations().get(locationResult.getLocations().size()-1).getLatitude();
                    longitude = locationResult.getLocations().get(locationResult.getLocations().size()-1).getLongitude();

                    LatLng latLng = new LatLng(latitude, longitude);
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15.0f).build();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                    mMap.moveCamera(cameraUpdate);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);

//                    mMap.addMarker(new MarkerOptions()
//                            .position(new LatLng(latitude, longitude))
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bike))).showInfoWindow();

//                    if (timercheck) {
//                        if (!lineDrow) {
//                            mMap.addMarker(new MarkerOptions()
//                                    .position(new LatLng(Double.parseDouble(pickupuserlat), Double.parseDouble(pickupuserlong)))
//                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_red))
//                                    .title("Pickup Location")).showInfoWindow();
//
//                            String url = getDirectionsUrl(new LatLng(latitude, longitude), new LatLng(Double.parseDouble(pickupuserlat), Double.parseDouble(pickupuserlong)));
//                            DownloadTask downloadTask = new DownloadTask();
//                            downloadTask.execute(url);
//                            lineDrow = true;
//                        }
//                    } else {
                        if (!lineDrow2) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(dropuserlat), Double.parseDouble(dropuserlong)))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green))
                                    .title("Drop Location")).showInfoWindow();

                            String url = getDirectionsUrl(new LatLng(latitude, longitude), new LatLng(Double.parseDouble(dropuserlat), Double.parseDouble(dropuserlong)));
                            DownloadTask downloadTask = new DownloadTask();
                            downloadTask.execute(url);
                            lineDrow2 = true;
                        }
//                    }


//                    if (timercheck) {
//
//                        if (distance(Double.parseDouble(pickupuserlat), Double.parseDouble(pickupuserlong), latitude, longitude) < 0.2) {
//                            timercheck = false;
//                            lineDrow = true;
//                            tv_Address.setText(address);
//                            tv_landMark.setText(landMark);
//                            tv_landMark.setVisibility(View.VISIBLE);
//                            btn_deliverd.setVisibility(View.VISIBLE);
//                            if(orderStatus.equalsIgnoreCase("1")){
//                                if (Global.validatename(imageUploade)) {
//                                    btn_deliverd.setText("Confirm and Pickup");
//                                } else btn_deliverd.setText("Upload Pay slip");
//                            }else btn_deliverd.setText("Mark as Delivered");
//
//
//                        }
//                    }
                }
            }

            ;
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(getApplicationContext(), "location permission required !!", Toast.LENGTH_SHORT).show();
            return;
        }
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null /* Looper */);
//        Toast.makeText(getApplicationContext(),"Location update started",Toast.LENGTH_SHORT).show();
    }

    private void stopLocationUpdates() {

        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
//        Toast.makeText(getApplicationContext(),"Location update stopped.",Toast.LENGTH_SHORT).show();
    }

    private void updateMarker(Location location) {
        if (location == null) {
            return;
        }

        if (mMap != null && mapLoaded) {
            if (carMarker == null) {
                oldLocation = location;
                MarkerOptions markerOptions = new MarkerOptions();
                BitmapDescriptor car = BitmapDescriptorFactory.fromResource(R.drawable.bike);
                markerOptions.icon(car);
                markerOptions.anchor(0.5f, 0.5f); // set the car image to center of the point instead of anchoring to above or below the location
                markerOptions.flat(true); // set as true, so that when user rotates the map car icon will remain in the same direction
                markerOptions.position(new LatLng(location.getLatitude(), location.getLongitude()));
                carMarker = mMap.addMarker(markerOptions);
                if (location.hasBearing()) { // if location has bearing set the same bearing to marker(if location is acquired using GPS bearing will be available)
                    bearing = location.getBearing();
                } else {
                    bearing = 0; // no need to calculate bearing as it will be the first point
                }
                carMarker.setRotation(bearing);
                moveThread = new MoveThread();
                Log.e("update : Lat", location.getLatitude() + " Long " + location.getLongitude());
                moveThread.setNewPoint(new LatLng(location.getLatitude(), location.getLongitude()), 15);
                handler.post(moveThread);

            } else {
                if (location.hasBearing()) {// if location has bearing set the same bearing to marker(if location is acquired using GPS bearing will be available)
                    bearing = location.getBearing();
                } else { // if not, calculate bearing between old location and new location point
                    bearing = oldLocation.bearingTo(location);
                }
                carMarker.setRotation(bearing);
                moveThread.setNewPoint(new LatLng(location.getLatitude(), location.getLongitude()), mMap.getCameraPosition().zoom); // set the map zoom to current map's zoom level as user may zoom the map while tracking.
                animateMarkerToICS(carMarker, new LatLng(location.getLatitude(), location.getLongitude())); // animate the marker smoothly
            }
        } else {
            Log.e("map null or not loaded", "");
        }
    }

    private class MoveThread implements Runnable {
        LatLng newPoint;
        float zoom = 15;

        void setNewPoint(LatLng latLng, float zoom) {
            this.newPoint = latLng;
            this.zoom = zoom;
        }

        @Override
        public void run() {
            final CameraUpdate point = CameraUpdateFactory.newLatLngZoom(newPoint, zoom);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMap.animateCamera(point);
                }
            });
        }
    }

    static void animateMarkerToICS(Marker marker, LatLng finalPosition) {
        TypeEvaluator<LatLng> typeEvaluator = new TypeEvaluator<LatLng>() {
            @Override
            public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
                return interpolate(fraction, startValue, endValue);
            }
        };
        Property<Marker, LatLng> property = Property.of(Marker.class, LatLng.class, "position");
        ObjectAnimator animator = ObjectAnimator.ofObject(marker, property, typeEvaluator, finalPosition);
        animator.setDuration(1000);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                handler.post(moveThread);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }

    public static LatLng interpolate(float fraction, LatLng a, LatLng b) {
        // function to calculate the in between values of old latlng and new latlng.
        // To get more accurate tracking(Car will always be in the road even when the latlng falls away from road), use roads api from Google apis.
        // As it has quota limits I didn't have used that method.
        double lat = (b.latitude - a.latitude) * fraction + a.latitude;
        double lngDelta = b.longitude - a.longitude;

        // Take the shortest path across the 180th meridian.
        if (Math.abs(lngDelta) > 180) {
            lngDelta -= Math.signum(lngDelta) * 360;
        }
        double lng = lngDelta * fraction + a.longitude;
        return new LatLng(lat, lng);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }


    private void selectImage() {

        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(DriverFoodInProgressActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Camera")) {
                    openCamera();
                } else if (items[item].equals("Gallery")) {
                    openGalery();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void openGalery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                    ContentResolver resolver = getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis());
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + System.currentTimeMillis());
                    Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    try {
                        OutputStream fos = resolver.openOutputStream(imageUri);

                        selectedImagePath = ImageFilePath.getPath(DriverFoodInProgressActivity.this, imageUri);
                        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        try {
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    viewModel.excuteUpdateStatus(BookingId,
                            SessionManager.get_user_id(prefes), "4", selectedImagePath, DriverFoodInProgressActivity.this);
                }else {

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                    File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    selectedImagePath = String.valueOf(destination);
                    viewModel.excuteUpdateStatus(BookingId,
                            SessionManager.get_user_id(prefes), "4", selectedImagePath, DriverFoodInProgressActivity.this);
                }
            } else if (requestCode == SELECT_FILE) {
                Bitmap bm = null;
                if (data != null) {
                    try {
                        Uri selectedImageUri = data.getData();
                        selectedImagePath = ImageFilePath.getPath(DriverFoodInProgressActivity.this, selectedImageUri);
                        viewModel.excuteUpdateStatus(BookingId,
                                SessionManager.get_user_id(prefes), "4", selectedImagePath,DriverFoodInProgressActivity.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

