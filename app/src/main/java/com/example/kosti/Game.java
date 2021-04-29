package com.example.kosti;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

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
                    UnlockAllDices();
                }
                if (income == 0) {
                    saveScore(0);
                    currentRollScoreTextView.setText("Проигрыш");
                    UnlockAllDices();
                }
                else {
                    currentScore += income;
                    if (!burned) { currentRollScoreTextView.setText(Integer.toString(currentScore)); }

                    int possibilityScore = playerScores[currentPlayer] + currentScore;
                    boolean inBurningScoreRange = possibilityScore > burnedScoreStart & possibilityScore < burnedScoreEnd;

                    if (!inBurningScoreRange & (playerScores[currentPlayer] != 0 | possibilityScore >= 100) & !(possibilityScore > winScore) & !burned) {
                        ShowSaveScoreButton();
                    } else {
                        saveScoreButton.setVisibility(View.INVISIBLE);
                    }

                    if (possibilityScore > winScore) {
                        saveScore(0);
                        currentRollScoreTextView.setText("Перебор!");
                        UnlockAllDices();
                    }
                }
                    break;

            case R.id.saveScore:
                saveScore(currentScore);
                for (int i = 0; i < dicesList.length; i++){
                    dicesList[i][2] = false;
                }
                diceImageButton_1.setImageResource(dicesImagesList[(int) dicesList[0][1] - 1][0]);
                diceImageButton_2.setImageResource(dicesImagesList[(int) dicesList[1][1] - 1][0]);
                diceImageButton_3.setImageResource(dicesImagesList[(int) dicesList[2][1] - 1][0]);
                diceImageButton_4.setImageResource(dicesImagesList[(int) dicesList[3][1] - 1][0]);
                diceImageButton_5.setImageResource(dicesImagesList[(int) dicesList[4][1] - 1][0]);
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
        HideAndShowGameInterfaceElemetns();
        if (!(boolean) dicesList[0][2]) { diceImageButton_1.setY(height + 100); }
        if (!(boolean) dicesList[1][2]) { diceImageButton_2.setY(height + 100); }
        if (!(boolean) dicesList[2][2]) { diceImageButton_3.setY(height + 100); }
        if (!(boolean) dicesList[3][2]) { diceImageButton_4.setY(height + 100); }
        if (!(boolean) dicesList[4][2]) { diceImageButton_5.setY(height + 100); }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!(boolean) dicesList[0][2]) {
                    ObjectAnimator AnimationY = ObjectAnimator.ofFloat(diceImageButton_1, "translationY", -height/16);
                    ObjectAnimator AnimationRotation = ObjectAnimator.ofFloat(diceImageButton_1, "rotation", new Random().nextInt(250) + 250);
                    AnimationY.setDuration(400);
                    AnimationRotation.setDuration(420);
                    AnimationY.start();
                    AnimationRotation.start();
                }
                if (!(boolean) dicesList[1][2]) {
                    ObjectAnimator AnimationY = ObjectAnimator.ofFloat(diceImageButton_2, "translationY", -height/16);
                    ObjectAnimator AnimationRotation = ObjectAnimator.ofFloat(diceImageButton_2, "rotation", new Random().nextInt(250) + 250);
                    AnimationY.setDuration(400);
                    AnimationRotation.setDuration(420);
                    AnimationY.start();
                    AnimationRotation.start();
                }
                if (!(boolean) dicesList[2][2]) {
                    ObjectAnimator AnimationY = ObjectAnimator.ofFloat(diceImageButton_3, "translationY", -height/32);
                    ObjectAnimator AnimationRotation = ObjectAnimator.ofFloat(diceImageButton_3, "rotation", new Random().nextInt(250) + 250);
                    AnimationY.setDuration(400);
                    AnimationRotation.setDuration(420);
                    AnimationY.start();
                    AnimationRotation.start();
                }
                if (!(boolean) dicesList[3][2]) {
                    ObjectAnimator AnimationY = ObjectAnimator.ofFloat(diceImageButton_4, "translationY", -height/24);
                    ObjectAnimator AnimationRotation = ObjectAnimator.ofFloat(diceImageButton_4, "rotation", new Random().nextInt(250) + 250);
                    AnimationY.setDuration(400);
                    AnimationRotation.setDuration(420);
                    AnimationY.start();
                    AnimationRotation.start();
                }
                if (!(boolean) dicesList[4][2]) {
                    ObjectAnimator AnimationY = ObjectAnimator.ofFloat(diceImageButton_5, "translationY", -height/40);
                    ObjectAnimator AnimationRotation = ObjectAnimator.ofFloat(diceImageButton_5, "rotation", new Random().nextInt(250) + 250);
                    AnimationY.setDuration(400);
                    AnimationRotation.setDuration(420);
                    AnimationY.start();
                    AnimationRotation.start();
                }
            }
        }, 1000);


    }

    public void HideAndShowGameInterfaceElemetns()
    {
        rollTheDicesImageButton.setVisibility(View.INVISIBLE);
        currentRollScoreTextView.setVisibility(View.INVISIBLE);
        currentPlayerTextView.setVisibility(View.INVISIBLE);
        currentPlayerScoreTextView.setVisibility(View.INVISIBLE);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rollTheDicesImageButton.setVisibility(View.VISIBLE);
                currentRollScoreTextView.setVisibility(View.VISIBLE);
                currentPlayerTextView.setVisibility(View.VISIBLE);
                currentPlayerScoreTextView.setVisibility(View.VISIBLE);
            }
        }, 1500);
    }

    public void ShowSaveScoreButton()
    {
        saveScoreButton.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                saveScoreButton.setVisibility(View.VISIBLE);
            }
        }, 1500);
    }

    public void UnlockAllDices(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < dicesList.length; i++){
                    dicesList[i][2] = false;
                }
                diceImageButton_1.setImageResource(dicesImagesList[(int) dicesList[0][1] - 1][0]);
                diceImageButton_2.setImageResource(dicesImagesList[(int) dicesList[1][1] - 1][0]);
                diceImageButton_3.setImageResource(dicesImagesList[(int) dicesList[2][1] - 1][0]);
                diceImageButton_4.setImageResource(dicesImagesList[(int) dicesList[3][1] - 1][0]);
                diceImageButton_5.setImageResource(dicesImagesList[(int) dicesList[4][1] - 1][0]);
            }
        }, 1500);

    }
}