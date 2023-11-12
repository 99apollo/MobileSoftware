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
            String imageFileName = sharedPreferences.getString(dataSetKey + "_imagePath", "");
            String foodName = sharedPreferences.getString(dataSetKey + "_foodName", "");
            String selectedDate = sharedPreferences.getString(dataSetKey + "_selectedDate", "");

            // 각 데이터를 dataList에 추가
            dataList.add(new FoodData(imageFileName, foodName, selectedDate));
        }
        Log.e("test",dataList.toString());

        // FoodDataAdapter를 초기화하고 RecyclerView에 설정
        adapter = new FoodDataAdapter(this, dataList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged(); // 데이터가 변경되었음을 알림
    }
}
