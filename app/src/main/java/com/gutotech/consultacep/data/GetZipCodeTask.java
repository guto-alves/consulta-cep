package com.gutotech.consultacep.data;

import android.os.AsyncTask;

import com.gutotech.consultacep.Contract;
import com.gutotech.consultacep.model.ZipCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetZipCodeTask extends AsyncTask<URL, Void, String> {
    private Contract.Presenter mPresenter;

    public GetZipCodeTask(Contract.Presenter presenter) {
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
            ZipCode zipCode = convertJSONtoZipCode(result);
            mPresenter.displayZipCodeInfo(zipCode);
            mPresenter.insertZipCode(zipCode);
            mPresenter.hideProgressBar();
        } else {
            mPresenter.hideProgressBarAndGrid();
            mPresenter.setError("CEP não encontrado");
        }
    }

    private ZipCode convertJSONtoZipCode(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            return new ZipCode(
                    jsonObject.getString("cep"),
                    jsonObject.getString("address"),
                    jsonObject.getString("district"),
                    jsonObject.getString("city"),
                    jsonObject.getString("state"),
                    jsonObject.getString("lat"),
                    jsonObject.getString("lng")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
