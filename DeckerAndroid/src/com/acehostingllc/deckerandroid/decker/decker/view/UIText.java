package com.acehostingllc.deckerandroid.decker.decker.view;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.FontMetricsInt;

import com.acehostingllc.deckerandroid.decker.decker.model.*;


/** displays a TEXT structure */
final class UIText extends DisplayedComponent
{
	private String text;
	private int color = Color.WHITE;
	private Paint font;
	private int y_offset;




	UIText (final Value _component, final DisplayedComponent _parent, final DisplayedComponent current_clip_source) {
		super(_component, _parent);
		component.structure().addValueListener(this);
		updateText();
		super.update(CUSTOM_SIZE, current_clip_source);
		child_count = 0; // cannot have children
	}




	void determineSize (final boolean width_already_determined, final boolean height_already_determined, final DisplayedComponent current_clip_source) {
		final FontMetricsInt fm = font.getFontMetricsInt();
		if (!width_already_determined)
			w = (int) font.measureText(text);
		if (!height_already_determined)
			h = (int) ((int) -1 * (font.ascent() - font.descent()));
	}




	protected void draw (final AndroidGraphics g) {
		//if (color != -1)
			g.setColor(color);
		g.setFont(font);
		g.drawString(text, x, y-y_offset);
		System.out.println("Drawing string from UIText:"+text+" | y offset was "+y_offset+" and y was " + y);
	}




	public void eventValueChanged (final int index, final ArrayWrapper wrapper, final Value old_value, final Value new_value) {
		updateText();
		super.eventValueChanged(index, wrapper, old_value, new_value);
	}




	public void eventValueChanged (final String variable_name, final Structure container, final Value old_value, final Value new_value) {
		updateText();
		super.eventValueChanged(variable_name, container, old_value, new_value);
	}




	protected void update (final int customSettings, final DisplayedComponent current_clip_source) {
		super.update(customSettings|CUSTOM_SIZE, current_clip_source);
		updateText();
	}




	private void updateText () {
		final Structure t = component.structure();
		Value v;
		// fetch the text and its style settings
		text = t.get("text").toString();
		v = t.get("font");
		font = AbstractView.getFont((v.type() == Value.STRING)?v.string():"", null, false);
		color = ((v=t.get("color")).type() == Value.STRING) ? AbstractView.getColor(v.string()) : Color.WHITE;
		y_offset = (int) (font.ascent() - font.descent());
		System.out.println("Ascent was " + font.ascent() + ", descent was " + font.descent());
	}
}