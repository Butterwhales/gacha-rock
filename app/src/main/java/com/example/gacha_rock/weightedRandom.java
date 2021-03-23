package com.example.gacha_rock;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Robust weighted collection based off https://gamedev.stackexchange.com/a/162987

public class weightedRandom<randObject extends Object>{
    private class Entry {
        double accumulatedWeight;
        randObject object;
    }

    private List<Entry> entries  = new ArrayList<>();
    private double accumulatedWeight;
    private Random rand = new Random();

    public void addEntry(randObject object, double weight) {
        accumulatedWeight += weight;
        Entry entry = new Entry();
        entry.object = object;
        entry.accumulatedWeight = accumulatedWeight;
        entries.add(entry);
    }

    public randObject getRandom() {
        double r = rand.nextDouble() * accumulatedWeight;

        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).accumulatedWeight >= r) {
                return entries.get(i).object;
            }
        }
        return null; //should only happen when there are no entries
    }
}