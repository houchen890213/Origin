package com.yulin.common.bar;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.List;

/**
 * 支持菜单项的基础控件 通过添加BarMenuItem来设置菜单
 */
public class Bar extends RelativeLayout {

    protected int mItemPaddingLeft = 0;
    protected int mItemPaddingTop = 0;
    protected int mItemPaddingRight = 0;
    protected int mItemPaddingBottom = 0;

    protected int mItemSelectedTextColor = Color.BLACK;
    protected int mItemTextColor = Color.WHITE;
    protected int mItemTextSize = 14;

    protected BarMenu mBarMenu = new BarMenu();
    protected int mCurrIndex = 0;
    private boolean mItemSelectable = true;

    protected int mDividerColor = Color.GRAY;
    protected int mDividerResource = 0;
    protected int mDividerWidth = 1;
    protected boolean mDividerEnabled = false;

    public Bar(Context context) {
        super(context);
    }

    public Bar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Bar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置菜单项的padding
     * 
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setItemPaddings(int left, int top, int right, int bottom) {
        mItemPaddingLeft = left;
        mItemPaddingTop = top;
        mItemPaddingRight = right;
        mItemPaddingBottom = bottom;
    }

    /**
     * 设置菜单项文字选中颜色
     * 
     * @param color
     */
    public void setItemSelectedTextColor(int color) {
        mItemSelectedTextColor = color;
    }

    /**
     * 设置菜单项文字默认颜色
     * 
     * @param color
     */
    public void setItemTextColor(int color) {
        mItemTextColor = color;
    }

    /**
     * 设置菜单项文字大小
     * 
     * @param size
     */
    public void setItemTextSize(int size) {
        mItemTextSize = size;
    }

    /**
     * 设置菜单项是否可以选中
     * 
     * @param selectable
     */
    public void setItemSelectable(boolean selectable) {
        mItemSelectable = selectable;
    }

    /**
     * 菜单项是否可以选中
     * 
     * @return
     */
    public boolean isItemSelectable() {
        return mItemSelectable;
    }

    protected View createDivider(LinearLayout.LayoutParams params) {
        View view = new View(getContext());
        view.setLayoutParams(params);
        if (mDividerResource != 0) {
            view.setBackgroundResource(mDividerResource);
        } else {
            view.setBackgroundColor(mDividerColor);
        }
        return view;
    }

