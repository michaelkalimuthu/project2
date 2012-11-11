package com.cs413.walker.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.cs413.walker.actors.Actor;
import com.cs413.walker.locations.Location;
import com.cs413.walker.locations.Neighbor;
import com.example.walker.R;





public class TerrainView extends View {
	
	
	private static final String TAG = "VIEW";
	private Location start;
	
	private static final int UP = -1;
	private static final int DOWN = -2;
	
	private Actor actor;
	
	private HashMap<Integer, ArrayList<Location>> map;
	private HashMap<Integer, Location> mapping;
	
	int currentLoc;
	
	float placeHolderX;
	float placeHolderY;
	
	Paint paint;
	
	boolean stroke = false;
	
	  Bitmap player = BitmapFactory.decodeResource(getResources(), R.drawable.player);
      Bitmap upRed = BitmapFactory.decodeResource(getResources(), R.drawable.up_red);
      Bitmap downRed = BitmapFactory.decodeResource(getResources(), R.drawable.down_red);
      Bitmap upGreen = BitmapFactory.decodeResource(getResources(), R.drawable.up_green);
      Bitmap downGreen = BitmapFactory.decodeResource(getResources(), R.drawable.down_green);

	public TerrainView(Context context, Location start, HashMap<Integer, ArrayList<Location>> map) {
		super(context);
		this.start = start;
		this.map = map;
		DisplayMetrics metrics = new DisplayMetrics();
		int x = getContext().getResources().getDisplayMetrics().widthPixels;
		int y = getContext().getResources().getDisplayMetrics().heightPixels;
		
		
		
		currentLoc = 0;

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
 
        paint = new Paint();

        Rect rect = new Rect();
        int countHeight = 0;

        boolean first = true;
        
        canvas.drawColor(Color.GRAY);
        
        String text = "";
        int left = 0;
        int top = 0;
        int right = canvas.getWidth()/5;
        int inc = canvas.getWidth()/5;
        int bottom = canvas.getHeight()/10;
        int bottomInc = canvas.getHeight()/10;

        int count = 0;

        
        int loc = runGrid(canvas);
        Log.d(TAG, String.valueOf(loc) + " LOC");
 //       Log.d(TAG, String.valueOf(loc));
        if (loc == UP || loc == DOWN){
        	setCurrentLoc(0);					//eventually will be get up/down neighbor and set loc to that. right now just go back to tile 0
        }
        
        for (int i = 0; i<45; i++){
        	setUpPaint(i);
        	
        	
        	// paint.setColor(Color.BLACK);
        	paint.setStyle(Paint.Style.FILL);
        	if (stroke){
        		paint.setStyle(Paint.Style.FILL);
        		stroke = false;
        	}
        	if (countHeight == 5){
        		left = 0;
        		right = inc;
        		top += bottomInc;
        		bottom += bottomInc;
        		countHeight = 0;
        	}
        	if (first){
        		rect.set(left, top , right, bottom);
        		first = false;
        	} else {
        		rect.set(left, top , right, bottom);
        	}
        
        	int x = (int) rect.exactCenterX();
        	int y = (int) rect.exactCenterY();
        	
      //  	Log.d(TAG, "clickX = " + String.valueOf(clickX)
      //  			+ " clickY = " + String.valueOf(clickY));
        	
        	

        	text = String.valueOf(count);
        	canvas.drawText(text, x, y, paint);
        	//canvas.drawPoint(center, center2, paint);

        	canvas.drawRect(rect, paint);
        	
        	if (i == getCurrentLoc())
        		canvas.drawBitmap(player, x, y, paint);
        	
        	left = right;
        	right += inc; 
        //canvas.drawRect(0, 50, getWidth() - 1, getHeight() -1, paint);
        	count++;
        	countHeight++;
        	
        }   
        drawButtons(rect, canvas);
        
        //canvas.drawRect(10, 10, 10, 10, paint);
    }
    
    private void drawButtons(Rect rect, Canvas canvas) {
    	int bRow = canvas.getHeight() - (canvas.getHeight()/10);
        
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        
    	rect.set(0, bRow, canvas.getWidth()/2, canvas.getHeight());
    	
    	int x = (int) rect.exactCenterX();
    	int y = (int) rect.exactCenterY();
    	
    	canvas.drawBitmap(downRed, x, y-20, paint);
    	
        canvas.drawRect(rect, paint);
        
        rect.set(canvas.getWidth()/2, bRow, canvas.getWidth(), canvas.getHeight());
        
        x = (int) rect.exactCenterX();
    	y = (int) rect.exactCenterY();
    	
    	canvas.drawBitmap(upRed, x, y-20, paint);
    	
    	canvas.drawRect(rect, paint);
		
	}



