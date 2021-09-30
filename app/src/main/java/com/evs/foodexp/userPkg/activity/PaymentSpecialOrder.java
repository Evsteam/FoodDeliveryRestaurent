package com.evs.foodexp.userPkg.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import  androidx.lifecycle.ViewModelProvider;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.stripe.Stripe;
import com.evs.foodexp.stripe.TokenCallback;
import com.evs.foodexp.stripe.dialog.ErrorDialogFragment;
import com.evs.foodexp.stripe.model.Card;
import com.evs.foodexp.stripe.model.Token;
import com.evs.foodexp.utils.CardValidator;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;
import com.evs.foodexp.utils.Utills;
import com.stripe.model.Charge;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PaymentSpecialOrder extends AppCompatActivity implements AuthMsgListener {

    TextView makePaymentTxt, payablePaymentAmount;
    String totalAmount, tip, specialrequestId, totototot, whatDoWant, addressType,stateTax, likeGet, store, nocontact, landmarkStr, phoneStr, latitudeStr, longitudeStr, addressStr, stateStr, cityStr, zipStr, nameStr;
    LoaderProgress progress;
    SharedPreferences preferences;
    APIViewModel apiViewModel;
    EditText creditCardNumberEdit, cardExpireMonth, et_cvv, et_cardNumber, et_expDate;
    boolean isValid = true;
    String TokenIds = "";
    DecimalFormat formater = new DecimalFormat("#.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_user);

        Utills.StatusBarColour(PaymentSpecialOrder.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPaymentCardInfoUser);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("PAYMENT");
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
        creditCardNumberEdit = findViewById(R.id.creditCardNumberEdit);
        cardExpireMonth = findViewById(R.id.cardExpireMonth);
        et_cvv = findViewById(R.id.et_cvv);
        et_cardNumber = findViewById(R.id.et_cardNumber);
        et_expDate = findViewById(R.id.et_expDate);
        preferences = PreferenceManager.getDefaultSharedPreferences(PaymentSpecialOrder.this);
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        progress = new LoaderProgress(PaymentSpecialOrder.this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            totalAmount = bundle.getString("totalAmount");
            tip = bundle.getString("tip");
            specialrequestId = bundle.getString("specialrequestId");
//            stateStr = bundle.getString("stateStr");
//            cityStr = bundle.getString("cityStr");
//            zipStr = bundle.getString("zipStr");
//            nameStr = bundle.getString("nameStr");
//            phoneStr = bundle.getString("phoneStr");
//            landmarkStr = bundle.getString("landmarkStr");
//            addressType = bundle.getString("addressType");
//            whatDoWant = bundle.getString("whatDoWant");
//            price = bundle.getString("price");
//            tip = bundle.getString("tip");
//            delFee = bundle.getString("delFee");
//            likeGet = bundle.getString("likeGet");
//            store = bundle.getString("store");
//            stateTax = bundle.getString("stateTax");
//            nocontact = bundle.getString("nocontact");
        }

        makePaymentTxt = (TextView) findViewById(R.id.sch_laterUser);
        payablePaymentAmount = (TextView) findViewById(R.id.payablePaymentAmount);
//        if (!Global.validatename(likeGet)) {
//            Double pricess = Double.parseDouble(price) + Double.parseDouble(tip) + Double.parseDouble(delFee);
//            totototot=String.valueOf(pricess);
            payablePaymentAmount.setText("$ " + formater.format(Double.parseDouble(totalAmount)));
//        }else  payablePaymentAmount.setText("$ " + formater.format(Double.parseDouble(price)));
////       double totalPrice=Double.parseDouble(price)+Double.parseDouble(stateTax);
////        price=String.valueOf(totalPrice);


        creditCardNumberEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_cardNumber.setText(s);
                if (CardValidator.validateCardNumber(s.toString())) {
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        TextWatcher mDateEntryWatcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String working = s.toString();
                isValid = true;
                if (working.length() == 2 && before == 0) {
                    if (Integer.parseInt(working) < 1 || Integer.parseInt(working) > 12) {
                        isValid = false;
                    } else {
                        working += "/";
                        cardExpireMonth.setText(working);
                        cardExpireMonth.setSelection(working.length());
                        et_expDate.setText(working);
                    }
                } else if (working.length() == 5 && before == 0) {
                    String enteredYear = working.substring(3);
                    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                    currentYear = Integer.parseInt(String.valueOf(currentYear).substring(2));
                    if (Integer.parseInt(enteredYear) < currentYear) {
                        isValid = false;
                    }
                } else if (working.length() != 5) {
                    isValid = false;
                }

                if (!isValid) {
                    cardExpireMonth.setError("Enter a valid date: MM/YY");
                } else {
                    cardExpireMonth.setError(null);
                }
                et_expDate.setText(working);

            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

        };
        cardExpireMonth.addTextChangedListener(mDateEntryWatcher);

        makePaymentTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String totalAmount="0";
//                if (!Global.validatename(likeGet)) {
//                     totalAmount = totototot;
//                }else  totalAmount = price;

                if (slipt(et_expDate.getText().toString()).length == 2) {
                    paynow(et_cardNumber.getText().toString(),
                            Integer.parseInt(slipt(et_expDate.getText().toString())[0]),
                            Integer.parseInt(slipt(et_expDate.getText().toString())[1]),
                            et_cvv.getText().toString(),
                            totalAmount);
                }else Toast.makeText(PaymentSpecialOrder.this,"Expire Date is not valid",Toast.LENGTH_LONG).show();

            }
        });


    }


    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialog(PaymentSpecialOrder.this, message);
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        progress.dismiss();
//        Toast.makeText(PaymentSpecialOrder.this, msgResponce.getMsg(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(PaymentSpecialOrder.this, OrderPlacedActivity.class);
        intent.putExtra("foodOrderGroupId", "");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void paynow(String cardval, int monthval, int yearval, String cvcval, final String amount) {

        Card card = new Card(cardval, monthval, yearval, cvcval);
//        Card card = new Card("4242424242424242", monthval, yearval, cvcval);
        card.setCurrency("USD");
        boolean validation = card.validateCard();
        progress.show();
        if (validation) {

            new Stripe().createToken(card, Global.PUBLISHABLE_KEY, new TokenCallback() {
                public void onSuccess(Token token) {
                    progress.dismiss();
                    try {
                        com.stripe.Stripe.apiKey = Global.SECRET_KEY;
                        TokenIds = token.getId();
                        Map<String, Object> chargeParams = new HashMap<String, Object>();

                        Double    usd = Double.parseDouble(new DecimalFormat("##.##")
                                    .format(Double.parseDouble(amount)));

                        Log.e("price", String.valueOf(usd));
//                        chargeParams.put("amount", usd);
                        chargeParams.put("amount", (int) Math.round(usd * 100));
                        chargeParams.put("currency", "USD");
                        chargeParams.put("source", token.getId());
                        Map<String, String> initialMetadata = new HashMap<String, String>();
                        initialMetadata.put("order_id", "" + TokenIds);
                        chargeParams.put("metadata", initialMetadata);

                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);

                        try {
                            Charge.create(chargeParams);
                            progress.dismiss();
                            if (Global.validatename(TokenIds)) {
//                                if (Global.validatename(likeGet)) {
//                                    apiViewModel.togetOrder(latitudeStr, longitudeStr, SessionManager.get_user_id(preferences), addressStr, stateStr, cityStr, zipStr, nameStr, phoneStr,
//                                            landmarkStr, addressType, whatDoWant, price, tip, delFee, likeGet, store, nocontact, TokenIds,stateTax, PaymentSpecialOrder.this);
//                                } else {
                                    apiViewModel.specialOrderPayment( SessionManager.get_user_id(preferences), specialrequestId, totalAmount, TokenIds, tip, PaymentSpecialOrder.this);
//                                }
                            } else Global.msgDialog(PaymentSpecialOrder.this, "Transaction Faild");

                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.print("" + e.getMessage());
                            progress.dismiss();
                            Global.msgDialog(PaymentSpecialOrder.this, e.getMessage());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progress.dismiss();
                    }
                }

                public void onError(Exception error) {
                    handleError(error.getLocalizedMessage());
                }

            });
        } else if (!card.validateNumber()) {
            handleError("The card number that you entered is invalid");
        } else if (!card.validateExpiryDate()) {
            handleError("The expiration date that you entered is invalid");
        } else if (!card.validateCVC()) {
            handleError("The CVC code that you entered is invalid");
        } else {
            handleError("The card details that you entered are invalid");
        }
    }

    private void handleError(String error) {
        DialogFragment fragment = ErrorDialogFragment.newInstance(R.string.validationErrors, error);
        fragment.show(getSupportFragmentManager(), "error");
        progress.dismiss();
    }

    public String[] slipt(String expDate) {
        String[] separated = expDate.split("/");
        return separated;
    }
}
