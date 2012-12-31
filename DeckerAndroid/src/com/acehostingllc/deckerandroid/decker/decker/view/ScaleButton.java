package com.acehostingllc.deckerandroid.decker.decker.view;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ScaleButton extends Button {

	public ScaleButton(final ViewWrapper viewWrapper, final float addition) {
		super(viewWrapper.getContext());
		this.setOnTouchListener(
				new OnTouchListener()
				{
					public boolean onTouch(View v, MotionEvent event) {
						viewWrapper.screen.scale += addition;
						viewWrapper.draw();
						return true;
					}
					
				}
				);
	}

}
