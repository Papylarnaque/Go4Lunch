package com.oc.go4lunch.activity.auth;

import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.oc.go4lunch.activity.BaseActivity;

public class UserManagementActivity extends BaseActivity {

    //FOR DATA - Identify each Http Request
//    private static final int SIGN_OUT_TASK = 10;
//    private static final int DELETE_USER_TASK = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // --------------------
    // REST REQUESTS
    // --------------------

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
    // UI
    // --------------------

    // Create OnCompleteListener called after tasks ended
//    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin){
//        return new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                switch (origin){
//                    case SIGN_OUT_TASK:
//                        finish();
//                        break;
//                    case DELETE_USER_TASK:
//                        finish();
//                        break;
//                    default:
//                        break;
//                }
//            }
//        };
//    }



}
