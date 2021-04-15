package com.example.gacha_rock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import androidx.appcompat.app.AppCompatActivity;

public class rockList extends AppCompatActivity {
    public rockObject<String> rocksOwned = new rockObject<>();
    public rockObject<String> rocks = new rockObject<>();
//        rockList rockList = new rockList();
//        try {
//            rockList.setup();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    /**
     * Adds all rocks to a list and adds owned rocks to a list.
     *
     * @throws IOException
     */
    public void setup() throws IOException {
        InputStream stream = getResources().openRawResource(R.raw.rocks);
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        while (br.ready()) {
            String[] e = br.readLine().split("/ | +/");
            rocks.addEntry(Integer.parseInt(e[0]), e[1], Double.parseDouble(e[2]), Double.parseDouble(e[3]), e[4]);
        }
        br.close();

        //stream = getResources().openRawResource(R.raw.rocks_owned);
        //br = new BufferedReader(new InputStreamReader(stream));
        //while (br.ready()) {
        //    int e = Integer.parseInt(br.readLine());
         //   rocksOwned.addEntry(e, rocks.getName(e), rocks.getRarity(e), rocks.getRarityOverall(e), rocks.getDescription(e));
        //}
        //br.close();
    }

    /**
     * Writes Owned rocks to file
     *
     * @throws IOException
     */
    public void close() throws IOException {
        rocks.writeAll();
    }
}
