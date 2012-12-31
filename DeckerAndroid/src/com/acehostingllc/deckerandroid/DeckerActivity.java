package com.acehostingllc.deckerandroid;

import com.acehostingllc.deckerandroid.decker.decker.model.Global;
import com.acehostingllc.deckerandroid.tasks.StartGame;
import com.acehostingllc.deckerandroid.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.RelativeLayout;

public class DeckerActivity extends Activity {
	
	//private static ImageView imageView;
	private static Context context;
	private boolean inputChanged;
		
	String[] args = new String[0];
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeckerActivity.context = this.getApplicationContext();
        setContentView(R.layout.splashscreen);

        Global.debug_level = 5;
        // parse the command line switches
 		String initial_ruleset = "Decker";
 		for (int i = 0; i < args.length; i++) {
 			int equal_sign_position = args[i].indexOf('=');
 			if (equal_sign_position > 0) {
 				final String variable = args[i].substring(0,equal_sign_position);
 				final String value = args[i].substring(equal_sign_position+1);
 				if (variable.equals("initial_ruleset"))
 					initial_ruleset = value;
 			}
 		}
 		
 		Global.initializeDataModel(this);
 		
 		new Thread(new StartGame(initial_ruleset, this)).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.splashscreen, menu);
        return true;
    }
	
	public static Context getAppContext() {
		return context;
	}
}
