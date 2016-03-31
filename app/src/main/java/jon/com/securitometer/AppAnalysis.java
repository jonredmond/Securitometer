package jon.com.securitometer;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class AppAnalysis extends AppCompatActivity {
    App app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_analysis);

        Securitometer securitometer = (Securitometer) getApplicationContext();
        Device device = securitometer.getDevice();

        Intent intent = getIntent();
        int appNo = intent.getIntExtra("appNo", 0);
        app = device.apps.get(appNo);

        TextView appName = (TextView)findViewById(R.id.appName);
        appName.setText(app.getName());

        TextView appScore = (TextView)findViewById(R.id.appAnalysisScore);
        appScore.setText(new DecimalFormat("0.00").format(app.getScore()));
        ((GradientDrawable)appScore.getBackground()).setColor(Utils.redGreenColourScale(app.getScore()));

        ListView lv = (ListView)findViewById(R.id.listView);
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> scores = new ArrayList<String>();
        for(String p : app.getPermissions()) {
            names.add(p.substring(p.lastIndexOf(".") + 1));
            try {
                String json = Utils.AssetJSONFile("permissions.json", getApplicationContext());
                JSONObject obj = new JSONObject(json);
                JSONArray values = obj.getJSONArray(p);
                double score = values.getDouble(0);
                scores.add(new DecimalFormat("0.00").format(score*10));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        lv.setAdapter(new ListViewAdapter(this, names, scores));
    }
}
