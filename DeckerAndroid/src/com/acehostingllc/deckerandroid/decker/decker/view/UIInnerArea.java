package com.acehostingllc.deckerandroid.decker.decker.view;

class UIInnerArea extends DisplayedComponent
{
	UIInnerArea (final DisplayedComponent _parent) {
		super(null, _parent);
	}




	protected void draw (final AndroidGraphics g) {
		// draw the child components
		final DisplayedComponent[] c = child;
		final int cc = child_count;
		for (int i = 0; i < cc; i++)
			c[i].draw(g);
	}




	protected void update (final int customSettings, final DisplayedComponent current_clip_source) {
		parent.applyInnerBounds(this);
		updateChildren(current_clip_source);
	}




	void updateChildren (final DisplayedComponent current_clip_source) {
		component = parent.component;
		super.updateChildren(current_clip_source);
		component = null;
	}
}