package com.example.kosti;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Collections;

public class PlayersScores extends AppCompatActivity implements View.OnClickListener {
    int players;

    int[] playersScores = new int[6];
    Integer[] tempArray = new Integer[6];
    int[] playersPlaces = new int[6];
    String[] playersNames = new String[6];
    boolean isGameEnd;

    TextView gameEndsTextView;

    ImageButton closeScoreActivity;
    Object[][] playersList = {
            {R.id.pName1, R.id.pScore1, R.id.pPlace1},
            {R.id.pName2, R.id.pScore2, R.id.pPlace2},
            {R.id.pName3, R.id.pScore3, R.id.pPlace3},
            {R.id.pName4, R.id.pScore4, R.id.pPlace4},
            {R.id.pName5, R.id.pScore5, R.id.pPlace5},
            {R.id.pName6, R.id.pScore6, R.id.pPlace6},
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_players_scores);
        closeScoreActivity = findViewById(R.id.imageButton4);
        closeScoreActivity.setOnClickListener(this);
        gameEndsTextView = findViewById(R.id.textView2);
        Intent intent = getIntent();
        players = intent.getIntExtra("players", 0);
        playersNames = intent.getStringArrayExtra("names");
        playersScores = intent.getIntArrayExtra("scores");
        isGameEnd = intent.getBooleanExtra("isGameEnd", false);
        setPlayersTable(players, playersNames, playersScores);
        if (isGameEnd) { gameEndsTextView.setVisibility(View.VISIBLE); }
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    public static int findIndex(int arr[], int t)
    {
        if (arr == null) {
            return -1;
        }
        int len = arr.length;
        int i = 0;
        while (i < len) {
            if (arr[i] == t) {
                return i;
            }
            else {
                i = i + 1;
            }
        }
        return -1;
    }

    private void setPlayersTable(int players, String[] playersNames, int[] playersScores) {
        for (int n = 0; n < players; n++) {
            for (int i = 0; i < playersScores.length; i++) {
                tempArray[i] = playersScores[i];
            }
            Arrays.sort(tempArray, Collections.reverseOrder());

            for (int i = 0; i < playersScores.length; i++) {
                playersPlaces[i] = tempArray[i];
            }
            TextView playerName = findViewById((Integer) playersList[n][0]);
            playerName.setText(playersNames[n]);

            TextView playerScore = findViewById((Integer) playersList[n][1]);
            playerScore.setText(Integer.toString(playersScores[n]));

            TextView playerPlace = findViewById((Integer) playersList[n][2]);
            playerPlace.setText(Integer.toString(findIndex(playersPlaces, playersScores[n]) + 1));

            playerName.setVisibility(View.VISIBLE);
            playerScore.setVisibility(View.VISIBLE);
            playerPlace.setVisibility(View.VISIBLE);
        }
    }
}
