package com.oc.go4lunch;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SplashActivity extends Activity {

    private static final String TAG = "SPLASH";

    private FirebaseUser currentUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.AppTheme_Splash);
        super.onCreate(savedInstanceState);
        new BackgroundSplashTask().execute();

    }

    /**
     * Async Task
     */
    private class BackgroundSplashTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            // Check if user is already logged
            currentUser = mAuth.getCurrentUser();

            // Call Firestore Database
            loadRestaurant();

            return null;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object result) {
//            super.onPostExecute(result);
            Intent intent;
            // any info loaded can during splash_show
            // can be passed to main activity using
            // below

//            i.putExtra(TAG, " ");

            if (currentUser != null) {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, SignInActivity.class);
            }

            startActivity(intent);
            finish();
        }

    }

    private void loadRestaurant() {

        db.collection("restaurant")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {


                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        // Récupération des données

                        // adapter.setData
                        // notifydatasetchanged


                    }
                });
    }


}


