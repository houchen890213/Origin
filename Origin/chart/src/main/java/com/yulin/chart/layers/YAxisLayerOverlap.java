package com.yulin.chart.layers;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

/**
 * 纵坐标层
 * 
 * @author Administrator
 *
 */
public class YAxisLayerOverlap extends YAxisLayer {

    @Override
    public void doDraw(Canvas canvas) {


        Paint.FontMetrics fm = getPaint().getFontMetrics();
        float startY = mTop + mPaddingTop + (fm.top - fm.bottom) / 2 - fm.top;

        int middleIndex = -1;
        if (mAxisCount >= 3) {
            middleIndex = mAxisCount / 2;
        }

        for (int i = 0; i < mAxisCount; i++) {
            if (mDrawingListener != null) {
                mDrawingListener.onDrawing(getPaint(), i);
            }
            String data = String.valueOf(mAxisValues[i]);
            if (mFormatDataListener != null) {
                data = mFormatDataListener.onFormatData(mAxisValues[i]);
            }

            if (i == 0) {
                if (mIsDrawHeadTailCoordinate && mIsDrawHeadCoordinate) {
                    canvas.drawText(data, mStartX, startY + mTextHeight / 2, getPaint());
                }
            } else if (i == mAxisCount - 1) {
                if (mIsDrawHeadTailCoordinate && mIsDrawTailCoordinate) {
                    canvas.drawText(data, mStartX, startY - mTextHeight / 2, getPaint());
                }
            }

             /*else if (i == middleIndex) {
                canvas.drawText(data, mStartX, startY - (fm.bottom - fm.top) / 2, getPaint());
            }*/

            else {
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

        int tRectLeftY = (int) (mTaFloatCoordinate.getCoorDinateY() - mFloatCoordinateRectHeight / 2);
        if (tRectLeftY < mTop) {
            tRectLeftY = (int) mTop;
        } else if (tRectLeftY + mFloatCoordinateRectHeight > mBottom) {
            tRectLeftY = (int) (mBottom - mFloatCoordinateRectHeight);
        }


        int tRectRightY = (int) (tRectLeftY + mFloatCoordinateRectHeight);
        int tRectRightX = (int) (mLeft + getPaint().measureText(mTaFloatCoordinate.getText()) + 8);
        
        Paint paintbg = new Paint();
        
        paintbg.setStrokeWidth(0);
        paintbg.setStyle(Style.FILL);
        paintbg.setColor(mFloatCoordinateBgColor);
        Rect rect = new Rect((int) mLeft, tRectLeftY, tRectRightX, tRectRightY);
        canvas.drawRect(rect, paintbg);

        paintbg.setStrokeWidth(2);
        paintbg.setStyle(Style.STROKE);
        paintbg.setColor(0xff828282);
        canvas.drawRect(rect, paintbg);

        float newY = tRectLeftY + mFloatCoordinateRectHeight / 2 + mFloatCoordinateOffsetY;

        mFloatCoordinatePaint.setColor(mTaFloatCoordinate.getTextColor());
        canvas.drawText(mTaFloatCoordinate.getText(), mStartX, newY, mFloatCoordinatePaint);
    }
}
