package com.example.jeju_makcha;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class BusAdapter extends RecyclerView.Adapter<BusAdapter.ViewHolder> {

    private List<String> busDataList;

    public BusAdapter(List<String> busDataList) {
        this.busDataList = busDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 카드 뷰를 생성합니다.
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus, parent, false);

        // 각 줄에 대한 다른 스타일을 적용한 ViewHolder를 생성합니다.
        ViewHolder viewHolder = new ViewHolder(cardView);
        viewHolder.line1TextView = cardView.findViewById(R.id.line1TextView);
        viewHolder.line2TextView = cardView.findViewById(R.id.line2TextView);
        viewHolder.line3TextView = cardView.findViewById(R.id.line3TextView);

        return viewHolder;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView line1TextView;
        public TextView line2TextView;
        public TextView line3TextView;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = busDataList.get(position);

        ViewHolder viewHolder = (ViewHolder) holder;
        String[] lines = item.split("\n");
        viewHolder.line1TextView.setText(lines[0]);
        viewHolder.line2TextView.setText(lines[1]);
        viewHolder.line3TextView.setText(lines[2]);
    }

    @Override
    public int getItemCount() {
        return busDataList.size();
    }



}
