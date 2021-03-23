package com.example.gacha_rock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Hey you");
    }

    public void clickerClick(View view) {
        startActivity(new Intent(MainActivity.this, click.class));
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