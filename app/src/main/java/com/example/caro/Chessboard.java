package com.example.caro;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class Chessboard extends View {
    private Paint mPaint, xPaint, oPaint, lastPaint;
    private final int n = 18, m = 15; //n hang, m cot
    private int cellSize; //kich thuoc cua 1 o
    private int[][] board = new int[n][m];
    private final String TAG = "CHESSBOARD";
    private boolean playerTurn = true;
    private final int x_cell = 1, o_cell = 2, empty_cell = 0;
    private final boolean x_turn = true, o_turn = false;
    private final int textSize = 50;
    private final Rect textBounds = new Rect();
    private Point last;
    Point lastTouch;
    private boolean finish = false;
    private int direction = 0;
    List<Point> status;

    public Chessboard(Context context) {
        super(context);
        initPaint();
    }

    public Chessboard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public Chessboard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        if (finish)
            return true;

        int row = (int) Math.floor(event.getY() / cellSize);
        int col = (int) Math.floor(event.getX() / cellSize);

        Log.d(TAG, "cell " + row + " " + col);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "Action down");
                if (row < n && col < m) {
                    lastTouch.y = row;
                    lastTouch.x = col;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "Action move");
                if (row < n && col < m && (lastTouch.y != col || lastTouch.x != row)) {
                    lastTouch.set(col, row);
                    if(last.x != row || last.y != col) last.set(-1, -1);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "Action up");
                if (row >= 0 && row < n && col >= 0 && col < m && board[row][col] == empty_cell) {
                    //Log.d(TAG, last.x + " " + last.y);
                    if (last.x == row && last.y == col) {
                        if (playerTurn == x_turn) board[row][col] = x_cell;
                        if (playerTurn == o_turn) board[row][col] = o_cell;
                        playerTurn = !playerTurn;
                        status.add(new Point(row, col));
                        if (countXO(row, col) >= 5) {
                            if (board[row][col] == x_cell) {
                                Toast.makeText(getContext(), "X thắng", Toast.LENGTH_LONG).show();
                            }
                            if (board[row][col] == o_cell) {
                                Toast.makeText(getContext(), "O thắng", Toast.LENGTH_LONG).show();
                            }
                            finish = true;
                            last.set(-1, -1);
                        }
                    }
                    last.set(row,col);
                    //Log.d(TAG, last.x + " " + last.y);
                }
                lastTouch.set(-1, -1);
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "Action cancel");
                break;
            case MotionEvent.ACTION_OUTSIDE:
                Log.d(TAG, "Action outside");
                break;
        }

        return true;
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(1);

        lastPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lastPaint.setColor(Color.GREEN);
        lastPaint.setStrokeWidth(5);

        xPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        xPaint.setColor(Color.BLUE);
        xPaint.setStyle(Paint.Style.FILL);
        xPaint.setTextSize(textSize);
        xPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        oPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        oPaint.setColor(Color.RED);
        oPaint.setStyle(Paint.Style.FILL);
        oPaint.setTextSize(textSize);
        oPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));


        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                board[i][j] = empty_cell;

        status = new ArrayList<Point>();
        lastTouch = new Point(-1,-1);
        last = new Point(-1,-1);
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
        if (last.x >= 0 && last.y >= 0) {
            int x1 = last.y * cellSize, y1 = last.x * cellSize;
            int x2 = x1 + cellSize, y2 = y1 + cellSize;
            canvas.drawRect(x1, y1, x2, y2, lastPaint);
        }
        if (lastTouch.x >= 0 && lastTouch.y >= 0 && board[lastTouch.y][lastTouch.x] == empty_cell) {
            int x1 = lastTouch.x * cellSize, y1 = lastTouch.y * cellSize;
            int x2 = x1 + cellSize, y2 = y1 + cellSize;
            canvas.drawRect(x1, y1, x2, y2, lastPaint);
        }

    }

    public void drawTextCentred(Canvas canvas, Paint paint, String text, float cx, float cy) {
        paint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text, cx - textBounds.exactCenterX(), cy - textBounds.exactCenterY(), paint);
    }

    private void drawXO(Canvas canvas) {
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < m; col++) {
                if (board[row][col] == empty_cell) continue;
                int count = countXO(row, col);
                if (count >= 4) {
                    xPaint.setTextSize((int) (textSize * 1.5));
                    oPaint.setTextSize((int) (textSize * 1.5));
                }


                if (board[row][col] == x_cell)
                    drawTextCentred(canvas, xPaint, "X", col * cellSize + (float) cellSize / 2, row * cellSize + (float) cellSize / 2);
                if (board[row][col] == o_cell)
                    drawTextCentred(canvas, oPaint, "O", col * cellSize + (float) cellSize / 2, row * cellSize + (float) cellSize / 2);
                if (count >= 5) {
                    switch (direction) {
                        case 1:
                            canvas.drawLine(cellSize * col, cellSize * row + cellSize / 2f, cellSize * (col + 1), cellSize * row + +cellSize / 2f, lastPaint);
                            break;
                        case 2:
                            canvas.drawLine(cellSize * col + cellSize / 2f, cellSize * row, cellSize * col + cellSize / 2f, cellSize * (row + 1), lastPaint);

                            break;
                        case 3:
                            canvas.drawLine(cellSize * col, cellSize * row, cellSize * (col + 1), cellSize * (row + 1), lastPaint);

                            break;
                        case 4:
                            canvas.drawLine(cellSize * (col + 1), cellSize * row, cellSize * col, cellSize * (row + 1), lastPaint);
                            break;
                    }
                }
                if (count >= 4) {
                    xPaint.setTextSize(textSize);
                    oPaint.setTextSize(textSize);
                }
            }
        }
    }

    private final int dr[] = {0, 1, 1, 1};
    private final int dc[] = {1, 0, 1, -1};
    private final int direct[] = {1, 2, 3, 4};

    private int countXO(int row, int col) {
        int value = board[row][col];
        if (value == empty_cell) return 0;
        int maxCount = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                int startRow = row - dr[j] * i;
                int startCol = col - dc[j] * i;
                if (startRow >= 0 && startRow < n && startCol >= 0 && startCol < m) {
                    int count = 0;
                    for (int k = 0; k < 5; k++) {
                        int r = startRow + k * dr[j];
                        int c = startCol + k * dc[j];
                        if (r >= 0 && r < n && c >= 0 && c < m) {
                            int val = board[r][c];
                            if (val == value) {
                                count++;
                            }
                            if (val != value && val != empty_cell) {
                                count = 0;
                                break;
                            }
                        } else {
                            count = 0;
                            break;
                        }
                    }
                    if (maxCount < count) maxCount = count;
                    if (maxCount >= 5) {
                        direction = direct[j];
                        break;
                    }
                    ;
                }
            }
            if (maxCount >= 5) return maxCount;
        }
        return maxCount;
    }

    public void newGane() {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                board[i][j] = empty_cell;
        finish = false;
        playerTurn = x_turn;
        last.set(-1,-1);
        lastTouch.set(-1, -1);
        direction = 0;
        status.clear();
        invalidate();
    }

    public void undo() {
        if(status.isEmpty()) return;
        status.remove(status.size()-1);
        playerTurn = x_turn;
        finish = false;
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                board[i][j] = empty_cell;
            }
        }
        for(int i=0; i<status.size(); i++){
            Point p = status.get(i);
            if(playerTurn == x_turn) board[p.x][p.y] = x_cell;
            if(playerTurn == o_turn) board[p.x][p.y] = o_cell;
            playerTurn = !playerTurn;
            last.set(p.x, p.y);
        }


        invalidate();
    }
}
