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
        setTitle("TH??NG TIN CH???M B??I");
        showDialog = new ShowDialog(this);

        //Tham chi???u id
        listView = findViewById(R.id.lvListCB);
        them = findViewById(R.id.btnThemCB);
        home = findViewById(R.id.btnHomeCB);
        chamBaiDao = new ChamBaiDao(this);
        list = chamBaiDao.getAll();
        adapter = new ThongTinChamAdapter(this, list);
        listView.setAdapter(adapter);

        //L??y list m??n h???c ????? set v??o spinner
        monHocDao = new MonHocDao(this);
        listMH = monHocDao.getAll();

        //L???y list phi???u
        phieuChamDao = new PhieuChamDao(this);
        listPCB = phieuChamDao.getAll();

        //Qu???t icon x??a, s???a
        if (!MainActivity.isAdmin) {
            them.setVisibility(View.GONE);
        } else {
            swipeLayout();
        }
        //Khi click button th??m
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

        //L???c theo t??m ki???m
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


        //????? list spinner gi??o vi??n
        final ArrayAdapter spPhieu = new ArrayAdapter(ThongTinChamBaiActivity.this, R.layout.onespinner, listPCB);
        soPhieu.setAdapter(spPhieu);

        //????? list m??n h???c
        final ArrayAdapter spMonHoc = new ArrayAdapter(ThongTinChamBaiActivity.this, R.layout.onespinner, listMH);
        maMon.setAdapter(spMonHoc);

        //Th??m phi???u m???i
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

                //????? list spinner gi??o vi??n
                final ArrayAdapter sp = new ArrayAdapter(ThongTinChamBaiActivity.this, R.layout.onespinner, listGV);
                maGv.setAdapter(sp);

                //Set ti??u ?????
                title.setText("TH??M PHI???U CH???M B??I");
                //Set d??? li???u v??o alert
                builder.setView(view2);
                final AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.up_down;
                //Khi nh???n n??t S???a trong alert
                sua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String t = ngay.getText().toString();
                        try {
                            if (t.isEmpty() || listGV.isEmpty()) {
                                showDialog.show("C??c tr?????ng kh??ng ???????c ????? tr???ng!");
                            } else {
                                GiaoVien gv = (GiaoVien) maGv.getSelectedItem();
                                String s = gv.getMaGv();
                                PhieuChamBai phieuCham1 = new PhieuChamBai(0, t, s);

                                if (phieuChamDao.them(phieuCham1) == true) {
                                    showDialog.show("Th??m th??nh c??ng!");
                                    listPCB.clear();
                                    listPCB.addAll(phieuChamDao.getAll());
                                    spPhieu.notifyDataSetChanged();
                                    alertDialog.dismiss();
                                } else {
                                    showDialog.show("S??? phi???u ???? t???n t???i. Vui l??ng ch???nh s???a phi???u n???u mu???n th??m s??? b??i!");
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

        //Th??m m??n h???c m???i
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

                ma.setHint("Nh???p m?? m??n h???c");
                ten.setHint("Nh???p t??n m??n h???c");
                sdt.setHint("Nh???p chi ph?? m??n h???c");
                sdt.setInputType(InputType.TYPE_CLASS_NUMBER);
                //Set ti??u ?????
                title.setText("TH??M M??N H???C");
                //Set d??? li???u v??o alert
                sua.setText("TH??M");
                builder.setView(view2);
                final AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.up_down;
                //Khi nh???n n??t S???a trong alert
                sua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String m = ma.getText().toString();
                        String t = ten.getText().toString();
                        String s = sdt.getText().toString();
                        try {
                            if (t.isEmpty() || s.isEmpty() || m.isEmpty()) {
                                showDialog.show("C??c tr?????ng kh??ng ???????c ????? tr???ng!");
                            } else {
                                MonHoc monHoc1 = new MonHoc(m, t, Integer.parseInt(s));

                                if (monHocDao.them(monHoc1) == true) {
                                    showDialog.show("Th??m th??nh c??ng!");
                                    listMH.clear();
                                    listMH.addAll(monHocDao.getAll());
                                    spMonHoc.notifyDataSetChanged();
                                    alertDialog.dismiss();
                                } else {
                                    showDialog.show("Th??m th???t b???i!");
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

        //Set d??? li???u v??o alert
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.up_down;
        //Khi nh???n n??t S???a trong alert
        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String soBaiOne = soBai.getText().toString();
                try {
                    if (listPCB.isEmpty() || listMH.isEmpty() || soBaiOne.isEmpty()) {
                        showDialog.show("C??c tr?????ng kh??ng ???????c ????? tr???ng!");
                    } else {
                        PhieuChamBai phieuChamBai = (PhieuChamBai) soPhieu.getSelectedItem();
                        int soPhieuOne = phieuChamBai.getSoPhieu();

                        MonHoc monHoc = (MonHoc) maMon.getSelectedItem();
                        String maMonOne = monHoc.getMaMon();

                        ThongTinChamBai phieuCham1 = new ThongTinChamBai(soPhieuOne, maMonOne, Integer.parseInt(soBaiOne));

                        if (chamBaiDao.them(phieuCham1) == true) {
                            showDialog.show("Th??m th??nh c??ng!");
                            list.clear();
                            list.addAll(chamBaiDao.getAll());
                            adapter.notifyDataSetChanged();
                            alertDialog.dismiss();
                        } else {
                            showDialog.show("S??? phi???u ch???m b??i ???? t???n t???i!" +
                                    " N???u mu???n th??m s??? b??i, vui l??ng ch???nh s???a tr???c ti???p s??? b??i thu???c phi???u ????!");
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
                //l???y v??? tr??
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

                        //Set ti??u ?????
                        title.setText("S???A TH??NG TIN CH???M B??I");
                        soPhieu.setFocusable(false);
                        sua.setText("S???A");
                        builder.setView(view);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.getWindow().getAttributes().windowAnimations = R.style.up_down;
                        //????? list spinner gi??o vi??n
                        final ArrayAdapter spPhieu = new ArrayAdapter(ThongTinChamBaiActivity.this, R.layout.onespinner, listPCB);
                        soPhieu.setAdapter(spPhieu);

                        //????? list m??n h???c
                        final ArrayAdapter spMonHoc = new ArrayAdapter(ThongTinChamBaiActivity.this, R.layout.onespinner, listMH);
                        maMon.setAdapter(spMonHoc);

                        //Set ????ng v??? tr?? c???a s??? phi???u v?? m??n h???c
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

                        //Khi nh???n n??t S???a trong alert
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
                                        showDialog.show("C??c tr?????ng kh??ng ???????c ????? tr???ng!");

                                    } else {
                                        ThongTinChamBai phieuCham1 = new ThongTinChamBai(soPhieuOne, maMonOne, soBaiOne);

                                        if (chamBaiDao.sua(phieuCham1) == true) {
                                            showDialog.show("S???a th??nh c??ng!");
                                            list.clear();
                                            list.addAll(chamBaiDao.getAll());
                                            adapter.notifyDataSetChanged();
                                            alertDialog.dismiss();
                                        } else {
                                            showDialog.show("S???a th???t b???i!");
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
                    //x??a
                    case 1:

                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(ThongTinChamBaiActivity.this);
                        builder2.setTitle("C???nh b??o");
                        builder2.setMessage("B???n ch???c ch???n x??a kh??ng?");
                        builder2.setNegativeButton("X??a", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //code yes ????? x??a
                                if (chamBaiDao.xoa(phieuCham.getSoPhieu()) == true) {
                                    showDialog.show("X??a th??nh c??ng!");
                                    list.clear();
                                    list.addAll(chamBaiDao.getAll());
                                    adapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                } else {
                                    showDialog.show("X??a th???t b???i!");
                                }

                            }
                        });
                        builder2.setPositiveButton("Kh??ng", new DialogInterface.OnClickListener() {
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