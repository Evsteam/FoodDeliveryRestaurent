package com.evs.foodexp.driverPkg.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.evs.foodexp.R;
import com.evs.foodexp.utils.ImageFilePath;
import com.evs.foodexp.utils.Utills;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadImageDeliveryActivity extends AppCompatActivity {
    CircleImageView driverImage;
    Button uploadCameraImgBtn, uploadGalleryImgBtn, submitImgBtn;
    private static final int REQUEST_CAMERA = 1, SELECT_FILE = 2;
    String selectedImagePathFront;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_picture_delivery);

        Utills.StatusBarColour(UploadImageDeliveryActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDriverUploadImage);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("UPLOAD IMAGES");
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

        uploadCameraImgBtn = (Button) findViewById(R.id.btn_camera);
        uploadGalleryImgBtn = (Button) findViewById(R.id.btn_gallery);
        driverImage = findViewById(R.id.upload_driver_image1);
        submitImgBtn = (Button) findViewById(R.id.btn_uploadImage_submit);

        uploadCameraImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraFront();
            }
        });

        uploadGalleryImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGaleryFront();
            }
        });

        submitImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UploadImageDeliveryActivity.this, "Done. Thank you", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UploadImageDeliveryActivity.this, DriverHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void openCameraFront() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void openGaleryFront() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File Front"), SELECT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
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
                    selectedImagePathFront = ImageFilePath.getPath(getBaseContext(), imageUri);
                    Glide.with(UploadImageDeliveryActivity.this).load(selectedImagePathFront).error(R.drawable.placeholder_user).dontAnimate().into(driverImage);
                } else {
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
                    selectedImagePathFront = String.valueOf(destination);
                    Glide.with(UploadImageDeliveryActivity.this).load(selectedImagePathFront).error(R.drawable.placeholder_user).dontAnimate().into(driverImage);
                }
            } else if (requestCode == SELECT_FILE) {
                Bitmap bm = null;
                if (data != null) {
                    try {
                        Uri selectedImageUri = data.getData();
                        selectedImagePathFront = ImageFilePath.getPath(getBaseContext(), selectedImageUri);
                        Glide.with(UploadImageDeliveryActivity.this).load(selectedImagePathFront).error(R.drawable.placeholder_user).dontAnimate().into(driverImage);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
