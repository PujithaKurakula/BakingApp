package com.example.acer.bakingapp;

public class IngredientsModel {

    String ingredient,measure,quantity;

    public IngredientsModel(String ingredient, String measure, String quantity) {
        this.ingredient = ingredient;
        this.measure = measure;
        this.quantity = quantity;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
