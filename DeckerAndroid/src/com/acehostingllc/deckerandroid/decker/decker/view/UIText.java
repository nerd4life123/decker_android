package com.acehostingllc.deckerandroid.decker.decker.view;
import com.acehostingllc.deckerandroid.DeckerActivity;
import com.acehostingllc.deckerandroid.decker.decker.model.*;



/** displays a TEXT structure */
public final class UIText extends DisplayedComponent
{
	private String text;
	private int color;
	private String font;
	private int y_offset;

	UIText (DeckerActivity activity, final Value _component, final DisplayedComponent _parent, final DisplayedComponent current_clip_source) {
		super(activity, _component, _parent);
		//component.structure().addValueListener(this);
		updateText();
		super.update(CUSTOM_SIZE, current_clip_source);
		child_count = 0; // cannot have children
	}

	void determineSize (final boolean width_already_determined, final boolean height_already_determined, final DisplayedComponent current_clip_source) {
		/*final FontMetrics fm = AbstractView.getFontMetrics(font);
		if (!width_already_determined)
			w = fm.stringWidth(text);
		if (!height_already_determined)
			h = fm.getHeight();*/
	}

	protected void draw () {
		//if (color != -1)
			//g.setColor(color);
		//g.setFont(font);
		//g.drawString(text, x, y+y_offset);
	}

	public void eventValueChanged (DeckerActivity activity, final int index, final ArrayWrapper wrapper, final Value old_value, final Value new_value) {
		updateText();
		super.eventValueChanged(activity, index, wrapper, old_value, new_value);
	}




	public void eventValueChanged (DeckerActivity activity, final String variable_name, final Structure container, final Value old_value, final Value new_value) {
		updateText();
		super.eventValueChanged(activity, variable_name, container, old_value, new_value);
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
		color = ((v=t.get("color")).type() == Value.STRING) ? AbstractView.getColor(v.string()) : null;
		//y_offset = AbstractView.getFontMetrics(font).getAscent();
	}
}