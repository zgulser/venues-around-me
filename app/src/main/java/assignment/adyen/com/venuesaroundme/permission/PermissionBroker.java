package assignment.adyen.com.venuesaroundme.permission;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Backbase R&D B.V on 16/01/2017.
 */

public class PermissionBroker {
    private List<Permission> permissionList = new ArrayList<Permission>();

    public void takePermission(Permission permission){
        permissionList.add(permission);
    }

    public void executePermissions(){
        for (Permission permission : permissionList) {
            permission.request();
        }
        permissionList.clear();
    }
}
