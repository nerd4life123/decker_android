package com.acehostingllc.deckerandroid.decker.decker.view;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.renderscript.Font;
import android.view.View;
import android.widget.TextView;

import com.acehostingllc.deckerandroid.DeckerActivity;
import com.acehostingllc.deckerandroid.decker.decker.model.*;

/** displays a TEXTBLOCK structure */
public final class UITextBlock extends DisplayedComponent
{
	UITextBlock(Value _component) {
		super(_component);
		// TODO Auto-generated constructor stub
	}




	private final static int X = 0, Y = 1, ROW_HEIGHT = 2, ELEMENTS = 3, CHILDREN = 4, LAST_FONT_HEIGHT = 5;
	private final static int aLEFT = 0, aCENTER = 1, aRIGHT = 2, aBLOCK = 3;
	private int alignment;
	private boolean has_explicit_height;
	private Value next_block;
	private Value previous_block;



	/*
	UITextBlock (final Value _component, final View _parent, final DisplayedComponent current_clip_source) {
		//super(context);
		//super(_component, _parent, current_clip_source);
		//component.structure().addValueListener(this);
	}*/
	

	/** data[X] is the current x position for appending children, data[Y] is the current y position for that, data[ROW_HEIGHT] is the height of the current row, data[ELEMENTS] is the number  of components on the current row so far */
	
	
	//@TargetApi(11)
	private void addChild (final Value c, final int[] data, final DisplayedComponent[][] current_row, final DisplayedComponent[][] children, final DisplayedComponent current_clip_source) {
		// add the component c
		if (c.type() == Value.STRUCTURE && !c.get("structure_type").equals("TEXT")) {
			DisplayedComponent k = createDisplayedComponent(c, this, current_clip_source);
			if (k != null) {
				// add k to the list of children
				if (children[0].length == data[CHILDREN]) {
					final DisplayedComponent[] ch = new DisplayedComponent[children[0].length*2];
					System.arraycopy(children[0], 0, ch, 0, children[0].length);
					children[0] = ch;
				}
				children[0][data[CHILDREN]] = k;
				data[CHILDREN]++;
				// move k to the next line if the current one already contains something and there is not enough room left for k
				if (data[X] > 0 && k.w > this.getWidth()-data[X]) {
					finishUpRow(data, current_row);
					k.x = (int) this.getX();
					data[X] = k.w;
					data[ROW_HEIGHT] = k.h;
					current_row[0][0] = k;
					data[ELEMENTS] = 1;
				}
				else {
					// otherwise add it to the current row
					k.x = (int) this.getX() + data[X];
					data[X] += k.w;
					if (k.h > data[ROW_HEIGHT])
						data[ROW_HEIGHT] = k.h;
					if (current_row[0].length == data[ELEMENTS]) {
						final DisplayedComponent[] cr = new DisplayedComponent[current_row[0].length*2];
						System.arraycopy(current_row[0], 0, cr, 0, current_row[0].length);
						current_row[0] = cr;
					}
					current_row[0][data[ELEMENTS]] = k;
					data[ELEMENTS]++;
				}
			}
		}
		else if (c.equalsConstant("NEWLINE")) {
			if (data[ELEMENTS] == 0)
				data[ROW_HEIGHT] = data[LAST_FONT_HEIGHT];
			finishUpRow(data, current_row);
		}
		else {
			// display whatever we've come across as either a TEXT or use toString()
			// determine the text, font and color
			String text = null;
			Paint font = null;
			int color = 0;
			Value v;
			if (c.type() == Value.STRUCTURE && c.get("structure_type").equals("TEXT")) {
				if ((v=c.get("text")).equalsConstant("UNDEFINED"))
					return; // don't add the text if the displayed string is not defined
				text = v.toString();
				if (!(v=c.get("font")).equalsConstant("UNDEFINED"))
					font = AbstractView.getFont(v.toString());
				if (!(v=c.get("color")).equalsConstant("UNDEFINED"))
					color = AbstractView.getColor(v.toString());
			}
			else if (c.equalsConstant("UNDEFINED"))
				return;
			else
				text = c.toString();
			// if we don't have a color or font yet, use the default values
			if (font == null || color == -1) {
				v = ScriptNode.getVariable("TEXT_STYLE");
				if (font == null)
					font = AbstractView.getFont(v.get("font").toString());
				if (color == -1)
					color = AbstractView.getColor(v.get("color").toString());
			}
			// split the text into chunks which fit into a line
			//final FontMetrics fm = AbstractView.getFontMetrics(font);
			final int fm_height = 10;//fm.getHeight();
			data[LAST_FONT_HEIGHT] = fm_height;
			final int text_length = text.length();
			int start = 0;
			int a, b, alength, blength;
			while (start < text_length) {
				// find the next chunk
				final int limit = this.getWidth()-data[X];
				b = text_length;
				blength = 50; //fm.stringWidth(text.substring(start,b));
				if (blength <= limit) {
					a = b;
					alength = blength;
				}
				else {
					a = start+1;
					alength = 50;//fm.stringWidth(text.substring(start,a));
					while (b > a+1) {
						final int split = (a+b) / 2;
						final int chunk_length = 50;//fm.stringWidth(text.substring(start,split));
						if (chunk_length <= limit) {
							a = split;
							alength = chunk_length;
						}
						else {
							b = split;
							blength = chunk_length;
						}
					}
				}

				// make sure we aren't splitting up a word
				if (a < text_length) {
					int u = a;
					while (u > start && text.charAt(u-1) != ' ')
						u--;
					while (u > start && text.charAt(u-1) == ' ')
						u--;
					if (u > start)
						a = u;
				}
				// if we can't fit a word onto the current row and there is already something on the row, move on to the next row
				if (alength > limit && data[X] > 0) {
					finishUpRow(data, current_row);
					data[ROW_HEIGHT] = fm_height;
					continue;
				}
				// add the chunk to the row
				//final UITextChunk k = new UITextChunk(text.substring(start,a), font, color, this.getX()+data[X], alength, fm_height, c, this, current_clip_source);
				//children[0][data[CHILDREN]] = k;
				data[CHILDREN]++;
				//current_row[0][data[ELEMENTS]] = k;
				data[ELEMENTS]++;
				data[X] += alength;
				if (data[ROW_HEIGHT] < fm_height)
					data[ROW_HEIGHT] = fm_height;
				// if (we still have text left, continue on the next row
				if (a < text_length) {
					finishUpRow(data, current_row);
					data[ROW_HEIGHT] = fm_height;
					// remove whitespace from the beginning of the next line
					while (a < text_length && text.charAt(a) == ' ')
						a++;
				}
				start = a;
			}
		}
	}


/*
	private DisplayedComponent createDisplayedComponent(Value c, UITextBlock uiTextBlock,
			DisplayedComponent current_clip_source) {
		// TODO Auto-generated method stub
		return null;
	}*/
	

	


