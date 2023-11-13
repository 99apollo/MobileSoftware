package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class FoodViewInfo extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.view_recyclerview_click);
        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("imagePath");
        String food = intent.getStringExtra("food");
        String date = intent.getStringExtra("date");
        String place = intent.getStringExtra("place");
        String type = intent.getStringExtra("type");
        String subName = intent.getStringExtra("subName");
        String evaluation = intent.getStringExtra("evaluation");
        String cost = intent.getStringExtra("cost");
// 이후 각 TextView에 데이터 설정
        TextView placeTextView = findViewById(R.id.viewPlace);
        TextView foodTextView = findViewById(R.id.food_text);
        TextView dateTextView = findViewById(R.id.date_info);
        TextView typeTextView = findViewById(R.id.typeText);
        TextView subNameTextView = findViewById(R.id.sub_text);
        TextView evaluationTextView = findViewById(R.id.evaluationText);
        TextView costTextView = findViewById(R.id.costText);

        placeTextView.setText(place);
        foodTextView.setText(food);
        dateTextView.setText(date);
        typeTextView.setText(type);
        subNameTextView.setText(subName);
        evaluationTextView.setText(evaluation);
        costTextView.setText(cost);
        // ImageView에 이미지 표시
        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this).load(imagePath).into(imageView);

        Button backButton=findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}