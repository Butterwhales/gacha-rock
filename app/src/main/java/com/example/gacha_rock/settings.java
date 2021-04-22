package com.example.gacha_rock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;

public class settings extends AppCompatActivity {
    private final ArrayList<Integer> ids = new ArrayList<>(Arrays.asList(R.id.audioSwitch, R.id.darkSwitch, R.id.devSwitch, R.id.infSwitch, R.id.everyFreeSwitch));
    private final ArrayList<CompoundButton> switches = new ArrayList<>();
    private static final String PREFS_NAME = "com.example.gacha_rock.prefs";
    private static final String FEATURED_ROCK_ID = "featuredRockId";
    private static final String AUDIO_PREF = "audioPref";
    private static final String DARK_PREF = "darkPref";
    private static final String DEV_MODE_PREF = "devModePref";
    private static final String INFINITE_LOOT_PREF = "infiniteLootPref";
    private static final String EVERYTHING_IS_FREE_PREF = "everythingIsFreePref";
    public rockObject<String> rocksOwned = new rockObject<>();
    public rockObject<String> rocks = new rockObject<>();

    private int audioMode;
    private int darkMode;
    private int devMode;
    private int featuredRockId;
    private int infiniteMode;
    private int freeMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        audioMode = prefs.getInt(AUDIO_PREF, audioMode);
        darkMode = prefs.getInt(DARK_PREF, darkMode);
        devMode = prefs.getInt(DEV_MODE_PREF, devMode);
        infiniteMode = prefs.getInt(INFINITE_LOOT_PREF, infiniteMode);
        freeMode = prefs.getInt(EVERYTHING_IS_FREE_PREF, freeMode);

        ids.forEach(mySwitch -> switches.add(findViewById(mySwitch)));
        if (audioMode == 1) switches.get(0).setChecked(true);
        if (darkMode == 1) switches.get(1).setChecked(true);
        if (devMode == 1) switches.get(2).setChecked(true);
        if (infiniteMode == 1) switches.get(3).setChecked(true);
        if (freeMode == 1) switches.get(4).setChecked(true);
        switches.forEach(switchThing -> switchThing.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int id = buttonView.getId();      //I would use a switch statement but that provides a warning
            if (isChecked) {
                if (id == R.id.audioSwitch) {
                    audioMode = 1;
                } else if (id == R.id.darkSwitch) {
                    darkMode = 1;
                } else if (id == R.id.devSwitch) {
                    devMode = 1;
                } else if (id == R.id.infSwitch) {
                    infiniteMode = 1;
                } else if (id == R.id.everyFreeSwitch) {
                    freeMode = 1;
                }
            } else {
                if (id == R.id.audioSwitch) {
                    audioMode = 0;
                } else if (id == R.id.darkSwitch) {
                    darkMode = 0;
                } else if (id == R.id.devSwitch) {
                    devMode = 0;
                } else if (id == R.id.infSwitch) {
                    infiniteMode = 0;
                } else if (id == R.id.everyFreeSwitch) {
                    freeMode = 0;
                    System.out.println(freeMode);
                }
            }
            updatePrefs();
            updateDisplay();
        }));
        updateDisplay();
    }

    private void updatePrefs() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(AUDIO_PREF, audioMode);
        editor.putInt(DARK_PREF, darkMode);
        editor.putInt(DEV_MODE_PREF, devMode);
        editor.putInt(INFINITE_LOOT_PREF, infiniteMode);
        editor.putInt(EVERYTHING_IS_FREE_PREF, freeMode);
        editor.putInt(FEATURED_ROCK_ID, featuredRockId);
        editor.apply();
    }

    private void updateDisplay() {
        LinearLayout devLayout = findViewById(R.id.devSettingsLayout);
        TextView devSettingsText = findViewById(R.id.devSettingsText);
        if (devMode == 1) {
            devSettingsText.setVisibility(View.VISIBLE);
            devLayout.setVisibility(View.VISIBLE);
        } else if (devMode == 0) {
            devSettingsText.setVisibility(View.INVISIBLE);
            devLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void clearClick(View view) throws IOException {
        rocksOwned.clearAll();
        featuredRockId = 0;
        updatePrefs();
    }

    public void backClick(View view) {
        startActivity(new Intent(settings.this, MainActivity.class));
    }
}