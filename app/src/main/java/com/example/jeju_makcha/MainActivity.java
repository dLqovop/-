package com.example.jeju_makcha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {  // implements RecyclerViewAdapterCallback

    //바텀 네비게이션
    BottomNavigationView bottomNavigationView;
    private String TAG = "메인페이지";

    //프래그먼트 변수
    fragment_1 fragment_frag1;
    Fragment fragment_frag2;
    Fragment fragment_frag3;

    private DBHelper dbHelper;
    private BusAdapter busAdapter;

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //프래그먼트 생성
        fragment_frag1 = new fragment_1();
        fragment_frag2 = new fragment_2();
        fragment_frag3 = new fragment_3();


        //바텀네비게이션
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

//        // RecyclerView 초기화
//        recyclerView = findViewById(R.id.main_recycler);
//        // 어댑터 생성
//        List<String> itemList = new ArrayList<>();
//        BusAdapter busAdapter = new BusAdapter(itemList);
//
//        // RecyclerView에 어댑터 설정
//        recyclerView.setAdapter(busAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        DBHelper dbHelper = new DBHelper(this);
//        List<String> favorites = dbHelper.getAllFavorites();

// 어댑터에 DB 내용 설정
//        busAdapter.setItemList(favorites);

// 어댑터 갱신
//        busAdapter.notifyDataSetChanged();

        //리스너 등록
        bottomNavigationView.setOnItemSelectedListener(item -> {
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment_frag1)
                    .commit();
                Log.i(TAG,"바텀네비게이션 클릭");
                int itemId = item.getItemId();//item을 클릭시 id값을 가져와 FrameLayout에 fragment.xml띄우기
                if (itemId == R.id.page_1) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,fragment_frag1).commitAllowingStateLoss();

                    return true;
                }
                else if (itemId == R.id.page_2) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,fragment_frag2).commitAllowingStateLoss();
                    return true;
                }
                else if (itemId == R.id.page_3) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,fragment_frag3).commitAllowingStateLoss();
                    return true;
                }
                return true;
            });

        // Frag1을 초기화면으로 설정
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, new fragment_1());
        fragmentTransaction.commit();

    }
}