	private int runGrid(Canvas canvas){
    	mapping = new HashMap<Integer, Location>();
    	int location = 0;
    	
    	int countHeight = 0;
        int left = 0;
        int top = 0;
        int right = canvas.getWidth()/5;
        int inc = canvas.getWidth()/5;
        int bottom = canvas.getHeight()/10;
        int bottomInc = canvas.getHeight()/10;
        int floor = canvas.getHeight();
 //       Log.d(TAG, String.valueOf(bottom));
        int count = 0;
        float clickX = getPlaceHolderX();
        float clickY = getPlaceHolderY();
        
        int bRow = canvas.getHeight() - (canvas.getHeight()/10);
        
        if (clickX < canvas.getWidth()/2 && clickY > bRow){
        	location = DOWN;
        } else if (clickX > canvas.getWidth()/2 && clickY > bRow){
        	location = UP;
        }
        
        for (int i=0; i<45; i ++){
        	
        	if (countHeight == 5){
        		left = 0;
        		right = inc;
        		top += bottomInc;
        		bottom += bottomInc;
        		countHeight = 0;
        	}
        	
        	if (clickX >= left && clickX <=right && clickY>=top && clickY <=bottom){
        		setCurrentLoc(i);
        		location = i;
        	}
        	left = right;
        	right += inc; 
        	
        	
        	countHeight++;
        	
        }

        
        return location;
    }
    
    private void setUpPaint(int i) {
    	int[] neighbors = new int[4];
    	HashMap<Neighbor, Location> m = map.get(1).get(getCurrentLoc()).getNeighbors();
    	
    	for (Map.Entry<Neighbor, Location> neigh : m.entrySet()){
    		
    	}
    	
		if (i == getCurrentLoc()){
			paint.setColor(Color.WHITE);
			stroke = true;
			return;
		} 
		
		if (i == getCurrentLoc() + 5){
			if (m.get(Neighbor.SOUTH) == null ){
				paint.setColor(Color.GRAY);
			} else if (m.get(Neighbor.SOUTH).canAddActor()){
				paint.setColor(Color.GREEN);
			} else {paint.setColor(Color.RED);}
		} 
		
		else if (i == getCurrentLoc() - 5){
			if (m.get(Neighbor.NORTH) == null ){
				paint.setColor(Color.GRAY);
			} else if (m.get(Neighbor.NORTH).canAddActor()){
				paint.setColor(Color.GREEN);
			} else {paint.setColor(Color.RED);}
		} 
		
		else if (i == getCurrentLoc() + 1){
			if (m.get(Neighbor.EAST) == null ){
				paint.setColor(Color.GRAY);
			} else if (m.get(Neighbor.EAST).canAddActor()){
				paint.setColor(Color.GREEN);
			} else {paint.setColor(Color.RED);}
		} 
		
		else if (i == getCurrentLoc() - 1){
			if (m.get(Neighbor.WEST) == null ){
				paint.setColor(Color.GRAY);
			} else if (m.get(Neighbor.WEST).canAddActor()){
				paint.setColor(Color.GREEN);
			} else {paint.setColor(Color.RED);}
		}
		
		else {paint.setColor(Color.GRAY);}
		
		/*
		else if (( 
		        (i == getCurrentLoc()+5 || i == getCurrentLoc()-5) ||
		        (i == getCurrentLoc() + 1 && (getCurrentLoc()+1) % 5 !=0) || 
		        (i == (getCurrentLoc() - 1) && (i+1) % 5  != 0)
		        )) 
		        {
			
			paint.setColor(Color.GREEN);
			
		} else{
			paint.setColor(Color.GRAY);	
		}
		*/
		
	}

	private Paint getPaint(Location location){
    	Paint paint = new Paint();
    	
    	return paint;
    }

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}
	
	public float getPlaceHolderX() {
		return placeHolderX;
	}

	public void setPlaceHolderX(float placeHolderX) {
		this.placeHolderX = placeHolderX;
	}
	
	public float getPlaceHolderY() {
		return placeHolderY;
	}

	public void setPlaceHolderY(float placeHolderY) {
		this.placeHolderY = placeHolderY;
	}
	
	public int getCurrentLoc() {
		return currentLoc;
	}

	public void setCurrentLoc(int currentLoc) {
		this.currentLoc = currentLoc;
	}
	
	
	

}
