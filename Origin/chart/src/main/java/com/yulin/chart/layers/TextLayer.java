package com.yulin.chart.layers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

public class TextLayer extends ChartLayer {

	private List<TextAtom> mLstTexts = new ArrayList<TextAtom>();
	private float mTextHeight = 0;
	
	private Align mAlign = Align.LEFT;
	private int mTextSize = 18;
	private float mTextWidth = 0;
	@Override
	public RectF prepareBeforeDraw(RectF rect) {
		// TODO Auto-generated method stub
		getPaint().setColor(mColor);
		getPaint().setTextSize(mTextSize);
		getPaint().setAntiAlias(true);
		getPaint().setTextAlign(Align.LEFT);
		mLeft = rect.left;
		mRight = rect.right;
		
		Paint.FontMetrics fm = getPaint().getFontMetrics();
		mTextHeight = (float) (Math.ceil(fm.descent - fm.ascent) + 2);
		mTextWidth = 0;
		for(int i = 0; i < mLstTexts.size(); i++)
		{
			TextAtom text = mLstTexts.get(i);
			mTextWidth += getPaint().measureText(text.getText());
		}
		mTop = rect.top;
		mBottom = rect.top + mTextHeight;
		return new RectF(mLeft, mTop, mRight, mBottom);
	}
	public RectF prepareBeforeDrawFixed(RectF rect) {
		// TODO Auto-generated method stub
		getPaint().setColor(mColor);
		getPaint().setTextSize(mTextSize);
		getPaint().setAntiAlias(true);
		getPaint().setTextAlign(Align.LEFT);
		mLeft = rect.left;
		mRight = rect.right;
		
		Paint.FontMetrics fm = getPaint().getFontMetrics();
		mTextHeight = (float) (Math.ceil(fm.descent - fm.ascent) + 2);
		mTextWidth = 0;
		for(int i = 0; i < mLstTexts.size(); i++)
		{
			TextAtom text = mLstTexts.get(i);
			mTextWidth += getPaint().measureText(text.getText());
		}
		mTop = rect.top;
		mBottom = rect.bottom;
		
		return new RectF(mLeft, mTop, mRight, mBottom);
	}
	@Override
	public void doDraw(Canvas canvas) {
		Paint.FontMetrics fm = getPaint().getFontMetrics();
		float startY = mTop + (mBottom - mTop - fm.bottom + fm.top) / 2 - fm.top;
		
		if(mAlign == Align.LEFT)
		{
			float startX = mLeft + mPaddingLeft;
			for(int i = 0; i < mLstTexts.size(); i++)
			{
				TextAtom text = mLstTexts.get(i);
				getPaint().setColor(text.getTextColor());
				canvas.drawText(text.getText(), startX, startY, getPaint());
				startX += getPaint().measureText(text.getText());
			}
		}
		else if(mAlign == Align.CENTER)
		{
			float startX = mLeft + mPaddingLeft + (mRight - mLeft - mPaddingLeft - mPaddingRight - mTextWidth)/2;
			for(int i = 0; i < mLstTexts.size(); i++)
			{
				TextAtom text = mLstTexts.get(i);
				getPaint().setColor(text.getTextColor());
				canvas.drawText(text.getText(), startX, startY, getPaint());
				startX += getPaint().measureText(text.getText());
			}
		}
		else if(mAlign == Align.RIGHT)
		{
			float startX = mRight - mPaddingRight - mTextWidth;
			for(int i = 0; i < mLstTexts.size(); i++)
			{
				TextAtom text = mLstTexts.get(i);
				getPaint().setColor(text.getTextColor());
				canvas.drawText(text.getText(), startX, startY, getPaint());
				startX += getPaint().measureText(text.getText());
			}
		}
	}

	@Override
	public void rePrepareWhenDrawing(RectF rect) {
		// TODO Auto-generated method stub

	}

	public void setAlign(Align align)
	{
		mAlign = align;
	}
	public void setTextSize(int textSize)
	{
		mTextSize = textSize;
	}
	public void addText(TextAtom atom)
	{
		mLstTexts.add(atom);
	}
	
	public void clearTexts()
	{
		mLstTexts.clear();
	}

	public void addText(String text, int textColor)
	{
		addText(new TextAtom(text, textColor));
	}
	public static class TextAtom
	{
		private String mText = "";
		private int mTextColor = Color.BLACK;
		
		public TextAtom(String text, int textColor)
		{
			mText = text;
			mTextColor = textColor;
		}
		
		public int getTextColor()
		{
			return mTextColor;
		}
		
		public String getText()
		{
			return mText;
		}
		
		public void setText(String text)
		{
			mText = text;
		}
		
		public void setTextColor(int color)
		{
			mTextColor = color;
		}
	}
}
