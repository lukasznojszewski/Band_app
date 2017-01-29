package com.zpjj.musicapp.musicianmanagementapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.zpjj.musicapp.musicianmanagementapp.R;
import com.zpjj.musicapp.musicianmanagementapp.activities.BaseAuthActivity;
import com.zpjj.musicapp.musicianmanagementapp.activities.auth.BaseActivity;
import com.zpjj.musicapp.musicianmanagementapp.adapters.BandListAdapter;
import com.zpjj.musicapp.musicianmanagementapp.models.Band;
import com.zpjj.musicapp.musicianmanagementapp.navigation.NavigationListener;
import com.zpjj.musicapp.musicianmanagementapp.services.BandService;
import com.zpjj.musicapp.musicianmanagementapp.services.UserService;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;


public class ChooseBandFragment extends Fragment {
    Spinner bandListSpinner;
    Button chooseBandButton;
    List<Band> bandList = new ArrayList<>();
    BandListAdapter bandListAdapter;
    public ChooseBandFragment() {

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
        Observable.create(
                subscriber -> {
                    UserService us = new UserService();
                    us.getUserInfo(((BaseAuthActivity)getActivity()).getUserInfo().getId()).subscribe(info -> {
                        ((BaseAuthActivity)getActivity()).setUserInfo(info);
                        for (String bandId: ((BaseAuthActivity)getActivity()).getUserInfo().getBands().keySet()) {
                            bandService.getBandById(bandId).subscribe(
                                    band -> {
                                        subscriber.onNext(band);
                                        if(((BaseAuthActivity)getActivity()).getUserInfo().getBands().keySet().size() == bandList.size()) {
                                            subscriber.onCompleted();
                                        }
                                    },
                                    err-> {
                                        ((BaseAuthActivity)getActivity()).logout();
                                    }
                            );
                        }
                    }, throwable -> {
                        ((BaseAuthActivity)getActivity()).logout();
                    });

                }
        ).subscribe(
                band ->  {
                    bandList.add((Band) band);
                },
                err -> {
                    err.printStackTrace();
                },
                () -> {
                    if(((BaseAuthActivity) getActivity()).getSelectedBand() != null) {
                        bandListSpinner.setSelection(bandListAdapter.getPosition(((BaseAuthActivity) getActivity()).getSelectedBand()));
                    } else {
                        bandListSpinner.setSelection(0);
                    }

                }
        );
        return view;
    }

    private void onChooseBand() {
        if(bandListSpinner.getSelectedItem() != null) {
            ((BaseAuthActivity)getActivity()).setSelectedBand((Band) bandListSpinner.getSelectedItem());
            updateMenuItemList();
            NavigationListener listener = new NavigationListener((BaseAuthActivity) getActivity());
            if(!listener.redirectOnNotify((BaseAuthActivity) getActivity())) {
                navigateToCurrentSong();
            }
            updateAppTitleInMenu();
        } else {
            Toast.makeText(getContext(), "Wybierz zespół",
                    Toast.LENGTH_LONG).show();
        }

    }

    private void updateMenuItemList() {
        String currentUserId = ((BaseAuthActivity) getActivity()).mAuth.getCurrentUser().getUid();
        String selectedBandMasterId = ((Band) bandListSpinner.getSelectedItem()).getMasterUID();
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        if(currentUserId.equals(selectedBandMasterId)) {
            menu.setGroupVisible(R.id.nav_master_band_items, true);
        } else {
            menu.setGroupVisible(R.id.nav_master_band_items, false);
        }
    }

    private void navigateToCurrentSong() {
        NavigationListener navigationListener = new NavigationListener((BaseAuthActivity)getActivity());
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationListener.onNavigationItemSelected(navigationView.getMenu().getItem(NavigationListener.CURRENT_SONG));
        navigationView.setCheckedItem(R.id.nav_current_song);
    }

    private void updateAppTitleInMenu() {
        TextView navAppName = (TextView) getActivity().findViewById(R.id.nav_app_title);
        navAppName.setText("BandApp - " + ((Band) bandListSpinner.getSelectedItem()).getName());
    }

}
