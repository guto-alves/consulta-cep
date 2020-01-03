package com.gutotech.consultacep;

import com.gutotech.consultacep.model.ZipCode;

import java.util.List;

public interface Contract {

    interface View {
        void showProgressBar();

        void hideProgressBar();

        void hideProgressBarAndGrid();

        void displayZipCodeInfo(ZipCode zipCode);

        void setError(String message);

        void updateAdapter();
    }

    interface Presenter {
        void searchZipCode(String zipCode);

        void showProgressBar();

        void hideProgressBar();

        void hideProgressBarAndGrid();

        void displayZipCodeInfo(ZipCode zipCode);

        void setError(String message);

        List<ZipCode> getZipCodeList();

        void getAllZipCodes();

        void deleteZipCode(int position);

        void insertZipCode(ZipCode zipCode);

        void close();
    }

}
