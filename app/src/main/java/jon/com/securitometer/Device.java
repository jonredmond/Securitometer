package jon.com.securitometer;

import java.util.Vector;

/**
 * Created by jon on 08/02/16.
 */
public class Device {
    private float appScore;
    private float osScore;
    public Vector<App> apps;

    public Device(){
        apps = new Vector<App>();
    }

    public void setAppScore(float appScore){
        this.appScore = appScore;
    }

    public void setOsScore(float osScore){
        this.osScore = osScore;
    }

    private float getAppScore(){
        return this.appScore;
    }

    private float getOsScore(){
        return this.osScore;
    }
}
