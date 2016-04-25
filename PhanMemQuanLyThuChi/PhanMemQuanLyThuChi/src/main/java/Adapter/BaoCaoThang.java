package Adapter;

import java.util.ArrayList;

import Object.BaoCao;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import example.phanmemquanlythuchi.R;

public class BaoCaoThang extends ArrayAdapter<BaoCao> {
    double Tong;
    Context context;
    int layoutResourceId;
    ArrayList<BaoCao> listData = null;
    ArrayList<Integer> ImageId;
    ArrayList<String> title;

    public BaoCaoThang(Context context, int layoutResourceId, ArrayList<BaoCao> data, ArrayList<Integer> ImageId, ArrayList<String> title) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.listData = data;
        this.ImageId = ImageId;
        this.title = title;
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
        ItemHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ItemHolder();
            holder.title = (TextView) row.findViewById(R.id.textView_titlethang);
            holder.tiencon = (TextView) row.findViewById(R.id.textView_tienduthang);
            holder.tienthu = (TextView) row.findViewById(R.id.textView_tienthuthang);
            holder.tienchi = (TextView) row.findViewById(R.id.textView_tienchithang);
            holder.image = (ImageView) row.findViewById(R.id.imageView_hinhanhthang1);
            row.setTag(holder);
        } else {
            holder = (ItemHolder) row.getTag();
        }
        // Object_BaoCaoHienTai item=listData.get(position);
        holder.title.setText(title.get(position));
        holder.tiencon.setText(String.valueOf(Double.parseDouble(listData.get(position).getTienthu()) - Double.parseDouble(listData.get(position).getTienchi())));
        holder.tienthu.setText(listData.get(position).getTienthu());
        holder.tienchi.setText(listData.get(position).getTienchi());
        holder.image.setImageResource(ImageId.get(position));
        return row;
    }

    static class ItemHolder {
        TextView tiencon;
        TextView tienthu;
        TextView tienchi;
        ImageView image;
        TextView title;
    }
}