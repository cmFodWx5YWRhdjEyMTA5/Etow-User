package com.user.etow.adapter;

/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. All rights reserved
 *  Author DangTin. Create on 2018/07/12
 * ******************************************************************************
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.user.etow.R;
import com.user.etow.models.CountryCode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CountryCodeAdapter extends ArrayAdapter<CountryCode> {

    private Context context;

    public CountryCodeAdapter(@NonNull Context context, @LayoutRes int resource,
                              @NonNull ArrayList<CountryCode> list) {
        super(context, resource, list);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_choose_country_code, null);
            TextView tvCountryCode = convertView.findViewById(R.id.tv_country_code);
            tvCountryCode.setText(this.getItem(position).getDialCode());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_drop_down_country_code, null);
        ImageView imgCountryCode = (ImageView) view.findViewById(R.id.image_country);
        TextView tvName = (TextView) view.findViewById(R.id.textview_name);
        TextView tvDialCode = (TextView) view.findViewById(R.id.textview_dial_code);

        try {
            // get input stream
            InputStream ims = context.getAssets().open("country_code/"
                    + this.getItem(position).getCode() + ".png");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            imgCountryCode.setImageDrawable(d);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        tvName.setText(this.getItem(position).getName());
        tvDialCode.setText(this.getItem(position).getDialCode());
        return view;
    }
}
