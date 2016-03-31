package jon.com.securitometer;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jon on 25/03/16.
 */
public class ListViewAdapter extends BaseAdapter {
    ArrayList<String> labels;
    ArrayList<String> scores;
    Activity activity;

    public ListViewAdapter(Activity activity, ArrayList<String> labels, ArrayList<String> scores) {
        super();
        this.activity = activity;
        this.labels = labels;
        this.scores = scores;
    }
    @Override
    public int getCount() {
        return labels.size();
    }

    @Override
    public Object getItem(int position) {
        return labels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, null);
        }
        TextView label = (TextView) convertView.findViewById(R.id.lblListItem);
        TextView score = (TextView) convertView.findViewById(R.id.itemScore);

        label.setText(labels.get(position));
        score.setText(scores.get(position));
        ((GradientDrawable)score.getBackground()).setColor(Utils.redGreenColourScale(Double.parseDouble(scores.get(position))));

        return convertView;
    }
}
