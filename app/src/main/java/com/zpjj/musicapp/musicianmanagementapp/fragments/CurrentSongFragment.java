package com.zpjj.musicapp.musicianmanagementapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zpjj.musicapp.musicianmanagementapp.R;
import com.zpjj.musicapp.musicianmanagementapp.activities.BaseAuthActivity;

import java.util.Timer;
import java.util.TimerTask;

public class CurrentSongFragment extends Fragment {
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

    /**
     * refresh current song
     */
    private void refreshCurrentSong() {
        if(((BaseAuthActivity)getActivity()).getSelectedBand() != null) {
            String currentBandId = ((BaseAuthActivity)getActivity()).getSelectedBand().getId();
            ((BaseAuthActivity) getActivity()).getmBandService().getCurrentSongForBand(currentBandId).subscribe(song -> {
                if(song!= null) {
                    authorTextView.setText(song.getAuthor());
                    titleTextView.setText(song.getTitle());
                }
            });
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(t!= null) {
            t.cancel();
        }
    }
}
