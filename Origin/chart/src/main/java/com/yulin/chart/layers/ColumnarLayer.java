package com.yulin.chart.layers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 柱状层
 *
 * @author Administrator
 */
public class ColumnarLayer extends ChartLayer {

    private static final int HLFLAG_PADDING_H = 14;
    private static final int HLFLAG_PADDING_V = 7;
    private static final int HLFLAG_LINKLINE_LEN = 65;

    private List<ColumnarAtom> mLstValues = new ArrayList<ColumnarAtom>();
    private int mMaxCount = 0;
    private float mColumnarWidth = 0;
    private float mSpace = 0;

    private float mPosPerValue = 0;// 单位值所代表的距离

    private float mMinValue = 0;
    private float mMaxValue = 0;
    private int mMinValuePos = -1;
    private int mMaxValuePos = -1;

    private int mCurrPos = -1;
    private float mStrokeWidth = 1;
    private float mLineWidth = 1;

    private int mLineColor = Color.BLACK;

    private int mStartPos = 0;

    private boolean mIsLine = false;

    private int mHasDataState = 0;

    private boolean mShowHighLowFlag = false;
    private int mHighLowFlagTxtColor = Color.BLACK;
    private int mHighLowFlagTxtSize = 18;
    private int mHighLowFlagBorderColor = Color.BLACK;
    private int mHighLowFlagBgColor = Color.WHITE;
    private int mHighLowFlagBorderWidth = 1;

    private onDrawHighLowListener mDrawHighLowListener;

    DrawInfoWriteOutCallBack mWriteOutCallback = null;
    private boolean mNeedPreCalcCoulumnar = false;

    private String lineName;
    /**
     * 跳空缺口
     */
    private KLineGap mKLineGap = null;
    private boolean mShowGap = false;
    private int mGapColor = Color.BLACK;

    public void setWriteOutCallback(DrawInfoWriteOutCallBack writeOutCallback) {
        this.mWriteOutCallback = writeOutCallback;
    }

    @Override
    public RectF prepareBeforeDraw(RectF rect) {
        mLeft = rect.left;
        mRight = rect.right;
        mTop = rect.top;
        mBottom = rect.bottom;

        getPaint().setColor(mColor);
        getPaint().setStrokeWidth(mStrokeWidth);
        getPaint().setAntiAlias(true);
        getPaint().setStyle(Style.FILL);

        return new RectF(mLeft, mTop, mRight, mBottom);
    }

    public float[] calMinAndMaxValue() {
        // int size = mLstValues.size();
        int size = mStartPos + mMaxCount > mLstValues.size() ? mLstValues.size() : mStartPos + mMaxCount;
        mMaxValuePos = mStartPos;
        mMinValuePos = mStartPos;
        if (isLine()) {
            for (int i = mStartPos; i < size; i++) {
                ColumnarAtom value = mLstValues.get(i);
                if (i == mStartPos) {
                    mMaxValue = value.mClose;
                    mMinValue = value.mClose;
                } else {
                    if (mMaxValue < value.mClose) {
                        mMaxValue = value.mClose;
                        mMaxValuePos = i;
                    }
                    if (mMinValue > value.mClose) {
                        mMinValue = value.mClose;
                        mMinValuePos = i;
                    }
                }
            }
        } else {
            for (int i = mStartPos; i < size; i++) {
                ColumnarAtom value = mLstValues.get(i);
                if (i == mStartPos) {
                    mMaxValue = value.mHigh;

                    if (mMaxValue < value.mClose) {
                        mMaxValue = value.mClose;
                    }
                    if (mMaxValue < value.mOpen) {
                        mMaxValue = value.mOpen;
                    }
                    if (mMaxValue < value.mLow) {
                        mMaxValue = value.mLow;
                    }

                    mMinValue = value.mLow;
                    if (mMinValue > value.mClose) {
                        mMinValue = value.mClose;
                    }
                    if (mMinValue > value.mOpen) {
                        mMinValue = value.mOpen;
                    }
                    if (mMinValue > value.mHigh) {
                        mMinValue = value.mHigh;
                    }
                } else {
                    if (mMaxValue < value.mHigh) {
                        mMaxValue = value.mHigh;
                        mMaxValuePos = i;
                    }
                    if (mMaxValue < value.mClose) {
                        mMaxValue = value.mClose;
                        mMaxValuePos = i;
                    }
                    if (mMaxValue < value.mOpen) {
                        mMaxValue = value.mOpen;
                        mMaxValuePos = i;
                    }
                    if (mMaxValue < value.mLow) {
                        mMaxValue = value.mLow;
                        mMaxValuePos = i;
                    }

                    if (mMinValue > value.mLow) {
                        mMinValue = value.mLow;
                        mMinValuePos = i;
                    }
                    if (mMinValue > value.mClose) {
                        mMinValue = value.mClose;
                        mMinValuePos = i;
                    }
                    if (mMinValue > value.mOpen) {
                        mMinValue = value.mOpen;
                        mMinValuePos = i;
                    }
                    if (mMinValue > value.mHigh) {
                        mMinValue = value.mHigh;
                        mMinValuePos = i;
                    }
                }
            }
        }
        if (mLstValues.size() == 0) {
            return null;
        } else {
            return new float[]{mMinValue, mMaxValue};
        }
    }

