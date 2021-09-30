package com.evs.foodexp.restaurentPkg.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import  androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.DTO.CategoryDto;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthCategoryListener;
import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.models.CategoryModel;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.ImageFilePath;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;
import com.evs.foodexp.utils.Utills;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class AddMenuListActivity extends AppCompatActivity implements AuthCategoryListener<CategoryDto>, AuthMsgListener {

    Button btn_save;
    ArrayList<String> foodType, cateGoryId, categoryName;
    APIViewModel apiViewModel;
    private static final int REQUEST_CAMERA = 1, SELECT_FILE = 2;
    private String selectedImagePath = "";
    SharedPreferences preferences;
    EditText et_desc, et_specialPrice, et_price, et_title;
    Spinner spn_Category, spn_foodType;
    TextView tv_addMoreMenu, tv_skip;
    RoundedImageView img_itemImage, img_Image;
    int requestType = 0;
    LoaderProgress progress;
    String categoryIds, foodId, foodName, price, specialPrice, description, foodImage, foodTypes;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_list);
        Utills.StatusBarColour(AddMenuListActivity.this);
        preferences = PreferenceManager.getDefaultSharedPreferences(AddMenuListActivity.this);
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);

        progress = new LoaderProgress(AddMenuListActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAddMenuList);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("MENU");
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
        img_itemImage = findViewById(R.id.img_itemImage);
        apiViewModel.category("category", this);
        spn_Category = (Spinner) findViewById(R.id.spn_Category);
        spn_foodType = (Spinner) findViewById(R.id.spn_foodType);
        img_Image = (RoundedImageView) findViewById(R.id.img_Image);
        et_desc = (EditText) findViewById(R.id.et_desc);
        et_specialPrice = (EditText) findViewById(R.id.et_specialPrice);
        et_price = (EditText) findViewById(R.id.et_price);
        et_title = (EditText) findViewById(R.id.et_title);
        tv_addMoreMenu = (TextView) findViewById(R.id.tv_addMoreMenu);
        tv_skip = (TextView) findViewById(R.id.tv_skip);
        btn_save = (Button) findViewById(R.id.btn_save);

        foodType = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.foodType)));
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(AddMenuListActivity.this,
                R.layout.spinner_item, foodType);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_foodType.setAdapter(genderAdapter);


        img_itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        img_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation(0);
            }
        });

        tv_addMoreMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation(1);
            }


        });
        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddMenuListActivity.this, RestaurentHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        if (Global.isOnline(AddMenuListActivity.this)) {
            apiViewModel.category("category", this);
        } else Global.showDialog(AddMenuListActivity.this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            categoryIds = bundle.getString("categoryId");
            foodId = bundle.getString("foodId");
            foodName = bundle.getString("foodName");
            price = bundle.getString("price");
            specialPrice = bundle.getString("specialPrice");
            description = bundle.getString("description");
            foodImage = bundle.getString("foodImage");
            foodTypes = bundle.getString("foodType");
        }

        if (foodId != null) {
            et_desc.setText(description);
            et_specialPrice.setText(specialPrice);
            et_price.setText(price);
            et_title.setText(foodName);
            Glide.with(AddMenuListActivity.this).load(foodImage).dontAnimate().into(img_Image);
            if (foodType.indexOf(foodTypes) > -1) {
                spn_foodType.setSelection(foodType.indexOf(foodTypes));
            }
            img_Image.setVisibility(View.VISIBLE);
            btn_save.setText("Update Menu");
            tv_addMoreMenu.setVisibility(View.GONE);
            tv_skip.setVisibility(View.GONE);
        }
        if (categoryIds != null) {
            tv_addMoreMenu.setVisibility(View.GONE);
            tv_skip.setVisibility(View.GONE);
        }

    }

    private void validation(int i) {
        requestType = i;
        if (!Global.validatename(et_title.getText().toString())) {
            et_title.setError("Please Enter Item Name!");
        } else if (spn_Category.getSelectedItemPosition() == 0) {
            Toast.makeText(AddMenuListActivity.this, "Please Select Food Category!", Toast.LENGTH_LONG).show();
        } else if (spn_foodType.getSelectedItemPosition() == 0) {
            Toast.makeText(AddMenuListActivity.this, "Please Select Food Type!", Toast.LENGTH_LONG).show();
        } else if (!Global.validatename(et_price.getText().toString())) {
            et_price.setError("Please Enter Price!");
//        } else if (!Global.validatename(et_specialPrice.getText().toString())) {
//            et_specialPrice.setError("Please Enter Special Price!");
        } else if (!validationSpecialPrice(et_price.getText().toString(),et_specialPrice.getText().toString())) {
            Toast.makeText(AddMenuListActivity.this,"Add special price less than or equal to price!! ",Toast.LENGTH_LONG).show();
        } else if (!Global.validatename(et_desc.getText().toString())) {
            et_desc.setError("Enter Description !");

        } else if (Global.isOnline(AddMenuListActivity.this)) {
            if (foodId != null) {
                apiViewModel.editMenu(SessionManager.get_user_id(preferences), foodId, cateGoryId.get(spn_Category.getSelectedItemPosition()),
                        et_title.getText().toString(), foodType.get(spn_foodType.getSelectedItemPosition()),
                        et_price.getText().toString(), et_specialPrice.getText().toString().trim(), et_desc.getText().toString(), selectedImagePath, this);
            } else {
                apiViewModel.addMenu(SessionManager.get_user_id(preferences), cateGoryId.get(spn_Category.getSelectedItemPosition()),
                        et_title.getText().toString(), foodType.get(spn_foodType.getSelectedItemPosition()),
                        et_price.getText().toString(), et_specialPrice.getText().toString().trim(), et_desc.getText().toString(), selectedImagePath, this);
            }


        } else Global.showDialog(AddMenuListActivity.this);


    }

    private boolean validationSpecialPrice(String price, String sprice) {
        if (!Global.validatename(price)) {
            et_price.setError("Please Enter Price");
            return false;
        } else if (!Global.validatename(sprice)) {
//            et_specialPrice.setError("Enter Special Price");
            return true;
        } else if (Double.parseDouble(sprice) > Double.parseDouble(price)) {
            et_specialPrice.setError("Add special price less than or equal to price!! ");
            return false;
        }else return true;

    }


    private void selectImage() {

        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddMenuListActivity.this);
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

    @Override
    public void onCategoryStarted() {
        progress.show();
    }

    @Override
    public void onCategorySuccess(LiveData<CategoryDto> data) {
        progress.dismiss();
        cateGoryId = new ArrayList<>();
        categoryName = new ArrayList<>();
        cateGoryId.add("0");
        categoryName.add("Select Category");
        if (data.getValue().getStatus().equalsIgnoreCase("success")) {
            for (CategoryModel model : data.getValue().getData()) {
                cateGoryId.add(model.getId());
                categoryName.add(model.getName());
            }
            ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(AddMenuListActivity.this,
                    R.layout.spinner_item, categoryName);
            genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_Category.setAdapter(genderAdapter);
            if (foodId != null) {
                if (cateGoryId.indexOf(categoryIds) > -1) {
                    spn_Category.setSelection(cateGoryId.indexOf(categoryIds));
                }
            }

        }


    }

    @Override
    public void onCategoryFailure(String message) {
        progress.dismiss();
        Global.msgDialog(AddMenuListActivity.this, message);
    }

    @Override
    public void onStarted() {
        progress.show();
    }


    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialog(AddMenuListActivity.this, message);

    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        progress.dismiss();
        if (msgResponce.getStatus().equalsIgnoreCase("success")) {
            Toast.makeText(AddMenuListActivity.this, msgResponce.getMsg(), Toast.LENGTH_LONG).show();
            if (requestType == 0) {
                Intent intent = new Intent(AddMenuListActivity.this, RestaurentHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        } else Global.msgDialog(AddMenuListActivity.this, msgResponce.getMsg());
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
                Glide.with(AddMenuListActivity.this).load(selectedImagePath).dontAnimate().into(img_Image);
                img_Image.setVisibility(View.VISIBLE);
            } else if (requestCode == SELECT_FILE) {
                Bitmap bm = null;
                if (data != null) {
                    try {
                        Uri selectedImageUri = data.getData();
                        selectedImagePath = ImageFilePath.getPath(getBaseContext(), selectedImageUri);
                        Glide.with(AddMenuListActivity.this).load(selectedImagePath).dontAnimate().into(img_Image);
                        img_Image.setVisibility(View.VISIBLE);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }


}
