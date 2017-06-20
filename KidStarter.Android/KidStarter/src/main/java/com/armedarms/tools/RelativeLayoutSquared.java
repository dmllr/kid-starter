package com.armedarms.tools;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class RelativeLayoutSquared extends RelativeLayout {

    public RelativeLayoutSquared(Context context) {
        super(context);
    }

    public RelativeLayoutSquared(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RelativeLayoutSquared(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int w = getMeasuredWidth();
        setMeasuredDimension(w, w);
    }
}
