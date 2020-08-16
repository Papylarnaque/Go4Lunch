package com.oc.go4lunch.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oc.go4lunch.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseFragment extends Fragment {

    abstract void displayPlacesIdList();
    abstract void getStandardDisplay();
//    abstract void displayPlace(Restaurant restaurant);

    private static final String TAG = "BaseFragment - ";

//    protected static RectangularBounds mLastKnownLocationBounds;
//    protected static PlacesClient mPlacesClient;
    protected static List<String> mPlacesIdList = new ArrayList<>();
    protected final LatLng mDefaultLocation = new LatLng(48.864716, 2.349014);
//    protected FusedLocationProviderClient mFusedLocationProviderClient;
    protected FirebaseFirestore mDb = FirebaseFirestore.getInstance();

    protected MainActivity mMainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Places.initialize(getContext(), getString(R.string.google_maps_key));
//        mPlacesClient = Places.createClient(getContext());
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mMainActivity = (mMainActivity) getActivity();
//        mMainActivity.mMySearch.addTextChangedListener(textWatcher);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

//    /**
//     * This section manages the Search input field
//     */
//    public TextWatcher textWatcher = new TextWatcher() {
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//            if (s.toString().length() > 1) {
//                mPlacesIdList.clear();
//                if (s.toString().equals("")) {
//                    getStandardDisplay();
//                } else {
//                    makeSearch(s.toString(), mLastKnownLocationBounds, mPlacesClient);
//                }
//            } else {
//                getStandardDisplay();
//            }
//        }
//    };

//    /**
//     * This method calculate and return the RectangularBounds needed to define the region where to search for the Autocomplete Google API
//     * @param coordinates indicates the original coordinates of the current user location
//     * @return a RectangularBounds
//     */
//    protected RectangularBounds getBound(LatLng coordinates) {
//        double latStart = coordinates.latitude - 0.005;
//        double latStop = coordinates.latitude + 0.005;
//        double lngStart = coordinates.longitude - 0.005;
//        double lngStop = coordinates.longitude + 0.005;
//
//        return RectangularBounds.newInstance(
//                new LatLng(latStart, lngStart),
//                new LatLng(latStop, lngStop));
//    }

//    /**
//     * This method searches, from the user query, for all the PlaceId which are restaurants and display them
//     * @param query is the string of characters entered by the user
//     * @param bounds is the RectangleBounds object - a rectangle on the map - which define where the API should search for restaurant
//     * @param placesClient is the object on which the Autocomplete API is going to work
//     */
//    protected void makeSearch(String query, RectangularBounds bounds, PlacesClient placesClient) {
//        if (bounds != null) {
//            AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
//
//            FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
//                    .setLocationRestriction(bounds)
//                    .setCountry("fr")
//                    .setTypeFilter(TypeFilter.ESTABLISHMENT)
//                    .setSessionToken(token)
//                    .setQuery(query.toString())
//                    .build();
//
//            placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
//                for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
//                    if (prediction.getPlaceTypes().contains(Place.Type.RESTAURANT)) {
//                        mPlacesIdList.add(prediction.getPlaceId());
//                    }
//                }
//                displayPlacesIdList();
//            }).addOnFailureListener((exception) -> {
//                if (exception instanceof ApiException) {
//                    ApiException apiException = (ApiException) exception;
//                    Log.e(TAG, "Place not found: " + apiException.getStatusCode());
//                }
//            });
//        }
//    }

//    /**
//     * This method create a Restaurant from a placeId thanks to Place Details Google API, ready to be displayed then in the specialized fragments
//     * @param placeId is the unique reference of the place
//     */
//    protected void makeRestaurantFromPlaceId(String placeId) {
//        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.PHONE_NUMBER, Place.Field.WEBSITE_URI, Place.Field.ADDRESS, Place.Field.RATING, Place.Field.PHOTO_METADATAS);
//        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);

//        mPlacesClient.fetchPlace(request).addOnSuccessListener((response) -> {
//            Place place = response.getPlace();
//            String name = place.getName();
//            String address = place.getAddress();
//            LatLng latLng = place.getLatLng();
//            OpeningHours openingHours = place.getOpeningHours();
//            PhotoMetadata photoMetadata = (place.getPhotoMetadatas() == null) ? null : place.getPhotoMetadatas().get(0);
//
//            Restaurant restaurant = new Restaurant(placeId, name, null, null, address, openingHours, latLng, null, null, photoMetadata);
//            createRestaurantInFirestore(restaurant);
//            displayPlace(restaurant);

//        }).addOnFailureListener((exception) -> {
//            if (exception instanceof ApiException) {
//                ApiException apiException = (ApiException) exception;
//                int statusCode = apiException.getStatusCode();
//                Log.e(TAG, "Restaurant not found: " + exception.getMessage());
//            }
//        });
//    }

//    protected void createRestaurantInFirestore(Restaurant restaurant) {
//        Map<String, Object> docData = new HashMap<>();
//        docData.put("name", restaurant.getName());
//        docData.put("id", restaurant.getId());
//        docData.put("address", restaurant.getAddress());
//        docData.put("openingHours", restaurant.getOpeningHours());
//        docData.put("photoMetadata", restaurant.getPhoto());
//
//        mDb.collection("restaurants").document(restaurant.getId()).set(docData, SetOptions.merge());
//    }

}

