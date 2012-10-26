package com.acehostingllc.deckerandroid.decker.decker.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.acehostingllc.deckerandroid.DeckerActivity;
import com.acehostingllc.deckerandroid.decker.decker.model.*;
import com.acehostingllc.deckerandroid.decker.decker.util.*;
import com.acehostingllc.deckerandroid.decker.decker.view.AbstractView;



public final class ViewWrapper extends ViewGroup
{
	
	public ViewWrapper() {
		super(DeckerActivity.getAppContext());
		this.setView(new AbstractView());

		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		relativeParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		this.setLayoutParams(relativeParams);
		//this.setMinimumWidth(10);
		//this.setMinimumHeight(10);
	}

	// methods other parts of this program will call ************************************************************************
	private ImageView buffer;
	private boolean painting;
	private AbstractView view;
	//private AWTEvent lastEvent;
	private final Queue events = new Queue();
	private int mouse_x, mouse_y;
	private int frame_x, frame_y;
	private int old_width = -1, old_height = -1;
	private String oldScreenTitle = "";
	private Value oldDisplayedScreen = new Value();
	
	//public ViewWrapper () {
		//if (Global.getDisplayedComponent() instanceof Frame)
		//	Global.getDisplayedComponent().addComponentListener(this);
	//}
	

	//protected AWTEvent getLastEvent () { return lastEvent; }
	public AbstractView getView () { return view; }


	public void setView (final AbstractView view) {
		if (view == null)
			throw new RuntimeException("view must not be null");
		if (view == this.view)
			return;
		this.removeAllViews();
		this.view = view;
		this.addView(view);
		repaint();
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
		/*
		for (int i = events.size(); --i >= 0; ) {
			final AWTEvent e = (AWTEvent) events.remove();
			final AWTEvent e2 = lastEvent;
			lastEvent = e;
			final AbstractView v = view;
			boolean discardEvent = true;
			final int mx = mouse_x, my = mouse_y;
			if (v != null) {
				discardEvent = false;
				try {
					if (e instanceof MouseEvent) {
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
		*/
	}


	public void repaint() {
		// TODO Auto-generated method stub
		paint();
	}



	
	public void paint () {
		update();
	}
	
	
	public void processEvent (final Object e) {
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
final int w = Math.max(11, DisplayedComponent.getScreenWidth()), h = Math.max(11, DisplayedComponent.getScreenHeight());

					// draw the next frame
					if (buffer == null || buffer.getWidth() != w || buffer.getHeight() != h) {
						try {
							buffer = createImage(w, h);
						} catch (Throwable t) {
							// this ought to be extremely rare
System.out.println("FAILED TO CREATE screen buffer o_O");
							painting = false;
							return;
						}
					}

					//final Graphics bg = buffer.getGraphics();
					//bg.setFont(getFont());

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
					DisplayedComponent.drawScreen();
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

	private void drawImage(ImageView buffer2, int i, int j,
			ViewWrapper viewWrapper) {
		// TODO Auto-generated method stub
	}


	private ImageView createImage(int w, int h) {
		// TODO Auto-generated method stub
		return new ImageView(DeckerActivity.getAppContext());
	}


	public void update () {
		if (painting)
		{
			System.out.println("ispainting!");
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


	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		System.out.println("Onlayout was called-ViewWrapper");
		for (int i = 0; i < this.getChildCount(); i++)
		{
			this.getChildAt(i).layout(l, t, r, b);
		}
	}
}