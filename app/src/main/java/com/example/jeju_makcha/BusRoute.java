package com.example.jeju_makcha;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Tag;

public class BusRoute {
    private static final String API_BASE_URL = "https://api.odcloud.kr/";
    private static final String API_PATH = "api/15051474/v1/uddi:71b96e42-f8da-4d7a-b873-c1d18379f296";
    private static final String API_KEY = "VoAWbmkmpaYzLZXXUNzch9EDIn1RNW0EUI77bFfo7kLkIl9tro8z5dONAYcD4GzbU1oo4OR%2BKzzIs39a0VKqcA%3D%3D";
    private static final String TAG = "BusRoute";

    private String busRouteNumber; // 버스 번호를 저장할 변수
    private Context context; // Context 멤버 변수 추가
    private BusData busData;
    List<String> busDataList = new ArrayList<>();

    public BusRoute(Context context) {
        this.context = context;
        // ...
    }

    private OnApiResponseListener listener;
    public BusRoute(Context context, OnApiResponseListener listener) {
        this.context = context;
        this.listener = listener;
    }
    public void setOnApiResponseListener(OnApiResponseListener listener) {
        this.listener = listener;
        busData = new BusData(); // busData 객체 초기화
        String routeNumber = busData.getRouteNumber();
        // routeNumber 변수를 listener에 전달하거나 다른 로직에서 사용할 수 있습니다.
    }



    public void setBusRouteNumber(String busRouteNumber, Context context) {
        this.busRouteNumber = busRouteNumber;
        this.context = context;
        BusAdapter busAdapter = new BusAdapter(busDataList, "", context);
        Log.d(TAG, "로그 1 : " + busRouteNumber);


        int cacheSize = 10 * 1024 * 1024; // 10MB
        Cache cache = new Cache(context.getCacheDir(), cacheSize);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        // Add the Authorization header to the new request
                        Request newRequest = originalRequest.newBuilder()
                                .header("Authorization", "Infuser " + "VoAWbmkmpaYzLZXXUNzch9EDIn1RNW0EUI77bFfo7kLkIl9tro8z5dONAYcD4GzbU1oo4OR+KzzIs39a0VKqcA==")
                                .build();

                        // Proceed with the new request and return the response
                        return chain.proceed(newRequest);
                    }
                })
                .build();

        // API 요청 호출
        String apiUrl = String.format("%s%s?page=1&perPage=70&serviceKey=%s", API_BASE_URL, API_PATH, API_KEY, busRouteNumber);
        Log.i(TAG, "API URL: " + apiUrl);

        // API 호출 및 응답 처리
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BusService busService = retrofit.create(BusService.class);
        Call<BusResponse> call = busService.getBusInfo(1, 70, API_KEY, busRouteNumber); //총 68개라 70페이지해야 전체 노선 불러올 수 있음.
        call.enqueue(new Callback<BusResponse>() {
            @Override
            public void onResponse(Call<BusResponse> call, Response<BusResponse> response) {
                if (response.isSuccessful()) {
                    BusResponse result = response.body();
                    String routeInfo = null;
                    if (result != null && result.getData() != null && !result.getData().isEmpty()) {
                        for (BusData data : result.getData()) {
                            if (data != null && data.getRouteNumber().equals(busRouteNumber)) {
                                routeInfo = data.get노선정보();
                                Log.d(TAG,"api 버스번호/버스 노선 in BusRoute : "+busRouteNumber+" "+routeInfo);
                                busAdapter.setRouteInfo(routeInfo);
                                if (routeInfo != null) {
                                    listener.onApiResponse(routeInfo);
                                }
                                break;
                            }
                        }
                    }

                } else {
                    // Handle unsuccessful response
                    Log.e(TAG, "Tldldldldlddldlqkffusdl ro enlwlffkrh Tlqkf wlsWk");
                }
            }

            @Override
            public void onFailure(Call<BusResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }

    // BusResponse 모델 클래스
    public class BusResponse {
        private int currentCount;
        private List<BusData> data;
        private int matchCount;
        private int page;
        private int perPage;
        private int totalCount;

        // getter 메서드들...

        public List<BusData> getData() {
            return data;
        }
    }
    public interface OnApiResponseListener {
        void onApiResponse(String routeInfo);
    }

    // BusData 모델 클래스
    public class BusData {
        private String 구분;
        private int 노선번호;
        private String 노선정보;
        private String 데이터기준일자;
        private String 비고;

        @Override
        public String toString() {
            return "BusData{" +
                    "구분='" + 구분 + '\'' +
                    ", 노선번호=" + 노선번호 +
                    ", 노선정보='" + 노선정보 + '\'' +
                    ", 데이터기준일자='" + 데이터기준일자 + '\'' +
                    ", 비고='" + 비고 + '\'' +
                    '}';
        }

        public String get노선정보() {
            return 노선정보;
        }

        public String getRouteNumber() {
            return String.valueOf(노선번호);
        }
    }

    // BusService 인터페이스
    public interface BusService {
        @GET("api/15051474/v1/uddi:71b96e42-f8da-4d7a-b873-c1d18379f296")
        Call<BusResponse> getBusInfo(
                @Query("page") int page,
                @Query("perPage") int perPage,
                @Query("serviceKey") String serviceKey,
                @Query("routeNo") String routeNo // routeNo 파라미터 추가
        );
    }
}