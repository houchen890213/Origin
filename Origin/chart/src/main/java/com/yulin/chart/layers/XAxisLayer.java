package com.yulin.chart.layers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

/**
 * 横坐标
 *
 * @author Administrator
 */
public class XAxisLayer extends ChartLayer {

    private Align mAlign = Align.CENTER;
    private int mTextSize = 18;
    private float mTextHeight = 0;

    private float mSpace = 0;
    private float mTotalWidth = 0;
    private String mMinLeftPaddingString = "";
    private String mMinRightPaddingString = "";
    private float mAxisLeftPadding = 0;
    private float mAxisRighttPadding = 0;
    private boolean mIsSameWidth;//类似linearLayout 同样的weight效果。均等分放入各自的位置。位置为居中。
    private List<String> mLstAxises = new ArrayList<>();

    ////////////////////////// 浮动坐标 //////////////////////////////////
    /**
     * 是否显示浮动坐标
     */
    boolean mIsFloatCoordinateOn = false;
    /**
     * 浮动坐标内容
     */
    TextAtom mTaFloatCoordinate = null;
    Paint mFloatCoordinatePaint = null;
    int mFloatCoordinateBgColor = 0xffffffff;
    float mFloatCoordinateRectWidth = 0;

    @Override
    public RectF prepareBeforeDraw(RectF rect) {
        getPaint().setColor(mColor);
        getPaint().setTextSize(mTextSize);
        getPaint().setAntiAlias(true);
        getPaint().setTextAlign(mAlign);

        Paint.FontMetrics fm = getPaint().getFontMetrics();
        mTextHeight = (float) (Math.ceil(fm.descent - fm.ascent) + 2);

        float ww = getPaint().measureText(mMinLeftPaddingString);
        if (ww > mAxisLeftPadding) {
            mAxisLeftPadding = ww;
        }

        ww = getPaint().measureText(mMinRightPaddingString);
        if (ww > mAxisRighttPadding) {
            mAxisRighttPadding = ww;
        }

        mLeft = rect.left;
        mTop = rect.top;
        mRight = rect.right;
        mBottom = rect.top + mTextHeight;

        float totalWidth = mRight - mLeft - mPaddingLeft - mPaddingRight - mAxisLeftPadding - mAxisRighttPadding;

        if (mLstAxises.size() == 1) {
            mSpace = totalWidth / 2;
        } else if (mLstAxises.size() > 1) {
            mSpace = totalWidth / (mLstAxises.size() - 1);
        }

        return new RectF(mLeft, mTop, mRight, mBottom);
    }

    @Override
    public void doDraw(Canvas canvas) {
        Paint.FontMetrics fm = getPaint().getFontMetrics();

        //文字的baseline
        float startY = mTop + (mBottom - mTop - fm.bottom + fm.top) / 2 - fm.top;
        int size = mLstAxises.size();
        if (mIsSameWidth) {
            float itemWidth = size > 0 ? mTotalWidth / size : mTotalWidth;
            for (int i = 0; i < size; i++) {
                String text = mLstAxises.get(i);
                if (text == null) {
                    continue;
                }
                float w = getPaint().measureText(text);
                float startX = mLeft + mPaddingLeft + mAxisLeftPadding + itemWidth * i + itemWidth / 2;
                canvas.drawText(text, startX, startY, getPaint());
            }
        } else {
            if (size == 1) {
                float startX = mLeft + mPaddingLeft + mSpace + mAxisLeftPadding;
                canvas.drawText(mLstAxises.get(0), startX, startY, getPaint());
            } else {
                for (int i = 0; i < size; i++) {
                    String text = mLstAxises.get(i);
                    if (text == null) {
                        continue;
                    }
                    float startX = mLeft + mPaddingLeft + mSpace * i + mAxisLeftPadding;
                    if (i == 0) {
                        float w = getPaint().measureText(text);
                        canvas.drawText(text, startX + w / 2, startY, getPaint());
                    } else if (i == size - 1) {
                        float w = getPaint().measureText(text);
                        canvas.drawText(text, startX - w / 2, startY, getPaint());
                    } else {
                        canvas.drawText(text, startX, startY, getPaint());
                    }
                }
            }
        }


        // 绘制浮动坐标
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
        }

        float tWidth = mFloatCoordinatePaint.measureText(mTaFloatCoordinate.getText());
        mFloatCoordinateRectWidth = tWidth + 16;

