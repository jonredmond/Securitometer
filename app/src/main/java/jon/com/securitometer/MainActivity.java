package jon.com.securitometer;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    Vector<App> apps;

    PackageManager pm;

    String[] vulnerabilities = {"APK_duplicate_file", "exploid_udev", "Gingerbreak",
            "KillingInTheNameOf_psneuter_ashmem", "ObjectInputStream_deserializable",
            "RageAgainstTheCage_zygote", "Stagefright", "TowelRoot", "zergRush"};

    public static String AssetJSONFile(String filename, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pm = getPackageManager();
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        listDataHeader.add("Applications");
        listDataHeader.add("Operating System");
        List<PackageInfo> installedApplications = pm.getInstalledPackages(PackageManager.GET_META_DATA);

        scanApps(installedApplications);

        Log.d("OS", scanOS(android.os.Build.VERSION.RELEASE)+"");

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        expListView.setAdapter(listAdapter);
    }

    public void scanApps(List<PackageInfo> apps) {
        ArrayList<String> listApplicationNames = new ArrayList<String>();
        for (PackageInfo app : apps) {
            listApplicationNames.add((String) pm.getApplicationLabel(app.applicationInfo));
            PermissionInfo[] permissions = app.permissions;
            if(permissions != null) this.apps.add(new App((String) pm.getApplicationLabel(app.applicationInfo), permissions));
        }
        listDataChild.put("Applications", listApplicationNames);
    }

    public float scanOS(String version) {
        ArrayList<String> OSvulnerabilities = new ArrayList<String>();
        float totalScore = 0, deviceScore = 0;
        for (String vulnerability : vulnerabilities) {
            try {
                String json = AssetJSONFile(vulnerability + ".json", this.getApplicationContext());
                //Log.d("vulnerability", json);
                JSONObject obj = new JSONObject(json);
                if (obj.getJSONArray("Affected_versions_regexp").length() > 0) {
                    String versions_regexp = obj.getJSONArray("Affected_versions_regexp").getString(0);
                    int score = obj.getInt("Score");
                    //Log.d(vulnerability, versions_regexp);
                    if (version.matches(versions_regexp)) {
                        OSvulnerabilities.add(vulnerability);
                        deviceScore += score;
                    }
                    totalScore += score;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listDataChild.put("Operating System", OSvulnerabilities);
        }
        return (deviceScore*10.0f)/totalScore;
    }
}
