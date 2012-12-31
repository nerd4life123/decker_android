package com.acehostingllc.deckerandroid.tasks;

import android.util.Log;
import android.widget.RelativeLayout;

import com.acehostingllc.deckerandroid.DeckerActivity;
import com.acehostingllc.deckerandroid.decker.decker.model.FunctionCall;
import com.acehostingllc.deckerandroid.decker.decker.model.Global;
import com.acehostingllc.deckerandroid.decker.decker.model.Ruleset;
import com.acehostingllc.deckerandroid.decker.decker.model.ScriptNode;
import com.acehostingllc.deckerandroid.decker.decker.model.Value;
import com.acehostingllc.deckerandroid.decker.decker.view.AbstractView;

public class StartGame implements Runnable {

	private String initial_ruleset;
	private transient final DeckerActivity activity;

	public StartGame(String initial_ruleset, DeckerActivity activity) {
		this.initial_ruleset = initial_ruleset;
		this.activity = activity;
	}

	public void run() {
		Global.loadRulesets();
		Log.w("DeckerActivity", "initializeRulesets");
		// center the game window on the screen by default
		//Global.getEngineData().add("display_center_x").set(ss.getToolkit().getScreenSize().width / 2);
		//Global.getEngineData().add("display_center_y").set(ss.getToolkit().getScreenSize().height / 2);
		Global.initializeRulesets();
		// load up the initial ruleset
		final Ruleset[] r = com.acehostingllc.deckerandroid.decker.decker.model.Global.ruleset;
		for (int i = r.length; --i >= 0; )
			if (i == 0 || r[i].getName().equalsIgnoreCase(initial_ruleset)) {
				Global.setCurrentRuleset(com.acehostingllc.deckerandroid.decker.decker.model.Global.ruleset[i]);
				break;
			}

		AbstractView.reloadArtwork();
		// set the initial screen
		Value initial_screen = Global.getCurrentRuleset().data.get("initial_screen");
		if (initial_screen == null || initial_screen.type() != Value.STRUCTURE) {
			throw new RuntimeException("The ruleset \""+Global.getCurrentRuleset().getName()+"\" does not have a STRUCTURE in its RULESET.initial_screen variable, instead it has "+((initial_screen==null)?" no initial_screen variable":(initial_screen+" ("+initial_screen.typeName()+")")));
		}
		Value displayScreenFunction = ScriptNode.getVariable("displayScreen");
		if (displayScreenFunction == null || displayScreenFunction.type() != Value.FUNCTION) {
			throw new RuntimeException("displayScreen must be a FUNCTION but instead has the value "+displayScreenFunction+" ("+((displayScreenFunction==null)?"null":displayScreenFunction.typeName()));
		}
		FunctionCall.executeFunctionCall(displayScreenFunction.function(), new Value[]{ initial_screen }, null);

		activity.runOnUiThread(
				new Runnable()
				{
					public void run() {
						RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
								RelativeLayout.LayoutParams.WRAP_CONTENT,
								RelativeLayout.LayoutParams.WRAP_CONTENT);
						activity.setContentView(Global.getViewWrapper(), layoutParams);
						Global.getViewWrapper().update();
					}
				}
		);
	}
}
