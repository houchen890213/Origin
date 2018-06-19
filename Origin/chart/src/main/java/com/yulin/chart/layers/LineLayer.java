package com.yulin.chart.layers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 折线层
 *
 * @author Administrator
 */
public class LineLayer extends ChartLayer {

    private Map<Integer, LineData> mLineMap = new HashMap<>();
    private List<Integer> mSplitPostion = new ArrayList<>(); //分割点， 位于该位置的点与后一点不连续.(ex:五日分时线)

    private int mMaxCount = 0;

    private float mSpace = 0;

    private float mMinValue = 0;
    private float mMaxValue = 0;

    private float mPosPerValue = 0;// 单位值所代表的距离

    private int mCurrPos = -1;
    private int mStartPos = 0;

    private float mFloorValue = Integer.MIN_VALUE;// 允许的最小值
    private boolean mIsIncludeFloor = true;

    private int mHasDataState = 0;


    public static class LinePoint {
        public LinePoint(float val, Object tag) {
            mValue = val;
            mTag = tag;
        }

        public LinePoint(float val) {
            this(val, "");
        }

        public LinePoint() {
        }

        ;

        public float mValue;
        public Object mTag;
    }

    public static class LineData {

        private String lineName;
        private LineConfig mLineConfig;
        private int mLayerSort = 0;//数字越大，所画的位于越上层
        private List<LinePoint> mLstValues;

        public LineData(String lineName, List<LinePoint> lstValues, int layerSort, LineConfig lineConfig) {
            this.lineName = lineName;
            this.mLayerSort = layerSort;
            this.mLineConfig = lineConfig;
            this.mLstValues = lstValues;
        }

        public LineData(int layerSort) {
            this(new ArrayList<LinePoint>(), layerSort);
        }

        public LineData(String lineName, int layerSort, LineConfig config) {
            this(lineName, new ArrayList<LinePoint>(), layerSort, config);
        }

        public LineData(int layerSort, LineConfig config) {
            this("", new ArrayList<LinePoint>(), layerSort, config);
        }

        public LineData(List<LinePoint> lstValues, int layerSort) {
            this("", lstValues, layerSort, new LineConfig());
        }

        public String getLineName() {
            return lineName;
        }

        public void setLineName(String lineName) {
            this.lineName = lineName;
        }

        public void setLineConfig(LineConfig lineConfig) {
            this.mLineConfig = lineConfig;
        }

        public void setLayerSort(int layerSort) {
            this.mLayerSort = layerSort;
        }

        public void setLstValues(List<LinePoint> lstValues) {
            this.mLstValues = lstValues;
        }

        public LineConfig getLineConfig() {
            return mLineConfig;
        }

        public int getLayerSort() {
            return mLayerSort;
        }

        public List<LinePoint> getLstValues() {
            return mLstValues;
        }
    }

    public static class LineConfig {
        private boolean mIsShowShadow = false;
        private int mShadowColor_start = 0x214690ef;
        private int mShadowColor_end = 0x00ffffff;
        private int mLineWidth = 2;
        private int mLineColor = Color.BLACK;

        public LineConfig() {

        }

        public LineConfig(int lineColor, int lineWidth) {
            this.mLineColor = lineColor;
            this.mLineWidth = lineWidth;
        }

        public LineConfig setShowShadow(boolean showShadow) {
            mIsShowShadow = showShadow;
            return this;
        }

        public LineConfig setShadowColor(int shadowColor_start, int shadowColor_end) {
            mShadowColor_start = shadowColor_start;
            mShadowColor_end = shadowColor_end;
            return this;
        }

        public LineConfig setLineWidth(int strokeWidth) {
            mLineWidth = strokeWidth;
            return this;
        }

        public LineConfig setLineColor(int color) {
            mLineColor = color;
            return this;
        }

        public boolean isShowShadow() {
            return mIsShowShadow;
        }

        public int getShadowColorStart() {
            return mShadowColor_start;
        }

        public int getShadowColorEnd() {
            return mShadowColor_end;
        }

        public int getLineWidth() {
            return mLineWidth;
        }

        public int getLineColor() {
            return mLineColor;
        }
    }


    @Override
    public RectF prepareBeforeDraw(RectF rect) {
        // TODO Auto-generated method stub
        getPaint().setAntiAlias(true);
        mLeft = rect.left;
        mRight = rect.right;
        mTop = rect.top;
        mBottom = rect.bottom;
        return new RectF(mLeft, mTop, mRight, mBottom);
    }

