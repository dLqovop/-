package com.example.jeju_makcha;



import android.util.Log;
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


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    if (expandedItems.contains(position)) {
                        // 아이템이 확장되어 있는 경우, 축소합니다.
                        expandedItems.remove(Integer.valueOf(position));
                    } else {
                        // 아이템이 축소되어 있는 경우, 확장합니다.
                        expandedItems.add(position);
                    }
                    notifyItemChanged(position);
                }
            }
        });

        return viewHolder;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView line1TextView;
        public TextView line2TextView;
        public TextView line3TextView;
        public View expandedView; // 확장될 추가 뷰

        public ViewHolder(View itemView) {
            super(itemView);
            line1TextView = itemView.findViewById(R.id.line1TextView);
            line2TextView = itemView.findViewById(R.id.line2TextView);
            line3TextView = itemView.findViewById(R.id.line3TextView);
            expandedView = itemView.findViewById(R.id.expandedView); // 레이아웃에서 확장될 뷰를 할당합니다.
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = busDataList.get(position);
        String[] lines = item.split("\n");

        holder.line1TextView.setText(lines[0]);
        holder.line2TextView.setText(lines[1]);
        holder.line3TextView.setText(lines[2]);

        if (expandedItems.contains(position)) {
            holder.expandedView.setVisibility(View.VISIBLE);
        } else {
            holder.expandedView.setVisibility(View.GONE);
        }

//클릭하면 DB 추가했던 코드
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    int adapterPosition = holder.getAdapterPosition();

                    //확장
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        if (expandedItems.contains(adapterPosition)) {
                            // 아이템이 확장되어 있는 경우, 축소합니다.
                            expandedItems.remove(Integer.valueOf(adapterPosition));
                        } else {
                            // 아이템이 축소되어 있는 경우, 확장합니다.
                            expandedItems.add(adapterPosition);
                        }
                        notifyItemChanged(adapterPosition);
                    }
                    //
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
