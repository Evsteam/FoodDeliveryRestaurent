package com.evs.foodexp.userPkg.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import  androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.DTO.ToGetRequest;

import com.evs.foodexp.commonPkg.viewModel.APIViewModel;

import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.Utills;

import java.text.DecimalFormat;

public class ToGetAmountActivity extends AppCompatActivity  {

    SharedPreferences preferences;
    EditText et_price, et_tipAmount,et_notes;
    TextView tv_delevaryFee, tv_totalAmount;
    Double totalAmount=0.0,transactionFee=0.0;
    Double foodPrice=0.0;
    Double tipAmount=0.0;
    APIViewModel apiViewModel;
    ToGetRequest toGetRequest;
    LoaderProgress progress;
    Button btn_payment;
    String store,likeGet;
    CheckBox cb_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_get_amount);

        Utills.StatusBarColour(ToGetAmountActivity.this);
        preferences = PreferenceManager.getDefaultSharedPreferences(ToGetAmountActivity.this);
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        toGetRequest = new ToGetRequest();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarToGetAmount);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        progress=new LoaderProgress(ToGetAmountActivity.this);
        toolbarTitle.setText("TO GET");
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
        et_tipAmount=findViewById(R.id.et_tipAmount);
        et_notes=findViewById(R.id.et_notes);
        btn_payment=findViewById(R.id.btn_payment);
        et_price=findViewById(R.id.et_price);
        tv_delevaryFee=findViewById(R.id.tv_delevaryFee);
        cb_check=findViewById(R.id.cb_check);
        tv_totalAmount=findViewById(R.id.tv_totalAmount);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            store = bundle.getString("store");
            likeGet = bundle.getString("likeGet");
        }


        et_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                    foodPrice=Double.parseDouble(String.valueOf(charSequence));
                    totalAmount=foodPrice+tipAmount+6;

                    transactionFee = foodPrice * 5.8 / 100;
                    transactionFee=transactionFee+0.60;


                    tv_totalAmount.setText("Total Amount $"+new DecimalFormat("##.##").format(totalAmount+transactionFee));


                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_tipAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    tipAmount = Double.parseDouble(String.valueOf(charSequence));
                    totalAmount=foodPrice+tipAmount+6;


                    transactionFee = totalAmount * 5.8 / 100;
                    transactionFee=transactionFee+0.60;

                    tv_totalAmount.setText("Total Amount $ "+new DecimalFormat("##.##").format(totalAmount+transactionFee));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nocontct=""+cb_check.isChecked();
                if(nocontct.equalsIgnoreCase("true")){
                    nocontct="1";
                }else nocontct="0";

               if(foodPrice<0){
                    Toast.makeText(ToGetAmountActivity.this,"Please enter price minimum 0",Toast.LENGTH_LONG).show();
                }else if(tipAmount<0){
                    Toast.makeText(ToGetAmountActivity.this,"Please Enter Tip Amount minimum 0",Toast.LENGTH_LONG).show();
               }else if(!Global.validatename(et_notes.getText().toString())){
                   et_notes.setError("Please Enter Notes");
               }else if(Global.isOnline(ToGetAmountActivity.this)){

                    Intent intent = new Intent(ToGetAmountActivity.this, SpecialAddressActivity.class);
                    intent.putExtra("whatDoWant",et_notes.getText().toString());
                    intent.putExtra("store",store);
                    intent.putExtra("likeGet",likeGet);
                    intent.putExtra("price",""+totalAmount);
                    intent.putExtra("tip",""+tipAmount);
                    intent.putExtra("delFee",""+6);
                    intent.putExtra("nocontact",nocontct);
                    intent.putExtra("transactionFee",""+transactionFee);
                    startActivity(intent);
                }else Global.showDialog(ToGetAmountActivity.this);

            }
        });

    }


}
