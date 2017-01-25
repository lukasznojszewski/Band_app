package com.zpjj.musicapp.musicianmanagementapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zpjj.musicapp.musicianmanagementapp.R;
import com.zpjj.musicapp.musicianmanagementapp.models.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 25.01.17.
 */

public class SongListAdapter extends BaseAdapter {
    List<Song> data = new ArrayList<>();
    Context context;
    LayoutInflater layoutInflater;

    public SongListAdapter(Context context, List<Song> data) {
        super();
        this.data = data;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Song getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.fragment_choose_current_song_item, null);
        TextView author = (TextView) convertView.findViewById(R.id.song_author_field);
        author.setText(data.get(position).getAuthor());
        TextView title = (TextView) convertView.findViewById(R.id.song_title_field);
        title.setText(data.get(position).getTitle());
        return convertView;
    }

}
