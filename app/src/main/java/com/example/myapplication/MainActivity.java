package com.example.myapplication;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private boolean isFabOpen = false;
    private FloatingActionButton fab1, fab2, fabMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button input=(Button) findViewById(R.id.inputFood);
        FloatingActionButton fabMain1 = findViewById(R.id.testpopup);
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),FoodInput.class);
                startActivity(intent);
            }
        });
        Button testForinfo=findViewById(R.id.testForInfo);
        testForinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),DayInfo.class);
                startActivity(intent);
            }
        });
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fabMain = findViewById(R.id.testpopup);  // Correct initialization

        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabMainClicked(view);
            }
        });
    }
    public void onFab1Clicked(View view) {
        Intent intent=new Intent(getApplicationContext(),FoodAnalyze.class);
        startActivity(intent);

    }

    public void onFab2Clicked(View view) {
        Intent intent=new Intent(getApplicationContext(),FoodView.class);
        startActivity(intent);
    }
    public void ontestpopupClicked(View view){
        toggleFab();
    }

    public void onFabMainClicked(View view) {
        toggleFab();
    }

    private void toggleFab() {
        if (isFabOpen) {
            ObjectAnimator.ofFloat(fab2, "translationY", 0f).start();
            ObjectAnimator.ofFloat(fab1, "translationY", 0f).start();
            fabMain.setImageResource(R.drawable.data_hosting_information);
        } else {
            ObjectAnimator.ofFloat(fab2, "translationY", -170f).start();
            ObjectAnimator.ofFloat(fab1, "translationY", -340f).start();
            fabMain.setImageResource(R.drawable.data_hosting_information_vertical);
        }

        isFabOpen = !isFabOpen;
    }
}