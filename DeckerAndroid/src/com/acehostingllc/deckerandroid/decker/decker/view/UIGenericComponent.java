package com.acehostingllc.deckerandroid.decker.decker.view;
import com.acehostingllc.deckerandroid.decker.decker.model.*;



class UIGenericComponent extends DisplayedComponent
{
	UIGenericComponent (final Value _component, final DisplayedComponent _parent, final DisplayedComponent current_clip_source) {
		super(_component, _parent, current_clip_source);
	}




	public void eventValueChanged (final int index, final ArrayWrapper wrapper, final Value old_value, final Value new_value) {
		final Value v = (component==null)?null:component.get("component");
		if (v != null && v.equals(wrapper)) {
			super.eventValueChanged(index, wrapper, old_value, new_value);
		}
	}




	public void eventValueChanged (final String variable_name, final Structure container, final Value old_value, final Value new_value) {
		if (variable_name.equals("x") || variable_name.equals("y") || variable_name.equals("width") || variable_name.equals("height")) {
			super.eventValueChanged(variable_name, container, old_value, new_value);
		}
	}




	protected void update (final int customSettings, final DisplayedComponent current_clip_source) {
		super.update(customSettings, current_clip_source);
		updateChildren(current_clip_source);
	}
}