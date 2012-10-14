package com.acehostingllc.deckerandroid;

import com.acehostingllc.deckerandroid.decker.decker.model.Global;
import com.acehostingllc.deckerandroid.decker.decker.model.Ruleset;

import com.acehostingllc.deckerandroid.decker.decker.model.FunctionCall;
import com.acehostingllc.deckerandroid.decker.decker.model.ScriptNode;
import com.acehostingllc.deckerandroid.decker.decker.model.Value;
import com.acehostingllc.deckerandroid.decker.decker.view.AbstractView;
import com.acehostingllc.deckerandroid.decker.decker.view.SplashScreen;

import com.acehostingllc.deckerandroid.R;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class DeckerActivity extends Activity {
	String[] args = new String[0];
    @Override
    public void onCreate(Bundle savedInstanceState) {
		Log.w("DeckerActivity", "test1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
       //Global.debug_level = 5;
     // parse the command line switches
     		String initial_ruleset = "decker";
     		for (int i = 0; i < args.length; i++) {
     			int equal_sign_position = args[i].indexOf('=');
     			if (equal_sign_position > 0) {
     				final String variable = args[i].substring(0,equal_sign_position);
     				final String value = args[i].substring(equal_sign_position+1);
     				if (variable.equals("initial_ruleset"))
     					initial_ruleset = value;
     			}
     		}

     		// display the splash screen, load the rulesets
     		
     		//    SplashScreen ss = new SplashScreen();
     		//    Global.setDisplayedComponent(ss);
     		int splashscreen = R.layout.splashscreen;
     		
     		this.setContentView(splashscreen);

    		Log.w("DeckerActivity", "initializeDataModel");
     		Global.initializeDataModel(this);
     		Log.w("DeckerActivity", "loadRulesets");
     		Global.loadRulesets(this);
     		Log.w("DeckerActivity", "initializeRulesets");
     		// center the game window on the screen by default
     		//Global.getEngineData().add("display_center_x").set(ss.getToolkit().getScreenSize().width / 2);
     		//Global.getEngineData().add("display_center_y").set(ss.getToolkit().getScreenSize().height / 2);
     		Global.initializeRulesets();
     		// load up the initial ruleset
     		Log.w("DeckerActivity", "place1");
     		final Ruleset[] r = com.acehostingllc.deckerandroid.decker.decker.model.Global.ruleset;
     		for (int i = r.length; --i >= 0; )
     			if (i == 0 || r[i].getName().equalsIgnoreCase(initial_ruleset)) {
     				Global.setCurrentRuleset(com.acehostingllc.deckerandroid.decker.decker.model.Global.ruleset[i]);
     				break;
     			}
     		// launch the game
     		//Global.setDisplayedComponent(frame);

     		Log.w("DeckerActivity", "place2");
     		AbstractView.reloadArtwork(true);
     		// set the initial screen
     		Value initial_screen = Global.getCurrentRuleset().data.get("initial_screen");
     		if (initial_screen == null || initial_screen.type() != Value.STRUCTURE) {
     			throw new RuntimeException("The ruleset \""+Global.getCurrentRuleset().getName()+"\" does not have a STRUCTURE in its RULESET.initial_screen variable, instead it has "+((initial_screen==null)?" no initial_screen variable":(initial_screen+" ("+initial_screen.typeName()+")")));
     		}
     		Log.w("DeckerActivity", "place3");
     		Value displayScreenFunction = ScriptNode.getVariable("displayScreen");
     		if (displayScreenFunction == null || displayScreenFunction.type() != Value.FUNCTION) {
     			throw new RuntimeException("displayScreen must be a FUNCTION but instead has the value "+displayScreenFunction+" ("+((displayScreenFunction==null)?"null":displayScreenFunction.typeName()));
     		}
     		Log.w("DeckerActivity", "initial screen type: "+initial_screen.type());
     		FunctionCall.executeFunctionCall(this, displayScreenFunction.function(), new Value[]{ initial_screen }, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.splashscreen, menu);
        return true;
    }
}
