package com.eltech.snc.maze;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.eltech.snc.R;
import com.eltech.snc.ui.maze.MazeFragment;

import java.util.ArrayList;

import static com.eltech.snc.ui.maze.MazeFragment.FAT_FINGERS_MARGIN;

/**
 * View of the line the user draws with their finger.
 */
public class FingerLine extends View {
    private Paint mPaint;
    private Path mPath;
    private int[][] solutionPath;
    private int solutionCellsVisited;
    private ArrayList<Boolean> solved;
    private boolean solvedMaze;
    private boolean stopDrawing;
    private boolean drawingStarted;
    private EndingEventListener endingEventListener;
    private float finishPointXL;
    private float finishPointXR;
    private float finishPointYT;
    private float finishPointYB;

    public FingerLine(Context context) {
        this(context, null);
        init();
    }

    public FingerLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FingerLine(Context context, AttributeSet attrs, int[][] solutionPath) {
        super(context, attrs);
        this.solutionPath = solutionPath;
        init();
    }

    private void init() {
        // Create the paint brush
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Style.STROKE);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.solver));
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(10f);
        mPath = new Path();

        solutionCellsVisited = 0;

        solvedMaze = false;
        stopDrawing = false;
        drawingStarted = false;

        solved = new ArrayList<>();

        for (int i = 0; i < solutionPath.length; i++) {
            solved.add(false);
        }
        // Log.d("SOLVED_LENGTH", Integer.toString(solved.size()));
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (stopDrawing) {
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!drawingStarted) {
                    MazeFragment.TIMER.start();
                    drawingStarted = true;
                }
                mPath.moveTo(event.getX(), event.getY());
                return true;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                return false;
        }
        // Schedule a repaint
        invalidate();
        // Check if user solved the maze
        boolean xInCell;
        boolean yInCell;

        for (int i = 0; i < solutionPath.length; i++) {

            xInCell = (event.getX() >= solutionPath[i][0] && event.getX() <= solutionPath[i][2]);
            yInCell = (event.getY() >= solutionPath[i][1] && event.getY() <= solutionPath[i][3]);

            if (xInCell && yInCell) {
                solved.set(i, true);

                if (!solvedMaze && !solved.contains(false)) {
                    solvedMaze = true;
                }
            }
        }

        if (finishPointXL <= event.getX() && event.getX() <= finishPointXR &&
                finishPointYT <= event.getY() && event.getY() <= finishPointYB) {
            MazeFragment.TIMER.stop();
            stopDrawing = true;
            endingEventListener.onEndingEvent(isMazeSolved());
        }
        return true;
    }

    private boolean isMazeSolved() {
        return solvedMaze;
    }

    public void setFinishPointCoords(float x, float y) {
        finishPointXL = x;
        finishPointXR = x + 75;
        finishPointYT = y;
        finishPointYB = y + 75;
    }

    public void setEndingEventListener(EndingEventListener endingEventListener) {
        this.endingEventListener = endingEventListener;
    }
}