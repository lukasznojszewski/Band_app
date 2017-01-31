package com.zpjj.musicapp.musicianmanagementapp.fragments;

import android.app.Dialog;
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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AcceptJoinBandFragment extends Fragment {
    private ArrayList<UserInfo> arrayList;
    private ArrayAdapter<UserInfo> arrayAdapter;
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

        ((BaseAuthActivity)getActivity()).getmBandService().getJoinRequestUsersToBand(((BaseAuthActivity)getActivity()).getSelectedBand().getId()).subscribe(
                userInfos -> {
                    arrayList.addAll(userInfos);
                    arrayAdapter.notifyDataSetChanged();
                },
                err-> {
                    ((BaseAuthActivity)getActivity()).logout();
                }
        );
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDecisionDialog(arrayList.get(position));
            }
        });
        return view;
    }

    /**
     * show decision dialog for selected user from join request list
     * you can accept or reject join request
     * @param userInfo selected user information object
     */
    private void showDecisionDialog(UserInfo userInfo) {
        Band currentBand = ((BaseAuthActivity)getActivity()).getSelectedBand();
        final Dialog dialog = new Dialog(getContext());
        dialog.setTitle(R.string.confirm_join_band);
        dialog.setContentView(R.layout.fragment_accept_join_band_dialog);
        TextView textView = (TextView) dialog.findViewById(R.id.join_user_title);
        textView.setText(userInfo.getEmail());

        Button reject = (Button) dialog.findViewById(R.id.reject_join_band_button);
        reject.setOnClickListener(l-> {
            ((BaseAuthActivity)getActivity()).getmBandService().rejectUserJoindRequest(currentBand, userInfo.getId());
            ((BaseAuthActivity)getActivity()).getNotifyService().sendRejectJoinBandNotification(userInfo.getFirebaseToken());
            dialog.dismiss();
        });
        Button accept = (Button) dialog.findViewById(R.id.accept_join_band_button);
        accept.setOnClickListener(l-> {
            ((BaseAuthActivity)getActivity()).getmBandService().acceptUserJoinRequest(currentBand, userInfo.getId());
            ((BaseAuthActivity)getActivity()).getNotifyService().sendAcceptJoinBandNotification(userInfo.getFirebaseToken());
            dialog.dismiss();
        });
        dialog.show();
    }

}
