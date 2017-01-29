package com.zpjj.musicapp.musicianmanagementapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.zpjj.musicapp.musicianmanagementapp.R;
import com.zpjj.musicapp.musicianmanagementapp.activities.BaseAuthActivity;
import com.zpjj.musicapp.musicianmanagementapp.adapters.BandListAdapter;
import com.zpjj.musicapp.musicianmanagementapp.models.Band;
import com.zpjj.musicapp.musicianmanagementapp.models.UserInfo;
import com.zpjj.musicapp.musicianmanagementapp.services.BandService;
import com.zpjj.musicapp.musicianmanagementapp.services.FirebaseService;
import com.zpjj.musicapp.musicianmanagementapp.services.NotifyService;
import com.zpjj.musicapp.musicianmanagementapp.services.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class JoinExistingBandFragment extends Fragment {
    private BandService bandService;
    Spinner bandListSpinner;
    Button chooseBandButton;
    BandListAdapter bandListAdapter;
    List<Band> availableBands = new ArrayList<>();
    public JoinExistingBandFragment() {
        bandService = new BandService();
        bandService.getBands().subscribe(
                bands -> {
                    System.out.println(bands);
                    availableBands.addAll(bands);
                },
                err-> {
                    ((BaseAuthActivity)getActivity()).logout();
                }
        );
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_join_existing_band, container, false);
        bandListSpinner = (Spinner) view.findViewById(R.id.band_list);

        bandListAdapter = new BandListAdapter(getContext(), R.layout.spinner,availableBands);
        bandListSpinner.setAdapter(bandListAdapter);

        chooseBandButton = (Button) view.findViewById(R.id.join_band);
        chooseBandButton.setOnClickListener(l-> {
            onJoinBand();
        });
        return view;
    }

    private void onJoinBand() {
        BandService bandService = new BandService();
        bandService.createUserJoinBandRequest((Band) bandListSpinner.getSelectedItem(), ((BaseAuthActivity)getActivity()).mAuth.getCurrentUser());
        UserService us = new UserService();
        us.getUserInfo(((Band) bandListSpinner.getSelectedItem()).getMasterUID()).subscribe(
                info -> {
                    ((BaseAuthActivity)getActivity()).getNotifyService().sendJoinRequestNotification(info.getFirebaseToken());
                },
                err -> {
                    err.printStackTrace();
                }
        );
        Toast toast = Toast.makeText(getContext(), "Czekaj na akceptację Mastera zespołu", Toast.LENGTH_LONG);
        toast.show();
    }

}
