package com.example.android.quizapp;

/**
 * Created by apple on 09/05/18.
 */

public class TechnoName {

    String techname;
    String creator;
    int backgroundColor;

    public TechnoName(String techname, String creator) {
        this.techname = techname;
        this.creator = creator;
        
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
