package com.yulin.chart.layers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.yulin.chart.ChartView;

public abstract class ChartLayer {

    // 标志左右移动时的状态
    static final int MOVE_STATE_NONE = 0;         //无数据，不操作
    static final int MOVE_STATE_LEFT_NONE = 1;    //最早的K线，不能再左移
    static final int MOVE_STATE_NORMAL = 2;       //正常的，还可以左右移动
    static final int MOVE_STATE_RIGHT_NONE = 3;   //最新的K线，不能再右移

    // 是否显示上下左右中的边框线
    private static final int SIDE_LEFT_V = 1;
    private static final int SIDE_MIDDLE_V = 2;
    private static final int SIDE_RIGHT_V = 4;
    private static final int SIDE_TOP_H = 8;
    private static final int SIDE_BOTTOM_H = 16;

    private Paint mPaint = new Paint();
    protected int mColor = Color.BLACK;

    protected float mLeft = 0;
    protected float mRight = 0;
    protected float mTop = 0;
    protected float mBottom = 0;

    protected float mPaddingLeft = 0;
    protected float mPaddingTop = 0;
    protected float mPaddingRight = 0;
    protected float mPaddingBottom = 0;

    private int mBorderColor = Color.GRAY;
    private int mBorderWidth = 1;
    private boolean mIsShowBorder = false;

    // 该layer在父layer垂直空间所占的百分比
    private float mHeightPercent = 0;

    private ChartView mChartView = null;

    // 网络线
    private boolean mIsShowHGrid = false;
    private boolean mIsShowVGrid = false;
    private int mHLineNum = 0;
    private int mVLineNum = 0;
    private boolean mMiddleHLineIsFull = false;
    private boolean mGridLineIsDash = true;

    private boolean mIsShowHPaddingLine = false;

    private boolean mIgnoreParentPadding = false;

    private boolean mIsShow = true;
    private String mTag = null;
    private int mShowSide = 0;

