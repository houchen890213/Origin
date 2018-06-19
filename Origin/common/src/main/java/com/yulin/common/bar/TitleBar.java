package com.yulin.common.bar;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.yulin.common.R;

import java.util.List;

/**
 * 标题栏 显示标题，以及左右导航按钮
 * <p>
 */
public class TitleBar extends Bar {
    private final int DEFAULT_TITLE_TXT_COLOR = 0xfff8f8f8;
    //dp
    private final int DEFAULT_TITLE_TXT_SIZE = 18;

    private boolean isTranslucentBar = true;

    private int mItemTextColor = DEFAULT_TITLE_TXT_COLOR;
    private int mItemTextSize = DEFAULT_TITLE_TXT_SIZE;
    private int height = 0;

    private LinearLayout mLeftContent = null;
    private LinearLayout mRightContent = null;
    private LinearLayout mCenterContent = null;


    public static enum Position {
        LEFT, RIGHT, CENTER
    }

    public TitleBar(Context context) {
        super(context);
        init(context, null, 0);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    @SuppressWarnings("resource")
    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SZTitlebar, 0, defStyle);

        isTranslucentBar = a.getBoolean(R.styleable.SZTitlebar_is_translucentBar, true);
        height = a.getDimensionPixelSize(R.styleable.SZTitlebar_android_layout_height, 0);

        mItemTextColor = a.getColor(R.styleable.SZTitlebar_title_txt_color, DEFAULT_TITLE_TXT_COLOR);
        int tpx = a.getDimensionPixelSize(R.styleable.SZTitlebar_title_txt_size, -1);
        if (tpx == -1) {
            mItemTextSize = DEFAULT_TITLE_TXT_SIZE;
        } else {
            float scale = getContext().getApplicationContext().getResources().getDisplayMetrics().density;
            mItemTextSize = (int) (tpx / scale + 0.5f);
        }

        setItemTextSize(mItemTextSize);

        setItemTextColor(mItemTextColor);

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLeftContent = new LinearLayout(getContext());

        params.addRule(ALIGN_PARENT_LEFT);
        params.addRule(CENTER_VERTICAL);
        mLeftContent.setLayoutParams(params);
        mLeftContent.setGravity(Gravity.CENTER);
        mLeftContent.setOrientation(LinearLayout.HORIZONTAL);

        addView(mLeftContent);

        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        mRightContent = new LinearLayout(getContext());
        params.addRule(ALIGN_PARENT_RIGHT);
        params.addRule(CENTER_VERTICAL);
        mRightContent.setLayoutParams(params);
        mRightContent.setGravity(Gravity.CENTER);
        mRightContent.setOrientation(LinearLayout.HORIZONTAL);

        addView(mRightContent);

        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        mCenterContent = new LinearLayout(getContext());
        mCenterContent.setId(mCenterContent.hashCode());
        params.addRule(CENTER_IN_PARENT);
        mCenterContent.setLayoutParams(params);
        mCenterContent.setGravity(Gravity.CENTER);
        mCenterContent.setOrientation(LinearLayout.HORIZONTAL);

        addView(mCenterContent);

