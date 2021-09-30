package com.evs.foodexp.commonPkg.common.activity;

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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import  androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.DTO.EditProfileRequestModel;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.driverPkg.activity.CompleteProfileDriverActivity;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.ImageFilePath;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class EditCompleteProfileActivity extends AppCompatActivity  {

    private Button completeProfileBtn;
    RoundedImageView img_logoImage,img_profileImage,img_Restorentlogo,img_CoverImage;
    private static final int REQUEST_CAMERA = 1, SELECT_FILE = 2,GETLOCATION=3;
    private String selectedImagePath="",selectedImagelogo="",getLat,getlongs;
    TextView tv_address, tv_country;
    EditProfileRequestModel editProfileRequest;
    SharedPreferences preferences;
    APIViewModel apiViewModel;
    LoaderProgress progress;
    int imagType=0;
    ImageView gettingLocation;
    Spinner spn_Type;
    ArrayList<String> RestaurentTypeList;
    String action="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        Utills.StatusBarColour(EditCompleteProfileActivity.this);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        editProfileRequest = new EditProfileRequestModel();
        progress=new LoaderProgress(EditCompleteProfileActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCompleteProfile);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);




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
        gettingLocation = (ImageView) findViewById(R.id.gettingLocation);
        completeProfileBtn = (Button)findViewById(R.id.btnCompleteProfile);
        tv_country = (TextView) findViewById(R.id.tv_country);
        tv_address = (TextView) findViewById(R.id.tv_address);
        img_logoImage = findViewById(R.id.img_logoImage);
        img_CoverImage = findViewById(R.id.img_CoverImage);
        img_Restorentlogo = findViewById(R.id.img_Restorentlogo);
        spn_Type = findViewById(R.id.spn_Type);
        img_profileImage = findViewById(R.id.img_profileImage);

        tv_address.setText(SessionManager.get_address(preferences));
        tv_country.setText(SessionManager.get_country(preferences));
        getLat=SessionManager.get_Lats(preferences);
        getlongs=SessionManager.get_Longs(preferences);

        if(Global.validatename(SessionManager.get_image(preferences))){
            img_Restorentlogo.setVisibility(View.VISIBLE);
        }
        if(Global.validatename(SessionManager.get_driverLicence(preferences))){
            img_Restorentlogo.setVisibility(View.VISIBLE);
        }


        Glide.with(EditCompleteProfileActivity.this).load(SessionManager.get_Doc(preferences)).into(img_Restorentlogo);
        Glide.with(EditCompleteProfileActivity.this).load(SessionManager.get_driverLicence(preferences)).into(img_CoverImage);

