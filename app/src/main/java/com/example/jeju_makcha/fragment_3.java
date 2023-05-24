package com.example.jeju_makcha;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class fragment_3 extends Fragment {
    private View view;
    private String TAG = "페이지 3";

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG,"페이지3 OnCreateView");
        view = inflater.inflate(R.layout.fragment_frag3, container, false);
        return view;
    }
}
