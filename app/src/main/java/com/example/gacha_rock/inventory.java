package com.example.gacha_rock;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private static final String FEATURED_ROCK_ID = "featuredRockId";
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
        buildGrid();
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

    private LinearLayout makeView(String number, String amount) {
        LinearLayout layout = new LinearLayout(this.getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        TextView textView = new TextView(this.getApplicationContext());
        textView.setText(String.format("%s", number));
        textView.setPadding(getResources().getDimensionPixelOffset(R.dimen.leftPadding), 0, getResources().getDimensionPixelSize(R.dimen.rightPadding), 0);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.grid_text_size));
        textView.setTextColor(Color.WHITE);
        layout.addView(textView);

        ImageView imageView = new ImageView(this.getApplicationContext());
        Drawable rockDrawable = ResourcesCompat.getDrawable(getResources(), getDrawableFromId(number), getApplicationContext().getTheme());
        imageView.setImageDrawable(rockDrawable);
        imageView.setMaxHeight(100);
        layout.addView(imageView);

        TextView textView2 = new TextView(this.getApplicationContext());
        textView2.setText(String.format("%s", amount));
        textView2.setPadding(getResources().getDimensionPixelOffset(R.dimen.leftPadding), 0, getResources().getDimensionPixelSize(R.dimen.rightPadding), 0);
        textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.grid_text_size));
        textView2.setTextColor(Color.WHITE);
        layout.addView(textView2);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        return layout;
    }

    public int getDrawableFromId(String id) {
        String name = id.toLowerCase().replaceAll(" ", "_").replaceAll("\\.", "_") + "_icon";
        int resourceId = getApplicationContext().getResources().getIdentifier(name, "drawable", getApplicationContext().getPackageName());
        if (resourceId == 0)
            resourceId = getApplicationContext().getResources().getIdentifier("rock_chan_icon", "drawable", getApplicationContext().getPackageName());
        return resourceId;
    }

    public void backClick(View view) throws IOException {
        startActivity(new Intent(inventory.this, MainActivity.class));
        rocksOwned.writeAll();
    }
}