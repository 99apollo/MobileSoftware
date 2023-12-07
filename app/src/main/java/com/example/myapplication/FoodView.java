package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FoodView extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FoodDataAdapter adapter;
    private SharedPreferences sharedPreferences;
    private static final String PREFERENCES_NAME = "FoodPreferences";
    private boolean needToRestartActivity = false;
    protected void onResume() {
        super.onResume();
        // 데이터를 저장하고 있는 dataList을 초기화
        List<FoodData> dataList = new ArrayList<>();
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        int dataSetCount = sharedPreferences.getInt("dataSetCount", 0);

        for (int i = 0; i < dataSetCount; i++) {
            String dataSetKey = "data_set_" + i;
            String dataJson = sharedPreferences.getString(dataSetKey, ""); // JSON 형식의 데이터 가져오기
            if (!dataJson.isEmpty()) {
                // JSON 데이터를 FoodData 객체로 파싱
                try {
                    FoodData data = new Gson().fromJson(dataJson, FoodData.class);
                    dataList.add(data);
                    Log.e("test",data.getSelectedDate()+"  중간다리  "+data.getImagePath());
                    // Gson을 사용하여 JSON 파싱하는 코드
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    Log.e("error json",dataJson);
                    // 예외 처리 로직 추가
                }


            }


        }
        adapter = new FoodDataAdapter(this, dataList, new FoodDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FoodData foodData) {
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
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged(); // 데이터가 변경되었음을 알림

        // 특정 조건을 만족할 때 액티비티를 다시 시작하려면
        /*if (needToRestartActivity) {
            Button temp=findViewById(R.id.DayToday);
            String item = temp.getText().toString();
            recreate(); // 현재 액티비티를 다시 생성
            temp.setText(item);
        }*/
    }
    private void onSomeButtonClick() {
        // 필요한 작업 수행

        // 액티비티를 다시 시작해야 하는 조건이 충족되면
        needToRestartActivity = true;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_view_layout);

        Button input = findViewById(R.id.view_Input);
        Button analyze = findViewById(R.id.view_Analyze);

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FoodInput.class);
                startActivity(intent);
            }
        });

        analyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FoodAnalyze.class);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPreferences1 = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences1.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // key와 value를 출력하거나 사용
            Log.d("SharedPreferences", "Key: " + key + ", Value: " + value.toString());
        }

        recyclerView = findViewById(R.id.data_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // SharedPreferences에서 데이터 개수를 가져옴
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        int dataSetCount = sharedPreferences.getInt("dataSetCount", 0);

        // 데이터를 저장하고 있는 dataList을 초기화
        List<FoodData> dataList = new ArrayList<>();

        for (int i = 0; i < dataSetCount; i++) {
            String dataSetKey = "data_set_" + i;
            String dataJson = sharedPreferences.getString(dataSetKey, ""); // JSON 형식의 데이터 가져오기
            if (!dataJson.isEmpty()) {
                // JSON 데이터를 FoodData 객체로 파싱
                try {
                    FoodData data = new Gson().fromJson(dataJson, FoodData.class);
                    dataList.add(data);
                    Log.e("test",data.getSelectedDate()+"  중간다리  "+data.getImagePath());
                    // Gson을 사용하여 JSON 파싱하는 코드
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    // 예외 처리 로직 추가
                }

            }


        }


        // FoodDataAdapter를 초기화하고 RecyclerView에 설정
        adapter = new FoodDataAdapter(this, dataList, new FoodDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FoodData foodData) {
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
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged(); // 데이터가 변경되었음을 알림
        Button all=findViewById(R.id.allFood);
        Button breakfast=findViewById(R.id.breakfast);
        Button drink=findViewById(R.id.drink);

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 데이터를 저장하고 있는 dataList을 초기화
                List<FoodData> dataList = new ArrayList<>();

                for (int i = 0; i < dataSetCount; i++) {
                    String dataSetKey = "data_set_" + i;
                    String dataJson = sharedPreferences.getString(dataSetKey, ""); // JSON 형식의 데이터 가져오기
                    if (!dataJson.isEmpty()) {
                        // JSON 데이터를 FoodData 객체로 파싱
                        FoodData data = new Gson().fromJson(dataJson, FoodData.class);
                        dataList.add(data);
                    }
                }
                Log.e("test",dataList.toString());

                breakfast.setText("장소");
                drink.setText("종류");
                // FoodDataAdapter를 초기화하고 RecyclerView에 설정
                adapter = new FoodDataAdapter(FoodView.this, dataList, new FoodDataAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(FoodData foodData) {
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
                });
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged(); // 데이터가 변경되었음을 알림
            }
        });
        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<FoodData> filteredDataList1 = getAllFoodDataFromPreferences();
                showDynamicPopupMenu(view,filteredDataList1);

            }
        });

        drink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<FoodData> filteredDataList1 = getAllFoodDataFromPreferences();
                showTypePopupMenu(view,filteredDataList1);

            }
        });


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
    private void typeChoose(String item,List<FoodData> dataList){
        List<FoodData> filteredDataList1 = getAllFoodDataFromPreferences();
        String temp= item;
        if(temp.equals("조식")){
            filterData("조식",filteredDataList1);
        }else if(temp.equals("중식")){
            filterData("중식",filteredDataList1);
        }else if(temp.equals("석식")){
            filterData("석식",filteredDataList1);
        }else{
            filterData("음료",filteredDataList1);
        }
    }
    private void placeChoose(String item,List<FoodData> dataList){
        List<FoodData> filteredDataList1 = getAllFoodDataFromPreferences();
        String temp= item;
        if(temp.equals("상록원 3층")){
            filterPlaceData("상록원 3층",filteredDataList1);
        }else if(temp.equals("상록원 2층")){
            filterPlaceData("상록원 2층",filteredDataList1);
        }else if(temp.equals("기숙사")){
            filterPlaceData("기숙사",filteredDataList1);
        }else{
            filterPlaceData("기타",filteredDataList1);
        }
    }
    private void showDynamicPopupMenu(View view,List<FoodData> dataList) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        Button place=(Button) findViewById(R.id.breakfast);
        popupMenu.getMenuInflater().inflate(R.menu.place_popup,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String place_name= (String) menuItem.getTitle();
                place.setText(place_name);
                List<FoodData> filteredDataList1 = getAllFoodDataFromPreferences();
                Button type=(Button) findViewById(R.id.drink);
                placeChoose(place_name,filteredDataList1);
                return true;
            }
        });

        popupMenu.show();
    }
    private void filterData(String dataType,List<FoodData> dataList) {
        List<FoodData> filteredDataList = new ArrayList<>();
        List<FoodData> filteredDataList1 = getAllFoodDataFromPreferences();
        Button place = findViewById(R.id.breakfast);
        for (FoodData data : filteredDataList1) {
            if (data.getSelectedType().equals(dataType)) {
                if(place.getText().equals("장소")){
                    filteredDataList.add(data);
                }else{
                    if(data.getSelectedPlace().equals(place.getText())){
                        filteredDataList.add(data);
                    }
                }
            }
        }
        adapter = new FoodDataAdapter(this, filteredDataList, new FoodDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FoodData foodData) {
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
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged(); // 데이터가 변경되었음을 알림
    }
    private void filterPlaceData(String dataType,List<FoodData> dataList) {
        List<FoodData> filteredDataList = new ArrayList<>();
        List<FoodData> filteredDataList1 = getAllFoodDataFromPreferences();
        Button type = findViewById(R.id.drink);
        for (FoodData data : filteredDataList1) {
            if (data.getSelectedPlace().equals(dataType)) {
                if(type.getText().equals("종류")){
                    filteredDataList.add(data);
                }else{
                    if(data.getSelectedType().equals(type.getText())){
                        filteredDataList.add(data);
                    }
                }
            }
        }

        adapter = new FoodDataAdapter(this, filteredDataList, new FoodDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FoodData foodData) {
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
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged(); // 데이터가 변경되었음을 알림
    }
    private void showTypePopupMenu(View view,List<FoodData> dataList) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        Button type=(Button) findViewById(R.id.drink);
        popupMenu.getMenuInflater().inflate(R.menu.type_popup,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String place_name= (String) menuItem.getTitle();
                type.setText(place_name);
                List<FoodData> filteredDataList1 = getAllFoodDataFromPreferences();
                typeChoose(place_name,filteredDataList1);
                return true;
            }
        });

        popupMenu.show();
    }
}
