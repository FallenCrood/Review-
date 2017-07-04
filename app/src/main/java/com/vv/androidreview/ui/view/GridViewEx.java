
package com.vv.androidreview.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Author：Laofu .
 * Mail：471996049@qq.com
 * Description：
 */
public class GridViewEx extends GridView {
    public GridViewEx(Context context) {
        this(context, null);
    }

    public GridViewEx(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
