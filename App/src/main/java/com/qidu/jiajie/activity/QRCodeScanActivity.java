package com.qidu.jiajie.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.base.utils.IntentParams;
import com.common.lib.base.AbsBaseActivity;
import com.common.lib.global.PermissionUtils;
import com.google.zxing.Result;
import com.liang.scancode.utils.Constant;
import com.liang.scancode.zxing.ScanListener;
import com.liang.scancode.zxing.ScanManager;
import com.liang.scancode.zxing.decode.DecodeThread;
import com.qidu.jiajie.R;
import com.qidu.jiajie.utils.PhotoUtils;

public class QRCodeScanActivity extends AbsBaseActivity implements ScanListener,View.OnClickListener{
    private static final int PICK_ACTIVITY_REQUEST_CODE = 10003;
    public static int QR_REQUEST_CODE=0x666;
    public static int QR_RESULT_CODE=0x888;
    SurfaceView scanPreview = null;
    View scanContainer;
    View scanCropView;
    ImageView scanLine;
    ScanManager scanManager;
    TextView iv_light;
    TextView qrcode_g_gallery;
    TextView qrcode_ic_back;
    private Handler handler=new Handler();


    //Button rescan;
    ImageView scan_image;

    View authorize_return;
    private int scanMode;//扫描模型（条形，二维码，全部）

    TextView title;
    TextView scan_hint;
    //TextView tv_scan_result;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scan);
        scanMode=getIntent().getIntExtra(Constant.REQUEST_SCAN_MODE, Constant.REQUEST_SCAN_MODE_ALL_MODE);
        initView();
    }

    private void initView() {
        //rescan=findViewById(R.id.service_register_rescan);
        scan_image= (ImageView) findViewById(R.id.scan_image);
        authorize_return=findViewById(R.id.authorize_return);
        title= (TextView) findViewById(R.id.common_title_TV_center);
        scan_hint= (TextView) findViewById(R.id.scan_hint);
        //tv_scan_result=findViewById(R.id.tv_scan_result);

        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = findViewById(R.id.capture_container);
        scanCropView = findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);
        qrcode_g_gallery = (TextView) findViewById(R.id.qrcode_g_gallery);
        qrcode_g_gallery.setOnClickListener(this);
        qrcode_ic_back = (TextView) findViewById(R.id.qrcode_ic_back);
        qrcode_ic_back.setOnClickListener(this);
        iv_light = (TextView) findViewById(R.id.iv_light);
        iv_light.setOnClickListener(this);
        //rescan.setOnClickListener(this);
        authorize_return.setOnClickListener(this);
        //构造出扫描管理器
        scanManager = new ScanManager(this, scanPreview, scanContainer, scanCropView, scanLine, scanMode,this);

        switch (scanMode){
            case DecodeThread.BARCODE_MODE:
                title.setText(getResources().getString(R.string.scan_barcode_title));
                scan_hint.setText(getResources().getString(R.string.scan_barcode_hint));
                break;
            case DecodeThread.QRCODE_MODE:
                title.setText(getResources().getString(R.string.scan_qrcode_title));
                scan_hint.setText(getResources().getString(R.string.scan_qrcode_hint));
                break;
            case DecodeThread.ALL_MODE:
                title.setText(getResources().getString(R.string.scan_allcode_title));
                scan_hint.setText(getResources().getString(R.string.scan_allcode_hint));
                break;
        }
    }

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_MULTI_PERMISSION:
                    PhotoUtils.pickPhoto(QRCodeScanActivity.this,PICK_ACTIVITY_REQUEST_CODE);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public void onClick(View view) {
        int id=view.getId();
        if (id == R.id.qrcode_g_gallery) {
            scan_image.setImageBitmap(null);
            String[] requestPermissions = {
                    PermissionUtils.PERMISSION_READ_EXTERNAL_STORAGE,
                    PermissionUtils.PERMISSION_WRITE_EXTERNAL_STORAGE
            };
            PermissionUtils.requestMultiPermissions(null,this,requestPermissions,mPermissionGrant);
        } else if (id == R.id.iv_light) {
            scanManager.switchLight();
        } else if (id == R.id.qrcode_ic_back) {
            finish();
        }/* else if (id == R.id.service_register_rescan) {
            startScan();
        }*/ else if (id == R.id.authorize_return) {
            finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        scanManager.onResume();
        //rescan.setVisibility(View.INVISIBLE);
        //scan_image.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        scanManager.onPause();
    }
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    /**
     *
     */
    @Override
    public void scanResult(Result rawResult, Bundle bundle) {
        //扫描成功后，扫描器不会再连续扫描，如需连续扫描，调用reScan()方法。
        //scanManager.reScan();
//		Toast.makeText(that, "result="+rawResult.getText(), Toast.LENGTH_LONG).show();
        vibrate();//震动一下
        if (!scanManager.isScanning()) { //如果当前不是在扫描状态
            //设置再次扫描按钮出现
            //rescan.setVisibility(View.VISIBLE);
            scan_image.setVisibility(View.VISIBLE);
            Bitmap barcode = null;
            byte[] compressedBitmap = bundle.getByteArray(DecodeThread.BARCODE_BITMAP);
            if (compressedBitmap != null) {
                barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
                barcode = barcode.copy(Bitmap.Config.ARGB_8888, true);
            }
            scan_image.setImageBitmap(barcode);
        }
        //rescan.setVisibility(View.VISIBLE);
        scan_image.setVisibility(View.VISIBLE);
        //tv_scan_result.setVisibility(View.VISIBLE);
        final String scanResult=rawResult.getText();
        //tv_scan_result.setText("扫描结果："+scanResult);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent();
                intent.putExtra(IntentParams.KEY_QR_CODE_SCAN_RESULT_VALUE,scanResult);
                setResult(QR_RESULT_CODE,intent);
                finish();
            }
        },500);
    }

    /*void startScan() {
        if (rescan.getVisibility() == View.VISIBLE) {
            rescan.setVisibility(View.INVISIBLE);
            //scan_image.setVisibility(View.GONE);
            scan_image.setImageBitmap(null);
            scanManager.reScan();
        }
    }*/

    @Override
    public void scanError(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        //相机扫描出错时
        if(e.getMessage()!=null&&e.getMessage().startsWith("相机")){
            //scanPreview.setVisibility(View.INVISIBLE);
            //重新扫描
            scan_image.setImageBitmap(null);
            scanManager.reScan();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK&&requestCode==PICK_ACTIVITY_REQUEST_CODE){
            if(data==null){
                return;
            }
            Uri imageUri = data.getData();
            String picturePath=PhotoUtils.getPathByUri4kitkat(this,imageUri);
            if(picturePath==null){
                return;
            }
            scanManager.scanningImage(picturePath);
        }
    }


}
