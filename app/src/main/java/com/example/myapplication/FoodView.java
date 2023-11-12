package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FoodView extends AppCompatActivity {
    private TextView displayDataTextView;
    private SharedPreferences sharedPreferences;
    private static final String PREFERENCES_NAME = "FoodPreferences";
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

        displayDataTextView = findViewById(R.id.display_data_textview);
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);

        // SharedPreferences에서 데이터를 가져와 텍스트 뷰에 표시
        String place = sharedPreferences.getString("place", "");
        String food = sharedPreferences.getString("food", "");
        String sub = sharedPreferences.getString("sub", "");
        String evaluation = sharedPreferences.getString("evaluation", "");
        String dateInfo = sharedPreferences.getString("dateInfo", "");
        String cost = sharedPreferences.getString("cost", "");

        String displayedData = "장소: " + place + "\n음식: " + food + "\n반찬: " + sub
                + "\n평가: " + evaluation + "\n날짜 및 시간: " + dateInfo + "\n비용: " + cost;

        displayDataTextView.setText(displayedData);
    }

}
