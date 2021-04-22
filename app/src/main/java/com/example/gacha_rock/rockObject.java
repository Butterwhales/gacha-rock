package com.example.gacha_rock;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class rockObject<rockObject> {
    private class Rock {
        int id;
        int amount;
        rockObject object;
        double rarity;
        double rarityOverall;
        String description;
        double gemChance;
        int gemAmount;
    }

    private final ArrayList<Rock> rocks = new ArrayList<>();

    /**
     * Adds rock to list
     *
     * @param id          Rock ID
     * @param object      Rock Object
     * @param rarity      Rock Rarity
     * @param description Rock Description
     * @param gemChance   Additional Chance to get a gem because of the rock
     * @param gemAmount   Additional Amount of gems gained by the gem chance
     */
    public void addEntry(int id, rockObject object, double rarityOverall, double rarity, double gemChance, int gemAmount, String description) {
        boolean isThere = false;
        for (Rock rock : rocks) {
            if (rock.id == id) {
                isThere = true;
                rock.amount += 1;
                //System.out.println(rock.object +": " + rock.amount);
            }
        }

        if (!isThere) {
            Rock rock = new Rock();
            rock.id = id;
            rock.amount = 1;
            rock.object = object;
            rock.rarity = rarity;
            rock.rarityOverall = rarityOverall;
            rock.description = description;
            rock.gemChance = gemChance;
            rock.gemAmount = gemAmount;
            rocks.add(rock);
        }
    }

    /**
     * Get Rock ID
     *
     * @param name Name of Rock
     * @return Id of rock or -1 if not found
     */
    public int getId(String name) {
        for (Rock rock : rocks) {
            if (rock.object.equals(name)) return rock.id;
        }
        return -1;
    }

    /**
     * Get Rock Name
     *
     * @param id Id of Rock
     * @return Name of rock or null if not found
     */
    public String getName(int id) {
        for (Rock rock : rocks) {
            if (rock.id == id) return (String) rock.object;
        }
        return null;
    }

    /**
     * Get Rock rarity
     *
     * @param id Id of Rock
     * @return rarity of rock or -1 if not found
     */
    public double getRarity(int id) {
        for (Rock rock : rocks) {
            if (rock.id == id) return rock.rarity;
        }
        return -1;
    }

    /**
     * Get Rock rarity overall. Ex. 0 = Neutral
     *
     * @param id Id of Rock
     * @return rarity overall of rock or -1 if not found
     */
    public double getRarityOverall(int id) {
        for (Rock rock : rocks) {
            if (rock.id == id) return rock.rarityOverall;
        }
        return -1;
    }

    /**
     * Get Rock Description
     *
     * @param id Id of Rock
     * @return Description of rock or null if not found
     */
    public String getDescription(int id) {
        for (Rock rock : rocks) {
            if (rock.id == id) return rock.description;
        }
        return null;
    }

    /**
     * Get amount of rocks in array
     *
     * @return Amount of rocks in array
     */
    public int getNumRocks() {
        return rocks.size();
    }

    /**
     * Get gem chance
     *
     * @param id Id of rock
     * @return Gem Chance
     */
    public double getGemChance(int id) {
        for (Rock rock : rocks) {
            if (rock.id == id) return rock.gemChance;
        }
        return -1;
    }

    /**
     * Get gem amount
     *
     * @param id Id of rock
     * @return Gem amount
     */
    public int getGemAmount(int id) {
        for (Rock rock : rocks) {
            if (rock.id == id) return rock.gemAmount;
        }
        return -1;
    }

    /**
     * Get amount of rocks in array
     *
     * @return Amount of rocks in array
     */
    public int getRockAmount(int id) {
        for (Rock rock : rocks) {
            if (rock.id == id) {
                //System.out.println(rock.id + ": " + rock.amount);
                return rock.amount;
            }
        }
        return -1;
    }

    public ArrayList<Integer> getAllRocksIds() {
        ArrayList<Integer> ids = new ArrayList<>();
        rocks.forEach(object -> ids.add(rocks.get(rocks.indexOf(object)).id));
        return ids;
    }

    /**
     * Writes all owned rocks to file
     */
    public void writeAll() throws IOException {
        String File = "rocks_owned.txt";
        String path = MyApplication.getContext().getFilesDir().getPath();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "/" + File), StandardCharsets.UTF_8));
        rocks.forEach(rock -> {
            for (int i = 0; i < rock.amount; i++) {
                try {
                    bw.write(rocks.get(rocks.indexOf(rock)).id + "\n");
                    //System.out.println("wrote: " + rocks.get(rocks.indexOf(rock)).id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        bw.close();
    }

    /**
     * Clears all rocks in file
     */
    public void clearAll() throws IOException {
        String File = "rocks_owned.txt";
        String path = MyApplication.getContext().getFilesDir().getPath();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "/" + File), StandardCharsets.UTF_8));
        bw.write("");
        bw.close();
    }
}
