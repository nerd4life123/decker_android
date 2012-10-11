package com.acehostingllc.deckerandroid.decker.decker.view;

import java.io.*;

import android.graphics.Color;
import android.renderscript.Font;
import android.view.View;
import android.widget.ImageView;

import com.acehostingllc.deckerandroid.decker.decker.util.StringTreeMap;

import com.acehostingllc.deckerandroid.decker.decker.model.*;
import com.acehostingllc.deckerandroid.decker.decker.util.*;


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
	//private static Font last_font = new Font(); // this is the last font that has been displayed. it will be used to fill in any missing data when fetching a new font
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
		
		return 0;
	}


	public final static int width (Object visible_object, final int parent_width) {
		
		return 0;
	}
}