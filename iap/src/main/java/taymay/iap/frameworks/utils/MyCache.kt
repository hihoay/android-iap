package taymay.iap.frameworks.utils

import android.content.Context
import android.content.SharedPreferences


object MyCache {
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var preferences: SharedPreferences
    fun putLongValueByName(context: Context, name: String, value: Long) {
        openLog(context)
        editor.putLong(name, value)
        editor.commit()
    }

    fun getLongValueByName(
        context: Context, name: String, defaultValue: Long
    ): Long {
        openLog(context)
        return preferences.getLong(name, defaultValue)
    }

    fun setArrayStrings(arrayName: String, array: ArrayList<String>, mContext: Context) {
        val prefs = mContext.getSharedPreferences("Cache_Array", 0)
        val editor = prefs.edit()
        editor.putInt(arrayName + "_size", array.size)
        for (i in array.indices) editor.putString(arrayName + "_" + i, array[i])
        editor.apply()
    }

    fun getArrayStrings(arrayName: String, mContext: Context): ArrayList<String> {
        val prefs = mContext.getSharedPreferences("Cache_Array", 0)
        val size = prefs.getInt(arrayName + "_size", 0)
        val array = ArrayList<String>(size)
        for (i in 0 until size) array.add(prefs.getString(arrayName + "_" + i, null).toString())
        return array
    }

    fun setPrefs(key: String, value: String, context: Context) {
        val sharedpreferences = context.getSharedPreferences("Cache_Array", Context.MODE_PRIVATE)
        val editor = sharedpreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getPrefs(key: String, context: Context): String {
        val sharedpreferences = context.getSharedPreferences("Cache_Array", Context.MODE_PRIVATE)
        return sharedpreferences.getString(key, "notfound").toString()
    }

    private fun openLog(context: Context) {
        preferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        editor = preferences.edit()
    }

    fun putStringValueByName(context: Context, name: String, value: String) {
        openLog(context)
        editor.putString(name, value)
        editor.commit()
    }

    fun getStringValueByName(
        context: Context, name: String, def: String
    ): String {
        openLog(context)
        return preferences.getString(name, def).toString()
    }

    fun getStringValueByName(
        context: Context, name: String
    ): String {
        openLog(context)
        return preferences.getString(name, "").toString()
    }

    fun putIntValueByName(context: Context, name: String, value: Int) {
        openLog(context)
        editor.putInt(name, value)
        editor.commit()
    }

    fun getIntValueByName(
        context: Context, name: String, defaultValue: Int
    ): Int {
        openLog(context)
        return preferences.getInt(name, defaultValue)
    }

    fun getIntValueByName(context: Context, name: String): Int {
        openLog(context)
        return preferences.getInt(name, 0)
    }

    fun putBooleanValueByName(context: Context, name: String, value: Boolean) {
        openLog(context)
        editor.putBoolean(name, value)
        editor.commit()
    }

    fun getBooleanValueByName(context: Context, name: String): Boolean {
        openLog(context)
        return preferences.getBoolean(name, false)
    }

    fun removeAll() {
        editor.clear()
        editor.commit()
    }

    fun getBooleanValueByName(context: Context, name: String, b: Boolean): Boolean {
        openLog(context)
        return preferences.getBoolean(name, b)
    }
}
