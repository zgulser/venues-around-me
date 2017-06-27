package assignment.adyen.com.venuesaroundme.permission;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import assignment.adyen.com.venuesaroundme.R;

/**
 * Created by Zeki
 */

public class Grant {

    private String permissionName;
    private String permissionExp;

    public Grant(String permissionName, String permissionExp){
        this.permissionName = permissionName;
        this.permissionExp = permissionExp;
    }

    public void getLocationPermission(Activity hostActivity){
        handlePermissionReq(hostActivity, permissionName, PermissionUtils.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    public void getInternetPermission(Activity hostActivity){
        handlePermissionReq(hostActivity, permissionName, PermissionUtils.MY_PERMISSIONS_REQUEST_INTERNET_ACCESS);
    }

    private void handlePermissionReq(Activity hostActivity, String perm, int requestType){
        if (ActivityCompat.shouldShowRequestPermissionRationale(hostActivity, perm)) {
            showPermissionReaskDialog(hostActivity, requestType);
        } else {
            ActivityCompat.requestPermissions(hostActivity, new String[]{perm}, requestType);
        }
    }

    private void showPermissionReaskDialog(Activity hostActivity, int requestType){
        new AlertDialog.Builder(hostActivity)
                .setMessage(permissionExp)
                .setPositiveButton(hostActivity.getResources().getString(R.string.permission_dialog_ok_button), new OKButtonListener(hostActivity, requestType))
                .setNegativeButton(hostActivity.getResources().getString(R.string.permission_dialog_cancel_button), null)
                .create()
                .show();
    }

    class OKButtonListener implements DialogInterface.OnClickListener {

        Activity hostActivity;
        int requestType;

        OKButtonListener(Activity hostActivity, int requestType){
            this.requestType = requestType;
            this.hostActivity = hostActivity;
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            ActivityCompat.requestPermissions(hostActivity, new String[]{permissionName}, requestType);
        }
    }
}
