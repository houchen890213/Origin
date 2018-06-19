package com.yulin.chart.layers;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * 折线层
 * 
 * @author Administrator
 *
 */
public class ReferenceLineLayer extends ChartLayer {

    private float mReferenceValue = 0;



    private float mMinValue = 0;
    private float mMaxValue = 0;

    private float mPosPerValue = 0;// 单位值所代表的距离

    private int mStrokeWidth = 2;

    private float mFloorValue = Integer.MIN_VALUE;// 允许的最小值
    private boolean mIsIncludeFloor = true;

    @Override
    public RectF prepareBeforeDraw(RectF rect) {
        // TODO Auto-generated method stub
        getPaint().setColor(mColor);
        getPaint().setStrokeWidth(mStrokeWidth);
        getPaint().setAntiAlias(true);
        mLeft = rect.left;
        mRight = rect.right;
        mTop = rect.top;
        mBottom = rect.bottom;
        return new RectF(mLeft, mTop, mRight, mBottom);
    }

    public float[] calMinAndMaxValue() {
        mMaxValue = mReferenceValue;
        mMinValue = mReferenceValue;
        return new float[] {mMaxValue, mMinValue};
    }

    @Override
    public void doDraw(Canvas canvas) {
        if (mIsIncludeFloor) {
            if (mDrawingListener != null) {
                mDrawingListener.onDrawing(getPaint(), 0);
            }
            float y = value2Y(mReferenceValue);
            if (mReferenceValue < mFloorValue) {
                y = value2Y(mFloorValue);
            }
            float x1 = mLeft + mPaddingLeft;
            float x2 = mRight = mPaddingRight;
            canvas.drawLine(x1, y, x2, y, getPaint());


        } else {
            if (mDrawingListener != null) {
                mDrawingListener.onDrawing(getPaint(), 0);
            }
            float y = value2Y(mReferenceValue);
            float x1 = mLeft + mPaddingLeft;
            float x2 = mRight = mPaddingRight;
            canvas.drawLine(x1, y, x2, y, getPaint());
        }
    }



    public void setStrokeWidth(int width) {
        mStrokeWidth = width;
    }

    public void setFloorValue(float val, boolean isInclude) {
        mFloorValue = val;
        mIsIncludeFloor = isInclude;
    }

    private float value2Y(float val) {
        return mBottom - mPosPerValue * (val - mMinValue) - mPaddingBottom;
    }

    public float getReferenceValue() {
        return mReferenceValue;
    }

    @Override
    public void rePrepareWhenDrawing(RectF rect) {

        float totalHeight = mBottom - mTop - mPaddingTop - mPaddingBottom;
        mPosPerValue = totalHeight / (mMaxValue - mMinValue);
    }

    public void setValue(float val) {
        mReferenceValue = val;
    }



    public float getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(float val) {
        mMaxValue = val;
    }

    public float getMinValue() {
        return mMinValue;
    }

    public void setMinValue(float val) {
        mMinValue = val;
    }

    @Override
    public void resetData() {
        clear();

    }

    public void clear() {
        mReferenceValue = 0;
        mMinValue = 0;
        mMaxValue = 0;
    }

}
