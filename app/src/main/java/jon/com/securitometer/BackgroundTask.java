package jon.com.securitometer;

import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jon on 11/11/15.
 */
public class BackgroundTask extends AsyncTask<ApplicationInfo, Integer, Void> {
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    @Override
    protected Void doInBackground(ApplicationInfo... appinfo_list) {
        String[] pkg_path = new String[appinfo_list.length];
        for (int x=0; x < appinfo_list.length; x++) {
            pkg_path[x] = appinfo_list[x].publicSourceDir; //store package path in array
            Log.d("File", pkg_path[x]);
            File file = new File(pkg_path[x]);
            int size = (int) file.length();
            byte[] bytes = new byte[size];
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                buf.read(bytes, 0, bytes.length);
                buf.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            MessageDigest digest=null;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            Log.d("SHA256", bytesToHex(digest.digest(bytes)));
        }
        return null;
    }
}
