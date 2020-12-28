package com.edescamp.go4lunch;

import com.edescamp.go4lunch.activity.MainActivity;
import com.edescamp.go4lunch.service.LocationService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class LocationServiceTest extends LocationServiceUnitTest {

    @Mock private MainActivity activity;
//    @Mock(name = "database") private ArticleDatabase dbMock;
//    @Mock(answer = RETURNS_MOCKS) private UserProvider userProvider;
//    @Mock(extraInterfaces = {Queue.class, Observer.class}) private  articleMonitor;
//    @Mock(stubOnly = true) private Logger logger;

//    private ArticleManager manager;

    @Before public void setup() {

//        manager = new ArticleManager(userProvider, database, calculator, articleMonitor, logger);
    }
}

public class LocationServiceUnitTest {

    LocationService locationService;
    MainActivity activity;

    @Before public void initMocks() {
        MockitoAnnotations.initMocks(this);
    locationService = new LocationService(activity);
    }

    @Test
    public void dummy() {
        locationService.getLocationPermission();
    }
}