package com.acehostingllc.deckerandroid.decker.decker.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.Region;

public class AndroidGraphics {
	
	private Bitmap pallet;
	private Canvas canvas;
	private int color;
	private Paint font;

	public AndroidGraphics(int width, int height) {
		this.pallet = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_4444);
		this.canvas = new Canvas(pallet);
		this.color = Color.WHITE;
		this.font = null;
	}
	
	public void drawImage(Bitmap image, int x, int y, Object object)
	{
		Paint alpha = new Paint();
		alpha.setAlpha(Color.rgb(255,  0,  255));
		canvas.drawBitmap(image, x, y, alpha);
	}

	public void setFont(Paint font) {
		this.font = font;
	}

	public void setColor(int background) {
		this.color = background;
	}

	public void fillRect(int x, int y, int w, int h) {
		Paint rectPaint = new Paint();
		rectPaint.setColor(this.color);
		rectPaint.setStyle(Paint.Style.FILL);
		System.out.println("Drawing rect with left at " + x + " and top at " + y);
		canvas.drawRect(x, y, x+w, y+h, rectPaint);
	}

	public void drawLine(int i, int j, int k, int l) {
		Paint linePaint = new Paint();
		linePaint.setColor(this.color);
		canvas.drawLine(i,  j,  k,  l,  linePaint);
	}

	public Paint getFont() {
		return this.font;
	}
	
	public FontMetrics getFontMetrics() {
		return this.font.getFontMetrics();
	}

	public void drawString(String text, int x, int y) {
		Paint stringPaint = new Paint(this.font);
		stringPaint.setColor(this.color);
		canvas.drawText(text, x, y, stringPaint);
	}
	
	public void clear() {
		Paint testPaint = new Paint();
		testPaint.setColor(Color.BLACK);
		this.canvas.drawRect(0, 0, 1000, 1000, testPaint);
	}

	public Bitmap getBitmapPallet(int left, int top, int width, int height, float scale)
	{
		Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
		Canvas newCanvas = new Canvas(newBitmap);
		//newCanvas.drawBitmap(this.pallet, left, top, new Paint());
		Rect src = new Rect(
				(int)(scale * left), 
				(int)(scale * top), 
				(int)(scale * (left+width)), 
				(int)(scale * (top+height)));
		Rect dst = new Rect(0, 0, width, height);//new Rect(left, top, left+width, top+height);
		newCanvas.drawBitmap(this.pallet, src, dst, new Paint());
		
		return newBitmap;
	}

	public Rect getClip() {
		return canvas.getClipBounds();
	}

	public boolean clipRect(int x, int y, int w, int h) {
		return canvas.clipRect(x, y, x+w, y+h);
	}
	
	public void setClip(Rect clip) {
		canvas.clipRect(clip, Region.Op.REPLACE);
	}

	public void setClip(int x, int y, int w, int h) {
		//canvas.clipRect(x-(w/2), x+(w/2), y+(h/2), y-(h/2), Region.Op.REPLACE);
		canvas.clipRect(x, y, x+w, y+h, Region.Op.REPLACE);
	}
}
