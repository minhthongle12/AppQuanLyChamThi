package com.duan.quanlychamthi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.duan.quanlychamthi.adapter.PhieuChamAdapter;
import com.duan.quanlychamthi.database.GiaoVienDao;
import com.duan.quanlychamthi.database.PhieuChamDao;
import com.duan.quanlychamthi.model.GiaoVien;
import com.duan.quanlychamthi.model.PhieuChamBai;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class PhieuChamBaiActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private SwipeMenuListView listView;
    private Button them, home;
    private PhieuChamDao phieuchamDao;
    private ArrayList<PhieuChamBai> list = new ArrayList<>();
    private PhieuChamAdapter adapter;

    private ArrayList<GiaoVien> listGV = new ArrayList<>();
    private GiaoVienDao giaoVienDao;
    private TextInputEditText search;
    private ShowDialog showDialog;
    private String[] cameraPermission;
    private String[] storagePermission;
    Uri image_uri;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phieu_cham_bai);
        setTitle("PHIẾU CHẤM BÀI");
        showDialog = new ShowDialog(this);

        //Tham chiếu id
        listView = findViewById(R.id.lvListPC);
        them = findViewById(R.id.btnThemPC);
        home = findViewById(R.id.btnHomePC);
        phieuchamDao = new PhieuChamDao(this);
        list = phieuchamDao.getAll();
        adapter = new PhieuChamAdapter(this, list);
        listView.setAdapter(adapter);

        //Lây list giáo viên để set vào spinner
        giaoVienDao = new GiaoVienDao(this);
        listGV = giaoVienDao.getAll();

        //Quẹt icon xóa, sửa

        if (!MainActivity.isAdmin){
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
        TextInputEditText edSeach = (TextInputEditText) findViewById(R.id.edtSearchPCB);
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(PhieuChamBaiActivity.this);
        LayoutInflater inflater = ((Activity) PhieuChamBaiActivity.this).getLayoutInflater();
        View view = inflater.inflate(R.layout.phieuchamlayout, null);
        final TextView title = view.findViewById(R.id.titleViewPhieu);
        final TextInputEditText ma = view.findViewById(R.id.edtMaPhieu);
        final TextInputEditText ngay = view.findViewById(R.id.edtNgayGiaoPhieu);
        final Spinner maGv = view.findViewById(R.id.spMaGV);
        final ImageView themMaGV = view.findViewById(R.id.ivThemGiaoVien);
        Button sua = view.findViewById(R.id.btnThemPhieu);
        Button huy = view.findViewById(R.id.btnHuyPhieu);
        TextInputLayout til1 = view.findViewById(R.id.lil1);
        til1.setHint("Ngày giao bài");
        ma.setVisibility(View.GONE);
        ngay.setFocusable(false);
        ngay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int d = calendar.get(Calendar.DAY_OF_MONTH);
                int m = calendar.get(Calendar.MONTH);
                int y = calendar.get(Calendar.YEAR);
                datePickerDialog = new DatePickerDialog(PhieuChamBaiActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        final String NgayGD = dayOfMonth + "/" + (month + 1) + "/" + year;
                        ngay.setText(NgayGD);
                    }
                }, y, m, d);
                datePickerDialog.show();
            }
        });

        //Đổ list spinner giáo viên
        final ArrayAdapter sp = new ArrayAdapter(PhieuChamBaiActivity.this, R.layout.onespinner, listGV);
        maGv.setAdapter(sp);

        //Khi ấn nút thêm giáo viên, diaog sẽ hiện ra
        themMaGV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(PhieuChamBaiActivity.this);
                LayoutInflater inflater = ((Activity) PhieuChamBaiActivity.this).getLayoutInflater();
                View view2 = inflater.inflate(R.layout.themlayout, null);
                final TextView title = view2.findViewById(R.id.titleView);
                final TextInputEditText ma = view2.findViewById(R.id.edtCot1);
                final TextInputEditText ten = view2.findViewById(R.id.edtCot2);
                final TextInputEditText sdt = view2.findViewById(R.id.edtCot3);
                Button sua = view2.findViewById(R.id.btnThem);
                Button huy = view2.findViewById(R.id.btnHuy);
                imageView = view2.findViewById(R.id.ivThemHinhGV);
                imageView.setVisibility(View.VISIBLE);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showImagePickDialog();
                    }
                });
                TextInputLayout til1 = view2.findViewById(R.id.tilMa);
                TextInputLayout til2 = view2.findViewById(R.id.tilTen);
                TextInputLayout til3 = view2.findViewById(R.id.tilSdt);
                til1.setHint("Nhập mã giáo viên");
                til2.setHint("Nhập tên giáo viên");
                til3.setHint("Nhập số điện thoại");
                sdt.setInputType(InputType.TYPE_CLASS_NUMBER);
                //Set tiêu đề
                title.setText("THÊM GIÁO VIÊN");
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
                            if (image_uri.toString().isEmpty()) {
                                showDialog.show("Vui lòng thêm hình cho giáo viên!");
                            } else if (m.isEmpty() || t.isEmpty() || s.isEmpty()) {
                                showDialog.show("Các trường không được để trống!");
                            } else if (t.length() < 5) {
                                showDialog.show("Tên giáo viên phải ít nhất 5 ký tự!");
                            } else if (!s.matches("^{0,84}[0-9]{9,10}")) {
                                showDialog.show("Số điện thoại không chính xác!");
                            } else {
                                GiaoVien giaoVien1 = new GiaoVien(m, t, s, image_uri.toString());
                                if (giaoVienDao.them(giaoVien1) == true) {
                                    showDialog.show("Thêm thành công!");
                                    listGV.clear();
                                    listGV.addAll(giaoVienDao.getAll());
                                    sp.notifyDataSetChanged();
                                    alertDialog.dismiss();
                                } else {
                                    showDialog.show("Mã giáo viên đã tồn tại!");
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

        //Set tiêu đề
        title.setText("THÊM PHIẾU CHẤM BÀI");
        //Set dữ liệu vào alert
//        sua.setText("THÊM");
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.up_down;
        //Khi nhấn nút Sửa trong alert
        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = ngay.getText().toString();
                try {
                    if (t.isEmpty() || listGV.size() == 0) {
                        showDialog.show("Các trường không được để trống!");
                    } else {
                        GiaoVien gv = (GiaoVien) maGv.getSelectedItem();
                        String s = gv.getMaGv();
                        PhieuChamBai phieuCham1 = new PhieuChamBai(0, t, s);
                        if (phieuchamDao.them(phieuCham1) == true) {
                            showDialog.show("Thêm thành công!");
                            list.clear();
                            list.addAll(phieuchamDao.getAll());
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
                final PhieuChamBai phieuCham = list.get(position);
                switch (index) {
                    case 0:
                        final AlertDialog.Builder builder = new AlertDialog.Builder(PhieuChamBaiActivity.this);
                        LayoutInflater inflater = ((Activity) PhieuChamBaiActivity.this).getLayoutInflater();
                        View view = inflater.inflate(R.layout.phieuchamlayout, null);
//                        final TextView title = view.findViewById(R.id.titleView);
                        final TextInputEditText sMa = view.findViewById(R.id.edtMaPhieu);
                        final TextInputEditText sNgay = view.findViewById(R.id.edtNgayGiaoPhieu);
                        final Spinner sMaGV = view.findViewById(R.id.spMaGV);
                        Button sua = view.findViewById(R.id.btnThemPhieu);
                        Button huy = view.findViewById(R.id.btnHuyPhieu);
                        TextInputLayout til1 = view.findViewById(R.id.lil1);
                        til1.setHint("Ngày giao bài");
                        //Set tiêu đề
//                        title.setText("CHỈNH SỬA PHIẾU CHẤM BÀI");
                        sMa.setFocusable(false);
                        //Set dữ liệu vào alert
                        sMa.setText(phieuCham.getSoPhieu() + "");
                        sNgay.setText(phieuCham.getNgayPhieu());
//                        sdt.set(phieuCham.getMaGv()+"");
                        sua.setText("SỬA");
                        builder.setView(view);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.getWindow().getAttributes().windowAnimations = R.style.up_down;
                        //Đổ list spinner giáo viên
                        final ArrayAdapter sp = new ArrayAdapter(PhieuChamBaiActivity.this, R.layout.onespinner, listGV);
                        sMaGV.setAdapter(sp);

                        //Set đúng vị trí
                        String maGV = listGV.get(position).getMaGv();
                        for (int i = 0; i < listGV.size(); i++) {
                            if (maGV.matches(listGV.get(i).getMaGv())) {
                                sMaGV.setSelection(i);
                            }
                        }

                        //Chọn ngày
                        sNgay.setFocusable(false);
                        sNgay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Calendar calendar = Calendar.getInstance();
                                int d = calendar.get(Calendar.DAY_OF_MONTH);
                                int m = calendar.get(Calendar.MONTH);
                                int y = calendar.get(Calendar.YEAR);
                                datePickerDialog = new DatePickerDialog(PhieuChamBaiActivity.this, new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        final String NgayGD = dayOfMonth + "/" + (month + 1) + "/" + year;
                                        sNgay.setText(NgayGD);
                                    }
                                }, y, m, d);
                                datePickerDialog.show();
                            }
                        });


                        //Khi nhấn nút Sửa trong alert
                        sua.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String m = sMa.getText().toString();
                                String t = sNgay.getText().toString();
                                GiaoVien gv = (GiaoVien) sMaGV.getSelectedItem();
                                String s = gv.getMaGv();
                                if (m.isEmpty() || t.isEmpty() || s.isEmpty()) {
                                    showDialog.show("Các trường không được để trống!");
                                } else {
                                    PhieuChamBai phieuCham1 = new PhieuChamBai(Integer.parseInt(m), t, s);

                                    if (phieuchamDao.sua(phieuCham1) == true) {
                                        showDialog.show("Sửa thành công!");
                                        list.clear();
                                        list.addAll(phieuchamDao.getAll());
                                        adapter.notifyDataSetChanged();
                                        alertDialog.dismiss();
                                    } else {
                                        showDialog.show("Sửa thất bại!");
                                    }
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

                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(PhieuChamBaiActivity.this);
                        builder2.setTitle("Cảnh báo");
                        builder2.setMessage("Nếu bạn xóa phiếu chấm bài, tất cả thông tin chấm bài thuộc mã phiếu này cũng sẽ bị xóa. Bạn có chắc chắn muốn xóa?");
                        builder2.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //code yes để xóa
                                if (phieuchamDao.xoa(phieuCham.getSoPhieu()) == true) {
                                    showDialog.show("Xóa thành công!");
                                    list.clear();
                                    list.addAll(phieuchamDao.getAll());
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

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(PhieuChamBaiActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragetPermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(PhieuChamBaiActivity.this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(PhieuChamBaiActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccept = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccept = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccept && storageAccept) {
                        pickFromCamera();
                    } else {
                        showDialog.show("Không truy cập được vào camera!");
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean writeStorageAccpted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccpted) {
                        pickFromGallery();
                    } else {
                        showDialog.show("Vui lòng bật quyền thư viện");
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_uri = data.getData();
                Picasso.with(this).load(image_uri).into(imageView);
//                Image image = new Image(0, image_uri.toString());
//                imageDao.them(image);
//                list.clear();
//                list.addAll(imageDao.getALl());
//                adapter.notifyDataSetChanged();
//                showDialog.show("Up ảnh thành công!");
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                Picasso.with(this).load(image_uri).into(imageView);
//                Image image = new Image(0, image_uri.toString());
//                imageDao.them(image);
//                list.clear();
//                list.addAll(imageDao.getALl());
//                adapter.notifyDataSetChanged();
//                showDialog.show("Up ảnh thành công!");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        image_uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    public void showImagePickDialog() {
        String option[] = {"Camera", "Thư viện ảnh", "Xóa ảnh"};

        AlertDialog.Builder builder = new AlertDialog.Builder(PhieuChamBaiActivity.this);
        builder.setTitle("Mời bạn chọn");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                }
                if (which == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragetPermission();
                    } else {
                        pickFromGallery();
                    }
                }
                if (which == 2) {
                    image_uri = Uri.parse("");
                    imageView.setImageResource(R.drawable.gallery);
                }
            }
        });
        builder.create().show();
    }
}