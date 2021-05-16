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
import com.duan.quanlychamthi.model.ThongTinChamBai;
import com.duan.quanlychamthi.model.PhieuChamBai;
import com.duan.quanlychamthi.model.ThongTinChamBai;

import java.util.ArrayList;

public class ThongTinChamAdapter extends BaseAdapter {
    Context context;
    ArrayList<ThongTinChamBai> list;
    ArrayList<ThongTinChamBai> listSort;
    Filter filter;

    public ThongTinChamAdapter(Context context, ArrayList<ThongTinChamBai> list) {
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
        final ThongTinChamBai x = list.get(i);
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
        holder.cot1.setText("Số phiếu: ");
        holder.cot2.setText("Mã môn học: ");
        holder.cot3.setText("Số bài: ");
        holder.tencot1.setText(x.getSoPhieu()+"");
        holder.tencot2.setText(x.getMaMon());
        holder.tencot3.setText(x.getSoBai()+"");
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
            filter = new ThongTinChamAdapter.CustomFilter();
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
                ArrayList<ThongTinChamBai> lsSach = new ArrayList<>();
                for (ThongTinChamBai p : list) {
                    if (p.getMaMon().toUpperCase().contains(constraint.toString().toUpperCase()) ||
                            String.valueOf(p.getSoBai()).toUpperCase().contains(constraint.toString().toUpperCase())||
                            String.valueOf(p.getSoPhieu()).toUpperCase().contains(constraint.toString().toUpperCase()))
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
                list = (ArrayList<ThongTinChamBai>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}
