package Adapter;

import example.phanmemquanlythuchi.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Menu extends BaseAdapter {

    private final String[] web;
    private final int[] Imageid;
    private Context mContext;

    public Menu(Context c, String[] web, int[] Imageid) {
        mContext = c;
        this.Imageid = Imageid;
        this.web = web;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (arg1 == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.t_custom_menu, null);
            TextView textView = (TextView) grid.findViewById(R.id.text_ten_custem_menu);
            ImageView imageView = (ImageView) grid.findViewById(R.id.img_anh_custem_menu);
            textView.setText(web[arg0]);
            imageView.setImageResource(Imageid[arg0]);
        } else {
            grid = arg1;
        }
        return grid;
    }

    @Override
    public int getCount() {
        return web.length;
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }
}
