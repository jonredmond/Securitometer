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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    HashMap<String, String> listScores, listChildScores;
    Device device;

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
        listScores = new HashMap<String, String>();
        listChildScores = new HashMap<String, String>();
        listDataHeader.add("Applications");
        listDataHeader.add("Operating System");
        List<PackageInfo> installedApplications = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS);

        device = new Device();

        listScores.put("Applications", new DecimalFormat("0.00").format(scanApps(installedApplications)));
        listScores.put("Operating System", new DecimalFormat("0.00").format(scanOS(android.os.Build.VERSION.RELEASE)));

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listScores, listDataChild, listChildScores);

        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        expListView.setAdapter(listAdapter);
    }

    public double scanApps(List<PackageInfo> apps) {
        ArrayList<String> listApplicationNames = new ArrayList<String>();
        List<Double> scores = new ArrayList<Double>();
        double score = 0.0;
        try {
            String json = AssetJSONFile("permissions.json", this.getApplicationContext());
            JSONObject obj = new JSONObject(json);
            for (PackageInfo app : apps) {
                double maliciousScore = 1.0;
                double benignScore = 1.0;
                if (app.requestedPermissions != null) {
                    Iterator<String> iter = obj.keys();
                    while (iter.hasNext()){
                        String key = iter.next();
                        JSONArray values = obj.getJSONArray(key);
                        if(Arrays.asList(app.requestedPermissions).contains("android.permission."+key)){
                            benignScore *= values.getDouble(0);
                            maliciousScore *= values.getDouble(1);
                        } else {
                            benignScore *= (1.0-values.getDouble(0));
                            maliciousScore *= (1.0-values.getDouble(1));
                        }
                    }
                }
                listApplicationNames.add((String) pm.getApplicationLabel(app.applicationInfo));
                Log.d("mscore", maliciousScore + "");
                Log.d("bscore", benignScore+"");
                double finalScore = maliciousScore/(maliciousScore+benignScore);
                scores.add(finalScore);
                listChildScores.put((String) pm.getApplicationLabel(app.applicationInfo), new DecimalFormat("0.00").format(finalScore*10));
                this.device.apps.add(new App((String) pm.getApplicationLabel(app.applicationInfo), app, finalScore*10));
            }
            for (double d : scores) {
                score += d;
            }
            score = score/scores.size();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listDataChild.put("Applications", listApplicationNames);
        return score*10;
    }

    public double scanOS(String version) {
        double totalScore = 0, deviceScore = 0;
        for (String vulnerability : vulnerabilities) {
            try {
                String json = AssetJSONFile(vulnerability + ".json", this.getApplicationContext());
                //Log.d("vulnerability", json);
                JSONObject obj = new JSONObject(json);
                if (obj.getJSONArray("Affected_versions_regexp").length() > 0) {
                    String versions_regexp = obj.getJSONArray("Affected_versions_regexp").getString(0);
                    double score = obj.getDouble("Score");
                    //Log.d(vulnerability, versions_regexp);
                    if (version.matches(versions_regexp)) {
                        this.device.osVulnerabilities.add(vulnerability);
                        deviceScore += score;
                    }
                    totalScore += score;
                    listChildScores.put(vulnerability, new DecimalFormat("0.00").format(score));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listDataChild.put("Operating System", this.device.osVulnerabilities);
        }
        return (deviceScore*10.0)/totalScore;
    }
}
