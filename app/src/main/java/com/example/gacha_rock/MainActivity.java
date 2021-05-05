package com.example.gacha_rock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;
import static androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode;

public class MainActivity extends AppCompatActivity {
    /** Used to hold all of the rocks that the user owns*/
    public rockObject<String> rocksOwned = new rockObject<>();
    /** Used to hold every single rock in the game*/
    public rockObject<String> rocks = new rockObject<>();

    /** The file address of the users stored preferences*/
    private static final String PREFS_NAME = "com.example.gacha_rock.prefs";

    /** The names of the preferences used to store the users data*/
    private static final String GOLD_PREF = "goldPref";
    private static final String GEMS_PREF = "gemsPref";
    private static final String PICKS_PREF = "picksPref";
    private static final String DARK_PREF = "darkPref";
    private static final String PLAYER_LVL_PREF = "playerLvlPref";
    private static final String TOTAL_CLICKS = "totalClicksPref";
    private static final String FEATURED_ROCK_ID = "featuredRockId";
    private static final String DEV_MODE_PREF = "devModePref";
    private static final String DISABLE_ADS = "disableAds";
    /**
     * The values that store the users basic data.
     */
    /** The amount of gold the player has*/
    private int goldCount = 0;
    /** The amount of gems the player has*/
    private int gemCount = 0;
    /** The amount of picks the player has*/
    private int pickCount = 0;
    /** The players current level:
     *  The whole number portion is displayed to the user
     *  The decimal portion is used to track their progress towards their next level and make the xp bar*/
    private float playerLvl = 0;
    /** The total number of times the user has clicked the rock
     * Used to set the player level*/
    private int totalClicks = 0;
    /**
     * Reference to various output text views
     */
    private TextView goldText;
    private TextView gemText;
    private TextView pickText;
    private TextView playerText;
    private ProgressBar xpBar;

    /** The number of fingers currently pressing the screen */
    private int fingerCount;

    /**A Random number generator*/
    private final Random rand = new Random();

    /**The users featured rock*/
    private ImageView featuredRock;
    /**The id of the users featured rock*/
    private int featuredRockId;
    /** If (1): developer mode is enabled. If (0): developer mode is disabled.*/
    private int devMode;
    /** If (1): dark mode is enabled. If (0): dark is disabled.*/
    private int darkMode;
    /** If (1): ads are disabled. If (0): ads are enabled.*/
    private int disableAds;

