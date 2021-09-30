package com.evs.foodexp.commonPkg.common.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import  androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.DTO.EditProfileRequestModel;
import com.evs.foodexp.commonPkg.DTO.ProfileDetailRequest;
import com.evs.foodexp.commonPkg.DTO.UserLoginDto;
import com.evs.foodexp.commonPkg.common.activity.AddYourLoaction;
import com.evs.foodexp.commonPkg.common.activity.EditCompleteProfileActivity;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.driverPkg.activity.EditCompleteProfileDriverActivity;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.ImageFilePath;
import com.evs.foodexp.utils.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment implements AuthListener<UserLoginDto> {

    EditText userNameEdit, userPhoneEdit, userAddressEdit, et_email;
    String userNameStr, getLat, getlongs, userPhoneStr, userAddressStr, userIdStr;
    Button editProfileBtn;
    APIViewModel apiViewModel;
    ProfileDetailRequest detailRequest;
    EditProfileRequestModel editProfileModel;
    private ProgressDialog progressDialog;
    SharedPreferences preferences;
    CircleImageView imag_profile;
    private String selectedImagePath = "";
    private static final int REQUEST_CAMERA = 1, SELECT_FILE = 2, GETLOCATION = 3;
    int num = 0;
    TextView tv_bank_Info, tv_editbusiness;
    LinearLayout layout_location;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_profile_layout, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("Edit Profile");

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        progressDialog = new ProgressDialog(getActivity());
        editProfileModel = new EditProfileRequestModel();

        System.out.println(userIdStr);
        userNameEdit = (EditText) view.findViewById(R.id.userNameEditProfile);
        layout_location = (LinearLayout) view.findViewById(R.id.layout_location);
        tv_bank_Info = (TextView) view.findViewById(R.id.tv_bank_Info);
        tv_editbusiness = (TextView) view.findViewById(R.id.tv_editbusiness);
        imag_profile = (CircleImageView) view.findViewById(R.id.imag_profile);
        userPhoneEdit = (EditText) view.findViewById(R.id.userPhoneEditProfile);
        userAddressEdit = (EditText) view.findViewById(R.id.userAddressEditProfile);
        et_email = (EditText) view.findViewById(R.id.et_email);
        editProfileBtn = (Button) view.findViewById(R.id.editProfileBtn);

        userNameEdit.setText(SessionManager.get_name(preferences));
        et_email.setText(SessionManager.get_emailid(preferences));
        userPhoneEdit.setText(SessionManager.get_mobile(preferences));
        userAddressEdit.setText(SessionManager.get_address(preferences));
        getLat=SessionManager.get_Lats(preferences);
        getlongs=SessionManager.get_Longs(preferences);

        Glide.with(getActivity()).load(SessionManager.get_image(preferences)).
                placeholder(R.drawable.placeholder_user).into(imag_profile);

        detailRequest = new ProfileDetailRequest();
        detailRequest.setAction("profile");
        detailRequest.setUserId(userIdStr);

        progressDialog.setMessage("Loading...");
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNameStr = userNameEdit.getText().toString().trim();
                userPhoneStr = userPhoneEdit.getText().toString().trim();
                userAddressStr = userAddressEdit.getText().toString().trim();

                if (userNameStr.isEmpty()) {
                    userNameEdit.setError("Enter Your Name");
                } else if (userPhoneStr.isEmpty()) {
                    userPhoneEdit.setError("Enter Phone Number");
                } else if (userAddressStr.isEmpty()) {
                    userAddressEdit.setError("Enter Address");
                } else {

                    editProfileModel.setAddress(userAddressStr);
                    editProfileModel.setFullname(userNameStr);
                    editProfileModel.setContactNumber(userPhoneStr);
                    editProfileModel.setUserId(SessionManager.get_user_id(preferences));
                    editProfileModel.setImage(selectedImagePath);
                    editProfileModel.setLats(getLat);
                    editProfileModel.setLongs(getlongs);
                    editProfileModel.setDevice("Android");
                    editProfileModel.setAction("editprofile");


                    apiViewModel.editProfile(editProfileModel, EditProfileFragment.this);
                }
            }
        });
        imag_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });



        if(!SessionManager.get_userType(preferences).equalsIgnoreCase("Member")){
            tv_editbusiness.setVisibility(View.VISIBLE);
        }

        tv_editbusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SessionManager.get_userType(preferences).equalsIgnoreCase(Global.type_business)){
                    Intent intent = new Intent(getContext(), EditCompleteProfileActivity.class);
                    startActivity(intent);
                }
                if(SessionManager.get_userType(preferences).equalsIgnoreCase(Global.type_driver)){
                    Intent intent = new Intent(getContext(), EditCompleteProfileDriverActivity.class);
                    startActivity(intent);
                }


            }
        });
        userAddressEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddYourLoaction.class);
                startActivityForResult(i, GETLOCATION);
            }
        });

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                SessionManager.save_device_token(preferences, task.getResult());
            }
        });

        return view;
    }


    @Override
    public void onStarted() {
        progressDialog.show();
    }

    @Override
    public void onSuccess(LiveData<UserLoginDto> data) {
        if (data.getValue().getStatus().equalsIgnoreCase("success")) {
            progressDialog.dismiss();
            SessionManager.save_name(preferences, data.getValue().getData().getFullName());
            SessionManager.save_mobile(preferences, data.getValue().getData().getContactNumber());
            SessionManager.save_image(preferences, data.getValue().getData().getImage());
            SessionManager.save_address(preferences, data.getValue().getData().getAddress());
            SessionManager.save_Lats(preferences, data.getValue().getData().getLatitude());
            SessionManager.save_Longs(preferences, data.getValue().getData().getLogitude());
            Toast.makeText(getContext(),data.getValue().getMsg(),Toast.LENGTH_LONG).show();
        } else Global.msgDialog(getActivity(), data.getValue().getMsg());
    }

    @Override
    public void onFailure(String message) {
        progressDialog.dismiss();
        //  Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
        System.out.println("Error Because === " + message);

    }

    private void selectImage() {

        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                    ContentResolver resolver = getContext().getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis());
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + System.currentTimeMillis());
                    Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    try {
                        OutputStream fos = resolver.openOutputStream(imageUri);

                        selectedImagePath = ImageFilePath.getPath(getActivity(), imageUri);
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
                    Log.e("image", selectedImagePath);
                    Glide.with(getActivity()).load(selectedImagePath).placeholder(R.drawable.placeholder_user).into(imag_profile);
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
                    Glide.with(getContext()).load(selectedImagePath).error(R.drawable.placeholder_user).dontAnimate().into(imag_profile);
                }
            } else if (requestCode == SELECT_FILE) {
                Bitmap bm = null;
                if (data != null) {
                    try {
                        Uri selectedImageUri = data.getData();
                        selectedImagePath = ImageFilePath.getPath(getContext(), selectedImageUri);
                        Glide.with(getContext()).load(selectedImagePath).error(R.drawable.placeholder_user).dontAnimate().into(imag_profile);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == GETLOCATION) {
                userAddressEdit.setText(data.getStringExtra("address"));
                getLat = data.getStringExtra("lat");
                getlongs = data.getStringExtra("long");
                userAddressEdit.setError(null);
            }
        }
    }
}



