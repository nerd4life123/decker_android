package com.acehostingllc.deckerandroid.decker.decker.view;
import android.graphics.Paint;

import com.acehostingllc.deckerandroid.decker.decker.model.*;



/** displays a TEXT or string within a TEXTBLOCK */
final class UITextChunk extends DisplayedComponent
{
	private String text;
	private int color;
	private Paint font;
	private int y_offset;




	UITextChunk (final String _text, final Paint _font, final int _color, final int _x, final int _w, final int _h, final Value _component, final DisplayedComponent _parent, final DisplayedComponent current_clip_source) {
		super(_component, _parent);
		if (component.type() == Value.STRUCTURE)
			component.structure().addValueListener(this);
		child_count = 0; // cannot have children
		text = _text;
		font = _font;
		color = _color;
		x = _x;
		w =_w;
		h = _h;
		y_offset = (int) _font.ascent();
	}




	protected void draw (final AndroidGraphics g) {
		g.setColor(color);
		g.setFont(font);
		g.drawString(text, x, y-y_offset);
		System.out.println("Drawing string from UITextChunk:"+text);
	}




	public void eventValueChanged (final int index, final ArrayWrapper wrapper, final Value old_value, final Value new_value) {
		parent.eventValueChanged(index, wrapper, old_value, new_value);
	}




	public void eventValueChanged (final String variable_name, final Structure container, final Value old_value, final Value new_value) {
		parent.eventValueChanged(variable_name, container, old_value, new_value);
	}




	protected void update (final int customSettings, final DisplayedComponent current_clip_source) {
	}
}