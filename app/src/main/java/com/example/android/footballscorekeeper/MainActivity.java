package com.example.android.footballscorekeeper;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements StopWatchInterface, NumberPicker.OnValueChangeListener {

    // Additional time picker based on list picker alert dialog http://stackoverflow.com/questions/17944061/how-to-use-number-picker-with-dialog

    public static int additionalTime = 0;
    private static Dialog myDialog;
    private int scoreTeamA = 0;
    private int amountOfYellowCardsTeamA = 0;
    private int amountOfRedCardsTeamA = 0;
    private int scoreTeamB = 0;
    private int amountOfYellowCardsTeamB = 0;
    private int amountOfRedCardsTeamB = 0;
    private String stopwatchCurrentTime = "00:00";
    // Team A
    private TextView teamAScoreTextView;
    private TextView teamAYellowCardTextView;
    private TextView teamARedCardTextView;
    private TextView teamANameTextView;
    // Team B
    private TextView teamBScoreTextView;
    private TextView teamBYellowCardTextView;
    private TextView teamBRedCardTextView;
    private TextView teamBNameTextView;
    // Stopwatch
    private TextView stopwatchTextView;
    private boolean isRunning = false;
    private TimeCalculator stopWatch;
    // Status of buttons
    private boolean statusOfButtons = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // *** RECOVERING THE INSTANCE STATE
        if (savedInstanceState != null) {
            // Team A
            scoreTeamA = savedInstanceState.getInt("TEAM_A_SCORE");
            amountOfYellowCardsTeamA = savedInstanceState.getInt("TEAM_A_YELLOW_CARD");
            amountOfRedCardsTeamA = savedInstanceState.getInt("TEAM_A_RED_CARD");
            // Team B
            scoreTeamB = savedInstanceState.getInt("TEAM_B_SCORE");
            amountOfYellowCardsTeamB = savedInstanceState.getInt("TEAM_B_YELLOW_CARD");
            amountOfRedCardsTeamB = savedInstanceState.getInt("TEAM_B_RED_CARD");
            // Stopwatch
            stopwatchCurrentTime = savedInstanceState.getString("STOPWATCH_TIMER");
            statusOfButtons = savedInstanceState.getBoolean("STATUS_OF_BUTTONS");
            additionalTime = savedInstanceState.getInt("ADDITIONAL_TIME");
        }

        setContentView(R.layout.activity_main);

        // *** DECLARATION TEXTVIEWS SO WE CAN MANIPULATE THEM LATER
        // Team A
        teamAScoreTextView = (TextView) findViewById(R.id.team_a_score);
        teamAYellowCardTextView = (TextView) findViewById(R.id.team_a_yellow_card);
        teamARedCardTextView = (TextView) findViewById(R.id.team_a_red_card);
        teamANameTextView = (TextView) findViewById(R.id.team_a_name);
        // Team B
        teamBScoreTextView = (TextView) findViewById(R.id.team_b_score);
        teamBYellowCardTextView = (TextView) findViewById(R.id.team_b_yellow_card);
        teamBRedCardTextView = (TextView) findViewById(R.id.team_b_red_card);
        teamBNameTextView = (TextView) findViewById(R.id.team_b_name);
        // Stopwatch
        stopwatchTextView = (TextView) findViewById(R.id.stopwatch);

        // *** Creating a new custom Typeface
        // For digits of counters
        Typeface counterCustomFont = Typeface.createFromAsset(getAssets(), "fonts/KARNIVOD.ttf");
        teamAScoreTextView.setTypeface(counterCustomFont);
        teamAYellowCardTextView.setTypeface(counterCustomFont);
        teamARedCardTextView.setTypeface(counterCustomFont);
        teamBScoreTextView.setTypeface(counterCustomFont);
        teamBYellowCardTextView.setTypeface(counterCustomFont);
        teamBRedCardTextView.setTypeface(counterCustomFont);
        stopwatchTextView.setTypeface(counterCustomFont);
        // For names of teams
        Typeface teamNameCustomFont = Typeface.createFromAsset(getAssets(), "fonts/KARNIVOF.ttf");
        teamANameTextView.setTypeface(teamNameCustomFont);
        teamBNameTextView.setTypeface(teamNameCustomFont);

        // Activation other buttons on the screen
        enableButtons(statusOfButtons);

        // Opening dialog by clicking on button for adding additional time for stopwatch
        ImageButton additionalTimeButton = (ImageButton) findViewById(R.id.addTimeButton);

        additionalTimeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                show();
            }
        });
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        Log.i("value is", "" + newVal);

    }

    public void show() {

        myDialog = new Dialog(MainActivity.this);
        myDialog.setTitle("NumberPicker");
        myDialog.setContentView(R.layout.dialog);
        Button buttonSet = (Button) myDialog.findViewById(R.id.button1);
        Button buttonCancel = (Button) myDialog.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) myDialog.findViewById(R.id.numberPicker1);
        np.setMaxValue(30); // max value 30
        np.setMinValue(0);   // min value 0
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                additionalTime = (Integer.parseInt(String.valueOf(np.getValue()))); //set the value for additional time
                myDialog.dismiss();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss(); // dismiss the dialog
            }
        });
        myDialog.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Team A
        outState.putInt("TEAM_A_SCORE", scoreTeamA);
        outState.putInt("TEAM_A_YELLOW_CARD", amountOfYellowCardsTeamA);
        outState.putInt("TEAM_A_RED_CARD", amountOfRedCardsTeamA);
        // Team B
        outState.putInt("TEAM_B_SCORE", scoreTeamB);
        outState.putInt("TEAM_B_YELLOW_CARD", amountOfYellowCardsTeamB);
        outState.putInt("TEAM_B_RED_CARD", amountOfRedCardsTeamB);
        // Stopwatch
        outState.putString("STOPWATCH_TIMER", stopwatchCurrentTime);
        outState.putInt("ADDITIONAL_TIME", additionalTime);

        if (stopWatch != null) {
            outState.putBoolean("STOPWATCH_STARTED", stopWatch.IsStarted());
            outState.putLong("STOPWATCH_START_TIME", stopWatch.GetStartTime());
        } else {
            outState.putBoolean("STOPWATCH_STARTED", false);
        }

        outState.putBoolean("STATUS_OF_BUTTONS", statusOfButtons);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        if (stopWatch != null) {
            stopWatch.Stop();
        }
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        enableButtons(statusOfButtons);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Team A
        teamAScoreTextView.setText(String.valueOf(savedInstanceState.getInt("TEAM_A_SCORE")));
        teamAYellowCardTextView.setText(String.valueOf(savedInstanceState.getInt("TEAM_A_YELLOW_CARD")));
        teamARedCardTextView.setText(String.valueOf(savedInstanceState.getInt("TEAM_A_RED_CARD")));
        // Team B
        teamBScoreTextView.setText(String.valueOf(savedInstanceState.getInt("TEAM_B_SCORE")));
        teamBYellowCardTextView.setText(String.valueOf(savedInstanceState.getInt("TEAM_B_YELLOW_CARD")));
        teamBRedCardTextView.setText(String.valueOf(savedInstanceState.getInt("TEAM_B_RED_CARD")));
        // Stopwatch
        stopwatchTextView.setText(String.valueOf(savedInstanceState.getString("STOPWATCH_TIMER")));
        additionalTime = savedInstanceState.getInt("ADDITIONAL_TIME");
        if (savedInstanceState.getBoolean("STOPWATCH_STARTED")) {
            isRunning = true;
            stopWatch = new TimeCalculator(this, savedInstanceState.getLong("STOPWATCH_START_TIME"));
            stopWatch.execute();
        }

        statusOfButtons = savedInstanceState.getBoolean("STATUS_OF_BUTTONS");

    }

    // *** LOGIC TO BUTTONS AND VIEWS
    //TEAM A

    /**
     * Displays the given score for Team A.
     *
     * @param score
     */
    public void displayForTeamA(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_a_score);
        scoreView.setText(String.valueOf(score));
    }

    /**
     * Increase points by 1 for Team A
     */
    public void addOnePointsForTeamA(View v) {
        scoreTeamA = scoreTeamA + 1;
        displayForTeamA(scoreTeamA);
    }

    /**
     * @param amountYellowCard Displays the given amount of yellow cards for Team A.
     */
    public void displayYellowCardsForTeamA(int amountYellowCard) {
        TextView scoreView = (TextView) findViewById(R.id.team_a_yellow_card);
        scoreView.setText(String.valueOf(amountYellowCard));
    }

    /**
     * Increase Team A's amount of yellow cards by 1
     */
    public void addOneYellowCardForTeamA(View v) {
        amountOfYellowCardsTeamA = amountOfYellowCardsTeamA + 1;
        displayYellowCardsForTeamA(amountOfYellowCardsTeamA);
    }

    /**
     * @param amountOfRedCards Displays the given amount of red cards for Team A.
     */
    public void displayRedCardsForTeamA(int amountOfRedCards) {
        TextView scoreView = (TextView) findViewById(R.id.team_a_red_card);
        scoreView.setText(String.valueOf(amountOfRedCards));
    }

    /**
     * Increase Team A's amount of red cards by 1
     */
    public void addOneRedCardForTeamA(View v) {
        amountOfRedCardsTeamA = amountOfRedCardsTeamA + 1;
        displayRedCardsForTeamA(amountOfRedCardsTeamA);
    }

    //TEAM B

    /**
     * Displays the given score for Team B.
     *
     * @param score
     */
    public void displayForTeamB(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_b_score);
        scoreView.setText(String.valueOf(score));
    }

    /**
     * Increase points by 1 for Team B
     */
    public void addOnePointsForTeamB(View v) {
        scoreTeamB = scoreTeamB + 1;
        displayForTeamB(scoreTeamB);
    }

    /**
     * @param amountYellowCard Displays the given amount of yellow cards for Team B.
     */
    public void displayYellowCardsForTeamB(int amountYellowCard) {
        TextView scoreView = (TextView) findViewById(R.id.team_b_yellow_card);
        scoreView.setText(String.valueOf(amountYellowCard));
    }

    /**
     * Increase Team B's amount of yellow cards by 1
     */
    public void addOneYellowCardForTeamB(View v) {
        amountOfYellowCardsTeamB = amountOfYellowCardsTeamB + 1;
        displayYellowCardsForTeamB(amountOfYellowCardsTeamB);
    }

    /**
     * @param amountOfRedCards Displays the given amount of red cards for Team B.
     */
    public void displayRedCardsForTeamB(int amountOfRedCards) {
        TextView scoreView = (TextView) findViewById(R.id.team_b_red_card);
        scoreView.setText(String.valueOf(amountOfRedCards));
    }

    /**
     * Increase Team B's amount of red cards by 1
     */
    public void addOneRedCardForTeamB(View v) {
        amountOfRedCardsTeamB = amountOfRedCardsTeamB + 1;
        displayRedCardsForTeamB(amountOfRedCardsTeamB);
    }

    /**
     * Reset the counters for the both teams back to 0
     */
    public void resetScoreCounters(View v) {
        if (stopWatch != null) stopWatch.Stop();

        //Set all values to 0 by default
        scoreTeamA = 0;
        scoreTeamB = 0;
        amountOfYellowCardsTeamA = 0;
        amountOfRedCardsTeamA = 0;
        amountOfYellowCardsTeamB = 0;
        amountOfRedCardsTeamB = 0;
        stopwatchCurrentTime = "00:00";
        statusOfButtons = false;
        additionalTime = 0;

        //Display default values
        displayForTeamA(scoreTeamA);
        displayForTeamB(scoreTeamB);
        displayYellowCardsForTeamA(amountOfYellowCardsTeamA);
        displayRedCardsForTeamA(amountOfRedCardsTeamA);
        displayYellowCardsForTeamB(amountOfYellowCardsTeamB);
        displayRedCardsForTeamB(amountOfRedCardsTeamB);
        displayStopwatchTime(stopwatchCurrentTime);
        enableButtons(statusOfButtons);

    }

    /**
     * Displays stopwatch.
     *
     * @param stopwatchCurrentTime
     */
    public void displayStopwatchTime(String stopwatchCurrentTime) {
        TextView stopwatchTextView = (TextView) findViewById((R.id.stopwatch));
        stopwatchTextView.setText(stopwatchCurrentTime);
    }

    /**
     * Update stopwatch
     */
    public void updateStopwatch(View v) {
        startStopwatch();
        statusOfButtons = true;
        enableButtons(statusOfButtons);
    }

    public void startStopwatch() {
        if (!isRunning) {
            isRunning = true;
            stopWatch = new TimeCalculator(this);
            stopWatch.execute();
        }
    }

    public void reportFinish() {
        isRunning = false;
    }

    public void enableButtons(boolean status) {
        // *** BUTTONS MAPPING
        // Top Buttons
        ImageButton playButton = (ImageButton) findViewById(R.id.playButton);
        ImageButton resetButton = (ImageButton) findViewById(R.id.resetButton);
        ImageButton addTimeButton = (ImageButton) findViewById(R.id.addTimeButton);

        // Team A Buttons
        Button goalForTeamAButton = (Button) findViewById(R.id.goalForTeamAButton);
        ImageButton yellowCardAddButtonTeamA = (ImageButton) findViewById(R.id.yellow_card_add_button_team_a);
        ImageButton redCardAddButtonTeamA = (ImageButton) findViewById(R.id.red_card_add_button_team_a);

        // Team B Buttons
        Button goalForTeamBButton = (Button) findViewById(R.id.goalForTeamBButton);
        ImageButton yellowCardButtonTeamB = (ImageButton) findViewById(R.id.yellow_card_add_button_team_b);
        ImageButton redCardButtonTeamB = (ImageButton) findViewById(R.id.red_card_add_button_team_b);

        // Score Board Buttons
        Button sentDataToScoreBoardButton = (Button) findViewById(R.id.sentToScoreBoardButton);
        Button switchViewOfScoreBoardButton = (Button) findViewById(R.id.switchViewOnTheScoreBoard);

        // Set status for fields
        playButton.setEnabled(true);
        sentDataToScoreBoardButton.setEnabled(true);
        switchViewOfScoreBoardButton.setEnabled(true);
        resetButton.setEnabled(status);
        addTimeButton.setEnabled(status);
        goalForTeamAButton.setEnabled(status);
        yellowCardAddButtonTeamA.setEnabled(status);
        redCardAddButtonTeamA.setEnabled(status);
        goalForTeamBButton.setEnabled(status);
        yellowCardButtonTeamB.setEnabled(status);
        redCardButtonTeamB.setEnabled(status);

        // Change color of buttons if are enabled/disabled
        if (status == false) {
            resetButton.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            addTimeButton.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            goalForTeamAButton.setTextColor(Color.GRAY);
            yellowCardAddButtonTeamA.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            redCardAddButtonTeamA.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            goalForTeamBButton.setTextColor(Color.GRAY);
            yellowCardButtonTeamB.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            redCardButtonTeamB.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        } else {
            resetButton.clearColorFilter();
            addTimeButton.clearColorFilter();
            goalForTeamAButton.setTextColor(Color.WHITE);
            yellowCardAddButtonTeamA.clearColorFilter();
            redCardAddButtonTeamA.clearColorFilter();
            goalForTeamBButton.setTextColor(Color.WHITE);
            yellowCardButtonTeamB.clearColorFilter();
            redCardButtonTeamB.clearColorFilter();
        }
    }
}