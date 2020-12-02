package com.eltech.snc.ui.lock;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.eltech.snc.R;
import com.eltech.snc.utils.ServerApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class LockFragment extends Fragment {
    private static final int REQUIRED_REGS_NUM = 10;
    private static final String LOCK_REG_TEXT = "Экран заблокирован. Первичная регистрация. Необходимо выполнить 10 жестов. Выполнено %s из " + REQUIRED_REGS_NUM + ".";
    private static final String LOCK_COMMON_TEXT = "Экран заблокирован. Выполните свой жест для разблокировки.";

    private String TAG = this.getClass().getSimpleName();
    private List<Float> xPoints;
    private List<Float> yPoints;
    private int newUserGesturesCounter = -1;
    private TextView lockText;

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

        lockText = root.findViewById(R.id.lockText);

        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return this.onTouchEvent(event);
            }

            private boolean onTouchEvent(MotionEvent event) {
                int index = event.getActionIndex();
                int action = event.getActionMasked();
                int pointerId = event.getPointerId(index);

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        xPoints = new ArrayList<>();
                        yPoints = new ArrayList<>();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.d(TAG, "X: " + event.getX() + " Y: " + event.getY());
                        xPoints.add(event.getX());
                        yPoints.add(event.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "x " + xPoints);
                        Log.d(TAG, "y " + yPoints);

                        try {
                            unlockProcedure();
                        } catch (JsonProcessingException | JSONException e) {
                            e.printStackTrace();
                        }
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                return true;
            }
        });

        return root;
    }

    private void unlockProcedure() throws JsonProcessingException, JSONException {
        if (xPoints.isEmpty()) return;

        Fragment lockFragment = this;
        ServerApi.getInstance().sendUnlockResult(xPoints, yPoints, getContext(), integer -> {
            if (newUserGesturesCounter >= REQUIRED_REGS_NUM) {
                getActivity().runOnUiThread(() -> {
                    if (integer != -1) {
                        Toast.makeText(getContext(), "ЭТО ВЫ", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "ЭТО НЕ ВЫ!!!", Toast.LENGTH_LONG).show();
                    }
                });
                getActivity().getSupportFragmentManager().beginTransaction().hide(lockFragment).commit();
            } else {
                ++newUserGesturesCounter;
                getActivity().runOnUiThread(() -> {
                    lockText.setText(String.format(LOCK_REG_TEXT, newUserGesturesCounter));
                });
            }
        });
    }

    public void checkUnlockRegs() {
        ServerApi.getInstance().getUnlockRegs(getContext(), integer -> {
            newUserGesturesCounter = integer;
            getActivity().runOnUiThread(() -> {
                if (newUserGesturesCounter < 10) {
                    lockText.setText(String.format(LOCK_REG_TEXT, newUserGesturesCounter));
                } else {
                    lockText.setText(LOCK_COMMON_TEXT);
                }
            });
        });
    }
}
