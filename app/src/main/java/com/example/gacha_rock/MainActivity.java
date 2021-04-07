package com.example.gacha_rock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "com.example.gacha_rock.prefs";

    private static final String GOLD_PREF = "goldPref";
    private static final String GEMS_PREF = "gemsPref";
    private static final String PICKS_PREF = "picksPref";
    /** Number of times clicker has been clicked */
    private int goldCount = 0;
    private int gemCount = 0;
    private int pickCount = 0;
    /** Reference to output text view */
    private TextView goldText;
    private TextView gemText;
    private TextView pickText;
    /** Reference to the clicks per second text viewc */

    private ImageView featuredRock;

    private int fingerCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        featuredRock = findViewById(R.id.featuredRock);

        // multitouch
        featuredRock.setOnTouchListener(fingerCounterListener);
        initializeItems();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(GEMS_PREF, gemCount);
        editor.putInt(GOLD_PREF, goldCount);
        editor.apply();
    }


    private final View.OnTouchListener fingerCounterListener = new View.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(goldCount < 0){
                goldCount = 0;
            }
            if(fingerCount < 0){
                fingerCount = 0;
            }
            switch(event.getActionMasked()){
                case MotionEvent.ACTION_DOWN:
                    fingerCount +=1;
                    goldCount += 1;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    goldCount += 1;
                    fingerCount +=1;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    fingerCount -= 1;
                    break;
                case MotionEvent.ACTION_UP:
                    fingerCount = 0;
                    break;

            }
            updateDisplay();

            return true;
        }
    };


    private void initializeItems(){
        goldText = findViewById(R.id.goldText);
        gemText = findViewById(R.id.gemText);
        // pickText = findViewById(R.id.pickText);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        goldCount = prefs.getInt(GOLD_PREF, goldCount);
        gemCount = prefs.getInt(GEMS_PREF, gemCount);
        pickCount = prefs.getInt(PICKS_PREF, pickCount);


        updateDisplay();
    }

    //cps counter
    // measure the amount of time between clicks. use that to calculate cps.
    // multi-threading
    //store last 5 time differences average them out

    private void updateDisplay(){
        goldText.setText(String.format("%d", goldCount));
        gemText.setText(String.format("%d", gemCount));
        // pickText.setText(String.format("%d", pickCount));
    }

    private void updatePrefs(){
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(GEMS_PREF, gemCount);
        editor.putInt(GOLD_PREF, goldCount);
        editor.apply();
    }

    public void inventoryClick(View view) {
        updatePrefs();
        startActivity(new Intent(MainActivity.this, inventory.class));
    }

    public void storeClick(View view) {
        updatePrefs();
        startActivity(new Intent(MainActivity.this, store.class));
    }

    public void summonsClick(View view) {
        updatePrefs();
        startActivity(new Intent(MainActivity.this, summon.class));
    }

    public void settingsClick(View view) {
        updatePrefs();
        startActivity(new Intent(MainActivity.this, settings.class));
    }
}