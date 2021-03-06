package com.acehostingllc.deckerandroid.decker.decker.model;
import android.util.Log;

import com.acehostingllc.deckerandroid.decker.decker.util.*;

import java.util.Locale;



public final class Ruleset
{
	public final Structure data;
	final StringTreeMap structure_types = new StringTreeMap();
	private Script[] script = new Script[0]; // the list of scripts in this ruleset


	Ruleset (final String ruleset_name)  {
		data = new Structure("RULESET", null);
		if (ruleset_name.equals("")) { // if this is the engine ruleset, change its name to ENGINE
			data.get("structure_type").set("ENGINE");
		}
		data.add("RULESET_NAME").set(ruleset_name);
		data.add("DEFAULT_LOCALIZATION").set("english");
		final Structure constants = new Structure("SET", null);
		if (ruleset_name.equals("")) { // if this is the engine ruleset, add the only standard constant, UNDEFINED
			constants.add("UNDEFINED");
		}
		data.add("CONSTANTS").set(constants);
		// add the set of structure types
		final Structure structure_types = new Structure("SET", null);
		data.add("STRUCTURE_TYPES").set(structure_types);
		if (ruleset_name.equals("")) { // if this is the engine ruleset, add the standard structure type ARRAY
			final String[] expandable_structure_types = { "COLLECTION", "ENGINE", "GLOBAL", "LOCAL", "RULESET", "SET" };
			for (int i = 0; i < expandable_structure_types.length; i++) {
				final Structure s = new Structure(expandable_structure_types[i], null);
				s.add("expandable").set(true);
				structure_types.add(expandable_structure_types[i]).set(s);
			}
		}
	}


	void addScript (final Script new_script)  {
		script = (Script[]) ArrayModifier.addElement(script, new Script[script.length+1], new_script);
	}


	void addStructureType (final StructureDefinition sd)  {
		structure_types.put(sd.getStructureType(), sd);
	}


	void initialize (final Locale[] accepted_localizations)  {
		final String s = data.get("DEFAULT_LOCALIZATION").toString();

		for (int i = 0; i < script.length; i++)
		{
			script[i].execute(accepted_localizations, s);
		}
	}


	public String getName()  { return data.get("RULESET_NAME").toString(); }
}