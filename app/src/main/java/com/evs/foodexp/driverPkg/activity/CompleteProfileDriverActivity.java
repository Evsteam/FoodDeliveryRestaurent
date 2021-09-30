package com.evs.foodexp.driverPkg.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.common.activity.AddYourLoaction;
import com.evs.foodexp.commonPkg.common.activity.PaymentInfoActivity;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.ImageFilePath;
import com.evs.foodexp.utils.Utills;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

public class CompleteProfileDriverActivity extends AppCompatActivity {

    Button saveProfileBtn;
    SharedPreferences prefs;
    RoundedImageView img_ProfileIcon, img_profile, img_DrivingLicenseIcon, img_DrivingLicense, img_DocImageDriver;
    private static final int REQUEST_CAMERA = 1, SELECT_FILE = 2, GETLOCATION = 3, SELECT_DOCFILE = 4;
    EditText et_address, et_country;
    String selectedDocFile = "", getLat = "", getlongs = "", selectedImage = "", selectedLicenceImage = "";
    int imagType = 0;
    ImageView gettingLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile_driver);
        prefs = PreferenceManager.getDefaultSharedPreferences(CompleteProfileDriverActivity.this);
        Utills.StatusBarColour(CompleteProfileDriverActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCompleteProfileDriver);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        et_address = (EditText) findViewById(R.id.et_address);
        et_country = (EditText) findViewById(R.id.et_country);


        img_ProfileIcon = findViewById(R.id.img_ProfileIcon);
        img_profile = findViewById(R.id.img_profile);
        img_DocImageDriver = findViewById(R.id.img_DocImageDriver);
        img_DrivingLicenseIcon = findViewById(R.id.img_DrivingLicenseIcon);
        img_DrivingLicense = findViewById(R.id.img_DrivingLicense);

        saveProfileBtn = (Button) findViewById(R.id.saveProfileBtn);
        gettingLocation = (ImageView) findViewById(R.id.gettingLocation);

        toolbarTitle.setText("COMPLETE PROFILE");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (!Global.GpsEnable(CompleteProfileDriverActivity.this)) {
            Global.showGPSDisabledAlertToUser(CompleteProfileDriverActivity.this);
        } else {
            setMyLastLocation();
        }

        img_ProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
                imagType = 0;
            }
        });

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
                imagType = 0;
            }
        });

        img_DrivingLicenseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
                imagType = 1;
            }
        });
        img_DrivingLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
                imagType = 1;
            }
        });

        img_DocImageDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagType = 2;
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_DOCFILE);
            }
        });


        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CompleteProfileDriverActivity.this, PaymentInfoActivity.class);
                intent.putExtra("address", et_address.getText().toString());
                intent.putExtra("country", et_country.getText().toString());
                intent.putExtra("lats", getLat);
                intent.putExtra("longs", getlongs);
                intent.putExtra("logo", selectedImage);
                intent.putExtra("coverlogo", selectedLicenceImage);
                intent.putExtra("doc", selectedDocFile);
                intent.putExtra("type", "");
                intent.putExtra("uType", "Driver");
                startActivity(intent);

            }
        });

        et_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CompleteProfileDriverActivity.this, AddYourLoaction.class);
                startActivityForResult(i, GETLOCATION);
            }
        });

        gettingLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Global.GpsEnable(CompleteProfileDriverActivity.this)) {
                    Global.showGPSDisabledAlertToUser(CompleteProfileDriverActivity.this);
                } else {
                    setMyLastLocation();
                }
            }
        });

    }


    private void selectImage() {

        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CompleteProfileDriverActivity.this);
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
                    if (imagType == 0) {
                        selectedImage = ImageFilePath.getPath(CompleteProfileDriverActivity.this, imageUri);
                        Glide.with(CompleteProfileDriverActivity.this).load(selectedImage).error(R.drawable.image).dontAnimate().into(img_profile);
                        img_profile.setVisibility(View.VISIBLE);
                    } else if (imagType == 1) {
                        selectedLicenceImage = ImageFilePath.getPath(CompleteProfileDriverActivity.this, imageUri);
                        Glide.with(CompleteProfileDriverActivity.this).load(selectedLicenceImage).error(R.drawable.image).dontAnimate().into(img_DrivingLicense);
                        img_DrivingLicense.setVisibility(View.VISIBLE);
                    } else selectedDocFile = ImageFilePath.getPath(CompleteProfileDriverActivity.this, imageUri);
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
                    if (imagType == 0) {
                        selectedImage = String.valueOf(destination);
                        Glide.with(CompleteProfileDriverActivity.this).load(selectedImage).error(R.drawable.image).dontAnimate().into(img_profile);
                        img_profile.setVisibility(View.VISIBLE);
                    } else if (imagType == 1) {
                        selectedLicenceImage = String.valueOf(destination);
                        Glide.with(CompleteProfileDriverActivity.this).load(selectedLicenceImage).error(R.drawable.image).dontAnimate().into(img_DrivingLicense);
                        img_DrivingLicense.setVisibility(View.VISIBLE);
                    } else selectedDocFile = String.valueOf(destination);
                }
            } else if (requestCode == SELECT_FILE) {
                Bitmap bm = null;
                if (data != null) {
                    try {
                        Uri selectedImageUri = data.getData();
                        if (imagType == 0) {
                            selectedImage = ImageFilePath.getPath(getBaseContext(), selectedImageUri);
                            Glide.with(CompleteProfileDriverActivity.this).load(selectedImage).error(R.drawable.image).dontAnimate().into(img_profile);
                            img_profile.setVisibility(View.VISIBLE);
                        } else if (imagType == 1) {
                            selectedLicenceImage = ImageFilePath.getPath(getBaseContext(), selectedImageUri);
                            Glide.with(CompleteProfileDriverActivity.this).load(selectedLicenceImage).error(R.drawable.image).dontAnimate().into(img_DrivingLicense);
                            img_DrivingLicense.setVisibility(View.VISIBLE);
                        } else
                            selectedDocFile = ImageFilePath.getPath(getBaseContext(), selectedImageUri);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == GETLOCATION) {
                et_address.setText(data.getStringExtra("address"));
                getLat = data.getStringExtra("lat");
                getlongs = data.getStringExtra("long");
                if (data.getStringExtra("address") != null && data.getStringExtra("address").length() != 0) {
                    getAddress(Double.valueOf(getLat), Double.valueOf(getlongs));
                }
            } else if (requestCode == SELECT_DOCFILE) {
                Bitmap bm = null;
                if (data != null) {
                    try {
                        Uri selectedImageUri = data.getData();
                        selectedDocFile = ImageFilePath.getPath(getBaseContext(), selectedImageUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    private String getAddress(double lat, Double longs) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        String address = "";
        try {
            addresses = geocoder.getFromLocation(lat, longs, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5


            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            et_country.setText(country);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }
    private void setMyLastLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    et_address.setText(getAddress(location.getLatitude(), location.getLongitude()));
                    getLat=""+location.getLatitude();
                    getlongs=""+location.getLongitude();
                }
            }
        });
    }
}
