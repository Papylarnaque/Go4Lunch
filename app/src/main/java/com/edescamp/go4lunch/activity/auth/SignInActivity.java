package com.edescamp.go4lunch.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.activity.BaseActivity;
import com.edescamp.go4lunch.activity.MainActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.LoginStatusCallback;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthProvider;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class SignInActivity extends BaseActivity {

    // TODO Override up action to avoid going back to main from up action

    // --------------------
    // FOR DATA
    // --------------------

    private String TAG = "SIGN";

    // LOGIN REQUEST CODE
    public static final int LOGIN_FACEBOOK = 2;
    private static final int RC_SIGN_IN = 3;

    // MODEL
    private FirebaseAuth firebaseAuth;
    private CallbackManager facebookCallbackManager;
    public GoogleSignInClient googleSignInClient;

    // UI
    private LoginButton facebookSignInButton;
    private SignInButton googleSignInButton;

    OAuthProvider.Builder provider = OAuthProvider.newBuilder(String.valueOf(R.string.twitter));

    // TODO : Fix Token issues for login - Facebook keeps the auth when Firebase is logged out ?
    // TODO : Link account if trying to log with other provider

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // INIT
        setContentView(R.layout.activity_signin);
        firebaseAuth = FirebaseAuth.getInstance();

        retrieveLoginStatus();

        // FACEBOOK
        facebookCallbackManager = CallbackManager.Factory.create();
        facebookSignInButton = findViewById(R.id.login_button_facebook);
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


        // TWITTER
        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret)))
                .debug(true)
                .build();
        Twitter.initialize(twitterConfig);

        TwitterLoginButton mLoginButton = findViewById(R.id.login_button_twitter);



        // GOOGLE SIGN IN BUTTON
        googleSignInButton = findViewById(R.id.login_button_google);
        // GOOGLE SIGN IN BUTTON SET ON CLICK LISTENER
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        // Set the dimensions of the sign-in button.
        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);

    }


    private void retrieveLoginStatus() {
        LoginManager.getInstance().retrieveLoginStatus(this, new LoginStatusCallback() {
            @Override
            public void onCompleted(AccessToken accessToken) {
                // User was previously logged in, can log them in directly here.
                // If this callback is called, a popup notification appears that says
                // "Logged in as <User Name>" } @Override public void onFailure() {
                // No access token could be retrieved for the user } @Override public void onError(Exception exception) {
                // An error occurred } });
                authLogin();
            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onError(Exception exception) {

            }


        });
    }

    FirebaseAuth.AuthStateListener mAuthListener = firebaseAuth -> authLogin();




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



    // ACTIVITY RESULT
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // FACEBOOK
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_FACEBOOK)
            facebookCallbackManager.onActivityResult(requestCode, resultCode, data);


    }


    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    // FACEBOOK AUTH
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
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(SignInActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        authLogin();
                    }
                });
    }


    private void authLogin() {
        if (isCurrentUserLogged()) {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}