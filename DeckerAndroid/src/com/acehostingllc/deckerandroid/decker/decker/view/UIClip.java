package com.acehostingllc.deckerandroid.decker.decker.view;

import android.graphics.drawable.shapes.Shape;

import com.acehostingllc.deckerandroid.decker.decker.model.ArrayWrapper;
import com.acehostingllc.deckerandroid.decker.decker.model.Structure;
import com.acehostingllc.deckerandroid.decker.decker.model.Value;

public class UIClip extends DisplayedComponent
{
	UIClip (final Value _component, final DisplayedComponent _parent, final DisplayedComponent current_clip_source) {
		super(_component, _parent);
		if (_component != null)
			update(0, current_clip_source);
		//if (_component != null && _component.type()  == Value.STRUCTURE)
		//	_component.structure().addValueListener(this);
	}




	protected void draw () {
		//final Shape old_clip = g.getClip();
		//g.setClip(x, y, w, h);
		//super.draw(g);
		//g.setClip(old_clip);
	}




	public void eventValueChanged (final String variable_name, final Structure container, final Value old_value, final Value new_value) {
		update(0, getCurrentClipSource());
	}




	public void eventValueChanged (final int index, final ArrayWrapper wrapper, final Value old_value, final Value new_value) {
		update(0, getCurrentClipSource());
	}




	protected DisplayedComponent getCurrentClipSource () {
		return this;
	}




	protected void update (final int customSettings, final DisplayedComponent current_clip_source) {
		if (component == null || component.type() != Value.STRUCTURE || !component.get("structure_type").equals("CLIP"))
			return;
		super.update(0, super.getCurrentClipSource());
		updateChildren(this);
	}
}