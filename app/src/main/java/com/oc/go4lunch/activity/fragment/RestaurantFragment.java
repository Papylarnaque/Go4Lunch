package com.oc.go4lunch.activity.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.oc.go4lunch.R;

import java.util.Objects;

public class RestaurantFragment extends BaseFragment {


    private static final String TAG = "TAG";
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView mRecyclerView;
    private static final String COLLECTION_RESTAURANTS = "restaurant";
    private CollectionReference restaurantsCollection;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        getRestaurantData();
        restaurantsCollection = db.collection(COLLECTION_RESTAURANTS);

        return v;
//        simpleQueries();



//        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);
//        Context context = view.getContext();
//        mRecyclerView = (RecyclerView) view;
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
//        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        configureRestaurantList();

    }

//    private void configureRestaurantList() {
//        TextView displayTextView = (TextView) requireView().findViewById(R.id.item_restaurant_first_line);
//        simpleQueries();
//        displayTextView =
//
//
//    }

//    public void simpleQueries() {
//        // [START simple_queries]
//
//        // Create a query against the collection.
//        Query query = restaurantsCollection.whereEqualTo("rating", 0);
//        // [END simple_queries]
//
//        Query first = db.collection(COLLECTION_RESTAURANTS)
//                .orderBy("rating")
//                .limit(25);
//    }

    /**
     * Get the restaurant data from Firestore
     */
    private void getRestaurantData() {

        db.collection(COLLECTION_RESTAURANTS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