    float value2Y(float val) {
        return mBottom - mPosPerValue * (val - mMinValue) - mPaddingBottom;
    }

    private float y2Value(float y) {
        if (mPosPerValue > 0)
            return (mBottom - mPaddingBottom - y) / mPosPerValue + mMinValue;
        return 0;
    }

    @Override
    public void doDraw(Canvas canvas) {
        int count = 0;

        if (mWriteOutCallback != null) {
            mWriteOutCallback.clear();
        }

        for (int i = mStartPos; i < mLstValues.size(); i++) {
            if (i - mStartPos >= mMaxCount) {
                break;
            }

            if (mIsLine) {
                getPaint().setStrokeWidth(mLineWidth);
                getPaint().setColor(mLineColor);
                if (count > 0 && i > 0) {
                    ColumnarAtom a1 = mLstValues.get(i - 1);
                    ColumnarAtom a2 = mLstValues.get(i);
                    canvas.drawLine(pos2X(count - 1), value2Y(a1.mClose), pos2X(count), value2Y(a2.mClose), getPaint());
                }
            } else {
                getPaint().setStrokeWidth(mStrokeWidth);
                if (mDrawingListener != null) {
                    mDrawingListener.onDrawing(getPaint(), i);
                }
                drawOneColumnar(i, pos2X(count), canvas, mLstValues.get(i));
            }
            count++;
        }
    }

    public void setStrokeWidth(int width) {
        mStrokeWidth = width;
    }

    public void setCheckWidth(float width) {
    }

    public void setLineColor(int color) {
        mLineColor = color;
    }

    public void setLineWidth(float width) {
        mLineWidth = width;
    }

    private float pos2X(int index) {
        return mLeft + mColumnarWidth / 2 + mPaddingLeft + (mColumnarWidth + mSpace) * index;
    }

    private int x2Pos(float x) {
        return (int) ((x - mLeft - mColumnarWidth / 2 - mPaddingLeft) / (mColumnarWidth + mSpace));
    }

    @Override
    public void rePrepareWhenDrawing(RectF rect) {
        float totalWidth = mRight - mLeft - mColumnarWidth * mMaxCount - mPaddingRight - mPaddingLeft;
        mSpace = totalWidth / (mMaxCount - 1);

        float totalHeight = mBottom - mTop - mPaddingTop - mPaddingBottom;
        mPosPerValue = totalHeight / (mMaxValue - mMinValue);

        // 预计算
        if (mNeedPreCalcCoulumnar) {
            preCalcColumnar();
        }

    }

