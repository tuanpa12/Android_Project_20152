package Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.phanmemquanlychitieu.R;

import java.util.ArrayList;

import Objects.BaoCao;

public class BaoCaoQuy extends ArrayAdapter<BaoCao> {
    Context context;
    int layoutResourceId;
    ArrayList<BaoCao> listData = null;
    ArrayList<String> a;

    public BaoCaoQuy(Context context, int layoutResourceId, ArrayList<BaoCao> data, ArrayList<String> a) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.listData = data;
        this.a = a;
    }

    @Override
    public int getCount() {

        return this.listData.size();
    }

    @Override
    public BaoCao getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ItemHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ItemHolder();
            holder.title = (TextView) row.findViewById(R.id.textView_tenquytrongnam);
            holder.hieuthuchi = (TextView) row.findViewById(R.id.textView_hieuthuchi);
            holder.tienthu = (TextView) row.findViewById(R.id.textView_thuquy);
            holder.tienchi = (TextView) row.findViewById(R.id.textView_chiquy);
            row.setTag(holder);
        } else {
            holder = (ItemHolder) row.getTag();
        }
        //Object_BaoCaoHienTai item=listData.get(position);
        holder.title.setText(a.get(position));
        holder.hieuthuchi.setText(String.valueOf(Integer.parseInt(listData.get(position).getTienthu()) - Integer.parseInt(listData.get(position).getTienchi())));
        holder.tienthu.setText(listData.get(position).getTienthu());
        holder.tienchi.setText(listData.get(position).getTienchi());
        return row;
    }

    static class ItemHolder {
        TextView title;
        TextView hieuthuchi;
        TextView tienthu;
        TextView tienchi;

    }
}