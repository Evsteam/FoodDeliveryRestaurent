package com.evs.foodexp.userPkg.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.evs.foodexp.R;
import com.evs.foodexp.userPkg.activity.ServiceFragment;
import com.evs.foodexp.userPkg.activity.ToGetActivity;
import com.evs.foodexp.utils.SessionManager;

public class UserHomeFragment extends Fragment implements View.OnClickListener{

    private ImageView foodButtonImg,toGetService,serviceBtn;
    FragmentManager fragmentManager;
    Fragment fragment = null;
    TextView tv_title;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main,container,false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        foodButtonImg = (ImageView)view.findViewById(R.id.foodForCustomer);
        toGetService = (ImageView)view.findViewById(R.id.toGetServices);
        serviceBtn = (ImageView)view.findViewById(R.id.serviceForCustomer);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
//        tv_title.setText("Welcome "+ SessionManager.get_name(preferences)+", Enjoy our services");
        tv_title.setText(R.string.DashTitile);
        foodButtonImg.setOnClickListener(this);
        toGetService.setOnClickListener(this);
        serviceBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        getActivity().setTitle("DASHBOARD");
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if(v == foodButtonImg){
            fragment = new RestaurentListFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user_home, fragment)
                    .addToBackStack(null).commit();
        }
        if(v == toGetService){
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.frame_container_user_home, new ToGetActivity())
                    .addToBackStack(null).commit();
        }
        if(v == serviceBtn){
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.frame_container_user_home, new ServiceFragment())
                    .addToBackStack(null).commit();

        }

    }
}
