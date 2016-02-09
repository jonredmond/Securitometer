package jon.com.securitometer;

import android.content.pm.PackageInfo;

/**
 * Created by Jon on 09/02/2016.
 */
public class App {
    private String name;
    private PackageInfo packageInfo;
    private double score;

    public App(String name, PackageInfo packageInfo, double score) {
        this.name = name;
        this.packageInfo = packageInfo;
        this.score = score;
    }
}
