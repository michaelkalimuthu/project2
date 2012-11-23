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
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cs413.walker.actors.Actor;
import com.cs413.walker.locations.Location;
import com.cs413.walker.locations.Neighbor;
import com.example.walker.R;

public class TerrainView extends View {

	private static final String TAG = "VIEW";
	private final Location start;

	public static final int UP = -1;
	public static final int DOWN = -2;

	private Actor actor;
	private Toast toast;

	private final HashMap<Integer, ArrayList<Location>> map;
	private final HashMap<Location, Integer> mapping;
	private final HashMap<Integer, GridCell> gridMap;
	private final ArrayList<Integer> movingOptions;

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

	Bitmap player = BitmapFactory.decodeResource(getResources(),
			R.drawable.player);
	Bitmap upRed = BitmapFactory.decodeResource(getResources(),
			R.drawable.up_red);
	Bitmap downRed = BitmapFactory.decodeResource(getResources(),
			R.drawable.down_red);
	Bitmap upGreen = BitmapFactory.decodeResource(getResources(),
			R.drawable.up_green);
	Bitmap downGreen = BitmapFactory.decodeResource(getResources(),
			R.drawable.down_green);

	public TerrainView(Context context,
			HashMap<Integer, ArrayList<Location>> map, Actor actor) {

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
	 * @Override protected void onMeasure(int widthMeasureSpec, int
	 * heightMeasureSpec) { setMeasuredDimension( getSuggestedMinimumWidth(),
	 * getSuggestedMinimumHeight()); }
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.GRAY);
		setCurrentLoc(mapping.get(actor.getLocation()));

		paint = new Paint();

		Rect rect = new Rect();

		runGrid(canvas);


		if (!initDraw) {
			movingOptions.clear();
		}
		setUpNeighbors(getCurrentLoc(), canvas, rect);
/*
		for (int i = 0; i < 45; i++) {
			setUpNeighbors(i);
			paint.setStyle(Paint.Style.FILL);
			if (stroke) {
				paint.setStyle(Paint.Style.FILL);
				stroke = false;
			}
			if (countHeight == 5) {
				left = 0;
				right = inc;
				top += bottomInc;
				bottom += bottomInc;
				countHeight = 0;
			}
			if (first) {
				rect.set(left, top, right, bottom);
				first = false;
			} else {
				rect.set(left, top, right, bottom);
			}

			int x = (int) rect.exactCenterX();
			int y = (int) rect.exactCenterY();
			canvas.drawRect(rect, paint);
			if (i == getCurrentLoc()) {
				canvas.drawBitmap(player, x - 20, y - 25, paint);
				//drawItems(canvas);
			}

			left = right;
			right += inc;
			count++;
			countHeight++;

		}
		*/
		rect.set(gridMap.get(getCurrentLoc()).getLeft(), gridMap.get(getCurrentLoc()).getTop(),
				gridMap.get(getCurrentLoc()).getRight(), gridMap.get(getCurrentLoc()).getBottom());
		int x = (int) rect.exactCenterX();
		int y = (int) rect.exactCenterY();
		canvas.drawBitmap(player, x-20, y-25, paint);
		
		drawButtons(rect, canvas);

