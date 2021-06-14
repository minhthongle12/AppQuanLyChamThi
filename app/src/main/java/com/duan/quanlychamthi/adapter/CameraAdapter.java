package com.duan.quanlychamthi.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.duan.quanlychamthi.R;
import com.duan.quanlychamthi.model.Image;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CameraAdapter extends BaseAdapter {
    Context context;
    ArrayList<Image> list;
    ArrayList<Image> listSort;
    Filter filter;

    public CameraAdapter(Context context, ArrayList<Image> list) {
        this.context = context;
        this.list = list;
        this.listSort = list;
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
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        final Image image = list.get(i);
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.onecamera, null);
            holder.tvImage = view.findViewById(R.id.tvImage);
            holder.ivImage = view.findViewById(R.id.ivImage);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvImage.setText("Hình "+image.getId());
        Picasso.with(context).load(image.getImage()).into(holder.ivImage);
        return view;
    }
    class ViewHolder {
        TextView tvImage;
        ImageView ivImage;
    }
    public void resetData() {
        list = listSort;
    }

    public Filter getFilter() {
        if (filter == null)
            filter = new CustomFilter();
        return filter;
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
// We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                results.values = listSort;
                results.count = listSort.size();
            } else {
                ArrayList<Image> lsSach = new ArrayList<>();
                for (Image p : list) {
                    if (String.valueOf(p.getId()).toUpperCase().contains(constraint.toString().toUpperCase()))
                        lsSach.add(p);
                }
                results.values = lsSach;
                results.count = lsSach.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                list = (ArrayList<Image>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}
