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
import com.teaagent.R
import com.teaagent.TeaAgentApplication
import com.teaagent.data.FirebaseUtil
import com.teaagent.databinding.ActivityShowTxListBinding
import com.teaagent.domain.firemasedbEntities.AccountType
import com.teaagent.domain.firemasedbEntities.BalanceTx
import com.teaagent.domain.firemasedbEntities.uimappingentities.SaveAccountInfo
import com.teaagent.ui.report.ReportActivity
import com.teaagent.ui.saveentry.SaveAccountViewModel
import com.teaagent.ui.saveentry.SaveEntryViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import util.StringEncryption
import java.util.*


class ListTransactionsActivity : AppCompatActivity() {
    val TAG: String = "ListTransactions"
    private lateinit var instituteName: String
    private var kg: Double = 0.0
    private var amount: Double = 0.0
    private lateinit var binding: ActivityShowTxListBinding

    // Repository
    private fun getTrackingApplicationInstance() = application as TeaAgentApplication
    private fun getTrackingRepository() = getTrackingApplicationInstance().trackingRepository

    var adapter: ItemAdapter? = null
    var dateTime = Calendar.getInstance()
    var data = ArrayList<String>()

    // ViewModel
    private val listEntryActivityyViewModel: ListTransactionsViewModel by viewModels {
        ListTransactionsViewModelFactory()
    }

    // ViewModel
    private val saveAccountDetailViewModel: SaveAccountViewModel by viewModels {
        SaveEntryViewModelFactory()
    }

    var recyclerview: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityShowTxListBinding.inflate(layoutInflater)
        val view = binding.root
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)

        recyclerview = binding.list
        recyclerview!!.layoutManager = LinearLayoutManager(this)

        setContentView(view)
        declareTypeSpinner()

        getAccountDetails()
        setSearchTransactionsClickListener()
        getNetAssetsByNameTransactionsClickListener()

        buttonCalender()
        sendClick()

    }

    private fun getAccountDetails() {
        lifecycleScope.launch {
            showProgressDialog()
            saveAccountDetailViewModel.getAllAccountDetailsFirebaseDb()
        }

        saveAccountDetailViewModel.accountsLiveData.observe(this, Observer() { it ->
            getAccountInfos(it as ArrayList<SaveAccountInfo>)
            dismissProgressDialog()
        })
    }

    var total: Long = 0
    private fun convertBalanceTxToStringList(customers: ArrayList<BalanceTx>): ArrayList<String> {
        total = 0
        var customerString: ArrayList<String> = ArrayList()
        for (customer in customers) {
            val customerText: String = convertBalanceTxToString(customer)
            customerString.add(customerText)

            val balanceAmount =
                StringEncryption.decryptMsg(customer?.balanceAmount).toString()
            total = total + balanceAmount.toLong()
        }
        return customerString
    }

    private fun convertBalanceTxToString(balanceTx: BalanceTx): String {
//        val id = StringEncryption.decryptMsg(balanceTx?.id).toString() //throwing "Invalid encypted text format" Exception, so commented
//        val accountType =
//            StringEncryption.decryptMsg(balanceTx?.accountType).toString()
        val accountType = balanceTx?.accountType.toString()
        val accountNo =
            StringEncryption.decryptMsg(balanceTx?.accountNo).toString()
        val balanceAmount =
            StringEncryption.decryptMsg(balanceTx?.balanceAmount).toString()
        val timestamp =
            StringEncryption.decryptMsg(balanceTx?.timestamp).toString()
//        val phoneUserName =
//            StringEncryption.decryptMsg(collectionEntry?.phoneUserName.toString()).toString()
        val phoneUserName =
            balanceTx?.phoneUserName.toString()
        val bankName = StringEncryption.decryptMsg(balanceTx?.bankName)

        val b = BalanceTx("id", accountType, accountNo, balanceAmount, timestamp, phoneUserName, bankName)
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

    private fun setSearchTransactionsClickListener() {
        showProgressDialog()
        binding.buttonSearchCustomerName.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                listEntryActivityyViewModel.getTxByTypeFromFirebaseDb(
                    institutionType!!
                )
            }
        }
        blanceTxMutableLiveDataCallback()

    }

    private fun blanceTxMutableLiveDataCallback() {
        listEntryActivityyViewModel.blanceTxMutableLiveData.observe(this, Observer { it ->
            Log.d(FirebaseUtil.TAG, "***************** ********************* customers $it")

            var customerString: ArrayList<String> =
                convertBalanceTxToStringList(it as ArrayList<BalanceTx>)

            adapter = ItemAdapter(customerString)
            recyclerview?.adapter = adapter

            binding.totalAmount.setText("Total amount : " + total)
            dismissProgressDialog()

        })
    }

    fun getNetAssetsByNameTransactionsClickListener() {
        showProgressDialog()
        binding.buttonGetNetAssetsByName.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                listEntryActivityyViewModel.getNetAssetsByName()
            }
        }
        blanceTxMutableLiveDataCallback()
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

    private fun getAccountInfos(list: ArrayList<SaveAccountInfo>) {

        var customerNames: ArrayList<String> = ArrayList()
        for (customer in list) {
            customer.bankName?.let { customerNames.add(it) }
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
                AccountType::class.java
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


}



