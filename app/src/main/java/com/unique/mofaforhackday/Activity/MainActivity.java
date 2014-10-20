package com.unique.mofaforhackday.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.unique.mofaforhackday.R;
import com.unique.mofaforhackday.Utils.ImageCaptureUtils;
import com.unique.mofaforhackday.Utils.L;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

/**
 * Created by ldx at 2014/8/
 */
public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static final boolean DEBUG = true;

    private static final int LOAD_IMAGE = 1;

    private Uri mImageUri;
    File photo;
    private String mCurrentPicturePath;

    private ImageButton mImageButtonPic;
    private ImageButton mImageButtonCam;
    private ImageButton mImageButtonWord;
    private ImageButton mImageButtonSet;
    private ImageButton mImageButtonFeedback;

    private FeedbackAgent agent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //full screen Content View
        initContentView();

        UmengUpdateAgent.update(this);
//        UmengUpdateAgent.setUpdateCheckConfig(false);

        findView();

        startCam();
        imageSelected();

        setOption();
    }

    private void initContentView(){
        // NO_TITLE go away the title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
        setContentView(R.layout.activity_main);
    }

    private void findView(){
        mImageButtonCam = (ImageButton)findViewById(R.id.imageButton_cam);
//        mImageButtonWord = (ImageButton)findViewById(R.id.imageButton_word);
        mImageButtonSet = (ImageButton)findViewById(R.id.imageButton_set);
        mImageButtonPic = (ImageButton)findViewById(R.id.imageButton_pic);
    }

    private void setOption(){
        mImageButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Option.class));
            }
        });
    }

    //init the Camera
    private void startCam(){
        mImageButtonCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = ImageCaptureUtils.createImageFile();
                        mCurrentPicturePath = photoFile.getAbsolutePath();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Toast.makeText(getBaseContext(),"MoFa出了点问题",Toast.LENGTH_SHORT).show();
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, LOAD_IMAGE);
                    }
                }
            }
        });
    }



    private void imageSelected(){
        mImageButtonPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ImageSelectedActivity.class));
            }
        });
    }
    //use Media Camera to load a photo.
//    static Bitmap bitmapFromCam;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode ==Activity.RESULT_OK){
            switch (requestCode){
                case LOAD_IMAGE:
                    Intent intent = new Intent();
                    intent.setClass(this,HandleImageActivity.class);
                    intent.putExtra("Cam", true);
//                    bitmapFromCam = (Bitmap)data.getExtras().get("data");
                    intent.putExtra("tem_pic",mCurrentPicturePath);
                    startActivity(intent);
                    break;
                default:
            }
        }
    }
}