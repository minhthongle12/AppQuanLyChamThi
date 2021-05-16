package com.duan.quanlychamthi;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.duan.quanlychamthi.adapter.ThongKeAdapter;
import com.duan.quanlychamthi.adapter.ThongKeTinhTrangAdapter;
import com.duan.quanlychamthi.database.ChamBaiDao;
import com.duan.quanlychamthi.database.GiaoVienDao;
import com.duan.quanlychamthi.database.MonHocDao;
import com.duan.quanlychamthi.database.PhieuChamDao;
import com.duan.quanlychamthi.model.GiaoVien;
import com.duan.quanlychamthi.model.MonHoc;
import com.duan.quanlychamthi.model.PhieuChamBai;
import com.duan.quanlychamthi.model.ThongKe;
import com.duan.quanlychamthi.model.ThongKeTinhTrang;
import com.duan.quanlychamthi.model.ThongTinChamBai;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class ThongKeActivity extends AppCompatActivity {
    private TextInputEditText search;
    private SwipeMenuListView listView;
    private GiaoVienDao giaoVienDao;
    private PhieuChamDao phieuChamDao;
    private ChamBaiDao chamBaiDao;
    private MonHocDao monHocDao;
    private Button home;
    PieChartView pieChartView;
    private ArrayList<GiaoVien> listGV = new ArrayList();
    private ArrayList<PhieuChamBai> listPCB = new ArrayList();
    private ArrayList<ThongTinChamBai> listTTCB = new ArrayList();
    private ArrayList<MonHoc> listMH = new ArrayList();
    public static ArrayList<ThongKe> listTK = new ArrayList<>();
    public static ArrayList<ThongKeTinhTrang> listTinhTrang = new ArrayList<>();
    private ThongKeAdapter thongKeAdapter;
    private ThongKeTinhTrangAdapter thongKeTinhTrangAdapter;
    //Danh sách lọc
    ArrayList<String> list = new ArrayList<>();
    private Spinner spinner;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String currentDateandTime = sdf.format(new Date());
    LinearLayout lnChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);
        setTitle("THỐNG KÊ");
        search = findViewById(R.id.edtSearchTK);
        listView = findViewById(R.id.lvListTK);
        home = findViewById(R.id.btnHomeTK);
        giaoVienDao = new GiaoVienDao(this);
        phieuChamDao = new PhieuChamDao(this);
        chamBaiDao = new ChamBaiDao(this);
        monHocDao = new MonHocDao(this);
        listGV = giaoVienDao.getAll();
        listMH = monHocDao.getAll();
        listPCB = phieuChamDao.getAll();
        listTTCB = chamBaiDao.getAll();
        spinner = findViewById(R.id.locDuLieu);
        lnChart = findViewById(R.id.lnTKChart);
        pieChartView = findViewById(R.id.chart);
        //Khi nhấn nút home sẽ về trang chủ
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.in, R.anim.out);

            }
        });
        list.add("1. Thống kê tổng quát");
        list.add("2. Thống kê chi tiết");
        list.add("3. Thống kê tiến độ");

        final ArrayAdapter sp = new ArrayAdapter(ThongKeActivity.this, R.layout.onespinner, list);
        spinner.setAdapter(sp);
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                //Khi chọn spinner
                switch (position) {
                    case 0:
                        lnChart.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        fullThongKe();
                        listView.refreshDrawableState();
                        thongKeAdapter.notifyDataSetChanged();

                        break;
                    case 1:
                        lnChart.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        tinhTrang();
                        thongKeTinhTrangAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        listView.setVisibility(View.GONE);
                        lnChart.setVisibility(View.VISIBLE);
                        TextView soBai, soGV;
                        soBai = findViewById(R.id.tvTongSoBai);
                        soGV = findViewById(R.id.tvTongGV);

                        soBai.setText(tongBai() + "");
                        soGV.setText(tongGv() + "");

                        chart();
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {// do nothing
            }

        });


    }

    private void fullThongKe() {
        listTK.clear();
        //Set dữ liệu ban đầu cho listTk
        for (int i = 0; i < listTTCB.size(); i++) {
            String gv = "", mon = "", bai = "", tong = "";
            gv = String.valueOf(listTTCB.get(i).getSoPhieu());
            mon = String.valueOf(listTTCB.get(i).getMaMon());
            bai = String.valueOf(listTTCB.get(i).getSoBai());
                listTK.add(new ThongKe(gv, mon, bai, tong));
        }

        //Thay thế số phiếu bằng mã giáo viên
        for (int i = 0; i < listTK.size(); i++) {
            String gv = "", mon = "", bai = "";
            gv = listTK.get(i).getGiaoVien();
            mon = listTK.get(i).getMonHoc();
            bai = String.valueOf(listTK.get(i).getSoBai());
            for (int j = 0; j < listPCB.size(); j++) {
                if (gv.matches(String.valueOf(listPCB.get(j).getSoPhieu()))) {
                    listTK.set(i, new ThongKe(listPCB.get(j).getMaGv(), mon, bai, ""));
                    break;
                }
            }
        }

        //Set tên giáo viên lên listTK
        for (int i = 0; i < listGV.size(); i++) {
            String maGv = listGV.get(i).getMaGv();
            String tenGv = listGV.get(i).getHoTenGv();
            for (int j = 0; j < listTK.size(); j++) {
                if (listTK.get(j).getGiaoVien().matches(maGv)) {
                    ThongKe tk = listTK.get(j);
                    listTK.set(j, new ThongKe(tenGv, tk.getMonHoc(), tk.getSoBai(), ""));
                }
            }
        }

        //Set tên môn và giá lên list
        for (int i = 0; i < listMH.size(); i++) {
            MonHoc mh = listMH.get(i);
            String ma, ten;
            int gia;
            ma = mh.getMaMon();
            ten = mh.getTenMon();
            gia = mh.getChiPhiMon();

            for (int j = 0; j < listTK.size(); j++) {
                ThongKe tk = listTK.get(j);
                String maMon = tk.getMonHoc();
                if (maMon.matches(ma)) {
                    listTK.set(j, new ThongKe(tk.getGiaoVien(), mh.getTenMon(),
                            tk.getSoBai(), String.valueOf(gia * Integer.parseInt(tk.getSoBai()))));
                }
            }
        }

        //Gộp số bài và giá tiền nếu tên giáo viên và tên môn giống nhau
        for (int i = 0; i < listTK.size(); i++) {
            for (int j = i + 1; j < listTK.size() - 1; j++) {
                if (listTK.get(i).getGiaoVien().contains(listTK.get(j).getGiaoVien()) ||
                        listTK.get(i).getMonHoc().contains(listTK.get(j).getMonHoc())) {
                    ThongKe tk = listTK.get(i);
                    ThongKe tk2 = listTK.get(j);
                    listTK.set(i, new ThongKe(tk.getGiaoVien(), tk.getMonHoc(),
                            String.valueOf(Integer.parseInt(tk.getSoBai()) + Integer.parseInt(tk2.getSoBai())),
                            String.valueOf(Integer.parseInt(tk.getTongTien()) + Integer.parseInt(tk2.getTongTien()))));
                    listTK.remove(j);
                }
            }
        }

        thongKeAdapter = new ThongKeAdapter(this, listTK);
        listView.setAdapter(thongKeAdapter);

        //Lọc theo tìm kiếm
        listView.setTextFilterEnabled(true);
        TextInputEditText edSeach = findViewById(R.id.edtSearchTK);
        edSeach.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int
                    count) {
                System.out.println("Text [" + s + "] - Start [" + start + "] - Before [" + before + "] - Count [" + count + "]");
                if (count < before) {
                    thongKeAdapter.resetData();
                }
                thongKeAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in, R.anim.out);
        finish();
    }

    //Thống kê tổng số bài và tổng giáo viên
    public int tongBai() {
        int tong = 0;
        for (int i = 0; i < listTTCB.size(); i++) {
            tong += listTTCB.get(i).getSoBai();
        }

        return tong;
    }

    public int tongGv() {
        return listGV.size();
    }


    //Thống kê tình trạng hoàn thành
    public void tinhTrang() {
        listTinhTrang.clear();
        //Set dữ liệu ban đầu cho listTk
        for (int i = 0; i < listTTCB.size(); i++) {
            String gv = "", mon = "", bai = "", tong = "", tinhTrang = "";
            gv = String.valueOf(listTTCB.get(i).getSoPhieu());
            mon = String.valueOf(listTTCB.get(i).getMaMon());
            bai = String.valueOf(listTTCB.get(i).getSoBai());
            listTinhTrang.add(new ThongKeTinhTrang(gv, mon, bai, tong, tinhTrang));
        }

        //Thay thế số phiếu bằng mã giáo viên
        for (int i = 0; i < listTinhTrang.size(); i++) {
            String gv = "", mon = "", bai = "";
            gv = listTinhTrang.get(i).getGiaoVien();
            mon = listTinhTrang.get(i).getMonHoc();
            bai = String.valueOf(listTinhTrang.get(i).getSoBai());
            for (int j = 0; j < listPCB.size(); j++) {
                if (gv.matches(String.valueOf(listPCB.get(j).getSoPhieu()))) {
                    listTinhTrang.set(i, new ThongKeTinhTrang(listPCB.get(j).getMaGv(), mon, bai, "", listPCB.get(i).getNgayPhieu()));
                    break;
                }
            }
        }

        //Set tên giáo viên lên listTinhTrang
        for (int i = 0; i < listGV.size(); i++) {
            String maGv = listGV.get(i).getMaGv();
            String tenGv = listGV.get(i).getHoTenGv();
            for (int j = 0; j < listTinhTrang.size(); j++) {
                if (listTinhTrang.get(j).getGiaoVien().matches(maGv)) {
                    ThongKeTinhTrang tk = listTinhTrang.get(j);
                    listTinhTrang.set(j, new ThongKeTinhTrang(tenGv, tk.getMonHoc(), tk.getSoBai(), "", tk.getNgayGiao()));
                }
            }
        }

        //Set tên môn và giá lên list
        for (int i = 0; i < listMH.size(); i++) {
            MonHoc mh = listMH.get(i);
            String ma, ten;
            int gia;
            ma = mh.getMaMon();
            ten = mh.getTenMon();
            gia = mh.getChiPhiMon();

            for (int j = 0; j < listTinhTrang.size(); j++) {
                ThongKeTinhTrang tk = listTinhTrang.get(j);
                String maMon = tk.getMonHoc();
                if (maMon.matches(ma)) {
                    listTinhTrang.set(j, new ThongKeTinhTrang(tk.getGiaoVien(), mh.getTenMon(),
                            tk.getSoBai(), String.valueOf(gia * Integer.parseInt(tk.getSoBai())), tk.getNgayGiao()));
                }
            }
        }

        thongKeTinhTrangAdapter = new ThongKeTinhTrangAdapter(this, listTinhTrang);
        listView.setAdapter(thongKeTinhTrangAdapter);
    }

    //show biểu đồ
    public void chart() {
        //Tính số bài chưa chấm
        tinhTrang();
        int chuaGiao = 0;
        for (int i = 0; i < listTinhTrang.size(); i++) {
            try {
                Date ngay = sdf.parse(listTinhTrang.get(i).getNgayGiao());
                Date today = sdf.parse(currentDateandTime);
                if (today.before(ngay)) {
                    chuaGiao += Integer.parseInt(listTinhTrang.get(i).getSoBai());
                }
            } catch (Exception e) {

            }
        }

        int daGiao = tongBai()- chuaGiao;


        List pieData = new ArrayList<>();
        pieData.add(new SliceValue(chuaGiao, Color.BLUE).setLabel("Chưa giao: "+chuaGiao));
        pieData.add(new SliceValue(daGiao, Color.RED).setLabel("Đã giao: "+daGiao));

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
        pieChartData.setHasCenterCircle(true).setCenterText1("Thống kê số bài").setCenterText1FontSize(20).setCenterText1Color(this.getColor(R.color.violet));
        pieChartView.setPieChartData(pieChartData);
    }
}