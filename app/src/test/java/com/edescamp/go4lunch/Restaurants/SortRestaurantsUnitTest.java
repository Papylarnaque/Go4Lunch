package com.edescamp.go4lunch.Restaurants;

import com.edescamp.go4lunch.activity.fragment.RestaurantsFragment;
import com.edescamp.go4lunch.model.map.ResultAPIMap;
import com.edescamp.go4lunch.util.SortRestaurantsUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SortRestaurantsUnitTest {

    ResultAPIMap resultAPIMap1 = new ResultAPIMap();
    ResultAPIMap resultAPIMap2 = new ResultAPIMap();
    ResultAPIMap resultAPIMap3 = new ResultAPIMap();

    List<ResultAPIMap> listToSort = new ArrayList<>();

    List<ResultAPIMap> azSortedList = new ArrayList<>();
    List<ResultAPIMap> zaSortedList = new ArrayList<>();
    List<ResultAPIMap> ratingSortedList = new ArrayList<>();
    List<ResultAPIMap> workmatesSortedList = new ArrayList<>();
    List<ResultAPIMap> distanceSortedList = new ArrayList<>();

    @Before
    public void setUp() {

        resultAPIMap1.setName("resultAPIMap1");
        resultAPIMap1.setPlaceId("resultAPIMap1Id");
        resultAPIMap1.setVicinity("resultAPIMap1 Adresse");
        resultAPIMap1.setRating(1f);

        resultAPIMap2.setName("resultAPIMap2");
        resultAPIMap2.setPlaceId("resultAPIMap2Id");
        resultAPIMap2.setVicinity("resultAPIMap2 Adresse");
        resultAPIMap2.setRating(5f);

        resultAPIMap3.setName("resultAPIMap3");
        resultAPIMap3.setPlaceId("resultAPIMap3Id");
        resultAPIMap3.setVicinity("resultAPIMap3 Adresse");
        resultAPIMap3.setRating(3f);

        initWorkmatesHashMap();
        initDistanceHashMap();

        setUpLists();
    }

    private void setUpLists() {

        listToSort.add(resultAPIMap3);
        listToSort.add(resultAPIMap1);
        listToSort.add(resultAPIMap2);

        azSortedList.add(resultAPIMap1);
        azSortedList.add(resultAPIMap2);
        azSortedList.add(resultAPIMap3);

        zaSortedList.add(resultAPIMap3);
        zaSortedList.add(resultAPIMap2);
        zaSortedList.add(resultAPIMap1);

        ratingSortedList.add(resultAPIMap2);
        ratingSortedList.add(resultAPIMap3);
        ratingSortedList.add(resultAPIMap1);

        workmatesSortedList.add(resultAPIMap2);
        workmatesSortedList.add(resultAPIMap3);
        workmatesSortedList.add(resultAPIMap1);

        distanceSortedList.add(resultAPIMap2);
        distanceSortedList.add(resultAPIMap3);
        distanceSortedList.add(resultAPIMap1);
    }

    private void initWorkmatesHashMap() {
        RestaurantsFragment.workmatesCountHashMap.put(resultAPIMap1.getPlaceId(), 1);
        RestaurantsFragment.workmatesCountHashMap.put(resultAPIMap2.getPlaceId(), 5);
        RestaurantsFragment.workmatesCountHashMap.put(resultAPIMap3.getPlaceId(), 4);
    }

    private void initDistanceHashMap() {
        RestaurantsFragment.distanceHashMap.put(resultAPIMap1.getPlaceId(), 500);
        RestaurantsFragment.distanceHashMap.put(resultAPIMap2.getPlaceId(), 1300);
        RestaurantsFragment.distanceHashMap.put(resultAPIMap3.getPlaceId(), 40);
    }


    @Test
    public void testSortZA() {
        SortRestaurantsUtil.sortZA(listToSort);
        Assert.assertEquals(listToSort, zaSortedList);
    }

    @Test
    public void testSortAZ() {
        SortRestaurantsUtil.sortAZ(listToSort);
        Assert.assertEquals(listToSort, azSortedList);
    }

    @Test
    public void testSortRating() {
        SortRestaurantsUtil.sortRatingDesc(listToSort);
        Assert.assertEquals(listToSort, ratingSortedList);
    }

    @Test
    public void testSortWorkmates() {
        SortRestaurantsUtil.sortRatingDesc(listToSort);
        Assert.assertEquals(listToSort, workmatesSortedList);
    }

    @Test
    public void testSortDistance() {
        SortRestaurantsUtil.sortRatingDesc(listToSort);
        Assert.assertEquals(listToSort, distanceSortedList);
    }
}
