package com.acehostingllc.deckerandroid.decker.decker.view;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.FontMetricsInt;
import android.renderscript.Font;

import com.acehostingllc.deckerandroid.decker.decker.model.*;

final class UITextField extends DisplayedComponent
{
	private String text;
	private int color;
	private Paint font;
	private FontMetricsInt font_metrics;
	private int char_limit;
	private Object cursor;



	UITextField (final Value _component, final DisplayedComponent _parent, final DisplayedComponent current_clip_source) {
		super(_component, _parent);
		updateText();
		update(0, current_clip_source);
		child_count = 0; // cannot have children
		// register it as a hard coded key listener
		hasHardcodedEventFunction[ON_KEY_DOWN] = true;
		if (!eventFunctionRegistered[ON_KEY_DOWN])
			addEventListener(ON_KEY_DOWN);
	}




	protected void draw (final AndroidGraphics g) {
		System.out.println("draw called on UITextField");
		Value v = null, w;
		// if the color is explicitly defined, use it, otherwise try to use the default color from TEXTFIELD_STYLE
		if (color != 0) {
			g.setColor(color);
		}
		else {
			v = ScriptNode.getVariable("TEXTFIELD_STYLE");
			if (v != null && v.type() == Value.STRUCTURE && (w=v.get("color")) != null) {
				int c = AbstractView.getColor(v.toString());
				if (c != 0) {
					g.setColor(c);
				}
			}
		}
		// if the font is explicitly defined, use it, otherwise try to use the default front from TEXTFIELD_STYLE
		if (font != null) {
			g.setFont(font);
		}
		else {
			// fetch TEXTFIELD_STYLE if we haven't done so before
			if (v == null) {
				v = ScriptNode.getVariable("TEXTFIELD_STYLE");
			}
			if (v != null && v.type() == Value.STRUCTURE && (w=v.get("font")) != null) {
				Paint f = AbstractView.getFont(w.toString(), null, false);
				if (f != null) {
					g.setFont(f);
				}
			}
		}
		font_metrics = g.getFont().getFontMetricsInt();
		final int font_ascent = font_metrics.ascent;
		g.drawString(text, x, y+font_ascent);
		if (cursor instanceof DisplayedComponent) {
			((DisplayedComponent)cursor).x = (int) (x+font.measureText(text));
			((DisplayedComponent)cursor).draw(g);
		}
		else {
			g.drawString((String)cursor, (int) (x+font.measureText(text)), y+font_ascent);
		}
	}




	boolean eventUserInput (final int event_id, final Object e, final int mouse_x, final int mouse_y, final int mouse_dx, final int mouse_dy) {
		/*
		if (event_id == ON_KEY_DOWN) {
			final KeyEvent k = (KeyEvent) e;
			if (k.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				if (text.length() > 0) {
					text = text.substring(0, text.length()-1);
					component.get("text").set(text);
				}
			}
			else if (k.getKeyChar() != KeyEvent.CHAR_UNDEFINED && text.length() < char_limit) {
				// also make sure that the text fits into the field
				Value v = component.get("width");
				if ( v.type() == Value.INTEGER && v.integer() < font_metrics.stringWidth(text+k.getKeyChar()) + ((cursor instanceof String)?font_metrics.stringWidth((String)cursor):((DisplayedComponent)cursor).w) ) {
					return true;
				}
				text = text + k.getKeyChar();
				component.get("text").set(text);
			}
		}
		*/
		return true;
	}




	public void eventValueChanged (final String variable_name, final Structure container, final Value old_value, final Value new_value) {
		if (variable_name.equals("text") && container.get("text").equals(text))
			return;
		super.eventValueChanged(variable_name, container, old_value, new_value);
	}




	protected void update (final int customSettings, final DisplayedComponent current_clip_source) {
		super.update(customSettings, current_clip_source);
		updateText();
		updateCursor(current_clip_source);
	}




	private void updateCursor (final DisplayedComponent current_clip_source) {
		final Value v = component.get("cursor");
		if (v.type() == Value.STRUCTURE) {
			if (!(cursor instanceof DisplayedComponent) || !((DisplayedComponent)cursor).component.equals(v)) {
				cursor = createDisplayedComponent(v, this, current_clip_source);
			}
		}
		else {
			cursor = v.toString();
		}
	}




	private void updateText () {
		final Structure t = component.structure();
		Value v;
		// fetch the text and its style settings
		text = t.get("text").toString();
		v = t.get("font");
		font = (v.type() == Value.STRING) ? AbstractView.getFont(v.string(), null, false) : null;
		font_metrics = ( (font!=null) ? font : AbstractView.getFont("", null, false) ).getFontMetricsInt();
		color = ((v=t.get("color")).type() == Value.STRING) ? AbstractView.getColor(v.string()) : null;
		char_limit = ((v=t.get("char_limit")).type() == Value.INTEGER) ? v.integer() : Integer.MAX_VALUE;
	}
}