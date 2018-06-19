package com.yulin.chart.layers;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import com.yulin.chart.ChartView;
import com.yulin.chart.ChartView.IGroupLayer;

public class StackLayer extends ChartLayer implements IGroupLayer {
    private List<ChartLayer> mLstLayers = new ArrayList<ChartLayer>();
    private float mMinValue = 0;
    private float mMaxValue = 0;

    @Override
    public RectF prepareBeforeDraw(RectF rect) {
        for (int i = 0; i < mLstLayers.size(); i++) {
            ChartLayer layer = mLstLayers.get(i);
            if (layer.getIgnoreParentPadding()) {
                layer.setPaddings(layer.mPaddingLeft, layer.mPaddingTop, layer.mPaddingRight, layer.mPaddingBottom);
            } else {
                layer.setPaddings(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);

            }

            layer.prepareBeforeDraw(rect);

        }
        mLeft = rect.left;
        mRight = rect.right;
        mTop = rect.top;
        mBottom = rect.bottom;
        return rect;
    }

    public float[] calMinAndMaxValue() {
        int count = 0;
        for (int i = 0; i < mLstLayers.size(); i++) {
            ChartLayer layer = mLstLayers.get(i);
            float[] minAndMax = layer.calMinAndMaxValue();
            if (minAndMax != null && layer.isShow()) {
                if (count == 0) {
                    mMinValue = minAndMax[0];
                    mMaxValue = minAndMax[1];
                } else {
                    if (mMinValue > minAndMax[0]) {
                        mMinValue = minAndMax[0];
                    }
                    if (mMaxValue < minAndMax[1]) {
                        mMaxValue = minAndMax[1];
                    }
                }
                count++;
            }
        }
        return new float[]{mMinValue, mMaxValue};
    }

    @Override
    public void doDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        for (int i = 0; i < mLstLayers.size(); i++) {
            if (mLstLayers.get(i).isShow()) {
                mLstLayers.get(i).doDraw(canvas);
            }
        }
    }

    @Override
    public void rePrepareWhenDrawing(RectF rect) {
        // TODO Auto-generated method stub
        for (int i = 0; i < mLstLayers.size(); i++) {
            mLstLayers.get(i).rePrepareWhenDrawing(rect);
        }
    }


    public void clear() {
        mLstLayers.clear();
        mMinValue = 0;
        mMaxValue = 0;

    }

    public float getMaxValue() {
        return mMaxValue;
    }

    public float getMinValue() {
        return mMinValue;
    }

    public void addLayer(ChartLayer layer) {
        mLstLayers.add(layer);
    }

    public void layout(float left, float top, float right, float bottom) {
        super.layout(left, top, right, bottom);
        for (int i = 0; i < mLstLayers.size(); i++) {
            mLstLayers.get(i).layout(left, top, right, bottom);
        }
    }

    public boolean onActionShowPress(MotionEvent event) {
        for (int i = 0; i < mLstLayers.size(); i++) {
            if (mLstLayers.get(i).onActionShowPress(event)) {
                return true;
            }
        }
        return false;
    }

    public void onActionUp(MotionEvent event) {
        for (int i = 0; i < mLstLayers.size(); i++) {
            mLstLayers.get(i).onActionUp(event);
        }
    }

    public boolean onActionMove(MotionEvent event) {
        for (int i = 0; i < mLstLayers.size(); i++) {
            if (mLstLayers.get(i).onActionMove(event)) {
                return true;
            }
        }
        return false;
    }

    public boolean onActionDown(MotionEvent event) {
        for (int i = 0; i < mLstLayers.size(); i++) {
            if (mLstLayers.get(i).onActionDown(event)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onActionDoubleTap(MotionEvent event) {
        for (int i = 0; i < mLstLayers.size(); i++) {
            if (mLstLayers.get(i).onActionDoubleTap(event)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onActionSingleTap(MotionEvent event) {
        for (int i = 0; i < mLstLayers.size(); i++) {
            if (mLstLayers.get(i).onActionSingleTap(event)) {
                return true;
            }
        }
        return false;
    }

    public void setChartView(ChartView cv) {
        for (int i = 0; i < mLstLayers.size(); i++) {
            mLstLayers.get(i).setChartView(cv);
        }
    }

//    public boolean isShowHGrid() {
//        for (int i = 0; i < mLstLayers.size(); i++) {
//            if (mLstLayers.get(i).isShowHGrid()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean isShowVGrid() {
//        for (int i = 0; i < mLstLayers.size(); i++) {
//            if (mLstLayers.get(i).isShowVGrid()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public int getHLineCount() {
//        for (int i = 0; i < mLstLayers.size(); i++) {
//            if (mLstLayers.get(i).isShowHGrid()) {
//                return mLstLayers.get(i).getHLineCount();
//            }
//        }
//        return 0;
//    }
//
//    public int getVLineCount() {
//        for (int i = 0; i < mLstLayers.size(); i++) {
//            if (mLstLayers.get(i).isShowVGrid()) {
//                return mLstLayers.get(i).getVLineCount();
//            }
//        }
//        return 0;
//    }

    public void show(boolean isShow) {
        for (int i = 0; i < mLstLayers.size(); i++) {
            mLstLayers.get(i).show(isShow);
        }
    }

    public boolean isShow() {
        for (int i = 0; i < mLstLayers.size(); i++) {
            if (mLstLayers.get(i).isShow()) {
                return true;
            }
        }
        return false;
    }

    public List<ChartLayer> getLayers() {
        return mLstLayers;
    }

    public ChartLayer getLayer(int pos) {
        return mLstLayers.get(pos);
    }

    @Override
    public ChartLayer getLayerByTag(String tag) {
        for (int i = 0; i < mLstLayers.size(); i++) {
            ChartLayer layer = mLstLayers.get(i);
            if (layer instanceof IGroupLayer) {
                ChartLayer l = ((IGroupLayer) layer).getLayerByTag(tag);
                if (l != null) {
                    return l;
                }
            }
            if (tag.equals(layer.getTag())) {
                return layer;
            }
        }
        return null;
    }

}
