package com.armedarms.tools;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageViewSquared extends ImageView {

    public ImageViewSquared(Context context) {
        super(context);
    }

    public ImageViewSquared(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewSquared(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int w = getMeasuredWidth();
        setMeasuredDimension(w, w);
    }
}
