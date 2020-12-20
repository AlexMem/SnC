package com.eltech.snc.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.eltech.snc.R;
import com.eltech.snc.utils.ServerApi;
import com.eltech.snc.utils.StatisticEntity;

import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {
    private final Timer timer = new Timer();

    private HomeViewModel homeViewModel;

    private TextView imageCopyAvgText;
    private TextView imageCopyBestText;

    private TextView mazeAvgText;
    private TextView mazeBestText;

    private TextView platformAvgText;
    private TextView platformBestText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        imageCopyAvgText = root.findViewById(R.id.imageCopyAvgValueText);
        imageCopyBestText = root.findViewById(R.id.imageCopyBestValueText);

        mazeAvgText = root.findViewById(R.id.mazeAvgValueText);
        mazeBestText = root.findViewById(R.id.mazeBestValueText);

        platformAvgText = root.findViewById(R.id.platformAvgValueText);
        platformBestText = root.findViewById(R.id.platformBestValueText);

        if (ServerApi.getInstance().getUserId() == null) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    ServerApi.getInstance()
                             .getStatistics(getContext(), statistics -> getActivity().runOnUiThread(() -> updateStatistics(statistics)));
                }
            }, 1000);
        } else {
            ServerApi.getInstance()
                     .getStatistics(getContext(), statistics -> getActivity().runOnUiThread(() -> updateStatistics(statistics)));
        }
        return root;
    }

    public void updateStatistics(final StatisticEntity statistics) {
        imageCopyAvgText.setText(String.format("%.2f%%", statistics.getAverageStencil()));
        imageCopyBestText.setText(String.format("%.2f%%", statistics.getBestStencil()));

        mazeAvgText.setText(String.format("%.3f s", statistics.getAverageMaze()));
        mazeBestText.setText(String.format("%.3f s", statistics.getBestMaze()));

        platformAvgText.setText(String.format("%.0f pts", statistics.getAverageBall()));
        platformBestText.setText(String.format("%.0f pts", statistics.getBestBall()));
    }
}
