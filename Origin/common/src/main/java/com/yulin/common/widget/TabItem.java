package com.yulin.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.yulin.common.R;

/**
 * Created by liu_lei on 2017/7/3.
 * tab_bar里面的item，显示一个图形和一个文字
 */

public class TabItem extends View {

    public static final int STATE_NORMAL = 0;
    public static final int STATE_SELECT = 1;

    private static final int COLOR_DEFAULT = 0xFF999999;
    private static final int COLOR_SELECT = 0xFF45C01A;

    private static final int DEFAULT_COLOR = COLOR_DEFAULT;
    private static final int DEFAULT_TEXT_SIZE = 30;

    private Drawable mIcon;

    private CharSequence mText;
    private StaticLayout mTextLayout;

    /* Text Drawing */
    private TextPaint mTextPaint;
    private Point mTextOrigin;

    public TabItem(Context context) {
        this(context, null);
    }

    public TabItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int style) {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextOrigin = new Point(0, 0);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TabItem, 0, style);

        Drawable d = a.getDrawable(R.styleable.TabItem_icon);
        if (d != null)
            setIconDrawable(d);

        int color = a.getColor(R.styleable.TabItem_color, DEFAULT_COLOR);
        mTextPaint.setColor(color);

        int rawSize = a.getDimensionPixelSize(R.styleable.TabItem_text_size, DEFAULT_TEXT_SIZE);
        mTextPaint.setTextSize(rawSize);

        CharSequence text = a.getText(R.styleable.TabItem_text);
        setText(text);

        a.recycle();
    }

    public void setState(int state) {
        if (state == STATE_SELECT) {
            mTextPaint.setColor(COLOR_SELECT);
            mIcon.setColorFilter(COLOR_SELECT, PorterDuff.Mode.SRC_IN);
        } else {
            mTextPaint.setColor(COLOR_DEFAULT);
            mIcon.setColorFilter(COLOR_DEFAULT, PorterDuff.Mode.SRC_IN);
        }
        updateContentBounds();
        invalidate();
    }

    public void setIconDrawable(Drawable icon) {
        mIcon = icon;
        updateContentBounds();
        invalidate();
    }

    public void setText(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            mText = text;
            updateContentBounds();
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Get the width measurement
        int widthSize = View.resolveSize(0, widthMeasureSpec);

        // Get the height measurement
        int heightSize = View.resolveSize(0, heightMeasureSpec);

        // Must call this to store the measurements
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        /*
        * Android doesn't know the real size at start, it needs to calculate it.
        * Once it's done, onSizeChanged() will notify you with the real size.
        * onSizeChanged() is called once the size as been calculated.
        * so you can set bounds when onSizeChanged() is called.
        * */

        if (w != oldw || h != oldh)
            updateContentBounds();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mIcon != null) {
            mIcon.draw(canvas);
        }

        if (mTextLayout != null) {
            canvas.save();
            canvas.translate(mTextOrigin.x, mTextOrigin.y);

            mTextLayout.draw(canvas);

            canvas.restore();
        }
    }

    private void updateContentBounds() {

        if (mText == null) {
            mText = "";
        }
        float textWidth = mTextPaint.measureText(mText, 0, mText.length());
        mTextLayout = new StaticLayout(mText, mTextPaint, (int)textWidth, Layout.Alignment.ALIGN_CENTER, 1f, 0f, true);

        if (mIcon != null) {
            int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                    getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - mTextLayout.getHeight());

            int left = getMeasuredWidth() / 2 - iconWidth / 2;
            int top = getMeasuredHeight() / 2 - (mTextLayout.getHeight() + iconWidth) / 2;

            mIcon.setBounds(left, top, left + mIcon.getIntrinsicWidth(), top + mIcon.getIntrinsicHeight());
        }

        if (mTextLayout != null) {
            int x = getMeasuredWidth() / 2 - mTextLayout.getWidth() / 2;
            int y = (int) (mIcon.getBounds().bottom + 0.2 * mTextLayout.getHeight());
            mTextOrigin.set(x, y);
        }

    }

}
