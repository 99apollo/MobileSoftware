package com.example.myapplication;

public class FoodData {
    private String imagePath;
    private String food;
    private String date;

    public FoodData(String imagePath, String food, String date) {
        this.imagePath = imagePath;
        this.food = food;
        this.date = date;
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
}
