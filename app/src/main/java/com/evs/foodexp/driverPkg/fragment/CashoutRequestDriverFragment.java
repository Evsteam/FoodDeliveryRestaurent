package com.evs.foodexp.driverPkg.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.evs.foodexp.R;

public class CashoutRequestDriverFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cashout_request_drver_layout,container,false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("CASHOUT");
    }
}
