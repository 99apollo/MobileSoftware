package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FoodView extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FoodDataAdapter adapter;
    private SharedPreferences sharedPreferences;
    private static final String PREFERENCES_NAME = "FoodPreferences";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_view_layout);

        Button input = findViewById(R.id.view_Input);
        Button analyze = findViewById(R.id.view_Analyze);

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Press", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), FoodInput.class);
                startActivity(intent);
            }
        });

        analyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Press", Toast.LENGTH_LONG).show();
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
                FoodData data = new Gson().fromJson(dataJson, FoodData.class);
                dataList.add(data);
                Log.e("test",data.getSelectedDate()+"  중간다리  "+data.getImagePath());
            }


        }


        // FoodDataAdapter를 초기화하고 RecyclerView에 설정
        adapter = new FoodDataAdapter(this, dataList, new FoodDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FoodData foodData) {
                Intent intent = new Intent(getApplicationContext(),FoodViewInfo.class);
                intent.putExtra("imagePath", foodData.getImagePath());
                intent.putExtra("food", foodData.getFood());
                intent.putExtra("date", foodData.getSelectedDate());
                intent.putExtra("place", foodData.getSelectedPlace());
                intent.putExtra("type", foodData.getSelectedType());
                intent.putExtra("subName", foodData.getSubName());
                intent.putExtra("evaluation", foodData.getEvlText());
                intent.putExtra("cost", foodData.getCost());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged(); // 데이터가 변경되었음을 알림
        Button all=findViewById(R.id.allFood);
        Button breakfast=findViewById(R.id.breakfast);
        Button Lunch=findViewById(R.id.lunch);
        Button dinner = findViewById(R.id.Dinner);
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

                // FoodDataAdapter를 초기화하고 RecyclerView에 설정
                adapter = new FoodDataAdapter(FoodView.this, dataList, new FoodDataAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(FoodData foodData) {
                        Intent intent = new Intent(getApplicationContext(),FoodViewInfo.class);
                        intent.putExtra("imagePath", foodData.getImagePath());
                        intent.putExtra("food", foodData.getFood());
                        intent.putExtra("date", foodData.getSelectedDate());
                        intent.putExtra("place", foodData.getSelectedPlace());
                        intent.putExtra("type", foodData.getSelectedType());
                        intent.putExtra("subName", foodData.getSubName());
                        intent.putExtra("evaluation", foodData.getEvlText());
                        intent.putExtra("cost", foodData.getCost());
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
                // 조식 데이터만 표시
                filterData("조식",dataList);
            }
        });

        Lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 중식 데이터만 표시
                filterData("중식",dataList);
            }
        });

        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 석식 데이터만 표시
                filterData("석식",dataList);
            }
        });

        drink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 음료 데이터만 표시
                filterData("음료",dataList);
            }
        });
    }

    private void filterData(String dataType,List<FoodData> dataList) {
        List<FoodData> filteredDataList = new ArrayList<>();
        for (FoodData data : dataList) {
            if (data.getSelectedType().equals(dataType)) {
                filteredDataList.add(data);
            }
        }

        adapter = new FoodDataAdapter(this, filteredDataList, new FoodDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FoodData foodData) {
                Intent intent = new Intent(getApplicationContext(),FoodViewInfo.class);
                intent.putExtra("imagePath", foodData.getImagePath());
                intent.putExtra("food", foodData.getFood());
                intent.putExtra("date", foodData.getSelectedDate());
                intent.putExtra("place", foodData.getSelectedPlace());
                intent.putExtra("type", foodData.getSelectedType());
                intent.putExtra("subName", foodData.getSubName());
                intent.putExtra("evaluation", foodData.getEvlText());
                intent.putExtra("cost", foodData.getCost());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged(); // 데이터가 변경되었음을 알림
    }

}
