package com.example.gacha_rock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "com.example.gacha_rock.prefs";

    private static final String SCORE_PREF = "scorePref";
    /** Number of times clicker has been clicked */
    private int count = 0;
    /** Reference to output text view */
    private TextView tv;
    /** Reference to the clicks per second text viewc */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.clickCount);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        count = prefs.getInt(SCORE_PREF, count);

        System.out.println("Hey you");

        if(count > 0){
            String scoreMcScoreFace = "%d", count;
            tv.setText(scoreMcScoreFace);
        }
        updateDisplay();

    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(SCORE_PREF, count);
        editor.apply();
    }

    public void click(View view){

        int id = view.getId();
         if(id == R.id.featuredRock){
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

    public void clickerClick(View view) {
        startActivity(new Intent(MainActivity.this, click.class));
    }

    public void inventoryClick(View view) {
        startActivity(new Intent(MainActivity.this, inventory.class));
    }

    public void storeClick(View view) {
        startActivity(new Intent(MainActivity.this, store.class));
    }

    public void summonsClick(View view) {
        startActivity(new Intent(MainActivity.this, summon.class));
    }

    public void settingsClick(View view) {
        startActivity(new Intent(MainActivity.this, settings.class));
    }
}