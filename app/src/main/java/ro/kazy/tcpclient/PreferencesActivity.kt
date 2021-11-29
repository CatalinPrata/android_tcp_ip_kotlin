package ro.kazy.tcpclient

import android.os.Bundle
import android.preference.PreferenceActivity

/**
 * Created by catalin on 29/11/2021
 */
class PreferencesActivity : PreferenceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
    }
}