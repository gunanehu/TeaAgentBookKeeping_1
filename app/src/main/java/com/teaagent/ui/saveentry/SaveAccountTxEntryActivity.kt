package com.teaagent.ui.saveentry

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.teaagent.R
import com.teaagent.TeaAgentApplication
import com.teaagent.data.FirebaseUtil
import com.teaagent.database.TeaAgentsharedPreferenceUtil
import com.teaagent.databinding.ActivitySaveCollectionBinding
import com.teaagent.databinding.ActivitySaveCollectionBinding.inflate
import com.teaagent.domain.firemasedbEntities.BalanceTx
import com.teaagent.domain.firemasedbEntities.TradeAnalysis
import com.teaagent.ui.listEntries.ListTransactionsActivity
import com.teaagent.ui.report.xcel.ExcelUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


/**
 * Main Screen
 */
class SaveAccountTxEntryActivity : AppCompatActivity() {
    private var selectedAccountInfo: TradeAnalysis? = null

    //    private lateinit var customerName: String
    private var kg: Long = 0
    private var amount: Long = 0
    private var advancedPaymentAmount = null
    var labourAmount: Long = 0
    var netTotal: Long = 0
    var allTrades: List<TradeAnalysis>? = null
    var dateTime = Calendar.getInstance()

    //    var phoneUserAndCustomer: PhoneUserAndCustomer? = null
    private var totalKgAmount: Long = 0

    val TAG: String = "SaveAccountTxEntryActivity"
    private lateinit var binding: ActivitySaveCollectionBinding

    // ViewModel
    private val mapsActivityViewModel: SaveAccountViewModel by viewModels {
        SaveEntryViewModelFactory()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        // Switch to AppTheme for displaying the activity
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        val view = binding.root
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)

        setContentView(view)

        showProgressDialog()

        getAllAccountDetailsFirebaseDb()

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        binding.todaysDate.setText(currentDate)

        // Set up button click events
        binding.saveButton.setOnClickListener {
            if (
                binding.editTextAmount.text?.length!! > 0
            ) {
                val cuustomerEntry  : BalanceTx= createCollectionEntryFromEditText()!!

                cuustomerEntry.accountId= selectedAccountInfo?.id!!
                mapsActivityViewModel.addTeaTransactionRecord(cuustomerEntry)
            } else {
                showErrorMesage()
            }
        }

        binding.endButton.setOnClickListener {
            val listActiviTyIntent = Intent(this, ListTransactionsActivity::class.java)
            startActivity(listActiviTyIntent)
        }


