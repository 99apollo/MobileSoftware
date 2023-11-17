package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DayInfo extends AppCompatActivity {
    private RecyclerView recyclerView1, recyclerView2, recyclerView3, recyclerView4, recyclerView5;
    private Button button1, button2, button3, button4, button5;
    private Button dayToday;
    private DatePicker datePicker;
    private TextView textView_Date;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    private static final String PREFERENCES_NAME = "FoodPreferences";
    private FoodAnalyzeAdapter adapter1,adapter2,adapter3,adapter4;
    protected void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.view_test_layout);
        List<FoodData> foodList=getAllFoodDataFromPreferences();
        // 각 RecyclerView 및 Button을 초기화합니다.
        recyclerView1 = findViewById(R.id.breakfastView);
        recyclerView2 = findViewById(R.id.lunchView);
        recyclerView3 = findViewById(R.id.dinnerView);
        recyclerView4 = findViewById(R.id.drinkView);

        button1 = findViewById(R.id.breakfastInfo);
        button2 = findViewById(R.id.lunchInfo);
        button3 = findViewById(R.id.dinnerInfo);
        button4 = findViewById(R.id.drinkInfo);

        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView3.setLayoutManager(new LinearLayoutManager(this));
        recyclerView4.setLayoutManager(new LinearLayoutManager(this));
        dayToday = findViewById(R.id.DayToday);
        //날짜 선정
        Button dateButton=findViewById(R.id.DayToday);

        // 현재 날짜를 가져오기
        Date currentDate = new Date();

        // 출력 형식 지정
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년MM월dd일");

        // 현재 날짜를 지정한 형식으로 변환
        String formattedDate = dateFormat.format(currentDate);
        dayToday.setText(formattedDate);
        adapter1 = new FoodAnalyzeAdapter(this, new ArrayList<>());
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("date confirm",dateButton.getText().toString());
                if (recyclerView1.getVisibility() == View.VISIBLE) {
                    recyclerView1.setVisibility(View.GONE);
                } else {
                    List<FoodData> dataList2 = new ArrayList<>();
                    List<FoodData> dataList1 = new ArrayList<>();
                    dataList2=toDayList(foodList);
                    dataList1=typeList(dataList2,"조식");
                    adapter1 = new FoodAnalyzeAdapter(DayInfo.this, dataList1);
                    recyclerView1.setAdapter(adapter1);
                    recyclerView1.setVisibility(View.VISIBLE);
                }
            }

        });
        adapter2 = new FoodAnalyzeAdapter(this, new ArrayList<>());
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerView2.getVisibility() == View.VISIBLE) {
                    recyclerView2.setVisibility(View.GONE);
                } else {
                    List<FoodData> dataList2 = new ArrayList<>();
                    List<FoodData> dataList1 = new ArrayList<>();
                    dataList2=toDayList(foodList);
                    dataList1=typeList(dataList2,"중식");
                    adapter2 = new FoodAnalyzeAdapter(DayInfo.this, dataList1);
                    recyclerView2.setAdapter(adapter2);
                    recyclerView2.setVisibility(View.VISIBLE);
                }
            }

        });
        adapter3 = new FoodAnalyzeAdapter(this, new ArrayList<>());
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerView3.getVisibility() == View.VISIBLE) {
                    recyclerView3.setVisibility(View.GONE);
                } else {
                    List<FoodData> dataList2 = new ArrayList<>();
                    List<FoodData> dataList1 = new ArrayList<>();
                    dataList2=toDayList(foodList);
                    dataList1=typeList(dataList2,"석식");
                    adapter3 = new FoodAnalyzeAdapter(DayInfo.this, dataList1);
                    recyclerView3.setAdapter(adapter3);
                    recyclerView3.setVisibility(View.VISIBLE);
                }
            }

        });
        adapter4 = new FoodAnalyzeAdapter(this, new ArrayList<>());
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerView4.getVisibility() == View.VISIBLE) {
                    recyclerView4.setVisibility(View.GONE);
                } else {
                    List<FoodData> dataList2 = new ArrayList<>();
                    List<FoodData> dataList1 = new ArrayList<>();
                    dataList2=toDayList(foodList);
                    dataList1=typeList(dataList2,"음료");
                    adapter4 = new FoodAnalyzeAdapter(DayInfo.this, dataList1);
                    recyclerView4.setAdapter(adapter4);
                    recyclerView4.setVisibility(View.VISIBLE);
                }
            }

        });
        //오늘 날짜
        Long today = MaterialDatePicker.todayInUtcMilliseconds();
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //InitializeView();
                //InitializeListener();
                //showDatePickerDialog();
                MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("Date Picker")
                        .setSelection(today) // 오늘 날짜 셋팅
                        .setTheme(R.style.AppTheme); // AppTheme을 여기에 사용

                MaterialDatePicker<Long> materialDatePicker = builder.build();

                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");

                // 확인 버튼
                materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년MM월d일");
                    Date date = new Date();
                    date.setTime(selection);

                    String dateString = simpleDateFormat.format(date);

                    dateButton.setText(dateString);
                });

            }
        });

    }
    private List<FoodData> typeList(List<FoodData> dataList,String type){
        List<FoodData> dataList1 = new ArrayList<>();
        for (FoodData temp : dataList) {
            if(temp.getSelectedType().equals(type)){
                dataList1.add(temp);
            }

        }
        return dataList1;
    }
    private List<FoodData> toDayList(List<FoodData> dataList){
        Button toDay=findViewById(R.id.DayToday);
        List<FoodData> TodatList=new ArrayList<>();
        String today1=toDay.getText().toString();
        for(FoodData temp:dataList){
            if(temp.getDate().equals(today1)){
                TodatList.add(temp);
            }
        }

        return TodatList;
    }
    private void showDatePickerDialog() {
        InitializeView(); // textView_Date 초기화

        // 현재 날짜로 초기화된 DatePickerDialog 생성
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // DatePickerDialog 객체 생성
        DatePickerDialog dialog = new DatePickerDialog(
                DayInfo.this,
                callbackMethod,
                year,
                month,
                dayOfMonth
        );
        // DatePickerDialog 표시
        dialog.show();
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
            Log.e("시발 : ",foodDataJson+" key "+entry.getKey());
            FoodData foodData = new Gson().fromJson(foodDataJson, FoodData.class);
            foodDataList.add(foodData);
        }

        return foodDataList;
    }
    public void InitializeView()
    {
        textView_Date = (TextView)findViewById(R.id.DayToday);
    }

    public void InitializeListener()
    {
        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                int realMonth = monthOfYear + 1;
                String selectedDate = year + "년" + realMonth + "월" + dayOfMonth + "일";
                textView_Date.setText(selectedDate);
            }
        };
    }

    // RecyclerView를 토글하는 메서드
    private void toggleRecyclerView(RecyclerView recyclerView) {
        if (recyclerView.getVisibility() == View.VISIBLE) {
            fadeOutRecyclerView(recyclerView);
        } else {
            fadeInRecyclerView(recyclerView);
        }
    }
    // RecyclerView를 페이드 인하는 메서드
    private void fadeInRecyclerView(RecyclerView recyclerView) {
        recyclerView.setVisibility(View.VISIBLE);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(recyclerView, "alpha", 0f, 1f);
        fadeIn.setDuration(500);
        fadeIn.start();
    }

    // RecyclerView를 페이드 아웃하는 메서드
    private void fadeOutRecyclerView(RecyclerView recyclerView) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(recyclerView, "alpha", 1f, 0f);
        fadeOut.setDuration(500);
        fadeOut.start();
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                recyclerView.setVisibility(View.GONE);
            }
        });
    }
}
