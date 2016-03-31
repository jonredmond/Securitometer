package jon.com.securitometer;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jon on 29/02/16.
 */
public class Utils {
    public static String AssetJSONFile(String filename, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }

    public static OS getNextBestVersion(String currVersion, double score, Context context) {
        String[] AndroidVersions = {"1.5", "1.6", "2.0", "2.0.1", "2.1", "2.2", "2.2.1", "2.2.2", "2.2.3", "2.3", "2.3.1", "2.3.2", "2.3.3",
                "2.3.4", "2.3.5", "2.3.6", "2.3.7", "3.0", "3.1", "3.2", "3.2.1", "3.2.2", "3.2.3", "3.2.4", "3.2.5", "3.2.6",
                "4.0", "4.0.1", "4.0.2", "4.0.3", "4.0.4", "4.1", "4.1.1", "4.1.2", "4.2", "4.2.1", "4.2.2", "4.3", "4.3.1", "4.4",
                "4.4.1", "4.4.2", "4.4.3", "4.4.4", "5.0", "5.0.1", "5.0.2", "5.1", "5.1.1", "6.0", "6.0.1"};

        boolean found = false;
        for(String version : AndroidVersions) {
            if(!found) {
                if(version.equals(currVersion)) found = true;
                continue;
            } else {
                OS tempOS = OS.scanOS(version, context);
                if(tempOS.getScore() > score) {
                    return tempOS;
                }
            }
        }
        return null;
    }

    public static int redGreenColourScale(double value) {
        int r = 0;
        int g = 0;
        int b = 0;

        if(value <= 5.0) {
            g = (int)(51*value);
            r = 255;
        } else {
            r = 255 - (int)(51*(value-5.0));
            g = 255;
        }
        return Color.rgb(r, g, b);
    }

}
