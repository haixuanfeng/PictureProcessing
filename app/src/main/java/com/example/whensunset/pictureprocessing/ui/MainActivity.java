package com.example.whensunset.pictureprocessing.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.whensunset.pictureprocessing.Utils.Utils;
import com.example.whensunset.pictureprocessing.base.BaseActivity;
import com.example.whensunset.pictureprocessing.viewModel.MainActivityVM;
import com.example.whensunset.pictureprocessinggraduationdesign.R;

import java.io.File;

import static android.support.v4.content.FileProvider.getUriForFile;

public class MainActivity extends BaseActivity {



    private MainActivityVM mMainActivityVM;
    private FloatingActionButton btn;
    String file;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_ALBUM_LOAD = 2;

    private int loginRequestCode =1;
    private boolean permit = true;

    static  {
        System.loadLibrary("opencv_java3");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.floatingActionButton);


        File getfile = new File(Environment.getExternalStorageDirectory(), Utils.getOutputMediaFile());
        if (!getfile.exists()){
            getfile.mkdirs();
        }

//        final boolean[] isAllowedPermission = {false};
//        RxPermissions rxPermissions = new RxPermissions(this);
//        rxPermissions.requestEach(
//                Manifest.permission.READ_EXTERNAL_STORAGE ,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .filter(permission -> permission.granted)
//                .subscribe(permission -> {
//                    if (!isAllowedPermission[0]) {
////                        mMainActivityBinding = DataBindingUtil.setContentView(MainActivity.this , R.layout.activity_main);
////                        mMainActivityVM = getViewModel(MainActivityVM.class);
////                        mMainActivityBinding.setViewModel(mMainActivityVM);
//
//                        registeredViewModelFiledsObserver();
//                    }
//                    isAllowedPermission[0] = true;
//                });


        String[] strings={"android.permission.CAMERA",
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.INTERNET"};
        for (int i = 0;i<strings.length;i++){
            if (ContextCompat.checkSelfPermission(MainActivity.this,strings[i])== PackageManager.PERMISSION_DENIED)
                permit = false;
        }
        if(!permit)
        {
            ActivityCompat.requestPermissions(this,strings,loginRequestCode);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopueWindow();
            }
        });


    }

    @Override
    protected void registeredViewModelFiledsObserver() {
//        // 监听bar上面目录切换时候的toast显示
//        showToast(mMainActivityVM.getDirectorySpinnerItemManagerVM());
//
//        // 监听列表中item的点击事件
//        initListener(mMainActivityVM.getPictureItemManagerVM() , (observable, i) -> {
//            String imageUri = ObserverParamMap.staticGetValue(observable , PictureItemManagerVM_mImageUri);
//            Intent intent = new Intent(MainActivity.this , PictureProcessingActivity.class);
//            intent.putExtra("imageUri" , imageUri);
//            MainActivity.this.startActivity(intent);
//
//            MyLog.d(TAG, "onPropertyChanged", "状态:imageUri:", "监听列表中item的点击事件" , imageUri);
//        } , CLICK_ITEM);
//
//        // 监听bar上面的目录切换事件
//        initListener(mMainActivityVM.getDirectorySpinnerItemManagerVM() , (observable, i) -> {
//            String directoryName = ObserverParamMap.staticGetValue(observable, DirectorySpinnerItemManagerVM_directoryName);
//            mMainActivityVM.getPictureItemManagerVM().freshPictureList(directoryName);
//            MyLog.d(TAG, "onPropertyChanged", "状态:directoryName:", "监听bar上面的目录切换事件" , directoryName);
//        } , CLICK_ITEM);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainActivityVM.onCleared();
    }

    private void showPopueWindow(){
        View popView = View.inflate(this,R.layout.popupwindow_camera_need,null);
        Button bt_album = (Button) popView.findViewById(R.id.btn_pop_album);
        Button bt_camera = (Button) popView.findViewById(R.id.btn_pop_camera);
        Button bt_cancle = (Button) popView.findViewById(R.id.btn_pop_cancel);
        //获取屏幕宽高
        int weight = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels*1/3;

        final PopupWindow popupWindow = new PopupWindow(popView,weight,height);
//        popupWindow.setAnimationStyle(R.style.anim_popup_dir);
        popupWindow.setFocusable(true);
        //点击外部popueWindow消失
        popupWindow.setOutsideTouchable(true);

        bt_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, REQUEST_ALBUM_LOAD);
                popupWindow.dismiss();

            }
        });
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeCamera();
                popupWindow.dismiss();

            }
        });
        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });
        //popupWindow消失屏幕变为不透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        //popupWindow出现屏幕变为半透明
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM,0,50);

    }

    private void takeCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Utils.getOutputMediaFile()+"/"+ Utils.getCurrentTime()+".jpg";

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            if (Build.VERSION.SDK_INT < 24) {
                Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),file));
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }else {
                Uri imageUri = getUriForFile(this,getPackageName()+".fileprovider",new File(Environment.getExternalStorageDirectory(),file));
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_IMAGE_CAPTURE){
                imageProcess(Environment.getExternalStorageDirectory()+file);
            }else if (requestCode == REQUEST_ALBUM_LOAD){
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                final String picturePath = cursor.getString(columnIndex);
                imageProcess(picturePath);
            }
        }

    }

    public void imageProcess(String imageUri){
        Intent intent = new Intent(MainActivity.this , PictureProcessingActivity.class);
            intent.putExtra("imageUri" , imageUri);
            MainActivity.this.startActivity(intent);
    }
}
