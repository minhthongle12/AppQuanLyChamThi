package com.duan.quanlychamthi.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.duan.quanlychamthi.R;
import com.duan.quanlychamthi.model.GiaoVien;
import com.duan.quanlychamthi.model.MonHoc;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MonHocAdapter extends BaseAdapter {
    Context context;
    ArrayList<MonHoc> list;
    ArrayList<MonHoc> listSort;
    Filter filter;
    DecimalFormat fm = new DecimalFormat("#,###");

    public MonHocAdapter(Context context, ArrayList<MonHoc> list) {
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
        final MonHoc x = list.get(i);
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
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.cot1.setText("Mã môn học: ");
        holder.cot2.setText("Tên môn học: ");
        holder.cot3.setText("Chi phí: ");
        holder.tencot1.setText(x.getMaMon());
        holder.tencot2.setText(x.getTenMon());
        holder.tencot3.setText(fm.format(x.getChiPhiMon())+" VND");
        return view;
    }

    class ViewHolder {
        TextView cot1, cot2, cot3, tencot1, tencot2, tencot3;
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
                ArrayList<MonHoc> lsSach = new ArrayList<>();
                for (MonHoc p : list) {
                    if (p.getMaMon().toUpperCase().contains(constraint.toString().toUpperCase()) ||
                            p.getTenMon().toUpperCase().contains(constraint.toString().toUpperCase()) ||
                            String.valueOf(p.getChiPhiMon()).toUpperCase().contains(constraint.toString().toUpperCase()))
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
                list = (ArrayList<MonHoc>) results.values;
                notifyDataSetChanged();
            }
        }
    }


}
