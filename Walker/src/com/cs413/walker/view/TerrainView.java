package com.cs413.walker.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import com.cs413.walker.locations.Location;




public class TerrainView extends View {
	private static final String TAG = "VIEW";
	private Location start;

	public TerrainView(Context context, Location start) {
		super(context);
		this.start = start;
		/*
		DisplayMetrics metrics = new DisplayMetrics();
		int x = getContext().getResources().getDisplayMetrics().widthPixels;
		int y = getContext().getResources().getDisplayMetrics().heightPixels;
		*/
		
		//setMinimumWidth(x);
		//setMinimumHeight(y-400);
		
		
		
		setFocusable(true);
	}
	
	/*
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
            getSuggestedMinimumWidth(),
            getSuggestedMinimumHeight());
    }
    */
    @Override protected void onDraw(Canvas canvas) {
       // canvas.drawColor(Color.WHITE);
 
        Paint paint = new Paint();
        
        
        
        
        Rect rect = new Rect();
        int countHeight = 0;
        int five = 5;
        
        String text = "";
        int left = 0;
        int top = 0;
        int right = canvas.getWidth()/5;
        int inc = canvas.getWidth()/5;
        int bottom = canvas.getHeight()/10;
        int bottomInc = canvas.getHeight()/10;
        int floor = canvas.getHeight();
        Log.d(TAG, String.valueOf(bottom));
        int count = 0;
        for (int i = 0; i<51; i++){
        	
        	countHeight++;
        	paint.setColor(Color.BLACK);
        	paint.setStyle(Paint.Style.STROKE);
        	if (countHeight == 6){
        		left = 0;
        		right = inc;
        		top += bottomInc;
        		bottom += bottomInc;
        		countHeight = 1;
        	}
        	
        	int x = (int) rect.exactCenterX();
        	int y = (int) rect.exactCenterY();
        	
        	rect.set(left, top , right, bottom);
        	text = String.valueOf(count);
        	canvas.drawText(text, x, y, paint);
        	//canvas.drawPoint(center, center2, paint);
        	if (count % 2 == 0){
        		paint.setColor(Color.BLUE);
        		paint.setStyle(Paint.Style.FILL);
        	}
        	canvas.drawRect(rect, paint);
        	left = right;
        	right += inc; 
        //canvas.drawRect(0, 50, getWidth() - 1, getHeight() -1, paint);
        	count++;
        	
        }
        
        
        //canvas.drawRect(10, 10, 10, 10, paint);
        
   
    }
    
    private Paint getPaint(Location location){
    	Paint paint = new Paint();
    	
    	return paint;
    }

}
