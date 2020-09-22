package com.example.caro;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import javax.xml.xpath.XPath;

import androidx.annotation.Nullable;

public class chessboard extends View {
    private Paint mPaint, xPaint, yPaint;
    private final int n = 20, m = 15; //n hang, m cot
    private int cellSize; //kich thuoc cua 1 o
    private int[][] board = new int[n][m];
    private final String TAG = "CHESSBOARD";
    private boolean playerTurn = true;
    private final int x_cell = 1, o_cell = 2, empty_cell = 0;
    private final boolean x_turn = true, o_turn = false;
    private final int textSize = 30;
    private final Rect textBounds = new Rect();

    public chessboard(Context context) {
        super(context);
        initPaint();
    }

    public chessboard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public chessboard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        initBroadChess(canvas);
        drawXO(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int row = (int) Math.floor(event.getY() / cellSize);
        int col = (int) Math.floor(event.getX() / cellSize);

        Log.d(TAG, "cell " + row + " " + col);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "Action down");
                if (row < n && col < m &&   board[row][col] == empty_cell) {
                    if (playerTurn == x_turn) board[row][col] = x_cell;
                    if (playerTurn == o_turn) board[row][col] = o_cell;
                    playerTurn = !playerTurn;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "Action move");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "Action up");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "Action cancel");
                break;
            case MotionEvent.ACTION_OUTSIDE:
                Log.d(TAG, "Action outside");
                break;
        }

        invalidate();
        return true;
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(1);

        xPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        xPaint.setColor(Color.BLUE);
        xPaint.setStyle(Paint.Style.FILL);
        xPaint.setTextSize(textSize);
//        xPaint.setTextAlign(Paint.Align.CENTER);

        yPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        yPaint.setColor(Color.RED);
        yPaint.setStyle(Paint.Style.FILL);
        yPaint.setTextSize(textSize);
//        yPaint.setTextAlign(Paint.Align.CENTER);

        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                board[i][j] = empty_cell;

    }

    private void initBroadChess(Canvas canvas) {
        if (getWidth() / m < getHeight() / n) {
            cellSize = getWidth() / m;
        } else {
            cellSize = getHeight() / n;
        }
        for (int i = 0; i <= n; i++) {
            int x1 = 0, y1 = i * cellSize;
            int x2 = cellSize * m, y2 = i * cellSize;
            canvas.drawLine(x1, y1, x2, y2, mPaint);
        }
        for (int i = 0; i <= m; i++) {
            int x1 = i * cellSize, y1 = 0;
            int x2 = i * cellSize, y2 = n * cellSize;
            canvas.drawLine(x1, y1, x2, y2, mPaint);
        }
    }

    public void drawTextCentred(Canvas canvas, Paint paint, String text, float cx, float cy){
        paint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text, cx - textBounds.exactCenterX(), cy - textBounds.exactCenterY(), paint);
    }

    private void drawXO(Canvas canvas) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (board[i][j] == x_cell)
                    drawTextCentred(canvas, xPaint, "X", j * cellSize + (float) cellSize / 2, i * cellSize + (float) cellSize / 2 );
                if (board[i][j] == o_cell)
                    drawTextCentred(canvas, yPaint, "O", j * cellSize + (float) cellSize / 2, i * cellSize + (float) cellSize / 2 );
            }
        }
    }
}
