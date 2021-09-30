package com.evs.foodexp.restaurentPkg.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import  androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.DTO.MenuListDto;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.restaurentPkg.activity.AddMenuListActivity;
import com.evs.foodexp.restaurentPkg.activity.RestCategoryMenuMenuActivity;
import com.evs.foodexp.restaurentPkg.adapter.CategoryAdapter;
import com.evs.foodexp.restaurentPkg.adapter.PagedItemClick;
import com.evs.foodexp.userPkg.adapter.RestaurentMenuRestAdapter;
import com.evs.foodexp.models.CategoryModel;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;

import java.util.List;

public class RestaurentHomeFragment extends Fragment implements  AuthListener<MenuListDto>, PagedItemClick<CategoryModel> {

    GridView gridView;
    RestaurentMenuRestAdapter adapter;
    LoaderProgress progress;
    APIViewModel apiViewModel;
    Button breakfast, quickByteBtn, desertBtn;
    RecyclerView categoryTypeRecycle;
    SharedPreferences preferences;
    ImageView coverImage;
    TextView tv_title,tv_noDataFound;


    private void populateGridView(final List<MenuListDto.Data> spacecraftList) {
        gridView = (GridView) getView().findViewById(R.id.grid_viewRestaurant);
        adapter = new RestaurentMenuRestAdapter(getActivity(),spacecraftList);
        gridView.setAdapter(adapter);
        if(spacecraftList.size()==0){
            tv_noDataFound.setVisibility(View.VISIBLE);
        }

//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent=new Intent(getContext(),AddMenuListActivity.class);
//                intent.putExtra("categoryId",spacecraftList.get(i).getCategoryId());
//                intent.putExtra("foodId",spacecraftList.get(i).getMenuId());
//                intent.putExtra("foodName",spacecraftList.get(i).getFoodName());
//                intent.putExtra("price",spacecraftList.get(i).getPrice());
//                intent.putExtra("specialPrice",spacecraftList.get(i).getSpecialPrice());
//                intent.putExtra("description",spacecraftList.get(i).getDescription());
//                intent.putExtra("foodImage",spacecraftList.get(i).getImage1());
//                intent.putExtra("foodType",spacecraftList.get(i).getFoodType());
//                startActivity(intent);
//            }
//        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.restaurant_home_layout_fragment,container,false);
        setHasOptionsMenu(true);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("Home");
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        breakfast = (Button)view.findViewById(R.id.breakfastBtn);
        quickByteBtn = (Button)view.findViewById(R.id.quickByteBtn);
         desertBtn = (Button)view.findViewById(R.id.desertBtn);
        coverImage = (ImageView) view.findViewById(R.id.coverImage);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_noDataFound = (TextView) view.findViewById(R.id.tv_noDataFound);
        categoryTypeRecycle = (RecyclerView)view.findViewById(R.id.categoryTypeMenuList);
        progress=new LoaderProgress(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        categoryTypeRecycle.setLayoutManager(layoutManager);
        tv_title.setText(SessionManager.get_name(preferences));
        Glide.with(getContext()).load(SessionManager.get_driverLicence(preferences)).into(coverImage);



        return view;
    }





    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onSuccess(LiveData<MenuListDto> data) {
        progress.dismiss();
        populateGridView(data.getValue().getData());
        categoryTypeRecycle.setAdapter(new CategoryAdapter(getContext(), data.getValue().getCategoryList(),this));
    }




    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialog(getActivity(),message);
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
    public void pagedmClick(CategoryModel object) {
        Intent intent=new Intent(getActivity(), RestCategoryMenuMenuActivity.class);
        intent.putExtra("restaurantId",SessionManager.get_user_id(preferences));
        intent.putExtra("categoryID",object.getId());
        intent.putExtra("name",object.getName());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Global.isOnline(getContext())) {
            apiViewModel.menuList("menulist", SessionManager.get_user_id(preferences), SessionManager.get_user_id(preferences), "", this);
        }else Global.showDialog(getActivity());
    }
}
