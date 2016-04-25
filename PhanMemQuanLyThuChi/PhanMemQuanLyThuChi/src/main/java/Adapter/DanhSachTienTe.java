package Adapter;

import java.util.ArrayList;

import Object.TienThuChi;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import example.phanmemquanlythuchi.R;

public class DanhSachTienTe extends ArrayAdapter<TienThuChi> {
    String abc;
    //HTMLParser html;
    Context context;
    int layoutResourceId;
    ArrayList<TienThuChi> listData = null;

    public DanhSachTienTe(Context context, int layoutResourceId, ArrayList<TienThuChi> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.listData = data;
    }

    public void vietBua(String bcd) {
        abc = bcd;
    }

    @Override
    public int getCount() {

        return this.listData.size();
    }

    @Override
    public TienThuChi getItem(int position) {
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
            //html=new HTMLParser();

            holder.ten = (TextView) row.findViewById(R.id.textView_tenkhoanthu_custom);
            holder.nhom = (TextView) row.findViewById(R.id.textView_nhomkhoanthu_custom);
            holder.ngaythang = (TextView) row.findViewById(R.id.textView_ngaykhoanthu_custom);
            holder.tien = (TextView) row.findViewById(R.id.textView_tienkhoanthu_custom);
            row.setTag(holder);
        } else {
            holder = (ItemHolder) row.getTag();
        }
        TienThuChi item = listData.get(position);
        holder.ten.setText(item.getTen());
        holder.nhom.setText(item.getNhom());
        holder.ngaythang.setText(item.getNgaythang());
        holder.tien.setText(item.getNgoaiTe(abc));
        return row;
    }

    static class ItemHolder {
        TextView ten;
        TextView nhom;
        TextView ngaythang;
        TextView tien;

    }
}
