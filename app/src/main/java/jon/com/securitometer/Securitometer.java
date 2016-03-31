package jon.com.securitometer;

import android.app.Application;

/**
 * Created by jon on 01/03/16.
 */
public class Securitometer extends Application {
    private Device _device;

    public Device getDevice() {
        return this._device;
    }

    public void setDevice(Device d) {
        this._device = d;
    }
}
