package com.example.gacha_rock;

import java.util.ArrayList;
import java.util.List;

public class rockMasterList<rockObject extends Object> {
    private class Rock {
        double rarity;
        int id;
        rockObject object;
        String description;
    }

    private List<Rock> rocks = new ArrayList<>();

    /**Adds rock to list
     * @param id
     * @param object
     * @param rarity
     * @param description
     */
    public void addEntry(int id, rockObject object, double rarityOverall, double rarity, String description) {
        Rock rock = new Rock();
        rock.id = id;
        rock.object = object;
        rock.rarity = rarity + rarityOverall;
        rock.description = description;
        rocks.add(rock);
    }

    //TODO: figure out what to return for getPicture

    /**
     * Get picture id
     */
    public void getPicture() {

    }

    /**
     *
     */
    public void hasRock() {

    }

}
