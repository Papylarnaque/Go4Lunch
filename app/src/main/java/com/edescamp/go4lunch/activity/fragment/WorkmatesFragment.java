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
import com.edescamp.go4lunch.activity.MainActivity;
import com.edescamp.go4lunch.adapter.WorkmatesAdapter;
import com.edescamp.go4lunch.util.UserHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorkmatesFragment extends BaseFragment {


    private static final String TAG = "WorkmatesFragment";
    private ProgressBar progressBar;
    private TextView noWorkmates;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_workmates, container, false);

        noWorkmates = view.findViewById(R.id.workmates_no_workmates);
        progressBar = view.findViewById(R.id.workmates_progress_bar);

        // Add the following lines to create RecyclerView
        recyclerView = view.findViewById(R.id.workmates_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        progressBar.setVisibility(View.VISIBLE);
        getWorkmates();

        return view;

    }

    private void getWorkmates() {
        UserHelper.getAllUsers().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        if (queryDocumentSnapshots.getDocuments() == null) {
                            noWorkmates.setText(R.string.workmates_list_no_workmates_to_show);
                            noWorkmates.setVisibility(View.VISIBLE);
                        } else {
                            sendResultsToAdapter(queryDocumentSnapshots.getDocuments(), MainActivity.uid);
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        }
                    }
        });
    }


    private void sendResultsToAdapter(List<DocumentSnapshot> documents, String uid) {
        recyclerView.setAdapter(new WorkmatesAdapter(documents, uid));
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


}