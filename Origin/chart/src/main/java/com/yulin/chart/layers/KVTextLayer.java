package com.yulin.chart.layers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class KVTextLayer extends ChartLayer {
	private int mRow = 1;
	private int mCol = 1;
	private float mPerHeight = 0;
	private float mPerWidth = 0;
	
	private Map<KVTextPosition, KVTextAtom> mMapTexts = new HashMap<KVTextPosition, KVTextAtom>();
	private Map<KVTextPosition, KVDivider> mMapDividers = new HashMap<KVTextPosition, KVDivider>();
	private float mTextHeight = 0;
	
	private int mTextSize = 18;
	private int mTextColor = Color.GRAY;
	
	private int mOrientation = LinearLayout.HORIZONTAL;
	
	private String mEmptyString = "没有数据";
	private boolean mIsShowEmpty = false;
	public KVTextLayer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public RectF prepareBeforeDraw(RectF rect) {
		getPaint().setColor(mColor);
		getPaint().setTextSize(mTextSize);
		getPaint().setAntiAlias(true);
		getPaint().setTextAlign(Align.LEFT);
		mLeft = rect.left;
		mRight = rect.right;
		
		Paint.FontMetrics fm = getPaint().getFontMetrics();
		mTextHeight = (float) (Math.ceil(fm.descent - fm.ascent) + 2);

		mTop = rect.top;
		mBottom = rect.bottom;
		mPerHeight =  (mBottom - mTop - mPaddingTop - mPaddingBottom)/mRow;
		mPerWidth = (mRight - mLeft - mPaddingLeft - mPaddingRight)/mCol;
		return new RectF(mLeft, mTop, mRight, mBottom);
	}

	@Override
	public void doDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		Paint.FontMetrics fm = getPaint().getFontMetrics();
		float startX = mLeft + mPaddingLeft;
		if(isShowEmptyString())
		{
			float startY = mTop + (mBottom - mTop - mPaddingTop - mPaddingBottom) / 2 + (fm.bottom + fm.top)/2 + mPaddingTop;
			getPaint().setColor(mTextColor);
			float textWidth = getPaint().measureText(mEmptyString);
			canvas.drawText(mEmptyString, startX + textWidth/2, startY, getPaint());
		}
		else
		{
			if(mOrientation == LinearLayout.VERTICAL)
			{
				float startY = mTop + mPerHeight / 2 + (fm.bottom + fm.top)/2 + mPaddingTop;
				for(Entry<KVTextPosition, KVTextAtom> entry : mMapTexts.entrySet())
				{
					KVTextAtom textAtom = entry.getValue();
					KVTextPosition pos = entry.getKey();
					
					String text = textAtom.getText();
					int textColor = textAtom.getTextColor();
					float textWidth = getPaint().measureText(text);
					
					String label = textAtom.getLabel();
					int labelColor = textAtom.getLabelColor();
					float labelWidth = getPaint().measureText(label);
					
					getPaint().setColor(labelColor);
					float labelStartX = startX + pos.mCol * mPerWidth + (mPerWidth - labelWidth)/2;
					float labelStartY = startY + pos.mRow * mPerHeight;
					canvas.drawText(label, labelStartX, labelStartY, getPaint());
					
					getPaint().setColor(textColor);
					float textStartX = startX + pos.mCol * mPerWidth+ (mPerWidth - textWidth)/2;
					float textStartY = labelStartY + mTextHeight;
					canvas.drawText(text, textStartX, textStartY, getPaint());
					
				}
			}
			else
			{
				float startY = mTop + mPerHeight / 2 - (fm.bottom + fm.top)/2 + mPaddingTop;
				for(Entry<KVTextPosition, KVTextAtom> entry : mMapTexts.entrySet())
				{
					KVTextAtom textAtom = entry.getValue();
					KVTextPosition pos = entry.getKey();
					
					String text = textAtom.getText();
					int textColor = textAtom.getTextColor();
					float textWidth = getPaint().measureText(text);
					
					String label = textAtom.getLabel();
					int labelColor = textAtom.getLabelColor();
					float labelWidth = getPaint().measureText(label);
					
					String exLabel = textAtom.getExLabel();
					int exLabelColor = textAtom.getExLabelColor();
					float exLabelWidth = getPaint().measureText(exLabel);
					
					getPaint().setColor(exLabelColor);
					float exLabelStartX = startX + pos.mCol * mPerWidth;
					float exLabelStartY = startY + pos.mRow * mPerHeight;
					canvas.drawText(exLabel, exLabelStartX, exLabelStartY, getPaint());
					
					getPaint().setColor(labelColor);
					float labelStartX = startX + pos.mCol * mPerWidth + exLabelWidth;
					float labelStartY = startY + pos.mRow * mPerHeight;
					canvas.drawText(label, labelStartX, labelStartY, getPaint());
					
					getPaint().setColor(textColor);
					float textStartX = startX + pos.mCol * mPerWidth + exLabelWidth + labelWidth;
					float textStartY = labelStartY;
					canvas.drawText(text, textStartX, textStartY, getPaint());
					
					if(mMapDividers.containsKey(pos))
					{
						KVDivider divider = mMapDividers.get(pos);
						float dividerStartY = textStartY + mPerHeight / 2 + (fm.bottom + fm.top)/2;
						getPaint().setColor(divider.getDividerColor());
						getPaint().setStrokeWidth(divider.getDividerWidth());
						canvas.drawLine(startX, dividerStartY, startX + mPerWidth, dividerStartY, getPaint());
					}
				}
			}
		}
	}
	public void setTextSize(int textSize)
	{
		mTextSize = textSize;
	}
	
	public void setTextColor(int color)
	{
		mTextColor = color;
	}
	@Override
	public void rePrepareWhenDrawing(RectF rect) {
		// TODO Auto-generated method stub

	}
	
	public void addText(int row, int col,KVTextAtom atom)
	{
		KVTextPosition pos = new KVTextPosition(row, col);
		mMapTexts.put(pos, atom);
	}
	public void addText(int row, int col,KVTextAtom atom, KVDivider divider)
	{
		KVTextPosition pos = new KVTextPosition(row, col);
		mMapTexts.put(pos, atom);
		mMapDividers.put(pos, divider);
	}
	public void clearTexts()
	{
		mMapTexts.clear();
	}
	
	public void setCol(int col)
	{
		mCol = col;
	}
	public void setRow(int row)
	{
		mRow = row;
	}
	
	public void setOrientation(int orientation)
	{
		mOrientation = orientation;
	}
	
	public void setEmptyString(String emptyString)
	{
		mEmptyString = emptyString;
	}
	public String getEmptyString()
	{
		return mEmptyString;
	}
	public void showEmptyString(boolean isShow)
	{
		mIsShowEmpty = isShow;
	}
	public boolean isShowEmptyString()
	{
		return mIsShowEmpty;
	}
	static class KVTextPosition
	{
		int mRow = 0;
		int mCol = 0;

		public KVTextPosition(int row, int col)
		{
			mRow = row;
			mCol = col;
		}
	}
	public static class KVDivider
	{
		private int mDividerColor = Color.BLACK;
		private float mDividerWidth = 1;
		public KVDivider(int color, float width)
		{
			mDividerColor = color;
			mDividerWidth = width;
		}
		public void setDividerColor(int color)
		{
			mDividerColor = color;
		}
		public int getDividerColor()
		{
			return mDividerColor;
		}
		
		public void setDividerWidth(float width)
		{
			mDividerWidth = width;
		}
		public float getDividerWidth()
		{
			return mDividerWidth;
		}
	}
	public static class KVTextAtom
	{
		private String mText = "";
		private int mTextColor = Color.BLACK;
		private String mLabel = "";
		private int mLabelColor = Color.BLACK;
		
		private String mExLabel = "";
		private int mExLabelColor = Color.BLACK;
		
		public KVTextAtom(String lbl, String text, int textColor, int lableColor)
		{
			mText = text;
			mTextColor = textColor;
			mLabelColor = lableColor;
			mLabel = lbl;
		}
		
		public KVTextAtom(String lbl, String text, int textColor)
		{
			this(lbl, text, textColor, textColor);
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
		
		public void setLabel(String lbl)
		{
			mLabel = lbl;
		}
		public String getLabel()
		{
			return mLabel;
		}
		public void setLabelColor(int color)
		{
			mLabelColor = color;
		}
		public int getLabelColor()
		{
			return mLabelColor;
		}
		
		public void setExLabel(String lbl)
		{
			mExLabel = lbl;
		}
		public String getExLabel()
		{
			return mExLabel;
		}
		public void setExLabelColor(int color)
		{
			mExLabelColor = color;
		}
		public int getExLabelColor()
		{
			return mExLabelColor;
		}
	}
}
