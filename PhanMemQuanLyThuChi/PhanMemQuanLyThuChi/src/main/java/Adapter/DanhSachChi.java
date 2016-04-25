package Adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import example.phanmemquanlythuchi.R;

import Object.TienThuChi;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author tungtx91
 */

public class DanhSachChi extends ArrayAdapter<TienThuChi> {
    Context context;
    int layoutResourceId;
    ArrayList<TienThuChi> listData = null;
    DecimalFormat df = new DecimalFormat("###############.##");

    public DanhSachChi(Context context, int layoutResourceId, ArrayList<TienThuChi> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.listData = data;
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
            holder.ten = (TextView) row.findViewById(R.id.textView_tenchi);
            holder.nhom = (TextView) row.findViewById(R.id.textView_nhomchi);
            holder.ngaythang = (TextView) row.findViewById(R.id.textView_ngaychi);
            holder.tien = (TextView) row.findViewById(R.id.textView_tienchi);
            row.setTag(holder);
        } else {
            holder = (ItemHolder) row.getTag();
        }
        TienThuChi item = listData.get(position);
        holder.ten.setText(item.getTen());
        holder.nhom.setText(item.getNhom());
        holder.ngaythang.setText(item.getNgaythang());
        holder.tien.setText(item.getVND());
        return row;
    }

    static class ItemHolder {
        TextView ten;
        TextView nhom;
        TextView ngaythang;
        TextView tien;

    }
}