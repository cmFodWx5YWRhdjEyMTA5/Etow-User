package com.user.etow.adapter;

/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. All rights reserved
 *  Author DangTin. Create on 2018/07/12
 * ******************************************************************************
 */

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.user.etow.R;
import com.user.etow.models.CountryCode;

import java.util.ArrayList;

public class CountryPayCardAdapter extends ArrayAdapter<CountryCode> {

    private Context context;

    public CountryPayCardAdapter(@NonNull Context context, @LayoutRes int resource,
                                 @NonNull ArrayList<CountryCode> list) {
        super(context, resource, list);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_choose_country_pay_card, null);
            TextView tvCountry = convertView.findViewById(R.id.tv_country);
            tvCountry.setText(this.getItem(position).getName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_drop_down_country_pay_card, null);
        TextView tvName = (TextView) view.findViewById(R.id.textview_name);
        tvName.setText(this.getItem(position).getName());
        return view;
    }
}
