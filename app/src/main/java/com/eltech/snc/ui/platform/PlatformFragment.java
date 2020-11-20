package com.eltech.snc.ui.platform;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.eltech.snc.R;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class PlatformFragment extends Fragment {
    private final float executionRate = 0.00125f; // in sec
    private Ball ball;
    private BallSensor ballSensor;
    private BallView ballView;
    private TextView pitchText;
    private TextView rollText;

    final Handler coordinatorChangeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ballView.setCoord(ball.posX,ball.posY);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ballSensor = new BallSensor(this);
//        setContentView(R.layout.ball);
        ball = new Ball();
        Timer timer = new Timer();
        TimerTask timertask = new TimerTask() {
            public void run() {
//                ball.calculateAcceleration();
                ball.updateVelocity(executionRate);
                ball.updatePosition(executionRate);
                coordinatorChangeHandler.sendEmptyMessage(0);
            }
        };
        timer.schedule(timertask,0,(int)(executionRate*800));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_platform, container, false);
        ballView = root.findViewById(R.id.ballview);
        ballView.setParent(this);
        pitchText = root.findViewById(R.id.pitchtext);
        rollText = root.findViewById(R.id.rolltext);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        ballSensor.registerListener();
        if(!ballSensor.hasAppropriateSensor()) {
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
        rollText.setText(String.format(Locale.US,"Roll:\t%2.4f", orientation[2]));
        ball.calculateForce(-1 * orientation[1], orientation[2]);
    }
}