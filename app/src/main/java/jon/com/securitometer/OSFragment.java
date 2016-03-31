package jon.com.securitometer;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by jon on 02/03/16.
 */
public class OSFragment extends Fragment {
    public static OSFragment newInstance() {
        OSFragment osFragment = new OSFragment();

        return osFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.fragment_os, container, false);

        ListView lv = (ListView) view.findViewById(R.id.listVulns);

        TextView score = (TextView) view.findViewById(R.id.score);

        Securitometer securitometer = (Securitometer) getActivity().getApplicationContext();
        Device device = securitometer.getDevice();

        score.setText(new DecimalFormat("0.00").format(device.getOsScore()));
        ((GradientDrawable)score.getBackground()).setColor(Utils.redGreenColourScale(device.getOsScore()));

        TextView osHelp = (TextView) view.findViewById(R.id.osHelp);

        OS betterVersion = Utils.getNextBestVersion(android.os.Build.VERSION.RELEASE, device.getOsScore(), getContext());


        if(betterVersion != null) {
            osHelp.setText("The Android Version installed on your device is \"" + android.os.Build.VERSION.RELEASE + "\". Upgrading to  \"" + betterVersion.getVersion() + "\" would raise your android version score to " + betterVersion.getScore() + " (upgrading to a more recent version of android may slow your device down)");
        } else {
            TextView lbl = (TextView)view.findViewById(R.id.lbl);
            lbl.setText("The version of android installed on your device has no currently known exploits");
            osHelp.setText("");
        }

        lv.setAdapter(new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, device.os.getVulns()));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), OsAnalysis.class);
                intent.putExtra("vulnNo", position);

                startActivity(intent);

            }
        });

        return view;
    }
}
