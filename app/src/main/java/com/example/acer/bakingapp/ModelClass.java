package com.example.acer.bakingapp;

public class ModelClass {

    String recipie_name;
    String recipie_id;

    public ModelClass(String recipie_name, String recipie_id) {
        this.recipie_name = recipie_name;
        this.recipie_id = recipie_id;
    }

    public String getRecipie_name() {
        return recipie_name;
    }

    public void setRecipie_name(String recipie_name) {
        this.recipie_name = recipie_name;
    }

    public String getRecipie_id() {
        return recipie_id;
    }

    public void setRecipie_id(String recipie_id) {
        this.recipie_id = recipie_id;
    }
}
