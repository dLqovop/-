package com.example.jeju_makcha;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class BusAdapter extends RecyclerView.Adapter<BusAdapter.ViewHolder> {

    private List<String> busDataList;
    private OnItemClickListener onItemClickListener;

    private List<Integer> expandedItems;

    public BusAdapter(List<String> busDataList) {
        this.busDataList = busDataList;
        this.expandedItems = new ArrayList<>();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public List<String> getItemList() {
        return busDataList;
    }

    public void setItemList(List<String> busDAtaList) {
        this.busDataList = busDAtaList;
        notifyDataSetChanged();
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

        private TextView textViewItem;

        public ViewHolder(View itemView) {
            super(itemView);
            //textViewItem = itemView.findViewById(R.id.line1TextView);
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


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(v, adapterPosition);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (busDataList == null) {
            return 0; // 데이터가 없는 경우에는 항목 개수를 0으로 반환
        }
        return busDataList.size();
    }




}