    protected TextView createTextItem(final BarMenuTextItem textItem, final int index, LinearLayout.LayoutParams params) {
        TextView tvItem = new TextView(getContext());
        textItem.setItemView(tvItem);
        tvItem.setLayoutParams(params);
        tvItem.setText(textItem.getItemName());
        tvItem.setVisibility(textItem.getItemVisibility());
        tvItem.setPadding(mItemPaddingLeft, mItemPaddingTop, mItemPaddingRight, mItemPaddingBottom);
        if (textItem.getItemBackgroundResource() != 0) {
            tvItem.setBackgroundResource(textItem.getItemBackgroundResource());
        } else if (textItem.getItemBackgroundColor() != 0) {
            tvItem.setBackgroundColor(textItem.getItemBackgroundColor());
        }
        if (textItem instanceof BarMenuIconItem) {
            BarMenuIconItem iconItem = (BarMenuIconItem) textItem;
            BarMenuIconItem.IconPosition position = iconItem.getIconPosition();
            int iconResId = iconItem.getIcon();
            if (position == BarMenuIconItem.IconPosition.LEFT) {
                tvItem.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
            } else if (position == BarMenuIconItem.IconPosition.TOP) {
                tvItem.setCompoundDrawablesWithIntrinsicBounds(0, iconResId, 0, 0);
            } else if (position == BarMenuIconItem.IconPosition.RIGHT) {
                tvItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconResId, 0);
            } else if (position == BarMenuIconItem.IconPosition.BOTTOM) {
                tvItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, iconResId);
            }
        }
        if (isItemSelectable()) {
            if (index == mCurrIndex) {
                tvItem.setTextColor(mItemSelectedTextColor);
            } else {
                tvItem.setTextColor(mItemTextColor);
            }
            tvItem.setSelected(index == mCurrIndex);
        } else {
            tvItem.setTextColor(mItemTextColor);
        }
        tvItem.setGravity(Gravity.CENTER);
        if (textItem.getTextSize() > 0) {
            tvItem.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textItem.getTextSize());
        } else {
            // tvItem.setTextSize(mItemTextSize);
            tvItem.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mItemTextSize);
        }
        tvItem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mBarMenuWillSelectedListener != null) {
                    boolean bRet = mBarMenuWillSelectedListener.onItemWillSelected(index, textItem);
                    if (bRet == false) {
                        return;
                    }
                }


                setCurrentItem(index);
                if (mBarMenuSelectedListener != null) {
                    mBarMenuSelectedListener.onItemSelected(index, textItem);
                }
            }
        });
        return tvItem;
    }

    protected ImageView createImageItem(final BarMenuImgItem imgItem, final int index, LinearLayout.LayoutParams params) {
        ImageView ivItem = new ImageView(getContext());
        imgItem.setItemView(ivItem);
        ivItem.setLayoutParams(params);
        ivItem.setVisibility(imgItem.getItemVisibility());
        if (isItemSelectable()) {
            ivItem.setSelected(index == mCurrIndex);
        }
        if (imgItem.getItemBackgroundResource() != 0) {
            ivItem.setBackgroundResource(imgItem.getItemBackgroundResource());
        } else if (imgItem.getItemBackgroundColor() != 0) {
            ivItem.setBackgroundColor(imgItem.getItemBackgroundColor());
        }
        ivItem.setImageResource(imgItem.getItemImg());
        ivItem.setScaleType(ScaleType.CENTER_INSIDE);
        ivItem.setPadding(mItemPaddingLeft, mItemPaddingTop, mItemPaddingRight, mItemPaddingBottom);
        ivItem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mBarMenuWillSelectedListener != null) {
                    boolean bRet = mBarMenuWillSelectedListener.onItemWillSelected(index, imgItem);
                    if (bRet == false) {
                        return;
                    }
                }

                setCurrentItem(index);
                if (mBarMenuSelectedListener != null) {
                    mBarMenuSelectedListener.onItemSelected(index, imgItem);
                }
            }
        });
        return ivItem;
    }

    protected View createCustomItem(final BarMenuCustomItem viewItem, final int index, LinearLayout.LayoutParams params) {
        View view = viewItem.getCustomView();
        viewItem.setItemView(view);
        if (view.getLayoutParams() == null && params != null) {
            view.setLayoutParams(params);
        }
        if (viewItem.getItemBackgroundResource() != 0) {
            view.setBackgroundResource(viewItem.getItemBackgroundResource());
        } else if (viewItem.getItemBackgroundColor() != 0) {
            view.setBackgroundColor(viewItem.getItemBackgroundColor());
        }

        // 自定义menuItem 的视图不设置padding
        view.setPadding(mItemPaddingLeft, mItemPaddingTop, mItemPaddingRight, mItemPaddingBottom);

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBarMenuWillSelectedListener != null) {
                    boolean bRet = mBarMenuWillSelectedListener.onItemWillSelected(index, viewItem);
                    if (bRet == false) {
                        return;
                    }
                }

                setCurrentItem(index);
                if (mBarMenuSelectedListener != null) {
                    mBarMenuSelectedListener.onItemSelected(index, viewItem);
                }
            }
        });

        return view;
    }

    protected ViewSwitcher createGroupItem(BarMenuGroupItem groupItem, final int index, LinearLayout.LayoutParams params) {
        ViewSwitcher viewSwitcher = new ViewSwitcher(getContext());
        groupItem.setItemView(viewSwitcher);
        viewSwitcher.setLayoutParams(params);
        if (groupItem.getItemBackgroundResource() != 0) {
            viewSwitcher.setBackgroundResource(groupItem.getItemBackgroundResource());
        } else if (groupItem.getItemBackgroundColor() != 0) {
            viewSwitcher.setBackgroundColor(groupItem.getItemBackgroundColor());
        }

        viewSwitcher.setPadding(mItemPaddingLeft, mItemPaddingTop, mItemPaddingRight, mItemPaddingBottom);

        return viewSwitcher;
    }

    protected void setCurrentItem(int index) {

    }

    public void setDividerColor(int color) {
        mDividerColor = color;
    }

    public void setDividerResource(int resId) {
        mDividerResource = resId;
    }

    public void setDividerWidth(int w) {
        mDividerWidth = w;
    }

    public void setDividerEnabled(boolean enabled) {
        mDividerEnabled = enabled;
    }

    public boolean isDividerEnabled() {
        return mDividerEnabled;
    }

    public int getCurrentItem() {
        return mCurrIndex;
    }

    /**
     * 添加菜单项到菜单
     * 
     * @param item
     * @return
     */
    public BarMenuItem addMenuItem(BarMenuItem item) {
        return mBarMenu.addItem(item);
    }

    /**
     * 替换菜单项
     * 
     * @param item
     * @return
     */
    public BarMenuItem replaceMenuItem(int index, BarMenuItem item) {
        BarMenuItem itemTemp = null;
        if (mBarMenu.getItems().size() > index) {
            itemTemp = mBarMenu.getItem(index);
            mBarMenu.setItem(index, item);
        }
        return itemTemp;
    }


    /**
     * 添加多个菜单项
     * 
     * @param items
     */
    public void addMenuItems(List<BarMenuItem> items) {
        mBarMenu.addItems(items);
    }



    /**
     * 直接添加菜单项到视图
     * 
     * @param item
     */
    public void addMenuItemNow(BarMenuItem item) {

    }

    /**
     * 直接添加菜单项到视图
     * 
     * @param item
     * @param index
     */
    public void addMenuItemNow(BarMenuItem item, int index) {

    }

    /**
     * 获取菜单
     * 
     * @return
     */
    public BarMenu getBarMenu() {
        return mBarMenu;
    }

    public void performItemClick(int index) {
        List<BarMenuItem> items = mBarMenu.getItems();

        if (mBarMenuWillSelectedListener != null) {
            boolean bRet = mBarMenuWillSelectedListener.onItemWillSelected(index, items.get(index));
            if (bRet == false) {
                return;
            }
        }

        setCurrentItem(index);
        if (mBarMenuSelectedListener != null) {
            mBarMenuSelectedListener.onItemSelected(index, items.get(index));
        }
    }

    protected OnBarMenuSelectedListener mBarMenuSelectedListener = null;

    protected OnBarMenuWillSelectedListener mBarMenuWillSelectedListener = null;

    public void setOnBarMenuSelectedListener(OnBarMenuSelectedListener listener) {
        mBarMenuSelectedListener = listener;
    }

    public void setOnBarMenuWillSelectedListener(OnBarMenuWillSelectedListener listener) {
        mBarMenuWillSelectedListener = listener;
    }

    public static interface OnBarMenuSelectedListener {
        public void onItemSelected(int index, BarMenuItem item);
    }

    public static interface OnBarMenuWillSelectedListener {
        public boolean onItemWillSelected(int index, BarMenuItem item);
    }



    /**
     * 设置完bar的菜单信息后，通知bar去更新UI
     */
    public void notifyBarSetChanged() {

    }

    /**
     * 设置菜单项样式，更新
     */
    public void notifyItemSetChanged() {
        BarMenu menu = getBarMenu();
        List<BarMenuItem> items = menu.getItems();
        for (int i = 0; i < items.size(); i++) {
            final BarMenuItem item = items.get(i);
            if (item instanceof BarMenuTextItem) {
                BarMenuTextItem textItem = (BarMenuTextItem) item;
                View view = textItem.getItemView();
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mItemTextSize);
                    if (mCurrIndex == i && isItemSelectable()) {
                        tv.setTextColor(mItemSelectedTextColor);
                    } else {
                        tv.setTextColor(mItemTextColor);
                    }
                }
            }
        }
    }

    /**
     * 获取菜单项个数
     * 
     * @return
     */
    public int getItemCount() {
        return mBarMenu.getItems().size();
    }

    /**
     * 清除所有菜单项
     */
    public void clearBarMenu() {
        mBarMenu.clear();
    }

}
