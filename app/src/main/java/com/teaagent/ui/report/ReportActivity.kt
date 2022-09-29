package com.teaagent.ui.report

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.teaagent.R
import com.teaagent.TrackingApplication
import com.teaagent.databinding.ActivityReportBinding
import com.teaagent.domain.CustomerEntity
import com.teaagent.ui.listEntries.ListEntryViewModel
import com.teaagent.ui.listEntries.ListEntryViewModelFactory
import com.teaagent.ui.saveentry.SaveEntryViewModel
import com.teaagent.ui.saveentry.SaveEntryViewModelFactory
import kotlinx.coroutines.launch


class ReportActivity : AppCompatActivity() {

    val TAG: String = "ReportActivity"
    private lateinit var binding: ActivityReportBinding

    // ViewModel
    private val listEntryActivityyViewModel: ListEntryViewModel by viewModels {
        ListEntryViewModelFactory(getTrackingRepository())
    }

    // ViewModel
    private val saveEntryViewModel: SaveEntryViewModel by viewModels {
        SaveEntryViewModelFactory(getTrackingRepository())
    }
    var presenter: PaymentsReportPresenterImpl? = null
    private fun getTrackingApplicationInstance() = application as TrackingApplication
    private fun getTrackingRepository() = getTrackingApplicationInstance().trackingRepository
    var data = ArrayList<CustomerEntity>()
    public val ReportActivityBundleTag: String = "ReportActivityBundleTag"
    var customerName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        customerName = intent.getStringExtra(ReportActivityBundleTag)
        presenter = PaymentsReportPresenterImpl(this, customerName, listEntryActivityyViewModel)

        lifecycleScope.launch {
            presenter!!.  getALLByCustomers()
            val text = presenter!!.paymentReportData

            val webSettings: WebSettings = binding.tvReport.getSettings()
            webSettings.javaScriptEnabled = true
            val webViewClient = WebViewClientImpl(this@ReportActivity)
            binding.tvReport.setWebViewClient(webViewClient)


            binding.tvReport.loadData("<html><body>" +
                    text +
                    "</body></html>", "text/html", "UTF-8")


        }

        binding.buttonShare.setOnClickListener {
            lifecycleScope.launch {
                data =
                    customerName?.let { it1 -> listEntryActivityyViewModel.getALLByCustomerName(it1) } as ArrayList<CustomerEntity>
                emailReport()
            }
        }


    }

    private val READ_WRITE_PERMISSION_REQUEST_CODE = 0x01

    protected fun emailReport() {
        if (isFileReadWritePermissionGranted()) {
            presenter?.prepareDataForEmail()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                READ_WRITE_PERMISSION_REQUEST_CODE
            )
        }
    }


    /**
     * Callback method that confirms required @permission is granted or not, if yes then email
     * the report else show the message
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (READ_WRITE_PERMISSION_REQUEST_CODE == requestCode) {
            if (isFileReadWritePermissionGranted()) {
                presenter?.prepareDataForEmail()
            } else {
                Toast.makeText(
                    this, "permission_not_granted_alert",
                    Toast.LENGTH_LONG
                ).show()
                Log.e(TAG, "permission_not_granted_alert");
            }
        }
    }

    private fun isFileReadWritePermissionGranted(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) &&
                PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

}