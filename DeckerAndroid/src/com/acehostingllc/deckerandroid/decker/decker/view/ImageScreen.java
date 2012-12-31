package com.acehostingllc.deckerandroid.decker.decker.view;

import com.acehostingllc.deckerandroid.decker.decker.input.MouseEvent;

import android.view.MotionEvent;
import android.widget.ImageView;

public class ImageScreen extends ImageView {
	public int width;
	public int height;
	public float scale = 1;
	public float offsetX = 0;
	public float lastTouchX = 0;
	public float lastTouchY = 0;
	private ViewWrapper wrapper;

	public ImageScreen(ViewWrapper wrapper) {
		super(wrapper.getContext());
		this.wrapper = wrapper;
	}
	
	public void draw()
	{
		this.setImageBitmap(
				this.wrapper.buffer.getGraphics().getBitmapPallet(
						(int)offsetX, 0, width, height, scale
				)
		);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		int viewportX = (int) ((int) (e.getRawX() * scale) + this.offsetX);// + offsetX);
    	int viewportY = (int) (e.getRawY() * scale);
    	
    	// make these be a ratio of screen width / height
    	// apply ratio to viewport size to find coords, apply x offset for new coords
    	
    	if (e.getAction()==MotionEvent.ACTION_DOWN)
    	{
			this.lastTouchX = e.getRawX();
			this.lastTouchY = e.getRawY();
			this.wrapper.processEvent(new MouseEvent(MouseEvent.MOUSE_PRESSED, viewportX, viewportY, MouseEvent.BUTTON1));
			this.wrapper.update();
    	}
    	
    	if (e.getAction()==MotionEvent.ACTION_UP)
    	{
    		this.wrapper.processEvent(new MouseEvent(MouseEvent.MOUSE_RELEASED, viewportX, viewportY, MouseEvent.BUTTON1));
    		this.wrapper.update();
    	}
    	
    	if (e.getAction()==MotionEvent.ACTION_HOVER_ENTER)
    	{
    		this.wrapper.processEvent(new MouseEvent(MouseEvent.MOUSE_ENTERED, viewportX, viewportY, MouseEvent.BUTTON1));
    	}
    	if (e.getAction()==MotionEvent.ACTION_HOVER_EXIT)
    	{
    		this.wrapper.processEvent(new MouseEvent(MouseEvent.MOUSE_EXITED, viewportX, viewportY, MouseEvent.BUTTON1));
    	}

    	if (e.getAction()==MotionEvent.ACTION_MOVE)
    	{
			this.offsetX -= e.getRawX() - this.lastTouchX;
			if (this.offsetX < -250)
				this.offsetX = -250;
			if (this.offsetX > 150)
				this.offsetX = 150;
    		this.lastTouchX = e.getRawX();
    		this.lastTouchY = e.getRawY();
    		this.wrapper.draw();
    	}
		return true;
	}

}