	private void finishUpRow (final int[] data, final DisplayedComponent[][] current_row) {
		// center all the row elements in the row
		Value v;
		for (int i = data[ELEMENTS]; --i >= 0; ) {
			final DisplayedComponent k = current_row[0][i];
			// if the component has a valid y, use that value
			if (k.h < data[ROW_HEIGHT] && k.component != null && k.component.type() == Value.STRUCTURE && (v=k.component.get("y")) != null &&( v.type() == Value.INTEGER || v.equalsConstant("TOP") || v.equalsConstant("BOTTOM") )) {
				if (v.equalsConstant("TOP"))
					k.y = (int) (this.getY() + data[Y]);
				else if (v.equalsConstant("BOTTOM"))
					k.y = (int) (this.getY() + data[Y] + data[ROW_HEIGHT] - k.h);
				else
					k.y = (int) (this.getY() + data[Y] + v.integer());
			}
			// otherwise center it vertically
			else {
				k.y = (int) (this.getY() + data[Y] + (data[ROW_HEIGHT] - k.h) / 2);
			}
		}
		data[X] = 0;
		data[Y] += data[ROW_HEIGHT];
		data[ELEMENTS] = 0;
		data[ROW_HEIGHT] = 0;
	}


	

	protected void update (final int customSettings, final DisplayedComponent current_clip_source) {
		//super.update(customSettings|10, current_clip_source);
		//updateChildren(current_clip_source);
	}



	/*
	void updateChildren (final DisplayedComponent current_clip_source) {
		// destroy the old children
		for (int i = child_count; --i >= 0; ) {
			child[i].destroy();
		}
		// remove all children from this component
		child_count = 0;
		children_relative_to_width = 1; // every time the width changes, updateChildren() needs to rebuild the text block content
		children_relative_to_height = 0;
		// add the new children
		Value v = component.get("component");
		final int default_font_height = AbstractView.getFontMetrics(AbstractView.getFont(ScriptNode.getVariable("TEXT_STYLE").get("font").toString())).getHeight();
		int[] row_data = { 0, 0, 0, 0, 0, default_font_height };
		DisplayedComponent[][] row_elements = new DisplayedComponent[1][50];
		DisplayedComponent[][] children = new DisplayedComponent[1][50];
		if (!v.equalsConstant("UNDEFINED")) {
			if (v.type() != Value.ARRAY)
				addChild(v, row_data, row_elements, children, current_clip_source);
			else {
				final Value[] a = v.array();
				for (int i = 0; i < a.length; i++) {
					if (!a[i].equalsConstant("UNDEFINED")) {
						addChild(a[i], row_data, row_elements, children, current_clip_source);
					}
				}
			}
		}
		// finish up the final row
		finishUpRow(row_data, row_elements);
		// register the children with the display system
		child = children[0];
		child_count = row_data[CHILDREN];
	}*/
	
}