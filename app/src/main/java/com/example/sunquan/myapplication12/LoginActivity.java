package com.example.sunquan.myapplication12;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity{
    private TextView tv_main_title;//标题
    private Button btn_back,btn_register,btn_find_psw;//返回键,显示的注册，找回密码
    private Button btn_login,btn_qq;//登录按钮
    private String userName,psw;//获取的用户名，密码
    private EditText et_user_name,et_psw;//编辑框
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //初始化
        Bmob.initialize(this, "188616062aa6d472f2b457327012107b");
         initView();
    }
    //获取界面控件
    private void initView() {
        //从main_title_bar中获取的id
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("登录");
        btn_back = findViewById(R.id.btn_back);
        //从activity_login.xml中获取的
        btn_register = findViewById(R.id.btn_register);
        btn_find_psw = findViewById(R.id.btn_find_psw);
        btn_qq = findViewById(R.id.btn_qq);
        btn_login = findViewById(R.id.btn_login);
        et_user_name = findViewById(R.id.et_user_name);
        et_psw = findViewById(R.id.et_psw);
        //返回键的点击事件
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登录界面销毁
                LoginActivity.this.finish();
            }
        });
        //立即注册控件的点击事件
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //为了跳转到注册界面，并实现注册功能
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        //立即QQ登录控件的点击事件
        btn_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //为了跳转到QQ登录界面，并实现登录功能
                Intent intent = new Intent(LoginActivity.this, QqLoginActivity.class);
                startActivityForResult(intent, 2);
            }
        });
        //找回密码控件的点击事件
        btn_find_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FindPswActivity.class);
                startActivityForResult(intent, 3);
                //跳转到找回密码界面（此页面暂未可以使用）
            }
        });
        //登录按钮的点击事件
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

            //编写登录活动
            private  void login(){

                if (!validate()) {
                    onLoginFailed();
                    return;
                }

                //开始登录，获取用户名和密码 getText().toString().trim();
                 String number=et_user_name.getText().toString().trim();
                 String psw=et_psw.getText().toString().trim();

                User user=  new User();
                user.setMobilePhoneNumber(number);
                user.setPassword(psw);

                user.login(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if(e==null){
                            Toast.makeText(LoginActivity.this,bmobUser.getUsername()+"登录成功",Toast.LENGTH_SHORT).show();
                            // 允许用户使用应用
                            onLoginSuccess();

                        }else {
                            String errorMessage ;
                            switch (e.getErrorCode()) {
                                case 101:
                                    errorMessage = "用户名或者密码不正确";
                                    break;
                                case 9016:
                                    errorMessage = "网都没有，你登陆个啥！！！";
                                    break;
                                default:
                                    errorMessage = "登陆失败！";
                                    break;
                            }
                            Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT).show();
                            Log.i("htht", "登陆时出错了 "+e.getErrorCode());
                        }
                    }
                });
            }
            //输入框验证
            private boolean validate() {

                boolean valid = true;

                String userName = et_user_name.getText().toString();
                String password = et_psw.getText().toString();
                //匹配手机号正则表达式,
                //注意可能会有bug*******
                if (userName.isEmpty() ) {
                    et_user_name.setError("请输入正确的账号");
                    valid = false;
                } else {
                    et_user_name.setError(null);
                }


                if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                    et_psw.setError("请输入4到10位密码");
                    valid = false;
                } else {
                    et_psw.setError(null);
                }
                return valid;

            }
            public void onLoginSuccess() {
                btn_login.setEnabled(true);
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
            public void onLoginFailed() {
                btn_login.setEnabled(false);
            }

    }
    /**
     *从SharedPreferences中根据用户名读取密码
     */
    /*
    private String readPsw(String userName){
        //getSharedPreferences("loginInfo",MODE_PRIVATE);
        //"loginInfo",mode_private; MODE_PRIVATE表示可以继续写入
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        //sp.getString() userName, "";
        return sp.getString(userName , "");
    }*/

    /**
     *保存登录状态和登录用户名到SharedPreferences中
     */
    /*
    private void saveLoginStatus(boolean status,String userName){
        //saveLoginStatus(true, userName);
        //loginInfo表示文件名  SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        //获取编辑器
        SharedPreferences.Editor editor=sp.edit();
        //存入boolean类型的登录状态
        editor.putBoolean("isLogin", status);
        //存入登录状态时的用户名
        editor.putString("loginUserName", userName);
        //提交修改
        editor.commit();
    }*/
    /**
     * 注册成功的数据返回至此
     * @param requestCode 请求码
     * @param resultCode 结果码
     * @param data 数据
     *//*
    @Override
    //显示数据， onActivityResult
    //startActivityForResult(intent, 1); 从注册界面中获取数据
    //int requestCode , int resultCode , Intent data
    // LoginActivity -> startActivityForResult -> onActivityResult();
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            //是获取注册界面回传过来的用户名
            // getExtra().getString("***");
            String userName=data.getStringExtra("userName");
            if(!TextUtils.isEmpty(userName)){
                //设置用户名到 et_user_name 控件
                et_user_name.setText(userName);
                //et_user_name控件的setSelection()方法来设置光标位置
                et_user_name.setSelection(userName.length());
            }
        }
    }*/
