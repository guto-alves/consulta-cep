package com.gutotech.consultacep;

import androidx.annotation.StringRes;

import com.gutotech.consultacep.db.ZipCodeEntity;

import java.util.List;

public interface Contract {

    interface View {
        void showProgressBar();

        void hideProgressBar();

        void hideProgressBarAndGrid();

        void displayZipCodeInfo(ZipCodeEntity zipCodeEntity);

        void setError(@StringRes int resId);

        void updateList();
    }

    interface Presenter {
        void searchZipCode(String zipCode);

        void showProgressBar();

        List<ZipCodeEntity> getZipCodeEntityList();

        void getAllZipCodes();

        void deleteZipCode(int position);

        void insertZipCode(ZipCodeEntity zipCodeEntity);

        void close();
    }

}
