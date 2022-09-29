package com.teaagent.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.teaagent.databinding.ActivitySplashBinding
import com.teaagent.ui.saveentry.SaveCustomerActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashActivity : AppCompatActivity() {
    val TAG: String = "SplashActivity"

    val activityScope = CoroutineScope(Dispatchers.Main)
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        showAppVersion()
        activityScope.launch {
            delay(1000)

            var intent = Intent(this@SplashActivity, SaveCustomerActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
    }


    private fun showAppVersion() {
        val packageInfo = this.packageManager.getPackageInfo(packageName, 0)
        val versionCode = packageInfo.versionCode
        val version = packageInfo.versionName
        binding.appversion.setText(version)
    }

}


