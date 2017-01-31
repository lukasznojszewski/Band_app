package com.zpjj.musicapp.musicianmanagementapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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


public class AddBandFragment extends Fragment {
    EditText bandNameField;
    Button createBandButton;


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

    /**
     * create new band
     */
    private void addBand() {
        if(!validateForm()){
            return;
        }
        Band band = new Band(this.bandNameField.getText().toString(), ((BaseAuthActivity)getActivity()).mAuth.getCurrentUser().getUid());
        ((BaseAuthActivity) getActivity()).getmBandService().createOrUpdateBand(band);

        UserInfo info = (UserInfo) getActivity().getIntent().getSerializableExtra(getString(R.string.INTENT_LOGED_IN_USER_INFO));
        info.getBands().put(band.getId(), true);
        if(info != null) {
            ((BaseAuthActivity) getActivity()).getmUserService().createOrUpdateUserInfo(((BaseAuthActivity)getActivity()).mAuth.getCurrentUser(), info);
        }
        NavigationListener navigationListener = new NavigationListener((BaseAuthActivity)getActivity());
        navigationListener.changeFragment((BaseAuthActivity)getActivity(), ChooseBandFragment.class, R.id.nav_choose_band);
    }


    /**
     * validate band form
     * @return true if form is valid and false when invalid
     */
    private boolean validateForm() {
        boolean valid = true;

        if (TextUtils.isEmpty(bandNameField.getText().toString())) {
            bandNameField.setError("Required.");
            valid = false;
        } else {
            bandNameField.setError(null);
        }

        return valid;
    }
}
