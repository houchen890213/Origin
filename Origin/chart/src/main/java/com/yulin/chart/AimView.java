package com.yulin.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class AimView extends FrameLayout {

    private RectF mChartAreaRectF = null;

    private Paint mPaint = new Paint();

    private boolean mHasLayouted = false;
    protected int mColor = Color.BLACK;
    private float mLeft = 0;
    private float mTop = 0;
    private float mRight = 0;
    private float mBottom = 0;


    public AimView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AimView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setWillNotDraw(false);

        setFocusable(false);
        setLongClickable(false);
        setClickable(false);
        setFocusableInTouchMode(false);


    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        if (!mIsAimOn || mAimPointf == null) {
            return;
        }
        super.dispatchDraw(canvas);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        prepareBeforeDraw();

        if (!mIsAimOn || mAimPointf == null) {
            return;
        }
        super.onDraw(canvas);
        drawAimLine(canvas);
    }


    public void forceAdjust() {
        mLeft = getPaddingLeft();
        mTop = getPaddingTop();
        mRight = getMeasuredWidth() - getPaddingRight();
        mBottom = getMeasuredHeight() - getPaddingBottom();
        mChartAreaRectF = new RectF(mLeft, mTop, mRight, mBottom);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //        mTvCoordinates.clear();
        //        for (int i = 0; i < getChildCount(); i++) {
        //            TextView tv = (TextView) getChildAt(i);
        //            System.out.println("sky aim viewId:" + tv.getId());
        //            addCoordinate(tv.getId(), tv);
        //        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHasLayouted = true;
        forceAdjust();


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        forceAdjust();
    }


    public boolean hasLayouted() {
        return mHasLayouted;
    }


    PointF mAimPointf = null;

    private float mStrokeWidth = 1;
    private boolean mIsShowHLine = false;
    private boolean mIsShowVLine = false;
    private boolean mIsAimOn = false;
    private float mAimPointRadius = 0;


    public void prepareBeforeDraw() {
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public void drawAimLine(Canvas canvas) {

        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(mStrokeWidth);

        if (mIsShowHLine) {
            if (mAimPointf.y > mTop && mAimPointf.y < mBottom) {
                canvas.drawLine(mLeft, mAimPointf.y, mRight, mAimPointf.y, mPaint);
            }
        }

        if (mIsShowVLine) {
            if (mAimPointf.x > mLeft && mAimPointf.x < mRight) {
                canvas.drawLine(mAimPointf.x, mTop, mAimPointf.x, mBottom, mPaint);
            }
        }

        if (mAimPointRadius > 0) {
            if (mAimPointf.y > mTop && mAimPointf.y < mBottom && mAimPointf.x > mLeft && mAimPointf.x < mRight) {
                canvas.drawCircle(mAimPointf.x, mAimPointf.y, mAimPointRadius, mPaint);
            }
        }
    }


    public void setStrokeWidth(int width) {
        mStrokeWidth = width;
    }

    public void setAimPointf(PointF pointf) {
        mAimPointf = pointf;
    }

    public void setIsShowHLine(boolean b) {
        mIsShowHLine = b;
    }

    public void setIsShowVLine(boolean b) {
        mIsShowVLine = b;
    }

    public void switchAim(boolean isOn) {
        mIsAimOn = isOn;
    }

    public void setAimPointRadius(float r) {
        mAimPointRadius = r;
    }

    private Map<Integer, TextView> mTvCoordinates = new HashMap<>();

    //    private void addCoordinate(int id, TextView tv) {
    //        mTvCoordinates.put(id, tv);
    //    }


    public void hideCoordinateByName(int id, boolean hide) {
        TextView tv = (TextView) findViewById(id);
        if (tv == null)
            return;
        tv.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    /**
     * x , y为margin值,不使用时填写-1
     *
     * @param id
     * @param xLeft
     * @param xRight
     * @param yTop
     * @param yBottom
     * @param backgroundResId
     * @param textColor
     * @param lable
     */
    public void setCoordinate(int id, int xLeft, int xRight, int yTop, int yBottom, int backgroundResId, int textColor, String lable) {
        TextView tv = (TextView) findViewById(id);
        if (tv == null)
            return;

        FrameLayout.LayoutParams param = (LayoutParams) tv.getLayoutParams();
        if (xLeft > 0)
            param.leftMargin = xLeft;
        if (xRight > 0)
            param.rightMargin = xRight;
        if (yTop > 0)
            param.topMargin = yTop;
        if (yBottom > 0)
            param.bottomMargin = yBottom;

        tv.setLayoutParams(param);
        if (backgroundResId != -1)
            tv.setBackgroundResource(backgroundResId);
        tv.setTextColor(textColor);
        tv.setText(lable);
    }

    public void setCoordinateHtml(int id, int xLeft, int xRight, int yTop, int yBottom, int backgroundResId, String lable) {
        TextView tv = (TextView) findViewById(id);
        if (tv == null)
            return;

        FrameLayout.LayoutParams param = (LayoutParams) tv.getLayoutParams();
        if (xLeft > 0)
            param.leftMargin = xLeft;
        if (xRight > 0)
            param.rightMargin = xRight;
        if (yTop > 0)
            param.topMargin = yTop;
        if (yBottom > 0)
            param.bottomMargin = yBottom;

        tv.setLayoutParams(param);
        if (backgroundResId != -1)
            tv.setBackgroundResource(backgroundResId);
        Spanned spannedItem = Html.fromHtml(lable);
        tv.setText(spannedItem);
    }

}
