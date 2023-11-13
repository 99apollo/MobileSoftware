package com.example.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

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
                SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                // 데이터 세트의 현재 개수를 가져옴, 만약 값이 없으면 0으로 초기화
                int dataSetCount = sharedPreferences.getInt("dataSetCount", 0);
                // 새로운 데이터 세트 키를 생성
                String dataSetKey = "data_set_" + dataSetCount;
                // 장소, 종류, 날짜 값을 가져와서 저장
                String selectedPlace = (String) place.getText(); // 선택된 장소 값
                String selectedType = (String) typeButton.getText(); // 선택된 종류 값
                TextView date = findViewById(R.id.date_info);
                String selectedDate = date.getText().toString(); // 선택된 날짜 값



                // 이미지 저장을 위한 디렉토리 생성
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                if (!storageDir.exists()) {
                    storageDir.mkdirs();
                }
                // 이미지 파일의 이름 생성 (고유한 이름을 생성하는 것이 좋음)
                String imageFileName = selectedPlace + selectedDate + selectedType+".jpg";
                // 이미지 파일을 저장할 경로 생성
                File imageFile = new File(storageDir, imageFileName);
                // 이미지를 저장
                try {
                    FileOutputStream outputStream = new FileOutputStream(imageFile);

                    // 이미지를 Bitmap으로 변환 (이미지뷰의 이미지 가져오기)
                    Bitmap imageBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                    // 이미지를 JPEG 형식으로 저장
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.close();

                    // 이미지 파일의 경로를 SharedPreferences에 저장
                    editor.putString(dataSetKey+"_imagePath", imageFile.getAbsolutePath());
                    editor.apply();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 데이터 세트의 현재 개수를 1 증가시킴
                dataSetCount++;
                editor.putInt("dataSetCount", dataSetCount);
                editor.apply();
                // 음식 입력
                EditText foodInput = findViewById(R.id.food_input);
                String foodName = foodInput.getText().toString();
                // 반찬 입력
                EditText subInput = findViewById(R.id.sub_input);
                String subName = subInput.getText().toString();
                // 평가 입력
                EditText evlInput = findViewById(R.id.evaluation);
                String evlText = evlInput.getText().toString();
                // 가격 입력
                EditText costInput = findViewById(R.id.cost);
                String cost = costInput.getText().toString();
                // 데이터를 JSON으로 변환
                FoodData data = new FoodData(imageFileName, foodName, selectedDate,
                        selectedPlace, selectedType, selectedDate,
                        imageFileName, subName, evlText, cost);
                String dataJson = new Gson().toJson(data);

                // SharedPreferences에 저장
                editor.putString(dataSetKey, dataJson);
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