    protected void preCalcColumnar() {
        if (mWriteOutCallback != null) {
            mWriteOutCallback.clear();

            int count = 0;

            for (int i = mStartPos; i < mLstValues.size(); i++) {
                if (i - mStartPos >= mMaxCount) {
                    break;
                }

                if (!mIsLine) {
                    getPaint().setStrokeWidth(mStrokeWidth);
                    if (mDrawingListener != null) {
                        mDrawingListener.onDrawing(getPaint(), i);
                    }

                    mWriteOutCallback.out(pos2X(count), value2Y(mLstValues.get(i).mHigh), value2Y(mLstValues.get(i).mLow), (Integer) mLstValues.get(i).mTag, mColumnarWidth);
                }
                count++;
            }
        }

    }

    protected void drawOneColumnar(int pos, float centerX, Canvas canvas, ColumnarAtom value) {
        float left = centerX - mColumnarWidth / 2;
        float right = centerX + mColumnarWidth / 2;
        if (value.mOpen > value.mClose) {
            if (value.mHigh != value.mOpen) {
                canvas.drawLine(centerX, value2Y(value.mHigh), centerX, value2Y(value.mOpen), getPaint());
            }
            if (value.mClose != value.mLow) {
                canvas.drawLine(centerX, value2Y(value.mClose), centerX, value2Y(value.mLow), getPaint());
            }
            float openY = value2Y(value.mOpen);
            float closeY = value2Y(value.mClose);
            canvas.drawRect(new RectF(left, openY, right, closeY), getPaint());
            if (closeY - openY <= mStrokeWidth) {
                canvas.drawLine(left, closeY, right, closeY, getPaint());
            }

        } else if (value.mOpen < value.mClose) {
            if (value.mHigh != value.mClose) {
                canvas.drawLine(centerX, value2Y(value.mHigh), centerX, value2Y(value.mClose), getPaint());
            }
            if (value.mOpen != value.mLow) {
                canvas.drawLine(centerX, value2Y(value.mOpen), centerX, value2Y(value.mLow), getPaint());
            }
            canvas.drawRect(new RectF(left, value2Y(value.mClose), right, value2Y(value.mOpen)), getPaint());
            float openY = value2Y(value.mOpen);
            float closeY = value2Y(value.mClose);
            if (openY - closeY <= mStrokeWidth) {
                canvas.drawLine(left, closeY, right, closeY, getPaint());
            }
        } else {

            if (value.mHigh != value.mLow) {
                canvas.drawLine(centerX, value2Y(value.mHigh), centerX, value2Y(value.mLow), getPaint());
            }
            canvas.drawLine(left, value2Y(value.mClose), right, value2Y(value.mClose), getPaint());
        }

        //绘制缺口
        if (mShowGap && mKLineGap != null && mKLineGap.gapPosition == pos) {
            float tBigGapValue = mKLineGap.gapBigValue > mMaxValue ? mMaxValue : mKLineGap.gapBigValue;
            float tSmallGapValue = mKLineGap.gapSmallValue < mMinValue ? mMinValue : mKLineGap.gapSmallValue;
            getPaint().setColor(mGapColor);
            getPaint().setStyle(Style.FILL);
            canvas.drawRect(centerX, value2Y(tBigGapValue), mRight, value2Y(tSmallGapValue), getPaint());

        }


        //绘制最高最低flag
        //只有一根时不绘制最高最低
        if (getValueCount() > 1 && mShowHighLowFlag && mDrawHighLowListener != null && (pos == mMaxValuePos || pos == mMinValuePos)) {

            if (pos == mMaxValuePos) {
                if (pos - mStartPos < mMaxCount / 2) {
                    drawHighLowFlag(centerX, value2Y(value.mHigh) - 8, canvas, value.mHigh, 1);

                } else {
                    drawHighLowFlag(centerX, value2Y(value.mHigh) - 8, canvas, value.mHigh, -1);
                }
            }

            if (pos == mMinValuePos) {
                if (pos - mStartPos < mMaxCount / 2) {
                    drawHighLowFlag(centerX, value2Y(value.mLow) + 8, canvas, value.mLow, 1);
                } else {
                    drawHighLowFlag(centerX, value2Y(value.mLow) + 8, canvas, value.mLow, -1);
                }
            }

        }


        if (mWriteOutCallback != null) {
            mWriteOutCallback.out(centerX, value2Y(value.mHigh), value2Y(value.mLow), (Integer) value.mTag, mColumnarWidth);
        }
    }

