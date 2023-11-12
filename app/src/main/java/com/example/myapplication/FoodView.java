package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FoodView extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FoodDataAdapter adapter;
    private SharedPreferences sharedPreferences;
    private static final String PREFERENCES_NAME = "MyPreferences";
    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.food_view_layout);
        Button input=(Button) findViewById(R.id.view_Input);
        Button analyze=(Button)findViewById(R.id.view_Analyze);
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"press",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getApplicationContext(),FoodInput.class);
                startActivity(intent);
            }
        });
        analyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"press",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getApplicationContext(),FoodAnalyze.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.data_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // FoodData 리스트를 생성하여 어댑터에 전달
        List<FoodData> dataList = createSampleData(); // 여기서 데이터를 채우는 방식은 사용자에게 따라 다를 수 있습니다.
        adapter = new FoodDataAdapter(this, dataList);

        recyclerView.setAdapter(adapter);

    }

    // 샘플 데이터 생성
    private List<FoodData> createSampleData() {
        List<FoodData> dataList = new ArrayList<>();
        // 여기서 FoodData 객체를 생성하고 dataList에 추가하여 리사이클러뷰에 표시할 데이터를 구성합니다.
        // 예: dataList.add(new FoodData("이미지 경로", "음식 이름", "날짜"));
        return dataList;
    }

}
