package com.example.gacha_rock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class store extends AppCompatActivity {

    private static final String PREFS_NAME = "com.example.gacha_rock.prefs";

    private static final String GOLD_PREF = "goldPref";
    private static final String GEMS_PREF = "gemsPref";
    private static final String PICKS_PREF = "picksPref";

    private TextView goldText;
    private TextView gemText;
    private TextView pickText;

    private int goldCount = 0;
    private int gemCount = 0;
    private int pickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        initializeItems();
    }

    private void initializeItems() {
        goldText = findViewById(R.id.goldText);
        gemText = findViewById(R.id.gemText);
        // pickText = findViewById(R.id.pickText);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        goldCount = prefs.getInt(GOLD_PREF, goldCount);
        gemCount = prefs.getInt(GEMS_PREF, gemCount);
        pickCount = prefs.getInt(PICKS_PREF, pickCount);


        updateDisplay();
    }

    private void updateDisplay() {
        goldText.setText(String.format("%d", goldCount));
        gemText.setText(String.format("%d", gemCount));
        // pickText.setText(String.format("%d", pickCount));
    }

    public void backClick(View view) {
        updatePrefs();
        startActivity(new Intent(store.this, MainActivity.class));
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