    /**
     * @param x1        中心x坐标
     * @param y1        中心y坐标
     * @param canvas
     * @param direction -1:中心左方 ; 1:中心右方
     */
    protected void drawHighLowFlag(float x1, float y1, Canvas canvas, float value, int direction) {
        String tStr = mDrawHighLowListener.formatValue(value);
        Paint p = new Paint();
        p.setTextSize(mHighLowFlagTxtSize);
        float txtWidth = p.measureText(tStr);

        Paint.FontMetrics fm = getPaint().getFontMetrics();
        float txtHeight = (float) (Math.ceil(fm.descent - fm.ascent) + 2);
        float txtOffsetY = (fm.bottom - fm.top) / 2 - fm.bottom + 4;

        //画圆点
        p.setColor(mHighLowFlagBorderColor);
        p.setAntiAlias(true);
        p.setStyle(Style.FILL);
        canvas.drawCircle(x1, y1, 6, p);


        if (direction == -1) {
            //画横线连接线
            p.setStrokeWidth(mHighLowFlagBorderWidth);
            p.setColor(mHighLowFlagBorderColor);
            p.setStyle(Style.STROKE);
            float tx = x1 - HLFLAG_LINKLINE_LEN;
            canvas.drawLine(tx, y1, x1, y1, p);

            //画框的背景
            p.setColor(mHighLowFlagBgColor);
            p.setStyle(Style.FILL);
            canvas.drawRect(tx - txtWidth - 2 * HLFLAG_PADDING_H, y1 - txtHeight / 2 - HLFLAG_PADDING_V, tx, y1 + txtHeight / 2 + HLFLAG_PADDING_V, p);

            //画边框
            p.setStrokeWidth(mHighLowFlagBorderWidth);
            p.setColor(mHighLowFlagBorderColor);
            p.setStyle(Style.STROKE);
            canvas.drawRect(tx - txtWidth - 2 * HLFLAG_PADDING_H, y1 - txtHeight / 2 - HLFLAG_PADDING_V, tx, y1 + txtHeight / 2 + HLFLAG_PADDING_V, p);

            //画字
            p.setColor(mHighLowFlagTxtColor);
            p.setStyle(Style.FILL);
            canvas.drawText(tStr, tx - txtWidth - HLFLAG_PADDING_H, y1 + txtOffsetY, p);

        }

        if (direction == 1) {
            //画横线连接线
            p.setStrokeWidth(mHighLowFlagBorderWidth);
            p.setColor(mHighLowFlagBorderColor);
            p.setStyle(Style.STROKE);
            float tx = x1 + HLFLAG_LINKLINE_LEN;
            canvas.drawLine(x1, y1, tx, y1, p);

            //画框的背景
            p.setColor(mHighLowFlagBgColor);
            p.setStyle(Style.FILL);
            canvas.drawRect(tx, y1 - txtHeight / 2 - HLFLAG_PADDING_V, tx + txtWidth + 2 * HLFLAG_PADDING_H, y1 + txtHeight / 2 + HLFLAG_PADDING_V, p);

            //画边框
            p.setStrokeWidth(mHighLowFlagBorderWidth);
            p.setColor(mHighLowFlagBorderColor);
            p.setStyle(Style.STROKE);
            canvas.drawRect(tx, y1 - txtHeight / 2 - HLFLAG_PADDING_V, tx + txtWidth + 2 * HLFLAG_PADDING_H, y1 + txtHeight / 2 + HLFLAG_PADDING_V, p);

            //画字
            p.setColor(mHighLowFlagTxtColor);
            p.setStyle(Style.FILL);
            canvas.drawText(tStr, tx + HLFLAG_PADDING_H, y1 + txtOffsetY, p);
        }


    }

