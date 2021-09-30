package com.evs.foodexp.userPkg.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import  androidx.lifecycle.ViewModelProvider;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.DTO.UpdatePaymentRequest;
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

public class MorePayment extends AppCompatActivity implements AuthMsgListener {
    String TokenIds = "";
    private LinearLayout paymentBtn;
    SharedPreferences preferences;
    EditText creditCardNumberEdit,cardExpireMonth,et_cvv,et_cardNumber,et_expDate;
    String togetRequestId, amount;
    UpdatePaymentRequest paymentRequest;
    LoaderProgress progress;
    TextView makePaymentTxt,payablePaymentAmount,sch_laterUser;
    APIViewModel apiViewModel;
    boolean isValid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_user);

        Utills.StatusBarColour(MorePayment.this);
        paymentBtn = (LinearLayout)findViewById(R.id.paymentBtn);
        creditCardNumberEdit=findViewById(R.id.creditCardNumberEdit);
        cardExpireMonth=findViewById(R.id.cardExpireMonth);
        et_cvv=findViewById(R.id.et_cvv);
        et_cardNumber=findViewById(R.id.et_cardNumber);
        et_expDate=findViewById(R.id.et_expDate);
        makePaymentTxt = (TextView) findViewById(R.id.sch_laterUser);
        payablePaymentAmount = (TextView) findViewById(R.id.payablePaymentAmount);
        sch_laterUser = (TextView) findViewById(R.id.sch_laterUser);

        preferences = PreferenceManager.getDefaultSharedPreferences(MorePayment.this);
        paymentRequest = new UpdatePaymentRequest();
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        progress = new LoaderProgress(MorePayment.this);

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

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            togetRequestId=bundle.getString("togetRequestId");
            amount=bundle.getString("amount");
        }
        payablePaymentAmount.setText("$ "+amount);


        creditCardNumberEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_cardNumber.setText(s);
                if(CardValidator.validateCardNumber(s.toString())){}
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


                    }
                } else if (working.length() == 5 && before == 0) {
                    String enteredYear = working.substring(3);
                    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                    currentYear= Integer.parseInt(String.valueOf(currentYear).substring(2));
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



        sch_laterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(slipt(et_expDate.getText().toString()).length==2){
                    paynow(et_cardNumber.getText().toString(),
                            Integer.parseInt(slipt(et_expDate.getText().toString())[0]),
                            Integer.parseInt(slipt(et_expDate.getText().toString())[1]),
                            et_cvv.getText().toString(),
                            amount);
                }else Toast.makeText(MorePayment.this,"Expire Date is not valid",Toast.LENGTH_LONG).show();

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
        Global.showDialog(MorePayment.this);
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        progress.dismiss();
        if(msgResponce.getStatus().equalsIgnoreCase("success")){
            onBackPressed();
        }
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


                        Double usd = Double.parseDouble(new DecimalFormat("##.##")
                                .format(Double.parseDouble(amount)));

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
                            if(Global.validatename(TokenIds)) {
                               apiViewModel.moreRequestPayment(SessionManager.get_user_id(preferences),togetRequestId,TokenIds,MorePayment.this);
                            }else Global.msgDialog(MorePayment.this,"Transaction Faild");

                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.print("" + e.getMessage());
                            progress.dismiss();
                            Global.msgDialog(MorePayment.this, e.getMessage());
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
