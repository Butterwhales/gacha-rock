package com.example.gacha_rock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import androidx.appcompat.app.AppCompatActivity;

public class settings extends AppCompatActivity {
    public rockObject<String> rocksOwned = new rockObject<>();
    public rockObject<String> rocks = new rockObject<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

    }

    public void clearClick(View view) throws IOException {
        rocksOwned.clearAll();
    }

    public void backClick(View view) {
        startActivity(new Intent(settings.this, MainActivity.class));
    }
}