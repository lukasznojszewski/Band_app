package com.zpjj.musicapp.musicianmanagementapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.zpjj.musicapp.musicianmanagementapp.R;
import com.zpjj.musicapp.musicianmanagementapp.activities.BaseAuthActivity;
import com.zpjj.musicapp.musicianmanagementapp.adapters.BandListAdapter;
import com.zpjj.musicapp.musicianmanagementapp.models.Band;
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
        bandListAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        bandListSpinner.setAdapter(bandListAdapter);
        bandListSpinner.setAdapter(bandListAdapter);
        chooseBandButton = (Button) view.findViewById(R.id.choose_band);
        chooseBandButton.setOnClickListener(l-> {
            onChooseBand();
        });
        loadCurrentUserBandList();
        return view;
    }

    /**
     * Load list of bands for current User
     */
    private void loadCurrentUserBandList() {
        BandService bandService = ((BaseAuthActivity) getActivity()).getmBandService();
        UserService userService = ((BaseAuthActivity) getActivity()).getmUserService();
        Observable.create(
                subscriber -> {
                    userService.getUserInfo(((BaseAuthActivity)getActivity()).getUserInfo().getId()).subscribe(info -> {
                        ((BaseAuthActivity)getActivity()).setUserInfo(info);
                        Integer userBandCount = info.getBands().keySet().size();
                        for (String bandId: info.getBands().keySet()) {
                            bandService.getBandById(bandId).subscribe(
                                    band -> {
                                        subscriber.onNext(band);
                                        if(userBandCount == bandList.size()) {
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
    }

    /**
     * Set selected band and navigate to current song view
     */
    private void onChooseBand() {
        if(bandListSpinner.getSelectedItem() != null) {
            ((BaseAuthActivity)getActivity()).setSelectedBand((Band) bandListSpinner.getSelectedItem());
            ((BaseAuthActivity) getActivity()).navigateToCurrentSong();
        } else {
            Toast.makeText(getContext(), R.string.selectBand,
                    Toast.LENGTH_LONG).show();
        }
    }
}
