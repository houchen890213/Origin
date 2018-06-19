package com.yulin.chart.layers;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.yulin.chart.ChartView;
import com.yulin.chart.ChartView.IGroupLayer;

public class GroupLayer_count3 extends ChartLayer implements IGroupLayer {

    private ChartLayer mLeftLayer = null;
    private ChartLayer mCenterLayer = null;
    private ChartLayer mRightLayer = null;

    private RectF mLeftRectF = null;
    private RectF mCenterRectF = null;
    private RectF mRightRectF = null;

    @Override
    public RectF prepareBeforeDraw(RectF rect) {

        mLeftRectF = mLeftLayer.prepareBeforeDraw(rect);
        mRightRectF = mRightLayer.prepareBeforeDraw(rect);
        mCenterRectF = new RectF(mLeftRectF.right, rect.top, mRightRectF.left, rect.bottom);
        mCenterLayer.prepareBeforeDraw(mCenterRectF);

        mLeft = mLeftRectF.left;
        mTop = rect.top;
        mRight = rect.right;
        mBottom = rect.bottom;
        return rect;
    }

    @Override
    public void doDraw(Canvas canvas) {
        if (mLeftLayer.isShow()) {
            getPaint().setColor(mLeftLayer.getBorderColor());
            getPaint().setStrokeWidth(mLeftLayer.getBorderWidth());
            getPaint().setStyle(Style.STROKE);
            if (mLeftLayer.getIsShowBorder()) {
//				canvas.drawRect(mLeftLayer.getRectF(), getPaint());
                mLeftLayer.onDrawSide(canvas, getPaint());
            }


            if (mLeftLayer.isShowHGrid()) {
                Path path = new Path();

                int hLineNum = mLeftLayer.getHLineCount();
                float height = mLeftLayer.getHeight();
                float perHeight = height / (hLineNum + 1);
                float top = mLeftLayer.getTop();


                for (int i = 0; i < hLineNum; i++) {
                    top += perHeight;

                    path.moveTo(mLeftLayer.getLeft(), top);
                    path.lineTo(mLeftLayer.getRight(), top);
                    PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
                    getPaint().setPathEffect(effects);
                    canvas.drawPath(path, getPaint());
                    path.reset();
                }
            }
            if (mLeftLayer.isShowVGrid()) {
                int vLineNum = mLeftLayer.getVLineCount();
                float width = mLeftLayer.getWidth();
                float perWidth = width / (vLineNum + 1);
                float x = mLeftLayer.getLeft();
                for (int i = 0; i < vLineNum; i++) {
                    x += perWidth;
                    canvas.drawLine(x, mLeftLayer.getTop(), x, mLeftLayer.getBottom(), getPaint());
                }
            }

            getPaint().reset();
            getPaint().setColor(mLeftLayer.getBorderColor());
            getPaint().setStrokeWidth(mLeftLayer.getBorderWidth());
            getPaint().setStyle(Style.STROKE);
            mLeftLayer.doDraw(canvas);
        }

        if (mCenterLayer.isShow()) {
            getPaint().setColor(mCenterLayer.getBorderColor());
            getPaint().setStrokeWidth(mCenterLayer.getBorderWidth());
            getPaint().setStyle(Style.STROKE);

            if (mCenterLayer.getIsShowBorder()) {
                //canvas.drawRect(mCenterLayer.getRectF(), getPaint());
                canvas.drawRect(mCenterLayer.getRectF(), getPaint());
            }

            if (mCenterLayer.isShowHGrid()) {
                Path path = new Path();

                int hLineNum = mCenterLayer.getHLineCount();
                float height = mCenterLayer.getHeight();
                float perHeight = height / (hLineNum + 1);
                float top = mCenterLayer.getTop();

                for (int i = 0; i < hLineNum; i++) {
                    top += perHeight;

                    path.moveTo(mCenterLayer.getLeft(), top);
                    path.lineTo(mCenterLayer.getRight(), top);
                    PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
                    getPaint().setPathEffect(effects);
                    canvas.drawPath(path, getPaint());
                    path.reset();
                }
            }
            if (mCenterLayer.isShowVGrid()) {
                int vLineNum = mCenterLayer.getVLineCount();
                float width = mCenterLayer.getWidth();
                float perWidth = width / (vLineNum + 1);
                float x = mCenterLayer.getLeft();
                for (int i = 0; i < vLineNum; i++) {
                    x += perWidth;
                    canvas.drawLine(x, mCenterLayer.getTop(), x, mCenterLayer.getBottom(), getPaint());
                }
            }

            getPaint().reset();
            getPaint().setColor(mCenterLayer.getBorderColor());
            getPaint().setStrokeWidth(mCenterLayer.getBorderWidth());
            getPaint().setStyle(Style.STROKE);
            mCenterLayer.doDraw(canvas);
        }

        if (mRightLayer.isShow()) {
            getPaint().setColor(mRightLayer.getBorderColor());
            getPaint().setStrokeWidth(mRightLayer.getBorderWidth());
            getPaint().setStyle(Style.STROKE);

            if (mRightLayer.getIsShowBorder()) {
                //canvas.drawRect(mRightLayer.getRectF(), getPaint());
                mRightLayer.onDrawSide(canvas, getPaint());
            }

            if (mRightLayer.isShowHGrid()) {
                Path path = new Path();

                int hLineNum = mRightLayer.getHLineCount();
                float height = mRightLayer.getHeight();
                float perHeight = height / (hLineNum + 1);
                float top = mRightLayer.getTop();

                for (int i = 0; i < hLineNum; i++) {
                    top += perHeight;


                    path.moveTo(mRightLayer.getLeft(), top);
                    path.lineTo(mRightLayer.getRight(), top);
                    PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
                    getPaint().setPathEffect(effects);
                    canvas.drawPath(path, getPaint());
                    path.reset();
                }
            }
            if (mRightLayer.isShowVGrid()) {
                int vLineNum = mRightLayer.getVLineCount();
                float width = mRightLayer.getWidth();
                float perWidth = width / (vLineNum + 1);
                float x = mRightLayer.getLeft();
                for (int i = 0; i < vLineNum; i++) {
                    x += perWidth;
                    canvas.drawLine(x, mRightLayer.getTop(), x, mRightLayer.getBottom(), getPaint());
                }
            }

            getPaint().reset();
            getPaint().setColor(mRightLayer.getBorderColor());
            getPaint().setStrokeWidth(mRightLayer.getBorderWidth());
            getPaint().setStyle(Style.STROKE);
            mRightLayer.doDraw(canvas);
        }
    }