        binding.buttonsetDate.setOnClickListener {
            val c = Calendar.getInstance()
            val mYear = c[Calendar.YEAR]
            val mMonth = c[Calendar.MONTH]
            val mDay = c[Calendar.DAY_OF_MONTH]

            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    dateTime.set(mYear, monthOfYear, dayOfMonth)
                    binding.todaysDate.text =
                        dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year

                    retrievEditTextData()
                    calculateNetTotalAmount()
                    binding.editTextTotalAmount.setText("Net total amount : " + netTotal + "")
                },
                mYear,
                mMonth,
                mDay
            )
            datePickerDialog.show()
        }
    }

    private fun getAllAccountDetailsFirebaseDb() {
        GlobalScope.launch(Dispatchers.Main) { // launches coroutine in main thread
            showProgressDialog()
            mapsActivityViewModel.getAllAccountDetailsFirebaseDb()
        }

        mapsActivityViewModel.tradeDetailsLiveData.observe(this, Observer() { it ->
//            var tradeAnalyses: ArrayList<TradeAnalysis> =
//                decryptToAccountDetailsList(it as ArrayList<TradeAnalysis>)

            allTrades = it as ArrayList<TradeAnalysis>
            getALLCustomerNamesToSpinner(allTrades as ArrayList<TradeAnalysis>)


            val dataList=  it as ArrayList<TradeAnalysis>
            ExcelUtils.exportDataIntoWorkbook(this,"tradeAlalysisReport",dataList)

            dismissProgressDialog()

        })
    }




    // Repository
    private fun getTrackingApplicationInstance() = application as TeaAgentApplication
    private fun getTrackingRepository() = getTrackingApplicationInstance().trackingRepository

  /*  private fun clearEditTextValues() {
        binding.editTextCustomerName.setText("")
        binding.editTextKG.setText("")
        binding.editTextAmount.setText("")
        binding.editTextLabourAmount.setText("")
        binding.editTextAmount.setText("")
        binding.editTextAdvancedPaymentAmountt.setText("")
        binding.todaysDate.setText("")
//        customerName = ""

        kg = 0
        amount?.toLong()!!
        advancedPaymentAmount = null
        labourAmount = 0
        totalKgAmount = 0

        dateTime.timeInMillis = 0
        spinner?.setSelection(0)
    }*/

    private fun createCollectionEntryFromEditText(): BalanceTx? {

        retrievEditTextData()

        // advancedPaymentAmount = binding.editTextAdvancedPaymentAmountt.text?.toString()?.toLong()!!

        calculateNetTotalAmount()
        Log.d(
            TAG,
            "totalKgAmount before insert : " + totalKgAmount + " timeInMillis " + dateTime.timeInMillis+" selectedAccountInfo!!.id "+            selectedAccountInfo!!.id,

            );

        var entryTimestampDate = dateTime.timeInMillis
        var entryConvertedDate = BalanceTx.convertDate(dateTime.timeInMillis)

        /*  data class BalanceTx(
              var accountType: String,
              var accountNo: String,
              var balanceAmount: Long,
              var timestamp: Long,

              var phoneUserName: String?,
              var customerName: String
          )*/
        val tran = BalanceTx(
            "",
            selectedAccountInfo!!.id,
            selectedAccountInfo!!.tradeIncomeType,
            selectedAccountInfo!!.ExitPrice!!,

            amount.toString(),
            System.currentTimeMillis().toString(),

            FirebaseUtil.getCurrentPhoneUser(TeaAgentsharedPreferenceUtil.getAppId().toString()).name,
            selectedAccountInfo?.stockName.toString()
        )
        return tran
    }

    private fun retrievEditTextData() {
//        customerName = binding.editTextCustomerName.text.toString()
        //  kg = binding.editTextKG.text?.toString()?.toLong()!!
        amount = binding.editTextAmount.text?.toString()?.toLong()!!
        //  labourAmount = binding.editTextLabourAmount.text?.toString()?.toLong()!!
    }

    private fun calculateNetTotalAmount() {
//        netTotal = (kg * amount) - labourAmount
    }


    private fun showErrorMesage() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Entry alert")
        builder.setMessage("Enter all values")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            Toast.makeText(
                applicationContext,
                android.R.string.yes, Toast.LENGTH_SHORT
            ).show()
        }

        val show = builder.show()
    }

    var spinner: Spinner? = null


    private fun getALLCustomerNamesToSpinner(list: ArrayList<TradeAnalysis>) {
        showProgressDialog()
        var accountInfoDisplayName: ArrayList<String> = ArrayList()

        for (customer in list) {
//            customer.name?.let { customerNames.add(it) }
            accountInfoDisplayName.add(/*customer.bankName!! + */customer.stockName.toString())
        }

        /*  val hashSet: HashSet<String> = HashSet()
          hashSet.addAll(accountInfoDisplayName!!)

          val customerNAmes: MutableList<String> = ArrayList()
          customerNAmes.addAll(hashSet)*/

        spinner = findViewById(R.id.spinnerSearchCustomerName)
        val adapter: ArrayAdapter<Any?> =
            ArrayAdapter<Any?>(
                this, android.R.layout.simple_spinner_dropdown_item,
                accountInfoDisplayName!! as List<Any?>
            )
        spinner?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val customerText = accountInfoDisplayName?.get(position).toString()

                selectedAccountInfo = retrieveSpinnerSelectedItem(customerText, list)
//                binding.editTextCustomerName.setText(selectedAccountInfo?.ExitPrice)        //TODO with actual value

                Log.d(TAG, "onItemSelected selectedAccountInfo " + selectedAccountInfo)

                showtvSelectedAccountDetail(selectedAccountInfo)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
//                binding.editTextCustomerName.setText("")
//                selectedAccountInfo = list?.get(0)
            }
        })

        spinner?.adapter = adapter
    }

    private fun retrieveSpinnerSelectedItem(
        bankName: String,
        list: ArrayList<TradeAnalysis>
    ): TradeAnalysis? {
        for (item in list) {
            if (item.stockName.toString().contentEquals(bankName)) {
                return item
            }
        }
        return null
    }

    private fun showtvSelectedAccountDetail(selectedAccountInfo: TradeAnalysis?) {
        binding.tvSelectedAccountDetail.setText(selectedAccountInfo.toString())
    }

    var mProgressDialog: ProgressDialog? = null
    private fun showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(this)
        }
        mProgressDialog?.setTitle("Loading data...")
        mProgressDialog?.show()
    }

    private fun dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog?.dismiss()
        }
    }

