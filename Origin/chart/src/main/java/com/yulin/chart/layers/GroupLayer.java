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

public class GroupLayer extends ChartLayer implements IGroupLayer {
    public static final int SIDE_LEFT_V = 1;
    public static final int SIDE_MIDDLE_V = 2;
    public static final int SIDE_RIGHT_V = 4;
    public static final int SIDE_TOP_H = 8;
    public static final int SIDE_BOTTOM_H = 16;


    private ChartLayer mLeftLayer = null;
    private ChartLayer mRightLayer = null;

    private RectF mLeftRectF = null;
    private RectF mRightRectF = null;
    private int mShowSide = 0;
    private int mSideWidth = 0;
    private int mSideColor = 0;

    @Override
    public RectF prepareBeforeDraw(RectF rect) {

        mLeftRectF = mLeftLayer.prepareBeforeDraw(rect);
        mRightRectF = new RectF(mLeftRectF.right, rect.top, rect.right, rect.bottom);
        mRightLayer.prepareBeforeDraw(mRightRectF);

        mLeft = mLeftRectF.left;
        mTop = rect.top;
        mRight = rect.right;
        mBottom = rect.bottom;
        return rect;
    }

    @Override
    public void doDraw(Canvas canvas) {
        if ((mShowSide & SIDE_TOP_H) == SIDE_TOP_H) {
            getPaint().setColor(mSideColor);
            getPaint().setStrokeWidth(mSideWidth);
            getPaint().setStyle(Style.STROKE);

            canvas.drawLine(mLeft, mTop, mRight, mTop, getPaint());
        }

        if ((mShowSide & SIDE_BOTTOM_H) == SIDE_BOTTOM_H) {
            getPaint().setColor(mSideColor);
            getPaint().setStrokeWidth(mSideWidth);
            getPaint().setStyle(Style.STROKE);

            canvas.drawLine(mLeft, mBottom, mRight, mBottom, getPaint());
        }

        if ((mShowSide & SIDE_LEFT_V) == SIDE_LEFT_V) {
            getPaint().setColor(mSideColor);
            getPaint().setStrokeWidth(mSideWidth);
            getPaint().setStyle(Style.STROKE);

            canvas.drawLine(mLeft, mTop, mLeft, mBottom, getPaint());
        }

        if ((mShowSide & SIDE_RIGHT_V) == SIDE_RIGHT_V) {
            getPaint().setColor(mSideColor);
            getPaint().setStrokeWidth(mSideWidth);
            getPaint().setStyle(Style.STROKE);

            canvas.drawLine(mRight, mTop, mRight, mBottom, getPaint());
        }


        if (mLeftLayer.isShow()) {
            getPaint().setColor(mLeftLayer.getBorderColor());
            getPaint().setStrokeWidth(mLeftLayer.getBorderWidth());
            getPaint().setStyle(Style.STROKE);

            if (mLeftLayer.getIsShowBorder()) {
                canvas.drawRect(mLeftLayer.getRectF(), getPaint());
            }

            if ((mShowSide & SIDE_MIDDLE_V) == SIDE_MIDDLE_V) {
                getPaint().setColor(mSideColor);
                getPaint().setStrokeWidth(mSideWidth);
                getPaint().setStyle(Style.STROKE);

                canvas.drawLine(mLeftLayer.mRight, mLeftLayer.mTop, mLeftLayer.mRight, mLeftLayer.mBottom, getPaint());
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

        if (mRightLayer.isShow()) {
            getPaint().setColor(mRightLayer.getBorderColor());
            getPaint().setStrokeWidth(mRightLayer.getBorderWidth());
            getPaint().setStyle(Style.STROKE);

            if (mRightLayer.getIsShowBorder()) {
                canvas.drawRect(mRightLayer.getRectF(), getPaint());
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

                Path path = new Path();
                int vLineNum = mRightLayer.getVLineCount();
                float width = mRightLayer.getWidth();
                float perWidth = width / (vLineNum + 1);
                float x = mRightLayer.getLeft();


                for (int i = 0; i < vLineNum; i++) {
                    x += perWidth;


                    path.moveTo(x, mRightLayer.getTop());
                    path.lineTo(x, mRightLayer.getBottom());
                    PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
                    getPaint().setPathEffect(effects);
                    canvas.drawPath(path, getPaint());
                    path.reset();
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
    }

    public void layout(float left, float top, float right, float bottom) {
        super.layout(left, top, right, bottom);
        mLeftLayer.layout(mLeftLayer.getLeft(), top, mLeftLayer.getRight(), bottom);
        mRightLayer.layout(mRightLayer.getLeft(), top, mRightLayer.getRight(), bottom);
    }

    public void setLeftLayer(ChartLayer layer) {
        mLeftLayer = layer;
    }

    public void setRightLayer(ChartLayer layer) {
        mRightLayer = layer;
    }

    public void show(boolean isShow) {
        mLeftLayer.show(isShow);
        mRightLayer.show(isShow);
    }

    public boolean isShow() {
        return mRightLayer.isShow() || mLeftLayer.isShow();
    }

    public boolean onActionShowPress(MotionEvent event) {
        if (mLeftLayer.onActionShowPress(event) || mRightLayer.onActionShowPress(event)) {
            return true;
        }
        return false;
    }

    public void onActionUp(MotionEvent event) {
        mLeftLayer.onActionUp(event);
        mRightLayer.onActionUp(event);
    }

    public boolean onActionMove(MotionEvent event) {
        if (mLeftLayer.onActionMove(event) || mRightLayer.onActionMove(event)) {
            return true;
        }
        return false;
    }

    public boolean onActionDown(MotionEvent event) {
        if (mLeftLayer.onActionDown(event) || mRightLayer.onActionDown(event)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onActionDoubleTap(MotionEvent event) {
        if (mLeftLayer.onActionDoubleTap(event) || mRightLayer.onActionDoubleTap(event)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onActionSingleTap(MotionEvent event) {
        if (mLeftLayer.onActionSingleTap(event) || mRightLayer.onActionSingleTap(event)) {
            return true;
        }
        return false;
    }

    public void setChartView(ChartView cv) {
        mLeftLayer.setChartView(cv);
        mRightLayer.setChartView(cv);
    }

    @Override
    public ChartLayer getLayerByTag(String tag) {
        if (mLeftLayer instanceof IGroupLayer) {
            ChartView.IGroupLayer layer = (IGroupLayer) mLeftLayer;
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
        return null;
    }


    public void setSideColor(int color) {
        mSideColor = color;
    }

    public void setSideWidth(int width) {
        mSideWidth = width;
    }

    public void setShowSide(int side) {
        mShowSide = side;
    }
}
