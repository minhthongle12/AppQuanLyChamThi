package com.duan.quanlychamthi.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duan.quanlychamthi.R;
import com.duan.quanlychamthi.model.ThongKe;
import com.duan.quanlychamthi.model.ThongKeTinhTrang;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ThongKeTinhTrangAdapter extends BaseAdapter {
    Context context;
    ArrayList<ThongKeTinhTrang> list;
    ArrayList<ThongKeTinhTrang> listSort;
    Filter filter;
    DecimalFormat fm = new DecimalFormat("#,###");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String currentDateandTime = sdf.format(new Date());
    public ThongKeTinhTrangAdapter(Context context, ArrayList<ThongKeTinhTrang> list) {
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

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        final ThongKeTinhTrang giaoVien = list.get(i);
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.onethongke, null);
            holder.cot1 = view.findViewById(R.id.tenGVTK);
            holder.cot2 = view.findViewById(R.id.tenMonTK);
            holder.cot3 = view.findViewById(R.id.soBaiTK);
            holder.cot4 = view.findViewById(R.id.tongTK);
            holder.tinhTrang = view.findViewById(R.id.tvTinhTrang);
            holder.lntinhTrang = view.findViewById(R.id.lnTinhTrang);
            holder.ngay = view.findViewById(R.id.tvNgayGiao);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.lntinhTrang.setVisibility(View.VISIBLE);
        holder.cot1.setText(giaoVien.getGiaoVien());
        holder.cot2.setText(giaoVien.getMonHoc());
        holder.cot3.setText(giaoVien.getSoBai());
        holder.cot4.setText(fm.format(Integer.parseInt(giaoVien.getTongTien()))+" VND");
        holder.ngay.setText(giaoVien.getNgayGiao());
        //Chuyển ngày
        try {
            Date ngay = sdf.parse(giaoVien.getNgayGiao());
            Date today = sdf.parse(currentDateandTime);
            if(today.after(ngay)||today.equals(ngay)){
                holder.tinhTrang.setText("Đã được giao");
                holder.tinhTrang.setTextColor(context.getColor(R.color.green));
            }
            else {
                holder.tinhTrang.setText("Chưa được giao");
                holder.tinhTrang.setTextColor(context.getColor(R.color.red));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


        return view;
    }

    class ViewHolder {
        TextView cot1, cot2, cot3, cot4, tinhTrang, ngay;
        LinearLayout lntinhTrang;
    }

    public void resetData() {
        list = listSort;
    }

    public Filter getFilter() {
        if (filter == null)
            filter = new ThongKeTinhTrangAdapter.CustomFilter();
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
                ArrayList<ThongKeTinhTrang> lsSach = new ArrayList<>();
                for (ThongKeTinhTrang p : list) {
                    if (p.getGiaoVien().toUpperCase().contains((constraint.toString().toUpperCase())) ||
                            p.getMonHoc().toUpperCase().contains(constraint.toString().toUpperCase())) {
                        lsSach.add(p);
                    }
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
                list = (ArrayList<ThongKeTinhTrang>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}
