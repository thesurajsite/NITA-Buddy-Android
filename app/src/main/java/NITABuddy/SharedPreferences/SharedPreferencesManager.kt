package NITABuddy.SharedPreferences

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(private val context: Context): SharedPreferenceHandler {

    override fun getLoginState(): Boolean{
        val pref: SharedPreferences = getSharedPreferences("Shared-Preference", Context.MODE_PRIVATE)
        return pref.getBoolean("Login-State", false)
    }

    override fun updateLoginState(boolean: Boolean){
        val pref: SharedPreferences=getSharedPreferences("Shared-Preference", Context.MODE_PRIVATE)
        val editor=pref.edit()
        editor.putBoolean("Login-State", boolean)
        editor.apply()
    }

    override fun getUserToken(): String{
        val pref: SharedPreferences=getSharedPreferences("Shared-Preference", Context.MODE_PRIVATE)
        return pref.getString("User-Token", "").toString()
    }

    override fun updateUserToken(token: String){
        val pref: SharedPreferences=getSharedPreferences("Shared-Preference", Context.MODE_PRIVATE)
        val editor=pref.edit()
        editor.putString("User-Token", token)
        editor.apply()
    }

    private fun getSharedPreferences(s: String, modePrivate: Int): SharedPreferences {
        return context.getSharedPreferences(s, modePrivate)
    }


}

interface SharedPreferenceHandler {

fun getLoginState(): Boolean

fun updateLoginState(boolean: Boolean)

fun getUserToken(): String

fun updateUserToken(token: String)
}