        setItemSelectable(false);
    }

    public void initTranslucentBar() {
        // 沉浸式状态栏设置
        if (isTranslucentBar && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            int statusBarH = 0;
            int tResId = getContext().getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (tResId > 0) {
                statusBarH = getContext().getApplicationContext().getResources().getDimensionPixelSize(tResId);
            }

            getLayoutParams().height = statusBarH + height;
            setItemPaddings(0, statusBarH, 0, 0);
        }
    }

    private View addCustomView(Position pos, BarMenuCustomItem item, int index) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = createCustomItem(item, index, params);
        if (pos == Position.LEFT) {
            mLeftContent.addView(view);
        } else if (pos == Position.RIGHT) {
            mRightContent.addView(view);
        } else if (pos == Position.CENTER) {
            mCenterContent.addView(view);
        }
        return view;
    }

    private TextView addTextButton(Position pos, BarMenuTextItem item, int index) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView tv = createTextItem(item, index, params);
        if (pos == Position.LEFT) {
            mLeftContent.addView(tv);
        } else if (pos == Position.RIGHT) {
            mRightContent.addView(tv);
        } else if (pos == Position.CENTER) {
            mCenterContent.addView(tv);
        }
        return tv;
    }

    private ImageView addImageButton(Position pos, BarMenuImgItem item, int index) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ImageView iv = createImageItem(item, index, params);
        if (pos == Position.LEFT) {
            mLeftContent.addView(iv);
        } else if (pos == Position.RIGHT) {
            mRightContent.addView(iv);
        } else if (pos == Position.CENTER) {
            mCenterContent.addView(iv);
        }
        return iv;
    }

    private ViewSwitcher addGroupView(Position pos, BarMenuGroupItem groupItem, int index) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ViewSwitcher vs = createGroupItem(groupItem, index, params);
        List<BarMenuItem> items = groupItem.getItems();
        for (int i = 0; i < items.size(); i++) {
            BarMenuItem item = items.get(i);
            if (item instanceof BarMenuTextItem) {
                BarMenuTextItem textItem = (BarMenuTextItem) item;
                TextView tv = createTextItem(textItem, index, params);
                vs.addView(tv);
            } else if (item instanceof BarMenuImgItem) {
                BarMenuImgItem imgItem = (BarMenuImgItem) item;
                ImageView iv = createImageItem(imgItem, index, params);
                vs.addView(iv);
            } else if (item instanceof BarMenuCustomItem) {
                BarMenuCustomItem viewItem = (BarMenuCustomItem) item;
                View view = createCustomItem(viewItem, index, params);
                vs.addView(view);
            }
        }
        if (pos == Position.LEFT) {
            mLeftContent.addView(vs);
        } else if (pos == Position.RIGHT) {
            mRightContent.addView(vs);
        } else if (pos == Position.CENTER) {
            mCenterContent.addView(vs);
        }
        return vs;
    }

    public void addMenuItemNow(final BarMenuItem item, final int index) {
        if (item instanceof BarMenuTextItem) {
            BarMenuTextItem textItem = (BarMenuTextItem) item;
            TextView tv = addTextButton((Position) textItem.getTag(), textItem, index);
            textItem.setItemView(tv);
        } else if (item instanceof BarMenuImgItem) {
            BarMenuImgItem imgItem = (BarMenuImgItem) item;
            addImageButton((Position) imgItem.getTag(), imgItem, index);
        } else if (item instanceof BarMenuCustomItem) {
            BarMenuCustomItem viewItem = (BarMenuCustomItem) item;
            addCustomView((Position) viewItem.getTag(), viewItem, index);
        } else if (item instanceof BarMenuGroupItem) {
            BarMenuGroupItem groupItem = (BarMenuGroupItem) item;
            ViewSwitcher vs = addGroupView((Position) groupItem.getTag(), groupItem, index);
            groupItem.setSwitcher(vs);
        }
    }


    public void addMenuItemNow(BarMenuItem item) {
        int leftCount = mLeftContent.getChildCount();
        int rightCount = mRightContent.getChildCount();
        int centerCount = mCenterContent.getChildCount();
        int count = leftCount + rightCount + centerCount;
        addMenuItemNow(item, count);
    }

    public void notifyBarSetChanged() {
        clearAll();

        BarMenu menu = getBarMenu();
        List<BarMenuItem> items = menu.getItems();
        for (int i = 0; i < items.size(); i++) {
            BarMenuItem item = items.get(i);
            addMenuItemNow(item, i);
        }
    }

    public void performItemClick(int index) {
    }

    public void clear(Position position) {
        if (position == Position.LEFT) {
            clearLeft();
        } else if (position == Position.CENTER) {
            clearCenter();
        } else if (position == Position.RIGHT) {
            clearRight();
        }
    }

    private void clearLeft() {
        if (mLeftContent != null) {
            mLeftContent.removeAllViews();
            mLeftContent.removeAllViewsInLayout();
        }
    }

    private void clearCenter() {
        if (mCenterContent != null) {
            mCenterContent.removeAllViews();
            mCenterContent.removeAllViewsInLayout();
        }
    }

    private void clearRight() {
        if (mRightContent != null) {
            mRightContent.removeAllViews();
            mRightContent.removeAllViewsInLayout();
        }
    }

    private void clearAll() {
        clearLeft();
        clearCenter();
        clearRight();
    }


}
