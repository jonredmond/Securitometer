package jon.com.securitometer;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jon on 08/01/16.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in the form header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private HashMap<String, String> _listScores;
    private HashMap<String, String> _listChildScores;

    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, String> listScores, HashMap<String, List<String>> listDataChild, HashMap<String, String> listChildScores){
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listDataChild;
        this._listScores = listScores;
        this._listChildScores = listChildScores;
    }


    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final String headerTitle = this._listDataHeader.get(groupPosition);
        final String score = this._listScores.get(headerTitle);
        if(convertView == null) {
            LayoutInflater infalInflator = (LayoutInflater)this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflator.inflate(R.layout.list_group, null);
        }
        TextView txtListHeader = (TextView)convertView.findViewById(R.id.lblListHeader);
        TextView txtScore = (TextView)convertView.findViewById(R.id.score);
        txtListHeader.setText(headerTitle);
        txtScore.setText(score);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String)getChild(groupPosition, childPosition);
        final String score = _listChildScores.get(childText);
        if(convertView == null) {
            LayoutInflater infalInflator = (LayoutInflater)this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflator.inflate(R.layout.list_item, null);
        }
        TextView txtListChild = (TextView)convertView.findViewById(R.id.lblListItem);
        TextView txtChildScore = (TextView)convertView.findViewById(R.id.itemScore);
        txtListChild.setText(childText);
        txtChildScore.setText(score);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
