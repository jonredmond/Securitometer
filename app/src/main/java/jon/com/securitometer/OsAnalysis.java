package jon.com.securitometer;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DecimalFormat;

public class OsAnalysis extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os_analysis);

        Intent intent = getIntent();
        int vulnNo = intent.getIntExtra("vulnNo", 0);

        Securitometer securitometer = (Securitometer)getApplicationContext();
        Device device = securitometer.getDevice();
        String vulnName = device.os.getVulns().get(vulnNo);

        TextView textVulnName = (TextView) findViewById(R.id.vulnName);
        textVulnName.setText(vulnName);

        try {
            String json = Utils.AssetJSONFile(vulnName + ".json", getApplicationContext());
            JSONObject obj = new JSONObject(json);
            String description = obj.getJSONArray("Details").getJSONArray(0).getString(0);
            String score = new DecimalFormat("0.00").format(obj.getDouble("Score"));
            String affectedVersions = obj.getJSONArray("Affected_versions").getJSONArray(0).getString(0);

            TextView textDescription = (TextView) findViewById(R.id.description);
            textDescription.setText(description);

            TextView textScore = (TextView) findViewById(R.id.vulnScore);
            textScore.setText(score);
            ((GradientDrawable)textScore.getBackground()).setColor(Utils.redGreenColourScale(10.0 - obj.getDouble("Score")));

            TextView textAffectedVersions = (TextView) findViewById(R.id.affectedVersions);
            textAffectedVersions.setText("Affected Versions: " + affectedVersions);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
