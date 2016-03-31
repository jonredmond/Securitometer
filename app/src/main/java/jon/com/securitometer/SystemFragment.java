package jon.com.securitometer;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by jon on 02/03/16.
 */
public class SystemFragment extends Fragment {

    public static SystemFragment newInstance() {
        SystemFragment systemFragment = new SystemFragment();

        return systemFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_system, container, false);

        Securitometer securitometer = (Securitometer) getActivity().getApplicationContext();

        Device device = securitometer.getDevice();

        TextView appScore = (TextView) view.findViewById(R.id.appScore);
        appScore.setText(new DecimalFormat("0.00").format(device.getAppScore()));
        ((GradientDrawable)appScore.getBackground()).setColor(Utils.redGreenColourScale(device.getAppScore()));

        TextView osScore = (TextView) view.findViewById(R.id.osScore);
        osScore.setText(new DecimalFormat("0.00").format(device.getOsScore()));
        ((GradientDrawable)osScore.getBackground()).setColor(Utils.redGreenColourScale(device.getOsScore()));

        return view;
    }
}
