package com.evs.foodexp.commonPkg.common.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import  androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.DTO.UserLoginDto;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.driverPkg.activity.DriverCompletaddress;
import com.evs.foodexp.driverPkg.activity.DriverHomeActivity;
import com.evs.foodexp.restaurentPkg.activity.AddMenuListActivity;
import com.evs.foodexp.models.User;
import com.evs.foodexp.restaurentPkg.activity.RestaurentHomeActivity;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;
import com.evs.foodexp.utils.Utills;

import java.util.ArrayList;
import java.util.Arrays;

public class PaymentInfoActivity extends AppCompatActivity implements AuthListener<UserLoginDto> {

    Button paymentInfoBtn;
    String address, country, lats, longs, logo, coverlogo, type, doc, uType;
    Spinner spn_accountType;
    EditText et_name, et_bankName, bankAccountNumber, branchName, routingNumber;
    ArrayList<String> AccountTypeList;
    LoaderProgress progress;
    APIViewModel apiViewModel;
    SharedPreferences prefe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_info);
        Utills.StatusBarColour(PaymentInfoActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPaymentInfo);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("PAYMENT INFORMATION");
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
        paymentInfoBtn = (Button) findViewById(R.id.btnPaymentInfo);
        et_name = (EditText) findViewById(R.id.et_name);
        et_bankName = (EditText) findViewById(R.id.et_bankName);
        bankAccountNumber = (EditText) findViewById(R.id.bankAccountNumber);
        branchName = (EditText) findViewById(R.id.branchName);
        routingNumber = (EditText) findViewById(R.id.routingNumber);
        spn_accountType = (Spinner) findViewById(R.id.spn_accountType);


        AccountTypeList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.AccountType)));
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(PaymentInfoActivity.this,
                R.layout.spinner_item, AccountTypeList);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_accountType.setAdapter(genderAdapter);

        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        progress = new LoaderProgress(PaymentInfoActivity.this);
        prefe = PreferenceManager.getDefaultSharedPreferences(PaymentInfoActivity.this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            address = bundle.getString("address");
            country = bundle.getString("country");
            lats = bundle.getString("lats");
            longs = bundle.getString("longs");
            logo = bundle.getString("logo");
            coverlogo = bundle.getString("coverlogo");
            doc = bundle.getString("doc");
            uType = bundle.getString("uType");
            type = bundle.getString("type");
        }


        et_name.setText(SessionManager.get_AccountHolderName(prefe));
        et_bankName.setText(SessionManager.get_BankName(prefe));
        bankAccountNumber.setText(SessionManager.get_AccountNo(prefe));
        branchName.setText(SessionManager.get_Branch(prefe));
        routingNumber.setText(SessionManager.get_RoutingNo(prefe));
        if (AccountTypeList.indexOf(SessionManager.get_accountType(prefe)) > -1) {
            spn_accountType.setSelection(AccountTypeList.indexOf(SessionManager.get_accountType(prefe)));
        }


        paymentInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }


        });
    }

    private void validation() {
        if (!Global.validatename(et_name.getText().toString())) {
            et_name.setError(Html.fromHtml("<font color='red'> Enter Your Name</font>"));
        } else if (!Global.validatename(et_bankName.getText().toString())) {
            et_bankName.setError(Html.fromHtml("<font color='red'> Enter Your Bank Name</font>"));
        } else if (!Global.validatename(bankAccountNumber.getText().toString())) {
            bankAccountNumber.setError(Html.fromHtml("<font color='red'> Enter Bank Account Number </font>"));
        } else if (!Global.validatename(branchName.getText().toString())) {
            branchName.setError(Html.fromHtml("<font color='red'> Enter Branch  Name</font>"));
        } else if (!Global.validatename(routingNumber.getText().toString())) {
            routingNumber.setError(Html.fromHtml("<font color='red'> Enter Routing Number</font>"));
        } else if (spn_accountType.getSelectedItemPosition() == 0) {
            Toast.makeText(PaymentInfoActivity.this, "Please Select Account Type", Toast.LENGTH_LONG).show();
        } else if (Global.isOnline(PaymentInfoActivity.this)) {
//            SessionManager.save_Branch(prefe, branchName.getText().toString());
            if (uType.equalsIgnoreCase(Global.type_business)) {
                apiViewModel.restaurentUpdate(SessionManager.get_user_id(prefe), address, country, et_name.getText().toString(),
                        et_bankName.getText().toString(), bankAccountNumber.getText().toString(), routingNumber.getText().toString(),
                        AccountTypeList.get(spn_accountType.getSelectedItemPosition()), branchName.getText().toString(), lats, longs, type, logo, coverlogo, this);
            } else if (uType.equalsIgnoreCase(Global.type_driver)){
                apiViewModel.DirverUpdate(SessionManager.get_user_id(prefe), address, country, et_name.getText().toString(),
                        et_bankName.getText().toString(), bankAccountNumber.getText().toString(), routingNumber.getText().toString(),
                        AccountTypeList.get(spn_accountType.getSelectedItemPosition()), branchName.getText().toString(), lats, longs, type, logo, coverlogo, doc, this);
            }else if(uType.equalsIgnoreCase("Edit+"+Global.type_business)){
                apiViewModel.restaurentUpdate(SessionManager.get_user_id(prefe), address, country, et_name.getText().toString(),
                        et_bankName.getText().toString(), bankAccountNumber.getText().toString(), routingNumber.getText().toString(),
                        AccountTypeList.get(spn_accountType.getSelectedItemPosition()), branchName.getText().toString(), lats, longs, type, logo, coverlogo, this);
            }else if(uType.equalsIgnoreCase("Edit+"+Global.type_driver)){
                apiViewModel.DirverUpdate(SessionManager.get_user_id(prefe), address, country, et_name.getText().toString(),
                        et_bankName.getText().toString(), bankAccountNumber.getText().toString(), routingNumber.getText().toString(),
                        AccountTypeList.get(spn_accountType.getSelectedItemPosition()), branchName.getText().toString(), lats, longs, type, logo, coverlogo, doc, this);
            }
        } else Global.showDialog(PaymentInfoActivity.this);
    }

    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onSuccess(LiveData<UserLoginDto> data) {
        progress.dismiss();
        if (data.getValue().getStatus().equalsIgnoreCase("success")) {
            saveInformation(data.getValue().getData());
            if (uType.equalsIgnoreCase(Global.type_business)) {
                Intent intent = new Intent(PaymentInfoActivity.this, AddMenuListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (uType.equalsIgnoreCase(Global.type_driver)) {
                Intent intent = new Intent(PaymentInfoActivity.this, DriverCompletaddress.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
//                finish();
            } else if(uType.equalsIgnoreCase("Edit+"+Global.type_business)) {
                Intent intent = new Intent(PaymentInfoActivity.this, RestaurentHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else if(uType.equalsIgnoreCase("Edit+"+Global.type_driver)) {
                Intent intent = new Intent(PaymentInfoActivity.this, DriverHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }

    }

    private void saveInformation(User data) {
//      userId":85,"fullName":"radhe","lastName":"","middleName":"","email":"radhe1@gmail.com",
//      "gender":"","role":"Restaurant","dob":"","address":"MP SH 2, Kailaras, Madhya Pradesh 476224, India",
//      "zipCode":"","state":"","country":"India","contactNumber":"8645445488","foodTag":"",
//      "image":"http:\/\/demo2.evirtualservices.com\/food-delivery\/site\/img\/uploads\/users\/15865087131584684256285.jpg",
//      "companyBackground":"http:\/\/demo2.evirtualservices.com\/food-delivery\/site\/img\/uploads\/users\/15865087131584684256285.jpg",
//      "ssnImage":"","AutoInsurance":"","drivlingImage":"","device":"","deviceToken":"","firebaseId":"","socialId":"",
//      "socialType":"","BankName":"bank name","AccountHolderName":"my name",
//      "AccountNo":"8464445545464344343443434","RoutingNo":"gegsgsgs","accountType":"Saving"}}

        SessionManager.save_country(prefe, data.getCountry());
        SessionManager.save_Branch(prefe, data.getBranchName());
        SessionManager.save_BankName(prefe, data.getBankName());
        SessionManager.save_AccountHolderName(prefe, data.getAccountHolderName());
        SessionManager.save_AccountNo(prefe, data.getAccountNo());
        SessionManager.save_RoutingNo(prefe, data.getRoutingNo());
        SessionManager.save_accountType(prefe, data.getAccountType());
        SessionManager.save_address(prefe, data.getAddress());
        SessionManager.save_image(prefe, data.getImage());
        SessionManager.save_BusinessType(prefe, data.getFoodTag());
        SessionManager.save_Lats(prefe, data.getLatitude());
        SessionManager.save_Longs(prefe, data.getLogitude());
        SessionManager.save_driverLicence(prefe, data.getCompanyBackground());
        SessionManager.save_CouverImage(prefe, data.getDrivlingImage());
        SessionManager.save_Doc(prefe, data.getSsnImage());
    }


    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialog(PaymentInfoActivity.this, message);
    }
}
