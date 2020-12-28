package com.edescamp.go4lunch.activity.fragment.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.activity.fragment.DetailsFragment;
import com.edescamp.go4lunch.model.User;
import com.edescamp.go4lunch.util.DetailsUtil;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesViewHolder> {

    private final List<DocumentSnapshot> documents;
    private final FragmentActivity activity;
    private User documentUser;
    private String tag;

    private Context context;

    public WorkmatesAdapter(List<DocumentSnapshot> documents, FragmentActivity activity, String tag) {
        this.documents = documents;
        this.activity = activity;
        this.tag = tag;
    }

//    public WorkmatesAdapter(List<DocumentSnapshot> documents) {
//        this.documents = documents;
//    }

    public WorkmatesAdapter(List<DocumentSnapshot> documents, FragmentActivity activity) {
        this.documents=documents;
        this.activity=activity;

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

        if (tag.equals(DetailsFragment.TAG)) {
            documentUser = documents.get(position).toObject(User.class);
            assert documentUser != null;
            holder.updateViewWithWorkmatesForDetailsFragment(documentUser);
        } else {
            documentUser = documents.get(position).toObject(User.class);
            assert documentUser != null;
            holder.updateViewWithWorkmates(documentUser);
        }


        holder.itemView.setOnClickListener(v -> {
            DetailsUtil.openDetailsFragmentOrCallApiThenOpenDetailsFragment(
                    activity,
                    documentUser.getChosenRestaurantId());
//                if (placeDetailsResultHashmap.containsKey(Objects.requireNonNull(results.get(position).getPlaceId()))) {
//                    DetailsUtil.openDetailsFragment(
//                            activity,
//                            placeDetailsResultHashmap.get(Objects.requireNonNull(results.get(position).getPlaceId())));
//
//                }
        });

    }


    @Override
    public int getItemCount() {
        return documents.size();
    }


}
