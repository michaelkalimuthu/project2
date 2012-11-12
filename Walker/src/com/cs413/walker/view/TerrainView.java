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
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cs413.walker.actors.Actor;
import com.cs413.walker.locations.Location;
import com.cs413.walker.locations.Neighbor;
import com.example.walker.R;





public class TerrainView extends View {


	private static final String TAG = "VIEW";
	private Location start;

	public static final int UP = -1;
	public static final int DOWN = -2;

	private Actor actor;
	private Toast toast;

	private HashMap<Integer, ArrayList<Location>> map;
	private HashMap<Location, Integer> mapping;
	private HashMap<Integer, GridCell> gridMap;
	private ArrayList<Integer> movingOptions;

	int currentLoc;

	int level;

	float placeHolderX;
	float placeHolderY;
	Context context;

	Paint paint;

	boolean stroke = false;

	boolean init = true;
	boolean initDraw = true;
	
	boolean canGoUp;
	boolean canGoDown;

	Bitmap player = BitmapFactory.decodeResource(getResources(), R.drawable.player);
	Bitmap upRed = BitmapFactory.decodeResource(getResources(), R.drawable.up_red);
	Bitmap downRed = BitmapFactory.decodeResource(getResources(), R.drawable.down_red);
	Bitmap upGreen = BitmapFactory.decodeResource(getResources(), R.drawable.up_green);
	Bitmap downGreen = BitmapFactory.decodeResource(getResources(), R.drawable.down_green);

	public TerrainView(Context context, HashMap<Integer, ArrayList<Location>> map, Actor actor) {

		super(context);
		this.context = context;
		canGoUp = false;
		canGoDown = false;
		this.start = actor.getLocation();
		this.map = map;
		setLevel(1);

		this.actor = actor;

		gridMap = new HashMap<Integer, GridCell>();

		movingOptions = new ArrayList<Integer>();
		mapping = new HashMap<Location, Integer>();
		setMapping();


		Log.d(TAG, String.valueOf(mapping.containsKey(start)));

		setCurrentLoc(mapping.get(actor.getLocation()));

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
		setCurrentLoc(mapping.get(actor.getLocation()));


		paint = new Paint();

		Rect rect = new Rect();
		int countHeight = 0;

		boolean first = true;


		canvas.drawColor(Color.WHITE);

		String text = "";
		int left = 0;
		int top = 0;
		int right = canvas.getWidth()/5;
		int inc = canvas.getWidth()/5;
		int bottom = canvas.getHeight()/10;
		int bottomInc = canvas.getHeight()/10;

		int count = 0;

		if (!initDraw)
			movingOptions.clear();


		int loc = runGrid(canvas);
		Log.d(TAG, String.valueOf(loc) + " LOC");
		//       Log.d(TAG, String.valueOf(loc));
		if (loc == UP || loc == DOWN){
			setCurrentLoc(0);					//eventually will be get up/down neighbor and set loc to that. right now just go back to tile 0
		}

		for (int i = 0; i<45; i++){
			setUpNeighbors(i);


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




			text = String.valueOf(count);
			canvas.drawText(text, x, y, paint);


			canvas.drawRect(rect, paint);

			if (i == getCurrentLoc())
				canvas.drawBitmap(player, x, y, paint);

			left = right;
			right += inc; 

			count++;
			countHeight++;

		}   
		drawButtons(rect, canvas);

		initDraw = false;

		//canvas.drawRect(10, 10, 10, 10, paint);
	}

	private void drawButtons(Rect rect, Canvas canvas) {
		int bRow = canvas.getHeight() - (canvas.getHeight()/10);

		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);

		rect.set(0, bRow, canvas.getWidth()/2, canvas.getHeight());
		gridMap.put(DOWN, new GridCell(0, bRow, canvas.getWidth()/2, canvas.getHeight()));

		int x = (int) rect.exactCenterX();
		int y = (int) rect.exactCenterY();
		if (canGoDown){
			canvas.drawBitmap(downGreen, x, y-20, paint);
		} else{
			canvas.drawBitmap(upRed, x, y-20, paint);
		}

		canvas.drawRect(rect, paint);

		rect.set(canvas.getWidth()/2, bRow, canvas.getWidth(), canvas.getHeight());
		gridMap.put(UP, new GridCell(canvas.getWidth()/2, bRow, canvas.getWidth(), canvas.getHeight()));

