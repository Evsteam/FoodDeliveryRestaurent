package com.evs.foodexp.userPkg.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import  androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.DTO.AddBookingDto;
import com.evs.foodexp.commonPkg.DTO.AddBookingRequest;
import com.evs.foodexp.commonPkg.common.activity.AddYourLoaction;
import com.evs.foodexp.commonPkg.common.sharedPreference.MySharedPreference;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;
import com.evs.foodexp.utils.Utills;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimeAndDateScheduleActivity extends AppCompatActivity
        implements View.OnClickListener, AuthListener<AddBookingDto>, AuthMsgListener {

    private Button timeAndDateScheduleBtn;
    private CaldroidFragment caldroidFragment;
    String selecteddate = "", offDays = "", startTimeStr = "", endTimeStr = "";
    LinearLayout scheduleStart, scheduleEnd;
    int i = 50, j = 0;
    private LoaderProgress progressDialog;
    int dayh, mHour, mMinute;
    SharedPreferences preferences;
    TextView startTimeTxt, endTimeTxt, totalPayableAmountService,
            selectTimeHourlyAmount, payableAmountTxt, tv_transactionfee;
    EditText other_amount, et_notes, addressEdit;
    APIViewModel apiViewModel;
    AddBookingRequest bookingRequest;
    double latitude, longitude;
    SimpleDateFormat format;
    RadioGroup tip_group;
    double tipAmount = 0.0, totalAmount = 0.0, estimatePrice = 0.0, discount = 0, totalServiceAmount = 0, transactionfee = 2.9;
    String serviceAmount = "100", services, desc, stateTax = "0";
    private static final int GETLOCATION = 3;
    ImageView gettingLocation;
    boolean stateTaxAply = false;
    Double estimatehours;
    SimpleDateFormat df;
    DecimalFormat formater = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_and_date_schedule);
        Utills.StatusBarColour(TimeAndDateScheduleActivity.this);
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        bookingRequest = new AddBookingRequest();
        timeAndDateScheduleBtn = (Button) findViewById(R.id.confirmPaymentDateTimeSchedule);
        tv_transactionfee = (TextView) findViewById(R.id.tv_transactionfee);
        selectTimeHourlyAmount = (TextView) findViewById(R.id.selectTimeHourlyAmount);
        payableAmountTxt = (TextView) findViewById(R.id.payableAmountTxt);
        totalPayableAmountService = (TextView) findViewById(R.id.totalPayableAmountService);
        et_notes = (EditText) findViewById(R.id.et_notes);
        preferences = PreferenceManager.getDefaultSharedPreferences(TimeAndDateScheduleActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDateTimeSchedule);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        startTimeTxt = (TextView) findViewById(R.id.startTimeTxt);
        endTimeTxt = (TextView) findViewById(R.id.endTimeTxt);
        tip_group = (RadioGroup) findViewById(R.id.tip_group);
        other_amount = (EditText) findViewById(R.id.other_amount);
        addressEdit = (EditText) findViewById(R.id.userAddressEdit);
        gettingLocation = (ImageView) findViewById(R.id.gettingLocation);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            services = bundle.getString("service");
            desc = bundle.getString("desc");
            serviceAmount = bundle.getString("serviceAmount");
        }
        selectTimeHourlyAmount.setText("$ " + serviceAmount);
        scheduleStart = (LinearLayout) findViewById(R.id.scheduleStartTimeLayout);
        scheduleEnd = (LinearLayout) findViewById(R.id.scheduleEndTimeLayout);

        toolbarTitle.setText("SCHEDULE DATE & TIME");
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


        transactionfee = Double.parseDouble(serviceAmount) * transactionfee / 100;
        transactionfee = transactionfee + 0.30;
        tv_transactionfee.setText("$ " + formater.format(transactionfee));


        caldroidFragment = new CaldroidFragment();
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendarLayout, caldroidFragment);
        t.commit();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
        args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);
        caldroidFragment.setArguments(args);
        caldroidFragment.setMinDate(new Date());
        scheduleStart.setOnClickListener(this);
        scheduleEnd.setOnClickListener(this);
        progressDialog = new LoaderProgress(TimeAndDateScheduleActivity.this);

        String[] separatedhr = offDays.split(",");
        for (int i = 0; i < separatedhr.length; i++) {
            breakDays(separatedhr[i]);
        }


        if (!Global.GpsEnable(TimeAndDateScheduleActivity.this)) {
            Global.showGPSDisabledAlertToUser(TimeAndDateScheduleActivity.this);
        } else {
            setMyLastLocation();
        }

        gettingLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Global.GpsEnable(TimeAndDateScheduleActivity.this)) {
                    Global.showGPSDisabledAlertToUser(TimeAndDateScheduleActivity.this);
                } else {
                    setMyLastLocation();
                }
            }
        });

        addressEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TimeAndDateScheduleActivity.this, AddYourLoaction.class);
                startActivityForResult(i, GETLOCATION);
            }
        });
        df = new SimpleDateFormat("dd-MM-yyyy");
        selecteddate = df.format(new Date());
        caldroidFragment.setCaldroidListener(new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                selecteddate = df.format(date);
//
                caldroidFragment.clearSelectedDates();
                caldroidFragment.setSelectedDate(date);
                caldroidFragment.refreshView();
                System.out.println("Selected Date === " + selecteddate);
                MySharedPreference.setServiceDate(preferences, selecteddate);
                //    confirm_dateandTime.setText("Selected Chore Date : "+selecteddate);
                Toast.makeText(TimeAndDateScheduleActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
//                dataViewModel.getTimeSlot(userId, selecteddate, SelectDate_time.this);
            }

            @Override
            public void onChangeMonth(int month, int year) {
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                Toast.makeText(TimeAndDateScheduleActivity.this, df.format(date), Toast.LENGTH_SHORT).show();

            }
        });

        System.out.println("Location All Time main === " + latitude + "," + longitude);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermissionLocation()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }

                    setMyLastLocation();
                }

            } else {
                requestPermissionLocation();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setMyLastLocation();
            }
        }

        totalPayableAmountService.setText("$ " + formater.format(transactionfee + Double.parseDouble(serviceAmount)));
