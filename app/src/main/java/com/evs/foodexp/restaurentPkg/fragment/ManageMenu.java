package com.evs.foodexp.restaurentPkg.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import  androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.DTO.MenuListDto;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.restaurentPkg.activity.AddMenuListActivity;
import com.evs.foodexp.restaurentPkg.adapter.ManageMenuAdapter;
import com.evs.foodexp.restaurentPkg.adapter.PagedItemClick;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;

import java.util.ArrayList;

public class ManageMenu extends Fragment implements AuthListener<MenuListDto>,
        PagedItemClick<MenuListDto.Data> , AuthMsgListener {

    ManageMenuAdapter adapter;
    LoaderProgress progress;
    APIViewModel apiViewModel;
    RecyclerView manageMenuList;
    SharedPreferences preferences;
    TextView tv_noDataFound;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manage_menu, container, false);
        setHasOptionsMenu(true);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("Manage Menu");
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        manageMenuList = (RecyclerView) view.findViewById(R.id.manageMenuList);
        tv_noDataFound = (TextView) view.findViewById(R.id.tv_noDataFound);
        progress = new LoaderProgress(getContext());
        manageMenuList.setHasFixedSize(false);
        manageMenuList.setLayoutManager(new GridLayoutManager(getContext(), 2));



        return view;
    }


    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onSuccess(LiveData<MenuListDto> data) {
        progress.dismiss();
        if (data.getValue().getData().size() > 0) {
            adapter = new ManageMenuAdapter(getActivity(), data.getValue().getData(),this);
            manageMenuList.setAdapter(adapter);
        }else {
            tv_noDataFound.setVisibility(View.VISIBLE);
            adapter = new ManageMenuAdapter(getActivity(), new ArrayList<MenuListDto.Data>(),this);
            manageMenuList.setAdapter(adapter);
        }
    }


    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialog(getActivity(), message);


    }

    @Override
    public void onResume() {
        super.onResume();
        if (Global.isOnline(getContext())) {
            apiViewModel.menuList("menulist", SessionManager.get_user_id(preferences), SessionManager.get_user_id(preferences), "", this);
        } else Global.showDialog(getActivity());
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        progress.dismiss();
        Toast.makeText(getActivity(),msgResponce.getMsg(),Toast.LENGTH_LONG).show();
        if(msgResponce.getStatus().equalsIgnoreCase("success")){
            apiViewModel.menuList("menulist", SessionManager.get_user_id(preferences), SessionManager.get_user_id(preferences), "", this);
        }
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.restaurent_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add:
                Intent intent=new Intent(getContext(), AddMenuListActivity.class);
                intent.putExtra("categoryId","");
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void pagedmClick(MenuListDto.Data object) {
        msgDialogBack(object.getMenuId(),object.getFoodName());
    }


    public  void msgDialogBack(final String id, String name) {
        try {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(getActivity());
            alertbox.setTitle(getActivity().getResources().getString(R.string.app_name));
            alertbox.setMessage("Are you sure delete "+ name+" menu!!");
            alertbox.setPositiveButton(getActivity().getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            apiViewModel.deleteMenu(id,SessionManager.get_user_id(preferences),ManageMenu.this);
                        }
                    });
            alertbox.setNegativeButton(getActivity().getResources().getString(R.string.cancle),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });
            alertbox.show();
        } catch (Exception e) {
        }
    }
}
