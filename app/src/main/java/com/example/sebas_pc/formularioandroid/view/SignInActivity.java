package com.example.sebas_pc.formularioandroid.view;

import android.app.usage.NetworkStats;
import android.content.Intent;
import android.net.Network;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.sebas_pc.formularioandroid.R;
import com.example.sebas_pc.formularioandroid.model.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class SignInActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        findViewById(R.id.sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        comeIn();
    }
    void comeIn(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            FirebaseDatabase.getInstance().getReference("users")
                    .child(userId)
                    .setValue(new User(userId, firebaseUser.getDisplayName(), firebaseUser.getEmail()));
            startActivity(new Intent(this, MenuActivity.class));
            finish();
        }
    }
    void signIn(){
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                               // new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                               // new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build()))
                        .build(),
                RC_SIGN_IN);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                comeIn();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    return;
                }
            }
        }
    }
}

