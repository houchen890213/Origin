package com.yulin.chart.layers;

import android.graphics.Canvas;


/**
 * 细柱图,放大缩小不改变宽度 如MACD
 * 
 * @author Administrator
 *
 */
public class FixedWidthColumnarLayer extends ColumnarLayer {

    @Override
    protected void drawOneColumnar(int pos, float centerX, Canvas canvas, ColumnarAtom value) {
        if (value.mOpen != value.mClose) {
            float openY = value2Y(value.mOpen);
            float closeY = value2Y(value.mClose);
            canvas.drawLine(centerX, openY, centerX, closeY, getPaint());
        }
        else {
            float openY = value2Y(value.mOpen);
            canvas.drawPoint(centerX, openY, getPaint());
        }

    }



}
