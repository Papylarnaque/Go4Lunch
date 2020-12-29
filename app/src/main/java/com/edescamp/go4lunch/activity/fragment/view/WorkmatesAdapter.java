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
    private final String tag;

    public WorkmatesAdapter(List<DocumentSnapshot> documents, String tag) {
        this.documents = documents;
        this.tag = tag;
    }

    public WorkmatesAdapter(List<DocumentSnapshot> documents) {
        this.documents = documents;
        this.tag = null;
    }


    @Override
    public int getItemViewType(final int position) {
        return R.layout.item_workmate;
    }

    @NonNull
    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        Context context = view.getContext();
        return new WorkmatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesViewHolder holder, int position) {

        User documentUser;
        if (tag==null) {
            documentUser = documents.get(position).toObject(User.class);
            assert documentUser != null;
            holder.updateViewWithWorkmates(documentUser);
        } else if ( tag.equals(DetailsFragment.TAG)){
            documentUser = documents.get(position).toObject(User.class);
            assert documentUser != null;
            holder.updateViewWithWorkmatesForDetailsFragment(documentUser);
        }

    }


    @Override
    public int getItemCount() {
        return documents.size();
    }


}
