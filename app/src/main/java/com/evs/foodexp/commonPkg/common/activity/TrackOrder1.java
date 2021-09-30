package com.evs.foodexp.commonPkg.common.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.evs.foodexp.R;
import com.evs.foodexp.location_services.DirectionsJSONParser;
import com.evs.foodexp.models.Comment;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.Utills;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

public class TrackOrder1 extends AppCompatActivity implements OnMapReadyCallback {

    Button trackOrder;
    GoogleMap mMap;
    String driverId, bookingId, dropLat,
            dropLongs, dropAddress, amount, driverName,uploadedImage;
    TextView tv_amount, tv_address, tv_items;
    DatabaseReference reference_check, reference;
    ChildEventListener mChildEventListener;
    Comment comment;
    private FirebaseAuth firebaseAuth;
    LoaderProgress progress;
    boolean status = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Utills.StatusBarColour(TrackOrder1.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("TRACK YOUR ORDER");
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
        trackOrder = (Button) findViewById(R.id.waitingOrderBtn);
        progress = new LoaderProgress(TrackOrder1.this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapLocation);
        mapFragment.getMapAsync(this);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            driverId = bundle.getString("driverId");
            bookingId = bundle.getString("bookingId");
            dropLat = bundle.getString("dropLat");
            dropLongs = bundle.getString("dropLongs");
            dropAddress = bundle.getString("dropAddress");
            amount = bundle.getString("amount");
            driverName = bundle.getString("driverName");
            uploadedImage = bundle.getString("uploadedImage");
        }
        tv_amount = findViewById(R.id.tv_amount);
        tv_address = findViewById(R.id.tv_address);
        tv_items = findViewById(R.id.tv_items);
        tv_address.setText(dropAddress);
        tv_amount.setText("$ " + amount);
        tv_items.setVisibility(View.GONE);
//        method_getTracking();
        progress.show();

        if(Global.validatename(uploadedImage)){
            trackOrder.setVisibility(View.VISIBLE);
            trackOrder.setText("Uploaded Slip");
        }else trackOrder.setVisibility(View.GONE);

        trackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TrackOrder1.this,ImageViewer.class);
                intent.putExtra("test",uploadedImage);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        if (Global.GpsEnable(TrackOrder1.this)) {

            //            myLocation(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()));
            method_getTracking();
        } else Global.showGPSDisabledAlertToUser(TrackOrder1.this);
    }


    private void method_getTracking() {
        firebaseAuth = FirebaseAuth.getInstance();
        reference_check = FirebaseDatabase.getInstance().getReference().child("liveTracking");
        reference_check.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("get val  ", "" + bookingId + "@" + driverId);
                Log.e("has dat  ", "" + dataSnapshot.hasChild(bookingId + "@" + driverId));
                reference = FirebaseDatabase.getInstance().getReference().child("liveTracking").child(bookingId + "@" + driverId);
                ChildEventListener childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                        Log.d("onChildAdded:", dataSnapshot.getKey());
                        progress.dismiss();
                        // A new comment has been added, add it to the displayed list
                        Comment comment = dataSnapshot.getValue(Comment.class);

                        // [START_EXCLUDE]
                        // Update RecyclerView

                        // [END_EXCLUDE]
                        Log.d("onChildAddedd:", comment.lat);

                        if (Global.validatename(comment.lat)) {
                            trakingAdress(dropLat, dropLongs, comment.lat, comment.longs, driverName);
                            progress.dismiss();
                        } else
                            Global.msgDialogBack(TrackOrder1.this, "Your Order is Pending!!!");
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                        Log.d("onChildChanged:", dataSnapshot.getKey());
                        progress.dismiss();
                        // A comment has changed, use the key to determine if we are displaying this
                        // comment and if so displayed the changed comment.
                        Comment newComment = dataSnapshot.getValue(Comment.class);
                        String commentKey = dataSnapshot.getKey();

                        // [START_EXCLUDE]
                        Log.d("onChildUpdate:", newComment.lat);

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.d("onChildRemoved:", dataSnapshot.getKey());

                        // A comment has changed, use the key to determine if we are displaying this
                        // comment and if so remove it.
                        String commentKey = dataSnapshot.getKey();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                        Log.d("onChildMoved:", dataSnapshot.getKey());
                        progress.dismiss();
                        Comment movedComment = dataSnapshot.getValue(Comment.class);
                        String commentKey = dataSnapshot.getKey();

                        // ...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        progress.dismiss();
                        Log.e("onCancelled", databaseError.toException().toString());
                        Toast.makeText(TrackOrder1.this, "Failed to load comments.",
                                Toast.LENGTH_SHORT).show();
                    }
                };
                reference.addChildEventListener(childEventListener);

                mChildEventListener = childEventListener;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progress.dismiss();
                Log.e("DatabaseError ", "onCancelled: " + databaseError.getMessage());
            }
        });
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

    class RestDownloadTask extends AsyncTask<String, Void, String> {

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

            RestParserTask parserTask = new RestParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }


        class RestParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

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

    private void trakingAdress(String userlat, String userlong,
                               String drivlat, String drivelong,  String drverName) {

        mMap.clear();

        LatLng latLng = new LatLng(Double.parseDouble(drivlat), Double.parseDouble(drivelong));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.moveCamera(cameraUpdate);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(drivlat), Double.parseDouble(drivelong)))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bike))
                .title(drverName))
                .showInfoWindow();




        if (Global.validatename(userlat) || Global.validatename(userlong)) {


            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(userlat), Double.parseDouble(userlong)))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green))
                    .title("Drop Location")).showInfoWindow();

            String url11 = getDirectionsUrl(new LatLng(Double.parseDouble(userlat), Double.parseDouble(userlong)), new LatLng(Double.parseDouble(drivlat), Double.parseDouble(drivelong)));
            RestDownloadTask downloadTask1 = new RestDownloadTask();
            downloadTask1.execute(url11);
        } else {
//            String url = getDirectionsUrl(new LatLng(Double.parseDouble(userlat), Double.parseDouble(userlong)), new LatLng(Double.parseDouble(drivlat), Double.parseDouble(drivelong)));
//            RestDownloadTask downloadTask = new RestDownloadTask();
//            downloadTask.execute(url);
        }
    }


}
