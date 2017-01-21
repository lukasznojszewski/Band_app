package com.zpjj.musicapp.musicianmanagementapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zpjj.musicapp.musicianmanagementapp.R;
import com.zpjj.musicapp.musicianmanagementapp.activities.auth.BaseActivity;

public class CurrentSongFragment extends Fragment {
    TextView text;
    public CurrentSongFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_song, container, false);
        text = (TextView) view.findViewById(R.id.curent_song_text);
        if(((BaseActivity)getContext()).mAuth.getCurrentUser() != null) {
            String useremail = ((BaseActivity) getContext()).mAuth.getCurrentUser().getEmail();
            text.setText("Witaj " + useremail);
        }
        return view;
    }
}
