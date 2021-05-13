package com.example.gacha_rock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class store extends AppCompatActivity {
    /**
     * The file address of the users stored preferences
     */
    private static final String PREFS_NAME = "com.example.gacha_rock.prefs";

    /**
     * The names of the preferences used to store the users data
     */
    private static final String GOLD_PREF = "goldPref";
    private static final String GEMS_PREF = "gemsPref";
    private static final String PICKS_PREF = "picksPref";

    private TextView goldText;
    private TextView gemText;
    private TextView pickText;

    private int goldCount = 0;
    private int gemCount = 0;
    private int pickCount = 0;
    DecimalFormat comaFormat = new DecimalFormat("#.##########");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        comaFormat.setGroupingUsed(true);
        comaFormat.setGroupingSize(3);
        initializeItems();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updatePrefs();
        startActivity(new Intent(store.this, MainActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeItems();
        updateDisplay();
    }

    private void initializeItems() {
        goldText = findViewById(R.id.goldText);
        gemText = findViewById(R.id.gemText);
        pickText = findViewById(R.id.pickText);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        goldCount = prefs.getInt(GOLD_PREF, goldCount);
        gemCount = prefs.getInt(GEMS_PREF, gemCount);
        pickCount = prefs.getInt(PICKS_PREF, pickCount);

        updateDisplay();
    }

    private void updateDisplay() {
        goldText.setText(comaFormat.format(goldCount));
        gemText.setText(comaFormat.format(gemCount));
        pickText.setText(comaFormat.format(pickCount));
    }

    public void buttonClick(View view) {
        String buttonName = String.valueOf(view.getTag());
        char character = buttonName.charAt(10);
        AlertDialog confirmMenu = null;
        if (buttonName.contains("cash")) {
            switch (character) {
                case '1':
                    confirmMenu = createDialog("dollars", 0.99, 300, "gems");
                    break;
                case '4':
                    confirmMenu = createDialog("dollars", 4.99, 1800, "gems");
                    break;
                case '6':
                    confirmMenu = createDialog("dollars", 9.99, 4200, "gems");
                    break;
                case '2':
                    confirmMenu = createDialog("dollars", 24.99, 12345, "gems");
                    break;
                case '5':
                    confirmMenu = createDialog("dollars", 49.99, 28000, "gems");
                    break;
                case '7':
                    confirmMenu = createDialog("dollars", 74.99, 70000, "gems");
                    break;
                case '3':
                    confirmMenu = createDialog("dollars", 99.99, 47000, "gems");
                    break;
            }
        } else if (buttonName.contains("pick")) {
            switch (character) {
                case '1':
                    if (gemCount >= 1000) {
                        confirmMenu = createDialog("gems", (double) 1000, 1, "picks");
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gems, Need " + comaFormat.format(1000 - gemCount) + " Gems", Toast.LENGTH_SHORT).show();
                    break;
                case '4':
                    if (gemCount >= 4875) {
                        confirmMenu = createDialog("gems", (double) 4875, 5, "picks");
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gems, Need " + comaFormat.format(4875 - gemCount) + " Gems", Toast.LENGTH_SHORT).show();
                    break;
                case '6':
                    if (gemCount >= 9250) {
                        confirmMenu = createDialog("gems", (double) 9250, 10, "picks");
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gems, Need " + comaFormat.format(9250 - gemCount) + " Gems", Toast.LENGTH_SHORT).show();
                    break;
                case '2':
                    if (gemCount >= 17000) {
                        confirmMenu = createDialog("gems", (double) 17000, 20, "picks");
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gems, Need " + comaFormat.format(17000 - gemCount) + " Gems", Toast.LENGTH_SHORT).show();
                    break;
                case '5':
                    if (gemCount >= 37500) {
                        confirmMenu = createDialog("gems", (double) 37500, 50, "picks");
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gems, Need " + comaFormat.format(37500 - gemCount) + " Gems", Toast.LENGTH_SHORT).show();
                    break;
                case '7':
                    if (gemCount >= 65000) {
                        confirmMenu = createDialog("gems", (double) 65000, 100, "picks");
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gems, Need " + comaFormat.format(65000 - gemCount) + " Gems", Toast.LENGTH_SHORT).show();
                    break;
                case '3':
                    if (gemCount >= 105000) {
                        confirmMenu = createDialog("gems", (double) 105000, 200, "picks");
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gems, Need " + comaFormat.format(105000 - gemCount) + " Gems", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else if (buttonName.contains("gems")) {
            switch (character) {
                case '1':
                    if (goldCount >= 12345) {
                        confirmMenu = createDialog("gold", (double) 12345, 1000, "gems");
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gold, Need " + comaFormat.format(12345 - goldCount) + " Gold", Toast.LENGTH_SHORT).show();
                    break;
                case '4':
                    if (goldCount >= 61725) {
                        confirmMenu = createDialog("gold", (double) 61725, 4875, "gems");
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gold, Need " + comaFormat.format(61725 - goldCount) + " Gold", Toast.LENGTH_SHORT).show();
                    break;
                case '6':
                    if (goldCount >= 123450) {
                        confirmMenu = createDialog("gold", (double) 123450, 9250, "gems");
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gold, Need " + comaFormat.format(123450 - goldCount) + " Gold", Toast.LENGTH_SHORT).show();
                    break;
                case '2':
                    if (goldCount >= 246900) {
                        confirmMenu = createDialog("gold", (double) 246900, 17000, "gems");
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gold, Need " + comaFormat.format(246900 - goldCount) + " Gold", Toast.LENGTH_SHORT).show();
                    break;
                case '5':
                    if (goldCount >= 617250) {
                        confirmMenu = createDialog("gold", (double) 617250, 37500, "gems");
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gold, Need " + comaFormat.format(617250 - goldCount) + " Gold", Toast.LENGTH_SHORT).show();
                    break;
                case '7':
                    if (goldCount >= 1234500) {
                        confirmMenu = createDialog("gold", (double) 1234500, 65000, "gems");
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gold, Need " + comaFormat.format(1234500 - goldCount) + " Gold", Toast.LENGTH_SHORT).show();
                    break;
                case '3':
                    if (goldCount >= 2469000) {
                        confirmMenu = createDialog("gold", (double) 2469000, 105000, "gems");
                    } else
                        Toast.makeText(getApplicationContext(), "Not Enough Gold, Need " + comaFormat.format(2469000 - goldCount) + " Gold", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            //disable ads
        }
        if (confirmMenu != null) {
            confirmMenu.show();
        }
        updateDisplay();
    }

    /**
     * Adds rock to list
     *
     * @param purchaseMedium What you purchase with (0-2)
     * @param purchaseAmount Cost
     */

    private AlertDialog createDialog(String purchaseMedium, Double purchaseAmount, int productAmount, String productMedium) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String purchaseString = comaFormat.format((int) Math.round(purchaseAmount));
        if (purchaseMedium == "dollars") purchaseString = "$" + purchaseAmount;
        builder.setMessage("Confirm you want to spend " + purchaseString + " " + purchaseMedium + " for " + comaFormat.format(productAmount) + " " + productMedium)
                .setTitle("Buy " + productMedium)
                .setPositiveButton("Confirm", (dialog, id) -> {
                    switch (purchaseMedium) {
                        case "dollars":
                            gemCount += productAmount;
                            Toast.makeText(getApplicationContext(), "You Spent " + purchaseAmount, Toast.LENGTH_SHORT).show();
                            break;
                        case "gems":
                            pickCount += productAmount;
                            gemCount -= purchaseAmount;
                            Toast.makeText(getApplicationContext(), "Bought " + productAmount + " Pick", Toast.LENGTH_SHORT).show();
                            break;
                        case "gold":
                            gemCount += productAmount;
                            goldCount -= purchaseAmount;
                            Toast.makeText(getApplicationContext(), "Bought " + comaFormat.format(productAmount) + " Gems", Toast.LENGTH_SHORT).show();
                            break;
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    // CANCEL
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void updatePrefs() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(GEMS_PREF, gemCount);
        editor.putInt(GOLD_PREF, goldCount);
        editor.putInt(PICKS_PREF, pickCount);
        editor.apply();
    }
}