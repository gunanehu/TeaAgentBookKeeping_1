package com.teaagent.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.teaagent.databinding.ActivityPopupChartBinding

class PopUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPopupChartBinding
    var stock: String? = null
    var interval: String? = null
        public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPopupChartBinding.inflate(layoutInflater)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)
        val view = binding.root
        setContentView(view)

        val webView = binding.webviewer
        val stock = intent.getStringExtra("stock")
        val interval = intent.getStringExtra("interval")

        if (stock != null && interval != null) {
            loadWebviewTradingViewchart(webView, stock, interval)
        }


    }
    fun onChartClicked(view: View?){

        val popUpActivity = Intent(this, PopUpActivity::class.java)

        popUpActivity.putExtra("stock", stock)
        popUpActivity.putExtra("interval", interval)

        startActivity(popUpActivity)
    }
    private fun loadWebviewTradingViewchart(webView: WebView, symbol: String, interval: String) {
        val url = "https://www.tradingview.com/chart/?symbol=$symbol&interval=$interval"
        webView.loadUrl(url)

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
    }
}