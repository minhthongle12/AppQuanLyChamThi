package com.duan.quanlychamthi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import com.google.android.material.textfield.TextInputEditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.duan.quanlychamthi.adapter.GiaoVienAdapter;
import com.duan.quanlychamthi.database.GiaoVienDao;
import com.duan.quanlychamthi.model.GiaoVien;
import com.duan.quanlychamthi.model.Image;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GiaoVienActivity extends AppCompatActivity {
    private SwipeMenuListView listView;
    private Button them, home;
    private GiaoVienDao giaoVienDao;
    ArrayList<GiaoVien> list = new ArrayList<>();
    private GiaoVienAdapter adapter;
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
        setContentView(R.layout.activity_giao_vien);
        setTitle("GI??O VI??N");
        showDialog = new ShowDialog(this);

        //Tham chi???u id
        listView = findViewById(R.id.lvListGV);
        them = findViewById(R.id.btnThemGV);
        home = findViewById(R.id.btnHomeGV);
        giaoVienDao = new GiaoVienDao(this);
        list = giaoVienDao.getAll();
        adapter = new GiaoVienAdapter(this, list);
        listView.setAdapter(adapter);
        //Khai b??o xin quy???n
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //???n n???u kh??ng ph???i admin
        if (!MainActivity.isAdmin){
            them.setVisibility(View.GONE);
        }
        else {
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
        TextInputEditText edSeach = (TextInputEditText) findViewById(R.id.edtSearchGV);
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


    public void themLayout() {
        image_uri = null;
        final AlertDialog.Builder builder = new AlertDialog.Builder(GiaoVienActivity.this);
        LayoutInflater inflater = ((Activity) GiaoVienActivity.this).getLayoutInflater();
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
        til1.setHint("Nh???p m?? gi??o vi??n");
        til2.setHint("Nh???p t??n gi??o vi??n");
        til3.setHint("Nh???p s??? ??i???n tho???i");


        //Hi???n h??nh
        imageView = view.findViewById(R.id.ivThemHinhGV);
        imageView.setVisibility(View.VISIBLE);
        sdt.setInputType(InputType.TYPE_CLASS_NUMBER);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickDialog();
            }
        });
        //Set ti??u ?????
        title.setText("TH??M GI??O VI??N");
        //Set d??? li???u v??o alert
        sua.setText("TH??M");
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.up_down;

        //Khi nh???n n??t S???a trong alert
        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String m = ma.getText().toString();
                String t = ten.getText().toString();
                String s = sdt.getText().toString();

                    if (image_uri==null) {
                        showDialog.show("Vui l??ng th??m h??nh cho gi??o vi??n!");
                    } else if (m.isEmpty() || t.isEmpty() || s.isEmpty()) {
                        showDialog.show("C??c tr?????ng kh??ng ???????c ????? tr???ng!");
                    } else if (t.length() < 5) {
                        showDialog.show("T??n gi??o vi??n ph???i ??t nh???t 5 k?? t???!");
                    } else if (!s.matches("^{0,84}[0-9]{9,10}")) {
                        showDialog.show("S??? ??i???n tho???i kh??ng ch??nh x??c!");
                    } else {
                        GiaoVien giaoVien1 = new GiaoVien(m, t, s, image_uri.toString());
                        if (giaoVienDao.them(giaoVien1) == true) {
                            showDialog.show("Th??m th??nh c??ng!");
                            list.clear();
                            list.addAll(giaoVienDao.getAll());
                            adapter.notifyDataSetChanged();
                            alertDialog.dismiss();
                        } else {
                            showDialog.show("M?? gi??o vi??n ???? t???n t???i!");
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
                final GiaoVien giaoVien = list.get(position);
                switch (index) {
                    case 0:
                        final AlertDialog.Builder builder = new AlertDialog.Builder(GiaoVienActivity.this);
                        LayoutInflater inflater = ((Activity) GiaoVienActivity.this).getLayoutInflater();
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
                        til1.setHint("Nh???p m?? gi??o vi??n");
                        til2.setHint("Nh???p t??n gi??o vi??n");
                        til3.setHint("Nh???p s??? ??i???n tho???i");
                        imageView = view.findViewById(R.id.ivThemHinhGV);
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showImagePickDialog();
                            }
                        });
                        //Set ti??u ?????
                        title.setText("CH???NH S???A GI??O VI??N");
                        ma.setFocusable(false);
                        //Set d??? li???u v??o alert
                        ma.setText(giaoVien.getMaGv());
                        ten.setText(giaoVien.getHoTenGv());
                        sdt.setText(giaoVien.getSdtGv());
                        sdt.setInputType(InputType.TYPE_CLASS_NUMBER);
                        sua.setText("S???A");
                        builder.setView(view);
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
                                    if (image_uri.toString().isEmpty()) {
                                        showDialog.show("Vui l??ng th??m h??nh cho gi??o vi??n!");
                                    } else if (m.isEmpty() || t.isEmpty() || s.isEmpty()) {
                                        showDialog.show("C??c tr?????ng kh??ng ???????c ????? tr???ng!");
                                    } else if (t.length() < 5) {
                                        showDialog.show("T??n gi??o vi??n ph???i ??t nh???t 5 k?? t???!");
                                    } else if (!s.matches("^{0,84}[0-9]{9,10}")) {
                                        showDialog.show("S??? ??i???n tho???i kh??ng ch??nh x??c!");
                                    } else {
                                        GiaoVien giaoVien1 = new GiaoVien(m, t, s, image_uri.toString());

                                        if (giaoVienDao.sua(giaoVien1) == true) {
                                            showDialog.show("S???a th??nh c??ng!");
                                            list.clear();
                                            list.addAll(giaoVienDao.getAll());
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

                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(GiaoVienActivity.this);
                        builder2.setTitle("C???nh b??o");
                        builder2.setMessage("Khi b???n x??a gi??o bi??n. T???t c??? b??n phi???u ch???m b??i ch???a gi??o vi??n s??? b??? x??a. B???n ch???c ch???n x??a kh??ng?");
                        builder2.setNegativeButton("X??a", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //code yes ????? x??a
                                if (giaoVienDao.xoa(giaoVien.getMaGv()) == true) {
                                    showDialog.show("X??a th??nh c??ng!");
                                    list.clear();
                                    list.addAll(giaoVienDao.getAll());
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

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(GiaoVienActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragetPermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(GiaoVienActivity.this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(GiaoVienActivity.this,
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
                        showDialog.show("Kh??ng truy c???p ???????c v??o camera!");
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
                        showDialog.show("Vui l??ng b???t quy???n th?? vi???n");
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
//                showDialog.show("Up ???nh th??nh c??ng!");
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                Picasso.with(this).load(image_uri).into(imageView);
//                Image image = new Image(0, image_uri.toString());
//                imageDao.them(image);
//                list.clear();
//                list.addAll(imageDao.getALl());
//                adapter.notifyDataSetChanged();
//                showDialog.show("Up ???nh th??nh c??ng!");
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
        String option[] = {"Camera", "Th?? vi???n ???nh", "X??a ???nh"};

        AlertDialog.Builder builder = new AlertDialog.Builder(GiaoVienActivity.this);
        builder.setTitle("M???i b???n ch???n");
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