package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button input=(Button) findViewById(R.id.inputFood);
        Button view=(Button)findViewById(R.id.viewFood);
        Button analyze=(Button)findViewById(R.id.analyzeFood);
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"press",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getApplicationContext(),FoodInput.class);
                startActivity(intent);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"press",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getApplicationContext(),FoodView.class);
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
    }
}