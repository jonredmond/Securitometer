package jon.com.securitometer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jon on 08/02/16.
 */
public class Device {
    private double _appScore;
    private double _osScore;
    public ArrayList<App> apps;
    public OS os;

    public Device(){
        apps = new ArrayList<App>();
    }

    public void setAppScore(double appScore){
        this._appScore = appScore;
    }

    public void setOsScore(double osScore){
        this._osScore = osScore;
    }

    public double getAppScore(){
        return this._appScore;
    }

    public double getOsScore(){
        return this._osScore;
    }
}
