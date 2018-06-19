package com.yulin.common.bar;


/**
 * 带图标的菜单项
 */
public class BarMenuIconItem extends BarMenuTextItem{

	protected IconPosition mIconPosition = IconPosition.RIGHT;
	protected int mIconResId = 0;
	public static enum IconPosition
	{
		LEFT,
		TOP,
		RIGHT,
		BOTTOM
	};
	
	/**
	 * 带图标的菜单项构造函数
	 * @param id 菜单项id
	 * @param name 菜单名称
	 */
	public BarMenuIconItem(int id, String name) {
		super(id, name);
	}
	
	/**
	 * 带图标的菜单项构造函数
	 * @param id 菜单项id
	 * @param pos 图标位置
	 * @param name 菜单名称
	 * @param iconResId 图标资源id
	 */
	public BarMenuIconItem(int id, IconPosition pos, String name, int iconResId)
	{
		mItemName = name;
		mIconResId = iconResId;
		mItemId = id;
		mIconPosition = pos;
	}
	
	public int getIcon()
	{
		return mIconResId;
	}
	
	public void setIcon(IconPosition pos, int iconResId)
	{
		mIconResId = iconResId;
		mIconPosition = pos;
	}
	
	public void setIcon(int iconResId)
	{
		mIconResId = iconResId;
	}
	
	public IconPosition getIconPosition()
	{
		return mIconPosition;
	}
}
