package com.example.gacha_rock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import androidx.appcompat.app.AppCompatActivity;

public class settings extends AppCompatActivity {
    public rockObject<String> rocksOwned = new rockObject<>();
    public rockObject<String> rocks = new rockObject<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
            //System.out.println(e);
            rocksOwned.addEntry(e, rocks.getName(e), rocks.getRarity(e), rocks.getRarityOverall(e), rocks.getDescription(e));
        }
        br.close();
    }

    public void clearClick(View view) throws IOException {
        rocksOwned.clearAll();
    }

    public void backClick(View view) {
        startActivity(new Intent(settings.this, MainActivity.class));
    }
}