//        tv_tipAmount.setText("$ 0");
        tip_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_five:
                        other_amount.setText("");
//                        tipAmount = Double.parseDouble(serviceAmount) * 5 / 100;
                        tipAmount = 5;
                        other_amount.setVisibility(View.GONE);
                        break;
                    case R.id.rb_ten:
                        other_amount.setText("");
//                        tipAmount = Double.parseDouble(serviceAmount) * 10 / 100;
                        tipAmount = 10;
                        other_amount.setVisibility(View.GONE);
                        break;
                    case R.id.rb_fifteen:
                        other_amount.setText("");
//                        tipAmount = Double.parseDouble(serviceAmount) * 15 / 100;
                        tipAmount = 15;
                        other_amount.setVisibility(View.GONE);
                        break;
                    case R.id.rb_twenty:
                        other_amount.setText("");
//                        tipAmount = Double.parseDouble(serviceAmount) * 20 / 100;
                        tipAmount = 20;
                        other_amount.setVisibility(View.GONE);
                        break;
                    case R.id.rb_other:
                        other_amount.setVisibility(View.VISIBLE);
                        tipAmount = 0;
                        break;

                }
//
                if (Global.validatename(startTimeStr)) {
                    if (Global.validatename(endTimeStr)) {
                        totalAmount = totalServiceAmount + tipAmount;
                    } else totalAmount = Double.parseDouble(serviceAmount) + tipAmount;
                } else totalAmount = Double.parseDouble(serviceAmount) + tipAmount;


//                totalAmount = Double.parseDouble(serviceAmount) + tipAmount;
                totalAmount = totalAmount - discount + Double.parseDouble(stateTax);
                transactionfee = totalAmount * 2.9 / 100;
                transactionfee = transactionfee + 0.30;
                tv_transactionfee.setText("$ " + formater.format(transactionfee));
                totalPayableAmountService.setText("$ " + formater.format(totalAmount + transactionfee));
            }
        });

        TextWatcher mDateEntryWatcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
