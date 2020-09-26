package com.oc.go4lunch.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.oc.go4lunch.R;

public class RestaurantFragment extends BaseFragment {


    private static final String TAG = "TAG";
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView mRecyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
//        View v = inflater.inflate(R.layout.fragment_restaurant_list, container, false);
//        return v;

//        getRestaurantData();


        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);
//        Context context = view.getContext();
//        mRecyclerView = (RecyclerView) view;
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        return view;

    }

//
//    /**
//     * Get the restaurant data from Firestore
//     */
//    private void getRestaurantData() {
//        db.collection("restaurant")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//                    }
//                });
//    }
}