    public void setValue(int index, ColumnarAtom value) {
        if (index >= 0 && index <= mLstValues.size() - 1) {
            mLstValues.set(index, value);
        } else if (index == mLstValues.size()) {
            addValue(value);
        }
    }

    public void setLastValue(ColumnarAtom value) {
        if (mLstValues.size() > 0) {
            setValue(mLstValues.size() - 1, value);
        }
    }

    public void addValue(ColumnarAtom value) {
        mLstValues.add(value);
    }

    public void addAllValue(int pos, List<ColumnarAtom> list) {
        mLstValues.addAll(pos, list);
    }

    public void addValue(int location, ColumnarAtom value) {
        mLstValues.add(location, value);
    }

    public ColumnarAtom getValue(int pos) {
        return mLstValues.get(pos);
    }

    public void removeValue(int pos) {
        if (pos >= getValueCount()) {
            return;
        }

        mLstValues.remove(pos);
    }

    public int getValueCount() {
        return mLstValues.size();
    }

    public int getDisplayValueCount() {
        int count = getValueCount() - mStartPos < mMaxCount ? getValueCount() - mStartPos : mMaxCount;
        return count > 0 ? count : 0;
    }

    public ColumnarAtom getLastValue() {
        if (getValueCount() > 0) {
            return getValue(getValueCount() - 1);
        }
        return null;
    }

    public ColumnarAtom getDisplayLastValue(int offset) {
        if (offset > 0) {
            return null;
        }

        if (getDisplayValueCount() > 0) {
            return getValue(mStartPos + getDisplayValueCount() - 1 + offset);
        }
        return null;
    }

    public ColumnarAtom getDisplayLastValue() {
        return getDisplayLastValue(0);
    }

    public ColumnarAtom getFirstValue() {
        if (getValueCount() > 0) {
            return getValue(0);
        }
        return null;
    }

    public ColumnarAtom getDisplayFirstValue() {
        if (getValueCount() > mStartPos) {
            return getValue(mStartPos);
        }
        return null;
    }

    /**
     * @param pos
     * @return 反回x横坐标值, y纵坐标直接返回0值, 无话意义
     */
    public PointF getPointByPos(int pos) {
        PointF pointf = new PointF();
        int count = pos - mStartPos < 0 ? 0 : pos - mStartPos;
        pointf.x = pos2X(count);
        //        ColumnarAtom val = getValue(pos);
        //        pointf.y = value2Y(val.mClose);
        return pointf;
    }

    public int getStartPos() {
        return mStartPos;
    }

    /**
     * 移动起始点
     *
     * @param offset
     * @return:查看：MOVE_STATE_NONE,MOVE_STATE_LEFT_NONE，MOVE_STATE_NOMAL，MOVE_STATE_RIGHT_NONE
     */
    @Override
    public int moveStartPos(int offset) {
        int newPos = mStartPos + offset;
        return setStartPos(newPos);
    }

    /**
     * 设置起始点
     *
     * @param pos 当前位置
     * @return 查看：MOVE_STATE_NONE, MOVE_STATE_LEFT_NONE, MOVE_STATE_NORMAL, MOVE_STATE_RIGHT_NONE
     */
    @Override
    public int setStartPos(int pos) {
        if (getValueCount() <= 0) {
            mStartPos = 0;
            return ChartLayer.MOVE_STATE_NONE;
        }

        int state;
        if (pos < 0) {
            mStartPos = 0;
            state = ChartLayer.MOVE_STATE_LEFT_NONE;
        } else if (pos > getValueCount() - mMaxCount) {
            mStartPos = getValueCount() - mMaxCount;
            if (mStartPos < 0) {
                mStartPos = 0;
            }
            state = ChartLayer.MOVE_STATE_RIGHT_NONE;
        } else {
            mStartPos = pos;
            state = ChartLayer.MOVE_STATE_NORMAL;
        }
        return state;
    }

