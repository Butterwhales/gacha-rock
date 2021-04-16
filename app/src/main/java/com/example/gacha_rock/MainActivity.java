package com.example.gacha_rock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class MainActivity extends AppCompatActivity {
    public rockObject<String> rocksOwned = new rockObject<>();
    public rockObject<String> rocks = new rockObject<>();
    private static final String PREFS_NAME = "com.example.gacha_rock.prefs";

    private static final String GOLD_PREF = "goldPref";
    private static final String GEMS_PREF = "gemsPref";
    private static final String PICKS_PREF = "picksPref";
    private static final String PLAYER_LVL_PREF = "playerLvlPref";
    private static final String TOTAL_CLICKS = "totalClicksPref";
    private static final String FEATURED_ROCK_ID = "featuredRockId";
    /**
     * Number of times clicker has been clicked
     */
    private int goldCount = 0;
    private int gemCount = 0;
    private int pickCount = 0;
    private float playerLvl = 0;
    private int totalClicks = 0;
    /**
     * Reference to output text view
     */
    private TextView goldText;
    private TextView gemText;
    private TextView pickText;
    private TextView playerText;
    private ProgressBar xpBar;

    private int fingerCount;
    private Random rand = new Random();
    private ImageView featuredRock;
    private int featuredRockId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /** Reference to the clicks per second text viewc */
        featuredRock = findViewById(R.id.featuredRock);

        // multitouch
        featuredRock.setOnTouchListener(fingerCounterListener);
        initializeItems();
        try {
            setup();
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateDisplay();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(GEMS_PREF, gemCount);
        editor.putInt(GOLD_PREF, goldCount);
        editor.putFloat(PLAYER_LVL_PREF, playerLvl);
        editor.putInt(TOTAL_CLICKS, totalClicks);
        editor.putInt(FEATURED_ROCK_ID, featuredRockId);
        editor.apply();
    }


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(GEMS_PREF, gemCount);
        editor.putInt(GOLD_PREF, goldCount);
        editor.putFloat(PLAYER_LVL_PREF, playerLvl);
        editor.putInt(TOTAL_CLICKS, totalClicks);
        editor.putInt(FEATURED_ROCK_ID, featuredRockId);
        editor.apply();
    }

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

    private final View.OnTouchListener fingerCounterListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int additionalGold = 0;
            int additionalGems = 0;
            float chance = rand.nextFloat();

            if (chance <= playerLvl / 100) {
                if (playerLvl >= 100)
                    additionalGold = Math.round(playerLvl / 100);
                else
                    additionalGold = 1;
            }

            if (chance <= rocks.getGemChance(featuredRockId)) {
                additionalGems = rocks.getGemAmount(featuredRockId);
            }

            if (goldCount < 0)
                goldCount = 0;
            if (fingerCount < 0)
                fingerCount = 0;

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    fingerCount += 1;
                    goldCount += 1 + additionalGold;
                    gemCount += additionalGems;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    goldCount += 1 + additionalGold;
                    gemCount += additionalGems;
                    fingerCount += 1;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    fingerCount -= 1;
                    break;
                case MotionEvent.ACTION_UP:
                    fingerCount = 0;
                    break;
            }
            updateDisplay();
            return true;
        }
    };

    private void initializeItems() {
        goldText = findViewById(R.id.goldText);
        gemText = findViewById(R.id.gemText);
        playerText = findViewById(R.id.playerText);
        // pickText = findViewById(R.id.pickText);
        xpBar = findViewById(R.id.progressBar);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        goldCount = prefs.getInt(GOLD_PREF, goldCount);
        gemCount = prefs.getInt(GEMS_PREF, gemCount);
        pickCount = prefs.getInt(PICKS_PREF, pickCount);
        playerLvl = prefs.getFloat(PLAYER_LVL_PREF, playerLvl);
        totalClicks = prefs.getInt(TOTAL_CLICKS, totalClicks);
        totalClicks = prefs.getInt(FEATURED_ROCK_ID, featuredRockId);
        totalClicks = goldCount; //TODO remove this
        updateDisplay();
    }

    //cps counter
    // measure the amount of time between clicks. use that to calculate cps.
    // multi-threading
    //store last 5 time differences average them out

    private void updateDisplay() {
        playerLvl = (float) (Math.sqrt(goldCount) / 10);
        xpBar.setProgress(Math.round(playerLvl % 1 * 100));

        goldText.setText(String.valueOf(goldCount));
        gemText.setText(String.valueOf(gemCount));
        playerText.setText(String.valueOf((int) Math.floor(playerLvl)));
        // pickText.setText(String.format("%d", pickCount));
        featuredRockId = 17;
        Drawable rockDrawable = ResourcesCompat.getDrawable(getResources(), getDrawableFromId(rocks.getName(featuredRockId)), getApplicationContext().getTheme());
        featuredRock.setImageDrawable(rockDrawable);
    }

    public int getDrawableFromId(String id) {
        int resourceId;
        if (id == null) {
            resourceId = getApplicationContext().getResources().getIdentifier("rock_chan", "drawable", getApplicationContext().getPackageName());
            System.out.println("Id = null in getDrawableFromId");
            return resourceId;
        }
        String name = id.toLowerCase().replaceAll(" ", "_").replaceAll("\\.", "_");
        resourceId = getApplicationContext().getResources().getIdentifier(name, "drawable", getApplicationContext().getPackageName());
        if (resourceId == 0) {
            name = name + "_icon";
            resourceId = getApplicationContext().getResources().getIdentifier(name, "drawable", getApplicationContext().getPackageName());
            if (resourceId == 0)
                resourceId = getApplicationContext().getResources().getIdentifier("rock_chan", "drawable", getApplicationContext().getPackageName());
        }
        return resourceId;
    }

    private void updatePrefs() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(GEMS_PREF, gemCount);
        editor.putInt(GOLD_PREF, goldCount);
        editor.putFloat(PLAYER_LVL_PREF, playerLvl);
        editor.putInt(TOTAL_CLICKS, totalClicks);
        editor.putInt(FEATURED_ROCK_ID, featuredRockId);
        editor.apply();
    }

    public void inventoryClick(View view) {
        updatePrefs();
        startActivity(new Intent(MainActivity.this, inventory.class));
    }

    public void storeClick(View view) {
        updatePrefs();
        startActivity(new Intent(MainActivity.this, store.class));
    }

    public void summonsClick(View view) {
        updatePrefs();
        startActivity(new Intent(MainActivity.this, summon.class));
    }

    public void settingsClick(View view) {
        updatePrefs();
        startActivity(new Intent(MainActivity.this, settings.class));
    }
}