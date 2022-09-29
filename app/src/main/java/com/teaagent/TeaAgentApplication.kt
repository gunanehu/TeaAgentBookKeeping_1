package com.teaagent

import android.app.Application
import android.content.Context
import android.util.Log
import com.teaagent.database.CustomerDatabase
import com.teaagent.database.TeaAgentsharedPreferenceUtil
import com.teaagent.repo.CustomerRepository

// 1
class TeaAgentApplication : Application() {
    // 2
    private val trackingDatabase by lazy { CustomerDatabase.getDatabase(this) }
    val trackingRepository by lazy { CustomerRepository(trackingDatabase.getTrackingDao()) }


    override fun onCreate() {
        super.onCreate()
        context = applicationContext
//        TeaAgentsharedPreferenceUtil.addToPreferenceTabId()
    }
    companion object {
        private var context: Context? = null

        /**
         * Gets the application context.
         * 1
         *
         * @return application context
         */

        fun getContext(): Context? {
            Log.i("APP","Context "+ context)
            return context
        }
    }
}


