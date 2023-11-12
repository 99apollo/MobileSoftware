package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class FoodAnalyze extends AppCompatActivity {

    private TextView displayDataTextView;
    private SharedPreferences sharedPreferences;
    private static final String PREFERENCES_NAME = "FoodPreferences";
    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.food_analyze_layout);
        Button button = findViewById(R.id.month);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDynamicPopupMenu(view);
            }
        });


    }
    private void showDynamicPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        Button button = findViewById(R.id.month);
        // 동적으로 생성한 항목을 팝업 메뉴에 추가
        for (int i = 0; i < getDataSize(); i++) {
            popupMenu.getMenu().add(0, i, i, getDataItem(i));
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                TextView text=findViewById(R.id.total_price);

                int itemId = menuItem.getItemId();
                String selectedData = getDataItem(itemId);
                text.setText(selectedData);
                button.setText(selectedData);
                Toast.makeText(FoodAnalyze.this, selectedData + " 선택", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        popupMenu.show();
    }
    // 데이터 크기 반환
    private int getDataSize() {
        // 여기에서 데이터 크기를 동적으로 반환
        // 예: 5개나 10개, 데이터 크기에 따라 다르게 반환
        return 5; // 예제로 5개로 설정
    }

    // 데이터 항목 반환
    private String getDataItem(int index) {
        // 여기에서 인덱스에 따른 데이터 항목을 동적으로 반환
        // 예: 데이터 리스트나 배열에서 해당 인덱스의 항목 반환
        // 예제로 간단히 문자열을 반환
        return "항목 " + (index + 1);
    }
}
