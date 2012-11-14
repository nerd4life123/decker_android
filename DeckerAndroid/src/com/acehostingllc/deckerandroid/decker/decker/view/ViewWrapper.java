package com.acehostingllc.deckerandroid.decker.decker.view;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import com.acehostingllc.deckerandroid.DeckerActivity;
import com.acehostingllc.deckerandroid.decker.decker.input.DeckerEvent;
import com.acehostingllc.deckerandroid.decker.decker.input.MouseEvent;
import com.acehostingllc.deckerandroid.decker.decker.model.*;
import com.acehostingllc.deckerandroid.decker.decker.util.*;
import com.acehostingllc.deckerandroid.decker.decker.view.AbstractView;

public final class ViewWrapper extends ImageView
{

	// methods other parts of this program will call ************************************************************************
	private Buffer buffer;
	private boolean painting;
	private AbstractView view;
	private DeckerEvent lastEvent;
	private final Queue events = new Queue();
	private int mouse_x, mouse_y;
	//private int frame_x, frame_y;
	private int old_width = -1, old_height = -1;
	private String oldScreenTitle = "";
	private Value oldDisplayedScreen = new Value();
	private int width;
	private int height;
	private float scrollX = 0;
	private float scrollY = 0;
	private float lastTouchX = 0;
	private float lastTouchY = 0;
	
