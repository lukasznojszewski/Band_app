package com.zpjj.musicapp.musicianmanagementapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.zpjj.musicapp.musicianmanagementapp.R;
import com.zpjj.musicapp.musicianmanagementapp.activities.auth.BaseActivity;
import com.zpjj.musicapp.musicianmanagementapp.activities.MainActivity;
import com.zpjj.musicapp.musicianmanagementapp.activities.auth.AuthActivity;
import com.zpjj.musicapp.musicianmanagementapp.exceptions.UserNotFoundException;
import com.zpjj.musicapp.musicianmanagementapp.models.UserInfo;
import com.zpjj.musicapp.musicianmanagementapp.services.UserService;

public class SignInTabFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "AUTH";
    private static final int RC_SIGN_IN = 48451;
    Button signInButton;
    EditText emailField;
    EditText passwordField;
    SignInButton googleSignInButton;
    GoogleApiClient mGoogleApiClient;


    public SignInTabFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.auth_fragment_sign_in_tab,container,false);
        signInButton = (Button) view.findViewById(R.id.sign_in);
        emailField = (EditText) view.findViewById(R.id.emailField);
        passwordField = (EditText) view.findViewById(R.id.passwordField);
        signInButton.setOnClickListener(this);
        googleSignInButton =  (SignInButton) view.findViewById(R.id.google_sign_in_button);
        googleSignInButton.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        return view;
    }



    private void signIn(String email, String password, AuthActivity context) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        context.showProgressDialog();
        context.mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(context , new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        context.hideProgressDialog();
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, task.getResult().getUser().getUid());
                            UserService userService = new UserService();
                            userService.getUserInfo(task.getResult().getUser()).subscribe(
                                    data -> {
                                        Intent i = new Intent(context, MainActivity.class);
                                        i.putExtra("USER_INFO", data);
                                        context.startActivity(i);
                                    }, err -> {
                                        if(err instanceof UserNotFoundException) {
                                            UserInfo info = new UserInfo();
                                            info.setEmail(task.getResult().getUser().getEmail());
                                            userService.createOrUpdateUserInfo(task.getResult().getUser(), info);
                                            Intent i = new Intent(context, MainActivity.class);
                                            i.putExtra("USER_INFO", info);
                                            context.startActivity(i);
                                        }
                                    }
                            );

                        }
                    }
                });

    }


    private boolean validateForm() {
        boolean valid = true;

        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Required.");
            valid = false;
        } else {
            emailField.setError(null);
        }

        String password = passwordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Required.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in:
                signIn(emailField.getText().toString(), passwordField.getText().toString(), (AuthActivity)getActivity());
                break;
            case R.id.google_sign_in_button:
                googleSignIn();
                break;
        }
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);
        } else {
            // Google Sign In failed, update UI appropriately
            // ...
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        AuthActivity context = (AuthActivity) getActivity();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        context.showProgressDialog();
        ((BaseActivity)getActivity()).mAuth.signInWithCredential(credential)
                .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        context.hideProgressDialog();
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            UserService userService = new UserService();
                            userService.getUserInfo(task.getResult().getUser()).subscribe(
                                        data -> {
                                            Intent i = new Intent(context, MainActivity.class);
                                            i.putExtra("USER_INFO", data);
                                            context.startActivity(i);
                                        }, err -> {
                                            if(err instanceof UserNotFoundException) {
                                                UserInfo info = new UserInfo();
                                                info.setEmail(task.getResult().getUser().getEmail());
                                                userService.createOrUpdateUserInfo(task.getResult().getUser(), info);
                                                Intent i = new Intent(context, MainActivity.class);
                                                i.putExtra("USER_INFO", info);
                                                context.startActivity(i);
                                            }
                                        }
                                );

                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



}
