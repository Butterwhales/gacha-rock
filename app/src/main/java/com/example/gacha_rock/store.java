package com.example.gacha_rock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;

import androidx.appcompat.app.AppCompatActivity;

public class store extends AppCompatActivity {
    /** The file address of the users stored preferences*/
    private static final String PREFS_NAME = "com.example.gacha_rock.prefs";

    /** The names of the preferences used to store the users data*/
    private static final String GOLD_PREF = "goldPref";
    private static final String GEMS_PREF = "gemsPref";
    private static final String PICKS_PREF = "picksPref";

    private TextView goldText;
    private TextView gemText;
    private TextView pickText;

    private int goldCount = 0;
    private int gemCount = 0;
    private int pickCount = 0;
    DecimalFormat comaFormat = new DecimalFormat("#.##########");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        comaFormat.setGroupingUsed(true);
        comaFormat.setGroupingSize(3);
        initializeItems();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updatePrefs();
        startActivity(new Intent(store.this, MainActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeItems();
        updateDisplay();
    }

    private void initializeItems() {
        goldText = findViewById(R.id.goldText);
        gemText = findViewById(R.id.gemText);
        pickText = findViewById(R.id.pickText);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        goldCount = prefs.getInt(GOLD_PREF, goldCount);
        gemCount = prefs.getInt(GEMS_PREF, gemCount);
        pickCount = prefs.getInt(PICKS_PREF, pickCount);

        updateDisplay();
    }

    private void updateDisplay() {
        goldText.setText(comaFormat.format(goldCount));
        gemText.setText(comaFormat.format(gemCount));
        pickText.setText(comaFormat.format(pickCount));
    }

    public void buttonClick(View view) {
        String buttonName = String.valueOf(view.getTag());
        char character = buttonName.charAt(10);
        if (buttonName.contains("cash")) {
            switch (character) {
                case '1':
                    gemCount += 300;
                    Toast.makeText(getApplicationContext(), "You Spent $0.99", Toast.LENGTH_SHORT).show();
                    break;
                case '4':
                    gemCount += 1800;
                    Toast.makeText(getApplicationContext(), "You Spent $4.99", Toast.LENGTH_SHORT).show();
                    break;
                case '6':
                    gemCount += 4200;
                    Toast.makeText(getApplicationContext(), "You Spent $9.99", Toast.LENGTH_SHORT).show();
                    break;
                case '2':
                    gemCount += 12345;
                    Toast.makeText(getApplicationContext(), "You Spent $24.99", Toast.LENGTH_SHORT).show();
                    break;
                case '5':
                    gemCount += 28000;
                    Toast.makeText(getApplicationContext(), "You Spent $49.99", Toast.LENGTH_SHORT).show();
                    break;
                case '7':
                    gemCount += 70000;
                    Toast.makeText(getApplicationContext(), "You Spent $74.99", Toast.LENGTH_SHORT).show();
                    break;
                case '3':
                    gemCount += 47000;
                    Toast.makeText(getApplicationContext(), "You Spent $99.99", Toast.LENGTH_SHORT).show();
                    break;
            }
            Toast.makeText(getApplicationContext(), "Bought Gems", Toast.LENGTH_SHORT).show();
        } else if (buttonName.contains("pick")) {
            switch (character) {
                case '1':
                    if (gemCount >= 1000) {
                    pickCount += 1;
                    gemCount -= 1000;
                        Toast.makeText(getApplicationContext(), "Bought 1 Pick", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gems, Need " + comaFormat.format(1000 - gemCount) + " Gems", Toast.LENGTH_SHORT).show();
                    break;
                case '4':
                    if (gemCount >= 4875) {
                    pickCount += 5;
                    gemCount -= 4875;
                        Toast.makeText(getApplicationContext(), "Bought 5 Picks", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gems, Need " + comaFormat.format(4875 - gemCount) + " Gems", Toast.LENGTH_SHORT).show();
                    break;
                case '6':
                    if (gemCount >= 9250) {
                    pickCount += 10;
                    gemCount -= 9250;
                        Toast.makeText(getApplicationContext(), "Bought 10 Picks", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gems, Need " + comaFormat.format(9250 - gemCount) + " Gems", Toast.LENGTH_SHORT).show();
                    break;
                case '2':
                    if (gemCount >= 17000) {
                    pickCount += 20;
                    gemCount -= 17000;
                        Toast.makeText(getApplicationContext(), "Bought 20 Picks", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gems, Need " + comaFormat.format(17000 - gemCount) + " Gems", Toast.LENGTH_SHORT).show();
                    break;
                case '5':
                    if (gemCount >= 37500) {
                    pickCount += 50;
                    gemCount -= 37500;
                        Toast.makeText(getApplicationContext(), "Bought 50 Picks", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gems, Need " + comaFormat.format(37500 - gemCount) + " Gems", Toast.LENGTH_SHORT).show();
                    break;
                case '7':
                    if (gemCount >= 65000) {
                        pickCount += 100;
                        gemCount -= 65000;
                        Toast.makeText(getApplicationContext(), "Bought 100 Picks", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gems, Need " + comaFormat.format(65000 - gemCount) + " Gems", Toast.LENGTH_SHORT).show();
                    break;
                case '3':
                    if (gemCount >= 105000) {
                        pickCount += 200;
                        gemCount -= 105000;
                        Toast.makeText(getApplicationContext(), "Bought 200 Picks", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gems, Need " + comaFormat.format(105000 - gemCount) + " Gems", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else if (buttonName.contains("gems")) {
            switch (character) {
                case '1':
                    if (goldCount >= 12345) {
                        gemCount += 1000;
                        goldCount -= 12345;
                        Toast.makeText(getApplicationContext(), "Bought 1,000 Gems", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gold, Need " + comaFormat.format(12345 - goldCount) + " Gold", Toast.LENGTH_SHORT).show();
                    break;
                case '4':
                    if (goldCount >= 61725) {
                        gemCount += 4875;
                        goldCount -= 61725;
                        Toast.makeText(getApplicationContext(), "Bought 4,875 Gems", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gold, Need " + comaFormat.format(61725 - goldCount) + " Gold", Toast.LENGTH_SHORT).show();
                    break;
                case '6':
                    if (goldCount >= 123450) {
                        gemCount += 9250;
                        goldCount -= 123450;
                        Toast.makeText(getApplicationContext(), "Bought 9,250 Gems", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gold, Need " + comaFormat.format(123450 - goldCount) + " Gold", Toast.LENGTH_SHORT).show();
                    break;
                case '2':
                    if (goldCount >= 246900) {
                        gemCount += 17000;
                        goldCount -= 246900;
                        Toast.makeText(getApplicationContext(), "Bought 17,000 Gems", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gold, Need " + comaFormat.format(246900 - goldCount) + " Gold", Toast.LENGTH_SHORT).show();
                    break;
                case '5':
                    if (goldCount >= 617250) {
                        gemCount += 37500;
                        goldCount -= 617250;
                        Toast.makeText(getApplicationContext(), "Bought 37,500 Gems", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gold, Need " + comaFormat.format(617250 - goldCount) + " Gold", Toast.LENGTH_SHORT).show();
                    break;
                case '7':
                    if (goldCount >= 1234500) {
                        gemCount += 65000;
                        goldCount -= 1234500;
                        Toast.makeText(getApplicationContext(), "Bought 65,000 Gems", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gold, Need " + comaFormat.format(1234500 - goldCount) + " Gold", Toast.LENGTH_SHORT).show();
                    break;
                case '3':
                    if (goldCount >= 2469000) {
                        gemCount += 105000;
                        goldCount -= 2469000;
                        Toast.makeText(getApplicationContext(), "Bought 105,000 Gems", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gold, Need " + comaFormat.format(2469000 - goldCount) + " Gold", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            //disable ads
        }
        updateDisplay();
    }

    private void updatePrefs() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(GEMS_PREF, gemCount);
        editor.putInt(GOLD_PREF, goldCount);
        editor.putInt(PICKS_PREF, pickCount);
        editor.apply();
    }
}