package com.yulin.chart.layers;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 折线层
 *
 * @author Administrator
 */
public class PointLayer extends ChartLayer {

    private List<PointAtom> mLstValues = new ArrayList<>();

    private int mMaxCount = 0;

    private float mPoint_r = 3;
    private float mPoint_borderStroke = 2;

    private float mSpace = 0;

    private float mMinValue = 0;
    private float mMaxValue = 0;

    private float mPosPerValue = 0;// 单位值所代表的距离

    private int mCurrPos = -1;

    private int mStrokeWidth = 2;

    private int mStartPos = 0;

    private float mFloorValue = Integer.MIN_VALUE;// 允许的最小值
    private boolean mIsIncludeFloor = true;

    private int mHasDataState = 0;
    private boolean mbLinked = false;
    private boolean mbShowPoint = true;

    private String lineName;

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
        // int size = mLstValues.size();
        int size = mStartPos + mMaxCount > mLstValues.size() ? mLstValues.size() : mStartPos + mMaxCount;
        if (mIsIncludeFloor) {
            boolean isFirst = true;
            for (int i = mStartPos; i < size; i++) {
                Float val = mLstValues.get(i).value;
                if (val.isNaN()) {
                    continue;
                }
                if (val < mFloorValue) {
                    continue;
                }
                if (isFirst) {
                    isFirst = false;
                    mMinValue = val;
                    mMaxValue = val;
                } else {
                    if (mMinValue > val) {
                        mMinValue = val;
                    }
                    if (mMaxValue < val) {
                        mMaxValue = val;
                    }
                }
            }
            if (isFirst) {
                return null;
            } else {
                return new float[]{mMinValue, mMaxValue};
            }
        } else {
            boolean isFirst = true;
            for (int i = mStartPos; i < size; i++) {
                Float val = mLstValues.get(i).value;
                if (val.isNaN()) {
                    continue;
                }
                if (val <= mFloorValue) {
                    continue;
                }
                if (isFirst) {
                    isFirst = false;
                    mMinValue = val;
                    mMaxValue = val;
                } else {
                    if (mMinValue > val) {
                        mMinValue = val;
                    }
                    if (mMaxValue < val) {
                        mMaxValue = val;
                    }
                }
            }
            if (isFirst) {
                return null;
            } else {
                return new float[]{mMinValue, mMaxValue};
            }
        }
    }

    @Override
    public void doDraw(Canvas canvas) {
        int size = mLstValues.size();
        if (mIsIncludeFloor) {
            int count = 0;

            float lastX = -9999;
            float lastY = -9999;
            int lastIndex = -1;

            getPaint().reset();
            getPaint().setColor(mColor);
            getPaint().setStrokeWidth(mStrokeWidth);
            getPaint().setAntiAlias(true);

            for (int i = mStartPos; i < size; i++) {
                Float tVal = mLstValues.get(i).value;
                if (tVal.isNaN()) {
                    count++;
                    continue;
                }
                int j = 1;
                if (i - mStartPos >= mMaxCount - 1) {
                    break;
                }
                if (mDrawingListener != null) {
                    mDrawingListener.onDrawing(getPaint(), i);
                }
                float x = pos2X(count);
                if (i >= 0 && i < size - 1) {
                    float val1 = mLstValues.get(i).value;
                    Float val2 = Float.NaN;
                    for (; i + j < mLstValues.size(); j++) {
                        val2 = mLstValues.get(i + j).value;
                        if (!val2.equals(Float.NaN)) {
                            lastIndex = i + j;
                            break;
                        }

                    }

                    if (!val2.equals(Float.NaN)) {

                        if (val1 >= mFloorValue && val2.floatValue() >= mFloorValue) {
                            getPaint().setStrokeWidth(mStrokeWidth);
                            getPaint().setColor(mColor);

                            lastX = x + mSpace * j;
                            lastY = value2Y(val2.floatValue());


                            if (mbShowPoint) {
                                if (mDrawingListener != null) {
                                    mDrawingListener.onDrawing(getPaint(), i);
                                }
                                getPaint().setStrokeWidth(mPoint_borderStroke);
                                canvas.drawCircle(x, value2Y(val1), mPoint_r, getPaint());
                            }

                            getPaint().setColor(mColor);
                            getPaint().setStrokeWidth(mStrokeWidth);
                            if (mbLinked) {
                                canvas.drawLine(x, value2Y(val1), lastX, lastY, getPaint());
                            }


                        }
                        i += (j - 1);
                    }
                }
                count += j;
            }

            //画最后一个圈
            if (mbShowPoint && lastIndex > 0) {
                if (mDrawingListener != null) {
                    mDrawingListener.onDrawing(getPaint(), lastIndex);
                }
                getPaint().setStrokeWidth(mPoint_borderStroke);
                canvas.drawCircle(lastX, lastY, mPoint_r, getPaint());
            }


        } else {
            int count = 0;

            float lastX = -9999;
            float lastY = -9999;
            int lastIndex = -1;

            getPaint().reset();
            getPaint().setColor(mColor);
            getPaint().setStrokeWidth(mStrokeWidth);
            getPaint().setAntiAlias(true);
            for (int i = mStartPos; i < size; i++) {
                Float tVal = mLstValues.get(i).value;
                if (tVal.isNaN()) {
                    count++;
                    continue;
                }
                int j = 1;
                if (i - mStartPos >= mMaxCount - 1) {
                    break;
                }

                float x = pos2X(count);
                if (i >= 0 && i < size - 1) {
                    float val1 = mLstValues.get(i).value;
                    Float val2 = Float.NaN;
                    for (; i + j < mLstValues.size(); j++) {
                        val2 = mLstValues.get(i + j).value;
                        if (!val2.equals(Float.NaN)) {
                            lastIndex = i + j;
                            break;
                        }
                    }

                    if (!val2.equals(Float.NaN)) {

                        if (val1 > mFloorValue && val2.floatValue() > mFloorValue) {
                            getPaint().setStrokeWidth(mStrokeWidth);
                            lastX = x + mSpace * j;
                            lastY = value2Y(val2.floatValue());

                            if (mDrawingListener != null) {
                                mDrawingListener.onDrawing(getPaint(), i);
                            }
                            getPaint().setStrokeWidth(mPoint_borderStroke);

                            canvas.drawCircle(x, value2Y(val1), mPoint_r, getPaint());

                            getPaint().setColor(mColor);
                            getPaint().setStrokeWidth(mStrokeWidth);
                            if (mbLinked) {
                                canvas.drawLine(x, value2Y(val1), lastX, lastY, getPaint());
                            }

                        }
                        i += (j - 1);
                    }
                }

                count += j;
            }

            //画最后一个圈
            if (mbShowPoint && lastIndex > 0) {
                if (mDrawingListener != null) {
                    mDrawingListener.onDrawing(getPaint(), lastIndex);
                }
                getPaint().setStrokeWidth(mPoint_borderStroke);
                canvas.drawCircle(lastX, lastY, mPoint_r, getPaint());
            }

        }
        // if(mCurrPos > -1)
        // {
        // float x = pos2X(mCurrPos);
        // getPaint().setStrokeWidth(mCheckWidth);
        // getPaint().setColor(mCheckColor);
        // canvas.drawLine(x, mTop, x, mBottom, getPaint());
        // }
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public void setLinkPoint(boolean b) {
        mbLinked = b;
    }

    public void setShowPoint(boolean b) {
        mbShowPoint = b;
    }

    public void setmPoint_r(float mPoint_r) {
        this.mPoint_r = mPoint_r;
    }

    public void setmPoint_borderStroke(float mPoint_borderStroke) {
        this.mPoint_borderStroke = mPoint_borderStroke;
    }

    public void setStrokeWidth(int width) {
        mStrokeWidth = width;
    }

    public void setFloorValue(float val, boolean isInclude) {
        mFloorValue = val;
        mIsIncludeFloor = isInclude;
    }

    private int x2Pos(float x) {
        return (int) ((x - mLeft - mPaddingLeft) / mSpace);
    }

    private float pos2X(int pos) {
        return mLeft + mPaddingLeft + mSpace * pos;
    }

    private float value2Y(float val) {
        return mBottom - mPosPerValue * (val - mMinValue) - mPaddingBottom;
    }

    public PointF getPointByPos(int pos) {
        PointF pointf = new PointF();
        int count = pos - mStartPos < 0 ? 0 : pos - mStartPos;
        pointf.x = pos2X(count);
        float val = getValue(pos).value;
        pointf.y = value2Y(val);
        return pointf;
    }

    @Override
    public void rePrepareWhenDrawing(RectF rect) {
        // TODO Auto-generated method stub
        float totalWidth = mRight - mLeft - mPaddingLeft - mPaddingRight;
        mSpace = totalWidth / (mMaxCount - 1);

        float totalHeight = mBottom - mTop - mPaddingTop - mPaddingBottom;
        mPosPerValue = totalHeight / (mMaxValue - mMinValue);
    }

    public void setValue(int index, PointAtom val) {
        if (index >= 0 && index <= mLstValues.size() - 1) {
            mLstValues.set(index, val);
        } else if (index == mLstValues.size()) {
            addValue(val);
        }
    }

    public void addValue(PointAtom val) {
        mLstValues.add(val);
    }

    public void addAllValue(int index, List<PointAtom> list) {
        mLstValues.addAll(index, list);
    }

    public PointAtom getValue(int pos) {
        if (pos < mLstValues.size()) {
            return mLstValues.get(pos);
        }
        return null;
    }

    public void removeValue(int pos) {
        if (pos >= getValueCount()) {
            return;
        }

        mLstValues.remove(pos);
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
     * 设置最大显示根数,默认从倒数第count根开始显示
     *
     * @param count
     */
    @Override
    public void setMaxCount(int count) {
        setMaxCount(count, 1);
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

    public int getValueCount() {
        return mLstValues.size();
    }

    public PointAtom getLastValue() {
        if (getValueCount() > 0) {
            return getValue(getValueCount() - 1);
        }
        return new PointAtom(0);
    }

    public void setLastValues(PointAtom atom) {
        int count = getValueCount();
        if (count > 0) {
            setValue(count - 1, atom);
        } else {
            setValue(0, atom);
        }


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
        mCurrPos = -1;
        mMaxCount = 0;
        mHasDataState = 0;
        mStartPos = 0;
    }

    public void clear() {
        mLstValues.clear();
        mMinValue = 0;
        mMaxValue = 0;
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

                return true;
            }
        }
        return false;
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

                return true;
            }
        }
        return false;
    }

    public static class PointAtom {

        public float value;
        public String color;

        public PointAtom(float value) {
            this.value = value;
        }

        public PointAtom(float value, String color) {
            this.value = value;
            this.color = color;
        }
    }

    public void setEmptyData(int pos, int count) {
        int emptySize = count - getValueCount();
        if (emptySize > 0) {
            List<PointAtom> list = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                list.add(new PointLayer.PointAtom(Float.NaN));
            }
            addAllValue(pos, list);
        }
    }
}
