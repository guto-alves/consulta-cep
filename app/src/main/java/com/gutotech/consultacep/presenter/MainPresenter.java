package com.gutotech.consultacep.presenter;

import android.content.Context;

import com.gutotech.consultacep.Contract;
import com.gutotech.consultacep.data.GetZipCodeTask;
import com.gutotech.consultacep.data.ZipCodeDAO;
import com.gutotech.consultacep.model.ZipCode;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainPresenter implements Contract.Presenter {
    private Contract.View mView;

    private ZipCodeDAO zipCodeDAO;

    private List<ZipCode> zipCodeList;

    public MainPresenter(Contract.View view) {
        mView = view;

        zipCodeDAO = new ZipCodeDAO((Context) view);
        zipCodeList = new ArrayList<>();
    }

    @Override
    public List<ZipCode> getZipCodeList() {
        return zipCodeList;
    }

    @Override
    public void getAllZipCodes() {
        zipCodeDAO.queryAll(zipCodeList);
        mView.updateAdapter();
    }

    @Override
    public void deleteZipCode(int position) {
        zipCodeDAO.delete(getZipCodeList().get(position).zipCode);
        getZipCodeList().remove(position);
        mView.updateAdapter();
    }

    @Override
    public void insertZipCode(ZipCode zipCode) {
        if (zipCodeDAO.insert(zipCode) != -1) {
            getZipCodeList().add(0, zipCode);
            mView.updateAdapter();
        }
    }

    @Override
    public void searchZipCode(String zipCode) {
        if (isFormatValidZipCode(zipCode)) {
            URL url = createURL(zipCode);

            if (url != null)
                new GetZipCodeTask(this).execute(url);
        } else
            mView.setError("CEP deve conter 8 digitos");
    }

    private boolean isFormatValidZipCode(String zipCode) {
        return zipCode.matches("\\d{8}");
    }

    private URL createURL(String query) {
        try {
            return new URL("https://cep.awesomeapi.com.br/json/" + query);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void showProgressBar() {
        mView.showProgressBar();
    }

    @Override
    public void hideProgressBar() {
        mView.hideProgressBar();
    }

    @Override
    public void hideProgressBarAndGrid() {
        mView.hideProgressBarAndGrid();
    }

    @Override
    public void displayZipCodeInfo(ZipCode zipCode) {
        mView.displayZipCodeInfo(zipCode);
    }

    @Override
    public void setError(String message) {
        mView.setError(message);
    }

    @Override
    public void close() {
        zipCodeDAO.close();
    }
}
