package com.example.kosti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.util.Arrays;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Game extends AppCompatActivity implements View.OnClickListener {
    TextView playersTableTextView, //отображение кол-ва игроков
             deathScoreTextView, //отображение числа обнуления счета
             currentPlayerTextView, //текущий игрок
             currentPlayerScoreTextView, //его счет
             currentRollScoreTextView, //счет броска текущего игрока
             winScoreTextView; //игра до..

    Button saveScoreButton; //сохранить счет броска

    ImageButton playersScoresImageButton, //показать таблицу игроков
                exitGameImageButton, //выйти из игры
                rollTheDicesImageButton, //бросить кубики
                //кубики 1-5
                diceImageButton_1,
                diceImageButton_2,
                diceImageButton_3,
                diceImageButton_4,
                diceImageButton_5;

    int players, //кол-во игроков
        burnedScoreStart, //начало сгараемого счета
        burnedScoreEnd, //конец сгараемого счета
        nullNumber, //число обнуления счета
        winScore, //игра до..
        currentPlayer, //текущий игрок
        currentScore, //счет текущего игрока
        width,
        height;

    boolean burned = false;

    int[] playerScores = new int[6]; //массив с очками игроков
    String[] names = new String[6]; //массив с именами
    Object[][] dicesList = {
            {R.id.dice_1, 1, false}, // {кубик, значение, блокировка}
            {R.id.dice_2, 1, false},
            {R.id.dice_3, 1, false},
            {R.id.dice_4, 1, false},
            {R.id.dice_5, 1, false},
    };
    int[][] dicesImagesList = {
            {R.drawable.dice_1, R.drawable.dice_1_locked},
            {R.drawable.dice_2, R.drawable.dice_2_locked},
            {R.drawable.dice_3, R.drawable.dice_3_locked},
            {R.drawable.dice_4, R.drawable.dice_4_locked},
            {R.drawable.dice_5, R.drawable.dice_5_locked},
            {R.drawable.dice_6, R.drawable.dice_6_locked},
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        height = size.y;
        width = size.x;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        // получаем данные из прошлой активности
        Intent intent = getIntent();
        players = intent.getIntExtra("players", 0);
        names = intent.getStringArrayExtra("names");
        burnedScoreStart = intent.getIntExtra("burnedScoreStart", 0);
        burnedScoreEnd = intent.getIntExtra("burnedScoreEnd", 0);
        nullNumber = intent.getIntExtra("nullNumber", 0);
        winScore = intent.getIntExtra("winScore", 0);

        // привязываем кнопки к прослушке на нажатие
        playersScoresImageButton = findViewById(R.id.showPlayersTable); //показать таблицу игроков
        playersScoresImageButton.setOnClickListener(this);
        exitGameImageButton = findViewById(R.id.exitGame); //выйти из игры
        exitGameImageButton.setOnClickListener(this);

        rollTheDicesImageButton = findViewById(R.id.rollTheDice); //бросить кубики
        rollTheDicesImageButton.setOnClickListener(this);

        diceImageButton_1 = findViewById(R.id.dice_1);
        diceImageButton_2 = findViewById(R.id.dice_2);
        diceImageButton_3 = findViewById(R.id.dice_3);
        diceImageButton_4 = findViewById(R.id.dice_4);
        diceImageButton_5 = findViewById(R.id.dice_5);
        diceImageButton_1.setOnClickListener(this);
        diceImageButton_2.setOnClickListener(this);
        diceImageButton_3.setOnClickListener(this);
        diceImageButton_4.setOnClickListener(this);
        diceImageButton_5.setOnClickListener(this);

        saveScoreButton = findViewById(R.id.saveScore); //сохранить результат
        saveScoreButton.setOnClickListener(this);

        // привязываем элементы
        deathScoreTextView = findViewById(R.id.deathScore); // число обнуления счета
        deathScoreTextView.setText(Integer.toString(nullNumber));
        winScoreTextView = findViewById(R.id.winScore); // победный счет
        winScoreTextView.setText(Integer.toString(winScore));
        currentPlayerTextView = findViewById(R.id.currentPlayer); // имя текущего игрока
        currentPlayerTextView.setText(names[currentPlayer]);
        currentPlayerScoreTextView = findViewById(R.id.currentScore); //его счет
        currentPlayerScoreTextView.setText(Integer.toString(playerScores[currentPlayer]));
        currentRollScoreTextView = findViewById(R.id.currentRollScore); // счет броска текущего игрока
        currentRollScoreTextView.setText("0");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) { //Отслеживание нажатия кнопок

            case R.id.dice_1:
                if (!(boolean) dicesList[0][2]) {
                    dicesList[0][2] = true;
                    diceImageButton_1.setImageResource(dicesImagesList[(int) dicesList[0][1] - 1][1]);
                }
                else {
                    dicesList[0][2] = false;
                    diceImageButton_1.setImageResource(dicesImagesList[(int) dicesList[0][1] - 1][0]);
                }
                checkFivesLock();
                break;

            case R.id.dice_2:
                if (!(boolean) dicesList[1][2]) {
                    dicesList[1][2] = true;
                    diceImageButton_2.setImageResource(dicesImagesList[(int) dicesList[1][1] - 1][1]);
                }
                else {
                    dicesList[1][2] = false;
                    diceImageButton_2.setImageResource(dicesImagesList[(int) dicesList[1][1] - 1][0]);
                }
                checkFivesLock();
                break;

            case R.id.dice_3:
                if (!(boolean) dicesList[2][2]) {
                    dicesList[2][2] = true;
                    diceImageButton_3.setImageResource(dicesImagesList[(int) dicesList[2][1] - 1][1]);
                }
                else {
                    dicesList[2][2] = false;
                    diceImageButton_3.setImageResource(dicesImagesList[(int) dicesList[2][1] - 1][0]);
                }
                checkFivesLock();
                break;

            case R.id.dice_4:
                if (!(boolean) dicesList[3][2]) {
                    dicesList[3][2] = true;
                    diceImageButton_4.setImageResource(dicesImagesList[(int) dicesList[3][1] - 1][1]);
                }
                else {
                    dicesList[3][2] = false;
                    diceImageButton_4.setImageResource(dicesImagesList[(int) dicesList[3][1] - 1][0]);
                }
                checkFivesLock();
                break;

            case R.id.dice_5:
                if (!(boolean) dicesList[4][2]) {
                    dicesList[4][2] = true;
                    diceImageButton_5.setImageResource(dicesImagesList[(int) dicesList[4][1] - 1][1]);
                }
                else {
                    dicesList[4][2] = false;
                    diceImageButton_5.setImageResource(dicesImagesList[(int) dicesList[4][1] - 1][0]);
                }
                checkFivesLock();
                break;

            case R.id.showPlayersTable:
                Intent intent = new Intent(this, PlayersScores.class);
                intent.putExtra("players", players);
                intent.putExtra("names", names);
                intent.putExtra("scores", playerScores);
                startActivity(intent);
                break;

            case R.id.exitGame:
                openExitGameDialog();
                break;

            case R.id.rollTheDice:
                burned = false;
                rollAnimation();
                MediaPlayer rollSong = MediaPlayer.create(this, R.raw.roll);
                rollSong.start();
                if (playerScores[currentPlayer] >= winScore) {
                    saveScore(0);
                    break;
                }
                //1, 2, 3, 4, 5, 6
                int one = 0;
                int two = 0;
                int three = 0;
                int four = 0;
                int five = 0;
                int six = 0;
                int[] res = rollTheDices(); //получаем массив со случайными числами для кубиков
                for (int n = 0; n < 5; n++) {
                    if (!(boolean) dicesList[n][2]) {
                        dicesList[n][1] = res[n];
                        ImageButton dice = findViewById((Integer) dicesList[n][0]);
                        dice.setImageResource(dicesImagesList[res[n] - 1][0]);
                    } else {
                        res[n] = 0;
                    }
                }
                int income = 0; //кол-во очков, полученных за бросок

                for (int n : res) { //подсчет кубиков
                    if (n == 1) one += 1;
                    if (n == 2) two += 1;
                    if (n == 3) three += 1;
                    if (n == 4) four += 1;
                    if (n == 5) five += 1;
                    if (n == 6) six += 1;
                }
                //подсчет очков
                if (one == 1) income += 10;
                if (one == 2) income += 20;
                if (one == 3) income += 100;
                if (one == 4) income += 200;
                if (one == 5) income += 1000;

                if (two == 3) income += 20;
                if (two == 4) income += 40;
                if (two == 5) income += 200;

                if (three == 3) income += 30;
                if (three == 4) income += 60;
                if (three == 5) income += 300;

                if (four == 3) income += 40;
                if (four == 4) income += 80;
                if (four == 5) income += 400;

                if (five == 1) income += 5;
                if (five == 2) income += 10;
                if (five == 3) income += 50;
                if (five == 4) income += 100;
                if (five == 5) income += 500;

                if (six == 3) income += 60;
                if (six == 4) income += 120;
                if (six == 5) income += 600;

                if (one == 1 && two == 1 && three == 1 && four == 1 && five == 1) income += 125;
                if (two == 1 && three == 1 && four == 1 && five == 1 && six == 1) income += 250;

                //проверка хода
                if (playerScores[currentPlayer] + income == nullNumber)
                {
                    playerScores[currentPlayer] = 0;
                    saveScore(0);
                    currentRollScoreTextView.setText("Сгорел");
                    burned = true;
                }
                if (income == 0) {
                    saveScore(0);
                    currentRollScoreTextView.setText("Проигрыш");
                }
                else {
                    currentScore += income;
                    if (!burned) { currentRollScoreTextView.setText(Integer.toString(currentScore)); }

                    int possibilityScore = playerScores[currentPlayer] + currentScore;
                    boolean inBurningScoreRange = possibilityScore > burnedScoreStart & possibilityScore < burnedScoreEnd;

                    if (!inBurningScoreRange & (playerScores[currentPlayer] != 0 | possibilityScore >= 100)) {
                        saveScoreButton.setVisibility(View.VISIBLE);
                    } else {
                        saveScoreButton.setVisibility(View.INVISIBLE);
                    }

                    if (possibilityScore > winScore) {
                        saveScore(0);
                        currentRollScoreTextView.setText("Перебор!");

                    }
                }
                    break;

            case R.id.saveScore:
                saveScore(currentScore);
                break;
        }
    }

    public void onBackPressed() { openExitGameDialog(); }

    public void openExitGameDialog() { //диалог выхода из игры
        ExitGameDialog exitGameDialog = new ExitGameDialog();
        exitGameDialog.show(getSupportFragmentManager(),"exit game dialog");
    }


    public int[] rollTheDices() { //генерация массива чисел для кубиков
        int[] result = new int[5];
        for (int n = 0; n < result.length; n++) {
            result[n] = new Random().nextInt(6) + 1;
        }
        return result;
    }

    public void saveScore(int score) { //сохранение результа(ов) броска(ов) кубиков
        saveScoreButton.setVisibility(View.INVISIBLE); //скрываем кнопку "сохранить"
        currentScore = 0; //обнуляем текущий счет
        currentRollScoreTextView.setText("0");
        playerScores[currentPlayer] += score; //прибавляем сохраненный счет игроку
        if (!isOnePlayerLeft()) {
            do {
                currentPlayer++;
                if (currentPlayer > players - 1) currentPlayer = 0;
            } while (playerScores[currentPlayer] == winScore);
        }
        else
        {
            endGame();
        }
        currentPlayerTextView.setText(names[currentPlayer]); //отображаем имя нового текущего игрока
        currentPlayerScoreTextView.setText(Integer.toString(playerScores[currentPlayer])); //отображаем счет нового текущего игрока
    }

    public boolean isOnePlayerLeft(){
        int activePlayersCount = 0;
        for (int n = 0; n < players; n++) {
            if (playerScores[n] < winScore)
            {
                activePlayersCount++;
            }
        }
        if (activePlayersCount > 1) { return false; }
        else { return true; }
    }

    public void endGame(){
        Intent intent = new Intent(this, PlayersScores.class);
        intent.putExtra("players", players);
        intent.putExtra("names", names);
        intent.putExtra("scores", playerScores);
        intent.putExtra("isGameEnd", true);
        startActivity(intent);
        finish();
    }
    public void checkFivesLock()
    {
        if ((boolean)dicesList[0][2] &&
                (boolean)dicesList[1][2] &&
                (boolean)dicesList[2][2] &&
                (boolean)dicesList[3][2] &&
                (boolean)dicesList[4][2]) { rollTheDicesImageButton.setEnabled(false); }
        else {rollTheDicesImageButton.setEnabled(true);}
    }
    public void rollAnimation()
    {
        rollTheDicesImageButton.setVisibility(View.INVISIBLE);
        currentRollScoreTextView.setVisibility(View.INVISIBLE);
        if (!(boolean) dicesList[0][2]) { diceImageButton_1.setY(height + 100); }
        if (!(boolean) dicesList[1][2]) { diceImageButton_2.setY(height + 100); }
        if (!(boolean) dicesList[2][2]) { diceImageButton_3.setY(height + 100); }
        if (!(boolean) dicesList[3][2]) { diceImageButton_4.setY(height + 100); }
        if (!(boolean) dicesList[4][2]) { diceImageButton_5.setY(height + 100); }
        Thread t = new Thread(){
            @Override
            public void run(){
                try {
                    sleep(1000);
                    int diceRandomY = width / 6 + 15 + new Random().nextInt(10);
                    int slow = 0;
                    for(int i=0; i< diceRandomY; i++){
                        if (i > diceRandomY / 5) { slow = 1; }
                        if (i > diceRandomY / 4) { slow = 2; }
                        if (i > diceRandomY / 3) { slow = 3; }
                        if (i > diceRandomY / 2) { slow = 5; }
                        if (!(boolean) dicesList[0][2]) {
                            diceImageButton_1.setRotation(diceImageButton_1.getRotation() + (new Random().nextInt(3) + 7 - slow - 1));
                            diceImageButton_1.setTranslationY(diceImageButton_1.getTranslationY() - (10 - slow));
                        }
                        if (!(boolean) dicesList[1][2]) {
                            diceImageButton_2.setRotation(diceImageButton_2.getRotation() + (new Random().nextInt(4) + 8 - slow - 2));
                            diceImageButton_2.setTranslationY(diceImageButton_2.getTranslationY() - (11 - slow));
                        }
                        if (!(boolean) dicesList[2][2]) {
                            diceImageButton_3.setRotation(diceImageButton_3.getRotation() + (new Random().nextInt(5) + 9 - slow - 3));
                            diceImageButton_3.setTranslationY(diceImageButton_3.getTranslationY() - (12 - slow));
                        }
                        if (!(boolean) dicesList[3][2]) {
                            diceImageButton_4.setRotation(diceImageButton_4.getRotation() + (new Random().nextInt(6) + 10 - slow - 4));
                            diceImageButton_4.setTranslationY(diceImageButton_4.getTranslationY() - (13 - slow));
                        }
                        if (!(boolean) dicesList[4][2]) {
                            diceImageButton_5.setRotation(diceImageButton_5.getRotation() + (new Random().nextInt(7) + 11 - slow - 5));
                            diceImageButton_5.setTranslationY(diceImageButton_5.getTranslationY() - (14 - slow));
                        }
                        sleep(1,300000);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rollTheDicesImageButton.setVisibility(View.VISIBLE);
                            currentRollScoreTextView.setVisibility(View.VISIBLE);
                        }
                    });

                } catch (InterruptedException ex) {
                    Log.i("error","thread");
                }
            }
        };
        t.start();
    }
}