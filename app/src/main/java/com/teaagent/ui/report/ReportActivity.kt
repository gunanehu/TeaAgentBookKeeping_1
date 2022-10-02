package com.teaagent.ui.report

import androidx.appcompat.app.AppCompatActivity


class ReportActivity : AppCompatActivity()
/*
{

    val TAG: String = "ReportActivity"
    private lateinit var binding: ActivityReportBinding

    // ViewModel
    private val listEntryActivityyViewModel: ListTransactionsViewModel by viewModels {
        ListTransactionsViewModelFactory()
    }

    // ViewModel
    private val saveEntryViewModel: SaveEntryViewModel by viewModels {
        SaveEntryViewModelFactory()
    }
    var presenter: PaymentsHTMLReportCreator? = null
    private fun getTrackingApplicationInstance() = application as TeaAgentApplication
    private fun getTrackingRepository() = getTrackingApplicationInstance().trackingRepository
    var data = ArrayList<BalanceTx>()
    public val ReportActivityBundleTag: String = "ReportActivityBundleTag"
    var customerName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        customerName = intent.getStringExtra(ReportActivityBundleTag)
        presenter = PaymentsHTMLReportCreator(this, customerName)
        lifecycleScope.launch {

            getALLByCustomers()
            val webSettings: WebSettings = binding.tvReport.getSettings()
            webSettings.javaScriptEnabled = true
            val webViewClient = WebViewClientImpl(this@ReportActivity)
            binding.tvReport.setWebViewClient(webViewClient)


        }

        binding.buttonShare.setOnClickListener {
            emailReport()
        }

        listEntryActivityyViewModel.reportEntities.observe(this, Observer { it ->

            val listOfStrings: ArrayList<BalanceTx> = ArrayList()
            listOfStrings.addAll(it)
            data = listOfStrings
            Log.d(
                FirebaseUtil.TAG,
                "******reportEntities MutableLiveData<List<CollectionEntry>> *********** ********************* customers $it"
            )

            val collectionEntry: BalanceTx? = presenter?.getCommonCustomerDetail(data.get(0))
            commonCustomerDetails = presenter?.getCommonCustomerDetailsText(collectionEntry)
//            presenter?.setCommonCustomerDetails(commonCustomerDetails)

            textFile = presenter?.setSalesSummaryDataForShare(data)
            textFile?.let { presenter?.setTextFile(it) }

            binding.tvReport.loadData(
                "<html><body>" +
                        commonCustomerDetails + textFile +
                        "</body></html>", "text/html", "UTF-8"
            )
        })

    }


    suspend fun getALLByCustomers() {
        var collectionEntrys: java.util.ArrayList<BalanceTx> = java.util.ArrayList()

        if (customerName != null) {

            customerName?.let { it1 ->
                listEntryActivityyViewModel.getByNameFirebaseDb(it1)
            }

        }
    }

    var textFile: String? = null
    var commonCustomerDetails: String? = null

    private val READ_WRITE_PERMISSION_REQUEST_CODE = 0x01

    protected fun emailReport() {
        if (isFileReadWritePermissionGranted()) {
            textFile?.let { presenter?.prepareDataForEmail(it) }
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


    */
/**
     * Callback method that confirms required @permission is granted or not, if yes then email
     * the report else show the message
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     *//*

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (READ_WRITE_PERMISSION_REQUEST_CODE == requestCode) {
            if (isFileReadWritePermissionGranted()) {
                textFile?.let { presenter?.prepareDataForEmail(it) }
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

}*/
