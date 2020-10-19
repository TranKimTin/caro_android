package com.example.caro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Chessboard chessboard;
    private Button btnNewGame, btnUndo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setView();

        btnNewGame.setOnClickListener(this);
        btnUndo.setOnClickListener(this);
    }

    void setView(){
        chessboard = (Chessboard) findViewById(R.id.chessboard);
        btnNewGame = (Button) findViewById(R.id.btnNewGame);
        btnUndo = (Button) findViewById(R.id.btnUndo);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnNewGame:
                chessboard.newGane();
                break;
            case R.id.btnUndo:
                chessboard.undo();
        }
    }
}