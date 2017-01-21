package com.zpjj.musicapp.musicianmanagementapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseUser;
import com.zpjj.musicapp.musicianmanagementapp.R;
import com.zpjj.musicapp.musicianmanagementapp.models.Band;
import com.zpjj.musicapp.musicianmanagementapp.models.BandMember;
import com.zpjj.musicapp.musicianmanagementapp.services.BandService;
import com.zpjj.musicapp.musicianmanagementapp.services.UserService;

import java.util.ArrayList;
import java.util.List;

public class CreateBandActivity extends BaseAuthActivity {
    private EditText mBandNameField;
    private Button mCreateBandButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_band);
        mBandNameField = (EditText) findViewById(R.id.bandNameField);
        mCreateBandButton = (Button) findViewById(R.id.createBandButton);
        mCreateBandButton.setOnClickListener(listener -> {
            onCreateBandBandButtonClick();
        });

    }

    private void onCreateBandBandButtonClick() {
        Band band = new Band();
        band.setMasterUID(mAuth.getCurrentUser().getUid());
        band.setName(mBandNameField.getText().toString());
        List members = new ArrayList();
        BandMember member = new BandMember();
        member.setUuid(mAuth.getCurrentUser().getUid());
        member.setConfirmed(true);
        members.add(member);
        BandService bandService = new BandService();
        bandService.addBand(band);

        UserService userService = new UserService();
        userService.addBandForUser(mAuth.getCurrentUser(), band);
    }

}
