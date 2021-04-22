package com.example.gacha_rock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import androidx.appcompat.app.AppCompatActivity;

public class summon extends AppCompatActivity {
    private static final String PREFS_NAME = "com.example.gacha_rock.prefs";
    private static final String INFINITE_LOOT_PREF = "infiniteLootPref";
    private static final String EVERTHING_IS_FREE_PREF = "everythingIsFreePref";
    private static final String PICKS_PREF = "picksPref";
    public rockObject<String> rocksOwned = new rockObject<>();
    public rockObject<String> rocks = new rockObject<>();
    weightedRandom<String> rarity = new weightedRandom<>();

    private int infiniteMode;
    private int freeMode;
    private int pickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summon);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        infiniteMode = prefs.getInt(INFINITE_LOOT_PREF, infiniteMode);
        freeMode = prefs.getInt(EVERTHING_IS_FREE_PREF, freeMode);
        pickCount = prefs.getInt(PICKS_PREF, pickCount);
        TextView pickText = findViewById(R.id.pickCountText);
        pickText.setText(String.valueOf(pickCount));
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
        String description;
        while (br.ready()) {
            String[] e = br.readLine().split(" \\| ");
            //System.out.println(Arrays.toString(e));
            if (e.length == 6) {
                description = " ";
                e[6] = e[6].replace(" |", "");
            } else
                description = e[6];
            rocks.addEntry(Integer.parseInt(e[0]), e[1], Double.parseDouble(e[2]), Double.parseDouble(e[3]), Double.parseDouble(e[4]), Integer.parseInt(e[5]), description);
        }
        stream.close();
        br.close();

        String File = "rocks_owned.txt";
        String path = getApplicationContext().getFilesDir().getPath();
        br = new BufferedReader(new InputStreamReader(new FileInputStream(path + "/" + File), StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine()) != null) {
            int e = Integer.parseInt(line);
            //System.out.println(rocks.getName(e));
            rocksOwned.addEntry(e, rocks.getName(e), rocks.getRarity(e), rocks.getRarityOverall(e), rocks.getGemChance(e), rocks.getGemAmount(e), rocks.getDescription(e));
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
        TextView pickText = findViewById(R.id.pickCountText);

        if (freeMode == 0 && infiniteMode == 0) {
            if (pickCount < count) {
                Toast.makeText(getApplicationContext(), "Not enough picks", Toast.LENGTH_SHORT).show();
                return;
            }
        }

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
            System.out.println("rock id: " + rockId + " rock name: " + rocks.getName(rockId) + " Count: " + count);
            rocksOwned.addEntry(rockId, rocks.getName(rockId), rocks.getRarity(rockId), rocks.getRarityOverall(rockId), rocks.getGemChance(rockId), rocks.getGemAmount(rockId), rocks.getDescription(rockId));
        }
        rocksOwned.writeAll();
        if (freeMode == 0 && infiniteMode == 0) {
            pickCount -= count;
            pickText.setText(String.valueOf(pickCount));
        }
        if (count == 1)
            Toast.makeText(getApplicationContext(), "You mined " + count + " rock", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "You mined " + count + " rocks", Toast.LENGTH_SHORT).show();

        updatePrefs();
    }

    private void updatePrefs() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PICKS_PREF, pickCount);
        editor.apply();
    }

    public void backClick(View view) {
        startActivity(new Intent(summon.this, MainActivity.class));
    }
}