package com.gs.buluo.app.view.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.File;
import java.util.Hashtable;

import butterknife.Bind;

/**
 * Created by hjn on 2017/3/6.
 */

public class QRShowActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "Share";
    private int QR_WIDTH = 0;
    private int QR_HEIGHT = 0;


    @Bind(R.id.qr_image)
    ImageView image;
    private Bitmap bitmap;

    private static IWXAPI msgApi = null;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        msgApi = WXAPIFactory.createWXAPI(TribeApplication.getInstance().getApplicationContext(), null);
        // 将该app注册到微信
        msgApi.registerApp(Constant.Base.WX_ID);
        QR_WIDTH = DensityUtils.dip2px(this, 300);
        QR_HEIGHT = DensityUtils.dip2px(this, 300);
        findViewById(R.id.share_wx).setOnClickListener(this);
        findViewById(R.id.share_msg).setOnClickListener(this);
        createQRImage(Constant.APK_URL);
    }

    public void createQRImage(String url) {
        try {
            if (url == null || "".equals(url) || url.length() < 1) {
                return;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            image.setImageBitmap(bitmap);
            CommonUtils.saveBitmap2file(bitmap,"qr_image");
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_qr_show;
    }

    @Override
    public void onClick(View v) {
        if (bitmap==null)return;
        switch (v.getId()) {
            case R.id.share_wx:
                shareWeChat(Constant.DIR_PATH+"qr_image");
                break;
            case R.id.share_msg:
                shareMsg(Constant.DIR_PATH+"qr_image");
                break;
        }
    }

    private void shareMsg(String path) {
        File file = new File(path);
        Uri uriToImage = Uri.fromFile(file);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享图片"));
    }

    private void shareWeChat(String path){
        if (!CommonUtils.checkInstallation("com.tencent.mm")){
            ToastUtils.ToastMessage(getCtx(),"您当前未安装微信");
            return;
        }
        File file = new File(path);
        Uri uriToImage = Uri.fromFile(file);
        Intent shareIntent = new Intent();
        //发送图片到朋友圈
        //ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        //发送图片给好友。
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        shareIntent.setComponent(comp);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享图片"));
    }
}