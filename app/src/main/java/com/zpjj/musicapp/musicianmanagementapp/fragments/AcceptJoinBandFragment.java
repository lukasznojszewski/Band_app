package com.zpjj.musicapp.musicianmanagementapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zpjj.musicapp.musicianmanagementapp.R;
import com.zpjj.musicapp.musicianmanagementapp.activities.BaseAuthActivity;
import com.zpjj.musicapp.musicianmanagementapp.models.Band;
import com.zpjj.musicapp.musicianmanagementapp.models.UserInfo;
import com.zpjj.musicapp.musicianmanagementapp.services.BandService;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AcceptJoinBandFragment extends Fragment {
    private ArrayList<UserInfo> arrayList;
    private ArrayAdapter<UserInfo> arrayAdapter;
    private BandService mBandService;
    public AcceptJoinBandFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accpet_join_band, container, false);
        ListView listView=(ListView)view.findViewById(R.id.listv);
        arrayAdapter= new ArrayAdapter<UserInfo>(getContext(), R.layout.fragment_accept_join_band_item, R.id.txtitem,arrayList);
        mBandService = new BandService();
        mBandService.getJoinRequestUsersToBand(((BaseAuthActivity)getActivity()).getSelectedBand().getId()).subscribe(
                userInfos -> {
                    arrayList.addAll(userInfos);
                    arrayAdapter.notifyDataSetChanged();
                }
        );
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDescisionDialog(arrayList.get(position));
            }
        });
        return view;
    }

    private void showDescisionDialog(UserInfo info) {
        Band currentBand = ((BaseAuthActivity)getActivity()).getSelectedBand();
        final Dialog dialog = new Dialog(getContext());
        dialog.setTitle(R.string.confirm_join_band);
        dialog.setContentView(R.layout.fragment_accept_join_band_dialog);
        TextView textView = (TextView) dialog.findViewById(R.id.join_user_title);
        textView.setText(info.getEmail());

        Button reject = (Button) dialog.findViewById(R.id.reject_join_band_button);
        reject.setOnClickListener(l-> {
            mBandService.rejectUserJoindRequest(currentBand, info.getId());
            dialog.dismiss();
        });
        Button accept = (Button) dialog.findViewById(R.id.accept_join_band_button);
        accept.setOnClickListener(l-> {
            mBandService.acceptUserJoinRequest(currentBand, info.getId());
            dialog.dismiss();
        });
        dialog.show();
    }

}
