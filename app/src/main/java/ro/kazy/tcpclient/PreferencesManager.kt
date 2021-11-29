package ro.kazy.tcpclient

import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Used to load username from the preferences
 *
 * Created by catalin on 29/11/2021
 */
class PreferencesManager private constructor() {

    private val mPreferenceManager: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.context)

    val userName: String
        get() = mPreferenceManager.getString("pref_username", null) ?: ""

    companion object {
        val instance = PreferencesManager()
    }

}