package com.zpjj.musicapp.musicianmanagementapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zpjj.musicapp.musicianmanagementapp.models.Band;

import java.util.List;

public class BandListAdapter extends ArrayAdapter<Band> {
    List<Band> values;
    public BandListAdapter(Context context, int resource, List<Band> objects) {
        super(context, resource, objects);
        values = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(this.getContext());
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getName());
        notifyDataSetChanged();
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(getContext());
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getName());

        return label;
    }

}
