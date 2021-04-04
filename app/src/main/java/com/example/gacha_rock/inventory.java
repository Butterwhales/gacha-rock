package com.example.gacha_rock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import androidx.appcompat.app.AppCompatActivity;

public class inventory extends AppCompatActivity {
    rockMasterList<String> rocksOwned = new rockMasterList<>();
    rockMasterList<String> rocks = new rockMasterList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
    }

    private void setup() throws IOException {
        InputStream stream = getResources().openRawResource(R.raw.rocks);
        Reader fr = new InputStreamReader(stream);
        BufferedReader br = new BufferedReader(fr);
        while (br.ready()) {
            String[] e = br.readLine().split("/ | +/");
            rocks.addEntry(Integer.parseInt(e[0]), e[1],Double.parseDouble(e[2]), Double.parseDouble(e[3]), e[4]);
        }
        br.close();

        stream = getResources().openRawResource(R.raw.rocksOwned);
        fr = new InputStreamReader(stream);
        br = new BufferedReader(fr);
        while (br.ready()) {
            String[] e = br.readLine().split("/ | +/");
            rocksOwned.addEntry(Integer.parseInt(e[0]), e[1], Double.parseDouble(e[2]), Double.parseDouble(e[3]), e[4]);
        }
        br.close();
    }

    public void backClick(View view) {
        startActivity(new Intent(inventory.this, MainActivity.class));
    }
}