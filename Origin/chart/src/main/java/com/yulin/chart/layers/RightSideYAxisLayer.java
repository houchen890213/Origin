package com.yulin.chart.layers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * 右边纵坐标层
 *
 * @author Administrator
 */
public class RightSideYAxisLayer extends ChartLayer {

    private float mMinWidth = 0;
    private float mMinValue = 0;
    private float mMaxValue = 0;


    private int mAxisCount = 2;
    private float[] mAxisValues = null;
    private float mSpace = 0;

    private float mTextHeight = 0;
    private float mAxisAreaWidth = 0;

    private String mMinAxisWidthString = "";
    private Align mAlign = Align.LEFT;
    private int mTextSize = 18;

    private float mStartX = 0;
    // 浮动坐标,百分比
    private Paint mFloatCoordinatePaint = null;

    private int mFloatCoordinateBgColor = 0xffffffff;
    // 为了让文字居中对齐的offset
    private float mFloatCoordinateOffsetY = 0;
    private float mFloatCoordinateRectHeight = 0;
    private boolean mIsDrawHeadTailCoordinate = true;
    private boolean mIsDrawHeadCoordinate = true;
    private boolean mIsDrawTailCoordinate = true;

    @Override
    public RectF prepareBeforeDraw(RectF rect) {
        // TODO Auto-generated method stub
        mTop = rect.top;
        mRight = rect.right;
        mBottom = rect.bottom;

        getPaint().setColor(mColor);
        getPaint().setTextSize(mTextSize);
        getPaint().setAntiAlias(true);
        getPaint().setTextAlign(mAlign);

        Paint.FontMetrics fm = getPaint().getFontMetrics();
        mTextHeight = (float) (Math.ceil(fm.descent - fm.ascent) + 2);

        mAxisValues = new float[mAxisCount];
        float offsetVal = mMaxValue - mMinValue;
        float perVal = offsetVal / (mAxisCount - 1);

        mAxisAreaWidth = mMinWidth;

        float ww = getPaint().measureText(mMinAxisWidthString);
        if (ww > mAxisAreaWidth) {
            mAxisAreaWidth = ww;
        }
        for (int i = 0; i < mAxisValues.length; i++) {
            if (i == 0) {
                mAxisValues[i] = mMaxValue;
            } else if (i == mAxisCount - 1) {
                mAxisValues[i] = mMinValue;
            } else {
                mAxisValues[i] = mMaxValue - perVal * i;
            }
            String data = String.valueOf(mAxisValues[i]);
            if (mFormatDataListener != null) {
                data = mFormatDataListener.onFormatData(mAxisValues[i]);
            }
            float w = getPaint().measureText(data);
            if (w > mAxisAreaWidth) {
                mAxisAreaWidth = w;
            }
        }

        mLeft = mRight - mPaddingRight - mPaddingLeft - mAxisAreaWidth;

        if (mAlign == Align.LEFT) {
            mStartX = mLeft + mPaddingLeft;
        } else if (mAlign == Align.RIGHT) {
            mStartX = mLeft + mAxisAreaWidth + mPaddingLeft;
        } else if (mAlign == Align.CENTER) {
            mStartX = mLeft + mAxisAreaWidth / 2 + mPaddingLeft;
        }

        return new RectF(mLeft, mTop, mRight, mBottom);
    }

    @Override
    public void doDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        Paint.FontMetrics fm = getPaint().getFontMetrics();
        float startY = mTop + mPaddingTop + (fm.top - fm.bottom) / 2 - fm.top;
        for (int i = 0; i < mAxisCount; i++) {
            if (mDrawingListener != null) {
                mDrawingListener.onDrawing(getPaint(), i);
            }
            String data = String.valueOf(mAxisValues[i]);
            if (mFormatDataListener != null) {
                data = mFormatDataListener.onFormatData(mAxisValues[i]);
            }
            if (i == 0) {
                if (mIsDrawHeadTailCoordinate && mIsDrawHeadCoordinate == true) {
                    canvas.drawText(data, mStartX, startY + mTextHeight / 2, getPaint());
                }
            } else if (i == mAxisCount - 1) {
                if (mIsDrawHeadTailCoordinate && mIsDrawTailCoordinate == true) {
                    canvas.drawText(data, mStartX, startY - mTextHeight / 2, getPaint());
                }
            } else {
                canvas.drawText(data, mStartX, startY, getPaint());
            }
            startY += mSpace;
        }

        if (!mIsFloatCoordinateOn || mTaFloatCoordinate == null) {
            return;
        }
        if (mTaFloatCoordinate.getText() == null || mTaFloatCoordinate.equals("")) {
            return;
        }


        if (mFloatCoordinatePaint == null) {
            mFloatCoordinatePaint = new Paint();
            mFloatCoordinatePaint.setColor(mTaFloatCoordinate.getTextColor());
            mFloatCoordinatePaint.setTextSize(mTaFloatCoordinate.getTextSize());
            mFloatCoordinatePaint.setAntiAlias(true);
            mFloatCoordinatePaint.setTextAlign(mAlign);
            mFloatCoordinatePaint.setFakeBoldText(true);
            Paint.FontMetrics fm_float = mFloatCoordinatePaint.getFontMetrics();
            // *****让文件居中对齐的offset算法*****
            float tHeight = fm_float.bottom - fm_float.top;
            mFloatCoordinateRectHeight = tHeight + 8;
            mFloatCoordinateOffsetY = tHeight / 2 - fm_float.bottom;
        }

