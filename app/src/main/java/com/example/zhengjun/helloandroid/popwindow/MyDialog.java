package com.example.zhengjun.helloandroid.popwindow;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zhengjun.helloandroid.R;

public class MyDialog extends Dialog implements View.OnClickListener{
    //定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener{
            public void back(String name);


    }
    private OnCustomDialogListener customDialogListener;
    EditText etName;
    EditText editText;
    TextView title;
    String titleString;
    public MyDialog(Context context,String name,OnCustomDialogListener customDialogListener) {
            super(context);
            this.titleString=name;
            this.customDialogListener = customDialogListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) { 
            super.onCreate(savedInstanceState);
            MyDialog.this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_mine_change_info);
            //设置标题
            title =(TextView)findViewById(R.id.dialog_title);
            title.setText(titleString);
            etName = (EditText)findViewById(R.id.mine_info_content);
            Button cancleBtn = (Button) findViewById(R.id.dialog_cancle);
            Button sureButton =(Button)findViewById(R.id.dialog_sure);
            cancleBtn.setOnClickListener(this);
            sureButton.setOnClickListener(this);
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.dialog_cancle:
			MyDialog.this.dismiss();
			break;
		case R.id.dialog_sure:
			customDialogListener.back(String.valueOf(etName.getText().toString()));
			MyDialog.this.dismiss();
		default:
			break;
		}
	}

}