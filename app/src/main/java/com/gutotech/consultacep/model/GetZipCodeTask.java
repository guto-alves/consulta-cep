package com.gutotech.consultacep.model;

import android.os.AsyncTask;

import com.gutotech.consultacep.Contract;
import com.gutotech.consultacep.db.ZipCodeEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class GetZipCodeTask extends AsyncTask<URL, Void, String> {

    public interface GetZipCodeListener {
        void onZipCodeReceived(ZipCodeEntity zipCodeEntity);
    }

    private GetZipCodeListener mListener;
    private Contract.Presenter mPresenter;

    public GetZipCodeTask(GetZipCodeListener listener, Contract.Presenter presenter) {
        mListener = listener;
        mPresenter = presenter;
    }

    @Override
    protected void onPreExecute() {
        mPresenter.showProgressBar();
    }

    @Override
    protected String doInBackground(URL... urls) {
        URL url = urls[0];

        HttpURLConnection connection;

        try {
            connection = (HttpURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder builder = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null)
                builder.append(line);

            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            ZipCodeEntity zipCodeEntity = convertJSONtoZipCode(result);
            mListener.onZipCodeReceived(zipCodeEntity);
        } else
            mListener.onZipCodeReceived(null);
    }

    private ZipCodeEntity convertJSONtoZipCode(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            return new ZipCodeEntity(
                    jsonObject.getString("cep"),
                    jsonObject.getString("address"),
                    jsonObject.getString("district"),
                    jsonObject.getString("city"),
                    jsonObject.getString("state"),
                    jsonObject.getString("lat"),
                    jsonObject.getString("lng"),
                    new Date().getTime()
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
