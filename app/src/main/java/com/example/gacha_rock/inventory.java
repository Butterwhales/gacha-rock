package com.example.gacha_rock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.gridlayout.widget.GridLayout;

public class inventory extends AppCompatActivity {
    private static final String PREFS_NAME = "com.example.gacha_rock.prefs";
    private static final String FEATURED_ROCK_ID = "featuredRockId";
    public rockObject<String> rocksOwned = new rockObject<>();
    public rockObject<String> rocks = new rockObject<>();
    private int featuredRockId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        featuredRockId = prefs.getInt(FEATURED_ROCK_ID, featuredRockId);
        try {
            setup();
        } catch (IOException e) {
            e.printStackTrace();
        }
        buildGrid();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(FEATURED_ROCK_ID, featuredRockId);
        editor.apply();
    }


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(FEATURED_ROCK_ID, featuredRockId);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(FEATURED_ROCK_ID, featuredRockId);
        editor.apply();
        try {
            rocksOwned.writeAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(inventory.this, MainActivity.class));
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

    private void buildGrid() {
        GridLayout grid = findViewById(R.id.grid);
        ArrayList<Integer> ids = rocksOwned.getAllRocksIds();
        ids.forEach(id -> grid.addView(makeView(rocksOwned.getName(id), String.valueOf(rocksOwned.getRockAmount(id)))));
    }

    private LinearLayout makeView(String rockName, String amount) {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.textColor, typedValue, true);
        int color = typedValue.data;

        LinearLayout layout = new LinearLayout(this.getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        TextView textView = new TextView(this.getApplicationContext());
        textView.setText(String.format("%s", rockName));
        textView.setPadding(getResources().getDimensionPixelOffset(R.dimen.leftPadding), 0, getResources().getDimensionPixelSize(R.dimen.rightPadding), 0);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.grid_text_size));
        textView.setTextColor(color);
        layout.addView(textView);

        TextView textView2 = new TextView(this.getApplicationContext());
        textView2.setText(String.format("%s", amount));
        textView2.setPadding(getResources().getDimensionPixelOffset(R.dimen.leftPadding), 0, getResources().getDimensionPixelSize(R.dimen.rightPadding), 0);
        textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.grid_text_size));
        textView2.setTextColor(color);
        layout.addView(textView2);

        ImageView imageView = new ImageView(this.getApplicationContext());
        Drawable rockDrawable = ResourcesCompat.getDrawable(getResources(), getDrawableFromId(rockName), getApplicationContext().getTheme());
        imageView.setImageDrawable(rockDrawable);
        imageView.setMaxHeight(100);
        imageView.setId(rocksOwned.getId(rockName));
        imageView.setPadding(0, 0, 0, 100);
        imageView.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "Set favorite rock to " + rocks.getName(v.getId()), Toast.LENGTH_SHORT).show();
            featuredRockId = v.getId();
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(FEATURED_ROCK_ID, featuredRockId);
            editor.apply();
        });
        layout.addView(imageView);

        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        return layout;
    }

    public int getDrawableFromId(String id) {
        String name = id.toLowerCase().replaceAll(" ", "_") .replaceAll("\\.", "_").replaceAll("\"","") + "_icon";
        int resourceId = getApplicationContext().getResources().getIdentifier(name, "drawable", getApplicationContext().getPackageName());
        if (resourceId == 0)
            resourceId = getApplicationContext().getResources().getIdentifier("rock_chan_icon", "drawable", getApplicationContext().getPackageName());
        return resourceId;
    }
}