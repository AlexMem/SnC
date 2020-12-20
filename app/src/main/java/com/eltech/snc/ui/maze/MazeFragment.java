package com.eltech.snc.ui.maze;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.eltech.snc.R;
import com.eltech.snc.maze.EndingEventListener;
import com.eltech.snc.maze.FingerLine;
import com.eltech.snc.maze.MazeView;
import com.eltech.snc.maze.TimerUpdateEventListener;
import com.eltech.snc.utils.ServerApi;
import com.eltech.snc.utils.Timer;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class MazeFragment extends Fragment implements EndingEventListener, TimerUpdateEventListener {
    public static final int FAT_FINGERS_MARGIN = 25;
    private static final String RESULT_TIMER_FORMAT = "%s,%s s";
    private static final int PADDING = 64;
    private static final int mazeSize = 10;
    public static final Timer TIMER = new Timer();

    private TextView resultTimer;
    private MazeView mMazeView;
    private FingerLine mFingerLine;
    private ImageView finishPoint;
    private ImageView startPoint;
    private DisplayMetrics displaymetrics = new DisplayMetrics();
    private FrameLayout mFrameLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_labyrinth, container, false);

        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        mFrameLayout = root.findViewById(R.id.mazeWrapper);
        ViewGroup.LayoutParams params = mFrameLayout.getLayoutParams();
        params.height = (int) Math.floor(displaymetrics.heightPixels * 0.7);
        mFrameLayout.setLayoutParams(params);

        startPoint = root.findViewById(R.id.startPoint);
        finishPoint = root.findViewById(R.id.finishPoint);

        resultTimer = root.findViewById(R.id.resultTimer);
        resultTimer.setText(String.format(RESULT_TIMER_FORMAT, 0, 0));

        ImageButton newMazeButton = root.findViewById(R.id.newMazeButton);
        newMazeButton.setOnClickListener(v -> createMaze());
        newMazeButton.performClick();
        return root;
    }

    public void createMaze() {
        // First remove any existing MazeView and FingerLine
        if (mMazeView != null) {
            ((ViewGroup) mMazeView.getParent()).removeView(mMazeView);
        }
        if (mFingerLine != null) {
            ((ViewGroup) mFingerLine.getParent()).removeView(mFingerLine);
        }
        mMazeView = new MazeView(this.getContext(), mazeSize);

        TIMER.stop();
        TIMER.setTimerUpdateEventListener(this);
        resultTimer.setText(String.format(RESULT_TIMER_FORMAT, 0, 0));
        resultTimer.setTextColor(Color.GRAY);

        // Trace the path from start to farthestVertex using the line of predecessors,
        // apply this information to form an array of rectangles
        // which will be passed on to fingerLine view
        // where the line has to pass.
        // The array be checked against the drawn line in FingerLine.

        int[][] solutionAreas = new int[mMazeView.lengthOfSolutionPath][4];

        int currentVertexKey;
        int totalMazeWidth = displaymetrics.widthPixels - PADDING;
        // int totalMazeHeight = totalMazeWidth;
        int cellSide = totalMazeWidth / mazeSize;
        int row, column;
        int topLeftX, topLeftY, bottomRightX, bottomRightY;

        for (int i = 0; i < mMazeView.lengthOfSolutionPath; i++) {

            currentVertexKey = mMazeView.listOfSolutionVertecesKeys[i];

            // Translate vertex key to location on screen
            row = (currentVertexKey) / mazeSize;
            column = (currentVertexKey) % mazeSize;
            topLeftX = (PADDING / 2) + (column * cellSide) - FAT_FINGERS_MARGIN;
            topLeftY = (PADDING / 2) + (row * cellSide) - FAT_FINGERS_MARGIN;

            bottomRightX = (PADDING / 2) + ((column + 1) * cellSide) + FAT_FINGERS_MARGIN;
            bottomRightY = (PADDING / 2) + ((row + 1) * cellSide) + FAT_FINGERS_MARGIN;
            solutionAreas[i] = new int[]{ topLeftX, topLeftY, bottomRightX, bottomRightY };
        }

        mFrameLayout.addView(mMazeView);
        mFingerLine = new FingerLine(this.getContext(), null, solutionAreas);
        mFingerLine.setEndingEventListener(this);
        mFrameLayout.addView(mFingerLine);

        // Add start arrow pic
        int startCellArrowX = solutionAreas[mMazeView.lengthOfSolutionPath - 1][0] + 12 + FAT_FINGERS_MARGIN;
        int startCellArrowY = solutionAreas[mMazeView.lengthOfSolutionPath - 1][1] + 100 + FAT_FINGERS_MARGIN;
        startPoint.setX(startCellArrowX);
        startPoint.setY(startCellArrowY);
        startPoint.setVisibility(View.VISIBLE);

        // Add strawberry pic
        int endCellStrawberryX = solutionAreas[0][0] + 8 + FAT_FINGERS_MARGIN;
        int endCellStrawberryY = solutionAreas[0][1] + 10 + FAT_FINGERS_MARGIN;
        finishPoint.setX(endCellStrawberryX);
        finishPoint.setY(endCellStrawberryY);
        finishPoint.setVisibility(View.VISIBLE);
        mFingerLine.setFinishPointCoords(finishPoint.getX(), finishPoint.getY());

    }

    @Override
    public void onEndingEvent(boolean isMazeSolved) {
        Log.d(TAG, "onEndingEvent: " + isMazeSolved);
        if (isMazeSolved) {
            resultTimer.setTextColor(Color.GREEN);
            Toast.makeText(this.getContext(), R.string.maze_solved, Toast.LENGTH_SHORT).show();
            try {
                ServerApi.getInstance().sendMazeModuleResult(((double) TIMER.getMeasure())/1000, getContext()); // send to server
            } catch (JsonProcessingException | JSONException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error sending result", Toast.LENGTH_SHORT).show();
            }
        } else {
            resultTimer.setTextColor(Color.RED);
            Toast.makeText(this.getContext(), R.string.maze_not_solved, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTimerUpdate(long timeInMillis) {
        long seconds = timeInMillis / 1000;
        long millis = timeInMillis % 1000;
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(() -> resultTimer.setText(String.format(RESULT_TIMER_FORMAT, seconds, millis)));
        }
    }
}