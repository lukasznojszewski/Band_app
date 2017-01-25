package com.zpjj.musicapp.musicianmanagementapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.zpjj.musicapp.musicianmanagementapp.R;
import com.zpjj.musicapp.musicianmanagementapp.activities.BaseAuthActivity;
import com.zpjj.musicapp.musicianmanagementapp.models.Band;
import com.zpjj.musicapp.musicianmanagementapp.models.UserInfo;
import com.zpjj.musicapp.musicianmanagementapp.navigation.NavigationListener;
import com.zpjj.musicapp.musicianmanagementapp.services.BandService;
import com.zpjj.musicapp.musicianmanagementapp.services.UserService;


public class AddBandFragment extends Fragment {
    EditText bandNameField;
    Button createBandButton;
    public AddBandFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_band, container, false);
        bandNameField = (EditText) view.findViewById(R.id.bandNameField);
        createBandButton = (Button) view.findViewById(R.id.createBandButton);
        createBandButton.setOnClickListener(v -> {
            addBand();
        });
        return view;
    }

    private void addBand() {
        BandService bandService = new BandService();
        Band band = new Band(this.bandNameField.getText().toString(), ((BaseAuthActivity)getActivity()).mAuth.getCurrentUser().getUid());
        bandService.addBand(band);

        UserInfo info = (UserInfo) getActivity().getIntent().getSerializableExtra("USER_INFO");
        info.getBands().put(band.getId(), true);
        if(info != null) {
            UserService userService = new UserService();
            userService.createOrUpdateUserInfo(((BaseAuthActivity)getActivity()).mAuth.getCurrentUser(), info);
        }
        NavigationListener navigationListener = new NavigationListener((BaseAuthActivity)getActivity());
        navigationListener.changeFragment((BaseAuthActivity)getActivity(), ChooseBandFragment.class, R.id.nav_choose_band);
    }
}
