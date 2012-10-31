package com.acehostingllc.deckerandroid.decker.decker.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.shapes.Shape;

public class AndroidGraphics {
	
	private Bitmap pallet;
	private Canvas canvas;
	private int color = Color.BLUE;
	private Paint font;

	public AndroidGraphics() {
		this.pallet = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
		this.canvas = new Canvas(pallet);
	}
	
	public void drawImage(Bitmap image, int x, int y, Object object)
	{
		//System.out.println("Drawing bitmap");
		canvas.drawBitmap(image, x, y, new Paint());
	}

	public void setFont(Paint font) {
		// TODO Auto-generated method stub
		this.font = font;
		//System.out.println("Setting font");
	}

	public void setColor(int background) {
		// TODO Auto-generated method stub
		this.color = background;
		//System.out.println("Setting color");
	}

	public void fillRect(int i, int j, int k, int l) {
		// TODO Auto-generated method stub
		canvas.drawRect(i,  j,  k,  l,  this.font);
		//System.out.println("Filling rect");
	}

	public void drawLine(int i, int j, int k, int l) {
		// TODO Auto-generated method stub
		canvas.drawLine(i,  j,  k,  l,  this.font);
		//System.out.println("Drawing line");
	}

	public Paint getFont() {
		// TODO Auto-generated method stub
		return this.font;
	}

	public void drawString(String text, int x, int y) {
		canvas.drawText(text, x, y, this.font);
	}

	public Bitmap getBitmapPallet()
	{
		return this.pallet;
	}

	public Rect getClip() {
		// TODO Auto-generated method stub
		//System.out.println("Getting clip");
		return canvas.getClipBounds();
	}

	public boolean clipRect(int x, int y, int w, int h) {
		// TODO Auto-generated method stub
		//System.out.println("Clipping rect");
		return canvas.clipRect(x-(w/2), x+(w/2), y+(h/2), y-(h/2));
	}

	public void setClip(Rect clip) {
		// TODO Auto-generated method stub
		System.out.println("Setting clip");
	}
}
