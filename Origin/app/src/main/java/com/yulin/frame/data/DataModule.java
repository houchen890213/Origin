package com.yulin.frame.data;

/**
 * Created by liu_lei on 2017/5/22.
 *
 * 缓存数据统一入口
 */

public enum DataModule {

    INSTANCE;

    private UserInfo mUserInfo;

    public static DataModule getInstance() {
        return INSTANCE;
    }

    public UserInfo getUserInfo() {
        if (mUserInfo == null) {
            mUserInfo = new UserInfo();
        }

        return mUserInfo;
    }

    /**
     * 切换用户时，需要清空缓存的用户数据
     * 再次getUserInfo时，会再创建一份新的UserInfo
     * */
    public void clearUserInfo() {
        mUserInfo = null;
    }

}
