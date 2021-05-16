package com.duan.quanlychamthi.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.duan.quanlychamthi.CameraActivity;
import com.duan.quanlychamthi.R;
import com.duan.quanlychamthi.model.GiaoVien;
import com.duan.quanlychamthi.model.GiaoVien;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GiaoVienAdapter  extends BaseAdapter {
    Context context;
    ArrayList<GiaoVien> list;
    ArrayList<GiaoVien> listSort;
    Filter filter;

    public GiaoVienAdapter(Context context, ArrayList<GiaoVien> list) {
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
        final GiaoVien giaoVien = list.get(i);
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.onelayout, null);
            holder.cot1 = view.findViewById(R.id.tvCot1);
            holder.cot2 = view.findViewById(R.id.tvCot2);
            holder.cot3 = view.findViewById(R.id.tvCot3);
            holder.tencot1 = view.findViewById(R.id.tvTenCot1);
            holder.tencot2 = view.findViewById(R.id.tvTenCot2);
            holder.tencot3 = view.findViewById(R.id.tvTenCot3);
            holder.imageView = view.findViewById(R.id.ivImageGV);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //Hiện ảnh
        holder.imageView.setVisibility(View.VISIBLE);

        holder.cot1.setText("Mã giáo viên: ");
        holder.cot2.setText("Họ và tên: ");
        holder.cot3.setText("Số điện thoại: ");
        holder.tencot1.setText(giaoVien.getMaGv());
        holder.tencot2.setText(giaoVien.getHoTenGv());
        holder.tencot3.setText(giaoVien.getSdtGv());
        Picasso.with(context).load(giaoVien.getLinkImage()).into(holder.imageView);
        return view;
    }
    class ViewHolder {
        TextView cot1, cot2, cot3, tencot1, tencot2, tencot3;
        ImageView imageView;
    }
    public void resetData() {
        list = listSort;
    }

    public Filter getFilter() {
        if (filter == null)
            filter = new GiaoVienAdapter.CustomFilter();
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
                ArrayList<GiaoVien> lsSach = new ArrayList<>();
                for (GiaoVien p : list) {
                    if (p.getMaGv().toUpperCase().contains(constraint.toString().toUpperCase()) ||
                            p.getHoTenGv().toUpperCase().contains(constraint.toString().toUpperCase())||
                            p.getSdtGv().toUpperCase().contains(constraint.toString().toUpperCase()))
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
                list = (ArrayList<GiaoVien>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}
