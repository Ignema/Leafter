package com.android.leafter.ui.writer;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.leafter.R;


public class WriterFragment  extends Fragment {

    private WriterViewModel writerViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        writerViewModel =
                new ViewModelProvider(this).get(WriterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_writer, container, false);
        final TextView textView = root.findViewById(R.id.text_writer);
        writerViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
