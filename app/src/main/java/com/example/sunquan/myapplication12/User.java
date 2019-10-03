package com.example.sunquan.myapplication12;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;


public class User extends BmobUser implements Serializable {
    private Boolean sex;
    private BmobFile userIcon;
    private String userName;
    private Integer age;

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public BmobFile getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(BmobFile userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
