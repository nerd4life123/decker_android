package com.acehostingllc.deckerandroid.decker.decker.model;
import java.util.Locale;

import android.util.Log;

import com.acehostingllc.deckerandroid.DeckerActivity;
import com.acehostingllc.deckerandroid.decker.decker.util.StringTreeMap;


final class Script
{
	private String filename;
	private DeckerActivity activity;
	private final StringTreeMap localizations = new StringTreeMap(true); // <- the (true) parameter means the language names are treated case-insensitive when fetching localizations


	public String getFilename()
	{
		return filename;
	}

	Script (DeckerActivity activity, final String _filename)  { this.activity = activity; filename = _filename; }


	void execute (final Locale[] accepted_localizations, final String default_localization)  {
		for(int i = 0; i < accepted_localizations.length; i++)  {
			final String s = accepted_localizations[i].getDisplayLanguage(accepted_localizations[i]);
			Object ls = getLocalization(s);
			if (ls == null && s.equalsIgnoreCase(default_localization))
				ls = getLocalization("default");
			if (ls != null) {
				// we need a LOCAL object on the stack for the script execution
//if (Global.debug_level > 0)
Log.w("DeckerActivity", "   running script from file : "+filename);
				final Structure local = new Structure("LOCAL", null);
				ScriptNode.addStackItem(local);
				((Block)ls).execute();
				ScriptNode.removeStackItem(local, (Block)ls);
				return;
			}
		}
System.err.println("The script "+filename+" doesn't support any of the localizations you have chosen");
	}


	void addLocalization (final String language, final Object script)  { localizations.put(language, script); }
	Block getLocalization (final String language)  { return (Block) localizations.get(language); }
	int localizationCount ()  { return localizations.size(); }
}