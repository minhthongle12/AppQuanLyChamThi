package com.duan.quanlychamthi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import com.google.android.material.textfield.TextInputEditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.duan.quanlychamthi.adapter.MonHocAdapter;
import com.duan.quanlychamthi.database.MonHocDao;
import com.duan.quanlychamthi.model.MonHoc;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class MonHocActivity extends AppCompatActivity {

    private SwipeMenuListView listView;
    private Button them, home;
    private MonHocDao monhocDao;
    ArrayList<MonHoc> list = new ArrayList<>();
    private MonHocAdapter adapter;
    private ShowDialog showDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_hoc);
        setTitle("MÔN HỌC");
        showDialog = new ShowDialog(this);
        //Tham chiếu id
        listView = findViewById(R.id.lvListMH);
        them = findViewById(R.id.btnThemMH);
        home = findViewById(R.id.btnHomeMH);
        monhocDao = new MonHocDao(this);
        list = monhocDao.getAll();
        adapter = new MonHocAdapter(this, list);
        listView.setAdapter(adapter);

        if (!MainActivity.isAdmin){
            them.setVisibility(View.GONE);
        }
        else {
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
        TextInputEditText edSeach = findViewById(R.id.edtSearchMH);
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(MonHocActivity.this);
        LayoutInflater inflater = ((Activity) MonHocActivity.this).getLayoutInflater();
        View view = inflater.inflate(R.layout.themlayout, null);
        final TextView title = view.findViewById(R.id.titleView);
        final TextInputEditText ma = view.findViewById(R.id.edtCot1);
        final TextInputEditText ten = view.findViewById(R.id.edtCot2);
        final TextInputEditText sdt = view.findViewById(R.id.edtCot3);
        Button sua = view.findViewById(R.id.btnThem);
        Button huy = view.findViewById(R.id.btnHuy);
        TextInputLayout til1 = view.findViewById(R.id.tilMa);
        TextInputLayout til2 = view.findViewById(R.id.tilTen);
        TextInputLayout til3 = view.findViewById(R.id.tilSdt);
        til1.setHint("Nhập mã môn học");
        til2.setHint("Nhập tên môn học");
        til3.setHint("Nhập chi phí môn học");
        sdt.setInputType(InputType.TYPE_CLASS_NUMBER);
        //Set tiêu đề
        title.setText("THÊM MÔN HỌC");
        //Set dữ liệu vào alert
        sua.setText("THÊM");
        builder.setView(view);
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
                    if (m.isEmpty() || t.isEmpty() || s.isEmpty()) {
                        showDialog.show("Các trường không được để trống!");
                    } else if (t.length() < 5) {
                        showDialog.show("Tên môn học phải ít nhất 5 ký tự!");
                    } else {
                        MonHoc monHoc1 = new MonHoc(m, t, Integer.parseInt(s));
                        if (monhocDao.them(monHoc1) == true) {
                            showDialog.show("Thêm thành công!");
                            list.clear();
                            list.addAll(monhocDao.getAll());
                            adapter.notifyDataSetChanged();
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
                final MonHoc monHoc = list.get(position);
                switch (index) {
                    case 0:
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MonHocActivity.this);
                        LayoutInflater inflater = ((Activity) MonHocActivity.this).getLayoutInflater();
                        View view = inflater.inflate(R.layout.themlayout, null);
                        final TextView title = view.findViewById(R.id.titleView);
                        final TextInputEditText ma = view.findViewById(R.id.edtCot1);
                        final TextInputEditText ten = view.findViewById(R.id.edtCot2);
                        final TextInputEditText sdt = view.findViewById(R.id.edtCot3);
                        Button sua = view.findViewById(R.id.btnThem);
                        Button huy = view.findViewById(R.id.btnHuy);
                        TextInputLayout til1 = view.findViewById(R.id.tilMa);
                        TextInputLayout til2 = view.findViewById(R.id.tilTen);
                        TextInputLayout til3 = view.findViewById(R.id.tilSdt);
                        til1.setHint("Nhập mã môn học");
                        til2.setHint("Nhập tên môn học");
                        til3.setHint("Nhập chi phí môn học");
                        //Set tiêu đề
                        title.setText("CHỈNH SỬA MÔN HỌC");
                        ma.setFocusable(false);
                        //Set dữ liệu vào alert
                        ma.setText(monHoc.getMaMon());
                        ten.setText(monHoc.getTenMon());
                        sdt.setText(monHoc.getChiPhiMon() + "");
                        sdt.setInputType(InputType.TYPE_CLASS_NUMBER);
                        sua.setText("SỬA");
                        builder.setView(view);
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
                                    if (m.isEmpty() || t.isEmpty() || s.isEmpty()) {
                                        showDialog.show("Các trường không được để trống!");
                                    } else if (t.length() < 5) {
                                        showDialog.show("Tên môn học phải ít nhất 5 ký tự!");
                                    } else {
                                        MonHoc monHoc1 = new MonHoc(m, t, Integer.parseInt(s));

                                        if (monhocDao.sua(monHoc1) == true) {
                                            showDialog.show("Sửa thành công!");
                                            list.clear();
                                            list.addAll(monhocDao.getAll());
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

                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(MonHocActivity.this);
                        builder2.setTitle("Cảnh báo");
                        builder2.setMessage("Khi bạn xóa môn học, thông tin chấm bài thuộc môn học cũng sẽ bị xóa theo. Bạn chắc chắn xóa không?");
                        builder2.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //code yes để xóa
                                if (monhocDao.xoa(monHoc.getMaMon()) == true) {
                                    showDialog.show("Xóa thành công!");
                                    list.clear();
                                    list.addAll(monhocDao.getAll());
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