package com.edescamp.go4lunch.restaurants;

import com.edescamp.go4lunch.model.map.ResultAPIMap;
import com.edescamp.go4lunch.util.SortRestaurantsUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SortRestaurantsUnitTest {

    final ResultAPIMap resultAPIMap1 = new ResultAPIMap();
    final ResultAPIMap resultAPIMap2 = new ResultAPIMap();
    final ResultAPIMap resultAPIMap3 = new ResultAPIMap();

    final List<ResultAPIMap> listToSort = new ArrayList<>();

    final List<ResultAPIMap> azSortedList = new ArrayList<>();
    final List<ResultAPIMap> zaSortedList = new ArrayList<>();
    final List<ResultAPIMap> ratingSortedList = new ArrayList<>();
    final List<ResultAPIMap> workmatesSortedList = new ArrayList<>();
    final List<ResultAPIMap> distanceSortedList = new ArrayList<>();

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

        distanceSortedList.add(resultAPIMap3);
        distanceSortedList.add(resultAPIMap1);
        distanceSortedList.add(resultAPIMap2);
    }

    private void initWorkmatesHashMap() {
        SortRestaurantsUtil.workmatesCountHashMap.put(resultAPIMap1.getPlaceId(), 1);
        SortRestaurantsUtil.workmatesCountHashMap.put(resultAPIMap2.getPlaceId(), 5);
        SortRestaurantsUtil.workmatesCountHashMap.put(resultAPIMap3.getPlaceId(), 4);
    }

    private void initDistanceHashMap() {
        SortRestaurantsUtil.distanceHashMap.put(resultAPIMap1.getPlaceId(), 500);
        SortRestaurantsUtil.distanceHashMap.put(resultAPIMap2.getPlaceId(), 1300);
        SortRestaurantsUtil.distanceHashMap.put(resultAPIMap3.getPlaceId(), 40);
    }


    @Test
    public void testSortZA() {
        List<ResultAPIMap> resultAPIMaps =SortRestaurantsUtil.sortZA(listToSort);
        Assert.assertEquals(resultAPIMaps, zaSortedList);
    }

    @Test
    public void testSortAZ() {
        List<ResultAPIMap> resultAPIMaps = SortRestaurantsUtil.sortAZ(listToSort);
        Assert.assertEquals(resultAPIMaps, azSortedList);
    }

    @Test
    public void testSortRating() {
        List<ResultAPIMap> resultAPIMaps = SortRestaurantsUtil.sortRatingDesc(listToSort);
        Assert.assertEquals(resultAPIMaps, ratingSortedList);
    }

    @Test
    public void testSortWorkmates() {
        List<ResultAPIMap> resultAPIMaps = SortRestaurantsUtil.sortWorkmatesDesc(listToSort);
        Assert.assertEquals(resultAPIMaps, workmatesSortedList);
    }

    @Test
    public void testSortDistance() {
        List<ResultAPIMap> resultAPIMaps = SortRestaurantsUtil.sortDistanceAsc(listToSort);
        Assert.assertEquals(resultAPIMaps, distanceSortedList);
    }
}
