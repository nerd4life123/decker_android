package com.acehostingllc.deckerandroid.decker.decker.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.graphics.drawable.shapes.Shape;

public class AndroidGraphics {
	
	private Bitmap pallet;
	private Canvas canvas;
	private Paint style;
	//private Paint color;
	//private Paint font;

	public AndroidGraphics() {
		this.pallet = Bitmap.createBitmap(320, 400, Bitmap.Config.ARGB_8888);
		this.canvas = new Canvas(pallet);
		//this.color = new Paint();
		//this.font = new Paint();
		this.style = new Paint();
	}
	
	public void drawImage(Bitmap image, int x, int y, Object object)
	{
		// THIS IS WORKING! JUST NEED TO USE ALPHA COLOR TO GET RID OF THAT NASTY PINK
		Paint alpha = new Paint();
		alpha.setAlpha(Color.rgb(255,  0,  255));
		canvas.drawBitmap(image, x, y, alpha);
	}

	public void setFont(Typeface font) {
		//System.out.println("Setting font. Has color " + font.getColor());
		this.style.setTypeface(font);
	}

	public void setColor(int background) {
		// does this really set background color?
		this.style.setColor(background);
	}

	public void fillRect(int x, int y, int w, int h) {
		// TODO Auto-generated method stub
		//System.out.println("Filling rect");
		Paint rectPaint = new Paint();
		rectPaint.setColor(Color.GREEN);
		//canvas.drawRect(x-(w/2), y+(h/2),  x+(w/2),  y-(h/2),  rectPaint);
		canvas.drawRect(x, y-h, x+w, y, this.style);
	}

	public void drawLine(int i, int j, int k, int l) {
		// THIS IS WORKING FINE, EXCEPT FOR COLOR
		Paint linePaint = new Paint();
		linePaint.setColor(Color.RED);
		canvas.drawLine(i,  j,  k,  l,  linePaint);
	}

	public Typeface getFont() {
		return this.style.getTypeface();
	}
	
	public FontMetrics getFontMetrics() {
		return this.style.getFontMetrics();
	}

	public void drawString(String text, int x, int y) {
		canvas.drawText(text, x, y-calculateHeight(this.style.getFontMetrics()), this.style);
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
		//System.out.println("Setting clip");
		canvas.clipRect(clip, Region.Op.REPLACE);
	}

	public void setClip(int x, int y, int w, int h) {
		// TODO Auto-generated method stub
		//canvas.clipr
		canvas.clipRect(x-(w/2), x+(w/2), y+(h/2), y-(h/2), Region.Op.REPLACE);
	}
	
	public static float calculateHeight(FontMetrics fm) {
		return fm.bottom - fm.top;
	}
}
