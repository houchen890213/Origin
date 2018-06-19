package com.yulin.frame.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.yulin.origin.BR;

/**
 * Created by liu_lei on 2017/5/22.
 *
 * 存储用户个人信息
 * 内存中只存储一份
 */

public class UserInfo extends BaseObservable {

    private String userName = "";
    private String token = "123";
    private boolean isLogin = false;

    @Bindable
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        notifyPropertyChanged(BR.userName);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Bindable
    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
        notifyPropertyChanged(BR.login);
    }

}
