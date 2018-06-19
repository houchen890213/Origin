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

public class GroupLayerOverlap_count3 extends ChartLayer implements IGroupLayer {
    private ChartLayer mLeftLayer = null;
    private ChartLayer mCenterLayer = null;
    private ChartLayer mRightLayer = null;

    private RectF mCenterRectF = null;

    @Override
    public RectF prepareBeforeDraw(RectF rect) {

        mLeftLayer.prepareBeforeDraw(rect);
        mRightLayer.prepareBeforeDraw(rect);
        mCenterRectF = new RectF(rect);
        mCenterLayer.prepareBeforeDraw(mCenterRectF);

        mLeft = rect.left;
        mTop = rect.top;
        mRight = rect.right;
        mBottom = rect.bottom;
        return rect;
    }

    @Override
    public void doDraw(Canvas canvas) {

        System.out.println("GroupLayerOverlap_count3 doDrawAimLine  p1");
        if (mCenterLayer.isShow()) {
            getPaint().setColor(mCenterLayer.getBorderColor());
            getPaint().setStrokeWidth(mCenterLayer.getBorderWidth());
            getPaint().setStyle(Style.STROKE);

            if (mCenterLayer.getIsShowBorder()) {
                //                RectF rectF = mCenterLayer.getRectF();
                //                canvas.drawRect(rectF, getPaint());
                mCenterLayer.onDrawSide(canvas, getPaint());
                System.out.println("GroupLayerOverlap_count3 onDrawSide  p2");
            }



            if (mCenterLayer.isShowHGrid()) {
                System.out.println("GroupLayerOverlap_count3 isShowHGrid  p3");
                Path path = new Path();

                int hLineNum = mCenterLayer.getHLineCount();
                float height = mCenterLayer.getHeight();
                float perHeight = height / (hLineNum + 1);
                float top = mCenterLayer.getTop();

                int middleIndex = -1;
                if (hLineNum >= 1 && hLineNum % 2 == 1) {
                    middleIndex = hLineNum / 2;
                }

                for (int i = 0; i < hLineNum; i++) {
                    top += perHeight;

                    float x_loc = mCenterLayer.getLeft();

                    if (middleIndex == i) {
                        getPaint().setPathEffect(null);
                        if (mCenterLayer.getMiddleHLineIsFull()) {
                            getPaint().reset();
                            getPaint().setColor(mCenterLayer.getBorderColor());
                            getPaint().setStrokeWidth(mCenterLayer.getBorderWidth());
                            getPaint().setStyle(Style.STROKE);
                            canvas.drawLine(x_loc, top, mCenterLayer.getRight(), top, getPaint());
                        } else {
                            path.moveTo(mCenterLayer.getLeft(), top);
                            path.lineTo(mCenterLayer.getRight(), top);
                            PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
                            getPaint().setPathEffect(effects);
                            canvas.drawPath(path, getPaint());
                            path.reset();
                        }
                    } else {
                        if (mCenterLayer.isGridLineIsDash()) {
                            path.moveTo(mCenterLayer.getLeft(), top);
                            path.lineTo(mCenterLayer.getRight(), top);
                            PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
                            getPaint().setPathEffect(effects);
                            canvas.drawPath(path, getPaint());
                            path.reset();
                        } else {
                            getPaint().reset();
                            getPaint().setColor(mCenterLayer.getBorderColor());
                            getPaint().setStrokeWidth(mCenterLayer.getBorderWidth());
                            getPaint().setStyle(Style.STROKE);
                            canvas.drawLine(x_loc, top, mCenterLayer.getRight(), top, getPaint());
                        }
                    }


                }


            }
            if (mCenterLayer.isShowVGrid()) {
                System.out.println("GroupLayerOverlap_count3 isShowVGrid  p4");
                Path path = new Path();
                int vLineNum = mCenterLayer.getVLineCount();
                float width = mCenterLayer.getWidth();
                float perWidth = width / (vLineNum + 1);
                float x = mCenterLayer.getLeft();


                for (int i = 0; i < vLineNum; i++) {
                    x += perWidth;

                    if (!mCenterLayer.isGridLineIsDash()) {
                        getPaint().reset();
                        getPaint().setColor(mCenterLayer.getBorderColor());
                        getPaint().setStrokeWidth(mCenterLayer.getBorderWidth());
                        getPaint().setStyle(Style.STROKE);
                        canvas.drawLine(x, mCenterLayer.getTop(), x, mCenterLayer.getBottom(), getPaint());
                    } else {
                        path.moveTo(x, mCenterLayer.getTop());
                        path.lineTo(x, mCenterLayer.getBottom());
                        PathEffect effects = new DashPathEffect(new float[]{4, 4, 4, 4}, 1);
                        getPaint().setPathEffect(effects);
                        canvas.drawPath(path, getPaint());
                        path.reset();
                    }
                }
            }

            getPaint().reset();
            getPaint().setColor(mCenterLayer.getBorderColor());
            getPaint().setStrokeWidth(mCenterLayer.getBorderWidth());
            getPaint().setStyle(Style.STROKE);

            mCenterLayer.doDraw(canvas);
        }

        if (mLeftLayer.isShow()) {

            mLeftLayer.doDraw(canvas);
        }

        if (mRightLayer.isShow()) {

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
