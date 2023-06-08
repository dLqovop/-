package com.example.jeju_makcha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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
  /*
    public void setBusAdapter(BusAdapter adapter) {
        busAdapter = adapter;
    }
*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        dbHelper = new DBHelper(this);

        //프래그먼트 생성
        fragment_frag1 = new fragment_1();
        fragment_frag2 = new fragment_2();
        fragment_frag3 = new fragment_3();

        //바텀네비게이션
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

//        fragment_frag1.setBusAdapter(busAdapter);

        //리스너 등록
        bottomNavigationView.setOnItemSelectedListener(item -> {
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
        dbHelper = new DBHelper(this); // DBHelper 인스턴스 생성

        // DB에 저장된 값 로그로 출력
        List<String> favorites = dbHelper.getAllFavorites();
        for (String item : favorites) {
            Log.i("DBHelper", "Favorite MainActivity: " + item);
        }
    }
}