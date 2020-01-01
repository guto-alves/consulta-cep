package com.gutotech.consultacep.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.gutotech.consultacep.R;
import com.gutotech.consultacep.data.ZipCodeDAO;
import com.gutotech.consultacep.model.ZipCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText zipCodeTextInputEditText;

    private ZipCodeDAO zipCodeDAO;

    private ZipCodesAdapter zipCodesAdapter;

    private List<ZipCode> zipCodeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zipCodeDAO = new ZipCodeDAO(this);

        zipCodeTextInputEditText = findViewById(R.id.zipCodeTextInputEditText);
        zipCodeTextInputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchZipCode();
                return true;
            }
        });

        FloatingActionButton searchFloatingActionButton = findViewById(R.id.searchFloatingActionButton);
        searchFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchZipCode();
            }
        });

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        RecyclerView lastestSearchedRecyclerView = findViewById(R.id.lastestCepsRecyclerView);
        lastestSearchedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        lastestSearchedRecyclerView.setHasFixedSize(true);
        lastestSearchedRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        zipCodesAdapter = new ZipCodesAdapter(zipCodeList, zipCodeClickListener);
        lastestSearchedRecyclerView.setAdapter(zipCodesAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int position = viewHolder.getAdapterPosition();
                zipCodeDAO.delete(zipCodeList.get(position).zipCode);
                zipCodeList.remove(position);
                zipCodesAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.zip_code_deleted), Toast.LENGTH_SHORT).show();
            }
        };

        new ItemTouchHelper(callback).attachToRecyclerView(lastestSearchedRecyclerView);
    }

    private void searchZipCode() {
        String zipCode = zipCodeTextInputEditText.getText().toString().trim();

        if (isFormatValidZipCode(zipCode)) {
            URL url = createURL(zipCode);

            if (url != null)
                new GetZipCodeTask().execute(url);
        } else
            zipCodeTextInputEditText.setError("CEP deve conter 8 digitos");
    }

    private boolean isFormatValidZipCode(String zipCode) {
        return zipCode.matches("\\d{8}");
    }

    private URL createURL(String query) {
        try {
            return new URL(getString(R.string.baseUrl) + query);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class GetZipCodeTask extends AsyncTask<URL, Void, String> {
        private ProgressBar progressBar;
        private GridLayout zipCodeGridLayout;

        @Override
        protected void onPreExecute() {
            progressBar = findViewById(R.id.progressBar);
            zipCodeGridLayout = findViewById(R.id.zipCodeGridLayout);

            progressBar.setVisibility(View.VISIBLE);
            zipCodeGridLayout.setVisibility(View.GONE);
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
                displayZipCodeInfo(zipCode);

                if (zipCodeDAO.insert(zipCode) != -1) {
                    zipCodeList.add(0, zipCode);
                    zipCodesAdapter.notifyDataSetChanged();
                }

                progressBar.setVisibility(View.GONE);
                zipCodeGridLayout.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);

                TextInputEditText zipCodeTextInputEditText = findViewById(R.id.zipCodeTextInputEditText);
                zipCodeTextInputEditText.setError(getString(R.string.zip_code_not_found));
            }
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

    private void displayZipCodeInfo(ZipCode zipCode) {
        TextView addressTextView = findViewById(R.id.addressTextView);
        TextView districtTextView = findViewById(R.id.districtTextView);
        TextView cityTextView = findViewById(R.id.cityTextView);
        TextView stateTextView = findViewById(R.id.stateTextView);

        addressTextView.setText(zipCode.address);
        districtTextView.setText(zipCode.district);
        cityTextView.setText(zipCode.city);
        stateTextView.setText(zipCode.state);
    }

    private final ZipCodesAdapter.ZipCodeClickListener zipCodeClickListener = new ZipCodesAdapter.ZipCodeClickListener() {
        @Override
        public void onClick(View view, ZipCode zipCode) {
            new ZipCodeDialog(MainActivity.this, zipCode).show();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        zipCodeDAO.queryAll(zipCodeList);
        zipCodesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        zipCodeDAO.close();
    }
}
