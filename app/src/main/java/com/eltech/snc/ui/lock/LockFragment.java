package com.eltech.snc.ui.lock;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.eltech.snc.R;

import java.util.ArrayList;
import java.util.List;

public class LockFragment extends Fragment {
    private String TAG = this.getClass().getSimpleName();
    private List<Float> xPoints;
    private List<Float> yPoints;

    public static LockFragment newInstance() {
        Bundle args = new Bundle();
        LockFragment fragment = new LockFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lock, container, false);
        Fragment lockFragment = this;

        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return this.onTouchEvent(event);
            }

            private boolean onTouchEvent(MotionEvent event) {
                int index = event.getActionIndex();
                int action = event.getActionMasked();
                int pointerId = event.getPointerId(index);

                switch(action) {
                    case MotionEvent.ACTION_DOWN:
                        xPoints= new ArrayList<>();
                        yPoints= new ArrayList<>();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.d(TAG, "X: " + event.getX() + " Y: " + event.getY());
                        xPoints.add(event.getX());
                        yPoints.add(event.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "x " + xPoints);
                        Log.d(TAG, "y " + yPoints);

                        if (!xPoints.isEmpty()) {
                            getActivity().getSupportFragmentManager().beginTransaction().hide(lockFragment).commit();
                        }
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                return true;
            }
        });

        return root;
    }
}