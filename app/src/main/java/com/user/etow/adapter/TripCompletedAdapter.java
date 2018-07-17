package com.user.etow.adapter;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/28
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.user.etow.R;
import com.user.etow.adapter.base.BaseRecyclerViewAdapter;
import com.user.etow.adapter.base.Releasable;
import com.user.etow.constant.Constant;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.injection.ActivityContext;
import com.user.etow.models.Trip;
import com.user.etow.ui.trip_detail.TripDetailActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class TripCompletedAdapter extends RecyclerView.Adapter<TripCompletedAdapter.TripCompletedViewHolder>
        implements Releasable {

    private Context context;
    private List<Trip> listTripCompleted;
    private RecyclerView mRecyclerView;

    @Inject
    public TripCompletedAdapter(@ActivityContext Context context) {
        this.context = context;
    }

    @Override
    public TripCompletedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TripCompletedViewHolder viewHolder = TripCompletedViewHolder.create(parent);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TripCompletedViewHolder holder, int position) {
        holder.bindData(context, listTripCompleted.get(position), position);
    }

    @Override
    public int getItemCount() {
        return null == listTripCompleted ? 0 : listTripCompleted.size();
    }

    public void setListData(List<Trip> list) {
        this.listTripCompleted = list;
        notifyDataSetChanged();
    }

    public void injectInto(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(this);
    }

    @Override
    public void release() {
        context = null;
    }

    public static class TripCompletedViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder<Trip> {

        @BindView(R.id.layout_item)
        LinearLayout layoutItem;

        public TripCompletedViewHolder(View itemView) {
            super(itemView);
        }

        public static TripCompletedViewHolder create(ViewGroup parent) {
            return new TripCompletedViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_trip_completed, parent, false));
        }

        @Override
        public void bindData(Context context, Trip trip, int position) {
            if (trip != null) {
                layoutItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Constant.IS_TRIP_COMPLETED, true);
                        bundle.putSerializable(Constant.OBJECT_TRIP, trip);
                        GlobalFuntion.startActivity(context, TripDetailActivity.class, bundle);
                    }
                });
            }
        }
    }
}