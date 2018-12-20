package skinsenor.android.com.ucropimage;

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
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.UCropView;

import java.io.File;

public class CropActivity extends AppCompatActivity {
    UCropView ucrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        ucrop = (UCropView) findViewById(R.id.ucrop);

        try {
            Uri selectedUri = getIntent().getParcelableExtra("source");
            Uri destinationUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "测试.jpeg"));

            ucrop.getCropImageView().setImageUri(selectedUri, destinationUri);//输入图片he输出图片
            ucrop.getCropImageView().setRotateEnabled(false);//是否可以旋转
            ucrop.getCropImageView().setTargetAspectRatio(0.5f);//切图框比例大小

            ucrop.getCropImageView().setMaxResultImageSizeX(480);//输出图片宽大小px
            ucrop.getCropImageView().setMaxResultImageSizeY(480);//输出图片高大小px

            ucrop.getOverlayView().setCircleDimmedLayer(true);//启用圆形框
            ucrop.getOverlayView().setCropFrameColor(Color.WHITE);//边框颜色
            ucrop.getOverlayView().setShowCropGrid(false);//是否显示网格
            ucrop.getOverlayView().setShowCropFrame(false);//是否显示边框

            //切图框是否可移动和缩放（非图片）
//            ucrop.getOverlayView().setFreestyleCropMode(OverlayView.FREESTYLE_CROP_MODE_ENABLE);

        } catch (Exception e) {
            e.printStackTrace();
        }

        ((CheckBox) findViewById(R.id.checkBox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ucrop.getOverlayView().setFreestyleCropMode(OverlayView.FREESTYLE_CROP_MODE_ENABLE);
                } else {
                    ucrop.getOverlayView().setFreestyleCropMode(OverlayView.FREESTYLE_CROP_MODE_DISABLE);
                }
                ucrop.getOverlayView().invalidate();
            }
        });

        ((Button) findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropAndSaveImage();
            }
        });
    }


    protected void cropAndSaveImage() {
        supportInvalidateOptionsMenu();
        /*
          Bitmap.CompressFormat compressFormat  图片类型 JPEG、PNG
         int compressQuality 图片质量
         */
        ucrop.getCropImageView().cropAndSaveImage(Bitmap.CompressFormat.JPEG, 90, new BitmapCropCallback() {

            @Override
            public void onBitmapCropped(@NonNull Uri resultUri, int offsetX, int offsetY, int imageWidth, int imageHeight) {

                Log.e("路径----",""+resultUri.getPath());
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
//        setResult(RESULT_OK, new Intent()
//                .putExtra(UCrop.EXTRA_OUTPUT_URI, uri)
//                .putExtra(UCrop.EXTRA_OUTPUT_CROP_ASPECT_RATIO, resultAspectRatio)
//                .putExtra(UCrop.EXTRA_OUTPUT_IMAGE_WIDTH, imageWidth)
//                .putExtra(UCrop.EXTRA_OUTPUT_IMAGE_HEIGHT, imageHeight)
//                .putExtra(UCrop.EXTRA_OUTPUT_OFFSET_X, offsetX)
//                .putExtra(UCrop.EXTRA_OUTPUT_OFFSET_Y, offsetY)
//        );
    }

    protected void setResultError(Throwable throwable) {
//        setResult(UCrop.RESULT_ERROR, new Intent().putExtra(UCrop.EXTRA_ERROR, throwable));
    }
}
