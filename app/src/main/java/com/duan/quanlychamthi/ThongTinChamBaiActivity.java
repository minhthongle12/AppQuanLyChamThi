package com.duan.quanlychamthi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.android.material.textfield.TextInputEditText;

import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.duan.quanlychamthi.adapter.ThongTinChamAdapter;
import com.duan.quanlychamthi.database.GiaoVienDao;
import com.duan.quanlychamthi.database.ChamBaiDao;
import com.duan.quanlychamthi.database.MonHocDao;
import com.duan.quanlychamthi.database.PhieuChamDao;
import com.duan.quanlychamthi.model.GiaoVien;
import com.duan.quanlychamthi.model.Image;
import com.duan.quanlychamthi.model.MonHoc;
import com.duan.quanlychamthi.model.PhieuChamBai;
import com.duan.quanlychamthi.model.ThongTinChamBai;

import java.util.ArrayList;
import java.util.Calendar;

public class ThongTinChamBaiActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private SwipeMenuListView listView;
    private Button them, home;
    private ChamBaiDao chamBaiDao;
    private ArrayList<ThongTinChamBai> list = new ArrayList<>();
    private ThongTinChamAdapter adapter;
    private ArrayList<MonHoc> listMH = new ArrayList<>();
    private MonHocDao monHocDao;
    private PhieuChamDao phieuChamDao;
    private ArrayList<PhieuChamBai> listPCB = new ArrayList<>();
    private ShowDialog showDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_cham_bai);
        setTitle("THÔNG TIN CHẤM BÀI");
        showDialog = new ShowDialog(this);

        //Tham chiếu id
        listView = findViewById(R.id.lvListCB);
        them = findViewById(R.id.btnThemCB);
        home = findViewById(R.id.btnHomeCB);
        chamBaiDao = new ChamBaiDao(this);
        list = chamBaiDao.getAll();
        adapter = new ThongTinChamAdapter(this, list);
        listView.setAdapter(adapter);

        //Lây list môn học để set vào spinner
        monHocDao = new MonHocDao(this);
        listMH = monHocDao.getAll();

        //Lấy list phiếu
        phieuChamDao = new PhieuChamDao(this);
        listPCB = phieuChamDao.getAll();

        //Quẹt icon xóa, sửa
        if (!MainActivity.isAdmin) {
            them.setVisibility(View.GONE);
        } else {
            swipeLayout();
        }
        //Khi click button thêm
        them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themLayout();
            }
        });
        //Khi click button home
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.in, R.anim.out);

            }
        });

        //Lọc theo tìm kiếm
        listView.setTextFilterEnabled(true);
        TextInputEditText edSeach = (TextInputEditText) findViewById(R.id.edtSearchCB);
        edSeach.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int
                    count) {
                System.out.println("Text [" + s + "] - Start [" + start + "] - Before [" + before + "] - Count [" + count + "]");
                if (count < before) {
                    adapter.resetData();
                }
                adapter.getFilter().filter(s.toString());
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

    private void themLayout() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ThongTinChamBaiActivity.this);
        LayoutInflater inflater = ((Activity) ThongTinChamBaiActivity.this).getLayoutInflater();
        View view = inflater.inflate(R.layout.thongtinchambai, null);
        final TextView title = view.findViewById(R.id.titleTT);
        final Spinner soPhieu = view.findViewById(R.id.spSoPhieu);
        final Spinner maMon = view.findViewById(R.id.spMaMon);
        final TextInputEditText soBai = view.findViewById(R.id.edtSoBai);
        final ImageView maP = view.findViewById(R.id.ivThemSoPhieu);
        final ImageView maMH = view.findViewById(R.id.ivThemMonHoc);
        Button sua = view.findViewById(R.id.btnTT);
        Button huy = view.findViewById(R.id.btnHuyTT);


        //Đổ list spinner giáo viên
        final ArrayAdapter spPhieu = new ArrayAdapter(ThongTinChamBaiActivity.this, R.layout.onespinner, listPCB);
        soPhieu.setAdapter(spPhieu);

        //Đổ list môn học
        final ArrayAdapter spMonHoc = new ArrayAdapter(ThongTinChamBaiActivity.this, R.layout.onespinner, listMH);
        maMon.setAdapter(spMonHoc);

        //Thêm phiếu mới
        maP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ThongTinChamBaiActivity.this);
                LayoutInflater inflater = ((Activity) ThongTinChamBaiActivity.this).getLayoutInflater();
                View view2 = inflater.inflate(R.layout.phieuchamlayout, null);
                final TextView title = view2.findViewById(R.id.titleViewPhieu);
                final TextInputEditText ma = view2.findViewById(R.id.edtMaPhieu);
                final TextInputEditText ngay = view2.findViewById(R.id.edtNgayGiaoPhieu);
                final Spinner maGv = view2.findViewById(R.id.spMaGV);
                final ImageView themMaGV = view2.findViewById(R.id.ivThemGiaoVien);
                Button sua = view2.findViewById(R.id.btnThemPhieu);
                Button huy = view2.findViewById(R.id.btnHuyPhieu);

                themMaGV.setVisibility(View.GONE);
                ma.setVisibility(View.GONE);
                ngay.setFocusable(false);
                ngay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar calendar = Calendar.getInstance();
                        int d = calendar.get(Calendar.DAY_OF_MONTH);
                        int m = calendar.get(Calendar.MONTH);
                        int y = calendar.get(Calendar.YEAR);
                        datePickerDialog = new DatePickerDialog(ThongTinChamBaiActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                final String NgayGD = dayOfMonth + "/" + (month + 1) + "/" + year;
                                ngay.setText(NgayGD);
                            }
                        }, y, m, d);
                        datePickerDialog.show();
                    }
                });
                GiaoVienDao giaoVienDao = new GiaoVienDao(ThongTinChamBaiActivity.this);
                final ArrayList<GiaoVien> listGV = giaoVienDao.getAll();

                //Đổ list spinner giáo viên
                final ArrayAdapter sp = new ArrayAdapter(ThongTinChamBaiActivity.this, R.layout.onespinner, listGV);
                maGv.setAdapter(sp);

                //Set tiêu đề
                title.setText("THÊM PHIẾU CHẤM BÀI");
                //Set dữ liệu vào alert
                builder.setView(view2);
                final AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.up_down;
                //Khi nhấn nút Sửa trong alert
                sua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String t = ngay.getText().toString();
                        try {
                            if (t.isEmpty() || listGV.isEmpty()) {
                                showDialog.show("Các trường không được để trống!");
                            } else {
                                GiaoVien gv = (GiaoVien) maGv.getSelectedItem();
                                String s = gv.getMaGv();
                                PhieuChamBai phieuCham1 = new PhieuChamBai(0, t, s);

                                if (phieuChamDao.them(phieuCham1) == true) {
                                    showDialog.show("Thêm thành công!");
                                    listPCB.clear();
                                    listPCB.addAll(phieuChamDao.getAll());
                                    spPhieu.notifyDataSetChanged();
                                    alertDialog.dismiss();
                                } else {
                                    showDialog.show("Số phiếu đã tồn tại. Vui lòng chỉnh sửa phiếu nếu muốn thêm số bài!");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        //Thêm môn học mới
        maMH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ThongTinChamBaiActivity.this);
                LayoutInflater inflater = ((Activity) ThongTinChamBaiActivity.this).getLayoutInflater();
                View view2 = inflater.inflate(R.layout.themlayout, null);
                final TextView title = view2.findViewById(R.id.titleView);
                final TextInputEditText ma = view2.findViewById(R.id.edtCot1);
                final TextInputEditText ten = view2.findViewById(R.id.edtCot2);
                final TextInputEditText sdt = view2.findViewById(R.id.edtCot3);
                Button sua = view2.findViewById(R.id.btnThem);
                Button huy = view2.findViewById(R.id.btnHuy);

                ma.setHint("Nhập mã môn học");
                ten.setHint("Nhập tên môn học");
                sdt.setHint("Nhập chi phí môn học");
                sdt.setInputType(InputType.TYPE_CLASS_NUMBER);
                //Set tiêu đề
                title.setText("THÊM MÔN HỌC");
                //Set dữ liệu vào alert
                sua.setText("THÊM");
                builder.setView(view2);
                final AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.up_down;
                //Khi nhấn nút Sửa trong alert
                sua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String m = ma.getText().toString();
                        String t = ten.getText().toString();
                        String s = sdt.getText().toString();
                        try {
                            if (t.isEmpty() || s.isEmpty() || m.isEmpty()) {
                                showDialog.show("Các trường không được để trống!");
                            } else {
                                MonHoc monHoc1 = new MonHoc(m, t, Integer.parseInt(s));

                                if (monHocDao.them(monHoc1) == true) {
                                    showDialog.show("Thêm thành công!");
                                    listMH.clear();
                                    listMH.addAll(monHocDao.getAll());
                                    spMonHoc.notifyDataSetChanged();
                                    alertDialog.dismiss();
                                } else {
                                    showDialog.show("Thêm thất bại!");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        //Set dữ liệu vào alert
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.up_down;
        //Khi nhấn nút Sửa trong alert
        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String soBaiOne = soBai.getText().toString();
                try {
                    if (listPCB.isEmpty() || listMH.isEmpty() || soBaiOne.isEmpty()) {
                        showDialog.show("Các trường không được để trống!");
                    } else {
                        PhieuChamBai phieuChamBai = (PhieuChamBai) soPhieu.getSelectedItem();
                        int soPhieuOne = phieuChamBai.getSoPhieu();

                        MonHoc monHoc = (MonHoc) maMon.getSelectedItem();
                        String maMonOne = monHoc.getMaMon();

                        ThongTinChamBai phieuCham1 = new ThongTinChamBai(soPhieuOne, maMonOne, Integer.parseInt(soBaiOne));

                        if (chamBaiDao.them(phieuCham1) == true) {
                            showDialog.show("Thêm thành công!");
                            list.clear();
                            list.addAll(chamBaiDao.getAll());
                            adapter.notifyDataSetChanged();
                            alertDialog.dismiss();
                        } else {
                            showDialog.show("Số phiếu chấm bài đã tồn tại!" +
                                    " Nếu muốn thêm số bài, vui lòng chỉnh sửa trực tiếp số bài thuộc phiếu đó!");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void swipeLayout() {
        //Thanh Swipe
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem editItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                editItem.setBackground(R.color.edit);
                // set item width
                editItem.setWidth(170);
                // set item title font color
                editItem.setIcon(R.drawable.ic_baseline_edit_24);
                // add to menu
                menu.addMenuItem(editItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(R.color.delete);
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_baseline_delete_24);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                //lấy vị trí
                final ThongTinChamBai phieuCham = list.get(position);
                switch (index) {
                    case 0:
                        final AlertDialog.Builder builder = new AlertDialog.Builder(ThongTinChamBaiActivity.this);
                        LayoutInflater inflater = ((Activity) ThongTinChamBaiActivity.this).getLayoutInflater();
                        View view = inflater.inflate(R.layout.thongtinchambai, null);
                        final TextView title = view.findViewById(R.id.titleTT);
                        final Spinner soPhieu = view.findViewById(R.id.spSoPhieu);
                        final Spinner maMon = view.findViewById(R.id.spMaMon);
                        final TextInputEditText soBai = view.findViewById(R.id.edtSoBai);
                        Button sua = view.findViewById(R.id.btnTT);
                        Button huy = view.findViewById(R.id.btnHuyTT);

                        //Set tiêu đề
                        title.setText("SỬA THÔNG TIN CHẤM BÀI");
                        soPhieu.setFocusable(false);
                        sua.setText("SỬA");
                        builder.setView(view);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.getWindow().getAttributes().windowAnimations = R.style.up_down;
                        //Đổ list spinner giáo viên
                        final ArrayAdapter spPhieu = new ArrayAdapter(ThongTinChamBaiActivity.this, R.layout.onespinner, listPCB);
                        soPhieu.setAdapter(spPhieu);

                        //Đổ list môn học
                        final ArrayAdapter spMonHoc = new ArrayAdapter(ThongTinChamBaiActivity.this, R.layout.onespinner, listMH);
                        maMon.setAdapter(spMonHoc);

                        //Set đúng vị trí của số phiếu và môn học
                        String soP = listPCB.get(position).getMaGv();
                        for (int i = 0; i < listPCB.size(); i++) {
                            if (soP.matches(listPCB.get(i).getMaGv())) {
                                soPhieu.setSelection(i);
                            }
                        }

                        String maM = listMH.get(position).getMaMon();
                        for (int i = 0; i < listMH.size(); i++) {
                            if (maM.matches(listMH.get(i).getMaMon())) {
                                maMon.setSelection(i);
                            }
                        }
                        TextInputEditText sobai = view.findViewById(R.id.edtSoBai);
                        sobai.setText(phieuCham.getSoBai() + "");

                        //Khi nhấn nút Sửa trong alert
                        sua.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PhieuChamBai phieuChamBai = (PhieuChamBai) soPhieu.getSelectedItem();
                                int soPhieuOne = phieuChamBai.getSoPhieu();

                                MonHoc monHoc = (MonHoc) maMon.getSelectedItem();
                                String maMonOne = monHoc.getMaMon();

                                int soBaiOne = Integer.parseInt(soBai.getText().toString());

                                try {
                                    if (String.valueOf(soPhieuOne).isEmpty() || maMonOne.isEmpty() || String.valueOf(soBaiOne).isEmpty()) {
                                        showDialog.show("Các trường không được để trống!");

                                    } else {
                                        ThongTinChamBai phieuCham1 = new ThongTinChamBai(soPhieuOne, maMonOne, soBaiOne);

                                        if (chamBaiDao.sua(phieuCham1) == true) {
                                            showDialog.show("Sửa thành công!");
                                            list.clear();
                                            list.addAll(chamBaiDao.getAll());
                                            adapter.notifyDataSetChanged();
                                            alertDialog.dismiss();
                                        } else {
                                            showDialog.show("Sửa thất bại!");
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                        huy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });

                        alertDialog.show();

                        break;
                    //xóa
                    case 1:

                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(ThongTinChamBaiActivity.this);
                        builder2.setTitle("Cảnh báo");
                        builder2.setMessage("Bạn chắc chắn xóa không?");
                        builder2.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //code yes để xóa
                                if (chamBaiDao.xoa(phieuCham.getSoPhieu()) == true) {
                                    showDialog.show("Xóa thành công!");
                                    list.clear();
                                    list.addAll(chamBaiDao.getAll());
                                    adapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                } else {
                                    showDialog.show("Xóa thất bại!");
                                }

                            }
                        });
                        builder2.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        final AlertDialog dialog = builder2.create();
                        dialog.getWindow().getAttributes().windowAnimations = R.style.up_down;
                        dialog.show();

                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in, R.anim.out);
        finish();
    }
}