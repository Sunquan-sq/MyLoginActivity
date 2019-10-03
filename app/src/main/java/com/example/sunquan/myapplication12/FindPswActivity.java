package com.example.sunquan.myapplication12;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FindPswActivity extends AppCompatActivity {
    private TextView tv_main_title;//标题
    private Button btn_back;//返回键
    private Button btn_login;//登录按钮
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_psw);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //初始化
        init();
    }
    //获取界面控件
    private void init() {
        //从main_title_bar中获取的id
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("找回密码");
        btn_back = findViewById(R.id.btn_back);
        btn_login = findViewById(R.id.btn_login);

        //返回键的点击事件
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登录界面销毁
               FindPswActivity.this.finish();
            }
        });
    }
}