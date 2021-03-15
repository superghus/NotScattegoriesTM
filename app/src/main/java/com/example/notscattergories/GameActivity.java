package com.example.notscattergories;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * A class for activity_game.xml
 */
public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView timerView; //Store the Timer text view
    private TextView letterView; //Store letterView
    private ProgressBar progressBar; //Store ProgressBar

    private Button btnPlayers; //Store the Players button
    private Button btnPlayPause; //Store the Play/Pause Button
    private Button btnRestart; //Store the restart Button
    private Button btnSettings; //Store the settings button

    private LinearLayout categoryView; //LinearLayout to store list of TextViews as categories.

    private ArrayList<String> allCategories; //Stores all categories from file.

    private Timer timer; //Stores a timer object.

    private final int GAME_TIME = 60000;

    /**
     * A method that is called when activity is created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        timerView = findViewById(R.id.countDownTimer);
        timerView.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        letterView = findViewById(R.id.letterView);
        letterView.setOnClickListener(this);

        allCategories = new ArrayList<>(); //Stores all categories from the categories.txt

        categoryView = findViewById(R.id.categoryLayoutView);

        btnPlayers = findViewById(R.id.btnPlayers);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnRestart = findViewById(R.id.btnRestart);
        btnSettings = findViewById(R.id.btnSettings);

        btnPlayers.setOnClickListener(this);
        btnPlayPause.setOnClickListener(this);
        btnRestart.setOnClickListener(this);
        btnSettings.setOnClickListener(this);

        getCategoriesFromFile();
        clearAllViews(); //Ensures consistency in apps display
    }

    /**
     * A method to listen for button presses, and perform actions based on that.
     * @param v The view being pressed.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.countDownTimer:
                break;
            case R.id.letterView:
                break;
            case R.id.btnPlayers:
                //Show player info
                break;
            case R.id.btnPlayPause:
                //Used to start, play, and pause the timer.
                if(!gameInProgress()){
                    startGame(GAME_TIME);
                }
                else if(timer.isRunning()){
                        timer.pause();
                    } else {
                        timer.resume();
                    }
                break;
            case R.id.btnRestart:
                //Used to be start
                if (timer != null) {
                    timer.restart();
                    clearAllViews();

                }
                break;
            case R.id.btnSettings:
                //open Settings popup
                Intent settingsPop = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsPop);
                break;
            default:
                break;
        }
    }

    /**
     * A method to start a game of NotScattegories.
     * @param time The length of the game in milliseconds.
     */
    private void startGame(int time) {
        if (timer == null) {
            //Create a new timer object if one does not exist. Timer will be null if it has finished.
            timer = new Timer(time, timerView, progressBar, this, btnPlayPause);
        }

        if (!timer.isRunning()) {
            //If game is not running, start a new game.
            Game game = new Game(7, allCategories.size());
            game.start();

            displayLetter(game.getLetter());
            displayCategories(game.getCategoryIndexes());
            timer.start();
        }
    }

    /**
     * A method to clear all views. This allows for user consistency.
     */
    private void clearAllViews(){
        timerView.setText("_");
        letterView.setText("_");
        categoryView.removeAllViews();
    }

    /**
     * A method to check whether a game is in progress. It checks whether the timer has finished.
     * This will return true if the game has been paused.
     * @return True if game is in progress. False if game is not in progress.
     */
    private boolean gameInProgress(){
        if (timer != null){
            return !timer.isFinished();
        } else {
            return false;
        }

    }

    /**
     * A method to get a list of categories from the categories.txt file.
     */
    private void getCategoriesFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("categories.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                allCategories.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method to display a letter in the letterView.
     * @param letter letter to be displayed.
     */
    private void displayLetter(String letter) {
        letterView.setText(letter);
    }

    /**
     * A method to display categories. Dynamically allocates to a the LinearLayout category view
     * @param categoryIndexes The indexes within the allCategories array to be displayed
     */
    private void displayCategories(int[] categoryIndexes) {
        int[] cats = categoryIndexes;
        categoryView.removeAllViews();

        for (int i=0; i<cats.length; i++) {
            TextView temp = new TextView(this);
            temp.setText(allCategories.get(cats[i]));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                temp.setTextAppearance(R.style.catText);
            }
            categoryView.addView(temp);
        }

    }


}