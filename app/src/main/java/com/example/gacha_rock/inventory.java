package com.example.gacha_rock;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

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
        buildGrid();
    }

    private void setup() throws IOException {
        InputStream stream = getResources().openRawResource(R.raw.rocks);
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String description = " ";
        while (br.ready()) {
            String[] e = br.readLine().split(" \\| ");
            //System.out.println(Arrays.toString(e));
            if (e.length == 4) {
                description = " ";
                e[3] = e[3].replace(" |", "");
            } else
                description = e[4];
            rocks.addEntry(Integer.parseInt(e[0]), e[1], Double.parseDouble(e[2]), Double.parseDouble(e[3]), description);
        }
        stream.close();
        br.close();

        String File = "rocks_owned.txt";
        String path = getApplicationContext().getFilesDir().getPath();
        br = new BufferedReader(new InputStreamReader(new FileInputStream(path + "/" + File), StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine()) != null) {
            int e = Integer.parseInt(line);
            System.out.println(rocks.getName(e));
            rocksOwned.addEntry(e, rocks.getName(e), rocks.getRarity(e), rocks.getRarityOverall(e), rocks.getDescription(e));
        }
        br.close();
    }

    private void buildGrid() {
        GridLayout grid = findViewById(R.id.grid);
        ArrayList<Integer> ids = rocksOwned.getAllRocksIds();
        ids.forEach(id -> {
            grid.addView(makeView(rocksOwned.getName(id), String.valueOf(rocksOwned.getRockAmount(id))));
        });
    }

    private TextView makeView(String number, String amount) {
        TextView view = new TextView(this.getApplicationContext());
        view.setText(String.format("%s %s", number, amount));
        view.setPadding(getResources().getDimensionPixelOffset(R.dimen.leftPadding), 0, getResources().getDimensionPixelSize(R.dimen.rightPadding), 0);
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.grid_text_size));
        view.setTextColor(Color.WHITE);
        return view;
    }

    public void backClick(View view) throws IOException {
        startActivity(new Intent(inventory.this, MainActivity.class));
        rocksOwned.writeAll(); //Doesn't work
    }
}