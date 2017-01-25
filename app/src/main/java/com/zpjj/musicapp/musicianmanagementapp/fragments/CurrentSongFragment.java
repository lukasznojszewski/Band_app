package com.zpjj.musicapp.musicianmanagementapp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zpjj.musicapp.musicianmanagementapp.R;
import com.zpjj.musicapp.musicianmanagementapp.activities.BaseAuthActivity;
import com.zpjj.musicapp.musicianmanagementapp.activities.auth.BaseActivity;
import com.zpjj.musicapp.musicianmanagementapp.services.BandService;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CurrentSongFragment extends Fragment {
    BandService mBandService;
    TextView authorTextView;
    TextView titleTextView;
    Timer t;
    public CurrentSongFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_song, container, false);
        authorTextView = (TextView) view.findViewById(R.id.curent_song_author);
        titleTextView = (TextView) view.findViewById(R.id.curent_song_title);
        mBandService = new BandService();
        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refreshCurrentSong();
            }
        },0, 5000);
        refreshCurrentSong();

        return view;
    }

    private void refreshCurrentSong() {
        String currentBandId = ((BaseAuthActivity)getActivity()).getSelectedBand().getId();
        mBandService.getCurrentSongForBand(currentBandId).subscribe(song -> {
            if(song!= null) {
                authorTextView.setText(song.getAuthor());
                titleTextView.setText(song.getTitle());
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(t!= null) {
            t.cancel();
        }
    }
}
