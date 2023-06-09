package com.example.jeju_makcha;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;

import android.util.Log;


import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.List;

public class MainActivity extends AppCompatActivity {  // implements RecyclerViewAdapterCallback

    //바텀 네비게이션
    BottomNavigationView bottomNavigationView;
    private final String TAG = "메인페이지";

    //프래그먼트 변수
    fragment_1 fragment_frag1;
    Fragment fragment_frag2;
    Fragment fragment_frag3;

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
            Log.i(TAG, "바텀네비게이션 클릭");
            int itemId = item.getItemId();//item을 클릭시 id값을 가져와 FrameLayout에 fragment.xml띄우기
            if (itemId == R.id.page_1) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment_frag1).commitAllowingStateLoss();
                return true;
            } else if (itemId == R.id.page_2) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment_frag2).commitAllowingStateLoss();
                return true;
            } else if (itemId == R.id.page_3) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment_frag3).commitAllowingStateLoss();
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