    public Paint getPaint() {
        return mPaint;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public void showHGrid(int hGridNum) {
        mHLineNum = hGridNum;
        mIsShowHGrid = hGridNum > 0;
    }

    public void showVGrid(int vGridNum) {
        mVLineNum = vGridNum;
        mIsShowVGrid = vGridNum > 0;
    }

    public int getHLineCount() {
        return mHLineNum;
    }

    public int getVLineCount() {
        return mVLineNum;
    }

    public boolean isShowHGrid() {
        return mIsShowHGrid;
    }

    public boolean isShowVGrid() {
        return mIsShowVGrid;
    }

    public void setMiddleLineIsFull(boolean bFull) {
        mMiddleHLineIsFull = bFull;
    }

    public boolean getMiddleHLineIsFull() {
        return mMiddleHLineIsFull;
    }

    public boolean isGridLineIsDash() {
        return mGridLineIsDash;
    }

    public void setGridLineIsDash(boolean gridLineIsDash) {
        this.mGridLineIsDash = gridLineIsDash;
    }

    public void setShowHPaddingLine(boolean bShow) {
        mIsShowHPaddingLine = bShow;
    }

    public boolean isShowHPaddingLine() {
        return mIsShowHPaddingLine;
    }

    public void setIgnoreParentPadding(boolean b) {
        mIgnoreParentPadding = b;
    }

    public boolean getIgnoreParentPadding() {
        return mIgnoreParentPadding;
    }

    public abstract RectF prepareBeforeDraw(RectF rect);

    public RectF prepareBeforeDrawFixed(RectF rect) {
        return null;
    }

    public float[] calMinAndMaxValue() {
        return null;
    }

    public abstract void doDraw(Canvas canvas);

    public abstract void rePrepareWhenDrawing(RectF rect);

    public void layout(float left, float top, float right, float bottom) {
        mLeft = left;
        mRight = right;
        mTop = top;
        mBottom = bottom;
    }

    public void setPaddings(float left, float top, float right, float bottom) {
        mPaddingLeft = left;
        mPaddingTop = top;
        mPaddingRight = right;
        mPaddingBottom = bottom;
    }

    public void setShowBorder(boolean b) {
        mIsShowBorder = b;
    }

    public boolean getIsShowBorder() {
        return mIsShowBorder;
    }

    public void setBorderColor(int color) {
        mBorderColor = color;
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderWidth(int width) {
        mBorderWidth = width;
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public float getLeft() {
        return mLeft;
    }

    public float getTop() {
        return mTop;
    }

    public float getRight() {
        return mRight;
    }

    public float getBottom() {
        return mBottom;
    }

    public float getWidth() {
        return mRight - mLeft;
    }

    public float getHeight() {
        return mBottom - mTop;
    }

    public RectF getRectF() {
        return new RectF(mLeft, mTop, mRight, mBottom);
    }

    public RectF getValidRegionRectF() {
        return new RectF(mLeft + mPaddingLeft, mTop + mPaddingTop, mRight - mPaddingRight, mBottom - mPaddingBottom);
    }

    public void setHeightPercent(float percent) {
        mHeightPercent = percent;
    }

    public float getHeightPercent() {
        return mHeightPercent;
    }

    public void resetData() {

    }

    /**
     * 移动起始点
     *
     * @param offset
     * @return:查看：MOVE_STATE_NONE,MOVE_STATE_LEFT_NONE，MOVE_STATE_NOMAL，MOVE_STATE_RIGHT_NONE
     */
    public int moveStartPos(int offset) {
        return MOVE_STATE_NONE;
    }

    /**
     * 设置起始点
     *
     * @param pos
     * @return 查看：MOVE_STATE_NONE,MOVE_STATE_LEFT_NONE，MOVE_STATE_NORMAL，MOVE_STATE_RIGHT_NONE
     */
    public int setStartPos(int pos) {
        return MOVE_STATE_NONE;
    }

    public void setMaxCount(int count) {
    }


    public boolean onActionShowPress(MotionEvent event) {
        return false;
    }

    public void onActionUp(MotionEvent event) {

    }

    public boolean onActionMove(MotionEvent event) {
        return false;
    }

    /**
     * 需求接收手势的layer需重载此方法,并返回true
     * @param event
     * @return
     */
    public boolean onActionDown(MotionEvent event) {
        return false;
    }

    public boolean onActionDoubleTap(MotionEvent event) {
        return false;
    }

    public boolean onActionSingleTap(MotionEvent event) {
        return false;
    }

    public void setChartView(ChartView view) {
        mChartView = view;
    }

    public ChartView getChartView() {
        return mChartView;
    }

    public RectF getCanvasRect() {
        if (mChartView != null)
            return mChartView.getCanvasRect();
        return null;
    }

    public void repaint() {
        if (mChartView != null) {
            mChartView.invalidate();
        }
    }

    public void show(boolean isShow) {
        mIsShow = isShow;
    }

    public boolean isShow() {
        return mIsShow;
    }

    //滑动事件监听
    protected OnActionListener mActionListener = null;

    public static interface OnActionListener {
        public boolean onDown(MotionEvent event);
        public boolean onMove(int pos, MotionEvent event);
        public boolean onUp(MotionEvent event);
        public boolean onLongPressed(int pos, MotionEvent event);
    }

    public void setOnActionListener(OnActionListener listener) {
        mActionListener = listener;
    }


    //单双击事件监听
    protected OnActionTapListener mActionTapListener = null;

    public static interface OnActionTapListener {
        public boolean onActionSingleTap(MotionEvent event);
        public boolean onActionDoubleTap(MotionEvent event);
    }

    public void setOnActionTapListener(OnActionTapListener listener) {
        mActionTapListener = listener;
    }


    //绘制监听
    protected OnDrawingListener mDrawingListener = null;

    public static interface OnDrawingListener {
        public void onDrawing(Paint paint, int pos);
    }

    public void setOnDrawingListener(OnDrawingListener listener) {
        mDrawingListener = listener;
    }

    public void setTag(String tag) {
        mTag = tag;
    }

    public String getTag() {
        return mTag;
    }

    public void setMaxValue(float val) {

    }

    public void setMinValue(float val) {

    }

    public void setShowSide(int side) {
        mShowSide = side;
    }

    public boolean isShowSideLeft() {
        return (mShowSide & SIDE_LEFT_V) == SIDE_LEFT_V;
    }

    public boolean isShowSideTop() {
        return (mShowSide & SIDE_TOP_H) == SIDE_TOP_H;
    }

    public boolean isShowSideRight() {
        return (mShowSide & SIDE_RIGHT_V) == SIDE_RIGHT_V;
    }

    public boolean isShowSideBottom() {
        return (mShowSide & SIDE_BOTTOM_H) == SIDE_BOTTOM_H;
    }

    public void onDrawSide(Canvas canvas, Paint paint) {
        //left
        float halfLineW = paint.getStrokeWidth() / 2;
        if (isShowSideLeft()) {
            float left = getRectF().left;
            if (left - halfLineW < 0) {//防止画边界线过界得情况
                left = halfLineW / 2;
            }
            canvas.drawLine(left, getRectF().top, left, getRectF().bottom, paint);
        }

        //top
        if (isShowSideTop()) {
            float top = getRectF().top;
            if (top - halfLineW < 0) {
                top = halfLineW;
            }
            canvas.drawLine(getRectF().left, top, getRectF().right, top, paint);
        }

        //right
        if (isShowSideRight()) {
            float right = getRectF().right;
            if (right + halfLineW > canvas.getWidth()) {
                right = canvas.getWidth() - halfLineW;
            }
            canvas.drawLine(right, getRectF().top, right, getRectF().bottom, paint);
        }

        //bottom
        if (isShowSideBottom()) {
            float bottom = getRectF().bottom;
            if (bottom + halfLineW > canvas.getHeight()) {
                bottom = canvas.getHeight() - halfLineW;
            }
            canvas.drawLine(getRectF().left, bottom, getRectF().right, bottom, paint);
        }
    }

    /**
     * group 或 stacklayer等非数据绘制layer 返回-1
     *
     * @return
     */
    public int getValueCount() {
        return -1;
    }

    public void setEmptyData(int pos, int count) {

    }

}
