package com.example.myapplication;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalendarTest extends AppCompatActivity {
    private RecyclerView recyclerView3;
    private CalendarView calendarView;
    private FoodAnalyzeAdapter adapter1;
    private FoodAnalyzeAdapter adapter2;
    private TextView displayDataTextView;
    private SharedPreferences sharedPreferences;
    private static final String PREFERENCES_NAME = "FoodPreferences";
    private int Color_num;
    protected void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.calendar_layout);
        List<FoodData> dataList=getAllFoodDataFromPreferences();
        // RecyclerView 초기화
        recyclerView3 = findViewById(R.id.recyclerView3);
        recyclerView3.setLayoutManager(new LinearLayoutManager(this));
        List<CalendarDay> calendarDayList = new ArrayList<>();
        //MaterialCalendarView에 사용
        MaterialCalendarView materialCalendarView = findViewById(R.id.customCalendarView1);
        materialCalendarView.setSelectedDate(CalendarDay.today());
        Log.e("날짜 형식 확인",materialCalendarView.getSelectedDate().toString());
        CalendarDay[] calendarDay;
        for(FoodData entry:dataList){
            calendarDayList.add(convertToCalendarDay(entry.getDate()));
        }
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
                adapter2 = new FoodAnalyzeAdapter(CalendarTest.this, dataList1, new FoodAnalyzeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(FoodData foodData) {

                    }
                });
                recyclerView3.setAdapter(adapter2);
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String foodDataJson = entry.getValue().toString();
            if (!entry.getKey().startsWith("data_set_")) {
                continue;
            }
            Log.e("test : ",foodDataJson+" key "+entry.getKey());
            FoodData foodData = new Gson().fromJson(foodDataJson, FoodData.class);
            foodDataList.add(foodData);
        }

        return foodDataList;
    }

    // 선택한 날짜에 해당하는 데이터를 가져오는 메서드
    private List<FoodData> getFoodDataForSelectedDate(String selectedDate) {
        // SharedPreferences에서 선택한 날짜에 해당하는 데이터를 가져와서 리스트에 추가
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        List<FoodData> foodDataList = new ArrayList<>();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String foodDataJson = entry.getValue().toString();
            if (entry.getKey().startsWith("data_set_")) {
                FoodData foodData = new Gson().fromJson(foodDataJson, FoodData.class);
                if (foodData.getDate().equals(selectedDate)) {
                    foodDataList.add(foodData);
                }
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
