package com.example.myapplication;
public class MonthData {
    private String date;
    private int totalCost; // 총 비용
    private int totalCalories; // 총 칼로리
    private int breakfastCalories; // 아침 식사 칼로리
    private int lunchCalories; // 점심 식사 칼로리
    private int dinnerCalories; // 저녁 식사 칼로리
    private int drinkCalories; // 음료 칼로리

    public MonthData(String date, int totalCost, int totalCalories, int breakfastCalories, int lunchCalories, int dinnerCalories, int drinkCalories) {
        this.date = date;
        this.totalCost = totalCost;
        this.totalCalories = totalCalories;
        this.breakfastCalories = breakfastCalories;
        this.lunchCalories = lunchCalories;
        this.dinnerCalories = dinnerCalories;
        this.drinkCalories = drinkCalories;
    }

    // Getter 및 Setter 메서드
    public String getDate(){
        return this.date;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public int getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(int totalCalories) {
        this.totalCalories = totalCalories;
    }

    public int getBreakfastCalories() {
        return breakfastCalories;
    }

    public void setBreakfastCalories(int breakfastCalories) {
        this.breakfastCalories = breakfastCalories;
    }

    public int getLunchCalories() {
        return lunchCalories;
    }

    public void setLunchCalories(int lunchCalories) {
        this.lunchCalories = lunchCalories;
    }

    public int getDinnerCalories() {
        return dinnerCalories;
    }

    public void setDinnerCalories(int dinnerCalories) {
        this.dinnerCalories = dinnerCalories;
    }

    public int getDrinkCalories() {
        return drinkCalories;
    }

    public void setDrinkCalories(int drinkCalories) {
        this.drinkCalories = drinkCalories;
    }

}
