package com.eltech.snc.ui.image_copying;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
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

public class ImageCopyingFragment extends Fragment {
    private static final String RESULT_FORMAT = "Совпадение %.1f%%";
    private static final int CALC_STEP = 4;
    private static final float VALID_ERROR = 2.5f;

    private ImageCopyingViewModel imageCopyingViewModel;
    private TextView resultText;
    private ImageView targetImageView;
    private DrawView drawView;
    private ImageButton refreshButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        imageCopyingViewModel = ViewModelProviders.of(this).get(ImageCopyingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_image_copying, container, false);

        resultText = root.findViewById(R.id.resultText);

        targetImageView = root.findViewById(R.id.targetImage);
        targetImageView.setDrawingCacheEnabled(true);

        drawView = root.findViewById(R.id.draw_view);
        drawView.setBackgroundColor(Color.TRANSPARENT);

        refreshButton = root.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(v -> {
            Bitmap drawnImage = drawView.getBitmap();
            Bitmap targetImage = targetImageView.getDrawingCache();
            float result = calculateResult(targetImage, drawnImage);
            resultText.setText(String.format(RESULT_FORMAT, 100*result));
        });
        return root;
    }

    private float calculateResult(final Bitmap expected, final Bitmap drawn) {
        int shouldBePainted = 0;
        int notMissed = 0;
        int missed = 0;
        int width = expected.getWidth();
        int height = expected.getHeight();
        for (int x = 0; x < width; x += CALC_STEP) {
            for (int y = 0; y < height; y += CALC_STEP) {
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
        float result = Math.max(notMissed - missed, 0) / (float) shouldBePainted;
        System.out.println("Should " + shouldBePainted + ", not missed " + notMissed + ", missed " + missed + ", result " + result);
        return result;
    }
}