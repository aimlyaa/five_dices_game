package com.example.kosti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button newGameButton, rulesButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //Новая игра
        newGameButton = findViewById(R.id.newGame);
        newGameButton.setOnClickListener(this);
        //Правила
        rulesButton = findViewById(R.id.rules);
        rulesButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newGame:
                Intent newGameIntent = new Intent(this, CreateGameActivity.class);
                startActivity(newGameIntent);
                break;

            case R.id.rules:
                Intent rulesIntent = new Intent(this, RulesActivity.class);
                startActivity(rulesIntent);
                break;
        }
    }
}
