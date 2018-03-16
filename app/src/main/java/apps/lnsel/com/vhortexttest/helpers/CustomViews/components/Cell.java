package apps.lnsel.com.vhortexttest.helpers.CustomViews.components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import apps.lnsel.com.vhortexttest.data.ChatData;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;


public abstract class Cell extends LinearLayout {
    protected String color_DARK = "#806C61", color_WHITE = "#FFFFFF", color_ORANGE = "#e27408";
    protected int width;

    public Cell(Context context) {
        super(context);
        initComponent(context);
    }

    public Cell(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public Cell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initComponent(context);
    }

    protected void initComponent(Context context) {
        int width_full = CommonMethods.getScreenWidth(context).widthPixels;
        width = (int) (width_full * 0.7f);

    }

    public abstract void setUpView(boolean isMine , ChatData mDataTextChat);

}
