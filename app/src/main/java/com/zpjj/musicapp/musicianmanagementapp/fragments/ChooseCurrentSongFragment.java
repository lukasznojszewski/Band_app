package com.zpjj.musicapp.musicianmanagementapp.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.zpjj.musicapp.musicianmanagementapp.R;
import com.zpjj.musicapp.musicianmanagementapp.activities.BaseAuthActivity;
import com.zpjj.musicapp.musicianmanagementapp.adapters.SongListAdapter;
import com.zpjj.musicapp.musicianmanagementapp.models.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseCurrentSongFragment extends Fragment {
    SongListAdapter arrayAdapter;
    List<Song> songs = new ArrayList<>();
    Button mAddSongButton;
    ListView listView;
    public ChooseCurrentSongFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_current_song, container, false);
        loadBandSongs();
        listView = (ListView) view.findViewById(R.id.songlist);
        arrayAdapter = new SongListAdapter(getContext(),songs);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(onSongSelect());
        mAddSongButton = (Button) view.findViewById(R.id.add_song_button);
        mAddSongButton.setOnClickListener(v -> {
            showAddSongDialog();
        });

        return view;
    }

    private AdapterView.OnItemClickListener onSongSelect() {
        return (parent, view, position, id) -> {
            view.setSelected(true);
            String currentBandId = ((BaseAuthActivity)getActivity()).getSelectedBand().getId();
            ((BaseAuthActivity) getActivity()).getmBandService().setCurrentSongForBand(currentBandId, songs.get(position));
            Toast t = Toast.makeText(getContext(), R.string.newSongWasSelected, Toast.LENGTH_LONG);
            t.show();
            for (String userId:
            ((BaseAuthActivity)getActivity()).getSelectedBand().getUsers().keySet()) {
                ((BaseAuthActivity) getActivity()).getmUserService().getUserInfo(userId).subscribe(info -> {
                    ((BaseAuthActivity)getActivity()).getNotifyService().sendChangeSongNotification(info.getFirebaseToken(), songs.get(position));

                }, err-> {
                            ((BaseAuthActivity)getActivity()).logout();
                        });
            }
        };

    }

    /**
     * show dialog for add song for band
     */
    private void showAddSongDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setTitle(getString(R.string.addSong));
        dialog.setContentView(R.layout.fragment_choose_current_song_add_song_dialog);

        EditText author = (EditText) dialog.findViewById(R.id.add_song_author_field);
        EditText title = (EditText) dialog.findViewById(R.id.add_song_title_field);
        Button cancelButton = (Button) dialog.findViewById(R.id.add_song_reject_button);
        cancelButton.setOnClickListener(v -> {
            dialog.dismiss();
        });
        Button addButton = (Button) dialog.findViewById(R.id.add_song_add_button);
        addButton.setOnClickListener(v -> {
            System.out.println(author.getText() + " " + title.getText());
            addSong(author.getText().toString(), title.getText().toString());
            dialog.dismiss();
        });

        dialog.show();
    }

    /**
     * add song for band
     * @param author song author
     * @param title song title
     */
    private void addSong(String author, String title) {
        String currentBandId = ((BaseAuthActivity)getActivity()).getSelectedBand().getId();
        Song song = new Song.Builder()
                .setAuthor(author)
                .setTitle(title)
                .build();
        ((BaseAuthActivity) getActivity()).getmBandService().addSongForBand(currentBandId, song);
        loadBandSongs();
    }

    /**
     * load band songs
     */
    public void loadBandSongs() {
        String currentBandId = ((BaseAuthActivity)getActivity()).getSelectedBand().getId();
        ((BaseAuthActivity) getActivity()).getmBandService().getBandById(currentBandId).subscribe(
                band -> {
                    ((BaseAuthActivity)getActivity()).setSelectedBand(band);
                    songs = new ArrayList<>(band.getSongs().values());
                    arrayAdapter = new SongListAdapter(getContext(), songs);
                    listView.setAdapter(arrayAdapter);
                },
                err-> {
                    ((BaseAuthActivity)getActivity()).logout();
                }
        );
    }
}
