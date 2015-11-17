package jon.com.securitometer;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button b1;

    PackageManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pm = getPackageManager();

        setUpButtonListeners();
    }

    public void setUpButtonListeners(){
        b1 = (Button)findViewById(R.id.button);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PackageInfo> pkginfo_list = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
                List<ApplicationInfo> appinfo_list = pm.getInstalledApplications(0);
                BackgroundTask bt = new BackgroundTask();
                bt.execute(appinfo_list.toArray(new ApplicationInfo[appinfo_list.size()]));
            }
        });
    }
}