    @Override
    public void rePrepareWhenDrawing(RectF rect) {
        // TODO Auto-generated method stub
        mLeftLayer.rePrepareWhenDrawing(rect);
        mRightLayer.rePrepareWhenDrawing(rect);
        mCenterLayer.rePrepareWhenDrawing(rect);
    }

    public void layout(float left, float top, float right, float bottom) {
        super.layout(left, top, right, bottom);
        mLeftLayer.layout(mLeftLayer.getLeft(), top, mLeftLayer.getRight(), bottom);
        mRightLayer.layout(mRightLayer.getLeft(), top, mRightLayer.getRight(), bottom);
        mCenterLayer.layout(mCenterLayer.getLeft(), top, mCenterLayer.getRight(), bottom);
    }

    public void setLeftLayer(ChartLayer layer) {
        mLeftLayer = layer;
    }

    public void setRightLayer(ChartLayer layer) {
        mRightLayer = layer;
    }

    public void setCenterLayer(ChartLayer layer) {
        mCenterLayer = layer;
    }

    public void show(boolean isShow) {
        mLeftLayer.show(isShow);
        mRightLayer.show(isShow);
        mCenterLayer.show(isShow);
    }

    public boolean isShow() {
        return mRightLayer.isShow() || mLeftLayer.isShow() || mCenterLayer.isShow();
    }

    public boolean onActionShowPress(MotionEvent event) {
        if (mLeftLayer.onActionShowPress(event) || mRightLayer.onActionShowPress(event) || mCenterLayer.onActionShowPress(event)) {
            return true;
        }
        return false;
    }

    public void onActionUp(MotionEvent event) {
        mLeftLayer.onActionUp(event);
        mRightLayer.onActionUp(event);
        mCenterLayer.onActionUp(event);
    }

    public boolean onActionMove(MotionEvent event) {
        if (mLeftLayer.onActionMove(event) || mRightLayer.onActionMove(event) || mCenterLayer.onActionMove(event)) {
            return true;
        }
        return false;
    }

    public boolean onActionDown(MotionEvent event) {
        if (mLeftLayer.onActionDown(event) || mRightLayer.onActionDown(event) || mCenterLayer.onActionDown(event)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onActionDoubleTap(MotionEvent event) {
        if (mLeftLayer.onActionDoubleTap(event) || mRightLayer.onActionDoubleTap(event) || mCenterLayer.onActionDoubleTap(event)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onActionSingleTap(MotionEvent event) {
        if (mLeftLayer.onActionSingleTap(event) || mRightLayer.onActionSingleTap(event) || mCenterLayer.onActionSingleTap(event)) {
            return true;
        }
        return false;
    }

    public void setChartView(ChartView cv) {
        mLeftLayer.setChartView(cv);
        mRightLayer.setChartView(cv);
        mCenterLayer.setChartView(cv);
    }

    @Override
    public ChartLayer getLayerByTag(String tag) {
        if (mLeftLayer instanceof IGroupLayer) {
            IGroupLayer layer = (IGroupLayer) mLeftLayer;
            ChartLayer l = layer.getLayerByTag(tag);
            if (l != null) {
                return l;
            }
        }
        if (tag.equals(mLeftLayer.getTag())) {
            return mLeftLayer;
        }
        if (mRightLayer instanceof IGroupLayer) {
            IGroupLayer layer = (IGroupLayer) mRightLayer;
            ChartLayer l = layer.getLayerByTag(tag);
            if (l != null) {
                return l;
            }
        }
        if (tag.equals(mRightLayer.getTag())) {
            return mRightLayer;
        }

        if (mCenterLayer instanceof IGroupLayer) {
            IGroupLayer layer = (IGroupLayer) mCenterLayer;
            ChartLayer l = layer.getLayerByTag(tag);
            if (l != null) {
                return l;
            }
        }
        if (tag.equals(mCenterLayer.getTag())) {
            return mCenterLayer;
        }
        return null;
    }
}
