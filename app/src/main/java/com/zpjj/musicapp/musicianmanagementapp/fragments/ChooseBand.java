package com.zpjj.musicapp.musicianmanagementapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.zpjj.musicapp.musicianmanagementapp.R;
import com.zpjj.musicapp.musicianmanagementapp.activities.BaseAuthActivity;
import com.zpjj.musicapp.musicianmanagementapp.activities.auth.BaseActivity;
import com.zpjj.musicapp.musicianmanagementapp.adapters.BandListAdapter;
import com.zpjj.musicapp.musicianmanagementapp.models.Band;
import com.zpjj.musicapp.musicianmanagementapp.navigation.NavigationListener;
import com.zpjj.musicapp.musicianmanagementapp.services.BandService;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;


public class ChooseBand extends Fragment {
    Spinner bandListSpinner;
    Button chooseBandButton;
    List<Band> bandList = new ArrayList<>();
    BandListAdapter bandListAdapter;
    public ChooseBand() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_band, container, false);
        bandListSpinner = (Spinner) view.findViewById(R.id.band_list);

        bandListAdapter = new BandListAdapter(getContext(), R.layout.spinner,bandList);
        bandListSpinner.setAdapter(bandListAdapter);

        chooseBandButton = (Button) view.findViewById(R.id.choose_band);
        chooseBandButton.setOnClickListener(l-> {
            onChooseBand();
        });
        BandService bandService = new BandService();
        for (String bandId: ((BaseAuthActivity)getActivity()).getUserInfo().getBands().keySet()) {
               bandService.getBandById(bandId).subscribe(
                       band -> {
                            bandList.add(band);
                       }
               );
        }
        if(((BaseAuthActivity) getActivity()).getSelectedBand() != null) {

        }
        return view;
    }

    private void onChooseBand() {
        System.out.println(bandListSpinner.getSelectedItem());
        ((BaseAuthActivity)getActivity()).setSelectedBand((Band) bandListSpinner.getSelectedItem());
        NavigationListener navigationListener = new NavigationListener((BaseAuthActivity)getActivity());
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationListener.onNavigationItemSelected(navigationView.getMenu().getItem(0));
        navigationView.setCheckedItem(R.id.nav_current_song);
        TextView navAppName = (TextView) getActivity().findViewById(R.id.nav_app_title);
        navAppName.setText("BandApp - " + ((Band) bandListSpinner.getSelectedItem()).getName());

    }

}
