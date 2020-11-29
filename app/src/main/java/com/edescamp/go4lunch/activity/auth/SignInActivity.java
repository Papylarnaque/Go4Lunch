package com.edescamp.go4lunch.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.activity.BaseActivity;
import com.edescamp.go4lunch.activity.MainActivity;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class SignInActivity extends BaseActivity {

    // --------------------
    // FOR DATA
    // --------------------

    private static final String TAG = "SIGN";

    // LOGIN REQUEST CODE
    public static final int LOGIN_FACEBOOK = 2;
    private static final int RC_SIGN_IN = 3;

    // MODEL
    private FirebaseAuth firebaseAuth;
    private CallbackManager facebookCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;

    FirebaseAuth.AuthStateListener mAuthListener = firebaseAuth -> authLogin();

    OAuthProvider.Builder provider = OAuthProvider.newBuilder(String.valueOf(R.string.twitter));

    // TODO : Fix Token issues for login - Facebook keeps the auth when Firebase is logged out ?
    // TODO : Link account if trying to log with other provider

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        firebaseAuth = FirebaseAuth.getInstance();

        configFacebookAuth();
        configGoogleAuth();
        configTwitterAuth();

        getCurrentUser();
        authLogin();

    }


    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // HANDLE PROVIDER RESULT
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // FACEBOOK
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_FACEBOOK) {
            if (resultCode == RESULT_OK){
            facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
        }
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (resultCode == RESULT_OK){
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = null;
                try {
                    account = task.getResult(ApiException.class);
                } catch (ApiException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                handleGoogleAccessToken(account.getIdToken());
            } else {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed");
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
                        //loginResult OK wen debugging
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

    // TWITTER AUTH
    private void configTwitterAuth() {
        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret)))
                .debug(true)
                .build();
        Twitter.initialize(twitterConfig);

        // TWITTER SIGN IN BUTTON
        TwitterLoginButton mLoginButton = findViewById(R.id.login_button_twitter);
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
                        authLogin();
                    } else {
                        // TODO Handle merging users account / linking providers to account
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
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            authLogin();
                        } else {
                            // TODO Handle merging users account / linking providers to account
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.signin_layout), "Google Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void authLogin() {
        if (isCurrentUserLogged()) {
            launchProgressBar();
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }


    private void launchProgressBar() {
        View mainLayout = findViewById(R.id.signin_main_layout);
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