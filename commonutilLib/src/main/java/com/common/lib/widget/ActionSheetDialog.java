package com.common.lib.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.common.lib.R;
import com.common.lib.adapter.ActionSheetDialogAdapter;
import com.common.lib.bean.ActionItem;
import com.common.lib.divider.DividerItemDecoration;
import com.common.lib.utils.ScreenUtils;

import java.util.List;

public class ActionSheetDialog {
	private Context context;
	private Dialog dialog;
	private TextView txt_title;
	private TextView txt_cancel;
	private RecyclerView recyclerView;
	//private ScrollView sLayout_content;
	private boolean showTitle = false;
	private boolean isShowSelectIcon;

	private String itemTextColor;
	private List<ActionItem> sheetItemList;
	private Display display;
	private ActionSheetDialogAdapter adapter;
	private OnSheetItemClickListener listener;

	private int defaultSelectPosition=-1;

	public ActionSheetDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public ActionSheetDialog builder() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.view_actionsheet, null);

		view.setMinimumWidth(display.getWidth());

		//sLayout_content = (ScrollView) view.findViewById(R.id.sLayout_content);
		recyclerView = (RecyclerView) view
				.findViewById(R.id.recycler);
		txt_title = (TextView) view.findViewById(R.id.txt_title);
		txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
		txt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
		dialog.setContentView(view);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.x = 0;
		lp.y = 0;
		dialogWindow.setAttributes(lp);

		return this;
	}

	public ActionSheetDialog setOnSheetItemClickListener(OnSheetItemClickListener listener) {
		this.listener = listener;
		return this;
	}

	public ActionSheetDialog setDefaultSelectPosition(int defaultSelectPosition){
		this.defaultSelectPosition=defaultSelectPosition;
		return this;
	}
	public ActionSheetDialog setTitle(String title) {
		showTitle = true;
		txt_title.setVisibility(View.VISIBLE);
		txt_title.setText(title);
		return this;
	}
	public ActionSheetDialog setItemTextColor(String itemTextColor){
		this.itemTextColor=itemTextColor;
		if(txt_cancel!=null){
			txt_cancel.setTextColor(Color.parseColor(itemTextColor));
		}
		return this;
	}
	public ActionSheetDialog showSelectIcon(boolean isShowSelectIcon){
		this.isShowSelectIcon=isShowSelectIcon;
		return this;
	}
	public ActionSheetDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	public ActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
		dialog.setCanceledOnTouchOutside(cancel);
		return this;
	}


	public ActionSheetDialog setSheetItems(List<ActionItem> itemList) {
		if (itemList != null || itemList.size() > 0) {
			this.sheetItemList = itemList;
		}

		int size = sheetItemList.size();
		adapter = new ActionSheetDialogAdapter(context);
		adapter.setItemTextColor(itemTextColor);
		adapter.setShowTitle(showTitle);
		adapter.showSelectIcon(isShowSelectIcon);
		recyclerView.setHasFixedSize(true);
		LinearLayoutManager manager = new LinearLayoutManager(context);
		recyclerView.setLayoutManager(manager);
		if (size >= 6) {
			LayoutParams params = (LayoutParams) recyclerView
					.getLayoutParams();
			params.height = (display.getHeight() - ScreenUtils.getStatusBarHeight(context) - ScreenUtils.dp2px(context, 50)) / 2;
			recyclerView.setLayoutParams(params);
		}
		DividerItemDecoration decoration=new DividerItemDecoration(context, LinearLayoutManager.VERTICAL, R.drawable.list_divider_default);
		decoration.showLastFootViewDivider(false);
		recyclerView.addItemDecoration(decoration);
		((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
		recyclerView.setAdapter(adapter);
		if(defaultSelectPosition!=-1){
			adapter.setSelectPosition(defaultSelectPosition);
			adapter.notifyDataSetChanged();
		}
		adapter.setOnItemClickListener(new ActionSheetDialogAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int position, View v) {
				adapter.setSelectPosition(position);
				adapter.notifyDataSetChanged();
				if (listener != null) {
					if (dialog != null) {
						new Handler().postDelayed(new Runnable(){
							public void run(){
								dialog.dismiss();
							}
						}, 300);
					}
					listener.onItemClick(adapter.getDataList().get(adapter.getSelectPosition()), adapter.getSelectPosition());
				}
			}
		});
		adapter.setList(sheetItemList);
		return this;
	}

	public void show() {
		dialog.show();
	}

	public interface OnSheetItemClickListener {
		void onItemClick(ActionItem data, int which);
	}
}