/*
    private fun decryptToAccountDetailsList(arrayList: ArrayList<TradeAnalysis>): ArrayList<TradeAnalysis> {
        var arrayList2: ArrayList<TradeAnalysis> = ArrayList()

        for (accountInfo in arrayList) {
//        val id = StringEncryption.decryptMsg(accountInfo?.id).toString() //throwing "Invalid encypted text format" Exception, so commented
            val accountType = accountInfo?.tradeIncomeType.toString()
//            val bankName =
//                StringEncryption.decryptMsg(accountInfo?.bankName).toString()
            val id =
                accountInfo?.id

            val stockName =
               accountInfo?.stockName
            val isbuy =
                accountInfo?.isBuy

            val phoneUserName =
                accountInfo?.phoneUserName
            val tradeIncomeType =
                accountInfo?.tradeIncomeType

            val EntryPrice =
               accountInfo?.EntryPrice
            val SLPrice =
               accountInfo?.SLPrice


            val sLLevel =
                accountInfo?.sLLevel
            val targetLevel =
                accountInfo?.targetLevel

            val ExitPrice =
                accountInfo?.ExitPrice.toString()
            val ITFTrend =accountInfo?.ITFTrend
            val HTFTrend = accountInfo?.HTFTrend


            val HTFLocation =accountInfo?.HTFLocation
            val ExecutionZone = accountInfo?.ExecutionZone

            val entryEmotion = accountInfo?.entryEmotion

            val timestampTradePlanned =accountInfo?.timestampTradePlanned
            val note = accountInfo?.note

            val b =
                TradeAnalysis(
                    id,
                    phoneUserName,
                    tradeIncomeType,
                    stockName,
                    isbuy,

                    EntryPrice,
                    SLPrice,
                    ExitPrice,


                    sLLevel,
                    targetLevel,

                    HTFLocation,
                    HTFTrend,
                    ITFTrend,
                    ExecutionZone,


                    tradeManagementType,
                    tradeExitPostAnalysisTypeType,
                    missedTradeType,
                    mentalState,
                    confidenceLevel,
                    exitNote,

                    entryEmotion,
                    timestampTradePlanned,
                    note

                )

          */
/*  class TradeAnalysis(
                var id: String,
                open var phoneUserName: String?,

                var tradeIncomeType: String,
                var stockName: String?,

                //Trade entry/sl/exit planned prices
                var EntryPrice: String?,
                var SLPrice: String?,
                var ExitPrice: String?,

                //Trade analysis
//    Higher time frame
                var HTFLocation: String?,
                var HTFTrend: String?,

                //    Intermediate time frame
                var ITFTrend: String?,

                //Execution time frame-type2/3
                var ExecutionZone: String?,

                var entryEmotion: String?,

//timestamp when trde is planned
                var timestampTradePlanned: String?,
                var note: String?

            )*//*



            arrayList2.add(b)
        }
        return arrayList2
    }
*/
}
