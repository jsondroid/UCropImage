package com.yalantis.ucrop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.sax.RootElement;

/**
 * Created by wenbaohe on 2018/12/20.
 */

public class OpenPhotoUtil {

    public static final int OPEN_REQUST_CODE = 2018;

    public static void openPhoto(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*").addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        activity.startActivityForResult(Intent.createChooser(intent, "选择图片"), 2018);
    }

    /**
     * 获取选择的图片
     */
    public static Uri getonActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == OPEN_REQUST_CODE) {
                Uri selectedUri = data.getData();
                return selectedUri;
            }
        }
        return null;
    }
}
