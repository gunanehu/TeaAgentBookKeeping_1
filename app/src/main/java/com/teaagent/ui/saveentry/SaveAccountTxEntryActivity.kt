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
import com.teaagent.databinding.ActivitySaveCollectionBinding
import com.teaagent.databinding.ActivitySaveCollectionBinding.inflate
import com.teaagent.domain.firemasedbEntities.BalanceTx
import com.teaagent.domain.firemasedbEntities.uimappingentities.SaveAccountInfo
import com.teaagent.ui.listEntries.ListTransactionsActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


/**
 * Main Screen
 */
class SaveAccountTxEntryActivity : AppCompatActivity() {
    private lateinit var customerName: String
    private var kg: Long = 0
    private var amount: Long = 0
    private var advancedPaymentAmount: Long = 0
    var labourAmount: Long = 0
    var netTotal: Long = 0

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
        GlobalScope.launch(Dispatchers.Main) { // launches coroutine in main thread
            mapsActivityViewModel.getAllAccountDetailsFirebaseDb()
        }

        mapsActivityViewModel.accountsLiveData.observe(this, Observer() { it ->
            getALLCustomerNamesToSpinner(it as ArrayList<SaveAccountInfo>)
            dismissProgressDialog()
        })

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        binding.todaysDate.setText(currentDate)

        // Set up button click events
        binding.saveButton.setOnClickListener {
            if (binding.editTextCustomerName.text.toString().length > 0 &&
                binding.editTextKG.text?.length!! > 0 &&
                binding.editTextAmount.text?.length!! > 0 &&
                binding.editTextLabourAmount.text?.length!! > 0 &&
                binding.todaysDate.text?.length!! > 0
            ) {
              /* todo  val cuustomerEntry = createCollectionEntryFromEditText()
                mapsActivityViewModel.addTeaTransactionRecord(cuustomerEntry)*/
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

    var dateTime = Calendar.getInstance()

    // Repository
    private fun getTrackingApplicationInstance() = application as TeaAgentApplication
    private fun getTrackingRepository() = getTrackingApplicationInstance().trackingRepository

    private fun clearEditTextValues() {
        binding.editTextCustomerName.setText("")
        binding.editTextKG.setText("")
        binding.editTextAmount.setText("")
        binding.editTextLabourAmount.setText("")
        binding.editTextAmount.setText("")
        binding.editTextAdvancedPaymentAmountt.setText("")
        binding.todaysDate.setText("")
        customerName = ""

        kg = 0
        amount = 0
        advancedPaymentAmount = 0
        labourAmount = 0
        totalKgAmount = 0

        dateTime.timeInMillis = 0
        spinner?.setSelection(0)
    }

//    private fun createCollectionEntryFromEditText(): BalanceTx? {
//
//        retrievEditTextData()
//
//        // advancedPaymentAmount = binding.editTextAdvancedPaymentAmountt.text?.toString()?.toLong()!!
//
//        calculateNetTotalAmount()
//        Log.d(
//            TAG,
//            "totalKgAmount before insert : " + totalKgAmount + " timeInMillis " + dateTime.timeInMillis
//        );
//
//        var entryTimestampDate = dateTime.timeInMillis
//        var entryConvertedDate = BalanceTx.convertDate(dateTime.timeInMillis)
//
//      /*  data class BalanceTx(
//            var accountType: String,
//            var accountNo: String,
//            var balanceAmount: Long,
//            var timestamp: Long,
//
//            var phoneUserName: String?,
//            var customerName: String
//        )*/
//        val tran = BalanceTx(
//            kg,
//            amount,
//            labourAmount,
//            netTotal,
//            entryTimestampDate, entryConvertedDate,
//            FirebaseUtil.getCurrentPhoneUser().name,
//            customerName
//        )
//        return tran
//    }

    private fun retrievEditTextData() {
        customerName = binding.editTextCustomerName.text.toString()
        kg = binding.editTextKG.text?.toString()?.toLong()!!
        amount = binding.editTextAmount.text?.toString()?.toLong()!!
        labourAmount = binding.editTextLabourAmount.text?.toString()?.toLong()!!
    }

    private fun calculateNetTotalAmount() {
        netTotal = (kg * amount) - labourAmount
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


    private fun getALLCustomerNamesToSpinner(list: ArrayList<SaveAccountInfo>) {
        //todo start progress dialog
        var customerNames: ArrayList<String> = ArrayList()
//        var list: ArrayList<Customer> =

        for (customer in list) {
            customer.name?.let { customerNames.add(it) }
        }

        val hashSet: HashSet<String> = HashSet()
        hashSet.addAll(customerNames!!)

        val customerNAmes: MutableList<String> = ArrayList()
        customerNAmes.add("Select pre selected name")
        customerNAmes.addAll(hashSet)

        spinner = findViewById(R.id.spinnerSearchCustomerName)
        val adapter: ArrayAdapter<Any?> =
            ArrayAdapter<Any?>(
                this, android.R.layout.simple_spinner_dropdown_item,
                customerNAmes!! as List<Any?>
            )
        spinner?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                customerName = customerNAmes?.get(position).toString()
                Log.d(TAG, "onItemSelected customerName " + customerName)
                binding.editTextCustomerName.setText(customerName)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                binding.editTextCustomerName.setText("")
            }
        })

        spinner?.adapter = adapter
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


}
