package com.example.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class FoodInput extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private TextView textView_Date;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    private EditText foodInput;
    private EditText subInput;
    private Button selectImageButton;
    private ImageView imageView;
    private Uri imageUri;

    private SharedPreferences sharedPreferences;
    private static final String PREFERENCES_NAME = "FoodPreferences";
    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.food_input_layout);
        Button place=(Button) findViewById(R.id.choose_place);
        //장소 선정
        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDynamicPopupMenu(view);
            }
        });
        //날짜 선정
        TextView textView=findViewById(R.id.date_info);
        Button dateButton=findViewById(R.id.select_date_time_button);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(FoodInput.this, callbackMethod, 2022, 11, 12);
                dialog.show();
                InitializeView();
                InitializeListener();
            }
        });
        //종류
        Button typeButton=(Button) findViewById(R.id.typeChoose);
        typeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTypePopupMenu(view);
            }
        });
        imageView = findViewById(R.id.imageView);
        Button pickImageButton = findViewById(R.id.pickImageButton);

        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageSourceOptions();
            }
        });



        // 저장 버튼
        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // SharedPreferences에 선택된 장소, 종류, 날짜 저장
                sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
                SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // 장소, 종류, 날짜 값을 가져와서 저장
                String selectedPlace = (String) place.getText(); // 선택된 장소 값
                String selectedType = (String) typeButton.getText(); // 선택된 종류 값
                TextView date=findViewById(R.id.date_info);
                String selectedDate = date.getText().toString(); // 선택된 날짜 값

                editor.putString("selectedPlace", selectedPlace);
                editor.putString("selectedType", selectedType);
                editor.putString("selectedDate", selectedDate);

                // 음식 입력
                EditText foodInput = findViewById(R.id.food_input);
                String foodName = foodInput.getText().toString();

                // 반찬 입력
                EditText subInput = findViewById(R.id.sub_input);
                String subName = subInput.getText().toString();

                // SharedPreferences에 음식과 반찬 저장
                editor.putString("foodName", foodName);
                editor.putString("subName", subName);

                // 변경 사항을 적용
                editor.apply();
                finish();
            }
        });
    }
    //날짜 선택 관련 항목 코드
    public void InitializeView()
    {
        textView_Date = (TextView)findViewById(R.id.date_info);
    }

    public void InitializeListener()
    {
        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                textView_Date.setText(year + "년" + monthOfYear + "월" + dayOfMonth + "일");
            }
        };
    }
    //장소 관련
    private void showDynamicPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        Button place=(Button) findViewById(R.id.choose_place);
        popupMenu.getMenuInflater().inflate(R.menu.place_popup,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String place_name= (String) menuItem.getTitle();
                place.setText(place_name);
                return true;
            }
        });

        popupMenu.show();
    }
    // 데이터 항목 반환
    private String getDataItem(int index) {
        // 여기에서 인덱스에 따른 데이터 항목을 동적으로 반환
        // 예: 데이터 리스트나 배열에서 해당 인덱스의 항목 반환
        // 예제로 간단히 문자열을 반환
        return "항목 " + (index + 1);
    }
    private void showTypePopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        Button type=(Button) findViewById(R.id.typeChoose);
        popupMenu.getMenuInflater().inflate(R.menu.type_popup,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String place_name= (String) menuItem.getTitle();
                type.setText(place_name);
                return true;
            }
        });

        popupMenu.show();
    }
    // 데이터 항목 반환
    private String getTypeDataItem(int index) {
        // 여기에서 인덱스에 따른 데이터 항목을 동적으로 반환
        // 예: 데이터 리스트나 배열에서 해당 인덱스의 항목 반환
        // 예제로 간단히 문자열을 반환
        return "항목 " + (index + 1);
    }

    private void saveFoodAndSideDish() {
        // 사용자가 입력한 음식과 반찬을 가져옴
        String food = foodInput.getText().toString();
        String sideDish = subInput.getText().toString();

        // SharedPreferences를 초기화
        SharedPreferences sharedPreferences = getSharedPreferences("FoodData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 사용자가 입력한 음식과 반찬을 SharedPreferences에 저장
        editor.putString("food", food);
        editor.putString("side_dish", sideDish);

        // 변경 내용을 저장
        editor.apply();

        // 저장 완료 메시지 표시 또는 다른 작업 수행
    }

    private void showImageSourceOptions() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK);
        pickImageIntent.setType("image/*");

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Intent chooserIntent = Intent.createChooser(pickImageIntent, "Select Image Source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePictureIntent});

        startActivityForResult(chooserIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST && data != null) {
                Uri selectedImage = data.getData();
                imageView.setImageURI(selectedImage);
            } else if (requestCode == CAMERA_REQUEST && data != null) {
                Bundle extras = data.getExtras();
                Bitmap photo = (Bitmap) extras.get("data");
                imageView.setImageBitmap(photo);
            }
        }
    }

}
