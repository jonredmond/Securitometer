package jon.com.securitometer;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Jon on 09/02/2016.
 */
public class App {
    private String _name;
    private double _score;
    private ArrayList<String> _permissions;

    public App(){
    }

    public App(String name, double score, ArrayList<String> permissions) {
        this._name = name;
        this._score = score;
        this._permissions = new ArrayList<String>();
        for(String permission:permissions) {
            this._permissions.add(permission);
        }
    }

    @Override
    public String toString() {
        return _name;
    }

    public String getName() {
        return this._name;
    }

    public double getScore() {
        return this._score;
    }

    public ArrayList<String> getPermissions() {
        return this._permissions;
    }

    public static ArrayList<App> scanApps(List<PackageInfo> packages, Context context) {
        PackageManager pm = context.getPackageManager();
        ArrayList<App> apps = new ArrayList<App>();
        try {
            String json = Utils.AssetJSONFile("permissions.json", context);
            JSONObject obj = new JSONObject(json);
            for (PackageInfo p : packages) {
                if((pm.getApplicationInfo(p.packageName, 0).flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    continue;
                }
                double maliciousScore = 1.0;
                double benignScore = 1.0;
                ArrayList<String> permissions = new ArrayList<String>();
                if (p.requestedPermissions != null) {
                    Iterator<String> iter = obj.keys();
                    while (iter.hasNext()){
                        String key = iter.next();
                        JSONArray values = obj.getJSONArray(key);
                        if(Arrays.asList(p.requestedPermissions).contains(key)){
                            permissions.add(key);
                            benignScore *= values.getDouble(0);
                            maliciousScore *= values.getDouble(1);
                        } else {
                            benignScore *= (1.0-values.getDouble(0));
                            maliciousScore *= (1.0-values.getDouble(1));
                        }
                    }
                    Collections.reverse(permissions);
                    App app = new App((String)pm.getApplicationLabel(p.applicationInfo), (benignScore*10)/(maliciousScore+benignScore), permissions);
                    apps.add(app);
                } else {
                    App app = new App((String)pm.getApplicationLabel(p.applicationInfo), 10.0, permissions);
                    apps.add(app);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Collections.sort(apps, new AppComparator());
        return apps;
    }
}
