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
    private String cost;

    public FoodData(String imagePath, String food, String date,
                    String selectedPlace, String selectedType, String selectedDate,
                    String imageFileName, String subName, String evlText, String cost) {
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

    public String getCost() {
        return cost;
    }
}
