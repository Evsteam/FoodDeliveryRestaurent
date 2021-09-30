package com.evs.foodexp.driverPkg.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import  androidx.lifecycle.ViewModelProvider;


import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.models.CardModel;
import com.evs.foodexp.repositry.DataResponse;
import com.evs.foodexp.restaurentPkg.adapter.AdapterItemClick;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;


public class Cards extends Fragment implements AuthListener<DataResponse<CardModel>>, AdapterItemClick {
//    RecyclerView recyclerView;
//    CardAdpter rvAdapter;
    APIViewModel viewModel;
    LoaderProgress progress;
    SharedPreferences prefs;
//    LinearLayoutManager layoutManager;
//    TextView tv_notFound;
//    private boolean isLoading = false;
//    private boolean isLastPage = false;
//    int page = 1;
//    ArrayList<CardModel> bookingList;
    TextView card_number,bank_name,exp_date,name;
    ImageView img_bankImage;
    RelativeLayout card;
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.review_layout, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        mTitle.setText("Card Details");

//        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        tv_notFound = (TextView) view.findViewById(R.id.tv_notFound);
        card_number = (TextView) view.findViewById(R.id.card_number);
        bank_name = (TextView) view.findViewById(R.id.bank_name);
        exp_date = (TextView) view.findViewById(R.id.exp_date);
        card = (RelativeLayout) view.findViewById(R.id.card);
        name = (TextView) view.findViewById(R.id.name);
        img_bankImage = (ImageView) view.findViewById(R.id.img_bankImage);

//        layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
//                new LinearLayoutManager(getContext()).getOrientation());
//        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.recycler_devider));
//        recyclerView.addItemDecoration(dividerItemDecoration);


        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        progress = new LoaderProgress(getContext());
        viewModel = new ViewModelProvider(getActivity()).get(APIViewModel.class);





//        viewModel.getCards().observe(this,
//                new Observer<List<CardModel>>() {
//            @Override
//            public void onChanged(List<CardModel> bookingModels) {
//                progress.dismiss();
//                if(bookingModels.size()>0){
//                CardAdpter adpter=new CardAdpter(getContext(),bookingModels,Cards.this);
//                recyclerView.setAdapter(adpter);
//                    tv_notFound.setVisibility(View.GONE);
//                }else {
//                    tv_notFound.setVisibility(View.VISIBLE);
//                }
//            }
//        });
        if (Global.isOnline(getContext())) {
            viewModel.getCards(SessionManager.get_user_id(prefs),Cards.this);
        } else Global.showDialog(getActivity());

        return view;
    }


    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onSuccess(LiveData<DataResponse<CardModel>> data) {
        progress.dismiss();
        if(data!=null){
            card.setVisibility(View.VISIBLE);
            name.setText(SessionManager.get_name(prefs));
            bank_name.setText("Strip");
            exp_date.setText("Exp : "+data.getValue().getData().getExp_month()+"/"+
                    data.getValue().getData().getExp_year());
            card_number.setText("X X X X   X X X X   X X X X   "+data.getValue().getData().getLast4());
        }

    }

    @Override
    public void onFailure(String message) {
            progress.dismiss();
        Global.msgDialog(getActivity(), message);
    }

    @Override
    public void onItemClick(int pos) {

    }



}