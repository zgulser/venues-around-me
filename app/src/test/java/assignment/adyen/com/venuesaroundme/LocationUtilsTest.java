package assignment.adyen.com.venuesaroundme;

import android.test.suitebuilder.annotation.MediumTest;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import assignment.adyen.com.venuesaroundme.location.LocationUtils;
import assignment.adyen.com.venuesaroundme.location.TestLocationProvider;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@MediumTest
public class LocationUtilsTest {

    @Test
    public void testGetFormattedDistance() throws Exception {
        String returnedString = LocationUtils.getFormattedDistance(4000);
        assertTrue(returnedString.contains(LocationUtils.KM_SUFFIX));

        returnedString = LocationUtils.getFormattedDistance(800);
        assertTrue(returnedString.contains(LocationUtils.METER_SUFFIX));
    }

    @Test
    public void testGetLocationRequest() throws Exception {
        assertNotNull(LocationUtils.getLocationRequest());
    }

    @Test
    public void testGetFormattedLatitudeAndLongitude() throws Exception {
        String returnedString = LocationUtils.getFormattedLatitudeAndLongitude(
                TestLocationProvider.testLatitude,
                TestLocationProvider.testLongitude);
        assertTrue(returnedString.contains(LocationUtils.COMMA));
    }

    @Test
    public void testGoogleApiClientConnec() throws Exception {
        String returnedString = LocationUtils.getFormattedLatitudeAndLongitude(
                TestLocationProvider.testLatitude,
                TestLocationProvider.testLongitude);
        assertTrue(returnedString.contains(LocationUtils.COMMA));
    }
}