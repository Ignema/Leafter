package com.android.leafter;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class MusicFragment extends Fragment {

    Button btnMusic;
    public MusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View musicFragmentView = inflater.inflate(R.layout.fragment_music, container, false);

        return musicFragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        btnMusic = (Button) getActivity().findViewById(R.id.btnMusic);
        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MusicPlayer_Activity.class);
                startActivity(intent);
//                Toast.makeText(getContext(), "Hello", Toast.LENGTH_LONG).show();
            }
        });
    }
}