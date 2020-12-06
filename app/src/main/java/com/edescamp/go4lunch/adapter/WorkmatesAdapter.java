package com.edescamp.go4lunch.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edescamp.go4lunch.R;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesViewHolder> {

    private static final String TAG = "WorkmatesAdapter";
    private final List<DocumentSnapshot> documents;
    private String userid;

    private Context context;


    public WorkmatesAdapter(List<DocumentSnapshot> documents, String userid) {
        this.documents = documents;
        this.userid = userid;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.item_workmate;
    }

    @NonNull
    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        context = view.getContext();
        return new WorkmatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesViewHolder holder, int position) {

        if (documents.get(position).get("uid").toString().equals(userid)) {
            Log.i(TAG, "userid: "+userid+" skipped from the recyclerview");
        }else{

            holder.updateViewWithWorkmates(documents.get(position));

//        holder.itemView.setOnClickListener(v -> getPlaceDetails(results.get(position).getPlaceId()));
        }

    }


    @Override
    public int getItemCount() {
        // -1 because we do not want to show current user in the workmates recyclerview
        return documents.size()-1;
    }


}
