package com.evs.foodexp.userPkg.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.evs.foodexp.R;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;

public class ToGetActivity extends Fragment {

    Button btn_next;
    EditText whatLikeToGet, whatToStore;
    SharedPreferences preferences;
    LoaderProgress progress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_to_get, container, false);
        getActivity().setTitle("TO GET");
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        btn_next = (Button) view.findViewById(R.id.btn_next);
        whatLikeToGet = (EditText) view.findViewById(R.id.whatLikeToGet);
        whatToStore = (EditText) view.findViewById(R.id.whatToStore);
        progress = new LoaderProgress(getContext());

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Global.validatename(whatLikeToGet.getText().toString())){
                    whatLikeToGet.setError("Enter What would you like us to get");
                }else  if(!Global.validatename(whatToStore.getText().toString())){
                    whatToStore.setError("Enter Store Name");
                }else if(Global.isOnline(getContext())){
                    Intent intent = new Intent(getContext(), ToGetDisclouserActivity.class);
                    intent.putExtra("store",whatToStore.getText().toString());
                    intent.putExtra("likeGet",whatLikeToGet.getText().toString());
                    startActivity(intent);
                }else Global.showDialog(getActivity());

            }
        });

        return view;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_to_get);
//
//        Utills.StatusBarColour(ToGetActivity.this);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarToGet);
//
//        toGetBtn.setOnClickListener(this);
//        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
//        toolbarTitle.setText("TO GET");
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//
//    }

}
