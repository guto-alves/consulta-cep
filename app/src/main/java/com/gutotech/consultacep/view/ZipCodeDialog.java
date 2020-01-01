package com.gutotech.consultacep.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gutotech.consultacep.R;
import com.gutotech.consultacep.model.ZipCode;


public class ZipCodeDialog extends Dialog {

    public ZipCodeDialog(@NonNull Context context, ZipCode zipCode) {
        super(context);
        setContentView(R.layout.dialog_zip_code);

        TextView addressTextView = findViewById(R.id.addressTextView);
        TextView districtTextView = findViewById(R.id.districtTextView);
        TextView cityTextView = findViewById(R.id.cityTextView);
        TextView stateTextView = findViewById(R.id.stateTextView);

        addressTextView.setText(zipCode.address);
        districtTextView.setText(zipCode.district);
        cityTextView.setText(zipCode.city);
        stateTextView.setText(zipCode.state);
    }
}
