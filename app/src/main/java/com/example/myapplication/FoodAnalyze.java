package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FoodAnalyze extends AppCompatActivity {
    private RecyclerView recyclerView2;
    private CalendarView calendarView;
    private FoodAnalyzeAdapter adapter1;
    private FoodAnalyzeAdapter adapter2;
    private TextView displayDataTextView;
    private SharedPreferences sharedPreferences;
    private static final String PREFERENCES_NAME = "FoodPreferences";
    private int Color_num;



    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.food_analyze_layout);
        List<FoodData> dataList=getAllFoodDataFromPreferences();
        int totalCost=0;
        int totalCal=0;
        int brekfastCal=0;
        int lunchCal=0;
        int dinnerCal=0;
        int drinkCal=0;
        int item=0;
        List<CalendarDay> calendarDayList = new ArrayList<>();
        CalendarDay[] calendarDay;
        for(FoodData entry:dataList){
            item++;
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
            calendarDayList.add(convertToCalendarDay(entry.getDate()));
        }
        TextView recent =findViewById(R.id.recent30days);
        recent.setText("최근 30일\n 총 칼로리: "+totalCal+"\n 총 비용 : "+totalCost+"\n 조식 칼로리 : "+brekfastCal+"\n 중식 칼로리 : "+lunchCal+"\n 석식 칼로리 : "+dinnerCal+"\n 음료 칼로리 : "+drinkCal);


        // SharedPreferences에서 데이터 개수를 가져옴
//        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
//// RecyclerView 초기화
//        recyclerView2 = findViewById(R.id.recyclerView2);
//        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
//// 어댑터 초기화
//        adapter1 = new FoodAnalyzeAdapter(this, dataList);
//        recyclerView2.setAdapter(adapter1);

        // RecyclerView 초기화
        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        adapter1 = new FoodAnalyzeAdapter(this, new ArrayList<>(), new FoodAnalyzeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FoodData foodData) {

            }
        });

        adapter2 = new FoodAnalyzeAdapter(this, new ArrayList<>(), new FoodAnalyzeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FoodData foodData) {

            }
        });
        //MaterialCalendarView에 사용
        MaterialCalendarView materialCalendarView = findViewById(R.id.customCalendarView);
        materialCalendarView.setSelectedDate(CalendarDay.today());
        Log.e("날짜 형식 확인",materialCalendarView.getSelectedDate().toString());
        for(CalendarDay entry:calendarDayList){
            EventDecorator eventDecorator = new EventDecorator(Color.RED, Collections.singletonList(entry));
            materialCalendarView.addDecorator(eventDecorator);
        }

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                // 사용자가 선택한 날짜를 이용하여 데이터를 가져와 RecyclerView를 업데이트
                int year = date.getYear();
                int month = date.getMonth(); // 월은 0부터 시작하므로 1을 더해줍니다.
                int dayOfMonth = date.getDay();

                String selectedDate = year + "년" + month + "월" + dayOfMonth + "일";
                Log.e("선택 날짜",selectedDate);
                List<FoodData> dataList1 = getFoodDataForSelectedDate(selectedDate);
                adapter2 = new FoodAnalyzeAdapter(FoodAnalyze.this, dataList1, new FoodAnalyzeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(FoodData foodData) {

                    }
                });
                recyclerView2.setAdapter(adapter2);
            }
        });
    }
    public static CalendarDay convertToCalendarDay(String dateStr) {
        // dateStr을 파싱하여 year, month, day를 추출
        // 예: "2023년11월24일" -> year=2023, month=11, day=24
        int year, month, day;

        // 날짜 포맷 지정
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년MM월dd일", Locale.getDefault());

        try {
            // SimpleDateFormat을 사용하여 Date 객체로 파싱
            Date date = dateFormat.parse(dateStr);

            // Date 객체를 사용하여 년, 월, 일 추출
            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 1을 더함
                day = calendar.get(Calendar.DAY_OF_MONTH);

                return CalendarDay.from(year, month, day);
            } else {
                System.out.println("날짜 형식이 올바르지 않습니다.");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
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
            try {
                FoodData foodData = new Gson().fromJson(foodDataJson, FoodData.class);
                foodDataList.add(foodData);
                String selectedDateStr = foodData.getSelectedDate(); // "2023년11월8일12시35분"와 같은 형식
                if(selectedDateStr==null){
                    continue;
                }else{
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

            } catch (JsonSyntaxException e) {
                // 데이터 형식이 일치하지 않는 경우, 로그를 남기고 다음 엔트리로 건너뜀
                Log.e("Error parsing data", "Key: " + entry.getKey() + ", Value: " + foodDataJson);
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
    // 선택한 날짜에 해당하는 데이터를 가져오는 메서드
    private List<FoodData> getFoodDataForSelectedDate(String selectedDate) {
        // SharedPreferences에서 선택한 날짜에 해당하는 데이터를 가져와서 리스트에 추가
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        List<FoodData> foodDataList = new ArrayList<>();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String foodDataJson = entry.getValue().toString();
            if (!entry.getKey().startsWith("data_set_")) {
                continue;
            }
            Log.e("test : ", foodDataJson + " key " + entry.getKey());

            try {
                FoodData foodData = new Gson().fromJson(foodDataJson, FoodData.class);
                if (foodData.getDate().equals(selectedDate)) {
                    foodDataList.add(foodData);
                }
            } catch (JsonSyntaxException e) {
                // 데이터 형식이 일치하지 않는 경우, 로그를 남기고 다음 엔트리로 건너뜀
                Log.e("Error parsing data", "Key: " + entry.getKey() + ", Value: " + foodDataJson);
            }
        }

        return foodDataList;
    }

    public class EventDecorator implements DayViewDecorator {

        private final int color;
        private final HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5, color));
        }
    }
}