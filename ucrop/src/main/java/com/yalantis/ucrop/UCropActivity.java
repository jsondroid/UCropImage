package com.yalantis.ucrop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.yalantis.ucrop.callback.BitmapCropCallback;
import com.yalantis.ucrop.view.GestureCropImageView;
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.UCropView;

import java.io.File;

public class UCropActivity extends AppCompatActivity {

    public final static String EXTRA_INPUT_URI = "InputUri";//选择要裁剪的图片的地址
    public final static String EXTRA_OUTPUT_URI = "OutputUri";//输出裁剪后图片的地址

    public final static int RESULT_CODE = 31;//回传
    public final static int REQUST_CODE = 32;//请求
    public final static int RESULT_ERROR = 33;//错误返回代码

    public static final String EXTRA_OUTPUT_CROP_ASPECT_RATIO = "CropAspectRatio";
    public static final String EXTRA_OUTPUT_IMAGE_WIDTH = "ImageWidth";
    public static final String EXTRA_OUTPUT_IMAGE_HEIGHT = "ImageHeight";
    public static final String EXTRA_OUTPUT_OFFSET_X = "OffsetX";
    public static final String EXTRA_OUTPUT_OFFSET_Y = "OffsetY";
    public static final String EXTRA_ERROR = "Error";

    public static final String EXTRA_ASPECT_RATIO_X = "AspectRatioX";
    public static final String EXTRA_ASPECT_RATIO_Y = "AspectRatioY";

    public static final String EXTRA_MAX_SIZE_X = "MaxSizeX";
    public static final String EXTRA_MAX_SIZE_Y = "MaxSizeY";

    private UCropView ucrop;
    private OverlayView overlayView;
    private GestureCropImageView cropImageView;
    private Button btn_crop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ucrop_layout);
        initView();
        setConfig();
    }

    private void initView() {
        ucrop = (UCropView) findViewById(R.id.ucrop);
        btn_crop = (Button) findViewById(R.id.btn_crop);
        overlayView = ucrop.getOverlayView();
        cropImageView = ucrop.getCropImageView();

        btn_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropAndSaveImage();
            }
        });
    }

    private void setConfig() {
        try {

            Intent intent = getIntent();
            Uri selectedUri = intent.getParcelableExtra(EXTRA_INPUT_URI);
            Uri destinationUri = intent.getParcelableExtra(EXTRA_OUTPUT_URI);
            int maxsize_x = intent.getIntExtra(EXTRA_MAX_SIZE_X, 0);//输出图像大小
            int maxsize_y = intent.getIntExtra(EXTRA_MAX_SIZE_Y, 0);


            cropImageView.setImageUri(selectedUri, destinationUri);//输入图片he输出图片
            cropImageView.setRotateEnabled(false);//是否可以旋转
            cropImageView.setTargetAspectRatio(0.5f);//切图框比例大小

            if (maxsize_x == 0 || maxsize_y == 0) {
                maxsize_x = 480;
                maxsize_y = 480;
            }
            cropImageView.setMaxResultImageSizeX(maxsize_x);//输出图片宽大小px
            cropImageView.setMaxResultImageSizeY(maxsize_y);//输出图片高大小px

            overlayView.setCircleDimmedLayer(true);//启用圆形框
            overlayView.setCropFrameColor(Color.WHITE);//边框颜色
            overlayView.setShowCropGrid(false);//是否显示网格
            overlayView.setShowCropFrame(false);//是否显示边框

        } catch (Exception e) {
            e.printStackTrace();
            setResultError(e);
            finish();
        }
    }


    private void cropAndSaveImage() {
        /*
          Bitmap.CompressFormat compressFormat  图片类型 JPEG、PNG
         int compressQuality 图片质量
         */
        ucrop.getCropImageView().cropAndSaveImage(Bitmap.CompressFormat.JPEG, 90, new BitmapCropCallback() {

            @Override
            public void onBitmapCropped(@NonNull Uri resultUri, int offsetX, int offsetY, int imageWidth, int imageHeight) {
                setResultUri(resultUri, ucrop.getCropImageView().getTargetAspectRatio(), offsetX, offsetY, imageWidth, imageHeight);
                finish();
            }

            @Override
            public void onCropFailure(@NonNull Throwable t) {
                setResultError(t);
                finish();
            }
        });
    }

    protected void setResultUri(Uri uri, float resultAspectRatio, int offsetX, int offsetY, int imageWidth, int imageHeight) {
        setResult(RESULT_CODE, new Intent()
                .putExtra(EXTRA_OUTPUT_URI, uri)
                .putExtra(EXTRA_OUTPUT_CROP_ASPECT_RATIO, resultAspectRatio)
                .putExtra(EXTRA_OUTPUT_IMAGE_WIDTH, imageWidth)
                .putExtra(EXTRA_OUTPUT_IMAGE_HEIGHT, imageHeight)
                .putExtra(EXTRA_OUTPUT_OFFSET_X, offsetX)
                .putExtra(EXTRA_OUTPUT_OFFSET_Y, offsetY)
        );
    }

    protected void setResultError(Throwable throwable) {
        setResult(RESULT_ERROR, new Intent().putExtra(EXTRA_ERROR, throwable));
    }


    /**
     * 可以拖动圆圈框
     * ((CheckBox) findViewById(R.id.checkBox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    if (isChecked) {
    ucrop.getOverlayView().setFreestyleCropMode(OverlayView.FREESTYLE_CROP_MODE_ENABLE);
    } else {
    ucrop.getOverlayView().setFreestyleCropMode(OverlayView.FREESTYLE_CROP_MODE_DISABLE);
    }
    ucrop.getOverlayView().invalidate();
    }
    });*/
}
