package com.evs.foodexp.restaurentPkg.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import  androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.viewModel.RestorentViewModel;
import com.evs.foodexp.models.ReviewModel;
import com.evs.foodexp.restaurentPkg.adapter.reviews.ReviewPagedAdapter;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;

public class ReviewsFragment extends Fragment implements AuthStatusListener {
    private SharedPreferences prefs;
    RecyclerView recylerView;
    LoaderProgress progress;
    RestorentViewModel viewModel;
    ReviewPagedAdapter adapter;
    TextView tv_notFound;
    LinearLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cashout_history, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        mTitle.setText("Reviews");
        recylerView=view.findViewById(R.id.recyclerView);
        tv_notFound=view.findViewById(R.id.tv_notFound);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        progress = new LoaderProgress(getContext());
        viewModel = new ViewModelProvider(getActivity()).get(RestorentViewModel.class);

        layoutManager = new LinearLayoutManager(getActivity());
        recylerView.setLayoutManager(layoutManager);
        recylerView.addItemDecoration(new DividerItemDecoration(getActivity(), new LinearLayoutManager(getContext()).getOrientation()));

        if (Global.isOnline(getContext())) {
            viewModel.apiReviews(SessionManager.get_user_id(prefs),  this);
        } else Global.showDialog(getActivity());

        adapter = new ReviewPagedAdapter(getContext());

        viewModel.getReviews().observe(this, new Observer<PagedList<ReviewModel>>() {
            @Override
            public void onChanged(PagedList<ReviewModel> reviewsModels) {
//                progress.dismiss();
                adapter.submitList(reviewsModels);

            }
        });
        recylerView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onFailure(String message) {
        progress.dismiss();
        if (message.equalsIgnoreCase("0")) {
            tv_notFound.setVisibility(View.GONE);
        }else if (message.equalsIgnoreCase("1")){
            tv_notFound.setVisibility(View.VISIBLE);
        }else Global.msgDialog(getActivity(),message);
    }
}
