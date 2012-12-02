package com.cs413.walker.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.cs413.walker.items.Portable;
import com.cs413.walker.items.Reward;
import com.cs413.walker.locations.Location;
import com.cs413.walker.locations.Neighbor;
import com.example.walker.R;

public class TerrainView extends View {

	private static final String TAG = "VIEW";
	private final Location start;

	public final static int MAX_CELLS = 45;

	public static final int UP = -1;
	public static final int DOWN = -2;
	public static final int INVENTORY = -3;

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
	Bitmap chest = BitmapFactory.decodeResource(getResources(),
			R.drawable.chest);
	Bitmap monster = BitmapFactory.decodeResource(getResources(),
			R.drawable.monster);

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

		

		setCurrentLoc(mapping.get(actor.getLocation()));

		setFocusable(true);

	}

	private void drawItems(Canvas canvas, GridCell gridCell) {
		ArrayList<Portable> items = actor.getLocation().getItems();
		if (items.size() > 0) {
			canvas.drawBitmap(chest, gridCell.getLeft(), gridCell.getTop(),
					paint);
		}
	}

	private void drawButtons(Rect rect, Canvas canvas) {
		int bRow = canvas.getHeight() - (canvas.getHeight() / 10);

		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);

		rect.set(0, bRow, canvas.getWidth() / 2 - 60, canvas.getHeight()); // left
																			// side,
																			// down
																			// button
		gridMap.put(DOWN, new GridCell(0, canvas.getWidth() / 2 - 60, bRow,
				canvas.getHeight()));

		int x = (int) rect.exactCenterX();
		int y = (int) rect.exactCenterY();
		if (canGoDown) {
			canvas.drawBitmap(downGreen, x, y - 20, paint);
		} else {
			canvas.drawBitmap(downRed, x, y - 20, paint);
		}

		canvas.drawRect(rect, paint);

		rect.set(canvas.getWidth() / 2 - 60, bRow, canvas.getWidth() / 2 + 60,
				canvas.getHeight()); // center, inventory
		gridMap.put(INVENTORY,
				new GridCell(canvas.getWidth() / 2 - 60,
						canvas.getWidth() / 2 + 60, bRow, canvas.getHeight()));
		x = (int) rect.exactCenterX();
		y = (int) rect.exactCenterY();
		paint.setTextSize(15);
		paint.setFakeBoldText(true);
		canvas.drawText("Inventory", x - 35, y, paint);
		canvas.drawRect(rect, paint);

		rect.set(canvas.getWidth() / 2 + 60, bRow, canvas.getWidth(), // right
																		// side,
																		// up
																		// button
				canvas.getHeight());
		gridMap.put(UP,
				new GridCell(canvas.getWidth() / 2 + 60, canvas.getWidth(),
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

	// Draws a box of player stats for current location, obtained items, health
	// & lives
	public void drawText(Location location, Actor actor, Canvas canvas) {
		LinearLayout layout = new LinearLayout(context);
		TextView textView = new TextView(context);
		textView.setVisibility(View.VISIBLE);
		textView.setText("Current Location: " + actor.getLocation().getName()
				+ "\nLives: " + actor.getLives() + "\nEnergy: "
				+ actor.getEnergy() + "\nHP: " + actor.getHealth()
				+ "\nCoins: " + actor.getCoins() + "\nItems: "
				+ actor.getCurrentCapacity() + "/" + actor.getCapacity()
				+ "\nDifficulty: not a attribute of actor");
		layout.addView(textView);

		layout.measure(canvas.getWidth(), canvas.getHeight());
		layout.layout(0, 0, canvas.getWidth(), canvas.getHeight());

		// To place the text view somewhere specific:
		// canvas.translate(0, 0);

		layout.draw(canvas);
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

		for (int i = 0; i < MAX_CELLS; i++) {

			if (countHeight == 5) {
				left = 0;
				right = inc;
				top += bottomInc;
				bottom += bottomInc;
				countHeight = 0;
			}
			gridMap.put(i, new GridCell(left, right, bottom, top));

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

	/*
	 * Colors are used to represent the accessibility of a location by the
	 * player where green is a location the player can move into, blue locations
	 * are not. (blue is water) The remaining locations in the level are colored
	 * gray
	 */
	private void setUpNeighbors(int i, Canvas canvas, Rect rect) {

		HashMap<Neighbor, Location> m = map.get(getLevel())
				.get(getCurrentLoc()).getNeighbors();

		/*
		 * if (i == getCurrentLoc()) { paint.setColor(Color.WHITE); stroke =
		 * true;
		 * 
		 * return; }
		 */

		GridCell cell = null;
		cell = gridMap.get(i);
		paint.setColor(Color.WHITE);
		rect.set(cell.getLeft(), cell.getTop(), cell.getRight(),
				cell.getBottom());
		canvas.drawRect(rect, paint);

		// get display info for south neighbor
		if (getCurrentLoc() + 5 < MAX_CELLS) {
			drawCell(m.get(Neighbor.SOUTH), gridMap.get(getCurrentLoc() + 5), rect, canvas);
		}
			/*
			cell = gridMap.get(getCurrentLoc() + 5);
			if (m.get(Neighbor.SOUTH) == null) {
				paint.setColor(Color.GRAY);
				rect.set(cell.getLeft(), cell.getTop(), cell.getRight(),
						cell.getBottom());
				canvas.drawRect(rect, paint);
			} else if (m.get(Neighbor.SOUTH).canAddActor()) {
				movingOptions.add(mapping.get(m.get(Neighbor.SOUTH)));
				paint.setColor(Color.GREEN);
				rect.set(cell.getLeft(), cell.getTop(), cell.getRight(),
						cell.getBottom());
				canvas.drawRect(rect, paint);
			} else {
				paint.setColor(Color.BLUE);
				rect.set(cell.getLeft(), cell.getTop(), cell.getRight(),
						cell.getBottom());
				canvas.drawRect(rect, paint);
			}
			if (m.get(Neighbor.SOUTH).getActors().size() > 0){
				drawMonster(cell, canvas);
			}
		}
	*/
		// get display info for north neighbor
		if (getCurrentLoc() - 5 >= 0) {
			drawCell(m.get(Neighbor.NORTH), gridMap.get(getCurrentLoc() - 5), rect, canvas);
		}


		// get display info for east neighbor
		if (getCurrentLoc() + 1 < MAX_CELLS) {
			drawCell(m.get(Neighbor.EAST), gridMap.get(getCurrentLoc() + 1), rect, canvas);
		}
		

		// get display info for west neighbor
		if (getCurrentLoc() - 1 >= 0) {
			drawCell(m.get(Neighbor.WEST), gridMap.get(getCurrentLoc() - 1), rect, canvas);
		}
			

		// display info for above and below neighbor
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
	
	private void drawCell(Location location, GridCell cell, Rect rect, Canvas canvas){
		if (location == null) {
			paint.setColor(Color.GRAY);
			rect.set(cell.getLeft(), cell.getTop(), cell.getRight(),
					cell.getBottom());
			canvas.drawRect(rect, paint);
		} else if (location.canAddActor()) {
			movingOptions.add(mapping.get(location));
			paint.setColor(Color.GREEN);
			rect.set(cell.getLeft(), cell.getTop(), cell.getRight(),
					cell.getBottom());
			canvas.drawRect(rect, paint);
		} else {
			paint.setColor(Color.BLUE);
			rect.set(cell.getLeft(), cell.getTop(), cell.getRight(),
					cell.getBottom());
			canvas.drawRect(rect, paint);
		}
		if (location != null && location.getActors().size() > 0){
			drawMonster(cell, canvas);
		}
	}

	private void drawMonster(GridCell cell, Canvas canvas ) {
		canvas.drawBitmap(monster, cell.getRight()-45, cell.getTop(),
				paint);	
	}

	public void notify(String string) {

		if (toast != null) {
			toast.cancel();
		}
		toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
		toast.show();
		Vibrator v = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(20);

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

		rect.set(gridMap.get(getCurrentLoc()).getLeft(),
				gridMap.get(getCurrentLoc()).getTop(),
				gridMap.get(getCurrentLoc()).getRight(),
				gridMap.get(getCurrentLoc()).getBottom());
		int x = (int) rect.exactCenterX();
		int y = (int) rect.exactCenterY();
		canvas.drawBitmap(player, x - 20, y - 25, paint);
		drawItems(canvas, gridMap.get(getCurrentLoc()));

		drawButtons(rect, canvas);

		initDraw = false;
		// canvas.drawRect(10, 10, 10, 10, paint);
		drawText(actor.getLocation(), actor, canvas);
		boolean newItem = true;
		if (actor.getLocation().getItems().size() > 0 && newItem) {
			drawItemsBox();
			newItem = false;
		}
	}
/* Not needed for this version. Could be used it Rewards that are not items are implemented in later versions
	void drawRewards() {
		final ArrayList<Reward> mSelectedItems = new ArrayList<Reward>();
		// Where we track the selected items
		String[] arr = new String[actor.getLocation().getRewards().size()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = actor.getLocation().getRewards().get(i).toString();
		}

		CharSequence[] list = new CharSequence[arr.length];
		for (int i = 0; i < arr.length; i++) {
			list[i] = arr[i];
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		// Set the dialog title
		builder.setTitle("Pick Up Rewards?")
				// Specify the list array, the items to be selected by default
				// (null for none),
				// and the listener through which to receive callbacks when
				// items are selected
				.setItems(list,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								actor.addCoins(actor.getLocation().getRewards().get(which).getValue());
								actor.getLocation().getRewards()
										.remove(which);
									// If the user checked the item, add it to the
									// selected
									// items
									// mSelectedItems.add(actor.getLocation().getItems().get(which));
									// If the user checked the item, add it to
									// the selected items
									//mSelectedItems.add(actor.getLocation()
									//		.getRewards().get(which));
								}
							}
						)
				.setPositiveButton("Done",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								Log.d(TAG, "Cancelled");
							}
						});
		builder.show();
	}
*/
	void drawItemsBox() {

		final ArrayList<Portable> mSelectedItems = new ArrayList<Portable>();
		// Where we track the selected items
		String[] arr = new String[actor.getLocation().getItems().size()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = actor.getLocation().getItems().get(i).toString();
		}

		CharSequence[] list = new CharSequence[arr.length];
		for (int i = 0; i < arr.length; i++) {
			list[i] = arr[i];
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		// Set the dialog title
		builder.setTitle("Pick Up?")
				// Specify the list array, the items to be selected by default
				// (null for none),
				// and the listener through which to receive callbacks when
				// items are selected
				.setItems(list, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Portable item = actor.getLocation().getItems()
								.get(which);
						if (actor.addItems(item)) {
							actor.getLocation().getItems().remove(which);
							// If the user checked the item, add it to the
							// selected
							// items
							// mSelectedItems.add(actor.getLocation().getItems().get(which));
						} else {
							AlertDialog.Builder alert = new AlertDialog.Builder(
									context);

							alert.setTitle("You inventory is full!");
							alert.show();

						}
					}

				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								Log.d(TAG, "Cancelled");
							}
						});
		builder.show();
	}

	public void showInventory() {
		final ArrayList<Portable> mSelectedItems = new ArrayList<Portable>();
		// Where we track the selected items
		String[] arr = new String[actor.getItems().size()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = actor.getItems().get(i).toString();
		}

		CharSequence[] list = new CharSequence[arr.length];
		for (int i = 0; i < arr.length; i++) {
			list[i] = arr[i];
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		// Set the dialog title
		builder.setTitle("Use")
				// Specify the list array, the items to be selected by default
				// (null for none),
				// and the listener through which to receive callbacks when
				// items are selected
				.setItems(list, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.d(TAG, String.valueOf(which));
						actor.useItem(actor.getItems().get(which));
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								Log.d(TAG, "Cancelled");
							}
						});
		builder.show();
	}

	private void setMapping() {
		mapping.clear();
		for (int i = 0; i < map.get(getLevel()).size(); i++) {
			mapping.put(map.get(getLevel()).get(i), i);
		}
	}
	
	private Bitmap fixBmp(Bitmap bmp){
		Bitmap bmp2 = bmp.copy(bmp.getConfig(), true);
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		for(int x = 0; x < width; x++){
		    for(int y = 0; y < height; y++)
		    {
		        if(bmp.getPixel(x, y) == Color.WHITE)
		        {
		            bmp2.setPixel(x, y, paint.getColor());
		        }
		    }
		}
		return bmp2;
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
