package com.example.admin.youhuo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.youhuo.R;

import java.util.List;

/**
 * Created by admin on 2016/11/22.
 */

public class MyAdapter1 extends BaseAdapter {
    private Context context;
    private List<String> list;

    public MyAdapter1(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(context, R.layout.item, null);
            holder.mTv = (TextView) view.findViewById(R.id.mTv);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mTv.setText(list.get(i));
        return view;
    }

    private class ViewHolder {
        private TextView mTv;
    }
}
