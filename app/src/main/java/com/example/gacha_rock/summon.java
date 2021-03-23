package com.example.gacha_rock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class summon extends AppCompatActivity {
    weightedRandom<String> rarity = new weightedRandom<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summon);

        rarity.addEntry("Rock", 0.1);
    }

    public void backClick(View view) {
        startActivity(new Intent(click.this, MainActivity.class));
    }
}