package com.yulin.common.helper;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.Pair;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yulin.common.R;

/**
 * Created by liu_lei on 2017/7/10.
 */

public class ImageHelper {

    // 图片显示形状
    public static final int SHAPE_NONE = 0;
    public static final int SHAPE_CIRCLE = 1;
    public static final int SHAPE_ROUND = 2;    // 圆角

    // 加载失败时显示的图片
    public static final int TYPE_NONE = 0;
    public static final int TYPE_AVATAR = 1;

    /**
     * 不指定形状和默认图
     * */
    public static void display(ImageView imageView, String url) {
        display(imageView, url, TYPE_NONE, SHAPE_NONE);
    }

    /**
     * @param holderType 默认图片
     * @param shapeType 圆形、圆角、正常
     * */
    public static void display(ImageView imageView, String url, int holderType, int shapeType) {
        Context context = imageView.getContext();
        Pair<Integer, Integer> holderPair = getPlaceHolder(holderType);

        switch (shapeType) {
            case SHAPE_NONE:
                try {
                    Glide.with(context).load(url)
//                            .placeholder(holderPair.first)
//                            .error(holderPair.second)
//                            .crossFade()
                            .into(imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case SHAPE_CIRCLE:
                try {
                    Glide.with(context).load(url)
//                            .transform(new GlideCircleTransform(context))
//                            .placeholder(holderPair.first)
//                            .error(holderPair.second)
//                            .crossFade()
                            .into(imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case SHAPE_ROUND:
                try {
                    // TODO: 这个glide是否还能继续使用
                    Glide.with(context).load(url)
//                            .placeholder(holderPair.first)
//                            .error(holderPair.second)
//                            .transform(new GlideRoundTransform(context, 5))
//                            .crossFade()
                            .into(imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private static Pair<Integer, Integer> getPlaceHolder(int holderType) {
        @DrawableRes
        int emptyImg;
        @DrawableRes
        int errorImg;

        switch (holderType) {
            case TYPE_AVATAR:
                emptyImg = R.drawable.ic_check_white_48dp;
                errorImg = R.drawable.ic_check_white_48dp;
                break;
            default:
                emptyImg = R.drawable.ic_check_white_48dp;
                errorImg = R.drawable.ic_check_white_48dp;
                break;
        }

        return Pair.create(emptyImg, errorImg);
    }

}
