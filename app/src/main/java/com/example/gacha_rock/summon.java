package com.example.gacha_rock;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

public class summon extends AppCompatActivity {
    private static final String PREFS_NAME = "com.example.gacha_rock.prefs";
    private static final String INFINITE_LOOT_PREF = "infiniteLootPref";
    private static final String EVERYTHING_IS_FREE_PREF = "everythingIsFreePref";
    private static final String PICKS_PREF = "picksPref";
    private static final String FAST_ANIMATIONS = "fastAnimations";
    private static final String DISABLE_ANIMATIONS = "disableAnimations";
    /**
     * If (1): fast animations is enabled. If (0): fast animations is disabled.
     */
    private int fastAnimations = 1;
    /**
     * If (1): disable animations is enabled. If (0): disable animations is disabled.
     */
    private int disableAnimations;
    public rockObject<String> rocksOwned = new rockObject<>();
    public rockObject<String> rocks = new rockObject<>();
    weightedRandom<String> rarity = new weightedRandom<>();
    weightedRandom<String> rarityLegendary = new weightedRandom<>();

    private int infiniteMode;
    private int freeMode;
    private int pickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summon);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        infiniteMode = prefs.getInt(INFINITE_LOOT_PREF, infiniteMode);
        freeMode = prefs.getInt(EVERYTHING_IS_FREE_PREF, freeMode);
        pickCount = prefs.getInt(PICKS_PREF, pickCount);
        disableAnimations = prefs.getInt(DISABLE_ANIMATIONS, disableAnimations);
        fastAnimations = prefs.getInt(FAST_ANIMATIONS, fastAnimations);
        if (fastAnimations == 0) fastAnimations = 1;
        TextView pickText = findViewById(R.id.pickCountText);
        pickText.setText(String.valueOf(pickCount));
        try {
            setup();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //rarity.addEntry("Rock", 0.1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updatePrefs();
        startActivity(new Intent(summon.this, MainActivity.class));
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
                    rarityLegendary.addEntry(String.valueOf(i), overallRarity);
                    break;
            }

            rarity.addEntry(String.valueOf(i), overallRarity);
        }
        AnimatorSet set = new AnimatorSet();
        List<Animator> animatorList = new ArrayList<>();
        ConstraintLayout layout = findViewById(R.id.layout);
        layout.setVisibility(View.INVISIBLE);

