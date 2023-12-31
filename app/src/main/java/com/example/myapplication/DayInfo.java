package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
    private boolean needToRestartActivity = false;
    private ProgressBar progressBar;
    List<FoodData> foodList=new ArrayList<>();
    private CircularProgressBar circularProgressBar;
    @Override
    protected void onResume() {
        super.onResume();
        foodList=getAllFoodDataFromPreferences();
        testFun(foodList);
        for(FoodData item:foodList){
            Log.e("restart? : ",item.getDate());
        }
        recyclerView4.setVisibility(View.GONE);
        recyclerView3.setVisibility(View.GONE);
        recyclerView2.setVisibility(View.GONE);
        recyclerView1.setVisibility(View.GONE);

        // 특정 조건을 만족할 때 액티비티를 다시 시작하려면
        /*if (needToRestartActivity) {
            Button temp=findViewById(R.id.DayToday);
            String item = temp.getText().toString();
            recreate(); // 현재 액티비티를 다시 생성
            temp.setText(item);
        }*/
    }

    // 예를 들어, 특정 버튼 클릭 시 액티비티를 다시 시작해야 하는 경우
    private void onSomeButtonClick() {
        // 필요한 작업 수행

        // 액티비티를 다시 시작해야 하는 조건이 충족되면
        needToRestartActivity = true;
    }
    protected void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.view_test_layout);

        foodList=getAllFoodDataFromPreferences();
        // 각 RecyclerView 및 Button을 초기화합니다.
        recyclerView1 = findViewById(R.id.breakfastView);
        recyclerView2 = findViewById(R.id.lunchView);
        recyclerView3 = findViewById(R.id.dinnerView);
        recyclerView4 = findViewById(R.id.drinkView);

        button1 = findViewById(R.id.breakfastInfo);
        button2 = findViewById(R.id.lunchInfo);
        button3 = findViewById(R.id.dinnerInfo);
        button4 = findViewById(R.id.drinkInfo);
        TextView breakfastCalText=findViewById(R.id.breakfastCal);
        TextView lunchCalText=findViewById(R.id.lunchCal);
        TextView dinnerCalText=findViewById(R.id.dinnerCal);
        TextView drinkCalText=findViewById(R.id.drinkCal);
        TextView totalCalText=findViewById(R.id.cal_setting_test);
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

        List<FoodData> foodList2=toDayList(foodList);

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
                    Log.e("clicked : ","?????");
                    adapter1 = new FoodAnalyzeAdapter(DayInfo.this, dataList1, new FoodAnalyzeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(FoodData foodData) {
                            hookUpData(foodData);
                            onSomeButtonClick();
                        }
                    });
                    recyclerView1.setAdapter(adapter1);
                    recyclerView1.setVisibility(View.VISIBLE);
                }
            }

        });
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
                    adapter2 = new FoodAnalyzeAdapter(DayInfo.this, dataList1, new FoodAnalyzeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(FoodData foodData) {
                            hookUpData(foodData);
                            onSomeButtonClick();
                        }
                    });
                    recyclerView2.setAdapter(adapter2);
                    recyclerView2.setVisibility(View.VISIBLE);
                }
            }

        });
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
                    adapter3 = new FoodAnalyzeAdapter(DayInfo.this, dataList1, new FoodAnalyzeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(FoodData foodData) {
                            hookUpData(foodData);
                            onSomeButtonClick();
                        }
                    });
                    recyclerView3.setAdapter(adapter3);
                    recyclerView3.setVisibility(View.VISIBLE);
                }
            }

        });
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
                    adapter4 = new FoodAnalyzeAdapter(DayInfo.this, dataList1, new FoodAnalyzeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(FoodData foodData) {
                            hookUpData(foodData);
                            onSomeButtonClick();
                        }
                    });
                    recyclerView4.setAdapter(adapter4);
                    recyclerView4.setVisibility(View.VISIBLE);
                }
            }

        });
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitializeView();
                InitializeListener(foodList);
                showDatePickerDialog();
                recyclerView4.setVisibility(View.GONE);
                recyclerView3.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.GONE);
                recyclerView1.setVisibility(View.GONE);
                testFun(foodList);
            }
        });
        circularProgressBar = findViewById(R.id.progressBarTest1);
        Button Before =findViewById(R.id.DayBefore);
        List<FoodData> sortedList = new ArrayList<>(foodList); // 기존 리스트를 복제하여 새로운 리스트 생성
        List<FoodData> sortedList1 = new ArrayList<>(foodList);
        Collections.sort(sortedList, new Comparator<FoodData>() {
            @Override
            public int compare(FoodData o1, FoodData o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy년MM월dd일", Locale.KOREA);
                try {
                    // 날짜 문자열을 Date 객체로 변환하여 비교
                    Date date1 = format.parse(o1.getDate());
                    Date date2 = format.parse(o2.getDate());

                    // 오름차순으로 정렬
                    //return date1.compareTo(date2);

                    // 내림차순으로 정렬
                    return date2.compareTo(date1);

                } catch (ParseException e) {
                    e.printStackTrace();
                    // 예외 발생 시, 날짜를 비교할 수 없으므로 0을 반환하거나 다른 적절한 처리를 수행할 수 있습니다.
                    return 0;
                }
            }
        });
        Collections.sort(sortedList1, new Comparator<FoodData>() {
            @Override
            public int compare(FoodData o1, FoodData o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy년MM월dd일", Locale.KOREA);
                try {
                    // 날짜 문자열을 Date 객체로 변환하여 비교
                    Date date1 = format.parse(o1.getDate());
                    Date date2 = format.parse(o2.getDate());
                    // 오름차순으로 정렬
                    return date1.compareTo(date2);
                    // 내림차순으로 정렬
                    //return date2.compareTo(date1);

                } catch (ParseException e) {
                    e.printStackTrace();
                    // 예외 발생 시, 날짜를 비교할 수 없으므로 0을 반환하거나 다른 적절한 처리를 수행할 수 있습니다.
                    return 0;
                }
            }
        });
        for(FoodData item:sortedList){
            Log.e("test compare :",item.getDate());
        }
        Before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button nowDay=findViewById(R.id.DayToday);
                // item과 비교하여 작은 값 찾기
                FoodData firstSmallerItem = null;
                String item=nowDay.getText().toString();
                for (FoodData data : sortedList) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy년MM월dd일", Locale.KOREA);
                    try {
                        Date date1 = format.parse(data.getDate());
                        Date date2 = format.parse(item);
                        Log.d("첫 번째 작은 값", date1.toString()+" "+date2.toString()+" "+date2.compareTo(date1));
                        if (date1.compareTo(date2) < 0) {
                            // item보다 작은 경우
                            firstSmallerItem = data;

                            break; // 작은 값 찾았으면 반복문 중단
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                }
                if (firstSmallerItem != null) {
                    Log.d("첫 번째 작은 값", firstSmallerItem.getDate());
                    nowDay.setText(firstSmallerItem.getDate());
                    testFun(foodList);
                } else {
                    Log.d("작은 값이 없음", "마지막 저장 데이터 입니다");
                }
                recyclerView4.setVisibility(View.GONE);
                recyclerView3.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.GONE);
                recyclerView1.setVisibility(View.GONE);
            }
        });

        Button nextDay=findViewById(R.id.NextDay);
        nextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button nowDay=findViewById(R.id.DayToday);
                // item과 비교하여 작은 값 찾기
                FoodData firstSmallerItem = null;
                String item=nowDay.getText().toString();
                for (FoodData data : sortedList1) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy년MM월dd일", Locale.KOREA);
                    try {
                        Date date1 = format.parse(data.getDate());
                        Date date2 = format.parse(item);
                        Log.d("첫 번째 작은 값", date1.toString()+" "+date2.toString()+" "+date2.compareTo(date1));
                        if (date1.compareTo(date2) > 0) {
                            // item보다 작은 경우
                            firstSmallerItem = data;
                            break; // 작은 값 찾았으면 반복문 중단
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                }
                if (firstSmallerItem != null) {
                    Log.d("첫 번째 작은 값", firstSmallerItem.getDate());
                    nowDay.setText(firstSmallerItem.getDate());
                    testFun(foodList);
                } else {
                    Log.d("작은 값이 없음", "마지막 저장 데이터 입니다");
                }
                recyclerView4.setVisibility(View.GONE);
                recyclerView3.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.GONE);
                recyclerView1.setVisibility(View.GONE);
            }
        });

    }
    private void updateslist(List<FoodData> dataList,RecyclerView recyclerView,FoodAnalyzeAdapter adapter){
        if (recyclerView.getVisibility() == View.VISIBLE) {
            recyclerView.setVisibility(View.GONE);
        } else {
            List<FoodData> dataList2 = new ArrayList<>();
            List<FoodData> dataList1 = new ArrayList<>();
            dataList2=toDayList(dataList);
            dataList1=typeList(dataList2,"음료");
            adapter = new FoodAnalyzeAdapter(DayInfo.this, dataList1, new FoodAnalyzeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(FoodData foodData) {

                }
            });
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
    private void hookUpData(FoodData foodData){
        Intent intent = new Intent(getApplicationContext(),FoodViewInfo.class);
        intent.putExtra("imagePath", foodData.getImagePath());
        int temp=foodData.getCalories();
        Log.e("next item",Integer.toString(temp));
        String cost=String.valueOf(foodData.getCost());
        intent.putExtra("food", foodData.getFood());
        intent.putExtra("date", foodData.getSelectedDate());
        intent.putExtra("place", foodData.getSelectedPlace());
        intent.putExtra("type", foodData.getSelectedType());
        intent.putExtra("subName", foodData.getSubName());
        intent.putExtra("evaluation", foodData.getEvlText());
        intent.putExtra("cost", cost);
        intent.putExtra("cal",temp);
        intent.putExtra("drink",foodData.getDrink());
        intent.putExtra("key",foodData.getKey());
        startActivity(intent);
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
        TextView breakfastCalText=findViewById(R.id.breakfastCal);
        TextView lunchCalText=findViewById(R.id.lunchCal);
        TextView dinnerCalText=findViewById(R.id.dinnerCal);
        TextView drinkCalText=findViewById(R.id.drinkCal);
        TextView totalCalText=findViewById(R.id.cal_setting_test);
        int totalCal=0;
        int brekfastCal=0;
        int lunchCal=0;
        int dinnerCal=0;
        int drinkCal=0;
        for(FoodData entry:TodatList){

            String Type=entry.getSelectedType();
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
        breakfastCalText.setText(brekfastCal+"칼로리");
        lunchCalText.setText(lunchCal+"칼로리");
        dinnerCalText.setText(dinnerCal+"칼로리");
        drinkCalText.setText(drinkCal+"칼로리");
        totalCalText.setText(totalCal+"/2000");
        progressBar = findViewById(R.id.progressBarTest);
        progressBar.setProgress(totalCal);

        return TodatList;
    }
    private void testFun(List<FoodData> foodList){
        List<FoodData> TodatList=toDayList(foodList);
        TextView breakfastCalText=findViewById(R.id.breakfastCal);
        TextView lunchCalText=findViewById(R.id.lunchCal);
        TextView dinnerCalText=findViewById(R.id.dinnerCal);
        TextView drinkCalText=findViewById(R.id.drinkCal);
        TextView totalCalText=findViewById(R.id.cal_setting_test);
        int totalCal=0;
        int brekfastCal=0;
        int lunchCal=0;
        int dinnerCal=0;
        int drinkCal=0;
        for(FoodData entry:TodatList){

            String Type=entry.getSelectedType();
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
        breakfastCalText.setText(brekfastCal+"칼로리");
        lunchCalText.setText(lunchCal+"칼로리");
        dinnerCalText.setText(dinnerCal+"칼로리");
        drinkCalText.setText(drinkCal+"칼로리");
        totalCalText.setText(totalCal+"/2000");
        progressBar = findViewById(R.id.progressBarTest);
        circularProgressBar = findViewById(R.id.progressBarTest1);
        progressBar.setProgress(totalCal);
        circularProgressBar.setProgress(totalCal);
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
            Log.e("test : ", foodDataJson + " key " + entry.getKey());

            try {
                FoodData foodData = new Gson().fromJson(foodDataJson, FoodData.class);
                foodDataList.add(foodData);
            } catch (JsonSyntaxException e) {
                // 데이터 형식이 일치하지 않는 경우, 로그를 남기고 다음 엔트리로 건너뜀
                Log.e("Error parsing data", "Key: " + entry.getKey() + ", Value: " + foodDataJson);
            }
        }

        return foodDataList;
    }

    public void InitializeView()
    {
        textView_Date = (TextView)findViewById(R.id.DayToday);
    }

    public void InitializeListener(List<FoodData> dataList)
    {
        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                int realMonth = monthOfYear + 1;
                String selectedDate = year + "년" + realMonth + "월" + dayOfMonth + "일";
                int count=0;
                for(FoodData item:dataList){
                    if(selectedDate.equals(item.getDate())){
                        count++;
                    }
                }
                if(count==0){
                    Toast.makeText(DayInfo.this, "데이터 없음!", Toast.LENGTH_LONG).show();
                    return;
                }
                textView_Date.setText(selectedDate);
                testFun(dataList);
            }
        };
    }

}