    /**The number of times in a row that the user has clicked the text "Player lvl" that is displayed in the upper left corner of the main page.
     *  If the user clicks it 10 times in a row dev mode will be enabled
     */
    private int clicksTowardsEnablingDevMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.preference, false);
        featuredRock = findViewById(R.id.featuredRock);
        // multitouch
        featuredRock.setOnTouchListener(fingerCounterListener);
        initializeItems();
        try {
            setup();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        if (disableAds == 0) {
            MobileAds.initialize(this, initializationStatus -> {
            });
            AdView mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

        if (darkMode == 1)
            setDefaultNightMode(MODE_NIGHT_YES);
        else
            setDefaultNightMode(MODE_NIGHT_NO);


        updateDisplay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        updatePrefs();
    }


    @Override
    protected void onPause() {
        super.onPause();
        updatePrefs();
    }

    /**
     * Loads in the collection of all rocks
     * Loads in the rocks owned by the user
     * @throws IOException
     */
    private void setup() throws IOException {
        // Load in all rocks
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

        //Load in all the rocks the user owns

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

    /**
     * Creates an onTouchListener that lets the user use multiple appendages at a time to press the rock.
     * Increments the gold, gem, and clickCounters on every click.
     */
    private final View.OnTouchListener fingerCounterListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int additionalGold = 0;
            int additionalGems = 0;
            float chance = rand.nextFloat();

            //Adds a random chance to receive extra gold upon clicking the rock
            if (chance <= playerLvl / 100) {
                if (playerLvl >= 100)
                    additionalGold = Math.round(playerLvl / 100);
                else
                    additionalGold = 1;
            }
            //Adds a random chance to receive gem upon clicking the rock
            if (chance <= rocks.getGemChance(featuredRockId)) {
                additionalGems = rocks.getGemAmount(featuredRockId) * rocksOwned.getRockAmount(featuredRockId);
            }

            if (goldCount < 0)
                goldCount = 0;
            if (fingerCount < 0)
                fingerCount = 0;
            //Adds support for multiple fingers. Increments the users gold, gems, and totalClicks.
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    fingerCount += 1;
                    goldCount += 1 + additionalGold;
                    gemCount += additionalGems;
                    totalClicks += 1;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    goldCount += 1 + additionalGold;
                    gemCount += additionalGems;
                    fingerCount += 1;
                    totalClicks += 1;
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


    /**
     * Reloads the page upon resuming.
     * Implemented to fix a bug where in a user could hit the back button to go to versions of the main page with out dated data.
     */
    @Override
    public void onResume() {
        super.onResume();
        initializeItems();
        updateDisplay();
    }

    /**
     *Initializes the text views, the counters, and then updates the display
     */
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
        featuredRockId = prefs.getInt(FEATURED_ROCK_ID, featuredRockId);
        darkMode = prefs.getInt(DARK_PREF, darkMode);
        devMode = prefs.getInt(DEV_MODE_PREF, devMode);
        disableAds = prefs.getInt(DISABLE_ADS, disableAds);
        updateDisplay();
    }


    /**
     * Updates the player lvl, xp bar, gold text, gem text, player level, and the displayed rock.
     */
    private void updateDisplay() {
        playerLvl = (float) (Math.sqrt(totalClicks) / 10);
        xpBar.setProgress(Math.round(playerLvl % 1 * 100));
        //System.out.println(totalClicks);
        DecimalFormat comaFormat = new DecimalFormat("#.##########");
        comaFormat.setGroupingUsed(true);
        comaFormat.setGroupingSize(3);
        goldText.setText(comaFormat.format(goldCount));
        gemText.setText(comaFormat.format(gemCount));
        playerText.setText(String.valueOf((int) Math.floor(playerLvl)));
        // pickText.setText(String.format("%d", pickCount));
        Drawable rockDrawable = ResourcesCompat.getDrawable(getResources(), getDrawableFromId(rocks.getName(featuredRockId)), getApplicationContext().getTheme());
        featuredRock.setImageDrawable(rockDrawable);
    }

    /**
     * Gets a drawable based on the given id.
     * Used to display the the players featured rock.
     * An example of an id is: rock_chan
     * rock_chan retrieves rock_chan_icon.png
     *
     * If no drawable exist based on the given id, the method returns the resource id of Rock Chan
     * @param id    The name portion of an rock image icon
     * @return      if the drawable was found: The resource id of the drawable. Else the resource id of rock_chan_icon.png
     */
    public int getDrawableFromId(String id) {
        int resourceId;
        if (id == null) {
            resourceId = getApplicationContext().getResources().getIdentifier("rock_chan", "drawable", getApplicationContext().getPackageName());
            System.out.println("No image. Id = null in getDrawableFromId");
            return resourceId;
        }
        String name = id.toLowerCase().replaceAll(" ", "_").replaceAll("\\.", "_").replaceAll("\"", "");
        resourceId = getApplicationContext().getResources().getIdentifier(name, "drawable", getApplicationContext().getPackageName());
        if (resourceId == 0) {
            name = name + "_icon";
            resourceId = getApplicationContext().getResources().getIdentifier(name, "drawable", getApplicationContext().getPackageName());
            if (resourceId == 0)
                resourceId = getApplicationContext().getResources().getIdentifier("rock_chan", "drawable", getApplicationContext().getPackageName());
        }
        return resourceId;
    }

    /**
     * Updates all of the users stored prefs.
     */
    private void updatePrefs() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(GEMS_PREF, gemCount);
        editor.putInt(GOLD_PREF, goldCount);
        editor.putFloat(PLAYER_LVL_PREF, playerLvl);
        editor.putInt(TOTAL_CLICKS, totalClicks);
        editor.putInt(FEATURED_ROCK_ID, featuredRockId);
        editor.putInt(DEV_MODE_PREF, devMode);
        editor.putInt(DISABLE_ADS, disableAds);
        editor.apply();
    }

    /**
     * Activate dev mode if the user presses the texts that say "Player lvl" that is displayed in the upper left corner of the main page 10 times in a row.
     * Dev mode adds some new options in the settings menu.
     * Will likely be removed in full releases.
     * @param view
     */
    public void devModeClick(View view) {
        if (clicksTowardsEnablingDevMode >= 10) {
            devMode = 1;
            Toast.makeText(getApplicationContext(), "Dev Mode Activated", Toast.LENGTH_SHORT).show();
            updatePrefs();
        }
        clicksTowardsEnablingDevMode++;
    }

    /**
     * Opens the inventory page
     * @param view
     */
    public void inventoryClick(View view) {
        updatePrefs();
        startActivity(new Intent(MainActivity.this, inventory.class));
    }

    /**
     * Opens the store page
     * @param view
     */
    public void storeClick(View view) {
        updatePrefs();
        startActivity(new Intent(MainActivity.this, store.class));
    }

    /**
     * Opens the summon page
     * @param view
     */
    public void summonsClick(View view) {
        updatePrefs();
        startActivity(new Intent(MainActivity.this, summon.class));
    }

    /**
     * Opens the settings page
     * @param view
     */
    public void settingsClick(View view) {
        updatePrefs();
        startActivity(new Intent(MainActivity.this, settings.class));
    }
}