    /**
     * 设置显示的最大根数
     *
     * @param count     最大根数
     * @param direction 0:从第一根显示; 1:从倒数第count根显示
     */
    public void setMaxCount(int count, int direction) {
        if (getValueCount() <= 0) {
            mMaxCount = count;
            mStartPos = 0;
        } else {
            if (mHasDataState == 0) {
                mHasDataState = 1;

                mStartPos = 0;
                if (direction == 1 && getValueCount() > count) {
                    mStartPos = getValueCount() - count;
                }

                mMaxCount = count;
            } else {
                if (getValueCount() > count) {
                    if (count < mMaxCount)/* 放大 */ {
                        mStartPos += (mMaxCount - count);
                        if (mStartPos > getValueCount() - count) {
                            mStartPos = getValueCount() - count;
                        }
                    } else if (count > mMaxCount)/* 缩小 */ {
                        mStartPos -= (count - mMaxCount);
                        if (mStartPos < 0) {
                            mStartPos = 0;
                        }
                    }
                } else {
                    mStartPos = 0;
                }
                mMaxCount = count;
            }
        }
    }

    /**
     * 设置最大显示根数,默认从倒数第count根开始显示
     *
     * @param count
     */
    @Override
    public void setMaxCount(int count) {
        setMaxCount(count, 1);
    }

    public float getMaxValue() {
        return mMaxValue;
    }

    public float getMinValue() {
        return mMinValue;
    }

    @Override
    public void resetData() {
        clear();
        mCurrPos = -1;
        mIsLine = false;
        mMaxCount = 0;
        mHasDataState = 0;
        mStartPos = 0;
    }

    public void clear() {
        mKLineGap = null;
        mLstValues.clear();
        mMinValue = 0;
        mMaxValue = 0;
    }

    public void setColumnarWidth(float width) {
        mColumnarWidth = width;
    }

    public static class ColumnarAtom {
        public float mOpen = 0;
        public float mHigh = 0;
        public float mClose = 0;
        public float mLow = 0;
        public long mAcount = 0;
        public Object mTag = null;
        public String mColor;

        public ColumnarAtom() {

        }

        public ColumnarAtom(float open, float high, float close, float low, long acount) {
            mOpen = open;
            mHigh = high;
            mClose = close;
            mLow = low;
            mAcount = acount;
        }

        public ColumnarAtom(float close) {
            mClose = close;
        }

        public ColumnarAtom(float close, String color) {
            mClose = close;
            mColor = color;
        }
    }

