package com.example.sunquan.myapplication12;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class RegisterActivity extends AppCompatActivity {
    private TextView tv_main_title;//标题
    private Button btn_back;//返回按钮
    private Button btn_register,btn_send;//注册按钮
    //用户名，密码，再次输入的密码的控件
    private EditText et_user_name,et_psw,et_psw_again,et_user_number,et_verify;
    //用户名，密码，再次输入的密码的控件的获取值
    //标题布局
    private RelativeLayout rl_title_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置页面布局 ,注册界面
        setContentView(R.layout.activity_register);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
    }

    private void initView() {
        //从main_title_bar.xml 页面布局中获取对应的UI控件
        tv_main_title=findViewById(R.id.tv_main_title);
        tv_main_title.setText("注册");

        //布局根元素
        rl_title_bar=findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.TRANSPARENT);
        btn_back=findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登录界面销毁
                RegisterActivity.this.finish();
            }
        });
        //从activity_register.xml 页面中获取对应的UI控件,密码，账号，验证码，手机号的接收。
        btn_register=findViewById(R.id.btn_register);
        et_user_name=findViewById(R.id.et_user_name);
        et_psw=findViewById(R.id.et_psw);
        et_psw_again=findViewById(R.id.et_psw_again);
        et_user_number=findViewById(R.id.et_user_number);
        et_verify=findViewById(R.id.et_verify);
        //请求验证码
        btn_send=findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //请求短信
                BmobSMS.requestSMSCode(et_user_number.getText().toString(), "", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if (e == null) {
                            //验证码发送成功
                            Toast.makeText(RegisterActivity.this, "发送成功", Toast.LENGTH_LONG).show();
                            //myCountDownTimer.start();
                            Log.i("https", "短信id：" + integer);//用于查询本次短信发送详情
                        } else {
                            String errorMessage;
                            switch (e.getErrorCode()) {
                                case 9018:
                                    errorMessage = "手机号码为空，咋子发出去嘛！！！";
                                    break;
                                default:
                                    errorMessage = "验证码获取失败！！！";
                                    break;

                            }
                            Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_LONG).show();
                            Log.e("htht", "done: " + e.getMessage());
                            Log.e("htht", "done:  code" + e.getErrorCode()+ "-" + e.getMessage());
                        }
                    }
                });
                //返回键
                btn_back = findViewById(R.id.btn_back);
                btn_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        RegisterActivity.this.finish();
                    }
                });
                //注册按钮
               btn_register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       register();

                    }
                });
            }

            private  void register(){
                if (!validate()) {
                    onregisterFailed();
                    Log.i("htht", "register: 直接退出了");
                    return;
                }
                btn_register.setEnabled(false);

                //验证码验证
                BmobSMS.verifySmsCode(et_user_number.getText().toString(), et_verify.getText().toString(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            //短信验证码已验证成功
                            Log.i("htht", "验证通过");
                            final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this,
                                    R.style.Theme_AppCompat_DayNight_Dialog);
                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("创建账号中...");
                            progressDialog.show();

                            String name = et_user_name.getText().toString();
                            String number = et_user_number.getText().toString();
                            String password = et_psw.getText().toString();

                            User user = new User();
                            user.setUserName(name);
                            user.setMobilePhoneNumber(number);
                            user.setPassword(password);
                            user.signUp(new SaveListener<User>() {
                                @Override
                                public void done(User user, BmobException e) {
                                    if(e == null){
                                        //注册成功，将新用户加入UserFarm表中
                                        UserFarm userFarm =new UserFarm();
                                        userFarm.setUser(user);
                                        userFarm.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                if(e == null){
                                                    Log.i("htht", "新用户注册成功，并加入到studentCommunity表中: ");
                                                }else{
                                                    Log.i("htht", "新用户注册成功，但加入到studentCommunity表中失败: "+e.getErrorCode());
                                                }
                                            }
                                        });

                                        onregisterSuccess();
                                    }else{
                                        String errorMessage ;
                                        switch (e.getErrorCode()){
                                            case 202:
                                            case 209:
                                                errorMessage = "该手机号码已经存在";
                                                break;
                                            case 9016:
                                                errorMessage = "网都没有，你注册个啥！！！";
                                                break;
                                            default:
                                                errorMessage = "注册失败！";
                                                break;

                                        }
                                        Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_LONG).show();
                                        Log.i("htht", "出错了 "+e.getErrorCode());
                                        onregisterFailed();
                                    }
                                    progressDialog.dismiss();
                                }
                            });

                        }else{
                            Log.i("htht", "验证失败：code ="+e.getErrorCode()+",msg = "+e.getLocalizedMessage()+"    线程名    "+Thread.currentThread().getName());
                            String errorMessage ;
                            switch (e.getErrorCode()){
                                case 207:
                                    errorMessage = "验证码错误，请重新输入 :)";
                                    break;
                                default:
                                    errorMessage = "出错了！！！";
                                    break;

                            }
                            Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT).show();
                            onregisterFailed();
                        }
                    }
                });
            }
            ////////////////////////////////////////////////////////////////////////////////////////
            public void onregisterFailed() {
                //表示按钮不可用，按钮是灰色的
                btn_register.setEnabled(true);

            }
            private void onregisterSuccess() {
                btn_register.setEnabled(true);
                Intent intent = new Intent();
                // 获取用户计算后的结果
                String number = et_user_number.getText().toString();
                intent.putExtra("number", number);
                setResult(RESULT_OK, intent);
                finish();
            }
            ////////////////////////////////////////////////////////////////////////////////////////
            //判断注册内容是否符合条件 validate证实; 确认; 确证; 使生效; 使有法律效力; 批准; 确认…有效; 认可;
            public boolean validate() {

                boolean valid = true;
                String name = et_user_name.getText().toString();
                String number = et_user_number.getText().toString();
                String psw = et_psw.getText().toString();
                String pswagain = et_psw_again.getText().toString();
                String verify = et_verify.getText().toString();

                if (name.isEmpty()|| name.length() > 10) {
                    et_user_name.setError("请输入小于10位用户名");
                    valid = false;
                } else {
                    et_user_name.setError(null);
                }


                String num = "[1][34578]\\d{9}";
                if (number.isEmpty() || !number.matches(num)) {
                    et_user_number.setError("请输入一个有效的手机号");
                    valid = false;
                } else {
                    et_user_number.setError(null);
                }

                if (psw.isEmpty() || psw.length() < 4 || psw.length() > 10) {
                    et_psw.setError("请输入4到10位密码");
                    valid = false;
                } else {
                    et_psw.setError(null);
                }

                if (pswagain.isEmpty() || !(pswagain.equals(psw))) {
                    et_psw_again.setError("两次输入密码不一致");
                    valid = false;
                } else {
                    et_psw_again.setError(null);
                }

                if(verify.isEmpty() ){
                    et_verify.setError("请输入验证码");
                    valid = false;
                }else{
                    et_verify.setError(null);
                }

                Log.i("htht", "返回之前：valid ="+valid+"线程名       "+Thread.currentThread().getName());
                return valid;
            }

            /**
             * 获取控件中的字符串
             */
            /*private void getEditString() {
                userName = et_user_name.getText().toString().trim();
                psw = et_psw.getText().toString().trim();
                pswAgain = et_psw_again.getText().toString().trim();
            }*/

            /**
             * 从SharedPreferences中读取输入的用户名，判断SharedPreferences中是否有此用户名
             */
           /* private boolean isExistUserName(String userName) {
                boolean has_userName = false;
                //mode_private SharedPreferences sp = getSharedPreferences( );
                // "loginInfo", MODE_PRIVATE
                SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
                //获取密码
                String spPsw = sp.getString(userName, "");//传入用户名获取密码
                //如果密码不为空则确实保存过这个用户名
                if (!TextUtils.isEmpty(spPsw)) {
                    has_userName = true;
                }
                return has_userName;
            }*/

            /**
             * 保存账号和密码到SharedPreferences中SharedPreferences
             */
            /*private void saveRegisterInfo(String userName, String psw) {
                //String md5Psw = MD5Utils.md5(psw);//把密码用MD5加密
                //loginInfo表示文件名, mode_private SharedPreferences sp = getSharedPreferences( );
                SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
                //获取编辑器， SharedPreferences.Editor  editor -> sp.edit();
                SharedPreferences.Editor editor = sp.edit();
                //以用户名为key，密码为value保存在SharedPreferences中
                //key,value,如键值对，editor.putString(用户名，密码）;
                editor.putString(userName, psw);
                //提交修改 editor.commit();
                editor.commit();
            }*/
        });
    }
}

