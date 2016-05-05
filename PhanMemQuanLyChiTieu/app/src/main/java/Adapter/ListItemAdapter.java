package Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.phanmemquanlychitieu.R;

import java.util.ArrayList;

import Objects.ChildItem;

/**
 * Created by Legendary on 05/05/2016.
 */
public class ListItemAdapter extends ArrayAdapter {
    Activity context;
    ArrayList<ChildItem> myList;
    int layoutId;

    public ListItemAdapter(Activity context, ArrayList<ChildItem> myList, int layoutId) {
        super(context, layoutId, myList);
        this.context = context;
        this.layoutId = layoutId;
        this.myList = myList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(layoutId, parent, false);

            holder = new ViewHolder();
            holder.type = (TextView) convertView.findViewById(R.id.type);
            holder.cost = (TextView) convertView.findViewById(R.id.cost);

            ChildItem childItem = myList.get(position);
            holder.type.setText(childItem.getType());
            holder.cost.setText(childItem.getCost());
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    static class ViewHolder {
        TextView type;
        TextView cost;
    }
}
