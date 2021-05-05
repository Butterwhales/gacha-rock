package com.example.gacha_rock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;


import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;
import static androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode;

public class settings extends AppCompatActivity {
    /** Holds the ids for all of the switches on the page*/
    private final ArrayList<Integer> ids = new ArrayList<>(Arrays.asList(R.id.audioSwitch, R.id.darkSwitch, R.id.devSwitch, R.id.infSwitch, R.id.everyFreeSwitch, R.id.disableAdsSwitch));
    private final ArrayList<CompoundButton> switches = new ArrayList<>();

    /** The file address of the users stored preferences*/
    private static final String PREFS_NAME = "com.example.gacha_rock.prefs";

    /** The names of the preferences used to store the users data*/
    private static final String FEATURED_ROCK_ID = "featuredRockId";
    private static final String AUDIO_PREF = "audioPref";
    private static final String DARK_PREF = "darkPref";
    private static final String DEV_MODE_PREF = "devModePref";
    private static final String INFINITE_LOOT_PREF = "infiniteLootPref";
    private static final String EVERYTHING_IS_FREE_PREF = "everythingIsFreePref";
    private static final String DISABLE_ADS = "disableAds";

    /** Used to hold all of the rocks that the user owns*/
    public rockObject<String> rocksOwned = new rockObject<>();
    /** Used to hold every single rock in the game*/
    public rockObject<String> rocks = new rockObject<>();

    /** Turns audio on and off
     * Currently does nothing*/
    private int audioMode;
    /**The id of the users featured rock*/
    private int featuredRockId;
    /** If (1): developer mode is enabled. If (0): developer mode is disabled.*/
    private int devMode;
    /** If (1): dark mode is enabled. If (0): dark is disabled.*/
    private int darkMode;
    /** If (1): ads are disabled. If (0): ads are enabled.*/
    private int disableAds;
    /** If (1): Infinite items are enabled. If (0): Infinite items are disabled.
     * Makes summoning free*/
    private int infiniteMode;
    /** If (1): Free mode is enabled. If (0): Free mode is disabled.
     * Makes all items in the store free*/
    private int freeMode;

    /** The ad displayed at the bottom of the page */
    private AdView mAdView;

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
        disableAds = prefs.getInt(DISABLE_ADS, disableAds);

        if (disableAds == 0) {
            MobileAds.initialize(this, initializationStatus -> {
            });
            mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }


        ids.forEach(mySwitch -> switches.add(findViewById(mySwitch)));
        if (audioMode == 1) switches.get(0).setChecked(true);
        if (darkMode == 1) switches.get(1).setChecked(true);
        if (devMode == 1) switches.get(2).setChecked(true);
        if (infiniteMode == 1) switches.get(3).setChecked(true);
        if (freeMode == 1) switches.get(4).setChecked(true);
        if (disableAds == 1) switches.get(5).setChecked(true);
        switches.forEach(switchThing -> switchThing.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int id = buttonView.getId();      //I would use a switch statement but that provides a warning
            if (isChecked) {
                if (id == R.id.audioSwitch) {
                    audioMode = 1;
                } else if (id == R.id.darkSwitch) {
                    darkMode = 1;
                    updatePrefs();
                    setDefaultNightMode(MODE_NIGHT_YES);
                } else if (id == R.id.devSwitch) {
                    devMode = 1;
                } else if (id == R.id.infSwitch) {
                    infiniteMode = 1;
                } else if (id == R.id.everyFreeSwitch) {
                    freeMode = 1;
                } else if (id == R.id.disableAdsSwitch) {
                    disableAds = 1;
                }
            } else {
                if (id == R.id.audioSwitch) {
                    audioMode = 0;
                } else if (id == R.id.darkSwitch) {
                    darkMode = 0;
                    updatePrefs();
                    setDefaultNightMode(MODE_NIGHT_NO);
                } else if (id == R.id.devSwitch) {
                    devMode = 0;
                    infiniteMode = 0;
                    freeMode = 0;
                } else if (id == R.id.infSwitch) {
                    infiniteMode = 0;
                } else if (id == R.id.everyFreeSwitch) {
                    freeMode = 0;
                } else if (id == R.id.disableAdsSwitch) {
                    disableAds = 0;
                }
            }
            updatePrefs();
            updateDisplay();
        }));
        updateDisplay();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(settings.this, MainActivity.class));
    }

    /**
     * Updates the users preferences
     */
    private void updatePrefs() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(AUDIO_PREF, audioMode);
        editor.putInt(DARK_PREF, darkMode);
        editor.putInt(DEV_MODE_PREF, devMode);
        editor.putInt(INFINITE_LOOT_PREF, infiniteMode);
        editor.putInt(EVERYTHING_IS_FREE_PREF, freeMode);
        editor.putInt(FEATURED_ROCK_ID, featuredRockId);
        editor.putInt(DISABLE_ADS, disableAds);
        editor.apply();
    }

    /**
     * Updates the display
     * If (devmode is on): Add switches to toggle: Dev mode, Infinite mode, Free mode, and Disable Ads.
     * If ads are enabled display an ad at the bottom of the page.
     */
    private void updateDisplay() {
        LinearLayout devLayout = findViewById(R.id.devSettingsLayout);
        TextView devSettingsText = findViewById(R.id.devSettingsText);
        AdView adView = findViewById(R.id.adView);
        if (devMode == 1) {
            devSettingsText.setVisibility(View.VISIBLE);
            devLayout.setVisibility(View.VISIBLE);
        } else if (devMode == 0) {
            devSettingsText.setVisibility(View.INVISIBLE);
            devLayout.setVisibility(View.INVISIBLE);
        }
        if (disableAds == 1) {
            adView.setVisibility(View.INVISIBLE);
        } else if (disableAds == 0) {
            adView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Clears the users owned rocks
     * @param view
     * @throws IOException
     */
    public void clearClick(View view) throws IOException {
        rocksOwned.clearAll();
        featuredRockId = 1;
        updatePrefs();
    }
}