package jon.com.securitometer;

import android.content.pm.PermissionInfo;

import java.util.Vector;

/**
 * Created by jon on 28/01/16.
 */
public class App {
    private String name;
    private Vector<PermissionInfo> permissionInfos;

    public App(String name, PermissionInfo[] permissionInfos) {
        this.name = name;
        for (PermissionInfo pi : permissionInfos) {
            this.permissionInfos.add(pi);
        }
    }
}
