package com.eltech.snc.utils;

import android.os.SystemClock;
import com.eltech.snc.maze.TimerUpdateEventListener;

import java.util.TimerTask;

public class Timer {
    private static final long DELAY = 0;
    private static final long PERIOD = 250;

    private long startT;
    private long endT;

    private java.util.Timer periodTimer;
    private TimerUpdateEventListener timerUpdateEventListener;

    public void start() {
        periodTimer = new java.util.Timer();
        endT = -1;
        startT = SystemClock.elapsedRealtime();
        periodTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (timerUpdateEventListener != null) {
                    timerUpdateEventListener.onTimerUpdate(getMeasure());
                }
            }
        }, DELAY, PERIOD);
    }

    public long stop() {
        if (periodTimer == null) return 0;
        endT = SystemClock.elapsedRealtime();
        long result = endT - startT;
        periodTimer.cancel();
        timerUpdateEventListener.onTimerUpdate(result);
        return result;
    }

    public long getMeasure() {
        if (endT == -1) {
            return SystemClock.elapsedRealtime() - startT;
        }
        return endT - startT;
    }

    public void setTimerUpdateEventListener(TimerUpdateEventListener timerUpdateEventListener) {
        this.timerUpdateEventListener = timerUpdateEventListener;
    }
}
