package com.yulin.chart.layers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;

/**
 * 纵坐标层
 *
 * @author Administrator
 */
public class YAxisLayer extends ChartLayer {

    protected float mMinWidth = 0;
    protected float mMinValue = 0;
    protected float mMaxValue = 0;

    protected boolean mIsDrawHeadTailCoordinate = true;
    protected boolean mIsDrawHeadCoordinate = true;
    protected boolean mIsDrawTailCoordinate = true;

    protected int mAxisCount = 2;
    protected float[] mAxisValues = null;
    protected float mSpace = 0;

    protected float mTextHeight = 0;
    protected float mAxisAreaWidth = 0;

    protected String mMinAxisWidthString = "";
    protected Align mAlign = Align.LEFT;
    protected int mTextSize = 18;

    protected float mStartX = 0;

    // 浮动坐标
    protected Paint mFloatCoordinatePaint = null;
    // 为了让文字居中对齐的offset
    protected float mFloatCoordinateOffsetY = 0;
    protected float mFloatCoordinateRectHeight = 0;

    private boolean mIsPutCoordinateAboveLine = false;

    @Override
    public RectF prepareBeforeDraw(RectF rect) {
        // TODO Auto-generated method stub
        mTop = rect.top;
        mLeft = rect.left;
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

        if (mAlign == Align.LEFT) {
            mStartX = mLeft + mPaddingLeft;
        } else if (mAlign == Align.RIGHT) {
            mStartX = mLeft + mAxisAreaWidth + mPaddingLeft;
        } else if (mAlign == Align.CENTER) {
            mStartX = mLeft + mAxisAreaWidth / 2 + mPaddingLeft;
        }

        mRight = mLeft + mAxisAreaWidth + mPaddingLeft + mPaddingRight;

        return new RectF(mLeft, mTop, mRight, mBottom);
    }

    @Override
    public void doDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        // getPaint().setColor(mColor);
        // getPaint().setTextSize(mTextSize);
        // getPaint().setAntiAlias(true);
        // getPaint().setTextAlign(mAlign);

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
                if (mIsDrawHeadTailCoordinate == true && mIsDrawHeadCoordinate) {
                    if (data != null) {
                        // 判断是否为自定义富坐标格式
                        // 主文本|文本1,int型颜色|文本2,int型颜色|文本3,int型颜色
                        if (data.contains("|")) {

                            String lineTag = "　－ ";
                            float lineTagWidth = getPaint().measureText(lineTag);

                            String[] richTexts = data.split("\\|");

                            float tStartX = mStartX;
                            float tStartY = startY + mTextHeight / 2;
                            if (richTexts != null && richTexts.length > 0) {
                                canvas.drawText(richTexts[0], tStartX, tStartY, getPaint());
                                tStartX += getPaint().measureText(richTexts[0]);
                            }

                            if (richTexts != null && richTexts.length > 1) {
                                for (int j = 1; j < richTexts.length; j++) {
                                    String richText = richTexts[j];
                                    String[] txt_color = richText.split(",");

                                    int tColor = getPaint().getColor();

                                    int richColr = 0xff000000;
                                    try {
                                        richColr = Integer.parseInt(txt_color[1]);
                                    } catch (Exception e) {
                                    }
                                    getPaint().setColor(richColr);
                                    getPaint().setFakeBoldText(true);

                                    canvas.drawText(lineTag, tStartX, tStartY, getPaint());
                                    tStartX += lineTagWidth;
                                    //                                    getPaint().setColor(tColor);
                                    getPaint().setFakeBoldText(false);
                                    canvas.drawText(txt_color[0], tStartX, tStartY, getPaint());
                                    tStartX += getPaint().measureText(txt_color[0]);
                                }
                            }


                        } else {
                            canvas.drawText(data, mStartX, startY + mTextHeight / 2, getPaint());
                        }
                    }
                }
            } else if (i == mAxisCount - 1) {
                if (mIsDrawHeadTailCoordinate == true && mIsDrawTailCoordinate) {
                    canvas.drawText(data, mStartX, startY - mTextHeight / 2, getPaint());
                }
            } else {
                float y = 0;
                if (mIsPutCoordinateAboveLine) {
                    y = startY - mTextHeight / 2 - 1;
                } else {
                    y = startY;
                }
                canvas.drawText(data, mStartX, y, getPaint());
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
            mFloatCoordinatePaint.setTextAlign(mTaFloatCoordinate.getTextAlign());
            mFloatCoordinatePaint.setFakeBoldText(true);
            Paint.FontMetrics fm_float = mFloatCoordinatePaint.getFontMetrics();
            // *****让文件居中对齐的offset算法*****
            mFloatCoordinateOffsetY = (fm_float.bottom - fm_float.top) / 2 - fm_float.bottom;
        }

        mFloatCoordinatePaint.setColor(mTaFloatCoordinate.getTextColor());
        // float newY = mTaFloatCoordinate.getCoorDinateY() + mFloatCoordinateOffsetY;
        float newY = mTaFloatCoordinate.getCoorDinateY();
        canvas.drawText(mTaFloatCoordinate.getText(), mStartX, newY, mFloatCoordinatePaint);
    }

    public void setDrawHeadAndTailCoordinate(boolean b) {
        mIsDrawHeadTailCoordinate = b;
    }

    public void setPutCoordinateAboveLine(boolean b) {
        mIsPutCoordinateAboveLine = b;
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

    protected OnFormatDataListener mFormatDataListener = null;

    public void setOnFormatDataListener(OnFormatDataListener listener) {
        mFormatDataListener = listener;
    }

    // 浮动坐标
    protected boolean mIsFloatCoordinateOn = false;
    protected TextAtom mTaFloatCoordinate = null;
    protected int mFloatCoordinateBgColor = 0xffffffff;


    public void switchFloatCoordinateOn(boolean isOn) {
        mIsFloatCoordinateOn = isOn;
    }

    public void setFloatCoordinateBgColor(int bgColor) {
        mFloatCoordinateBgColor = bgColor;
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
