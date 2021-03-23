package com.example.gacha_rock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class click extends AppCompatActivity {

    /** Number of times clicker has been clicked */
    private int count = 0;
    /** Reference to output text view */
    private TextView tv;
    /** Reference to the clicks per second text viewc */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click);
        tv = findViewById(R.id.outputTV);

        updateDisplay();
    }

    public void click(View view){

        int id = view.getId();
        if (id == R.id.resetView){
            count = 0;
        } else if(id == R.id.clickerButton){
            count++;
            if(count < 0){
                count = 0;
            }
        } else {
            throw new IllegalArgumentException("No action defined for this view");
        }

        // Take the count value and update the textview
        updateDisplay();
    }



    //cps counter
    // measure the amount of time between clicks. use that to calculate cps.
    // multi-threading
    //store last 5 time differences average them out

    private void updateDisplay(){
        tv.setText(String.format("%d", count));
    }

    public void backClick(View view) {
        startActivity(new Intent(click.this, MainActivity.class));
    }
}