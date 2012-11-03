package com.cs413.walker.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.DisplayMetrics;
import android.view.View;

import com.cs413.walker.locations.Location;




public class TerrainView extends View {
	
	private Location start;

	public TerrainView(Context context, Location start) {
		super(context);
		this.start = start;
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		setMinimumWidth(width);
		setMinimumHeight(height + 20);
	}
	
	
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
            getSuggestedMinimumWidth(),
            getSuggestedMinimumHeight());
    }
    
    @Override protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
 
        Paint paint = new Paint();
        paint.setStyle(Style.STROKE);
        paint.setColor(hasFocus() ? Color.BLUE : Color.GRAY);
        canvas.drawRect(0, 0, getWidth() - 1, getHeight() -1, paint);
 
        
    }

}
