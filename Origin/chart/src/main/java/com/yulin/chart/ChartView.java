package com.yulin.chart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.yulin.chart.layers.ChartLayer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ChartView extends View implements OnTouchListener {

    private List<ChartLayer> mLstLayers = new ArrayList<>();
    private RectF mChartAreaRectF = null;

    private Paint mPaint = new Paint();
    private GestureDetector mGestureDetector = null;

    private boolean mHasLayouted = false;

    public static final int ACTION_NONE = 0;// 正常状态
    public static final int ACTION_ZOOM = 1;// 缩放状态
    public int mActionType = ACTION_NONE;
    public float mLastDistance = 1f;
    private static final float MIN_ZOOM_DISTANCE = 10f;
    private boolean mIsSupportZoom = true;

    private float mScale = 1;

    private float mLeft = 0;
    private float mTop = 0;
    private float mRight = 0;
    private float mBottom = 0;

    private WeakReference<Bitmap> mDrawBitmap;
    private Canvas mDrawCanvas;
    private Paint mRenderPaint;

    public ChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChartView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mGestureDetector = new GestureDetector(new MyOnGestureListener());
        mRenderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setOnTouchListener(this);
        setFocusable(true);
        setLongClickable(true);
        setClickable(true);
        setFocusableInTouchMode(true);
        requestFocus();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDrawBitmap == null || mDrawBitmap.get() == null || mDrawBitmap.get().isRecycled()
                || mDrawBitmap.get().getWidth() != getWidth() || mDrawBitmap.get().getHeight() != getHeight()) {
            Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            mDrawBitmap = new WeakReference<>(bitmap);
            mDrawCanvas = new Canvas(mDrawBitmap.get());
        }

        super.onDraw(canvas);
        mDrawBitmap.get().eraseColor(Color.TRANSPARENT);
        if (mChartAreaRectF == null) {
            forceAdjustLayers();
        }
        rePrepareWhenDrawing(mChartAreaRectF);
        //不直接绘制在canvas上，这样当该view在scrollview中滚动的时候，系统会做优化（硬件加速开启时）,不会很卡
        //如果硬件加速被关闭，则可以考虑加一些flag使滚动时直接绘制mDrawBitmap
        //        doDrawAimLine(canvas);
        doDraw(mDrawCanvas);
        canvas.drawBitmap(mDrawBitmap.get(), 0, 0, mRenderPaint);
    }

    /**
     * 强制重新调整所有layers
     */
    public void forceAdjustLayers() {
        mLeft = getPaddingLeft();
        mTop = getPaddingTop();
        mRight = getMeasuredWidth() - getPaddingRight();
        mBottom = getMeasuredHeight() - getPaddingBottom();
        mChartAreaRectF = new RectF(mLeft, mTop, mRight, mBottom);
        prepareBeforeDraw(mChartAreaRectF);
    }

    public int getAvailHeight() {
        return getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
    }

    public int getAvailWidth() {
        return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    private void rePrepareWhenDrawing(RectF rectF) {
        for (int i = 0; i < mLstLayers.size(); i++) {
            mLstLayers.get(i).rePrepareWhenDrawing(rectF);
        }
    }

    private void prepareBeforeDraw(RectF rectF) {
        float usedHeight = 0;
        for (int i = 0; i < mLstLayers.size(); i++) {
            ChartLayer layer = mLstLayers.get(i);
            layer.prepareBeforeDraw(rectF);
            if (layer.getHeightPercent() == 0) {
                usedHeight += layer.getHeight();
            }
        }
        float leftHeight = getAvailHeight() - usedHeight;

        float tTop = mTop;
        for (int i = 0; i < mLstLayers.size(); i++) {
            ChartLayer layer = mLstLayers.get(i);
            float height = 0;
            if (layer.getHeightPercent() == 0) {
                height = layer.getHeight();
                layer.layout(layer.getLeft(), tTop, layer.getRight(), tTop + height);
                tTop += height;
            } else {
                height = leftHeight * layer.getHeightPercent();
                layer.layout(layer.getLeft(), tTop, layer.getRight(), tTop + height);
                tTop += height;
            }
        }
    }

    private void doDraw(Canvas canvas) {
        for (int i = 0; i < mLstLayers.size(); i++) {
            ChartLayer layer = mLstLayers.get(i);
            if (layer.isShow()) {
                mPaint.setColor(layer.getBorderColor());
                mPaint.setStrokeWidth(layer.getBorderWidth());
                mPaint.setStyle(Style.STROKE);
                if (layer.getIsShowBorder()) {
                    canvas.drawRect(layer.getRectF(), mPaint);
                }

                layer.doDraw(canvas);
            }
        }
    }

    public void addLayer(ChartLayer layer) {
        layer.setChartView(this);
        mLstLayers.add(layer);
    }

    public void addLayer(int index, ChartLayer layer) {
        layer.setChartView(this);
        mLstLayers.add(index, layer);
    }

    public void setLayer(int index, ChartLayer layer) {
        layer.setChartView(this);
        mLstLayers.set(index, layer);
    }

    public void removeLayer(int index) {
        mLstLayers.remove(index);
    }

    public void removeAllLayers() {
        mLstLayers.clear();
    }

    public void removeLayer(ChartLayer layer) {
        mLstLayers.remove(layer);
    }

    public void clearLayers() {
        mLstLayers.clear();
    }

    public int getLayerCount() {
        return mLstLayers.size();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // TODO Auto-generated method stub
        super.onLayout(changed, left, top, right, bottom);
        mHasLayouted = true;
        // if(mFinishLayoutListener != null)
        // {
        // mFinishLayoutListener.onFinishLayout();
        // }
        forceAdjustLayers();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
        forceAdjustLayers();
        invalidate();

        if (mOnChangeSizeListener != null) {
            mOnChangeSizeListener.onSizeChanged(getCanvasRect());
        }
    }

    public static interface OnFinishLayoutListener {
        public void onFinishLayout();
    }

    // private OnFinishLayoutListener mFinishLayoutListener = null;
    // public void setOnFinishLayoutListener(OnFinishLayoutListener listener)
    // {
    // mFinishLayoutListener = listener;
    // }

    public boolean hasLayouted() {
        return mHasLayouted;
    }

    class MyOnGestureListener extends SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return dispatchGestureDown(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            //时间太长,改为使用showPress
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            if (isInZooming()) {
                return;
            }
            boolean result = dispatchGestureShowPress(e);
            if (getParent() != null) {
                if (result) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
            }
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            dispatchGestureUp(e);
            // getParent().requestDisallowInterceptTouchEvent(false);
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            dispatchGestureDoubleTap(e);
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            dispatchGestureSingleTap(e);
            return super.onSingleTapConfirmed(e);
        }

    }

    protected boolean dispatchGestureShowPress(MotionEvent event) {
        for (int i = 0; i < mLstLayers.size(); i++) {
            if (mLstLayers.get(i).onActionShowPress(event)) {
                return true;
            }
        }
        return false;
    }

    protected void dispatchGestureUp(MotionEvent event) {
        for (int i = 0; i < mLstLayers.size(); i++) {
            mLstLayers.get(i).onActionUp(event);
        }
    }

    protected boolean dispatchActionMove(MotionEvent event) {
        for (int i = 0; i < mLstLayers.size(); i++) {
            if (mLstLayers.get(i).onActionMove(event)) {
                return true;
            }
        }
        return false;
    }

    protected boolean dispatchGestureDown(MotionEvent event) {
        for (int i = 0; i < mLstLayers.size(); i++) {
            if (mLstLayers.get(i).onActionDown(event)) {
                return true;
            }
        }
        return false;
    }

    protected boolean dispatchGestureDoubleTap(MotionEvent event) {
        if (mDoubleTapListener != null) {
            mDoubleTapListener.onDoubleTap();
        }

        for (int i = 0; i < mLstLayers.size(); i++) {
            if (mLstLayers.get(i).onActionDoubleTap(event)) {
                return true;
            }
        }
        return false;
    }

    protected boolean dispatchGestureSingleTap(MotionEvent event) {
        if (mSingleTapListener != null) {
            mSingleTapListener.onSingleTapConfirm();
        }

        for (int i = 0; i < mLstLayers.size(); i++) {
            if (mLstLayers.get(i).onActionSingleTap(event)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dispatchActionMove(event);
        doMultiPointsZoom(event);
        return mGestureDetector.onTouchEvent(event);
    }

    private void doMultiPointsZoom(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                // Log.v("ZYL", "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                // Log.v("ZYL", "ACTION_POINTER_DOWN");
                if (mZoomActionListener != null) {
                    if (mIsSupportZoom) {
                        checkMultiTouch(event);
                    }
                    if (mActionType == ACTION_ZOOM) {
                        // getParent().requestDisallowInterceptTouchEvent(true);
                        mZoomActionListener.onStartZoom();
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                // Log.v("ZYL", "ACTION_CANCEL");
                // break;
            case MotionEvent.ACTION_UP:
                dispatchGestureUp(event);
                // Log.v("ZYL", "ACTION_UP");
            case MotionEvent.ACTION_POINTER_UP:
                // Log.v("ZYL", "ACTION_POINTER_UP");
                if (mZoomActionListener != null) {
                    if (mActionType == ACTION_ZOOM) {
                        mActionType = ACTION_NONE;
                        // getParent().requestDisallowInterceptTouchEvent(false);
                        mZoomActionListener.onFinishZoom(mScale);
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // Log.v("ZYL", "ACTION_MOVE");
                if (mZoomActionListener != null) {
                    if (mActionType == ACTION_ZOOM) {
                        float newDist = distance(event);
                        if (newDist > MIN_ZOOM_DISTANCE) {
                            mScale = newDist / mLastDistance;
                            mZoomActionListener.onZooming(mScale);
                            invalidate();
                        }
                    }
                }
                break;
        }
    }

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

    public boolean isInZooming() {
        return mActionType == ACTION_ZOOM;
    }

    public void checkMultiTouch(MotionEvent event) {
        mLastDistance = distance(event);
        if (mLastDistance > MIN_ZOOM_DISTANCE && checkMultiTouchRange(event.getY(0), event.getY(1))) {
            mActionType = ACTION_ZOOM;
        }
    }

    public boolean checkMultiTouchRange(float py1, float py2) {
        int top = 0;
        int bottom = getMeasuredHeight();
        if (py1 >= top && py1 <= bottom && py2 >= top && py2 <= bottom) {
            return true;
        }
        return false;
    }

    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    protected OnZoomActionListener mZoomActionListener = null;

    public static interface OnZoomActionListener {
        public void onStartZoom();

        public void onFinishZoom(float rate);

        public void onZooming(float rate);
    }

    public void setOnZoomActionListener(OnZoomActionListener listener) {
        mZoomActionListener = listener;
    }

    public static interface OnDoubleTapListener {
        public void onDoubleTap();
    }

    protected OnDoubleTapListener mDoubleTapListener = null;

    public void setOnDoubleTapListener(OnDoubleTapListener listener) {
        mDoubleTapListener = listener;
    }

    public static interface OnSingleTapListener {
        public void onSingleTapConfirm();
    }

    protected OnSingleTapListener mSingleTapListener = null;

    public void setOnSingleTapListener(OnSingleTapListener listener) {
        mSingleTapListener = listener;
    }

    public static interface IGroupLayer {
        public ChartLayer getLayerByTag(String tag);
    }

    public RectF getCanvasRect() {
        return new RectF(mLeft, mTop, mRight, mBottom);
    }


    private OnChangeSizeListener mOnChangeSizeListener = null;

    public void setOnChangeSizeListener(OnChangeSizeListener onChangeSizeListener) {
        mOnChangeSizeListener = onChangeSizeListener;
    }

    public interface OnChangeSizeListener {
        public void onSizeChanged(RectF rectf);
    }

}
