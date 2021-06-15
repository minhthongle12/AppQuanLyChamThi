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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.duan.quanlychamthi.adapter.CameraAdapter;
import com.duan.quanlychamthi.database.ImageDao;
import com.duan.quanlychamthi.model.GiaoVien;
import com.duan.quanlychamthi.model.Image;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CameraActivity extends AppCompatActivity {
    private ImageView anh, chup, file, xoa;
    private String[] cameraPermission;
    private String[] storagePermission;
    Uri image_uri;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;
    private Animation blink;
    private ImageDao imageDao;
    private Button home;
    private ShowDialog showDialog;
    SwipeMenuListView listView;
    private ArrayList<Image> list = new ArrayList<>();
    CameraAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        showDialog = new ShowDialog(this);
        listView = findViewById(R.id.lvListImage);
        //Tiêu đề
        setTitle("CAMERA");
        //Tham chiếu id
        init();

        //Tự set hình lên nếu đã có
        list = imageDao.getALl();
        adapter = new CameraAdapter(this, list);
        listView.setAdapter(adapter);

        //Ấn home
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.in, R.anim.out);
            }
        });

        if (!MainActivity.isAdmin){
            chup.setVisibility(View.GONE);
            file.setVisibility(View.GONE);
        }
        else {
            swipeLayout();
        }

        //Sự kiện khi click vào
        chup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chup.startAnimation(blink);
                if (!checkCameraPermission()) {
                    requestCameraPermission();
                } else {
                    pickFromCamera();
                }
            }
        });

        //Thư viện
        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file.startAnimation(blink);
                if (!checkStoragePermission()) {
                    requestStoragetPermission();
                } else {
                    pickFromGallery();
                }
            }
        });

        xoa.setVisibility(View.GONE);


        //Khai báo xin quyền
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //Khi click vào list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posision, long l) {
                Image image = list.get(posision);
                Picasso.with(CameraActivity.this).load(image.getImage()).into(anh);

            }
        });


    }

    private void swipeLayout() {
        //Thanh Swipe
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
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
                final Image image = list.get(position);
                switch (index) {
                    //xóa
                    case 0:

                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(CameraActivity.this);
                        builder2.setTitle("Cảnh báo");
                        builder2.setMessage("Bạn chắc chắn muốn xóa Hình "+image.getId());
                        builder2.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //code yes để xóa
                                if (imageDao.xoa(image.getId())) {
                                    showDialog.show("Xóa thành công!");
                                    list.clear();
                                    list.addAll(imageDao.getALl());
                                    anh.setImageResource(R.drawable.gallery);
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

    private void init() {
        anh = findViewById(R.id.ivImage);
        chup = findViewById(R.id.ivChupAnh);
        file = findViewById(R.id.ivFile);
        xoa = findViewById(R.id.ivXoa);
        blink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.faded);
        imageDao = new ImageDao(this);
        home = findViewById(R.id.btnHome);

    }


    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragetPermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(CameraActivity.this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(CameraActivity.this,
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
                Picasso.with(this).load(image_uri).into(anh);
                Image image = new Image(0, image_uri.toString());
                imageDao.them(image);
                list.clear();
                list.addAll(imageDao.getALl());
                adapter.notifyDataSetChanged();
                showDialog.show("Up ảnh thành công!");
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                Picasso.with(this).load(image_uri).into(anh);
                Image image = new Image(0, image_uri.toString());
                imageDao.them(image);
                list.clear();
                list.addAll(imageDao.getALl());
                adapter.notifyDataSetChanged();
                showDialog.show("Up ảnh thành công!");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in, R.anim.out);
        finish();
    }

}