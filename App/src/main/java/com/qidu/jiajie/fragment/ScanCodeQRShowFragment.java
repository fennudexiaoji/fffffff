package com.qidu.jiajie.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.base.utils.HttpUrl;
import com.bumptech.glide.Glide;
import com.qidu.jiajie.R;

/**
 *展示二维码
 */

public class ScanCodeQRShowFragment extends DialogFragment{


    private String tips;
    private String url;
    public static ScanCodeQRShowFragment getInstance(String tips,String url) {
        ScanCodeQRShowFragment sf = new ScanCodeQRShowFragment();
        sf.tips = tips;
        sf.url=url;
        return sf;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_scan_code_qr_show, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView scanCodeTip=view.findViewById(R.id.scan_code_tip);
        scanCodeTip.setText(tips);
        ImageView codeImg=view.findViewById(R.id.code_qr_img);
        Glide.with(getActivity())
                .load(HttpUrl.API_HOST+"/"+url)
                .into(codeImg);
         View btnDis=view.findViewById(R.id.btn_dis);
         btnDis.setOnClickListener(v -> {
             dismiss();
         });
    }
}
