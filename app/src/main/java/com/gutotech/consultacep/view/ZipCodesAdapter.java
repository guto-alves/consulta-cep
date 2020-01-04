package com.gutotech.consultacep.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gutotech.consultacep.db.ZipCodeEntity;

import java.util.List;

public class ZipCodesAdapter extends RecyclerView.Adapter<ZipCodesAdapter.ViewHolder> {

    public interface ZipCodeClickListener {
        void onClick(View view, ZipCodeEntity zipCodeEntity);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;
        public int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(v, zipCodeEntityList.get(position));
                }
            });

            textView = itemView.findViewById(android.R.id.text1);
        }
    }

    private List<ZipCodeEntity> zipCodeEntityList;
    private ZipCodeClickListener clickListener;

    public ZipCodesAdapter(List<ZipCodeEntity> zipCodeEntityList, ZipCodeClickListener clickListener) {
        this.zipCodeEntityList = zipCodeEntityList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ZipCodeEntity zipCodeEntity = zipCodeEntityList.get(position);

        holder.position = position;
        holder.textView.setText(String.format("%s, %s", zipCodeEntity.address, zipCodeEntity.state));
    }

    @Override
    public int getItemCount() {
        return zipCodeEntityList.size();
    }
}
