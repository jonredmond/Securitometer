package jon.com.securitometer;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by jon on 29/02/16.
 */
public class OS {
    private ArrayList<String> _vulns;
    private double _score;
    private String _version;

    public OS(String version){
        this._vulns = new ArrayList<String>();
        this._score = 0.0;
        this._version = version;
    }

    public String getVersion() {
        return this._version;
    }

    public void addVuln(String vuln) {
        this._vulns.add(vuln);
    }

    public void setScore(double score) {
        this._score = score;
    }

    public ArrayList<String> getVulns() {
        return _vulns;
    }

    public static OS scanOS(String version, Context context) {
        String[] vulnerabilities = {"APK_duplicate_file", "exploid_udev", "Gingerbreak",
                "KillingInTheNameOf_psneuter_ashmem", "ObjectInputStream_deserializable",
                "Stagefright", "TowelRoot", "zergRush"};

        double totalScore = 0, deviceScore = 0;
        OS result = new OS(version);

        for (String vulnerability : vulnerabilities) {
            try {
                String json = Utils.AssetJSONFile(vulnerability + ".json", context);
                JSONObject obj = new JSONObject(json);
                if (obj.getJSONArray("Affected_versions_regexp").length() > 0) {
                    String versions_regexp = obj.getJSONArray("Affected_versions_regexp").getString(0);
                    double score = obj.getDouble("Score");
                    if (version.matches(versions_regexp)) {
                        result.addVuln(vulnerability);
                        deviceScore += score;
                    }
                    totalScore += score;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("total", new DecimalFormat("0.00").format(totalScore));
        Log.d("device", new DecimalFormat("0.00").format(deviceScore));
        result.setScore(10.0 - (deviceScore*10.0)/totalScore);
        return result;
    }

    public double getScore() {
        return this._score;
    }
}