//        if(!Global.GpsEnable(EditCompleteProfileActivity.this)){
//            Global.showGPSDisabledAlertToUser(EditCompleteProfileActivity.this);
//        }else {
//            tv_address.setText(getAddress(gpsTracker.getLatitude(),gpsTracker.getLongitude()));
//            getLat=String.valueOf(gpsTracker.getLatitude());
//            getlongs=String.valueOf(gpsTracker.getLongitude());
//        }

        RestaurentTypeList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.RestorentType)));
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(EditCompleteProfileActivity.this,
                R.layout.spinner_item, RestaurentTypeList);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_Type.setAdapter(genderAdapter);

       if(RestaurentTypeList.indexOf(SessionManager.get_BusinessType(preferences))>-1){
           spn_Type.setSelection(RestaurentTypeList.indexOf(SessionManager.get_BusinessType(preferences)));
       }

       gettingLocation.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(!Global.GpsEnable(EditCompleteProfileActivity.this)){
                   Global.showGPSDisabledAlertToUser(EditCompleteProfileActivity.this);
               }else {
                   setMyLastLocation();
               }
           }
       });

        img_logoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
                imagType=1;
            }
        });

        img_profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
                imagType=0;
            }
        });


        completeProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Global.validatename(tv_address.getText().toString())) {
                    Toast.makeText(EditCompleteProfileActivity.this, "Please Select Your Address Location!", Toast.LENGTH_LONG).show();
                }else if(!Global.validatename(selectedImagelogo)) {
                    Toast.makeText(EditCompleteProfileActivity.this, "Please Select Your Restaurant Logo!", Toast.LENGTH_LONG).show();
                }else if(spn_Type.getSelectedItemPosition()==0) {
                    Toast.makeText(EditCompleteProfileActivity.this, "Please Select Your Restaurant Type!", Toast.LENGTH_LONG).show();
                }else if(Global.isOnline(EditCompleteProfileActivity.this)){
                   Intent intent=new Intent(EditCompleteProfileActivity.this,PaymentInfoActivity.class);
                   intent.putExtra("address",tv_address.getText().toString());
                   intent.putExtra("country",tv_country.getText().toString());
                   intent.putExtra("lats",getLat);
                   intent.putExtra("longs",getlongs);
                   intent.putExtra("logo",selectedImagePath);
                   intent.putExtra("coverlogo",selectedImagelogo);
                   intent.putExtra("doc","");
                    intent.putExtra("uType","Edit+"+Global.type_business);
                   intent.putExtra("type",RestaurentTypeList.get(spn_Type.getSelectedItemPosition()));
                   startActivity(intent);
                }

            }
        });

        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditCompleteProfileActivity.this, AddYourLoaction.class);
                startActivityForResult(i, GETLOCATION);
            }
        });


    }



    private void selectImage() {

        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditCompleteProfileActivity.this);
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
                        selectedImagePath = ImageFilePath.getPath(getBaseContext(), imageUri);
                        Glide.with(EditCompleteProfileActivity.this).load(selectedImagePath).error(R.drawable.image).dontAnimate().into(img_CoverImage);
                        img_CoverImage.setVisibility(View.VISIBLE);
                    } else {
                        selectedImagelogo = ImageFilePath.getPath(getBaseContext(), imageUri);
                        Glide.with(EditCompleteProfileActivity.this).load(selectedImagelogo).error(R.drawable.image).dontAnimate().into(img_Restorentlogo);
                        img_Restorentlogo.setVisibility(View.VISIBLE);
                    }
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
                        selectedImagePath = String.valueOf(destination);
                        Glide.with(EditCompleteProfileActivity.this).load(selectedImagePath).error(R.drawable.image).dontAnimate().into(img_CoverImage);
                        img_CoverImage.setVisibility(View.VISIBLE);
                    } else {
                        selectedImagelogo = String.valueOf(destination);
                        Glide.with(EditCompleteProfileActivity.this).load(selectedImagelogo).error(R.drawable.image).dontAnimate().into(img_Restorentlogo);
                        img_Restorentlogo.setVisibility(View.VISIBLE);
                    }
                }
            } else if (requestCode == SELECT_FILE) {
                Bitmap bm = null;
                if (data != null) {
                    try {
                        Uri selectedImageUri = data.getData();
                        selectedImagePath = ImageFilePath.getPath(getBaseContext(), selectedImageUri);
                        if(imagType==0) {
                            selectedImagePath = ImageFilePath.getPath(getBaseContext(), selectedImageUri);
                            Glide.with(EditCompleteProfileActivity.this).load(selectedImagePath).error(R.drawable.image).dontAnimate().into(img_CoverImage);
                            img_CoverImage.setVisibility(View.VISIBLE);
                        }else {
                            selectedImagelogo = ImageFilePath.getPath(getBaseContext(), selectedImageUri);
                           Glide.with(EditCompleteProfileActivity.this).load(selectedImagelogo).error(R.drawable.image).dontAnimate().into(img_Restorentlogo);
                            img_Restorentlogo.setVisibility(View.VISIBLE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } if (requestCode == GETLOCATION) {
                tv_address.setText(data.getStringExtra("address"));
                getLat = data.getStringExtra("lat");
                getlongs = data.getStringExtra("long");
                if (data.getStringExtra("address") != null && data.getStringExtra("address").length() != 0) {
                    getAddress(Double.valueOf(getLat),Double.valueOf(getlongs));
                }
            }
        }

    }






    private String getAddress(double lat,Double longs) {
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
            tv_country.setText(country);

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
                    tv_address.setText(getAddress(location.getLatitude(), location.getLongitude()));
                    getLat=""+location.getLatitude();
                    getlongs=""+location.getLongitude();
                }
            }
        });
    }

}
