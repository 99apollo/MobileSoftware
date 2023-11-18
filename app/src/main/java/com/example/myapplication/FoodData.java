package com.example.myapplication;

public class FoodData {
    private String imagePath;
    private String food;
    private String date;
    private String selectedPlace;
    private String selectedType;
    private String selectedDate;
    private String imageFileName;
    private String subName;
    private String evlText;
    private int cost;
    private String drink;
    private int calories;

    private String key;
    public FoodData(String imagePath, String food, String date,
                    String selectedPlace, String selectedType, String selectedDate,
                    String imageFileName, String subName, String evlText, int cost,String drink, int calories, String key) {
        this.imagePath = imagePath;
        this.food = food;
        this.date = date;
        this.selectedPlace = selectedPlace;
        this.selectedType = selectedType;
        this.selectedDate = selectedDate;
        this.imageFileName = imageFileName;
        this.subName = subName;
        this.evlText = evlText;
        this.cost = cost;
        this.drink=drink;
        this.calories=calories;
        this.key=key;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getFood() {
        return food;
    }

    public String getDate() {
        return date;
    }

    public String getSelectedPlace() {
        return selectedPlace;
    }

    public String getSelectedType() {
        return selectedType;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public String getSubName() {
        return subName;
    }

    public String getEvlText() {
        return evlText;
    }

    public int getCost() {
        return cost;
    }

    public String getDrink() {
        return drink;
    }

    public int getCalories(){return calories;}

    public String getKey() {
        return key;
    }
}
