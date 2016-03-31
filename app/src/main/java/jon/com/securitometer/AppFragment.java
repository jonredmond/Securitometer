package jon.com.securitometer;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by jon on 29/02/16.
 */
public class AppFragment extends android.support.v4.app.Fragment {
    private double _score;
    private ArrayList<App> _apps;

    public static AppFragment newInstance(){
        AppFragment appFragment = new AppFragment();
        return appFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.fragment_app, container, false);

        final Securitometer securitometer = (Securitometer) getActivity().getApplicationContext();

        final Device device = securitometer.getDevice();

        double score = securitometer.getDevice().getAppScore();

        TextView appScore = (TextView)view.findViewById(R.id.appScore);
        appScore.setText(new DecimalFormat("0.00").format(score));
        ((GradientDrawable)appScore.getBackground()).setColor(Utils.redGreenColourScale(score));

        ListView lv = (ListView) view.findViewById(R.id.listApps);

        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> scores = new ArrayList<>();
        for(App a: device.apps) {
            names.add(a.getName());
            scores.add(new DecimalFormat("0.00").format(a.getScore()));
        }

        lv.setAdapter(new ListViewAdapter(this.getActivity(), names, scores));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), AppAnalysis.class);
                intent.putExtra("appNo", position);
                getActivity().startActivity(intent);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        GraphView graph = (GraphView) view.findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
                        for(int i = 0; i < device.apps.size(); ++i) {
                            series.appendData(new DataPoint(i, device.apps.get(i).getScore()), true, device.apps.size());
                        }

                        graph.addSeries(series);
                        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                            @Override
                            public String formatLabel(double value, boolean isValueX) {
                                if (isValueX) {
                                    return "";
                                } else {
                                    return super.formatLabel(value, isValueX);
                                }
                            }
                        });
                        graph.getGridLabelRenderer().setHorizontalAxisTitle("Apps");
                        graph.getViewport().setXAxisBoundsManual(true);
                        graph.getViewport().setYAxisBoundsManual(true);
                        graph.getViewport().setMinX(0);
                        graph.getViewport().setMaxX(device.apps.size());
                        graph.getViewport().setMinY(0);
                        graph.getViewport().setMaxY(10);
                    }
                });

            }
        }).start();

        return view;
    }
}
