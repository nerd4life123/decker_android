package com.acehostingllc.deckerandroid.decker.decker.view;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.provider.MediaStore.Files;
import android.view.View;
import android.widget.ImageView;

import com.acehostingllc.deckerandroid.DeckerActivity;
import com.acehostingllc.deckerandroid.decker.decker.util.BMPReader;
import com.acehostingllc.deckerandroid.decker.decker.util.StringTreeMap;

import com.acehostingllc.deckerandroid.decker.decker.model.FunctionCall;
import com.acehostingllc.deckerandroid.decker.decker.model.Global;
import com.acehostingllc.deckerandroid.decker.decker.model.ScriptNode;
import com.acehostingllc.deckerandroid.decker.decker.model.Structure;
import com.acehostingllc.deckerandroid.decker.decker.model.Value;

import com.acehostingllc.deckerandroid.decker.decker.view.AbstractView;


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
	private static Paint last_font = null; // this is the last font that has been displayed. it will be used to fill in any missing data when fetching a new font
	private static int last_color = Color.WHITE;

	public final static Bitmap getImage (final String name) {
		return getImage(name, false, 0xffff00ff);
	}


	public final static Bitmap getImage (final String name, final boolean buffered_image) {
		return getImage(name, buffered_image, 0xffff00ff);
	}


	public final static Bitmap getImage (final String name, final boolean buffered_image, final int transparent_color) {
		if (name == null)
			return null;
		Object o = IMAGES.get(name);
		if (name.equals("UNDEFINED"))
			return null;
		// there is no image of that name. if the name has no suffix, try to fetch one with a suffix
		if (o == null) {
			int suffix = name.lastIndexOf('.');
			if (suffix == -1 ||( suffix != name.length()-4 && !name.toLowerCase().endsWith(".jpeg") )) {
				final String[] type = { ".png", ".gif", ".bmp", ".jpg", ".jpeg" };
				for (int i = 0; i < type.length; i++) {
					if ((o=IMAGES.get(name+type[i])) != null) {
						IMAGES.put(name, o);
						break;
					}
				}
			}
		}
		// if we haven't done so before, fetch the image now
		if (o instanceof File) {
			final String path = ((File)o).getPath();
			final View c = Global.getDisplayedComponent();
			if (path.toLowerCase().endsWith(".bmp")) {
				try {
					InputStream stream = DeckerActivity.getAppContext().getAssets().open(path);
					o = BMPReader.readBMP(stream, c, transparent_color);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				// TODO: Handle non-bitmap
			/*	final MediaTracker mt = new MediaTracker(c);
				mt.addImage((Image)(o = c.getToolkit().getImage(AbstractView.class.getClassLoader().getResource( path ))), 0);
				try {
					mt.waitForAll();
				} catch (InterruptedException e) {
					System.err.println("interrupted while loading image "+path);
					System.exit(1);
				}*/
			}
			IMAGES.put(name, o);
		}
		if (o instanceof Bitmap) {
			final Bitmap image = (Bitmap) o;
			if (!buffered_image)// || image instanceof BufferedImage)
				return image;
			IMAGES.put(name, image);
			return image;
		}
		return null;
	}


	public final static Bitmap getTurnedImage (Bitmap bitmap, int angle) {
		//bitmap.
		return null;
	}


	/** reloads the list of all available images and sounds */


	/** reloads the list of all available images and sounds */
	public static void reloadArtwork ()  {
		IMAGES.clear();
		try {
			final View c = Global.getDisplayedComponent();
			//final MediaTracker mt = ( prefetchImages ? new MediaTracker(c) : null );
			//DeckerActivity.getAppContext().getAssets().openFd("asdf").
			String artworkDir = "rulesets"+File.separator+Global.getCurrentRuleset().getName()+File.separator+"artwork";
			System.out.println("Reading artwork from " + artworkDir);
			
			reloadArtwork(artworkDir, "");
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
		if (Global.debug_level > 0)
		System.out.println("finished reloading artwork");
	}

	private static void reloadArtwork (final String currentDir, String path) throws IOException  {
		AssetManager mgr = DeckerActivity.getAppContext().getAssets();
		// do not look for images in directories generated by the subversion system
		if (currentDir.toLowerCase().endsWith(".svn"))
			return;
		if (Global.debug_level > 0)
			System.out.println("loading artwork from "+(currentDir.length()>0?currentDir:"."));
		final View c = Global.getDisplayedComponent();
		
		final String[] files = mgr.list(currentDir);
		final String path_prefix = (path.length()==0) ? "/" : (path + "/");
		for (int i = 0; i < files.length; i++) {
			String subFilePath = currentDir + path_prefix + files[i];
			if (mgr.list(subFilePath).length > 0)
			{
				reloadArtwork(subFilePath, "");
			}
			else {
				// determine the file type
				final int pos = files[i].lastIndexOf('.');
				if (pos > -1) {
					final String file_type = files[i].substring(pos).toLowerCase();
					// check whether it is an image
					if (SUPPORTED_IMAGE_TYPES.indexOf(" "+file_type+" ") > -1) {
						IMAGES.put(path_prefix+files[i], BMPReader.readBMP(mgr.open(subFilePath), c, 0xffff00ff));
					}
				}
			}
		}
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
	
	public final static Paint createFontPaint(final String face, final int style, final int size) {
		Paint paint = new Paint();
		paint.setTypeface(Typeface.create(face,  style));
		paint.setTextSize(size);
		return paint;
	}
	
	public final static Paint getFontPaint (final String face, final int style, final int size, Object g) {
		Paint fm = (Paint) FONTS.get(face+";"+style+";"+size);
		if (fm == null) {
			fm = createFontPaint(face, style, size);
			FONTS.put(face+";"+style+";"+size, fm);
		}
		return fm;
	}

	public final static int height (Object visible_object, final int parent_height) {
		Value v;
		if (visible_object instanceof Value) {
			v = (Value) visible_object;
			if (v.type() == Value.STRUCTURE)
				visible_object = v.structure();
			else if (!v.equalsConstant("UNDEFINED"))
				visible_object = v.toString();
		}
		// is it a string with the name of an image?
		if (visible_object instanceof String) {
			final Bitmap image = getImage((String)visible_object);
			if (image != null)
				return image.getHeight();
		}
		// is it a structure?
		if (visible_object instanceof Structure) {
			final Structure d = (Structure) visible_object;
			final String type = d.get("structure_type").string();
			// check whether the height is explicitly defined
			v = d.get("height");
			if (v != null) {
				if (v.type() == Value.INTEGER)
					return v.integer();
				// check whether it's a percentage value
				if (v.type() == Value.STRING) {
					final String s = v.string();
					if (s.endsWith("%")) {
						try {
							return (Integer.parseInt(s.substring(0, s.length()-1)) * parent_height + 50)/100;
						} catch (NumberFormatException ex) {}
					}
				}
			}
			// if this is a BUTTON, use the definition for the idle state instead
/*			if (type.equals("BUTTON") && !d.get("idle").equalsConstant("UNDEFINED"))
				return height(d.get("idle"));
			if (type.equals("BORDER_BUTTON") && !d.get("idle").equalsConstant("UNDEFINED"))
				return height(d.get("idle")) + 2*ScriptNode.getValue("DEFAULT_BORDER_THICKNESS").integer();
*/			// if this is a STRING, determine its height
			if (type.equals("TEXT")) {
				if ((v=d.get("font")) != null && v.type() == Value.STRING)
					return getFont(v.string()).getFontMetricsInt().ascent;
				else
					return getFont("").getFontMetricsInt().ascent; // use the last font that was used to draw a string
			}
			else if (type.equals("IMAGE")) {
				Bitmap image;
				if ((v=d.get("image")) != null && (image=getImage(v.toString())) != null)
					return image.getHeight();
			}
			// if its structure type has a special pixelheight function, call it
			final Value t = ScriptNode.getStructureType(type);
			if (t != null) {
				if ((v=t.get("pixelheight")) != null && v.type() == Value.FUNCTION) {
					v = FunctionCall.executeFunctionCall(v, null, new Structure[]{ (Structure) visible_object } );
					if (v.type() == Value.INTEGER)
						return v.integer();
				}
			}
			// if not, use the height of the first sub-component
/*			if ((v=d.get("component")) != null) {
				if (v.type() == Value.ARRAY && v.array().length > 0)
					return height(v.get(0));
				else
					return height(v);
			}
*/		}
		// everything has failed, assume a height of 0 for the structure
		return 0;
	}
	
	public final static int width (Object visible_object, final int parent_width) {
		Value v;
		if (visible_object instanceof Value) {
			v = (Value) visible_object;
			if (v.type() == Value.STRUCTURE)
				visible_object = v.structure();
			else if (!v.equalsConstant("UNDEFINED"))
				visible_object = v.toString();
		}
		// is it a string with the name of an image?
		if (visible_object instanceof String) {
			final Bitmap image = getImage((String)visible_object);
			if (image != null)
				return image.getWidth();
		}
		// is it a structure?
		if (visible_object instanceof Structure) {
			final Structure d = (Structure) visible_object;
			final String type = d.get("structure_type").string();
			// check whether the width is explicitly defined
			v = d.get("width");
			if (v != null) {
				if (v.type() == Value.INTEGER)
					return v.integer();
				// check whether it's a percentage value
				if (v.type() == Value.STRING) {
					final String s = v.string();
					if (s.endsWith("%")) {
						try {
							return (Integer.parseInt(s.substring(0, s.length()-1)) * parent_width + 50)/100;
						} catch (NumberFormatException ex) {}
					}
				}
			}
			// if this is a BUTTON, use the definition for the idle state instead
/*			if (type.equals("BUTTON") && !d.get("idle").equalsConstant("UNDEFINED"))
				return width(d.get("idle"));
			if (type.equals("BORDER_BUTTON") && !d.get("idle").equalsConstant("UNDEFINED"))
				return width(d.get("idle")) + 2*ScriptNode.getValue("DEFAULT_BORDER_THICKNESS").integer();
*/			// if this is a STRING, calculate its width
			if (type.equals("TEXT")) {
				Paint fm;
				if ((v=d.get("font")) != null && v.type() == Value.STRING)
					fm = getFont(v.string());
				else
					fm = getFont(""); // use the last font that was used to draw a string
				return (int) fm.measureText((d.get("text").toString()));
			}
			else if (type.equals("IMAGE")) {
				Bitmap image;
				if ((v=d.get("image")) != null && (image=getImage(v.toString())) != null)
					return image.getWidth();
			}
			// if its structure type has a special pixelwidth function, call it
			final Value t = ScriptNode.getStructureType(type);
			if (t != null) {
				if ((v=t.get("pixelwidth")) != null && v.type() == Value.FUNCTION) {
					v = FunctionCall.executeFunctionCall(v, null, new Structure[]{ (Structure) visible_object });
					if (v.type() == Value.INTEGER)
						return v.integer();
				}
			}
			// if not, use the width of the first sub-component
/*			if ((v=d.get("component")) != null) {
				if (v.type() == Value.ARRAY && v.array().length > 0)
					return width(v.get(0));
				return width(v);
			}
*/		}
		// everything has failed, assume a width of 0 for the structure
		return 0;
	}
	

	public final static int getColor (String color) {
		return Color.BLUE;
		/*
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
		*/
	}


	public final static Paint getFont (final String description) {
		return getFont(description, last_font, false);
	}


	public final static Paint getFont (final String description, final boolean new_default_font) {
		return getFont(description, last_font, new_default_font);
	}


	/** parses the description, fills in missing data using the base_font and returns the new Font
	*   if base_font is null, last_font is used instead */
	public final static Paint getFont (final String description, Paint base_font) {
		return getFont(description, base_font, false);
	}


	/** parses the description, fills in missing data using the base_font and returns the new Font
	*   if base_font is null, last_font is used instead */
	public final static Paint getFont (final String description, Paint base_font, final boolean new_default_font) {
		if (base_font == null)
			base_font = last_font;
		String face = "Arial";
		int size = 12;
		int style = 0;
		final String s = description.trim();
		String s2, s3;
		int start = 0, end;
		while (start < s.length()) {
			end = s.indexOf(';', start);
			if (end == -1)
				end = s.length();
			s2 = s.substring(start, end).trim();
			s3 = s2.toLowerCase();
			if (s3.equals("plain"))
				style = Typeface.NORMAL;
			else if (s3.equals("bold"))
				style = (style==Typeface.ITALIC) ? (Typeface.BOLD_ITALIC) : Typeface.BOLD;
			else if (s3.equals("italic"))
				style = (style==Typeface.BOLD) ? (Typeface.BOLD_ITALIC) : Typeface.ITALIC;
			else if (s2.endsWith("pt") && Global.isInteger(s2.substring(0,s2.length()-2).trim()))
				size = Integer.parseInt(s2.substring(0,s2.length()-2).trim());
			else if (Global.isInteger(s2.trim()))
				size = Integer.parseInt(s2.trim());
			else if (s2.length() > 0)
				face = s2;
			start = end + 1;
		}
		return getFontPaint(face, style, size, new_default_font);
	}


	public final static Paint getFontPaint (final String face, final int style, final int size, final boolean new_default_font) {
		Paint f = getFontPaint(face, style, size, null);
		if (new_default_font)
			last_font = f;
		return f;
	}
}