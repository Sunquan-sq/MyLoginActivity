package com.example.sunquan.myapplication12;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

public class UserFarm extends BmobObject implements Serializable {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BmobRelation getFarms() {
        return farms;
    }

    public void setFarms(BmobRelation farms) {
        this.farms = farms;
    }

    private BmobRelation farms;
}
