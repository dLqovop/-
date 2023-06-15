package com.example.jeju_makcha;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class BusAdapter extends RecyclerView.Adapter<BusAdapter.ViewHolder> {
    private String TAG = "BusAdapter";
    private OnItemClickListener onItemClickListener;

    TextView expandedView;
    private DBHelper dbHelper;


    private List<String> busDataList;
    private List<Integer> expandedItems;
    private Context context;
    private String routeInfo; // routeInfo 변수 추가

    public BusAdapter(List<String> busDataList, String routeInfo, Context context) {
        this.busDataList = busDataList;
        this.expandedItems = new ArrayList<>();
        this.routeInfo = routeInfo;
        this.context = context;

    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public List<String> getItemList() {
        return busDataList;
    }

    public void setItemList(List<String> busDataList) {
        this.busDataList = busDataList;
        notifyDataSetChanged();
    }

    public void setRouteInfo(String routeInfo) {
        this.routeInfo = routeInfo;
        notifyDataSetChanged(); // 데이터 변경 시 어댑터에 알려줍니다.
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 카드 뷰를 생성합니다.
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus, parent, false);
        // 각 줄에 대한 다른 스타일을 적용한 ViewHolder를 생성합니다.
        ViewHolder holder = new ViewHolder(cardView);
        return holder;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView line1TextView;
        public TextView line2TextView;
        public TextView line3TextView;
        public TextView expandedView; // 확장될 추가 뷰
        public ImageButton favoritesButton;

        public ViewHolder(View itemView) {
            super(itemView);
            line1TextView = itemView.findViewById(R.id.line1TextView);
            line2TextView = itemView.findViewById(R.id.line2TextView);
            line3TextView = itemView.findViewById(R.id.line3TextView);
            expandedView = itemView.findViewById(R.id.expandedView); // 레이아웃에서 확장될 뷰를 할당합니다.
            favoritesButton = itemView.findViewById(R.id.favoritesButton);
        }
        String routeInfo;
        public void setRouteInfo(String routeInfo) {
            this.routeInfo = routeInfo;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//
        String item = busDataList.get(position);
        String[] lines = item.split("\n");
//        Log.d("tlqkf", item);

        holder.line1TextView.setText(lines[0]);
        holder.line2TextView.setText(lines[1]);
        holder.line3TextView.setText(lines[2]);

        boolean isExpanded = expandedItems.contains(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Log.d(TAG,"api ㄷ테스트");
                    String selectedItem = getItemList().get(adapterPosition);
                    String[] tokens = selectedItem.split("\n");
                    String busNumber = tokens[0].trim().replaceAll("번", "");
//                    Log.d(TAG, "api 버스넘버 " + busNumber);
                    BusRoute busRoute = new BusRoute(v.getContext(), new BusRoute.OnApiResponseListener() {
                        @Override
                        public void onApiResponse(String routeInfo) {
//                            Log.d(TAG, "들어옴?");
                            setRouteInfo(routeInfo); // 노선 정보 업데이트
                            if (expandedItems.contains(adapterPosition)) {
                                holder.expandedView.setText(routeInfo); // 변경된 routeInfo로 텍스트 설정
                                holder.expandedView.setVisibility(View.VISIBLE);
                            }
                            notifyItemChanged(adapterPosition);
//                            Log.d(TAG, "갱신함?");
                        }
                    });
                    busRoute.setBusRouteNumber(busNumber, v.getContext());
//                    Log.d(TAG, "api아님 버스 번호" + busNumber);
                }
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    if (isExpanded) {
                        expandedItems.remove(Integer.valueOf(adapterPosition));
                    } else {
                        expandedItems.add(adapterPosition);
                    }
                    notifyItemChanged(adapterPosition);
                }
            }
        });

        if (isExpanded) {
            holder.expandedView.setText(routeInfo); // 변경된 routeInfo로 텍스트 설정
            holder.expandedView.setVisibility(View.VISIBLE);
        } else {
            holder.expandedView.setVisibility(View.GONE);
        }

        holder.favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    String selectedItem = getItemList().get(adapterPosition);
                    // 즐겨찾기에 아이템 추가
                    addFavorite(selectedItem);
                }
            }
        });

    }
    private void addFavorite(String item) {
        boolean isClicked = false; // 중복 호출 방지를 위한 변수
        DBHelper dbHelper = new DBHelper(context); // 적절한 context를 사용하여 DBHelper 생성

        if (!isClicked) { // 중복 호출이 아닌 경우에만 처리
            isClicked = true; // 중복 호출 방지

            String[] tokens = item.split("\n");
            String itemToSave = tokens[0] + "\n" + tokens[1];

            // 이미 즐겨찾기에 추가되어 있는지 확인
            boolean isFavorite = dbHelper.isFavorite(itemToSave);

            if (isFavorite) {
                // 이미 즐겨찾기에 추가된 경우, 아이템을 삭제
                dbHelper.deleteFavorite(itemToSave);
                Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();
            } else {
                // 아직 즐겨찾기에 추가되지 않은 경우, 아이템을 추가
                dbHelper.insertFavorite(itemToSave);
                Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Already added to favorites", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        if (busDataList == null) {
            return 0; // busDataList가 null인 경우 0을 반환
        }
        return busDataList.size();
    }




}
