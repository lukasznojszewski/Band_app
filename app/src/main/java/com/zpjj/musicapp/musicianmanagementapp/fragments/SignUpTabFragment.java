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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.zpjj.musicapp.musicianmanagementapp.R;
import com.zpjj.musicapp.musicianmanagementapp.activities.MainActivity;
import com.zpjj.musicapp.musicianmanagementapp.activities.auth.AuthActivity;


public class SignUpTabFragment extends Fragment {
    private static final String TAG = "SIGN_UP";
    private EditText emailField;
    private EditText passwordField;
    private EditText repeatPasswordField;
    private Button signUpBtn;
    public SignUpTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.auth_fragment_sign_up_tab, container, false);
        emailField = (EditText) view.findViewById(R.id.emailField);
        passwordField = (EditText) view.findViewById(R.id.passwordField);
        repeatPasswordField = (EditText) view.findViewById(R.id.repeatPasswordField);
        signUpBtn = (Button) view.findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(click -> {
            signUp();
        });
        return view;
    }

    private void signUp() {
        AuthActivity context = (AuthActivity) getContext();
        Log.d(TAG, "signUp:" + emailField.getText().toString());
        if (!validateForm()) {
            return;
        }

        context.showProgressDialog();
        context.mAuth.createUserWithEmailAndPassword(emailField.getText().toString(), passwordField.getText().toString()).addOnCompleteListener(context , new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                context.hideProgressDialog();
                if (!task.isSuccessful()) {
                   if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                       Toast.makeText(context, "Użytkownik już istnieje",
                               Toast.LENGTH_LONG).show();
                   } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                       Toast.makeText(context, "Za słabe hasło",
                               Toast.LENGTH_LONG).show();
                   }
                    Log.w(TAG, "signInWithCredential", task.getException());

                } else {
                    context.mViewPager.setCurrentItem(0);
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

        String repeatPassword = repeatPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            repeatPasswordField.setError("Required.");
            valid = false;
        } else if(!password.equals(repeatPassword)) {
                repeatPasswordField.setError("Hasła muszą być takie same");
        } else{
            repeatPasswordField.setError(null);
        }

        return valid;
    }

}
