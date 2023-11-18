package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class FoodViewInfo extends AppCompatActivity {
    private ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.view_recyclerview_click);
        int defaultValue=0;
        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("imagePath");
        String food = intent.getStringExtra("food");
        String date = intent.getStringExtra("date");
        String place = intent.getStringExtra("place");
        String type = intent.getStringExtra("type");
        String subName = intent.getStringExtra("subName");
        String evaluation = intent.getStringExtra("evaluation");
        String cost = intent.getStringExtra("cost");
        String drink=intent.getStringExtra("drink");
        int cal = intent.getIntExtra("cal", defaultValue);
        String key = intent.getStringExtra("key");
        Log.e("key : "," ? "+key);
// 이후 각 TextView에 데이터 설정
        TextView placeTextView = findViewById(R.id.viewPlace);
        TextView foodTextView = findViewById(R.id.food_text);
        TextView dateTextView = findViewById(R.id.date_info);
        TextView typeTextView = findViewById(R.id.typeText);
        TextView subNameTextView = findViewById(R.id.sub_text);
        TextView evaluationTextView = findViewById(R.id.evaluationText);
        TextView costTextView = findViewById(R.id.costText);
        TextView drinkTextView=findViewById(R.id.drink_click);
        if(type.equals("음료")){
            drinkTextView.setText(drink);
            Log.e("typechoose",type.toString()+drink);
            // 음식과 반찬 입력 레이아웃 숨김
            findViewById(R.id.constraintLayout2).setVisibility(View.GONE);
            // 음료와 음료 입력 레이아웃 표시
            findViewById(R.id.beverageLayout).setVisibility(View.VISIBLE);
        }else {
            // 다른 종류가 선택된 경우, 음료와 음료 입력 레이아웃 숨김
            findViewById(R.id.beverageLayout).setVisibility(View.GONE);
            // 음식과 반찬 입력 레이아웃 표시
            findViewById(R.id.constraintLayout2).setVisibility(View.VISIBLE);
        }

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
        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(cal);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView textView=findViewById(R.id.cal_setting);
        textView.setText(cal+"/2000");

        ImageButton imageButton=findViewById(R.id.menu_icon);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDynamicPopupMenu(view,key);
            }
        });
    }

    private void showDynamicPopupMenu(View view,String key) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        ImageButton place=(ImageButton) findViewById(R.id.menu_icon);
        SharedPreferences sharedPreferences = getSharedPreferences("FoodPreferences", Context.MODE_PRIVATE);
        popupMenu.getMenuInflater().inflate(R.menu.month_popup,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle().equals("삭제")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(key);
                    editor.apply();
                    finish();
                    return true;
                } else if (menuItem.getTitle().equals("수정")) {
                    int defaultValue=0;
                    Intent intent = getIntent();
                    String imagePath = intent.getStringExtra("imagePath");
                    String food = intent.getStringExtra("food");
                    String date = intent.getStringExtra("date");
                    String place = intent.getStringExtra("place");
                    String type = intent.getStringExtra("type");
                    String subName = intent.getStringExtra("subName");
                    String evaluation = intent.getStringExtra("evaluation");
                    String cost = intent.getStringExtra("cost");
                    String drink=intent.getStringExtra("drink");
                    int cal = intent.getIntExtra("cal", defaultValue);
                    String key = intent.getStringExtra("key");
                }
                return true;
            }
        });

        popupMenu.show();
    }

}
