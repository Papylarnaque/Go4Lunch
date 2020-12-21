package com.edescamp.go4lunch.activity.fragment.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.activity.fragment.DetailsFragment;
import com.edescamp.go4lunch.model.User;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesViewHolder> {

    private final List<DocumentSnapshot> documents;
    private User documentUser;
    private String tag;

    private Context context;

    public WorkmatesAdapter(List<DocumentSnapshot> documents, String tag) {
        this.documents = documents;
        this.tag = tag;
    }

    public WorkmatesAdapter(List<DocumentSnapshot> documents) {
        this.documents = documents;
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

        if (tag == DetailsFragment.TAG) {
            documentUser = documents.get(position).toObject(User.class);
            holder.updateViewWithWorkmatesForDetailsFragment(documentUser);
        } else {
            documentUser = documents.get(position).toObject(User.class);
            holder.updateViewWithWorkmates(documentUser);
        }
    }


    @Override
    public int getItemCount() {
        return documents.size();
    }


}
