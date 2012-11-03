package com.acehostingllc.deckerandroid.decker.decker.input;

public class MouseEvent extends InputEvent {
	
	int x;
	int y;
	int button;
	
	public MouseEvent(int id, int x, int y, int button)
	{
		super(id);
		this.x = x;
		this.y = y;
		this.button = button;
	}

	public static final int MOUSE_DRAGGED = 100;
	public static final int MOUSE_ENTERED = 101;
	public static final int MOUSE_MOVED = 102;
	public static final int MOUSE_EXITED = 103;
	public static final int MOUSE_PRESSED = 104;
	public static final int MOUSE_RELEASED = 105;
	public static final int BUTTON1 = 106;
	public static final int BUTTON2 = 107;
	public static final int BUTTON3 = 108;
	public static final int BUTTON1_DOWN_MASK = 200;
	public static final int BUTTON2_DOWN_MASK = 201;
	public static final int BUTTON3_DOWN_MASK = 202;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getButton() {
		return button;
	}

	public int getModifiersEx() {
		// TODO Auto-generated method stub
		return 0;
	}

}
