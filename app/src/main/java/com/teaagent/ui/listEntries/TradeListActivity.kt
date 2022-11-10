package com.teaagent.ui.listEntries

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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.teaagent.R
import com.teaagent.data.FirebaseUtil
import com.teaagent.databinding.ActivityShowTradeListBinding
import com.teaagent.domain.firemasedbEntities.TradeAnalysis
import com.teaagent.domain.firemasedbEntities.enums.TradeIncomeType
import com.teaagent.ui.report.ReportActivity
import com.teaagent.ui.saveentry.SaveAccountDetailActivity
import com.teaagent.ui.saveentry.SaveAccountViewModel
import com.teaagent.ui.saveentry.SaveEntryViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class TradeListActivity : AppCompatActivity(), ItemClickListener {
    val TAG: String = "ListTransactions"
    private lateinit var instituteName: String
    private var kg: Double = 0.0
    private var amount: Double = 0.0
    private lateinit var binding: ActivityShowTradeListBinding

    // Repository

    var recycleViewAdapter: CustomAdapter? = null

    //    var recycleViewAdapter: ItemAdapter? = null
    var dateTime = Calendar.getInstance()
    var data = ArrayList<String>()

//    //    // ViewModel
//    private val listEntryActivityyViewModel: ListTransactionsViewModel by viewModels {
//        ListTransactionsViewModelFactory()
//    }


    // ViewModel
    private val saveAccountDetailViewModel: SaveAccountViewModel by viewModels {
        SaveEntryViewModelFactory()
    }

    var recyclerview: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityShowTradeListBinding.inflate(layoutInflater)
        val view = binding.root
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)

        recyclerview = binding.list

        recyclerview!!.layoutManager = LinearLayoutManager(this)

        setContentView(view)
        declareTypeSpinner()

        // getAccountDetails()
        // setSearchTransactionsClickListener()
        getNetAssetsByNameTransactionsClickListener()

        buttonCalender()
        sendClick()

    }

    private fun getAccountDetails() {
        lifecycleScope.launch {
            showProgressDialog()
            saveAccountDetailViewModel.getAllAccountDetailsFirebaseDb()
        }

        saveAccountDetailViewModel.tradeDetailsLiveData.observe(this, Observer() { it ->
            getAccountInfos(it as ArrayList<TradeAnalysis>)
            dismissProgressDialog()
        })
    }

    var total: Long = 0
    private fun convertBalanceTxToStringList(customers: ArrayList<TradeAnalysis>): ArrayList<String> {
        total = 0
        var customerString: ArrayList<String> = ArrayList()
        for (customer in customers) {
            val customerText: String = convertBalanceTxToString(customer)
            customerString.add(customerText)

//            val balanceAmount =
//                customer?.balanceAmount
//            val balanceAmount =
//                StringEncryption.decryptMsg(customer?.balanceAmount).toString()
            total = 0;//TODO commented temp total + balanceAmount.toLong()
        }
        return customerString
    }

    private fun convertBalanceTxToString(tradeAnalysis: TradeAnalysis): String {
        val id = tradeAnalysis?.id
        val stockName =
            tradeAnalysis?.stockName
        val tradeIncomeType = tradeAnalysis?.tradeIncomeType.toString()
        val EntryPrice =
            tradeAnalysis?.EntryPrice
        val ExitPrice =
            tradeAnalysis?.ExitPrice
        val SLPrice =
            tradeAnalysis?.SLPrice

        val sLLevel =
            tradeAnalysis?.sLLevel
        val targetLevel =
            tradeAnalysis?.targetLevel


        val timestampTradePlanned =
            tradeAnalysis?.timestampTradePlanned
        val phoneUserName =
            tradeAnalysis?.phoneUserName.toString()

        val HTFLocation =
            tradeAnalysis?.HTFLocation
        val HTFTrend =
            tradeAnalysis?.HTFTrend
        val ITFTrend =
            tradeAnalysis?.ITFTrend
        val ExecutionZone =
            tradeAnalysis?.ExecutionZone
        val note =
            tradeAnalysis?.note

        val entryEmotion = tradeAnalysis?.entryEmotion

        val b =
            TradeAnalysis(
                id,
                phoneUserName,
                tradeIncomeType,
                stockName,
                EntryPrice,
                SLPrice,
                ExitPrice,

                sLLevel,
                targetLevel,

                HTFLocation,
                HTFTrend,
                ITFTrend,
                ExecutionZone,

                entryEmotion,
                timestampTradePlanned,
                note

            )
        return b.toString()
    }

    private fun buttonCalender() {
        binding.buttonfromDate.setOnClickListener {
            val c = Calendar.getInstance()
            val mYear = c[Calendar.YEAR]
            val mMonth = c[Calendar.MONTH]
            val mDay = c[Calendar.DAY_OF_MONTH]

            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    dateTime.set(mYear, monthOfYear, dayOfMonth)

                    binding.tViewFromDate.text =
                        dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                },
                mYear,
                mMonth,
                mDay
            )
            datePickerDialog.show()
        }
    }

    var mProgressDialog: ProgressDialog? = null

    /* private fun setSearchTransactionsClickListener() {
         showProgressDialog()
         binding.buttonSearchCustomerName.setOnClickListener {
             GlobalScope.launch(Dispatchers.Main) {
                 listEntryActivityyViewModel.getTxByTypeFromFirebaseDb(
                     institutionType!!
                 )
             }
         }
         allTradeDetailsMutableLiveDataCallback()

     }
 */
    var customerString: ArrayList<String>? = null
    var customers: ArrayList<TradeAnalysis>? = null


    private suspend fun allTradeDetailsMutableLiveDataCallback() {
        saveAccountDetailViewModel.tradeDetailsLiveData.observe(this, Observer { it ->
            Log.d(FirebaseUtil.TAG, "***************** ********************* customers $it")

            customerString = convertBalanceTxToStringList(it as ArrayList<TradeAnalysis>)
            customers = it /*as ArrayList<BalanceTx>*/
//            recycleViewAdapter = ItemAdapter(customerString!!)
            recycleViewAdapter = CustomAdapter(customerString, this)

            recyclerview?.adapter = recycleViewAdapter
//            recycleViewAdapter!!.setClickListener(this);

            binding.totalAmount.setText("Total amount : " + total)
            dismissProgressDialog()

        })
    }

    fun getNetAssetsByNameTransactionsClickListener() {
//        showProgressDialog()
        binding.buttonGetNetAssetsByName.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                saveAccountDetailViewModel.getAllAccountDetailsFirebaseDb()
            }
        }
        GlobalScope.launch(Dispatchers.Main) { // launches coroutine in main thread
            allTradeDetailsMutableLiveDataCallback()
        }
    }

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

    public val ReportActivityBundleTag: String = "ReportActivityBundleTag"

    private fun sendClick() {
        binding.buttonShareScreen.setOnClickListener {
            if (instituteName.length > 0) {
                val listActiviTyIntent = Intent(this, ReportActivity::class.java)
                listActiviTyIntent.putExtra(ReportActivityBundleTag, instituteName)
                startActivity(listActiviTyIntent)
            } else {
                Toast.makeText(
                    this, "Please select customer ",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun getAccountInfos(list: ArrayList<TradeAnalysis>) {

        var customerNames: ArrayList<String> = ArrayList()
        for (customer in list) {
            customer.stockName?.let { customerNames.add(it) }
        }

        val hashSet: HashSet<String> = HashSet()
        hashSet.addAll(customerNames!!)

        val customerNAmes: MutableList<String> = ArrayList()
        customerNAmes.add("Select name")
        customerNAmes.addAll(hashSet)


        val spinner: Spinner = findViewById(R.id.spinnerSearchCustomerName)
        val adapter: ArrayAdapter<Any?> =
            ArrayAdapter<Any?>(
                this, android.R.layout.simple_spinner_dropdown_item,
                customerNAmes!! as List<Any?>
            )
        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                instituteName = customerNAmes?.get(position).toString()
                Log.d(TAG, "onItemSelected instituteName " + instituteName)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })
        spinner.adapter = adapter
    }


    var institutionType: String? = null

    private fun declareTypeSpinner() {
        val spinner: Spinner = findViewById(R.id.spinnerInstituteType)
        val enumValues: List<Enum<*>> = ArrayList(
            EnumSet.allOf(
                TradeIncomeType::class.java
            )
        )

        val adapter: ArrayAdapter<Any?> =
            ArrayAdapter<Any?>(
                this, android.R.layout.simple_spinner_dropdown_item,
                enumValues!! as List<Any?>
            )
        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                institutionType = enumValues?.get(position).toString()
                Log.d(TAG, "onItemSelected accountType " + institutionType)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                institutionType = enumValues?.get(0).toString()
                Log.d(TAG, "default onItemSelected accountType " + institutionType)

            }
        })
        spinner.adapter = adapter
    }


    override fun onClick(position: Int) {
        val balanceTx: TradeAnalysis? = customers?.get(position)
        val txId = balanceTx?.id
        var task: Task<DocumentSnapshot>? = FirebaseUtil.getTxById(txId!!)
        task?.addOnSuccessListener(OnSuccessListener { it ->
            it.data


            val i = Intent(this, SaveAccountDetailActivity::class.java)
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.putExtra("balanceTx", balanceTx)
            this.startActivity(i)

            Toast.makeText(
                this,
                it.data.toString(),
                Toast.LENGTH_LONG
            ).show()

        })
        task?.addOnCompleteListener(OnCompleteListener { it ->

            Toast.makeText(
                this,
                "completed ",
                Toast.LENGTH_LONG
            ).show()
        })


    }


}