	public ViewWrapper(DeckerActivity activity) {
		super(activity);
		this.setView(new AbstractView());
		//this.setv'
		Display display = ((WindowManager)activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		this.width = display.getWidth();//((DeckerActivity)context).getWindowManager().getDefaultDisplay().getWidth();
		this.height = display.getHeight();;//((DeckerActivity)context).getWindowManager().getDefaultDisplay().getHeight();
	}
    
    @Override
    public boolean onTouchEvent(MotionEvent e) {
    	DisplayMetrics dm = new DisplayMetrics();
    	((DeckerActivity)this.getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
    	int x = (int) (e.getRawX() - scrollX);//- 60;
    	int y = (int) (e.getRawY() - scrollY);// - 215;
    	
    	if (e.getAction()==MotionEvent.ACTION_DOWN)
    	{
			this.lastTouchX = e.getRawX();
			this.lastTouchY = e.getRawY();
        	Global.getViewWrapper().processEvent(new MouseEvent(MouseEvent.MOUSE_PRESSED, x, y, MouseEvent.BUTTON1));
    	}
    	
    	if (e.getAction()==MotionEvent.ACTION_UP)
    	{
        	Global.getViewWrapper().processEvent(new MouseEvent(MouseEvent.MOUSE_RELEASED, x, y, MouseEvent.BUTTON1));
     	}
    	
    	if (e.getAction()==MotionEvent.ACTION_HOVER_ENTER)
    	{
        	Global.getViewWrapper().processEvent(new MouseEvent(MouseEvent.MOUSE_ENTERED, x, y, MouseEvent.BUTTON1));
    	}
    	if (e.getAction()==MotionEvent.ACTION_HOVER_EXIT)
    	{
        	Global.getViewWrapper().processEvent(new MouseEvent(MouseEvent.MOUSE_EXITED, x, y, MouseEvent.BUTTON1));
    	}

    	if (e.getAction()==MotionEvent.ACTION_MOVE)
    	{
    		this.scrollX += e.getRawX() - this.lastTouchX;
    		this.scrollY += e.getRawY() - this.lastTouchY;
    		this.lastTouchX = e.getRawX();
    		this.lastTouchY = e.getRawY();
        	//Global.getViewWrapper().processEvent(new MouseEvent(MouseEvent.MOUSE_MOVED, x, y, MouseEvent.BUTTON1));
    	}
    	//create the event
    	System.out.println("Screen touched. passing to viewwrapper");
    	
    	Global.getViewWrapper().update();
		return true;
    }
	

	protected DeckerEvent getLastEvent () { return lastEvent; }
	public AbstractView getView () { return view; }


	public void setView (final AbstractView view) {
		if (view == null)
			throw new RuntimeException("component must not be null");
		if (view == this.view)
			return;
		this.view = view;
		update();
	}


// methods and variables which implement the class functionality **********************************************************************


	//public void componentHidden (ComponentEvent e)  {}

	/*
	public void componentMoved (ComponentEvent e)  {
		final Component c = e.getComponent();
		if (e.getComponent().getX() != frame_x)
			Global.getEngineData().get("display_center_x").set(c.getX()+c.getWidth()/2);
		if (e.getComponent().getY() != frame_y)
			Global.getEngineData().get("display_center_y").set(c.getY()+c.getHeight()/2);
	}
	*/

	//public void componentResized (ComponentEvent e)  {}


	//public void componentShown (ComponentEvent e)  {}


	private void handleUserInput () {
		System.out.println("ViewWrapper: Handling user input");
		for (int i = events.size(); --i >= 0; ) {
			final DeckerEvent e = (DeckerEvent) events.remove();
			final DeckerEvent e2 = lastEvent;
			lastEvent = e;
			final AbstractView v = view;
			boolean discardEvent = true;
			final int mx = mouse_x, my = mouse_y;
			if (v != null) {
				System.out.println("ViewWrapper: Abstract view was not null");
				discardEvent = false;
				try {
					if (e instanceof MouseEvent) {
						System.out.println("ViewWrapper: Read instance of MouseEvent");
						mouse_x = ((MouseEvent)e).getX();
						mouse_y = ((MouseEvent)e).getY();
					}
					discardEvent = DisplayedComponent.handleUserInput(e, mouse_x, mouse_y, mouse_x-mx, mouse_y-my);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			if (discardEvent) {
				lastEvent = e2;
			}
		}
		
	}
	
	
	public void processEvent (final DeckerEvent e) {
		events.add(e);
	}
	

	/** adjusts the Bounds of the Frame when the top level view's bounds settings change */
		
	private void setScreenSize (int new_width, int new_height)  {
		/*if (new_width <= 10)
			new_width = 11;
		if (new_height <= 11)
			new_height = 11;
		// the Frame should be the top-most parent object of the ViewWrapper
		Component parent = getParent();
		while (parent.getParent() != null)
			parent = parent.getParent();
		if (parent != null && parent instanceof Frame)  {
			// we need to add the size of the Frame's border to the bounds
			final Insets i = ((Frame)parent).getInsets();
			new_width  += i.left + i.right;
			new_height += i.top  + i.bottom;
			// adjust x and y
			frame_x = Global.getEngineData().get("display_center_x").integer() - new_width/2;
			frame_y = Global.getEngineData().get("display_center_y").integer() - new_height/2;
			if (frame_x+new_width > parent.getToolkit().getScreenSize().width)
				frame_x = parent.getToolkit().getScreenSize().width - new_width;
			if (frame_y+new_height > parent.getToolkit().getScreenSize().height)
				frame_y = parent.getToolkit().getScreenSize().height - new_height;
			if (frame_x < 0)
				frame_x = 0;
			if (frame_y < 0)
				frame_y = 0;
			// set the Frame bounds to the new values
			((Frame)parent).setBounds(frame_x, frame_y, new_width, new_height);
			((Frame)parent).doLayout();*/
		//}
	}
		
	
	private void setTitle (final String new_title)  {
		// the Frame should be the top-most parent object of the ViewWrapper
		//Component parent = Global.getViewWrapper().getParent();
		//while (parent.getParent() != null)
			//parent = parent.getParent();
		//if (parent != null && parent instanceof Frame)
			//((Frame)parent).setTitle(new_title);
		
		// LOL
	}
	
	
	private void synchronizedUpdate () {
		/*
		if (!this.isFocused()) {
			return;
		}*/
		painting = true;
		try {
			handleUserInput();

			final Value scr = Global.getDisplayedScreen();
			if (scr != null) {
				if (oldDisplayedScreen == null || !scr.equals(oldDisplayedScreen)) {
if (Global.debug_level > 0)
System.out.println("ViewWrapper: ** switching screens **:"+scr.toStringForPrinting());
					oldDisplayedScreen.set(scr);
					DisplayedComponent.setDisplayedScreen(scr);
				}

//			if (view != null) {
//final int w = Math.max(11, DisplayedComponent.getScreenWidth()), h = Math.max(11, DisplayedComponent.getScreenHeight());
		final int w = this.width;
		final int h = this.height;
					// draw the next frame
					if (buffer == null || buffer.getWidth() != w || buffer.getHeight() != h) {
						try {
							System.out.println("Creating new screen buffer");
							buffer = createImage(w, h);
						} catch (Throwable t) {
							// this ought to be extremely rare
System.out.println("FAILED TO CREATE screen buffer o_O");
							painting = false;
							return;
						}
					}

					final AndroidGraphics bg = buffer.getGraphics();
					bg.setFont(getFont());

					// fetch the background color
/*						final Value bgcolor_string = ScriptNode.getValue("BACKGROUND_COLOR");
					if (bgcolor_string != null && bgcolor_string.type() == Value.STRING) {
						final Color bgcolor = AbstractView.getColor(bgcolor_string.string());
						if (bgcolor != null) {
							setBackground(bgcolor);
							bg.setColor(bgcolor);
							bg.fillRect(0, 0, w, h);
						}
					}
					bg.setColor(getForeground());
*/
					DisplayedComponent.drawScreen(bg);
//						view.drawContent(bg); // call drawContent() instead of paint(), because the coordinate system already sits where it should
					if (w != old_width || h != old_height) {
						old_width = w;
						old_height = h;
						setScreenSize(w, h);
					}
					// update the title
					if (scr.type() == Value.STRUCTURE) {
						final Value title = scr.get("title");
						String s;
						if (title != null && !title.equalsConstant("UNDEFINED") && !(s=title.toString()).equals(oldScreenTitle)) {
							setTitle(s);
							oldScreenTitle = s;
						}
					}
					drawImage(buffer, 0, 0, this);
				}
//			}
		} catch (Throwable t) {
			t.printStackTrace();
System.out.println("exiting from program instead of trying to repaint after error");
System.exit(1);
		}
		painting = false;
	}


	private Paint getFont() {
		return new Paint();
	}


	private void drawImage(Buffer buffer, int i, int j,
			ViewWrapper viewWrapper) {
		Rect srcRect = new Rect((int)this.scrollX, (int)this.scrollY, (int)this.scrollX+width, (int)this.scrollY+height);
		Rect frame = new Rect(0, 0, width, height);
		this.setImageBitmap(buffer.getGraphics().getBitmapPallet(frame, srcRect));
	}


	private Buffer createImage(int w, int h) {
		return new Buffer(w, h);
	}


	public void update () {
		if (painting)
		{
			return;
		}
		/*
		if (this.getWidth() == 0 || this.getHeight() == 0)
		{
			System.out.println("width or height is 0, not updating");
			return;
		}*/
		synchronizedUpdate();
	}
}