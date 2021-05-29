package com.example.squareImage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ResizableImageViewHeight extends ImageView {
	public ResizableImageViewHeight(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override 
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        Drawable d = getDrawable();
        if(d!=null){
                int height = MeasureSpec.getSize(heightMeasureSpec);
                int width = (int) Math.ceil((float) height * (float) d.getIntrinsicWidth() / (float) d.getIntrinsicHeight());
                setMeasuredDimension(width, height);
        }else{
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
   }

}
