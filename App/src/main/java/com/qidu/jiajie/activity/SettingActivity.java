package com.qidu.jiajie.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.app.base.bean.UserRealm;
import com.app.base.utils.HttpUrl;
import com.app.base.utils.IntentParams;
import com.common.lib.base.AbsBaseActivity;
import com.common.lib.bean.ActionItem;
import com.common.lib.global.AppGlobal;
import com.common.lib.utils.SharedPreferencesUtils;
import com.common.lib.utils.ToastUtils;
import com.common.lib.widget.ActionSheetDialog;
import com.qidu.jiajie.BuildConfig;
import com.qidu.jiajie.R;
import com.qidu.jiajie.databinding.ActivitySettingBinding;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AbsBaseActivity{
    private ActivitySettingBinding binding;
    private List<ActionItem> actionItemList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        binding.layoutTitle.headerLeftBtnImg.setOnClickListener(v -> {
            finish();
        });
        binding.layoutTitle.headerText.setText("设置");
        ActionItem oldPasswordItem=new ActionItem();
        oldPasswordItem.setItemName("通过旧密码方式");
        oldPasswordItem.setItemType(0);
        actionItemList.add(oldPasswordItem);
        ActionItem newPasswordItem=new ActionItem();
        newPasswordItem.setItemName("通过手机验证方式");
        newPasswordItem.setItemType(1);
        actionItemList.add(oldPasswordItem);
        binding.passwordSetting.setOnClickListener(v -> {
            ActionSheetDialog dialog=new ActionSheetDialog(getActivity()).builder()
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(true)
                    .setSheetItems(actionItemList)
                    .setOnSheetItemClickListener(new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onItemClick(ActionItem data, int which) {
                            String url="";
                            if(data.getItemType()==0){
                                url="/#/setpay?type=1";
                            }else if(data.getItemType()==1){
                                url="/#/setpay?type=2";
                            }
                            startWebActivity(url);
                        }
                    });
            dialog.show();
        });
        binding.loginOut.setOnClickListener(v -> {
            SharedPreferencesUtils.getInstance(getActivity()).saveData(AppGlobal.KEY_LOGIN_TOKEN,"");
            SharedPreferencesUtils.getInstance(getActivity()).saveData(AppGlobal.KEY_LOGIN_USER_ID,"");
            try {
                UserRealm.deleteAll();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }


    public void startWebActivity(String url) {
        Intent intent=new Intent(getActivity(), WebAppActivity.class);
        intent.putExtra(IntentParams.WEB_URL, BuildConfig.DEFAULT_HOST+url);
        getActivity().startActivity(intent);
    }
}
