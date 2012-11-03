package com.acehostingllc.deckerandroid.decker.decker.input;

public class KeyEvent extends InputEvent {
	char keyChar;
	int keyCode;
	int modifiers;
	
	public KeyEvent(int id, char keyChar, int keyCode, int modifiers)
	{
		super(id);
		this.keyChar = keyChar;
		this.keyCode = keyCode;
		this.modifiers = modifiers;
	}

	public static final int KEY_PRESSED = 0;
	public static final int KEY_RELEASED = 1;
	public static final char CHAR_UNDEFINED = 2;
	public static final int VK_BACK_SPACE = 3;
	public static final int VK_ESCAPE = 4;
	public static final int VK_LEFT = 5;
	public static final int VK_F1 = 6;
	public static final int VK_RIGHT = 07;
	public static final int VK_UP = 8;
	public static final int VK_DOWN = 9;
	public static final int VK_F2 = 10;
	public static final int VK_F3 = 11;
	public static final int VK_F4 = 12;
	public static final int VK_F5 = 13;
	public static final int VK_F6 = 14;
	public static final int VK_F7 = 15;
	public static final int VK_F8 = 16;
	public static final int VK_F9 = 17;
	public static final int VK_F10 = 18;
	public static final int VK_F11 = 19;
	public static final int VK_F12 = 20;
	public char getKeyChar() {
		return this.keyChar;
	}
	public int getKeyCode() {
		return this.keyCode;
	}
	public int getModifiers() {
		return this.modifiers;
	}

}
