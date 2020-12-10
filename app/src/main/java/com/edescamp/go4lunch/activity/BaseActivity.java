package com.edescamp.go4lunch.activity;

import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.edescamp.go4lunch.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity {

    /**
     * This method returns the current logged user
     * @return a FirebaseUser object representing the logged user
     */
    @Nullable
    protected FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    /**
     * This method allows to know if a user is logged
     * @return true if there is a logged user
     */
    protected Boolean isCurrentUserLogged(){
        return (this.getCurrentUser() != null); }


    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().signOut();
    }


    // Create http requests (SignOut & Delete)
    public void signOutUserFromFirebase(){
        AuthUI.getInstance()
                .signOut(this);
//                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK));
    }

    private void deleteUserFromFirebase(){
        if (this.getCurrentUser() != null) {
            AuthUI.getInstance()
                    .delete(this);
//                    .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(DELETE_USER_TASK));
        }
    }



    // --------------------
    // ERROR HANDLER
    // --------------------

    protected OnFailureListener onFailureListener(){
        return e -> Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
    }

}

