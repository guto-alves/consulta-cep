package com.gutotech.consultacep.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.gutotech.consultacep.Contract;
import com.gutotech.consultacep.R;
import com.gutotech.consultacep.db.ZipCodeEntity;
import com.gutotech.consultacep.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity implements Contract.View {
    private Contract.Presenter mPresenter;

    private ZipCodesAdapter zipCodesAdapter;

    private TextInputEditText zipCodeTextInputEditText;

    private ProgressBar progressBar;
    private GridLayout zipCodeGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainPresenter(this);

        progressBar = findViewById(R.id.progressBar);
        zipCodeGridLayout = findViewById(R.id.zipCodeGridLayout);
        zipCodeTextInputEditText = findViewById(R.id.zipCodeTextInputEditText);

        zipCodeTextInputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String zipCode = zipCodeTextInputEditText.getText().toString().trim();
                mPresenter.searchZipCode(zipCode);
                return true;
            }
        });

        FloatingActionButton searchFloatingActionButton = findViewById(R.id.searchFloatingActionButton);
        searchFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zipCode = zipCodeTextInputEditText.getText().toString().trim();
                mPresenter.searchZipCode(zipCode);
            }
        });

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        RecyclerView lastestSearchedRecyclerView = findViewById(R.id.lastestCepsRecyclerView);
        lastestSearchedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        lastestSearchedRecyclerView.setHasFixedSize(true);
        lastestSearchedRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        zipCodesAdapter = new ZipCodesAdapter(mPresenter.getZipCodeEntityList(), zipCodeClickListener);
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
                mPresenter.deleteZipCode(position);
                Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.zip_code_deleted), Toast.LENGTH_SHORT).show();
            }
        };

        new ItemTouchHelper(callback).attachToRecyclerView(lastestSearchedRecyclerView);
    }

    private final ZipCodesAdapter.ZipCodeClickListener zipCodeClickListener = new ZipCodesAdapter.ZipCodeClickListener() {
        @Override
        public void onClick(View view, ZipCodeEntity zipCodeEntity) {
            new ZipCodeDialog(zipCodeEntity).show(getSupportFragmentManager(), "dialog");
        }
    };

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        zipCodeGridLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        zipCodeGridLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBarAndGrid() {
        progressBar.setVisibility(View.GONE);
        zipCodeGridLayout.setVisibility(View.GONE);
    }

    @Override
    public void displayZipCodeInfo(ZipCodeEntity zipCodeEntity) {
        TextView addressTextView = findViewById(R.id.addressTextView);
        TextView districtTextView = findViewById(R.id.districtTextView);
        TextView cityTextView = findViewById(R.id.cityTextView);
        TextView stateTextView = findViewById(R.id.stateTextView);

        addressTextView.setText(zipCodeEntity.address);
        districtTextView.setText(zipCodeEntity.district);
        cityTextView.setText(zipCodeEntity.city);
        stateTextView.setText(zipCodeEntity.state);
    }

    @Override
    public void setError(int resId) {
        zipCodeTextInputEditText.setError(getString(resId));
    }

    @Override
    public void updateList() {
        zipCodesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.getAllZipCodes();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.close();
    }
}
