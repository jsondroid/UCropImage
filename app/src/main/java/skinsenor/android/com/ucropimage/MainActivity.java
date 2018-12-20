package skinsenor.android.com.ucropimage;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yalantis.ucrop.UCropActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void select(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*").addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        startActivityForResult(Intent.createChooser(intent, "选择图片"), 2);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {

                    Intent intent = new Intent(this, UCropActivity.class);
                    Bundle mCropOptionsBundle = new Bundle();
                    mCropOptionsBundle.putParcelable(UCropActivity.EXTRA_INPUT_URI, selectedUri);

                    Uri destinationUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "测试.jpeg"));
                    mCropOptionsBundle.putParcelable(UCropActivity.EXTRA_OUTPUT_URI, destinationUri);
                    intent.putExtras(mCropOptionsBundle);
                    startActivityForResult(intent, UCropActivity.REQUST_CODE);
                } else {

                }
            }
            if (resultCode == UCropActivity.RESULT_CODE) {
                Uri uri = data.getParcelableExtra(UCropActivity.EXTRA_OUTPUT_URI);
                Log.e("输出路径---->", "" + uri.getPath());
            }
            if (resultCode == UCropActivity.RESULT_ERROR) {
                Log.e("裁剪错误---->", "" + data.getSerializableExtra(UCropActivity.EXTRA_ERROR));
            }
        }
    }
}
