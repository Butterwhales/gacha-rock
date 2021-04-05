package com.example.gacha_rock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;

public class inventory extends AppCompatActivity {
    public rockObject<String> rocksOwned = new rockObject<>();
    public rockObject<String> rocks = new rockObject<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        try {
            setup();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(rocks.getName(0));
        System.out.println(rocks.getId("Cobblestone"));
    }

    private void setup() throws IOException {
        InputStream stream = getResources().openRawResource(R.raw.rocks);
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        while (br.ready()) {
            String[] e = br.readLine().split(" \\| ");
            System.out.println(Arrays.toString(e));

            rocks.addEntry(Integer.parseInt(e[0]), e[1], Double.parseDouble(e[2]), Double.parseDouble(e[3]), e[4]);
        }
        br.close();

        stream = getResources().openRawResource(R.raw.rocks_owned);
        br = new BufferedReader(new InputStreamReader(stream));
        while (br.ready()) {
            int e = Integer.parseInt(br.readLine());
            rocksOwned.addEntry(e, rocks.getName(e), rocks.getRarity(e), rocks.getRarityOverall(e), rocks.getDescription(e));
        }
        br.close();
    }

    public void close() throws IOException {
        rocks.writeAll();
    }

    public void backClick(View view) throws IOException {
        startActivity(new Intent(inventory.this, MainActivity.class));
        //close(); Doesn't work
    }
}