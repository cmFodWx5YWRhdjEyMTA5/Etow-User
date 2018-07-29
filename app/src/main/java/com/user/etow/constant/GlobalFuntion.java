package com.user.etow.constant;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.user.etow.R;
import com.user.etow.data.prefs.DataStoreManager;
import com.user.etow.listener.IGetDateListener;
import com.user.etow.listener.IGetTimeListener;
import com.user.etow.models.CountryCode;
import com.user.etow.ui.auth.sign_in.SignInActivity;
import com.user.etow.ui.widget.image.ImagePicker;
import com.user.etow.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;

public class GlobalFuntion {

    public static double LATITUDE = 0.0;
    public static double LONGITUDE = 0.0;
    public static final int PICK_SCHEDULE_DATE = 10;
    public static final int PICK_IMAGE_AVATAR = 0;

    public static void startActivity(Context context, Class<?> clz) {
        Intent intent = new Intent(context, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(context, clz);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<?> clz, Bundle bundle, int flag) {
        Intent intent = new Intent(context, clz);
        intent.addFlags(flag);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.
                    getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public static void hideSoftKeyboard(Activity activity, TextView textView) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(textView.getWindowToken(), 0);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public static void showMessageError(Activity activity, int code) {
        switch (code) {
            /*case Constant.CODE_HTTP_401:
                Toast.makeText(activity, activity.getString(R.string.msg_error_login_with_email),
                 Toast.LENGTH_SHORT).show();
                break;*/

            case Constant.CODE_HTTP_510:
                showDialogLogout(activity);
                break;

            default:
                Toast.makeText(activity, Constant.GENERIC_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showDialogDescription(Context context, String description) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_description);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.BOTTOM;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(false);

        // Get view
        final TextView tvDescription = dialog.findViewById(R.id.tv_description);
        final TextView tvClose = dialog.findViewById(R.id.tv_close);

        tvDescription.setText(description);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static String loadJSONFromAsset(Context context, String file) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public static ArrayList<CountryCode> getListCountryCode(Context context) {
        ArrayList<CountryCode> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(loadJSONFromAsset(context, "CountryCodes.json"));
            CountryCode countryCode;
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                countryCode = new CountryCode(jsonObject.getString("name"), jsonObject.getString("dial_code"),
                        jsonObject.getString("code"));
                list.add(countryCode);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean checkMobileNumber(Context context, String mobileNumber, String code) {
        boolean isValid = false;
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.createInstance(context);
        try {
            final Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(mobileNumber, code);
            isValid = phoneNumberUtil.isValidNumber(phoneNumber);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        return isValid;
    }

    public static void showDatePicker(Context context, final IGetDateListener getDateListener) {
        Calendar mCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = new StringBuilder().append(year).append("-")
                        .append(StringUtil.getDoubleNumber(monthOfYear + 1)).append("-")
                        .append(StringUtil.getDoubleNumber(dayOfMonth)).toString();
                getDateListener.getDate(date);
            }

        };
        DatePickerDialog datePicker = new DatePickerDialog(context,
                callBack, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DATE));
        datePicker.show();
    }

    public static void showTimePicker(Context context, final IGetTimeListener getDateListener) {
        Calendar mCalendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener callBack = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = "";
                time = new StringBuilder().append(StringUtil.getDoubleNumber(hourOfDay)).append(":")
                        .append(StringUtil.getDoubleNumber(minute)).toString();
                getDateListener.getTime(time);
            }
        };
        TimePickerDialog timePicker = new TimePickerDialog(context,
                callBack, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE),
                true);
        timePicker.show();
    }

    public static void getCurrentLocation(Activity activity, LocationManager locationManager) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                GlobalFuntion.LATITUDE = location.getLatitude();
                GlobalFuntion.LONGITUDE = location.getLongitude();
                Log.e("Latitude current", GlobalFuntion.LATITUDE + "");
                Log.e("Longitude current", GlobalFuntion.LONGITUDE + "");
            } else {
                Toast.makeText(activity, activity.getString(R.string.unble_trace_location), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void showDialogNoGPS(Activity activity) {
        MaterialDialog materialDialog = new MaterialDialog.Builder(activity)
                .title(activity.getString(R.string.app_name))
                .content(activity.getString(R.string.message_turn_on_gps))
                .positiveText(activity.getString(R.string.action_ok))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .negativeText(activity.getString(R.string.action_cancel))
                .cancelable(false)
                .show();
    }

    public static void pickImage(Activity activity, int pickImage) {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(activity);
        activity.startActivityForResult(chooseImageIntent, pickImage);
    }

    public static void showDialogLogout(Activity activity) {
        MaterialDialog materialDialog = new MaterialDialog.Builder(activity)
                .title(activity.getString(R.string.app_name))
                .content(activity.getString(R.string.msg_confirm_login_another_device))
                .positiveText(activity.getString(R.string.action_ok))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        DataStoreManager.setIsLogin(false);
                        DataStoreManager.setUserToken("");
                        DataStoreManager.removeUser();
                        GlobalFuntion.startActivity(activity, SignInActivity.class);
                        activity.finishAffinity();
                    }
                })
                .cancelable(false)
                .show();
    }
}
