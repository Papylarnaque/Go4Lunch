package com.edescamp.go4lunch.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.model.User;
import com.edescamp.go4lunch.service.APIClient;
import com.edescamp.go4lunch.service.APIRequest;
import com.edescamp.go4lunch.service.entities.ResultAPIDetails;
import com.edescamp.go4lunch.service.entities.ResultsAPIDetails;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.edescamp.go4lunch.activity.MainActivity.FIELDS;

public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesViewHolder> {

    private static final String TAG = "WorkmatesAdapter";
    private final List<DocumentSnapshot> documents;
    private String userid;
    private User documentUser;

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
            Log.i(TAG, "userid: " + userid + " skipped from the recyclerview");
        } else {
            documentUser = documents.get(position).toObject(User.class);
            if (!documentUser.getHasChosenRestaurant().equals("")
                    && !documentUser.getHasChosenRestaurant().equals(null)) {
                getPlaceDetails(documentUser.getHasChosenRestaurant());
            }
            holder.updateViewWithWorkmates(documentUser);

//        holder.itemView.setOnClickListener(v -> getPlaceDetails(results.get(position).getPlaceId()));
        }

    }


    @Override
    public int getItemCount() {
        // -1 because we do not want to show current user in the workmates recyclerview
        return documents.size() - 1;
    }


    public void getPlaceDetails(String placeId) {
        APIRequest apiDetails = APIClient.getClient().create(APIRequest.class);
        Call<ResultsAPIDetails> placeDetails = apiDetails.getPlaceDetails(placeId, FIELDS, context.getResources().getString(R.string.google_maps_key));

        placeDetails.enqueue(new Callback<ResultsAPIDetails>() {
            @Override
            public void onResponse(@NotNull Call<ResultsAPIDetails> call, @NotNull Response<ResultsAPIDetails> response) {
                Log.d(TAG, "getPlaceDetails API ");
                if (response.isSuccessful()) {
                    ResultsAPIDetails body = response.body();
                    if (body != null) {
                        ResultAPIDetails result = body.getResult();
                        Log.d(TAG, "getPlaceDetails successful response " + result.getName() + " " + result.getPlaceId());

                        documentUser.setChosenRestaurantName(result.getName());

                    }
                    // TODO Handle failures, 404 error, etc
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResultsAPIDetails> call, @NotNull Throwable t) {
                Log.d(TAG, "getPlaceDetails API failure" + t);
            }
        });
    }


}