//                    tipAmount = Double.parseDouble(serviceAmount) * Integer.parseInt(String.valueOf(s)) / 100;
                    tipAmount = Integer.parseInt(String.valueOf(s));
                    if (Global.validatename(startTimeStr)) {
                        if (Global.validatename(endTimeStr)) {
                            totalAmount = totalServiceAmount + tipAmount;
                        } else totalAmount = Double.parseDouble(serviceAmount) + tipAmount;
                    } else totalAmount = Double.parseDouble(serviceAmount) + tipAmount;

                    totalAmount = totalAmount - discount + Double.parseDouble(stateTax);

                    totalAmount = totalAmount - discount + Double.parseDouble(stateTax);
                    transactionfee = totalAmount * 2.9 / 100;
                    transactionfee = transactionfee + 0.30;
                    tv_transactionfee.setText("$ " + formater.format(transactionfee));
                    totalPayableAmountService.setText("$ " + formater.format(totalAmount + transactionfee));
//                    tv_tipAmount.setText("$ "+tipAmount);
                } else {
                    tipAmount = 0;
                    if (Global.validatename(startTimeStr)) {
                        if (Global.validatename(endTimeStr)) {
                            totalAmount = totalServiceAmount + tipAmount;
                        } else totalAmount = Double.parseDouble(serviceAmount) + tipAmount;
                    } else totalAmount = Double.parseDouble(serviceAmount) + tipAmount;
                    totalAmount = totalAmount - discount + Double.parseDouble(stateTax);

                    totalAmount = totalAmount - discount + Double.parseDouble(stateTax);
                    transactionfee = totalAmount * 2.9 / 100;
                    transactionfee = transactionfee + 0.30;
                    tv_transactionfee.setText("$ " + formater.format(transactionfee));
                    totalPayableAmountService.setText("$ " + formater.format(totalAmount + transactionfee));
//                    tv_tipAmount.setText("$ "+tipAmount);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

        };
        other_amount.addTextChangedListener(mDateEntryWatcher);

        timeAndDateScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                "action":"addbooking",
