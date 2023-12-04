package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FoodViewChange extends AppCompatActivity {
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
    protected void onCreate(@Nullable Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.food_input_layout);
        int defaultValue=0;
        Button placebutton=(Button) findViewById(R.id.choose_place);

        Button dateButton=findViewById(R.id.select_date_time_button);
        imageView = findViewById(R.id.imageView);
        Button pickImageButton = findViewById(R.id.pickImageButton);
        Button typeButton=(Button) findViewById(R.id.typeChoose);
        // 저장 버튼
        Button saveButton = findViewById(R.id.save_button);
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
        String[] dateTimeParts = splitDateTime(date);
        if(type.equals("음료")){
            Log.e("typechoose",type);
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
        placebutton.setText(place);
        dateButton.setText(date);
        // 음식 입력
        EditText foodInput = findViewById(R.id.food_input);
        foodInput.setText(food);
        // 반찬 입력
        EditText subInput = findViewById(R.id.sub_input);
        subInput.setText(subName);
        // 평가 입력
        EditText evlInput = findViewById(R.id.evaluation);
        evlInput.setText(evaluation);
        // 가격 입력
        EditText costInput = findViewById(R.id.cost);
        costInput.setText(cost);
        //음료 입력
        EditText drinkInput= findViewById(R.id.drink_input);
        drinkInput.setText(drink);
        Glide.with(this)
                .load(imagePath)
                .into(imageView);
        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageSourceOptions();
            }
        });
        dateButton.setText(dateTimeParts[0]);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitializeView();
                InitializeListener();
                DatePickerDialog dialog = new DatePickerDialog(FoodViewChange.this, callbackMethod, 2023, 10, 12);

                dialog.show();

            }
        });
        Button timeButton = findViewById(R.id.time_info); // 시간 선택
        timeButton.setText(dateTimeParts[1]);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(timeButton);
            }
        });
        typeButton.setText(type);
        //장소 선정
        placebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDynamicPopupMenu(view);
            }
        });

        typeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTypePopupMenu(view);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                // 데이터 세트의 현재 개수를 가져옴, 만약 값이 없으면 0으로 초기화
                int dataSetCount = sharedPreferences.getInt("dataSetCount", 0);
                // 새로운 데이터 세트 키를 생성
                String dataSetKey = key;
                // 장소, 종류, 날짜 값을 가져와서 저장
                String selectedPlace = (String) placebutton.getText(); // 선택된 장소 값
                String selectedType = (String) typeButton.getText(); // 선택된 종류 값
                Button date = findViewById(R.id.select_date_time_button);
                Button time=findViewById(R.id.time_info);
                String selectedDate = date.getText().toString(); // 선택된 날짜 값
                String selectedTime = time.getText().toString();
                String madeTime=selectedDate+selectedTime;

                // 이미지 저장을 위한 디렉토리 생성
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                if (!storageDir.exists()) {
                    storageDir.mkdirs();
                }
                // 이미지 파일의 이름 생성 (고유한 이름을 생성하는 것이 좋음)
                String imageFileName = selectedPlace + selectedDate +System.currentTimeMillis()+ selectedType+dataSetCount+".jpg";
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


                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 음식 입력
                //음료 입력
                // 반찬 입력
                EditText subInput = findViewById(R.id.sub_input);
                String subName;
                EditText drinkInput= findViewById(R.id.drink_input);
                String drink;
                EditText foodInput = findViewById(R.id.food_input);
                String foodName;
                if(selectedType.equals("음료")){
                    foodName="";
                    subName="";
                    drink=drinkInput.getText().toString();
                }else{
                    foodName = foodInput.getText().toString();
                    subName = subInput.getText().toString();
                    drink="";
                }

                // 평가 입력
                EditText evlInput = findViewById(R.id.evaluation);
                String evlText = evlInput.getText().toString();
                // 가격 입력
                EditText costInput = findViewById(R.id.cost);
                String cost = costInput.getText().toString();

                int cal=calories(foodName,subName,drink,FoodViewChange.this);
                // 입력 값 검증
                Bitmap imageBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                if (imageBitmap == null) {
                    Toast.makeText(FoodViewChange.this, "이미지를 선택하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (evlText.isEmpty()) {
                    Toast.makeText(FoodViewChange.this, "평가를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    // 가격이 정수가 아닌 경우 NumberFormatException이 발생할 수 있음
                    Integer.parseInt(cost);
                } catch (NumberFormatException e) {
                    Toast.makeText(FoodViewChange.this, "가격은 정수로 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 데이터를 JSON으로 변환
                FoodData data = new FoodData(imageFile.getAbsolutePath(), foodName, selectedDate,
                        selectedPlace, selectedType, madeTime,
                        imageFileName, subName, evlText, Integer.parseInt(cost),drink,cal, dataSetKey);
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
        textView_Date = (TextView)findViewById(R.id.select_date_time_button);
    }

    public void InitializeListener()
    {
        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                int realMonth=monthOfYear+1;
                textView_Date.setText(year + "년" + realMonth + "월" + dayOfMonth + "일");
            }
        };
    }
    // TimePickerDialog를 열고 시간 선택
    private void showTimePickerDialog(final Button timeTextView) {
        // 현재 시간 정보 가져오기
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // 시간을 TextView에 설정
                timeTextView.setText(hourOfDay + "시" + minute + "분");
            }
        }, hour, minute, false);

        timePickerDialog.show();
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
                if(type.getText().toString().equals("음료")){
                    Log.e("typechoose",type.getText().toString());
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
    //현재 칼로리 데이터 없으므로 임으로 고정 500,100,200 으로
    public int calories(String food, String sub, String drink, Context context){
        int totalCalories = 0;
        List<FoodItem> foodItems = readExcelFile(context);
        Random random = new Random();
        // 음식 칼로리: 해당 음식의 칼로리 값
        Intent intent = getIntent();
        int defaultValue=0;
        int cal = intent.getIntExtra("cal", defaultValue);
        String foodtemp = intent.getStringExtra("food");
        if(foodtemp.equals(food)){
            return cal;
        }
        if (food != null && !food.isEmpty()) {
            int foodCalories = getCaloriesByName(foodItems, "음식", food);
            if(foodCalories==0){
                foodCalories=random.nextInt(401) + 100;
            }
            totalCalories += foodCalories;
        }

        // 반찬 칼로리: 해당 반찬의 칼로리 값
        if (sub != null && !sub.isEmpty()) {
            int subCalories = getCaloriesByName(foodItems, "반찬", sub);
            if(subCalories==0){
                subCalories=random.nextInt(151) + 50;
            }
            totalCalories += subCalories;
        }

        // 음료 칼로리: 해당 음료의 칼로리 값
        if (drink != null && !drink.isEmpty()) {
            int drinkCalories = getCaloriesByName(foodItems, "음료", drink);
            if(drinkCalories==0){
                drinkCalories=random.nextInt(301) + 50;
            }
            totalCalories += drinkCalories;
        }
        return totalCalories;
    }
    private int getCaloriesByName(List<FoodItem> foodItems, String type, String name) {
        for (FoodItem item : foodItems) {
            if (item.getName().equals(name)) {
                return item.getCalories();
            }
        }
        return 0; // 찾지 못한 경우 기본값으로 0을 반환하거나 다른 방식으로 처리
    }

    private List<FoodItem> readExcelFile(Context context) {
        List<FoodItem> foodItems = new ArrayList<>();

        try (InputStream inputStream = context.getAssets().open("localdata.xls");
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트를 읽어옴

            Iterator<Row> rowIterator = sheet.rowIterator();

            // 첫 번째 행은 헤더이므로 넘어감
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // 각 행에서 셀 값을 읽어옴
                Cell typeCell = row.getCell(0);
                Cell nameCell = row.getCell(1);
                Cell caloriesCell = row.getCell(2);

                String type = typeCell.getStringCellValue();
                String name = nameCell.getStringCellValue();

                int calories;
                if (caloriesCell.getCellType() == CellType.NUMERIC) {
                    calories = (int) caloriesCell.getNumericCellValue();
                } else {
                    // Handle the case when the cell contains a string value
                    // You may need to parse the string to get the numeric value
                    // For simplicity, you can set a default value or handle it according to your requirements
                    calories = 0; // Set a default value or handle it accordingly
                }

                FoodItem foodItem = new FoodItem(type, name, calories);
                foodItems.add(foodItem);
            }

        } catch (IOException | EncryptedDocumentException ex) {
            ex.printStackTrace();
        }

        return foodItems;
    }

    private static class FoodItem {
        private final String type;
        private final String name;
        private final int calories;

        public FoodItem(String type, String name, int calories) {
            this.type = type;
            this.name = name;
            this.calories = calories;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public int getCalories() {
            return calories;
        }
    }
    private static String[] splitDateTime(String dateTimeString) {
        // 년월일과 시분을 나누는 기준 위치 찾기
        int timeIndex = dateTimeString.indexOf("일");

        // 기준 위치를 찾을 수 없거나 형식이 맞지 않으면 null 반환
        if (timeIndex == -1 || timeIndex + 1 >= dateTimeString.length()) {
            return null;
        }

        // "년월일"과 "시분" 부분 추출
        String datePart = dateTimeString.substring(0, timeIndex + 1);
        String timePart = dateTimeString.substring(timeIndex + 1);

        // 공백 및 쉼표 제거
        datePart = datePart.replaceAll("[\\s,]+", "");
        timePart = timePart.replaceAll("[\\s,]+", "");

        return new String[]{datePart, timePart};
    }
}