        int tRectLeftX = (int) (mRight - getPaint().measureText(mTaFloatCoordinate.getText()) - 8);
        int tRectLeftY = (int) (mTaFloatCoordinate.getCoorDinateY() - mFloatCoordinateRectHeight / 2);
        if (tRectLeftY < mTop) {
            tRectLeftY = (int) mTop;
        } else if (tRectLeftY + mFloatCoordinateRectHeight > mBottom) {
            tRectLeftY = (int) (mBottom - mFloatCoordinateRectHeight);
        }


        int tRectRightY = (int) (tRectLeftY + mFloatCoordinateRectHeight);
        int tRectRightX = (int) (mRight);
        Rect rect = new Rect(tRectLeftX, tRectLeftY, tRectRightX, tRectRightY);

        Paint paintbg = new Paint();
        paintbg.setStrokeWidth(0);
        paintbg.setStyle(Style.FILL);
        paintbg.setColor(mFloatCoordinateBgColor);
        canvas.drawRect(rect, paintbg);

        paintbg.setStrokeWidth(2);
        paintbg.setStyle(Style.STROKE);
        paintbg.setColor(0xff828282);
        canvas.drawRect(rect, paintbg);

        float newY = tRectLeftY + mFloatCoordinateRectHeight / 2 + mFloatCoordinateOffsetY;

        mFloatCoordinatePaint.setColor(mTaFloatCoordinate.getTextColor());
        canvas.drawText(mTaFloatCoordinate.getText(), mStartX, newY, mFloatCoordinatePaint);
    }

    public void setDrawHeadAndTailCoordinate(boolean b) {
        mIsDrawHeadTailCoordinate = b;
    }


    public void setDrawHeadCoordinate(boolean b) {
        mIsDrawHeadCoordinate = b;
    }

    public void setDrawTailCoordinate(boolean b) {
        mIsDrawTailCoordinate = b;
    }

    public float getValue(int pos) {
        return mAxisValues[pos];
    }

    public void setMinWidth(float minWidth) {
        mMinWidth = minWidth;
    }

    public void setMaxValue(float maxVal) {
        mMaxValue = maxVal;
    }

    public float getMaxValue() {
        return mMaxValue;
    }

    public void setMinValue(float minVal) {
        mMinValue = minVal;
    }

    public float getMinValue() {
        return mMinValue;
    }

    public void setAxisCount(int count) {
        mAxisCount = count;
    }

    public int getAxisCount() {
        return mAxisCount;
    }

    public void setAlign(Align align) {
        mAlign = align;
    }

    public void setTextSize(int size) {
        mTextSize = size;
    }

    public void setMinWidthString(String str) {
        mMinAxisWidthString = str;
    }

    public float getAxisWidth() {
        return mAxisAreaWidth;
    }

    @Override
    public void rePrepareWhenDrawing(RectF rect) {
        // TODO Auto-generated method stub
        float totalHeight = mBottom - mTop - mPaddingTop - mPaddingBottom;
        mSpace = totalHeight / (mAxisCount - 1);
    }

    public static interface OnFormatDataListener {
        public String onFormatData(float val);
    }

    private OnFormatDataListener mFormatDataListener = null;

    public void setOnFormatDataListener(OnFormatDataListener listener) {
        mFormatDataListener = listener;
    }

    // 浮动坐标
    private boolean mIsFloatCoordinateOn = false;
    private TextAtom mTaFloatCoordinate = null;

    public void setFloatCoordinateBgColor(int bgColor) {
        mFloatCoordinateBgColor = bgColor;
    }

    public void switchFloatCoordinateOn(boolean isOn) {
        mIsFloatCoordinateOn = isOn;
    }

    public void setFloatCoordinateText(TextAtom ta) {
        mTaFloatCoordinate = ta;
    }

    public static class TextAtom {
        private String mText = "";
        private int mTextColor = Color.BLACK;
        private int mTextSize = 18;
        private Align mTextAlign = Align.LEFT;
        private float mCoorDinateY = 0;

        public TextAtom(String text, int textColor, int textSize, Align align, float coordinateY) {
            mText = text;
            mTextColor = textColor;
            mTextSize = textSize;
            mTextAlign = align;
            mCoorDinateY = coordinateY;
        }

        public float getCoorDinateY() {
            return mCoorDinateY;
        }

        public void setCoorDinateY(float coorDinateY) {
            this.mCoorDinateY = coorDinateY;
        }

        public int getTextColor() {
            return mTextColor;
        }

        public void setTextColor(int color) {
            mTextColor = color;
        }

        public String getText() {
            return mText;
        }

        public void setText(String text) {
            mText = text;
        }

        public int getTextSize() {
            return mTextSize;
        }

        public void setTextSize(int textSize) {
            mTextSize = textSize;
        }

        public Align getTextAlign() {
            return mTextAlign;
        }

        public void setTextAlign(Align textAlign) {
            this.mTextAlign = textAlign;
        }

    }

    /**
     * 获取坐标左上角相对画布的dy
     *
     * @param cdHeight 坐标的高度
     */
    public int getCoordinateMarginTop(float cdHeight, float point_y) {

        int tRectY = (int) (point_y - cdHeight / 2);
        if (tRectY < mTop) {
            tRectY = (int) mTop;
        } else if (tRectY + cdHeight > mBottom) {
            tRectY = (int) (mBottom - cdHeight);
        }

        RectF cvRectF = getCanvasRect();
        if (cvRectF != null)
            return (int) (tRectY - cvRectF.top);
        return (int) (tRectY - mTop); //异常情况
    }
}
