package com.example.kosti;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class CreateGameActivity extends AppCompatActivity implements View.OnClickListener{
    private Button addPlayer, removePlayer, startGame;
    private EditText bss, bse, nn, msg;
    int players = 2;
    int burnedScoreStart = 400;
    int burnedScoreEnd = 500;
    int nullNumber = 210;
    int winScore = 1000;
    String[] playersNames = new String[6];
    Object[][] playersList = {
            {R.id.textView5, R.id.nameText},
            {R.id.textView6, R.id.nameText2},
            {R.id.textView7, R.id.nameText3},
            {R.id.textView8, R.id.nameText4},
            {R.id.textView9, R.id.nameText5},
            {R.id.textView10, R.id.nameText6},
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_game);

        startGame = findViewById(R.id.startGame);
        addPlayer = findViewById(R.id.incPlayers);
        removePlayer = findViewById(R.id.decPlayers);

        startGame.setOnClickListener(this);
        addPlayer.setOnClickListener(this);
        removePlayer.setOnClickListener(this);

        bss = findViewById(R.id.burnedScoreStart);
        bse = findViewById(R.id.burnedScoreEnd);
        nn = findViewById(R.id.nullableScore);
        msg = findViewById(R.id.maxScoreGame);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startGame:
                burnedScoreStart = Integer.parseInt(bss.getText().toString());
                burnedScoreEnd = Integer.parseInt(bse.getText().toString());
                nullNumber = Integer.parseInt(nn.getText().toString());
                winScore = Integer.parseInt(msg.getText().toString());

                for (int n = 0; n < players; n++) {
                    EditText name = findViewById((Integer) playersList[n][1]);
                    playersNames[n] = (name.getText().toString());
                }

                Intent intent = new Intent(this, Game.class);
                intent.putExtra("players", players);
                intent.putExtra("names", playersNames);
                intent.putExtra("burnedScoreStart", burnedScoreStart);
                intent.putExtra("burnedScoreEnd", burnedScoreEnd);
                intent.putExtra("nullNumber", nullNumber);
                intent.putExtra("winScore", winScore);
                startActivity(intent);
                finish();
                break;

            case R.id.incPlayers:
                if (players < 6) {
                    refreshPlayerCount(true);
                    players += 1;
                }
                break;

            case R.id.decPlayers:
                if (players > 1) {
                    players -= 1;
                    refreshPlayerCount(false);
                }
                break;
        }
    }

    private void refreshPlayerCount(boolean state) {
        View playerTitle = findViewById((Integer) playersList[players][0]);
        View playerName =  findViewById((Integer) playersList[players][1]);
        if (state) {
            playerTitle.setVisibility(View.VISIBLE);
            playerName.setVisibility(View.VISIBLE);
        }
        else {
            playerTitle.setVisibility(View.INVISIBLE);
            playerName.setVisibility(View.INVISIBLE);
        }
    }

}


