package com.eltech.snc.ui.labyrinth;

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

public class LabyrinthFragment extends Fragment {

    private LabyrinthViewModel labyrinthViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        labyrinthViewModel = ViewModelProviders.of(this).get(LabyrinthViewModel.class);
        View root = inflater.inflate(R.layout.fragment_labyrinth, container, false);
//        final TextView textView = root.findViewById(R.id.text_slideshow);
//        labyrinthViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
}