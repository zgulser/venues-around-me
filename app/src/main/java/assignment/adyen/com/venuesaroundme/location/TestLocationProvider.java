package assignment.adyen.com.venuesaroundme.location;

/**
 * Created by Zeki on 25/06/2017.
 */

public class TestLocationProvider implements ILocationProvider {

    private static TestLocationProvider testLocationProvider;
    public static final double testLatitude = 52.353046;
    public static final double testLongitude = 4.863381;

    private TestLocationProvider(){}

    public static TestLocationProvider getInstance(){
        if(testLocationProvider == null){
            testLocationProvider = new TestLocationProvider();
        }

        return testLocationProvider;
    }

    @Override
    public double[] getMyLatitudeAndLongitude() {
        double[] testPosition = {testLatitude, testLongitude};
        return testPosition;
    }

    @Override
    public void onLocationSettingsRequestSuccess() {

    }

    @Override
    public void onLocationSettingsRequestFailed(int statusCode) {

    }
}
