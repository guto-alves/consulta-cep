package com.gutotech.consultacep.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.gutotech.consultacep.R;
import com.gutotech.consultacep.db.ZipCodeEntity;

public class ZipCodeDialog extends DialogFragment {

    private ZipCodeEntity zipCodeEntity;

    public ZipCodeDialog(ZipCodeEntity zipCodeEntity) {
        this.zipCodeEntity = zipCodeEntity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View zipCodeDialogView = requireActivity().getLayoutInflater().inflate(R.layout.dialog_zip_code, null);

        builder.setView(zipCodeDialogView);

        TextView zipCodeTextView = zipCodeDialogView.findViewById(R.id.zipCodeTextView);
        TextView addressTextView = zipCodeDialogView.findViewById(R.id.addressTextView);
        TextView districtTextView = zipCodeDialogView.findViewById(R.id.districtTextView);
        TextView cityTextView = zipCodeDialogView.findViewById(R.id.cityTextView);
        TextView stateTextView = zipCodeDialogView.findViewById(R.id.stateTextView);

        zipCodeTextView.setText(zipCodeEntity.zipCode);
        addressTextView.setText(zipCodeEntity.address);
        districtTextView.setText(zipCodeEntity.district);
        cityTextView.setText(zipCodeEntity.city);
        stateTextView.setText(zipCodeEntity.state);

        return builder.create();
    }
}
