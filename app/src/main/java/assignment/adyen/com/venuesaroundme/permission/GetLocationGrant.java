package assignment.adyen.com.venuesaroundme.permission;

import android.app.Activity;

/**
 * Created by Zeki
 */

public class GetLocationGrant implements Permission {
    Grant grant;
    Activity context;

    public GetLocationGrant(Grant grant, Activity context){
        this.grant = grant;
        this.context = context;
    }

    @Override
    public void request() {
        grant.getLocationPermission(context);
    }
}
