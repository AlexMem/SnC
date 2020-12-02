package com.eltech.snc.ui.platform;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentActivity;
import com.eltech.snc.R;
import com.eltech.snc.maze.TimerUpdateEventListener;
import com.eltech.snc.utils.ServerApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;

import java.util.*;

public class PlatformFragment extends Fragment implements TimerUpdateEventListener {
    private static final int CLIPPING_POINTS_TO_FINISH = 3;
    private static final String RESULT_TIMER_FORMAT = "Timer: %s,%s s";
    private static final String AVG_TIMER_FORMAT = "Avg: %s,%s s";
    private static final String CLIPPING_POINT_COUNTER_FORMAT = "Clipping points touched: %s of a " + CLIPPING_POINTS_TO_FINISH;
    public static final com.eltech.snc.utils.Timer TIMER = new com.eltech.snc.utils.Timer();
    private final float executionRate = 0.00125f; // in sec
    private Ball ball;
    private BallSensor ballSensor;
    private BallView ballView;
    private TextView pitchText;
    private TextView rollText;
    private TextView resultAvg;
    private TextView resultTimer;
    private TextView cpCounterText;
    private Timer gameTimer;
    private List<Long> clippingTimes;
    private ImageButton startButton;
    private ImageButton refreshButton;
    private int cPCounter;

    final Handler coordinatorChangeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ballView.setCoord(ball.posX, ball.posY);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ballSensor = new BallSensor(this);
//        setContentView(R.layout.ball);
        ball = new Ball();
        TIMER.setTimerUpdateEventListener(this);
    }

    private void start() {
        resultAvg.setTextColor(Color.BLACK);
        cPCounter = 0;
        ballView.spawnClippingPoint();
        clippingTimes = new ArrayList<>();
        clippingTimes.add(0L);
        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
            public void run() {
//                ball.calculateAcceleration();
                ball.updateVelocity(executionRate);
                ball.updatePosition(executionRate);
                coordinatorChangeHandler.sendEmptyMessage(0);
            }
        }, 0, (int) (executionRate * 800));
        TIMER.start();
        deactivateButton(startButton);
        activateButton(refreshButton);
    }

    public void stop(boolean isRefresh, long avg) {
        gameTimer.cancel();
        TIMER.stop();
        ballView.removeClippingPoint();
        ball.resetPosition();
        if (isRefresh) {
            resultAvg.setText(String.format(AVG_TIMER_FORMAT, 0, 0));
            resultTimer.setText(String.format(RESULT_TIMER_FORMAT, 0, 0));
            cpCounterText.setText(String.format(CLIPPING_POINT_COUNTER_FORMAT, 0));
            getActivity().runOnUiThread(() -> Toast.makeText(getContext(), R.string.ball_trial_refreshed, Toast.LENGTH_SHORT).show());
        } else {
            resultAvg.setTextColor(Color.GREEN);
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), R.string.ball_trial_finished, Toast.LENGTH_SHORT).show();
                try {
                    ServerApi.getInstance().sendBallModuleResult(((double) avg)/1000, getContext()); // send to server
                } catch (JsonProcessingException | JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error sending result", Toast.LENGTH_SHORT).show();
                }
            });
        }
        deactivateButton(refreshButton);
        activateButton(startButton);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_platform, container, false);

        ballView = root.findViewById(R.id.ballview);
        ballView.setParent(this);
        ball.setBallView(ballView);
        ball.resetPosition();

        pitchText = root.findViewById(R.id.pitchtext);
        rollText = root.findViewById(R.id.rolltext);

        resultAvg = root.findViewById(R.id.resultAvg);
        resultTimer = root.findViewById(R.id.resultTimer);
        cpCounterText = root.findViewById(R.id.clippingPCounter);
        resultAvg.setText(String.format(AVG_TIMER_FORMAT, 0, 0));
        resultTimer.setText(String.format(RESULT_TIMER_FORMAT, 0, 0));
        cpCounterText.setText(String.format(CLIPPING_POINT_COUNTER_FORMAT, 0));

        startButton = root.findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> {
            start();
        });

        refreshButton = root.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(v -> {
            stop(true, 0);
        });
        deactivateButton(refreshButton);
        return root;
    }

    private void activateButton(final ImageButton button) {
        button.setClickable(true);
        button.setAlpha(1f);
    }

    private void deactivateButton(final ImageButton button) {
        button.setClickable(false);
        button.setAlpha(.25f);
    }

    @Override
    public void onResume() {
        super.onResume();
        ballSensor.registerListener();
        if (!ballSensor.hasAppropriateSensor()) {
            Toast.makeText(getContext(), "No appropriate sensors are available", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ballSensor.unregisterListener();
    }

    public Ball getBall() {
        return ball;
    }

    void setOrientationText(float orientation[]) {
        pitchText.setText(String.format(Locale.US, "Pitch:\t%2.4f", orientation[1]));
        rollText.setText(String.format(Locale.US, "Roll:\t%2.4f", orientation[2]));
        ball.calculateForce(-1 * orientation[1], orientation[2]);
    }

    public void clippingPointTouched() {
        clippingTimes.add(TIMER.getMeasure());
        long sum = 0L;
        for (int i = 1; i < clippingTimes.size(); ++i) {
            sum += clippingTimes.get(i) - clippingTimes.get(i - 1);
        }
        long avg = sum / (clippingTimes.size() - 1);
        long seconds = avg / 1000;
        long millis = avg % 1000;
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(() -> resultAvg.setText(String.format(AVG_TIMER_FORMAT, seconds, millis)));
        }
        Log.d("Ball", clippingTimes.toString());
        ++cPCounter;
        cpCounterText.setText(String.format(CLIPPING_POINT_COUNTER_FORMAT, cPCounter));
        if (cPCounter == CLIPPING_POINTS_TO_FINISH) {
            stop(false, avg);
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