    @Override
    public float[] calMinAndMaxValue() {
        float[] minAndMax = null;
        for (LineData line : mLineMap.values()) {
            float[] vals = calMinAndMaxValue(line.getLstValues());
            if (vals != null) {
                if (minAndMax == null) {
                    minAndMax = vals;
                } else {
                    minAndMax[0] = Math.min(minAndMax[0], vals[0]);
                    minAndMax[1] = Math.max(minAndMax[1], vals[1]);
                }
            }
        }

        //设置最大、最小值
        if (minAndMax != null && minAndMax.length == 2) {
            mMinValue = minAndMax[0];
            mMaxValue = minAndMax[1];
        }

        return minAndMax;
    }


    @Override
    public void doDraw(Canvas canvas) {
        for (LineData line : getSortedLineList()) {
            doDrawLine(canvas, line);
        }
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

    public PointF getPointByPos(int key, int pos) {
        PointF pointf = new PointF();
        int count = pos - mStartPos < 0 ? 0 : pos - mStartPos;
        pointf.x = pos2X(count);
        float val = getValue(key, pos).mValue;
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

    public void addLine(int key, LineData lineData) {
        mLineMap.put(key, lineData);
    }

    public LineData getLine(int key) {
        return mLineMap.get(key);
    }

    public boolean containsLine(int key) {
        return mLineMap.containsKey(key);
    }

    public void addValue(int key, LinePoint val) {
        LineData data = getLine(key);
        if (data == null) {
            Log.e("LineLayer err", "LineLayer: key - " + key + " is not exist");
        }

        data.getLstValues().add(val);
    }

    public void addValue(int key, int index, LinePoint val) {
        LineData data = getLine(key);
        if (data == null) {
            Log.e("LineLayer err", "LineLayer: key - " + key + " is not exist");
        }

        data.getLstValues().add(index, val);
    }

    public void addSplitPostion(int... position) {
        for (int i : position) {
            mSplitPostion.add(i);
        }
    }

    public void addAllValue(int key, int index, List<LinePoint> list) {
        LineData data = getLine(key);
        if (data == null) {
            Log.e("LineLayer err", "LineLayer: key - " + key + " is not exist");
        }

        data.getLstValues().addAll(index, list);
    }

    public LinePoint getValue(int key, int pos) {
        LinePoint ret = null;
        LineData data = getLine(key);
        if (data == null) {
            Log.e("LineLayer err", "LineLayer: key - " + key + " is not exist");
        }
        if (data.getLstValues().size() > pos) {
            ret = data.getLstValues().get(pos);
        }
        return ret;
    }

    public void setValue(int key, int index, LinePoint val) {
        LineData data = getLine(key);
        if (data == null) {
            Log.e("LineLayer err", "LineLayer: key - " + key + " is not exist");
        }
        if (index >= 0 && index <= data.getLstValues().size() - 1) {
            data.getLstValues().set(index, val);
        } else if (index == data.getLstValues().size()) {
            addValue(key, val);
        }
    }

    public void setLastValue(int key, LinePoint value) {
        if (getValueCount(key) > 0) {
            setValue(key, getValueCount(key) - 1, value);
        }
    }

    //    public int getStartPos(){
    //        return mStartPos;
    //    }

    /**
     * 移动起始点
     *
     * @param offset
     * @return:查看：MOVE_STATE_LEFT_NONE，MOVE_STATE_NOMAL，MOVE_STATE_RIGHT_NONE
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
        if (mLineMap == null)
            return 0;
        int max = 0;
        for (LineData line : mLineMap.values()) {
            max = Math.max(max, line.getLstValues().size());
        }
        return max;
    }

    public int getValueCount(int key) {
        return getLine(key).getLstValues().size();
    }

    public LinePoint getLastValue(int key) {
        if (getValueCount(key) > 0) {
            return getValue(key, getValueCount(key) - 1);
        }
        return null;
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
        clearAll();
        mLineMap.clear();
        mCurrPos = -1;
        mMaxCount = 0;
        mHasDataState = 0;
        mStartPos = 0;
    }

    public void clearAll() {
        for (LineData data : mLineMap.values()) {
            data.getLstValues().clear();
        }
        mSplitPostion.clear();
        mMinValue = 0;
        mMaxValue = 0;
    }

    public void clear(String key) {
        LineData line = mLineMap.get(key);
        if (line != null) {
            line.getLstValues().clear();
        }
        mMinValue = 0;
        mMaxValue = 0;
    }

    public LineData removeLine(String key) {
        return mLineMap.remove(key);
    }


    @Override
    public boolean onActionDown(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        System.out.println("sky linelayer onActionDown 1");
        if (x >= mLeft && x <= mRight && y >= mTop && y <= mBottom) {
            if (mActionListener != null && mActionListener.onDown(event)) {
                repaint();

            }
            System.out.println("sky linelayer onActionDown 2");
            return true;
        }

        System.out.println("sky linelayer onActionDown 3");
        return false;
    }

    @Override
    public boolean onActionShowPress(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (x >= mLeft && x <= mRight && y >= mTop && y <= mBottom) {
            if (mActionListener != null && getMaxLineListDataSize() > 0) {
                mCurrPos = x2Pos(event.getX());
                if (mCurrPos < 0) {
                    mCurrPos = 0;
                }

                int realPos = mStartPos + mCurrPos;

                if (realPos > getMaxLineListDataSize() - 1) {
                    realPos = getMaxLineListDataSize() - 1 >= 0 ? getMaxLineListDataSize() - 1 : 0;
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
        if (mActionListener != null && getMaxLineListDataSize() > 0) {
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
            if (mActionListener != null && getMaxLineListDataSize() > 0) {
                mCurrPos = x2Pos(event.getX());
                if (mCurrPos < 0) {
                    mCurrPos = 0;
                }

                int realPos = mStartPos + mCurrPos;

                if (realPos > getMaxLineListDataSize() - 1) {
                    realPos = getMaxLineListDataSize() - 1 >= 0 ? getMaxLineListDataSize() - 1 : 0;
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

    private float[] calMinAndMaxValue(List<LinePoint> list) {
        int size = mStartPos + mMaxCount > list.size() ? list.size() : mStartPos + mMaxCount;
        if (mIsIncludeFloor) {
            boolean isFirst = true;
            for (int i = mStartPos; i < size; i++) {
                Float val = list.get(i).mValue;
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
                Float val = list.get(i).mValue;
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

    private void doDrawLine(Canvas canvas, LineData line) {
        int size = line.getLstValues().size();
        if (mIsIncludeFloor) {
            int count = 0;

            Path shadowPath = null;
            if (line.getLineConfig().isShowShadow()) {
                shadowPath = new Path();
                shadowPath.moveTo(pos2X(0), mBottom - mPaddingBottom);
            }

            float lastX = -9999;
            float lastY = -9999;

            getPaint().reset();
            getPaint().setColor(line.getLineConfig().getLineColor());
            getPaint().setStrokeWidth(line.getLineConfig().getLineWidth());
            getPaint().setAntiAlias(true);

            for (int i = mStartPos; i < size; i++) {
                Float tVal = line.getLstValues().get(i).mValue;
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
                    float val1 = line.getLstValues().get(i).mValue;
                    Float val2 = Float.NaN;
                    for (; i + j < line.getLstValues().size(); j++) {
                        val2 = line.getLstValues().get(i + j).mValue;
                        if (!val2.equals(Float.NaN)) {
                            break;
                        }

                    }

                    if (!val2.equals(Float.NaN)) {
                        boolean isSplit = isSplit(i, i + j);
                        i += (j - 1);
                        if (val1 >= mFloorValue && val2.floatValue() >= mFloorValue) {
                            getPaint().setStrokeWidth(line.getLineConfig().getLineWidth());
                            getPaint().setColor(line.getLineConfig().getLineColor());

                            lastX = x + mSpace * j;
                            lastY = value2Y(val2.floatValue());

                            if (!isSplit) {
                                canvas.drawLine(x, value2Y(val1), lastX, lastY, getPaint());
                            }

                            if (shadowPath != null) {
                                shadowPath.lineTo(x, value2Y(val1));
                            }
                        }
                    }
                }
                count += j;
            }

            if (line.getLineConfig().isShowShadow() && shadowPath != null) {
                if (lastX != -9999 && lastY != -9999) {
                    shadowPath.lineTo(lastX, lastY);
                    shadowPath.lineTo(lastX, mBottom - mPaddingBottom);
                }

                if (!shadowPath.isEmpty()) {
                    LinearGradient lg = new LinearGradient(mLeft, mTop, mLeft, mBottom - mPaddingBottom,
                            line.getLineConfig().getShadowColorStart(), line.getLineConfig().getShadowColorEnd(), Shader.TileMode.CLAMP);
                    getPaint().setShader(lg);

                    getPaint().setStyle(Style.FILL);
                    shadowPath.close();// 封闭
                    canvas.drawPath(shadowPath, getPaint());
                }
            }
        } else {
            int count = 0;

            Path shadowPath = null;
            if (line.getLineConfig().isShowShadow()) {
                shadowPath = new Path();
                shadowPath.moveTo(pos2X(0), mBottom - mPaddingBottom);
            }


            float lastX = -9999;
            float lastY = -9999;

            getPaint().reset();
            getPaint().setColor(line.getLineConfig().getLineColor());
            getPaint().setStrokeWidth(line.getLineConfig().getLineWidth());
            getPaint().setAntiAlias(true);
            for (int i = mStartPos; i < size; i++) {
                Float tVal = line.getLstValues().get(i).mValue;
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
                    float val1 = line.getLstValues().get(i).mValue;
                    Float val2 = Float.NaN;
                    for (; i + j < line.getLstValues().size(); j++) {
                        val2 = line.getLstValues().get(i + j).mValue;
                        if (!val2.equals(Float.NaN)) {
                            break;
                        }
                    }

                    if (!val2.equals(Float.NaN)) {
                        boolean isSplit = isSplit(i, i + j);
                        i += (j - 1);
                        if (val1 > mFloorValue && val2.floatValue() > mFloorValue) {
                            getPaint().setStrokeWidth(line.getLineConfig().getLineWidth());
                            lastX = x + mSpace * j;
                            lastY = value2Y(val2.floatValue());

                            if (!isSplit) {
                                canvas.drawLine(x, value2Y(val1), lastX, lastY, getPaint());
                            }
                            if (shadowPath != null) {
                                shadowPath.lineTo(x, value2Y(val1));
                            }
                        }
                    }
                }

                count += j;
            }
            if (line.getLineConfig().isShowShadow() && shadowPath != null) {
                if (lastX != -9999 && lastY != -9999) {
                    shadowPath.lineTo(lastX, lastY);
                    shadowPath.lineTo(lastX, mBottom - mPaddingBottom);
                }

                if (!shadowPath.isEmpty()) {
                    LinearGradient lg = new LinearGradient(mLeft, mTop, mLeft, mBottom - mPaddingBottom,
                            line.getLineConfig().getShadowColorStart(), line.getLineConfig().getShadowColorEnd(), Shader.TileMode.CLAMP);
                    getPaint().setShader(lg);

                    getPaint().setStyle(Style.FILL);
                    shadowPath.close();// 封闭
                    canvas.drawPath(shadowPath, getPaint());
                }
            }
        }
        // if(mCurrPos > -1)
        // {
        // float x = pos2X(mCurrPos);
        // getPaint().setStrokeWidth(mCheckWidth);
        // getPaint().setColor(mCheckColor);
        // canvas.doDrawLine(x, mTop, x, mBottom, getPaint());
        // }
    }

    private List<LineData> getSortedLineList() {
        List<LineData> list = new ArrayList<>(mLineMap.values());
        Collections.sort(list, new Comparator<LineData>() {
            @Override
            public int compare(LineData lhs, LineData rhs) {
                return lhs.getLayerSort() < rhs.getLayerSort() ? -1 : (lhs.getLayerSort() == rhs.getLayerSort() ? 0 : 1);
            }
        });

        return list;
    }

    private int getMaxLineListDataSize() {
        int max = 0;
        if (mLineMap != null) {
            for (LineData line : mLineMap.values()) {
                max = Math.max(max, line.getLstValues().size());
            }
        }

        return max;
    }

    private boolean isSplit(int p1, int p2) {
        if (p2 - p1 == 1) {
            for (Integer integer : mSplitPostion) {
                if (integer.intValue() == p1) {
                    return true;
                }
            }
        } else {
            //之间出现NAN的情况
            for (Integer integer : mSplitPostion) {
                int split = integer.intValue();
                if (split >= p1 && split < p2) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 获取当前当前所属区块（分割点分出来的区段）
     *
     * @return
     */
    public int getSectionPosBySplitPostion(int pos) {
        if (mSplitPostion == null || mSplitPostion.isEmpty())
            return 0;

        for (int i = 0; i < mSplitPostion.size(); i++) {
            if (pos <= mSplitPostion.get(i)) {
                return i;
            }
        }

        return mSplitPostion.size();
    }

    public int getLineSize() {
        return mLineMap.size();
    }

    public void setEmptyData(int pos, int count) {
        for (int i = 0; i < getLineSize(); i++) {
            int emptySize = count - getLine(i).getLstValues().size();
            if (emptySize > 0) {
                List<LinePoint> list = new ArrayList<>();
                for (int j = 0; j < emptySize; j++) {
                    LineLayer.LinePoint point = new LineLayer.LinePoint();
                    point.mValue = Float.NaN;
                    list.add(point);
                }
                addAllValue(i, pos, list);
            }
        }
    }

}
