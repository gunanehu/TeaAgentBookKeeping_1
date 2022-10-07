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

    fun addToPreferenceTabId( id: String) {

        Log.i(TAG, " added PhoneUser addToPreferenceTabId " + id)
        myEdit?.putString("AppId", id);
        myEdit?.commit();
    }

    fun getAppId(): String? {
        return sharedPreferences?.getString("AppId", "")
    }


    val TAG: String = "Application"
}