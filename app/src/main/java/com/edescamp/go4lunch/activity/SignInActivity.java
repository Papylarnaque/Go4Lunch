package com.edescamp.go4lunch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.util.CheckConnectivity;
import com.edescamp.go4lunch.util.SharedPrefs;
import com.edescamp.go4lunch.util.UserHelper;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class SignInActivity extends BaseActivity {

    // --------------------
    // FOR DATA
    // --------------------

    private static final String TAG = "SIGN";

    // LOGIN REQUEST CODE
    private static final int RC_SIGN_IN = 3;

    // MODEL
    public FirebaseAuth firebaseAuth;
    private CallbackManager facebookCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;

    SwipeRefreshLayout pullToRefresh;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // In case no connection is detected, enable pull to refresh on "no connection layout"
        pullToRefresh = findViewById(R.id.signin_no_connection_pull_to_refresh);
        pullToRefresh.setOnRefreshListener(() -> {
            checkConnectivity(); // your code
            pullToRefresh.setRefreshing(false);
        });

        checkConnectivity();
    }

    private void checkConnectivity() {
        if (!CheckConnectivity.isConnected(getApplicationContext())){
            pullToRefresh.setVisibility(View.VISIBLE);

        } else {
            pullToRefresh.setVisibility(View.GONE);
            firebaseAuth = FirebaseAuth.getInstance();

            if (SharedPrefs.getUserId(getApplicationContext()) != null) {
                startMainActivity();
            }

            configFacebookAuth();
            configGoogleAuth();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //--------------------- HANDLE PROVIDER RESULT---------//
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // FACEBOOK
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        Log.d(TAG, "requestCode: " + requestCode + " resultCode " + resultCode + " data " + data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (resultCode == RESULT_OK) {
                // Google Sign In was successful
                GoogleSignInAccount account = null;
                try {
                    account = task.getResult(ApiException.class);
                } catch (ApiException e) {
                    e.printStackTrace();
                }
                assert account != null;
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                handleGoogleAccessToken(account.getIdToken());
            } else {
                // Google Sign In failed
                Log.w(TAG, "Google sign in failed");

                Toast.makeText(SignInActivity.this, "Google Authentication Failed.",
                        Toast.LENGTH_SHORT).show();
                // ...
            }
        }

    }


    // FACEBOOK AUTH
    private void configFacebookAuth() {

        facebookCallbackManager = CallbackManager.Factory.create();
        // UI
        LoginButton facebookSignInButton = findViewById(R.id.login_button_facebook);
        facebookSignInButton.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                        // ...
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError", error);
                        // ...
                    }
                }
        );

        facebookSignInButton.setHeight((int) getResources().getDimension(R.dimen.signin_button_height));

    }

    // GOOGLE AUTH
    private void configGoogleAuth() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // GOOGLE SIGN IN BUTTON
        SignInButton googleSignInButton = findViewById(R.id.login_button_google);
        googleSignInButton.setOnClickListener(v -> signIn());
        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);

    }

    // FACEBOOK TOKEN
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        createUserInFirestore();

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(SignInActivity.this, "Facebook Authentication Failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // GOOGLE TOKEN
    private void handleGoogleAccessToken(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        createUserInFirestore();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Snackbar.make(findViewById(R.id.signin_layout), "Google Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }


    //-------------- USER LOGIN -------------------//

    private void createUserInFirestore() {
        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        // SAVE THE USER DETAILS
        SharedPrefs.saveUserId(getApplicationContext(), userId);

        String userUrlPicture = (Objects.requireNonNull(this.getCurrentUser()).getPhotoUrl() != null) ? Objects.requireNonNull(this.getCurrentUser().getPhotoUrl()).toString() : null;
        String userName = this.getCurrentUser().getDisplayName();
        String userMail = this.getCurrentUser().getEmail();

                UserHelper.createUser(userId, userName, userUrlPicture, userMail)
                        .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show());

        startMainActivity();
    }

    private void startMainActivity() {
        launchProgressBar();
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //-------------- UI -------------------//

    private void launchProgressBar() {
        View mainLayout = findViewById(R.id.signin_layout_main);
        mainLayout.setVisibility(View.INVISIBLE);
        View progressBar = findViewById(R.id.signin_progress_bar_layout);
        progressBar.setVisibility(View.VISIBLE);
    }

    // Override up action to avoid going back to main from up action
    @Override
    public void onBackPressed() {
        // Do nothing
    }


}