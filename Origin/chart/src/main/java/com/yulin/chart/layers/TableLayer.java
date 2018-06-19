package com.yulin.chart.layers;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import com.yulin.chart.ChartView;
import com.yulin.chart.ChartView.IGroupLayer;

public class TableLayer extends ChartLayer implements IGroupLayer{

	private int mRow = 1;
	private int mCol = 1;
	private float mPerHeight = 0;
	private float mPerWidth = 0;
	
	private List<ChartLayer> mLstLayers = new ArrayList<ChartLayer>();
	private List<TablePosition> mLstPositions = new ArrayList<TablePosition>();
	public TableLayer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public RectF prepareBeforeDraw(RectF rect) {
		mLeft = rect.left;
		mRight = rect.right;
		mTop = rect.top;
		mBottom = rect.bottom;
		mPerHeight =  (mBottom - mTop)/mRow;
		mPerWidth = (mRight - mLeft)/mCol;
		
		for(int i = 0; i < mLstLayers.size(); i++)
		{
			TablePosition pos = mLstPositions.get(i);
			RectF rectF = getRectByPos(pos.mRow, pos.mCol);
			if(mLstLayers.get(i).prepareBeforeDrawFixed(rectF) == null)
			{
				mLstLayers.get(i).prepareBeforeDraw(rectF);
			}
		}
		return new RectF(mLeft, mTop, mRight, mBottom);
	}

	private RectF getRectByPos(int row, int col)
	{
		float left = mLeft + col * mPerWidth;
		float top = mTop + row * mPerHeight;
		return new RectF(left, top, left + mPerWidth, top + mPerHeight);
	}
	
	@Override
	public void doDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		for(int i = 0; i < mLstLayers.size(); i++)
		{
			if(mLstLayers.get(i).isShow())
			{
				mLstLayers.get(i).doDraw(canvas);
			}
		}
	}

	@Override
	public void rePrepareWhenDrawing(RectF rect) {
		// TODO Auto-generated method stub
		for(int i = 0; i < mLstLayers.size(); i++)
		{
			mLstLayers.get(i).rePrepareWhenDrawing(rect);
		}
	}
	
	public void addLayer(int row, int col, ChartLayer layer)
	{
		mLstPositions.add(new TablePosition(row, col));
		mLstLayers.add(layer);
	}

	public void setCol(int col)
	{
		mCol = col;
	}
	public void setRow(int row)
	{
		mRow = row;
	}
	
	public void layout(float left, float top, float right, float bottom)
	{
		super.layout(left, top, right, bottom);
//		for(int i = 0; i < mLstLayers.size(); i++)
//		{
//			mLstLayers.get(i).layout(left, top, right, bottom);
//		}
	}
	
	public boolean onActionShowPress(MotionEvent event)
	{
		for(int i = 0; i < mLstLayers.size(); i++)
		{
			if(mLstLayers.get(i).onActionShowPress(event))
			{
				return true;
			}
		}
		return false;
	}
	public void onActionUp(MotionEvent event)
	{
		for(int i = 0; i < mLstLayers.size(); i++)
		{
			mLstLayers.get(i).onActionUp(event);
		}
	}
	public boolean onActionMove(MotionEvent event)
	{
		for(int i = 0; i < mLstLayers.size(); i++)
		{
			if(mLstLayers.get(i).onActionMove(event))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean onActionDown(MotionEvent event)
	{
		for(int i = 0; i < mLstLayers.size(); i++)
		{
			if(mLstLayers.get(i).onActionDown(event))
			{
				return true;
			}
		}
		return false;
	}
	
	public void setChartView(ChartView cv)
	{
		for(int i = 0; i < mLstLayers.size(); i++)
		{
			mLstLayers.get(i).setChartView(cv);
		}
	}
	
	public boolean isShowHGrid()
	{
		for(int i = 0; i < mLstLayers.size(); i++)
		{
			if(mLstLayers.get(i).isShowHGrid())
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean isShowVGrid()
	{
		for(int i = 0; i < mLstLayers.size(); i++)
		{
			if(mLstLayers.get(i).isShowVGrid())
			{
				return true;
			}
		}
		return false;
	}
	
	public int getHLineCount()
	{
		for(int i = 0; i < mLstLayers.size(); i++)
		{
			if(mLstLayers.get(i).isShowHGrid())
			{
				return mLstLayers.get(i).getHLineCount();
			}
		}
		return 0;
	}
	public int getVLineCount()
	{
		for(int i = 0; i < mLstLayers.size(); i++)
		{
			if(mLstLayers.get(i).isShowVGrid())
			{
				return mLstLayers.get(i).getVLineCount();
			}
		}
		return 0;
	}
	
	public void show(boolean isShow)
	{
		for(int i = 0; i < mLstLayers.size(); i++)
		{
			mLstLayers.get(i).show(isShow);
		}
	}
	public boolean isShow()
	{
		for(int i = 0; i < mLstLayers.size(); i++)
		{
			if(mLstLayers.get(i).isShow())
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public ChartLayer getLayerByTag(String tag) {
    	for(int i = 0; i < mLstLayers.size(); i++)
    	{
    		ChartLayer layer = mLstLayers.get(i);
    		if(layer instanceof IGroupLayer)
    		{
    			ChartLayer l = ((IGroupLayer) layer).getLayerByTag(tag);
    			if(l != null)
    			{
    				return l;
    			}
    		}
    		if(tag.equals(layer.getTag()))
    		{
    			return layer;
    		}
    	}
    	return null;
	}

	static class TablePosition
	{
		int mRow = 0;
		int mCol = 0;

		public TablePosition(int row, int col)
		{
			mRow = row;
			mCol = col;
		}
	}
}
