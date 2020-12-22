package com.edescamp.go4lunch;


import com.edescamp.go4lunch.model.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class UserUnitTest {
    private User user;
    private String username;
    private String userId;
    private String email;
    private String photoUrl;
    private String restaurantId;
    private String restaurantName;
    private String restaurantAddress;

    private String likedRestaurant1;
    private String likedRestaurant2;
    private String likedRestaurant3;

    private List<String> likedRestaurants;

    @Before
    public void setup(){
        username = "Etienne";
        userId = "69Ysdhei65";
        photoUrl = "http://photo";
        email = "name@email.com";
        restaurantId = "";
        restaurantName = "";
        user = new User(userId, username, email, photoUrl, restaurantId, restaurantName);
        restaurantId = "54321";
        restaurantName = "nameResto";
        restaurantAddress = "123 adresse Resto";
        likedRestaurant1 = "uid1";
        likedRestaurant2 = "uid2";
        likedRestaurant3 = "uid3";
        likedRestaurants = new ArrayList<>();
        likedRestaurants.add(likedRestaurant1);
        likedRestaurants.add(likedRestaurant2);
        likedRestaurants.add(likedRestaurant3);
    }

    @Test
    public void getInfoFromUser() {
        assertEquals(userId, user.getUid());
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getUserMail());
        assertEquals(photoUrl, user.getUrlPicture());
    }

    @Test
    public void updateInfoUser_getInfo() {
        String newName = "Etienne Test";
        String newUid = "4321";
        String newPhotoUrl = "http://newphoto";
        String newEmail = "new@email.com";
        user.setUsername(newName);
        user.setUid(newUid);
        user.setUrlPicture(newPhotoUrl);
        user.setUserMail(newEmail);
        user.setChosenRestaurantId(restaurantId);
        user.setChosenRestaurantName(restaurantName);
        user.setChosenRestaurantAddress(restaurantAddress);
        user.addLikedRestaurant(likedRestaurant1);
        user.addLikedRestaurant(likedRestaurant2);
        user.addLikedRestaurant(likedRestaurant3);


        assertEquals(newUid, user.getUid());
        assertEquals(newName, user.getUsername());
        assertEquals(newEmail, user.getUserMail());
        assertEquals(newPhotoUrl, user.getUrlPicture());
        assertEquals(restaurantId, user.getChosenRestaurantId());
        assertEquals(restaurantName, user.getChosenRestaurantName());
        assertEquals(restaurantAddress, user.getChosenRestaurantAddress());
        assertEquals(likedRestaurants, user.getLikedRestaurants());
    }

}

