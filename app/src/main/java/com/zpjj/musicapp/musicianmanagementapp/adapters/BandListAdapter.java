package com.zpjj.musicapp.musicianmanagementapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zpjj.musicapp.musicianmanagementapp.R;
import com.zpjj.musicapp.musicianmanagementapp.models.Band;

import java.util.List;

public class BandListAdapter extends ArrayAdapter<Band> {
    List<Band> values;
    public BandListAdapter(Context context, int resource, List<Band> objects) {
        super(context, resource, objects);
        setDropDownViewResource(R.layout.spinner_dropdown_item);
        values = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(this.getContext());
        label.setText(values.get(position).getName());
        label.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
        notifyDataSetChanged();
        return label;
    }
    
}
