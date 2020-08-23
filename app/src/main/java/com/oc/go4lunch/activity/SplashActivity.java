package com.oc.go4lunch.activity;

import android.content.Intent;
import android.os.Bundle;

import com.oc.go4lunch.R;
import com.oc.go4lunch.activity.auth.SignInActivity;

public class SplashActivity extends BaseActivity {

//    private static final String TAG = "SPLASH";
//    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Splash);
        super.onCreate(savedInstanceState);

        getCurrentUser();

        Intent intent;
        if (isCurrentUserLogged()) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        } else {
            signOutUserFromFirebase(); // TODO = line put temporarily to fix the Facebook auth issue - TOFIX !
            intent = new Intent(SplashActivity.this, SignInActivity.class);
        }
        startActivity(intent);
        finish();


    }


//    private void loadRestaurant() {
//
//        db.collection("restaurant")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//
//
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//
//                        // Récupération des données
//
//                        // adapter.setData
//                        // notifydatasetchanged
//
//
//                    }
//                });
//    }


}


