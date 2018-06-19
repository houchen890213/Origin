package com.yulin.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.yulin.common.R;

/**
 * Created by liu lei on 2017/5/16.
 * 实心圆形
 *
 * 可配置圆形颜色、边框宽度、边框颜色
 * 圆形的半径取宽度和高度的小者
 * 边框也为圆形，当边框较宽时，可作为圆环
 */

public class RoundView extends View {

    private static final int DEFAULT_BG_COLOR     = Color.GRAY;
    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_BORDER_COLOR = Color.BLUE;

    private int mOuterRadius;    // 边框外侧半径
    private int mInnerRadius;    // 边框内侧半径
    private int mCenterX;        // 圆心x坐标
    private int mCenterY;        // 圆心y坐标
    private int mBorderWidth;    // 边框宽度

    private Paint mOuterPaint;   // 外层边框画笔
    private Paint mInnerPaint;   // 内层圆形画笔

    public RoundView(Context context) {
        this(context, null);
    }

    public RoundView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mOuterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.RoundView, 0, 0);

        try {
            int solidColor = typedArray.getColor(R.styleable.RoundView_round_solidColor, DEFAULT_BG_COLOR);
            int borderColor = typedArray.getColor(R.styleable.RoundView_round_borderColor, DEFAULT_BORDER_COLOR);
            mBorderWidth = typedArray.getDimensionPixelSize(R.styleable.RoundView_round_borderWidth, DEFAULT_BORDER_WIDTH);

            mInnerPaint.setColor(solidColor);
            mOuterPaint.setColor(borderColor);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = View.resolveSize(getDesiredWidth(), widthMeasureSpec);
        int height = View.resolveSize(getDesiredHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);

        updateCenterValue(width / 2, height / 2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        updateCenterValue(w / 2, h / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(mCenterX, mCenterY, mOuterRadius, mOuterPaint);
        canvas.drawCircle(mCenterX, mCenterY, mInnerRadius, mInnerPaint);
    }

    public void setSolidColor(int color) {
        mInnerPaint.setColor(color);
        invalidate();
    }

    public void setBorderColor(int color) {
        mOuterPaint.setColor(color);
        invalidate();
    }

    private int getDesiredWidth() {
        return getPaddingLeft() + getPaddingRight();
    }

    private int getDesiredHeight() {
        return getPaddingTop() + getPaddingBottom();
    }

    private void updateCenterValue(int x, int y) {
        mOuterRadius = getRadius(x, y);
        mInnerRadius = mOuterRadius - mBorderWidth;
        mCenterX = x;
        mCenterY = y;
    }

    private int getRadius(int x, int y) {
        int xRadius = x - (getPaddingLeft() + getPaddingRight()) / 2;
        int yRadius = y - (getPaddingTop() + getPaddingBottom()) / 2;

        // 取长、宽一半的小者为半径
        return Math.min(xRadius, yRadius);
    }

}
