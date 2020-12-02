package com.eltech.snc.ui.image_copying;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.divyanshu.draw.widget.DrawView;
import com.eltech.snc.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ImageCopyingFragment extends Fragment {
    private static final String EXPLANATION = "Обведите изображение не отрывая пальца";
    private static final String BEFORE = "Совпадение ?%";
    private static final String RESULT_FORMAT = "Совпадение %.1f%%";
    private static final int CALC_STEP = 2;
    private static final float VALID_ERROR = 1.5f;
    private static final Random RANDOMIZER = new Random();
    private static final List<Integer> PATTERNS_IDS = Arrays.asList(R.drawable.cloud_thin, R.drawable.flower,
            R.drawable.flag, R.drawable.map, R.drawable.pin);
    private static final List<Drawable> patternsDrawables = new ArrayList<>();

    private TextView resultText;
    private ImageView targetImageView;
    private DrawView drawView;
    private ImageButton refreshButton;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_image_copying, container, false);

        resultText = root.findViewById(R.id.resultText);
        resultText.setText(BEFORE);

        targetImageView = root.findViewById(R.id.targetImage);
        targetImageView.setDrawingCacheEnabled(true);

        drawView = root.findViewById(R.id.draw_view);
        drawView.setStrokeWidth(30);
        drawView.setBackgroundColor(Color.TRANSPARENT);
        drawView.setOnTouchListener((v, event) -> {
            drawView.onTouchEvent(event);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                processImages();
            }
            return true;
        });

        refreshButton = root.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(v -> refreshModule());

        if (patternsDrawables.isEmpty()) {
            Resources resources = getResources();
            for (Integer patternsId : PATTERNS_IDS) {
                patternsDrawables.add(resources.getDrawable(patternsId, null));
            }
        }
        targetImageView.setImageDrawable(getRandomPattern());

        return root;
    }

    private float calculateResult(final Bitmap expected, final Bitmap drawn) {
        int count = 0;
        int shouldBePainted = 0;
        int notMissed = 0;
        int missed = 0;
        int width = expected.getWidth();
        int height = expected.getHeight();
        for (int x = 0; x < width; x += CALC_STEP) {
            for (int y = 0; y < height; y += CALC_STEP) {
                count++;
                int expectedColor = expected.getPixel(x, y);
                int drawnColor = drawn.getPixel(x, y);
                if (expectedColor != Color.TRANSPARENT) {
                    ++shouldBePainted;
                }
                if (drawnColor != Color.WHITE) {
                    if (expectedColor != Color.TRANSPARENT) {
                        ++notMissed;
                    } else {
                        ++missed;
                    }
                }
            }
        }
        shouldBePainted /= VALID_ERROR;
//        float result = Math.min(Math.max(notMissed - missed, 0), shouldBePainted) / (float) shouldBePainted;
        float result = Math.max(((float)notMissed / shouldBePainted) - ((float)missed / notMissed / 2), 0);
        //float result = ((float) notMissed / shouldBePainted + (float)(count - shouldBePainted - missed) / (count - shouldBePainted) * 0.3F) / 2;
        System.out.println("Should " + shouldBePainted + ", not missed " + notMissed + ", missed " + missed + ", result " + result);
        return result;
    }

    private void refreshModule() {
        targetImageView.destroyDrawingCache();
        targetImageView.setImageDrawable(getRandomPattern());
        drawView.clearCanvas();
        drawView.setAlpha(100);
        drawView.setEnabled(true);
        resultText.setText(BEFORE);
    }

    private void processImages() {
        System.out.println("Processing image ...");
        drawView.setEnabled(false);
        drawView.setAlpha(0);
        Bitmap drawnImage = drawView.getBitmap();
        Bitmap targetImage = targetImageView.getDrawingCache();
        float result = calculateResult(targetImage, drawnImage);
        resultText.setText(String.format(RESULT_FORMAT, 100 * result));
    }

    private Drawable getRandomPattern() {
        return patternsDrawables.get(RANDOMIZER.nextInt(patternsDrawables.size()));
    }
}