//                        "address":"MP SH 2, Kailaras, Madhya Pradesh 476224, India","bookingDate":"09-04-2020"
//                "latitude":"26.3042163","longitude":"77.62193","otherService":"","slote":"15:16-13:16"

                if (!Global.validatename(selecteddate)) {
                    Toast.makeText(TimeAndDateScheduleActivity.this, "Please Select  date", Toast.LENGTH_LONG).show();
                } else if (!Global.validatename(startTimeStr)) {
                    Toast.makeText(TimeAndDateScheduleActivity.this, "Please Select Start date", Toast.LENGTH_LONG).show();
                } else if (!Global.validatename(endTimeStr)) {
                    Toast.makeText(TimeAndDateScheduleActivity.this, "Please Select End date", Toast.LENGTH_LONG).show();
                } else if (!Global.validatename(addressEdit.getText().toString())) {
                    Toast.makeText(TimeAndDateScheduleActivity.this, "Please Select Address", Toast.LENGTH_LONG).show();
                } else if (tipAmount == 0) {
                    Toast.makeText(TimeAndDateScheduleActivity.this, "Please Select Tip Amount", Toast.LENGTH_LONG).show();
                } else if (!stateTaxAply) {
                    Toast.makeText(TimeAndDateScheduleActivity.this, "State is not Available Please Enter Correct Address", Toast.LENGTH_LONG).show();
                } else {
                    totalAmount = totalAmount + Double.parseDouble(stateTax);
//                    totalAmount=totalAmount-transactionfee;
                    bookingRequest.setAction("addbooking");
                    bookingRequest.setUserId(SessionManager.get_user_id(preferences));
                    bookingRequest.setServiceId(services);
                    bookingRequest.setBookingDate(selecteddate);
                    bookingRequest.setSlote(startTimeStr + "-" + endTimeStr);
                    bookingRequest.setAddress(getAddress(new LatLng(latitude, longitude)));
                    bookingRequest.setLatitude(String.valueOf(latitude));
                    bookingRequest.setLongitude(String.valueOf(longitude));
                    bookingRequest.setserviceAmount(String.valueOf(totalServiceAmount));
                    bookingRequest.setTotalAmount("" + totalAmount);
                    bookingRequest.setTIP("" + tipAmount);
                    bookingRequest.setOtherService(desc);
                    bookingRequest.setTransactionfee("" + transactionfee);
                    bookingRequest.setSpecialNote(et_notes.getText().toString());
                    bookingRequest.setSalesTax(stateTax);
//                    bookingRequest.setDiscountAmount(discount);

                    System.out.println("Full data add booking == " + bookingRequest);
                    apiViewModel.addBooking(bookingRequest, TimeAndDateScheduleActivity.this);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {

        if (v == scheduleStart) {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {


                            startTimeStr = String.format("%02d:%02d", hourOfDay, minute);
                            if (df.format(new Date()).equalsIgnoreCase(selecteddate)) {
                                Calendar.getInstance().getTime().setHours(Calendar.getInstance().getTime().getMinutes() + 1);
                                SimpleDateFormat sim = new SimpleDateFormat("HH:mm");
                                sleteStartTime(startTimeStr, sim.format(Calendar.getInstance().getTime()));
                                Log.e("Current Time", sim.format(Calendar.getInstance().getTime()));
                            } else {
                                startTimeTxt.setText(startTimeStr);
                            }


                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }

        if (v == scheduleEnd) {
            if (Global.validatename(startTimeTxt.getText().toString())) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                endTimeStr = String.format("%02d:%02d", hourOfDay, minute);
                                sletedDateTime(startTimeTxt.getText().toString(), endTimeStr);

                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            } else
                Toast.makeText(TimeAndDateScheduleActivity.this, "Please Select Start Time First!!", Toast.LENGTH_LONG).show();

        }

    }

    private void sletedDateTime(String startTime, String endTime) {
        Date date1, date2;
        int days, hours, min;
        totalAmount = 0;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        try {
            date1 = simpleDateFormat.parse(startTime);
            date2 = simpleDateFormat.parse(endTime);
            long difference = date2.getTime() - date1.getTime();
            if (difference > 0) {

                days = (int) (difference / (1000 * 60 * 60 * 24));
                hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
                min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
                hours = (hours < 0 ? -hours : hours);
                min = (min < 0 ? -min : min);
                Log.i("======= Hours", " :: " + min);
                Log.i("======= Hours", " :: " + hours * 60);
//                min = min + hours * 60;
                Log.i("======= Hours", " :: " + min);

                Log.i("======= Hours", " :: " + estimatehours);
                if (min == 0) {
                    endTimeTxt.setText(endTime);
                    totalAmount = Double.parseDouble(serviceAmount) * hours;
                    totalServiceAmount = Double.parseDouble(serviceAmount) * hours;
//                    totalAmount = totalAmount+Double.parseDouble(serviceAmount);


                    totalAmount = totalAmount + tipAmount;
                    totalAmount = totalAmount - discount;

                    transactionfee = totalAmount * 2.9 / 100;
                    transactionfee = transactionfee + 0.30;
                    tv_transactionfee.setText("$ " + formater.format(transactionfee));
                    totalPayableAmountService.setText("$ " + new DecimalFormat("##.##").format(totalAmount + transactionfee));

                } else {
                    endTimeTxt.setText("");
                    endTimeStr = "";
                    Toast.makeText(TimeAndDateScheduleActivity.this, "You can only select the time on Hourly Basis", Toast.LENGTH_LONG).show();
                }
//                totalAmount = Double.parseDouble(new DecimalFormat("##.##").format(totalAmount));

            } else {
                endTimeTxt.setText("");
                endTimeStr = "";
                Toast.makeText(TimeAndDateScheduleActivity.this, "Please select the Valid time!!", Toast.LENGTH_LONG).show();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void sleteStartTime(String startTime, String endTime) {
        Date date1, date2;


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        try {
            date1 = simpleDateFormat.parse(startTime);
            date2 = simpleDateFormat.parse(endTime);
            long difference = date1.getTime() - date2.getTime();
            if (difference > 0) {
                startTimeTxt.setText(startTime);
            } else {
                startTimeStr = "";
                startTimeTxt.setText("");
                Toast.makeText(TimeAndDateScheduleActivity.this, "Please Enter Valid Time", Toast.LENGTH_LONG).show();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void breakDays(String day) {
        switch (day) {
            case "Sun":
                dayh = Calendar.SUNDAY;
                break;
            case "Mon":
                dayh = Calendar.MONDAY;
                break;
            case "Tue":
                dayh = Calendar.TUESDAY;
                break;
            case "Wed":
                dayh = Calendar.WEDNESDAY;
                break;
            case "Thu":
                dayh = Calendar.THURSDAY;
                break;
            case "Fri":
                dayh = Calendar.FRIDAY;
                break;
            case "Sat":
                dayh = Calendar.SATURDAY;
                break;
        }

    }

    @Override
    public void onStarted() {
        progressDialog.show();
    }

    @Override
    public void onSuccess(LiveData<AddBookingDto> data) {
        try {
            if (data.getValue().getStatus().equalsIgnoreCase("success")) {
                progressDialog.dismiss();
//                Intent intent = new Intent(TimeAndDateScheduleActivity.this, PaymentActivity.class);
//                intent.putExtra("groupId", "" + data.getValue().getBookingGroupId());
//                intent.putExtra("totalAmount", "" + totalAmount);
//                intent.putExtra("tipAmount", "" + tipAmount);
//                intent.putExtra("discount", "" + discount);
//                intent.putExtra("serviceAmount", "" + serviceAmount);
//                intent.putExtra("service", "" + services);
//                startActivity(intent);

                Intent intent = new Intent(TimeAndDateScheduleActivity.this, BookingSuceess.class);
                intent.putExtra("foodOrderGroupId", data.getValue().getBookingGroupId());
                intent.putExtra("totalAmount", ""+totalAmount);
                intent.putExtra("type", "service");
                intent.putExtra("transactionFee", ""+transactionfee);
                startActivity(intent);
            } else Global.msgDialog(TimeAndDateScheduleActivity.this, data.getValue().getMsg());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error because, " + e.getMessage());
        }


    }

    @Override
    public void onFailure(String message) {
        progressDialog.show();
        System.out.println("Error because, " + message);
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        progressDialog.dismiss();
        if (msgResponce.getStatus().equalsIgnoreCase("stateSuccess")) {
            if (Double.parseDouble(msgResponce.getTotalCartItem()) > 0) {
                double stateTx = totalAmount * Double.parseDouble(msgResponce.getTotalCartItem()) / 100;
                stateTax = String.valueOf(stateTx);
            } else stateTax = "0.0";
            stateTaxAply = true;
        } else {
            stateTaxAply = false;
            Global.msgDialog(TimeAndDateScheduleActivity.this, msgResponce.getMsg());
        }
    }


    protected boolean checkPermissionLocation() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    protected void requestPermissionLocation() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(getApplicationContext(), "Read Phone State permission allows us to get Serial Number. Please allow this permission.", Toast.LENGTH_LONG).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
        }
    }

    private String getAddress(LatLng latlang) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        String address = "";
        try {
            addresses = geocoder.getFromLocation(latlang.latitude, latlang.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            apiViewModel.callState(state, TimeAndDateScheduleActivity.this);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GETLOCATION) {
            addressEdit.setText(data.getStringExtra("address"));
            latitude = Double.parseDouble(data.getStringExtra("lat"));
            longitude = Double.parseDouble(data.getStringExtra("long"));
            if (data.getStringExtra("address") != null && data.getStringExtra("address").length() != 0) {
                getAddress(new LatLng(latitude, longitude));
            }
        }
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
                    addressEdit.setText(getAddress(new LatLng(location.getLatitude(), location.getLongitude())));
                    latitude=location.getLatitude();
                    longitude=location.getLongitude();
                }
            }
        });
    }
}
