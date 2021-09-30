package com.evs.foodexp.userPkg.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import  androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.DTO.AnotherFoodSpecialRequest;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;

import java.text.DecimalFormat;

public class FoodAnotherOptionActivity extends Fragment  {

    Button submitRequest;
    EditText et_wtDoWant, et_price, et_tip;
    TextView tv_totalAmount,et_delaverFees;
    APIViewModel apiViewModel;
    LoaderProgress progress;
    SharedPreferences preferences;
    AnotherFoodSpecialRequest specialRequest;
    Double totalAmount=0.0;
    Double foodPrice=0.0;
    Double tipAmount=0.0;
    Double discount=0.0,transactionFee=0.0;
    TextView et_transactionFees;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_food_another_option);
//        Utills.StatusBarColour(FoodAnotherOptionActivity.this);
//        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
//
//        specialRequest = new AnotherFoodSpecialRequest();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarFoodAnotherOption);
//        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
//        toolbarImage = (ImageView)toolbar.findViewById(R.id.imageToolbar);
//        submitRequest = (Button)findViewById(R.id.submitRequestForFoodBtn);
//        anotherMenuTitleEdit = (EditText)findViewById(R.id.anotherMenuEdit);
//        priceAnotherFoodEdit = (EditText)findViewById(R.id.priceAnotherFoodOption);
//        tipAnotherFood = (EditText)findViewById(R.id.tipAnotherFoodOption);
//        totalAmount = (TextView)findViewById(R.id.totalAmountAnotherFoodOption);
//        deliveryFeesTxt = (TextView)findViewById(R.id.deliveryFeesAnotherFoodOption);
//        userId = MySharedPreference.getUserId(preferences);
//        toolbarTitle.setText("FOODS");
//        toolbarImage.setImageResource(R.drawable.ic_shopping_cart_white);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(false);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//        submitRequest.setOnClickListener(this);
//    }


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_food_another_option, container, false);
        getActivity().setTitle("Foods");
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        specialRequest = new AnotherFoodSpecialRequest();
        submitRequest = (Button) view.findViewById(R.id.submitRequestForFoodBtn);
        et_wtDoWant = (EditText) view.findViewById(R.id.et_wtDoWant);
        et_price = (EditText) view.findViewById(R.id.et_price);
        et_delaverFees = (TextView) view.findViewById(R.id.et_delaverFees);
        et_tip = (EditText) view.findViewById(R.id.et_tip);
        tv_totalAmount = (TextView) view.findViewById(R.id.tv_totalAmount);
        et_transactionFees = (TextView) view.findViewById(R.id.et_transactionFees);

        progress=new LoaderProgress(getContext());

        tv_totalAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!Global.validatename(et_wtDoWant.getText().toString())){
                    et_wtDoWant.setError("What do you want?");
                }else if(foodPrice<0){
                       Toast.makeText(getContext(),"Please Enter Food Price maximum 0",Toast.LENGTH_LONG).show();
                }else if(tipAmount<0){
                    Toast.makeText(getContext(),"Please Enter Tip Amount maximum 0",Toast.LENGTH_LONG).show();
                }else if(Global.isOnline(getContext())){
                    Intent intent = new Intent(getContext(), SpecialAddressActivity.class);
                    intent.putExtra("whatDoWant",et_wtDoWant.getText().toString());
                    intent.putExtra("price",""+foodPrice);
                    intent.putExtra("tip",""+tipAmount);
                    intent.putExtra("delFee",""+6);
                    intent.putExtra("transactionFee",""+transactionFee);
                    startActivity(intent);
                }else Global.showDialog(getActivity());

            }
        });

        et_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(charSequence.length()>0){
                            foodPrice=Double.parseDouble(String.valueOf(charSequence));
                            totalAmount=foodPrice+tipAmount+6;

                            transactionFee = totalAmount * 5.8 / 100;
                            transactionFee=transactionFee+0.60;

                            tv_totalAmount.setText("Total Amount $"+new DecimalFormat("##.##").format(totalAmount+transactionFee));
                        }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_tip.addTextChangedListener(new TextWatcher() {
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
                    tv_totalAmount.setText("Total Amount $"+new DecimalFormat("##.##").format(totalAmount+transactionFee));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void validation(){

    }



}
