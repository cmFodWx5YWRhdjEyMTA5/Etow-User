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
import android.widget.TextView;

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

public class TripUpcomingAdapter extends RecyclerView.Adapter<TripUpcomingAdapter.TripUpcomingViewHolder>
        implements Releasable {

    private Context context;
    private List<Trip> listTripUpcoming;
    private RecyclerView mRecyclerView;

    @Inject
    public TripUpcomingAdapter(@ActivityContext Context context) {
        this.context = context;
    }

    @Override
    public TripUpcomingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TripUpcomingViewHolder viewHolder = TripUpcomingViewHolder.create(parent);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TripUpcomingViewHolder holder, int position) {
        holder.bindData(context, listTripUpcoming.get(position), position);
    }

    @Override
    public int getItemCount() {
        return null == listTripUpcoming ? 0 : listTripUpcoming.size();
    }

    public void setListData(List<Trip> list) {
        this.listTripUpcoming = list;
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

    public static class TripUpcomingViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder<Trip> {

        @BindView(R.id.tv_status)
        TextView tvStatus;

        @BindView(R.id.layout_item)
        LinearLayout layoutItem;

        public TripUpcomingViewHolder(View itemView) {
            super(itemView);
        }

        public static TripUpcomingViewHolder create(ViewGroup parent) {
            return new TripUpcomingViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_trip_upcoming, parent, false));
        }

        @Override
        public void bindData(Context context, Trip trip, int position) {
            if (trip != null) {
                /*if (trip.getStatus() == 1) {
                    tvStatus.setText(context.getString(R.string.pending));
                    tvStatus.setTextColor(context.getResources().getColor(R.color.orange));
                } else if (trip.getStatus() == 2) {
                    tvStatus.setText(context.getString(R.string.confirmed));
                    tvStatus.setTextColor(context.getResources().getColor(R.color.button_green));
                } else {
                    tvStatus.setText(context.getString(R.string.no_driver_available));
                    tvStatus.setTextColor(context.getResources().getColor(R.color.button_red));
                }*/

                layoutItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Constant.IS_TRIP_COMPLETED, false);
                        bundle.putSerializable(Constant.OBJECT_TRIP, trip);
                        GlobalFuntion.startActivity(context, TripDetailActivity.class, bundle);
                    }
                });
            }
        }
    }
}