        int tRectLeft = (int) (mTaFloatCoordinate.getCoorDinateX() - mFloatCoordinateRectWidth / 2);
        if (tRectLeft < mLeft) {
            tRectLeft = (int) mLeft;
        } else if (tRectLeft + mFloatCoordinateRectWidth > mRight) {
            tRectLeft = (int) (mRight - mFloatCoordinateRectWidth);
        }

        int tRectRight = (int) (tRectLeft + mFloatCoordinateRectWidth);
        Paint.FontMetrics floatFm = getPaint().getFontMetrics();
        int tRectBottom = (int) (mTop + floatFm.bottom - floatFm.top);
        //悬浮坐标文字的baseline
        startY = mTop + (tRectBottom - mTop - floatFm.bottom + floatFm.top) / 2 - floatFm.top;
        Paint paintBg = new Paint();

        //画圆角矩形
        paintBg.setStyle(Paint.Style.FILL);//充满
        paintBg.setColor(0xffeb333b);
        paintBg.setAntiAlias(true);// 设置画笔的锯齿效果
        RectF oval3 = new RectF(tRectLeft, (int) mTop, tRectRight, tRectBottom);// 设置个新的长方形
        canvas.drawRoundRect(oval3, 6, 6, paintBg);//第二个参数是x半径，第三个参数是y半径

        //绘制悬浮坐标
        float newX = tRectLeft + mFloatCoordinateRectWidth / 2;
        mFloatCoordinatePaint.setColor(mTaFloatCoordinate.getTextColor());
        canvas.drawText(mTaFloatCoordinate.getText(), newX, startY, mFloatCoordinatePaint);
    }

    @Override
    public void rePrepareWhenDrawing(RectF rect) {
        mTotalWidth = mRight - mLeft - mPaddingLeft - mPaddingRight - mAxisLeftPadding - mAxisRighttPadding;
        if (mLstAxises.size() == 1) {
            mSpace = mTotalWidth / 2;
        } else if (mLstAxises.size() > 1) {
            mSpace = mTotalWidth / (mLstAxises.size() - 1);
        }
    }

    public void addValue(String value) {
        mLstAxises.add(value);
    }

    public void setValue(int pos, String value) {
        if (pos < 0 || pos > mLstAxises.size() - 1)
            return;

        mLstAxises.set(pos, value);
    }

    public void clear() {
        mLstAxises.clear();
    }

    public void setMinLeftPaddingString(String str) {
        mMinLeftPaddingString = str;
    }

    public void setMinLeftPadding(float rightWidth) {
        mAxisLeftPadding = rightWidth;
    }

    public void setMinRightPaddingString(String str) {
        mMinRightPaddingString = str;
    }

    public void setMinRightPadding(float rightWidth) {
        mAxisRighttPadding = rightWidth;
    }

    public void setTextSize(int size) {
        mTextSize = size;
    }

    public void switchFloatCoordinateOn(boolean isOn) {
        mIsFloatCoordinateOn = isOn;
    }

    public void setFloatCoordinateText(TextAtom ta) {
        mTaFloatCoordinate = ta;
    }

    public void setFloatCoordinateBgColor(int bgColor) {
        mFloatCoordinateBgColor = bgColor;
    }

    public void setSameWidth(boolean sameWidth) {
        mIsSameWidth = sameWidth;
    }


    /**
     * 保存浮动坐标内容
     */
    public static class TextAtom {

        private String mText = "";
        private int mTextColor = Color.BLACK;
        private int mTextSize = 18;
        private float mCoorDinateX = 0;

        public TextAtom(String text, int textColor, int textSize, float coordinateX) {
            mText = text;
            mTextColor = textColor;
            mTextSize = textSize;
            mCoorDinateX = coordinateX;
        }

        public float getCoorDinateX() {
            return mCoorDinateX;
        }

        public void setCoorDinateX(float coorDinateX) {
            this.mCoorDinateX = coorDinateX;
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

    }

    /**
     * 获取坐标左上角相对画布的dy
     *
     * @param cdHeight 坐标的高度
     */
    public Point getCoordinateMarginLeftTop(float cdWidth, float point_x) {
        Point point = null;

        int tRectX = (int) (point_x - cdWidth / 2);
        if (tRectX < mLeft) {
            tRectX = (int) mLeft;
        } else if (tRectX + cdWidth > mRight) {
            tRectX = (int) (mRight - cdWidth);
        }

        RectF cvRectF = getCanvasRect();
        if (cvRectF != null) {
            point = new Point((int) (tRectX - cvRectF.left), (int) (mTop - cvRectF.top));

        }
        return point;

    }

}
