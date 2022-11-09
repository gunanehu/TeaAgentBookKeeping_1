package com.teaagent.database

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import com.teaagent.AppHelper
import com.teaagent.TeaAgentApplication

object TeaAgentsharedPreferenceUtil {

    var sharedPreferences: SharedPreferences? =
        TeaAgentApplication.getContext()
            ?.getSharedPreferences("TeaAgentsharedPreference", MODE_PRIVATE)

    // Creating an Editor object to edit(write to the file)
    var myEdit: SharedPreferences.Editor? = sharedPreferences?.edit()

    fun addToPreferenceTabId() {
        val getTabletId: String = AppHelper.getInstance().uniqueUserID

        Log.i(TAG, " added PhoneUser addToPreferenceTabId " + getTabletId)
        myEdit?.putString("AppId", getTabletId);
        myEdit?.commit();
    }

    fun getAppId(): String? {
        return sharedPreferences?.getString("AppId", "")
    }
    fun addToPreferenceCurrentStartTime(millli:Long) {
        myEdit?.putLong("TimeLog", millli);
        myEdit?.commit();
    }
    fun getToPreferenceCurrentStartTime():Long?{
        return sharedPreferences?.getLong("TimeLog", 0)
    }
    val TAG: String = "Application"
}