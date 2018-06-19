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

public class GroupLayer_shell extends ChartLayer implements IGroupLayer {
    public static final int SIDE_LEFT_V = 1;
    public static final int SIDE_MIDDLE_V = 2;
    public static final int SIDE_RIGHT_V = 4;
    public static final int SIDE_TOP_H = 8;
    public static final int SIDE_BOTTOM_H = 16;

    private ChartLayer mContentLayer = null;

    private int mShowSide = 0;
    private int mSideWidth = 0;
    private int mSideColor = 0;

    @Override
    public RectF prepareBeforeDraw(RectF rect) {

        mContentLayer.prepareBeforeDraw(rect);

        mLeft = rect.left;
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

        if (mContentLayer.isShow()) {
            getPaint().setColor(mContentLayer.getBorderColor());
            getPaint().setStrokeWidth(mContentLayer.getBorderWidth());
            getPaint().setStyle(Style.STROKE);

            if (mContentLayer.getIsShowBorder()) {
//                canvas.drawRect(mContentLayer.getRectF(), getPaint());
                mContentLayer.onDrawSide(canvas,getPaint());
            }

            if (mContentLayer.isShowHGrid()) {
                Path path = new Path();

                int hLineNum = mContentLayer.getHLineCount();
                float height = mContentLayer.getHeight();
                float perHeight = height / (hLineNum + 1);
                float top = mContentLayer.getTop();

                int interval = 4;

                for (int i = 0; i < hLineNum; i++) {
                    top += perHeight;

                    path.moveTo(mContentLayer.getLeft(), top);
                    path.lineTo(mContentLayer.getRight(), top);
                    PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
                    getPaint().setPathEffect(effects);
                    canvas.drawPath(path, getPaint());
                    path.reset();
                }

            }
            if (mContentLayer.isShowVGrid()) {
                Path path = new Path();
                int vLineNum = mContentLayer.getVLineCount();
                float width = mContentLayer.getWidth();
                float perWidth = width / (vLineNum + 1);
                float x = mContentLayer.getLeft();
                int interval = 4;


                for (int i = 0; i < vLineNum; i++) {
                    x += perWidth;

                    path.moveTo(x, mContentLayer.getTop());
                    path.lineTo(x, mContentLayer.getBottom());
                    PathEffect effects = new DashPathEffect(new float[]{4, 4, 4, 4}, 1);
                    getPaint().setPathEffect(effects);
                    canvas.drawPath(path, getPaint());
                    path.reset();
                }
            }
            getPaint().reset();
            getPaint().setColor(mContentLayer.getBorderColor());
            getPaint().setStrokeWidth(mContentLayer.getBorderWidth());
            getPaint().setStyle(Style.STROKE);
            mContentLayer.doDraw(canvas);
        }
    }

    @Override
    public void rePrepareWhenDrawing(RectF rect) {
        // TODO Auto-generated method stub
        mContentLayer.rePrepareWhenDrawing(rect);
    }

    public void layout(float left, float top, float right, float bottom) {
        super.layout(left, top, right, bottom);
        mContentLayer.layout(mContentLayer.getLeft(), top, mContentLayer.getRight(), bottom);
    }

    public void setContenLayer(ChartLayer layer) {
        mContentLayer = layer;
    }

    public void show(boolean isShow) {
        mContentLayer.show(isShow);
    }

    public boolean isShow() {
        return mContentLayer.isShow();
    }

    public boolean onActionShowPress(MotionEvent event) {
        if (mContentLayer.onActionShowPress(event)) {
            return true;
        }
        return false;
    }

    public void onActionUp(MotionEvent event) {
        mContentLayer.onActionUp(event);
    }

    public boolean onActionMove(MotionEvent event) {
        if (mContentLayer.onActionMove(event)) {
            return true;
        }
        return false;
    }

    public boolean onActionDown(MotionEvent event) {
        if (mContentLayer.onActionDown(event)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onActionDoubleTap(MotionEvent event) {
        if (mContentLayer.onActionDoubleTap(event)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onActionSingleTap(MotionEvent event) {
        if (mContentLayer.onActionSingleTap(event)) {
            return true;
        }
        return false;
    }

    public void setChartView(ChartView cv) {
        mContentLayer.setChartView(cv);
    }

    @Override
    public ChartLayer getLayerByTag(String tag) {
        if (mContentLayer instanceof IGroupLayer) {
            IGroupLayer layer = (IGroupLayer) mContentLayer;
            ChartLayer l = layer.getLayerByTag(tag);
            if (l != null) {
                return l;
            }
        }
        if (tag.equals(mContentLayer.getTag())) {
            return mContentLayer;
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