		x = (int) rect.exactCenterX();
		y = (int) rect.exactCenterY();
		if (canGoUp){
			canvas.drawBitmap(upGreen, x, y-20, paint);
		} else {
			canvas.drawBitmap(upRed, x, y-20, paint);
		}

		canvas.drawRect(rect, paint);



	}



	private int runGrid(Canvas canvas){

		int location = getCurrentLoc();

		setCanGoUp(false);
		setCanGoDown(false);

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
/*
		if (clickX < canvas.getWidth()/2 && clickY > bRow){
			location = DOWN;
		} else if (clickX > canvas.getWidth()/2 && clickY > bRow){
			location = UP;
		}
*/
		for (int i=0; i<45; i ++){

			if (countHeight == 5){
				left = 0;
				right = inc;
				top += bottomInc;
				bottom += bottomInc;
				countHeight = 0;
			}
			gridMap.put(i, new GridCell(left, right, bottom, top));
			/*
        	if (!init && clickX >= left && clickX <=right && clickY>=top && clickY <=bottom){
        		setCurrentLoc(i);
        		location = i;
        	}
			 */
			left = right;
			right += inc; 


			countHeight++;

		}

		init = false;
		return location;
	}

	public HashMap<Integer, GridCell> getGridMap() {
		return gridMap;
	}



	private void setUpNeighbors(int i) {
		int[] neighbors = new int[4];
		HashMap<Neighbor, Location> m = map.get(getLevel()).get(getCurrentLoc()).getNeighbors();

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
				movingOptions.add(mapping.get(m.get(Neighbor.SOUTH)));
				paint.setColor(Color.GREEN);
			} else {paint.setColor(Color.RED);}
		} 

		else if (i == getCurrentLoc() - 5){
			if (m.get(Neighbor.NORTH) == null ){
				paint.setColor(Color.GRAY);
			} else if (m.get(Neighbor.NORTH).canAddActor()){
				movingOptions.add(mapping.get(m.get(Neighbor.NORTH)));
				paint.setColor(Color.GREEN);
			} else {paint.setColor(Color.RED);}
		} 

		else if (i == getCurrentLoc() + 1){
			if (m.get(Neighbor.EAST) == null ){
				paint.setColor(Color.GRAY);
			} else if (m.get(Neighbor.EAST).canAddActor()){
				movingOptions.add(mapping.get(m.get(Neighbor.EAST)));
				paint.setColor(Color.GREEN);
			} else {paint.setColor(Color.RED);}
		} 

		else if (i == getCurrentLoc() - 1){
			if (m.get(Neighbor.WEST) == null ){
				paint.setColor(Color.GRAY);
			} else if (m.get(Neighbor.WEST).canAddActor()){
				movingOptions.add(mapping.get(m.get(Neighbor.WEST)));
				paint.setColor(Color.GREEN);
			} else {paint.setColor(Color.RED);}
		}

		else {paint.setColor(Color.GRAY);}


		if (m.get(Neighbor.ABOVE) != null && m.get(Neighbor.ABOVE).canAddActor()){
			movingOptions.add(mapping.get(m.get(Neighbor.ABOVE)));
			setCanGoUp(true);
		} else if (m.get(Neighbor.BELOW) != null && m.get(Neighbor.BELOW).canAddActor()){
			movingOptions.add(mapping.get(m.get(Neighbor.BELOW)));		
			setCanGoDown(true);
		} else{
			setCanGoDown(false);
			setCanGoUp(false);
		}


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




	public void notify(Location newLocation, Actor player){
		String text = "Location " + newLocation.toString() + " entered by actor " + player.toString();

		if (toast != null){
			toast.cancel();
		}

		toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.show();
	}

	private void setMapping(){
		mapping.clear();
		for (int i=0; i<map.get(getLevel()).size(); i++)
			mapping.put(map.get(getLevel()).get(i), i);
	}
	public HashMap<Location, Integer> getMapping(){
		return mapping;
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

	public int getLevel(){
		return level;
	}
	public void setLevel(int level){
		this.level = level;
	}
	public ArrayList<Integer> getMovingOptions(){
		return movingOptions;
	}



	public boolean isCanGoUp() {
		return canGoUp;
	}
	private void setCanGoUp(boolean canGoUp) {
		this.canGoUp = canGoUp;
	}

	public void changeLevel(int dir){
		setLevel(getLevel() + dir);
		setMapping();
	}

	public boolean isCanGoDown() {
		return canGoDown;
	}
	private void setCanGoDown(boolean canGoDown) {
		this.canGoDown = canGoDown;
	}

}
