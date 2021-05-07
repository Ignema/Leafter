package com.android.leafter.ui.writerChoose;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.leafter.R;

public class WriterChooseFragment extends Fragment {

    private WriterChooseViewModel writerChooseViewModel;

    public static WriterChooseFragment newInstance() {
        return new WriterChooseFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        writerChooseViewModel=new ViewModelProvider(this).get(WriterChooseViewModel.class);
        return inflater.inflate(R.layout.writer_choose_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TextView textView=view.findViewById(R.id.text_writer_choose);

        writerChooseViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textView.setText(s);
            }
        });

    }
}