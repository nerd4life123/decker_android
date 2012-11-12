package com.acehostingllc.deckerandroid.decker.decker.view;

public class Buffer {
	
	private AndroidGraphics graphics;
	private int width;
	private int height;
	
	public Buffer(int width, int height)
	{
		this.graphics = new AndroidGraphics(width, height);
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public AndroidGraphics getGraphics() {
		return this.graphics;
	}
}
