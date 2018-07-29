package com.user.etow.adapter.autocompleteaddress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.user.etow.R;
import com.user.etow.constant.Constant;

import java.util.ArrayList;

public class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements
		Filterable {
	public ArrayList<String> resultList;

	LayoutInflater mInflater;

	public PlaceAPI mPlaceAPI = new PlaceAPI();
	Context mContext;
	int mResource;
	boolean mIsSelectCurrentLocation;

	public PlacesAutoCompleteAdapter(Context context, int resource, boolean isSelectCurrentLocation) {
		super(context, resource);

		mContext = context;
		mResource = resource;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mIsSelectCurrentLocation = isSelectCurrentLocation;
	}

	@Override
	public int getCount() {
		// Last item will be the footer
		return resultList.size();
	}

	@Override
	public String getItem(int position) {
		return resultList.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;

		if (convertView == null) {
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.item_auto_place, null);
			holder.lblPlace = convertView.findViewById(R.id.lbl_auto_place);
			holder.layoutUseCurrentLocation = convertView.findViewById(R.id.layout_use_current_location);
			holder.lblPlace.setSelected(true);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		String strPlace = resultList.get(position);
		if (mContext.getString(R.string.current_location).equals(strPlace)) {
			holder.layoutUseCurrentLocation.setVisibility(View.VISIBLE);
			holder.lblPlace.setVisibility(View.GONE);
		} else {
			holder.layoutUseCurrentLocation.setVisibility(View.GONE);
			holder.lblPlace.setVisibility(View.VISIBLE);
			if (!strPlace.equals("") && strPlace != null) {
				holder.lblPlace.setText(resultList.get(position));
			}
		}

		return convertView;
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				if (constraint != null) {
					resultList = mPlaceAPI.autocomplete(constraint.toString());

					filterResults.values = resultList;
					filterResults.count = resultList.size();

					if (mIsSelectCurrentLocation) {
						resultList.add(0, getContext().getString(R.string.current_location));
					}
				}

				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				if (results != null && results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}
		};

		return filter;
	}

	class Holder {
		TextView lblPlace;
		LinearLayout layoutUseCurrentLocation;
	}
}