    @Override
    public boolean onActionShowPress(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (x >= mLeft && x <= mRight && y >= mTop && y <= mBottom) {
            if (mActionListener != null && mLstValues.size() > 0) {
                mCurrPos = x2Pos(event.getX());
                if (mCurrPos < 0) {
                    mCurrPos = 0;
                }

                int realPos = mStartPos + mCurrPos;

                if (realPos > mLstValues.size() - 1) {
                    realPos = mLstValues.size() - 1 >= 0 ? mLstValues.size() - 1 : 0;
                } else if (realPos < 0) {
                    realPos = 0;
                }
                if (mActionListener.onLongPressed(realPos, event)) {
                    repaint();
                }
            }
            return true;
        }
        return false;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public void setNeedPreCalc(boolean b) {
        mNeedPreCalcCoulumnar = b;
    }

    public void setShowHighLowFlag(boolean b) {
        mShowHighLowFlag = b;
    }

    public void setHighLowFlagBorderColor(int color) {
        mHighLowFlagBorderColor = color;
    }

    public void setHighLowFlagBgColor(int color) {
        mHighLowFlagBgColor = color;
    }

    public void setHighLowFlagBorderWidth(int width) {
        mHighLowFlagBorderWidth = width;
    }

    public void setHighLowFlagTxtColor(int color) {
        mHighLowFlagTxtColor = color;
    }

    public void setHighLowFlagTxtSize(int size) {
        mHighLowFlagTxtSize = size;
    }

    public void setDrawHighLowListener(onDrawHighLowListener listener) {
        mDrawHighLowListener = listener;
    }

    @Override
    public void setMaxValue(float val) {
        mMaxValue = val;
    }

    @Override
    public void setMinValue(float val) {
        mMinValue = val;
    }

    public void change2Line(boolean flag) {
        mIsLine = flag;
    }

    public boolean isLine() {
        return mIsLine;
    }

    public float getColumnarWidth() {
        return mColumnarWidth;
    }

    @Override
    public void onActionUp(MotionEvent event) {
        if (mActionListener != null && mLstValues.size() > 0) {
            mCurrPos = -1;
            if (mActionListener.onUp(event)) {
                repaint();
            }
        }
    }

    @Override
    public boolean onActionMove(MotionEvent event) {
        // if (event.getAction() == MotionEvent.ACTION_MOVE && mIsPressed) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (mActionListener != null && mLstValues.size() > 0) {
                mCurrPos = x2Pos(event.getX());
                if (mCurrPos < 0) {
                    mCurrPos = 0;
                }

                int realPos = mStartPos + mCurrPos;

                if (realPos > mLstValues.size() - 1) {
                    realPos = mLstValues.size() - 1 >= 0 ? mLstValues.size() - 1 : 0;
                } else if (realPos < 0) {
                    realPos = 0;
                }
                if (mActionListener.onMove(realPos, event)) {
                    repaint();
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onActionDoubleTap(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (x >= mLeft && x <= mRight && y >= mTop && y <= mBottom) {
            if (mActionTapListener != null && mActionTapListener.onActionDoubleTap(event)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onActionSingleTap(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (x >= mLeft && x <= mRight && y >= mTop && y <= mBottom) {
            if (mActionTapListener != null && mActionTapListener.onActionSingleTap(event)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onActionDown(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (x >= mLeft && x <= mRight && y >= mTop && y <= mBottom) {
            if (mActionListener != null && mActionListener.onDown(event)) {
                repaint();

            }
            return true;
        }
        return false;
    }

    // 实现BS点画在最上层
    public interface DrawInfoWriteOutCallBack {
        public void out(float centerX, float topY, float bottomY, int time, float columWidth);

        public void clear();
    }


    public interface onDrawHighLowListener {
        public String formatValue(float value);
    }

    public float getValueByY(float y) {
        return y2Value(y);
    }


    public static class KLineGap {
        public int gapPosition = -1;
        public float gapBigValue = 0;
        public float gapSmallValue = 0;
    }


    public void setLineGap(KLineGap gap) {
        mKLineGap = gap;
    }

    public void setShowGap(boolean showGap) {
        mShowGap = showGap;
    }

    public void setGapColor(int gapColor) {
        mGapColor = gapColor;
    }

    public void calcGap() {
        if (mShowGap == false || mLstValues == null || getValueCount() < 2) {
            return;
        }
        float addHigh = getValue(getValueCount() - 1).mHigh;
        float addLow = getValue(getValueCount() - 1).mLow;

        ColumnarLayer.KLineGap gap = null;
        for (int i = getValueCount() - 2; i >= 0; i--) {
            float tHigh = getValue(i).mHigh;
            float tLow = getValue(i).mLow;
            boolean noOverlay = addLow > tHigh || addHigh < tLow;
            if (noOverlay) {
                gap = new ColumnarLayer.KLineGap();
                gap.gapPosition = i;
                if (tHigh < addLow) {
                    gap.gapBigValue = addLow;
                    gap.gapSmallValue = tHigh;
                } else if (tLow > addHigh) {
                    gap.gapBigValue = tLow;
                    gap.gapSmallValue = addHigh;
                }

                setLineGap(gap);

                break;
            }

            addHigh = Math.max(addHigh, tHigh);
            addLow = Math.min(addLow, tLow);
        }
    }

    public void setEmptyData(int pos, int count) {
        int emptySize = count - getValueCount();
        if (emptySize > 0) {
            List<ColumnarAtom> list = new ArrayList<>();
            for (int i = 0; i < emptySize; i++) {
                list.add(new ColumnarLayer.ColumnarAtom(Float.NaN));
            }
            addAllValue(pos, list);
        }
    }
}