//        TextView rockName = new TextView(this.getApplicationContext());
//        rockName.setPadding(150, 50, 150, 0);
//        layout.addView(rockName);

        ImageView pickView = new ImageView(this.getApplicationContext());
        Drawable pickDrawable = ResourcesCompat.getDrawable(getResources(), getApplicationContext().getResources().getIdentifier("summon_pick", "drawable", getApplicationContext().getPackageName()), getApplicationContext().getTheme());
        pickView.setImageDrawable(pickDrawable);
        pickView.setPadding(150, 170, 150, 0);
        layout.addView(pickView);

        ImageView bagBackView = new ImageView(this.getApplicationContext());
        bagBackView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), getApplicationContext().getResources().getIdentifier("bag_back", "drawable", getApplicationContext().getPackageName()), getApplicationContext().getTheme()));
        bagBackView.setPadding(100, 2100, 0, 0);
        layout.addView(bagBackView);

        if (count == 10) {
            count -= 1;
            int rockId = Integer.parseInt(rarityLegendary.getRandom());
            set = miningAnimation(rockId, animatorList, layout, pickView);
            rocksOwned.addEntry(rockId, rocks.getName(rockId), rocks.getRarity(rockId), rocks.getRarityOverall(rockId), rocks.getGemChance(rockId), rocks.getGemAmount(rockId), rocks.getDescription(rockId));
        }
        for (int i = 0; i < count; i++) {
            int rockId = Integer.parseInt(rarity.getRandom());
            set = miningAnimation(rockId, animatorList, layout, pickView);
            //System.out.println("rock id: " + rockId + " rock name: " + rocks.getName(rockId) + " Count: " + count);
            rocksOwned.addEntry(rockId, rocks.getName(rockId), rocks.getRarity(rockId), rocks.getRarityOverall(rockId), rocks.getGemChance(rockId), rocks.getGemAmount(rockId), rocks.getDescription(rockId));
        }

        ImageView mountainView = new ImageView(this.getApplicationContext());
        mountainView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), getApplicationContext().getResources().getIdentifier("mountain_side", "drawable", getApplicationContext().getPackageName()), getApplicationContext().getTheme()));
        mountainView.setPadding(750, 700, 0, 0);
        layout.addView(mountainView);

        ImageView bagFrontView = new ImageView(this.getApplicationContext());
        bagFrontView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), getApplicationContext().getResources().getIdentifier("bag_front", "drawable", getApplicationContext().getPackageName()), getApplicationContext().getTheme()));
        bagFrontView.setPadding(100, 2100, 0, 0);
        layout.addView(bagFrontView);
        if (disableAnimations == 0) {
            layout.setVisibility(View.VISIBLE);
            set.playSequentially(animatorList);
            set.start();
        }

        // / fastAnimations
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

    private AnimatorSet miningAnimation(int id, List<Animator> animatorList, ConstraintLayout layout, ImageView pickView) {
        ConstraintLayout constraintLayout = findViewById(R.id.summonLayout);
        //constraintLayout.setVisibility(View.INVISIBLE);
        AnimatorSet set = new AnimatorSet();

        ImageView imageView = new ImageView(this.getApplicationContext());
        Drawable rockDrawable = ResourcesCompat.getDrawable(getResources(), getDrawableFromId(rocks.getName(id)), getApplicationContext().getTheme());
        imageView.setImageDrawable(rockDrawable);
        imageView.setMaxHeight(100);
        imageView.setPadding(1000, 2300, 0, 0);
        imageView.setVisibility(View.VISIBLE);
        layout.addView(imageView);

        int target = 3;
        if (fastAnimations == 2) target = 1;

        for (int i = 0; i < target; i++) {
            animatorList.add(rotateImage(pickView, 0f, 60f, 180));
            animatorList.add(rotateImage(pickView, 60f, 0f, 180));
        }
        animatorList.add(translateImage(imageView, "Y", 0, -1000, 0));
        animatorList.add(translateImage(imageView, "X", -100, -500, 200));
        animatorList.add(translateImage(imageView, "Y", -1000, 0, 500));

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                layout.setVisibility(View.INVISIBLE);
                constraintLayout.setVisibility(View.VISIBLE);
                layout.removeView(imageView);
                layout.removeView(pickView);
            }

            public void onAnimationStart(Animator animation) {
                super.onAnimationEnd(animation);
                layout.setVisibility(View.VISIBLE);
                constraintLayout.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.VISIBLE);
                pickView.setVisibility(View.VISIBLE);
            }
        });
        return set;
    }

    public ObjectAnimator rotateImage(ImageView image, float start, float end, int duration) {
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(image, "rotation", start, end).setDuration(duration / fastAnimations);
        rotateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        return rotateAnimator;
    }

    public ObjectAnimator translateImage(ImageView image, String axis, float start, float end, int duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(image, "translation" + axis, start, end).setDuration(duration / fastAnimations);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        return animator;
    }

    public int getDrawableFromId(String id) {
        String name = id.toLowerCase().replaceAll(" ", "_").replaceAll("\\.", "_").replaceAll("\"", "") + "_icon";
        int resourceId = getApplicationContext().getResources().getIdentifier(name, "drawable", getApplicationContext().getPackageName());
        if (resourceId == 0)
            resourceId = getApplicationContext().getResources().getIdentifier("rock_chan_icon", "drawable", getApplicationContext().getPackageName());
        return resourceId;
    }
}