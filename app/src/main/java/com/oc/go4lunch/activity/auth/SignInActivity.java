package com.oc.go4lunch.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.LoginStatusCallback;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.oc.go4lunch.R;
import com.oc.go4lunch.activity.BaseActivity;
import com.oc.go4lunch.activity.MainActivity;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.Objects;

public class SignInActivity extends BaseActivity {

    // TODO Override up action to avoid going back to main from up action

    // --------------------
    // FOR DATA
    // --------------------

    private String TAG = "SIGN";

    // LOGIN REQUEST CODE
    public static final int LOGIN_FACEBOOK = 111;
    private static final int RC_SIGN_IN = 123;
    
    // MODEL
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    public GoogleSignInClient mGoogleSignInClient;

    // UI
    private ImageView go4LunchImageView;
    private TextView go4LunchTextView;
    private TextView descriptionTextView;
    private LoginButton facebookSignInButton;
    private SignInButton googleSignInButton;

    OAuthProvider.Builder provider = OAuthProvider.newBuilder(String.valueOf(R.string.twitter));

    // TODO : Fix Token issues for login - Facebook keeps the auth when Firebase is logged out ?
    // TODO : Link account if trying to log with other provider

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrieveLoginStatus();

//        twitterConfig();

        // INIT LAYOUT
        setContentView(R.layout.activity_signin);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


//        twitterPendingResult();
//        getTwitterPendingAuthResult();


        // FACEBOOK INIT CALLBACK MANAGER
        mCallbackManager = CallbackManager.Factory.create();

        // FACEBOOK LOGIN BUTTON
        facebookSignInButton = findViewById(R.id.login_button_facebook);
        //loginButton.setPermissions("email", "public_profile", "user_photos");

        // FACEBOOK LOGIN BUTTON REGISTER CALLBACK
        facebookSignInButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
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


//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            // Name, email address, and profile photo Url
//            String name = user.getDisplayName();
//            String email = user.getEmail();
//            Uri photoUrl = user.getPhotoUrl();
//
//            // Check if user's email is verified
//            boolean emailVerified = user.isEmailVerified();
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getIdToken() instead.
//            String uid = user.getUid();
//        }


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

    FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            authLogin();
        }
    };


    private void firebaseAuthWithTwitter(TwitterAuthConfig authConfig) {
        Log.d(TAG, "handleTwitterToken:" + authConfig);
        AuthCredential credential = TwitterAuthProvider.getCredential(authConfig.getConsumerKey(), authConfig.getConsumerSecret());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = getCurrentUser();
//                            updateUI(user);
//                            authLogin();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                            authLogin();
                        }

                        // ...
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    // --------------------
    // ACTIVITY RESULT
    // --------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
//        authLogin();

        // FACEBOOK
        if (requestCode == LOGIN_FACEBOOK)
            mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // GOOGLE
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

//
//    //region GOOGLE authentication
//
//    // Configure Google Sign In
//    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build();

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }
//
//    // --------------------
//    // GOOGLE AUTH
//    // --------------------
//    private void firebaseAuthWithGoogle(String idToken) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
////                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Toast.makeText(SignInActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
////                            updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });
//    }
//
//    //endregion


    //region FACEBOOK authentication

    // --------------------
    // FACEBOOK AUTH
    // --------------------
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
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

                        // ...
                    }
                });
    }

    //endregion


    //region TWITTER authentication

    private void twitterPendingResult() {
        Task<AuthResult> pendingResultTask = mAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    // User is signed in.
                                    // IdP data available in
                                    // authResult.getAdditionalUserInfo().getProfile().
                                    mAuth
                                            .startActivityForSignInWithProvider(/* activity= */ SignInActivity.this, provider.build())
                                            .addOnSuccessListener(
                                                    new OnSuccessListener<AuthResult>() {
                                                        @Override
                                                        public void onSuccess(AuthResult authResult) {
                                                            authLogin();
                                                            // User is signed in.
                                                            // IdP data available in
                                                            // authResult.getAdditionalUserInfo().getProfile().
                                                            // The OAuth access token can also be retrieved:
                                                            // authResult.getCredential().getAccessToken().
                                                            // The OAuth secret can be retrieved by calling:
                                                            // authResult.getCredential().getSecret().
                                                        }
                                                    })
                                            .addOnFailureListener(
                                                    new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            // Handle failure.
                                                        }
                                                    });
                                    // The OAuth access token can also be retrieved:
                                    // authResult.getCredential().getAccessToken().
                                    // The OAuth secret can be retrieved by calling:
                                    // authResult.getCredential().getSecret().
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure.
                                }
                            });
        }
//        else {
//            // There's no pending result so you need to start the sign-in flow.
//            // See below.
//        }
    }

    private void twitterConfig() {
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret)))
                .debug(true)
                .build();
        Twitter.initialize(config);

        TwitterLoginButton mLoginButton = findViewById(R.id.login_button_twitter);
        mLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                twitterSignIn();
//                AuthCredential credential = TwitterAuthProvider.getCredential(Twitter.getInstance().getTwitterAuthConfig().getConsumerKey(), Twitter.getInstance().getTwitterAuthConfig().getConsumerSecret())
//                handleTwitterAccessToken(result.data.getAuthToken().token);

//                getTwitterPendingAuthResult();
//                Objects.requireNonNull(result.data.getAuthToken());
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });
    }

    // --------------------
    // TWITTER SIGN IN
    // --------------------
    private void twitterSignIn() {
        mAuth
                .startActivityForSignInWithProvider(SignInActivity.this, provider.build())
                .addOnSuccessListener(
                        new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                // User is signed in.
                                // IdP data available in
                                Objects.requireNonNull(authResult.getAdditionalUserInfo()).getProfile();
                                firebaseAuthWithTwitter(Twitter.getInstance().getTwitterAuthConfig());
                                // The OAuth access token can also be retrieved:
                                // authResult.getCredential().getAccessToken();
                                // The OAuth secret can be retrieved by calling:
                                // authResult.getCredential().getSecret().
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure.
                            }
                        });
    }/**/


    //endregion



//    private void updateUI(FirebaseUser user) {
//        if (user != null) {
//            Toast.makeText(SignInActivity.this, "User ID: " + user.getUid(),
//                    Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(SignInActivity.this, "Error: sign in failed.",
//                    Toast.LENGTH_SHORT).show();
//        }
//    }


    private void authLogin() {
        if (isCurrentUserLogged()) {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        }
        // TEST
//        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
//        startActivity(intent);
    }


}
