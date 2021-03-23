package com.example.gacha_rock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class summon extends AppCompatActivity {
    weightedRandom<String> rarity = new weightedRandom<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summon);

        rarity.addEntry("Rock", 0.1);
    }
}