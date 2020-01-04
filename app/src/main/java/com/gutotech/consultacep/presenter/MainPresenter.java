package com.gutotech.consultacep.presenter;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import com.gutotech.consultacep.Contract;
import com.gutotech.consultacep.R;
import com.gutotech.consultacep.db.AppDatabase;
import com.gutotech.consultacep.model.GetZipCodeTask;
import com.gutotech.consultacep.db.ZipCodeDao;
import com.gutotech.consultacep.db.ZipCodeEntity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainPresenter implements Contract.Presenter {
    private Contract.View mView;

    private AppDatabase database;
    private ZipCodeDao zipCodeDAO;

    private List<ZipCodeEntity> zipCodeEntityList;

    public MainPresenter(Contract.View view) {
        mView = view;

        database = AppDatabase.getInstance((Context) mView);
        zipCodeDAO = database.getZipCodeDAO();

        zipCodeEntityList = new ArrayList<>();
    }

    @Override
    public void searchZipCode(String zipCode) {
        if (isFormatValidZipCode(zipCode)) {
            URL url = createURL(zipCode);

            if (url != null)
                new GetZipCodeTask(getZipCodeListener, this).execute(url);
        } else
            mView.setError(R.string.invalid_zip_code);
    }

    private boolean isFormatValidZipCode(String zipCode) {
        return zipCode.matches("\\d{8}");
    }

    private URL createURL(String query) {
        try {
            return new URL(((Activity) mView).getString(R.string.baseUrl) + query);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private final GetZipCodeTask.GetZipCodeListener getZipCodeListener = new GetZipCodeTask.GetZipCodeListener() {
        @Override
        public void onZipCodeReceived(ZipCodeEntity zipCodeEntity) {
            if (zipCodeEntity != null) {
                mView.displayZipCodeInfo(zipCodeEntity);
                insertZipCode(zipCodeEntity);
                mView.hideProgressBar();
            } else {
                mView.hideProgressBarAndGrid();
                mView.setError(R.string.zip_code_not_found);
            }
        }
    };

    @Override
    public List<ZipCodeEntity> getZipCodeEntityList() {
        return zipCodeEntityList;
    }

    @Override
    public void getAllZipCodes() {
        zipCodeEntityList.clear();
        zipCodeEntityList.addAll(zipCodeDAO.getAll());
        mView.updateList();
    }

    @Override
    public void deleteZipCode(int position) {
        zipCodeDAO.delete(getZipCodeEntityList().get(position));
        getZipCodeEntityList().remove(position);
        mView.updateList();
    }

    @Override
    public void insertZipCode(ZipCodeEntity zipCodeEntity) {
        try {
            zipCodeDAO.insert(zipCodeEntity);
        } catch (SQLiteConstraintException e) { // if exception occurred Postal Code already in List and in database
            zipCodeEntity.dateSearched = new Date().getTime();
            zipCodeDAO.updateDateSearched(zipCodeEntity);

            removeZipCodeFromList(zipCodeEntity);
        }

        getZipCodeEntityList().add(0, zipCodeEntity);

        mView.updateList();
    }

    private void removeZipCodeFromList(ZipCodeEntity zipCodeEntity) {
        for (ZipCodeEntity currentZipCodeEntity : getZipCodeEntityList()) {
            if (currentZipCodeEntity.zipCode.equals(zipCodeEntity.zipCode)) {
                getZipCodeEntityList().remove(currentZipCodeEntity);
                break;
            }
        }
    }

    @Override
    public void showProgressBar() {
        mView.showProgressBar();
    }

    @Override
    public void close() {
        database.close();
    }
}
