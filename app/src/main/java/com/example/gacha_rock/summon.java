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

public class summon extends AppCompatActivity {
    public rockObject<String> rocksOwned = new rockObject<>();
    public rockObject<String> rocks = new rockObject<>();
    weightedRandom<String> rarity = new weightedRandom<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summon);
        try {
            setup();
        } catch (IOException e) {
            e.printStackTrace();
        }
        rarity.addEntry("Rock", 0.1);
    }


    /**
     * The Plan:
     * click 4 times to open loot box
     */

    private void setup() throws IOException {

        InputStream stream = getResources().openRawResource(R.raw.rocks);
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String description = " ";
        while (br.ready()) {
            String[] e = br.readLine().split(" \\| ");
            System.out.println(Arrays.toString(e));

            if (e.length == 4) {
                description = " ";
                e[3] = e[3].replace(" |", "");
            } else
                description = e[4];

            rocks.addEntry(Integer.parseInt(e[0]), e[1], Double.parseDouble(e[2]), Double.parseDouble(e[3]), description);
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

    public void mine1(View view) throws IOException {
        roll(1);
    }

    public void mine10(View view) throws IOException {
        roll(10);
    }

    private void roll(int count) throws IOException {
        for (int i = 0; i < rocks.getNumRocks(); i++) {
            double overallRarity = rocks.getRarity(i);
            switch ((int) rocks.getRarityOverall(i)) {
                case 0:
                    overallRarity += 10.0;
                    break;
                case 1:
                    overallRarity += 5.0;
                    break;
                case 2:
                    overallRarity += 3.0;
                    break;
                case 3:
                    overallRarity += 2.0;
                    break;
                case 4:
                    overallRarity += 1.0;
                    break;
            }

            rarity.addEntry(String.valueOf(i), overallRarity);
        }
        for (int i = 0; i < count; i++) {
            int rockId = Integer.parseInt(rarity.getRandom());
            System.out.println("rock id: " + rockId + " rock name: " + rocks.getName(rockId));
            rocksOwned.addEntry(rockId, rocks.getName(rockId), rocks.getRarity(rockId), rocks.getRarityOverall(rockId), rocks.getDescription(rockId));
            //grid.addView(makeView(rocks.getName(rockId), String.valueOf(rocks.getRockAmount(rockId))));
        }
        rocksOwned.writeAll();
    }

    public void backClick(View view) {
        startActivity(new Intent(summon.this, MainActivity.class));
    }
}