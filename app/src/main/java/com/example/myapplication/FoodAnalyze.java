package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FoodAnalyze extends AppCompatActivity {

    private TextView displayDataTextView;
    private SharedPreferences sharedPreferences;
    private static final String PREFERENCES_NAME = "FoodPreferences";

    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.food_analyze_layout);
        Button button = findViewById(R.id.month);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDynamicPopupMenu(view);
            }
        });
        List<FoodData> dataList=getAllFoodDataFromPreferences();
        int totalCost=0;
        int totalCal=0;
        int brekfastCal=0;
        int lunchCal=0;
        int dinnerCal=0;
        int drinkCal=0;
        for(FoodData entry:dataList){
            String Type=entry.getSelectedType();
            totalCost+=entry.getCost();
            // 조식, 중식, 석식, 음료에 해당하는 칼로리 업데이트
            if (Type.equals("조식")) {
                brekfastCal+=entry.getCalories();
            } else if (Type.equals("중식")) {
                lunchCal+=entry.getCalories();
            } else if (Type.equals("석식")) {
                dinnerCal+=entry.getCalories();
            } else if (Type.equals("음료")) {
                drinkCal+=entry.getCalories();
            }
            totalCal+=entry.getCalories();
        }
        Log.e("cal : ",totalCal+" 조식 "+brekfastCal);
    }
    public List<FoodData> getAllFoodDataFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();

        List<FoodData> foodDataList = new ArrayList<>();
        List<FoodData> foodDatabefore30 = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        long currentTimeInMillis = calendar.getTimeInMillis();
        // 현재 시간 가져오기
        Calendar currentTime = Calendar.getInstance();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String foodDataJson = entry.getValue().toString();
            if (!entry.getKey().startsWith("data_set_")) {
                continue;
            }
            Log.e("시발 : ",foodDataJson+" key "+entry.getKey());
            FoodData foodData = new Gson().fromJson(foodDataJson, FoodData.class);
            foodDataList.add(foodData);
            String selectedDateStr = foodData.getSelectedDate(); // "2023년11월8일12시35분"와 같은 형식
            Calendar selectedTime = parseSelectedDate(selectedDateStr);
            // 현재 시간과 선택한 시간의 차이를 일로 계산
            String test= String.valueOf(currentTime.getTimeInMillis());

            long diffMillis = currentTime.getTimeInMillis() - selectedTime.getTimeInMillis();
            int diffDays = (int) (diffMillis / (1000 * 60 * 60 * 24));

            Log.e("시간 비교",String.valueOf(selectedTime.getTimeInMillis())+"  "+test+" "+diffDays);
            // 30일 이내인 경우만 추가
            if (diffDays <= 30) {
                foodDatabefore30.add(foodData);
            }
        }

        return foodDatabefore30;
    }
    // 선택한 날짜 문자열을 파싱하여 Calendar 객체로 변환
    private Calendar parseSelectedDate(String selectedDateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy년MM월dd일HH시mm분", Locale.getDefault());
        try {
            Date date = format.parse(selectedDateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void showDynamicPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        Button button = findViewById(R.id.month);
        // 동적으로 생성한 항목을 팝업 메뉴에 추가
        for (int i = 0; i < getDataSize(); i++) {
            popupMenu.getMenu().add(0, i, i, getDataItem(i));
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                TextView text=findViewById(R.id.total_price);

                int itemId = menuItem.getItemId();
                String selectedData = getDataItem(itemId);
                text.setText(selectedData);
                button.setText(selectedData);
                Toast.makeText(FoodAnalyze.this, selectedData + " 선택", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        popupMenu.show();
    }
    // 데이터 크기 반환
    private int getDataSize() {
        // 여기에서 데이터 크기를 동적으로 반환
        // 예: 5개나 10개, 데이터 크기에 따라 다르게 반환
        return 5; // 예제로 5개로 설정
    }

    // 데이터 항목 반환
    private String getDataItem(int index) {
        // 여기에서 인덱스에 따른 데이터 항목을 동적으로 반환
        // 예: 데이터 리스트나 배열에서 해당 인덱스의 항목 반환
        // 예제로 간단히 문자열을 반환
        return "항목 " + (index + 1);
    }
}