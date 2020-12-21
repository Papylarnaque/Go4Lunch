package com.edescamp.go4lunch.activity.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.activity.fragment.view.WorkmatesAdapter;
import com.edescamp.go4lunch.util.UserHelper;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.edescamp.go4lunch.activity.MainActivity.uid;

public class WorkmatesFragment extends BaseFragment {


    private static final String TAG = "WorkmatesFragment";
    private ProgressBar progressBar;
    private TextView noWorkmates;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_workmates, container, false);

        noWorkmates = view.findViewById(R.id.workmates_no_workmates);
        progressBar = view.findViewById(R.id.workmates_progress_bar);

        // Add the following lines to create RecyclerView
        recyclerView = view.findViewById(R.id.workmates_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        progressBar.setVisibility(View.VISIBLE);

        getWorkmatesExceptCurrentUser();

        return view;

    }


    private void getWorkmatesExceptCurrentUser() {
        UserHelper.getAllUsersOrderByRestaurant(uid).addOnSuccessListener(queryDocumentSnapshots -> {
            launchProgressBar();
            if (queryDocumentSnapshots == null) {
                noWorkmates.setText(R.string.workmates_list_no_workmates_to_show);
                noWorkmates.setVisibility(View.VISIBLE);
            } else {
                Log.d(TAG, "getWorkmatesExceptCurrentUser passed");

                List<DocumentSnapshot> documents = new ArrayList<>();
                // Handle removing current user from the list in the fragment
                // as firebase does not support double where queries
                for (DocumentSnapshot user : queryDocumentSnapshots.getDocuments()){
                    if(!Objects.equals(user.get("uid"), uid)){
                        documents.add(user);
                    }
                }
                sendResultsToAdapter(documents);
            }
        });
    }

    private void sendResultsToAdapter(List<DocumentSnapshot> documents) {
        recyclerView.setAdapter(new WorkmatesAdapter(documents));
    }

    private void launchProgressBar() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


}