		initDraw = false;
		// canvas.drawRect(10, 10, 10, 10, paint);
		drawText(actor.getLocation(), actor, canvas);

	}

	private void drawButtons(Rect rect, Canvas canvas) {
		int bRow = canvas.getHeight() - (canvas.getHeight() / 10);

		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);

		rect.set(0, bRow, canvas.getWidth() / 2, canvas.getHeight());
		gridMap.put(
				DOWN,
				new GridCell(0, canvas.getWidth() / 2, bRow, canvas.getHeight()));

		int x = (int) rect.exactCenterX();
		int y = (int) rect.exactCenterY();
		if (canGoDown) {
			canvas.drawBitmap(downGreen, x, y - 20, paint);
		} else {
			canvas.drawBitmap(downRed, x, y - 20, paint);
		}

		canvas.drawRect(rect, paint);

		rect.set(canvas.getWidth() / 2, bRow, canvas.getWidth(),
				canvas.getHeight());
		gridMap.put(UP, new GridCell(canvas.getWidth() / 2, canvas.getWidth(),
				bRow, canvas.getHeight()));

		x = (int) rect.exactCenterX();
		y = (int) rect.exactCenterY();
		if (canGoUp) {
			canvas.drawBitmap(upGreen, x, y - 20, paint);
		} else {
			canvas.drawBitmap(upRed, x, y - 20, paint);
		}

		canvas.drawRect(rect, paint);

	}

	private int runGrid(Canvas canvas) {

		int location = getCurrentLoc();

		setCanGoUp(false);
		setCanGoDown(false);

		int countHeight = 0;
		int left = 0;
		int top = 0;
		int right = canvas.getWidth() / 5;
		int inc = canvas.getWidth() / 5;
		int bottom = canvas.getHeight() / 10;
		int bottomInc = canvas.getHeight() / 10;

		for (int i = 0; i < 45; i++) {

			if (countHeight == 5) {
				left = 0;
				right = inc;
				top += bottomInc;
				bottom += bottomInc;
				countHeight = 0;
			}
			gridMap.put(i, new GridCell(left, right, bottom, top));
			/*
			 * if (!init && clickX >= left && clickX <=right && clickY>=top &&
			 * clickY <=bottom){ setCurrentLoc(i); location = i; }
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

	/* Colors are used to represent the accessibility of a location by the player 
	   where green is a location the player can move into, blue locations are not. (blue is water)
	    The remaining locations in the level
	   are colored gray*/
	private void setUpNeighbors(int i, Canvas canvas, Rect rect) {

		HashMap<Neighbor, Location> m = map.get(getLevel())
				.get(getCurrentLoc()).getNeighbors();

/*
		if (i == getCurrentLoc()) {
			paint.setColor(Color.WHITE);
			stroke = true;
			
			return;
		}
		*/
		
		GridCell cell = null;
		cell = gridMap.get(i);
		paint.setColor(Color.WHITE);
		rect.set(cell.getLeft(), cell.getTop(), cell.getRight(), cell.getBottom());
		canvas.drawRect(rect, paint);
		
		
		//get display info for south neighbor
		cell = gridMap.get(getCurrentLoc() + 5);
		if (m.get(Neighbor.SOUTH) == null) {
			paint.setColor(Color.GRAY);
			rect.set(cell.getLeft(), cell.getTop(), cell.getRight(), cell.getBottom());
			canvas.drawRect(rect, paint);
		} else if (m.get(Neighbor.SOUTH).canAddActor()) {
			movingOptions.add(mapping.get(m.get(Neighbor.SOUTH)));
			paint.setColor(Color.GREEN);
			rect.set(cell.getLeft(), cell.getTop(), cell.getRight(), cell.getBottom());
			canvas.drawRect(rect, paint);
		} else {
			paint.setColor(Color.BLUE);
			rect.set(cell.getLeft(), cell.getTop(), cell.getRight(), cell.getBottom());
			canvas.drawRect(rect, paint);
		}
		
		//get display info for north neighbor
		cell = gridMap.get(getCurrentLoc() - 5);

			if (m.get(Neighbor.NORTH) == null) {
				paint.setColor(Color.GRAY);
				rect.set(cell.getLeft(), cell.getTop(), cell.getRight(), cell.getBottom());
				canvas.drawRect(rect, paint);
			} else if (m.get(Neighbor.NORTH).canAddActor()) {
				movingOptions.add(mapping.get(m.get(Neighbor.NORTH)));
				paint.setColor(Color.GREEN);
				rect.set(cell.getLeft(), cell.getTop(), cell.getRight(), cell.getBottom());
				canvas.drawRect(rect, paint);
			} else {
				paint.setColor(Color.BLUE);
				rect.set(cell.getLeft(), cell.getTop(), cell.getRight(), cell.getBottom());
				canvas.drawRect(rect, paint);
			}

		//get display info for east neighbor
		cell = gridMap.get(getCurrentLoc() + 1);
			if (m.get(Neighbor.EAST) == null) {
				paint.setColor(Color.GRAY);
				rect.set(cell.getLeft(), cell.getTop(), cell.getRight(), cell.getBottom());
				canvas.drawRect(rect, paint);
			} else if (m.get(Neighbor.EAST).canAddActor()) {
				movingOptions.add(mapping.get(m.get(Neighbor.EAST)));
				paint.setColor(Color.GREEN);
				rect.set(cell.getLeft(), cell.getTop(), cell.getRight(), cell.getBottom());
				canvas.drawRect(rect, paint);
			} else {
				paint.setColor(Color.BLUE);
				rect.set(cell.getLeft(), cell.getTop(), cell.getRight(), cell.getBottom());
				canvas.drawRect(rect, paint);
			}
		
		//get display info for west neighbor
		cell = gridMap.get(getCurrentLoc() - 1);
			if (m.get(Neighbor.WEST) == null) {
				paint.setColor(Color.GRAY);
				rect.set(cell.getLeft(), cell.getTop(), cell.getRight(), cell.getBottom());
				canvas.drawRect(rect, paint);
			} else if (m.get(Neighbor.WEST).canAddActor()) {
				movingOptions.add(mapping.get(m.get(Neighbor.WEST)));
				paint.setColor(Color.GREEN);
				rect.set(cell.getLeft(), cell.getTop(), cell.getRight(), cell.getBottom());
				canvas.drawRect(rect, paint);
			} else {
				paint.setColor(Color.BLUE);
				rect.set(cell.getLeft(), cell.getTop(), cell.getRight(), cell.getBottom());
				canvas.drawRect(rect, paint);
			}
		

		//display info for above and below neighbor
		if (m.get(Neighbor.ABOVE) != null
				&& m.get(Neighbor.ABOVE).canAddActor()) {
			movingOptions.add(mapping.get(m.get(Neighbor.ABOVE)));
			setCanGoUp(true);
		} else if (m.get(Neighbor.BELOW) != null
				&& m.get(Neighbor.BELOW).canAddActor()) {
			movingOptions.add(mapping.get(m.get(Neighbor.BELOW)));
			setCanGoDown(true);
		} else {
			setCanGoDown(false);
			setCanGoUp(false);
		}



	}

	public void notify(Location newLocation, Actor player) {
		String text = "Location " + newLocation.toString()
				+ " entered by actor " + player.toString();

		if (toast != null) {
			toast.cancel();
		}

		toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.show();
		Vibrator v = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		// Vibrator v = (Vibrator)
		// context.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(20);
		// Canvas canvas = this.canvas;

	}

	// Draws a box of player stats for current location, obtained items and health
	public void drawText(Location location, Actor player, Canvas canvas) {
		LinearLayout layout = new LinearLayout(context);
		TextView textView = new TextView(context);
		textView.setVisibility(View.VISIBLE);
		textView.setText("Location: " + player.getLocation().getName()
				+ "\nEnergy: " + player.getEnergy() + "\nHP: " + player.getHealth());
		layout.addView(textView);

		layout.measure(canvas.getWidth(), canvas.getHeight());
		layout.layout(0, 0, canvas.getWidth(), canvas.getHeight());

		// To place the text view somewhere specific:
		// canvas.translate(0, 0);

		layout.draw(canvas);
	}

	private void setMapping() {
		mapping.clear();
		for (int i = 0; i < map.get(getLevel()).size(); i++) {
			mapping.put(map.get(getLevel()).get(i), i);
		}
	}

	public HashMap<Location, Integer> getMapping() {
		return mapping;
	}

	private Paint getPaint(Location location) {
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public ArrayList<Integer> getMovingOptions() {
		return movingOptions;
	}

	public boolean isCanGoUp() {
		return canGoUp;
	}

	private void setCanGoUp(boolean canGoUp) {
		this.canGoUp = canGoUp;
	}

	public void changeLevel(int dir) {
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
