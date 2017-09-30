package com.ongoza.itemsmenu.Utils;

import android.util.Log;

import com.ongoza.itemsmenu.MainActivity;

/**
 * Utility class for scripts.
 * 
 * public members in this class can be accessed in scripts
 * after adding as a global variable.
 *
 * <pre>
 * GVRScriptManager sm = getGVRContext().getScriptManager();
 *
 * sm.addVariable("utils", new ScriptUtils());
 * </pre>
 */
public class ScriptUtils {
	private static final String TAG = MainActivity.getTAG();
	//private static final String TAG = ScriptUtils.class.getSimpleName();
	public void log(String msg) {
		Log.d(TAG, msg);
	}
}