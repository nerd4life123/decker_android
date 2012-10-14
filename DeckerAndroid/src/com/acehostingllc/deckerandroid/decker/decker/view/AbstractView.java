package com.acehostingllc.deckerandroid.decker.decker.view;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.renderscript.Font;
import android.renderscript.Font.Style;
import android.view.View;
import android.widget.ImageView;

import com.acehostingllc.deckerandroid.decker.decker.util.StringTreeMap;

import com.acehostingllc.deckerandroid.decker.decker.model.Global;
import com.acehostingllc.deckerandroid.decker.decker.model.Value;


/** override drawContent() and the event messages relevant to your View class */
public class AbstractView
{
//*******************************************************************************************************************************************************
// static view related data and methods *****************************************************************************************************************
//*******************************************************************************************************************************************************


public final static int CENTER = Integer.MIN_VALUE;
public final static int NONE = Integer.MIN_VALUE+5;
public final static int ABSOLUTE_MIN_VALUE = Integer.MIN_VALUE+6; // coordinate values >= ABSOLUTE_MIN_VALUE will be treated as absolute values
	public final static String SUPPORTED_IMAGE_TYPES = " .png .gif .bmp .jpg .jpeg ";


	private final static StringTreeMap COLORS = new StringTreeMap(true);
	private final static StringTreeMap IMAGES = new StringTreeMap(true);
	private final static StringTreeMap FONTS = new StringTreeMap();
	private final static StringTreeMap FONTMETRICS = new StringTreeMap();
	private static String last_font = null; // this is the last font that has been displayed. it will be used to fill in any missing data when fetching a new font
	private static int last_color = Color.WHITE;

	public final static ImageView getImage (final String name) {
		return getImage(name, false, 0xffff00ff);
	}


	public final static ImageView getImage (final String name, final boolean buffered_image) {
		return getImage(name, buffered_image, 0xffff00ff);
	}


	public final static ImageView getImage (final String name, final boolean buffered_image, final int transparent_color) {
		if (name == null)
			return null;
		//redo entire method for android
		return null;
	}


	public final static ImageView getTurnedImage (final String name, int angle) {
		//redo for android
		return null;
	}


	/** reloads the list of all available images and sounds */
	public static void reloadArtwork (final boolean prefetchImages)  {
		//redo for android
	}

//*******************************************************************************************************************************************************
//*******************************************************************************************************************************************************
//*******************************************************************************************************************************************************


	public void displayTickerMessage (final String message)  {}


	public void drawContent(final View g)  {}


	public void eventKeyPressed (final char c, final int code, final boolean isAltDown)  {}
	public void eventKeyReleased (final char c, final int code, final boolean isAltDown)  {}
	public void eventMouseDragged (final int x, final int y, final int dx, final int dy)  {}
	public void eventMouseMoved (final int x, final int y)  {}
	public void eventMousePressed (final int x, final int y)  {}
	public void eventMouseReleased (final int x, final int y)  {}


	public final static int height (Object visible_object, final int parent_height) {
		Rect bounds = new Rect();
		Paint paint = new Paint();
		Value value = (Value) visible_object;
		
		int height = 0;
		
		switch (value.type())
		{
		case Value.STRING:
			String string = value.string();
			paint.getTextBounds(string, 0, string.length(), bounds);
			height = bounds.height();
			break;
		}
		
		return height;
	}


	public final static int width (Object visible_object, final int parent_width) {
		Rect bounds = new Rect();
		Paint paint = new Paint();
		Value value = (Value) visible_object;
		
		int width = 0;
		
		switch (value.type())
		{
		case Value.STRING:
			String string = value.string();
			paint.getTextBounds(string, 0, string.length(), bounds);
			width = bounds.width();
			break;
		}
		
		return width;
	}
	

	public final static int getColor (String color) {
		// remove the alpha if it is 100%
		if (color.length() == 9 && color.substring(1,3).equalsIgnoreCase("ff"))
			color = color.substring(0,1) + color.substring(3);
		int ret = Integer.parseInt(COLORS.get(color).toString());
		if (ret == -1) {
			try {
				if (color.charAt(0) == '#') {
					if (color.length() == 7) {
						final int r = Integer.parseInt(color.substring(1,3),16);
						final int g = Integer.parseInt(color.substring(3,5),16);
						final int b = Integer.parseInt(color.substring(5,7),16);
						COLORS.put(color, Color.rgb(r,  g,  b));
					}
					else if (color.length() == 9) {
						final int a = Integer.parseInt(color.substring(1,3),16);
						final int r = Integer.parseInt(color.substring(3,5),16);
						final int g = Integer.parseInt(color.substring(5,7),16);
						final int b = Integer.parseInt(color.substring(7,9),16);
						COLORS.put(color, Color.argb(a,  r,  g,  b));
					}
				}
			} catch (Throwable t) {}
		}
		return ret;
	}


	public final static String getFont (final String description) {
		return getFont(description, last_font, false);
	}


	public final static String getFont (final String description, final boolean new_default_font) {
		return getFont(description, last_font, new_default_font);
	}


	/** parses the description, fills in missing data using the base_font and returns the new Font
	*   if base_font is null, last_font is used instead */
	public final static String getFont (final String description, String base_font) {
		return getFont(description, base_font, false);
	}


	/** parses the description, fills in missing data using the base_font and returns the new Font
	*   if base_font is null, last_font is used instead */
	public final static String getFont (final String description, String base_font, final boolean new_default_font) {
		if (base_font == null)
			base_font = last_font;
		String face = "Arial";
		//Style style = null;
		int size = 12;
		final String s = description.trim();
		String s2, s3;
		int start = 0, end;
		while (start < s.length()) {
			end = s.indexOf(';', start);
			if (end == -1)
				end = s.length();
			s2 = s.substring(start, end).trim();
			s3 = s2.toLowerCase();
			/*if (s3.equals("plain"))
				style = Font.Style.NORMAL;
			else if (s3.equals("bold"))
				style = (style==Font.Style.ITALIC) ? (Font.Style.ITALIC|Font.Style.BOLD) : Font.Style.BOLD;
			else if (s3.equals("italic"))
				style = (style==Font.Style.BOLD) ? (Font.Style.ITALIC|Font.Style.BOLD) : Font.Style.ITALIC;
			else if (s2.endsWith("pt") && Global.isInteger(s2.substring(0,s2.length()-2).trim()))
				size = Integer.parseInt(s2.substring(0,s2.length()-2).trim());
			else if (Global.isInteger(s2.trim()))
				size = Integer.parseInt(s2.trim());
			else if (s2.length() > 0)
				face = s2;*/
			start = end + 1;
		}
		return getFont(face, null, size, new_default_font);
	}


	public final static String getFont (final String face, final String style, final int size, final boolean new_default_font) {
		String f = FONTS.get(face+";"+style+";"+size).toString();
		if (f == null) {
			f = "Arial;10pt;plain";
			FONTS.put(face+";"+style+";"+size, f);
		}
		if (new_default_font)
			last_font = f;
		return f;
	}
}