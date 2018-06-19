package com.yulin.common.bar;

import android.widget.ImageView;


/**
 * 图片菜单项
 */
public class BarMenuImgItem extends BarMenuItem {

    private int mItemImg = 0;

    /**
     * 图片菜单项构造函数·
     *
     * @param id    菜单项id
     * @param resId 图片资源id
     */
    public BarMenuImgItem(int id, int resId) {
        mItemId = id;
        mItemImg = resId;
    }

    public void setItemImg(int resId) {
        mItemImg = resId;
    }

    /**
     * 获取图片资源
     *
     * @return
     */
    public int getItemImg() {
        return mItemImg;
    }

    public ImageView getItemView() {
        return (ImageView) mItemView;
    }
}
