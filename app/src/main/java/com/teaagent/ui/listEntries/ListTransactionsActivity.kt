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
import com.teaagent.databinding.ActivityListBinding
import com.teaagent.domain.firemasedbEntities.Customer
import com.teaagent.ui.report.ReportActivity
import com.teaagent.ui.saveentry.SaveEntryViewModel
import com.teaagent.ui.saveentry.SaveEntryViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class ListTransactionsActivity : AppCompatActivity() {
    val TAG: String = "ListTransactions_Activity"
    private lateinit var customerName: String
    private var kg: Double = 0.0
    private var amount: Double = 0.0
    private lateinit var binding: ActivityListBinding

    // Repository
    private fun getTrackingApplicationInstance() = application as TeaAgentApplication
    private fun getTrackingRepository() = getTrackingApplicationInstance().trackingRepository

    var adapter: ItemAdapter? = null
    var dateTime = Calendar.getInstance()
    var data = ArrayList<String>()

    // ViewModel
    private val listEntryActivityyViewModel: ListTransactionsViewModel by viewModels {
        ListTransactionsViewModelFactory(getTrackingRepository())
    }

    // ViewModel
    private val saveEntryViewModel: SaveEntryViewModel by viewModels {
        SaveEntryViewModelFactory(getTrackingRepository())
    }

    var recyclerview: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        recyclerview = binding.list
        recyclerview!!.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            getALLCustomer()
        }

        lifecycleScope.launch {
            adapter = ItemAdapter(data)
            recyclerview?.adapter = adapter
        }

        searchCustomer()

        buttonCalender()
        sendClick()

        listEntryActivityyViewModel.customerNames.observe(this, Observer { it ->
            dismissProgressDialog()

        })

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
    private fun searchCustomer() {

        binding.buttonSearchCustomerName.setOnClickListener {

            showProgressDialog()


            customerName = binding.editTextSearchCustomerName.text.toString()

            lifecycleScope.launch {
//                data = mapsActivityViewModel.getTListByCustomerName(customerName)
                data = listEntryActivityyViewModel.getByNameAndDateFromFirebaseDb(
                    customerName,
                    dateTime.timeInMillis
                )

                adapter = ItemAdapter(data)
                recyclerview?.adapter = adapter

            }
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
            if (customerName.length > 0) {
                val listActiviTyIntent = Intent(this, ReportActivity::class.java)
                listActiviTyIntent.putExtra(ReportActivityBundleTag, customerName)
                startActivity(listActiviTyIntent)
            } else {
                Toast.makeText(
                    this, "Please select customer ",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private suspend fun getALLCustomer() {
        showProgressDialog()

        var customerNames: ArrayList<String> = ArrayList()
        var list: ArrayList<Customer> = saveEntryViewModel.getAllCustomerFirebaseDb()
        for (customer in list) {
            customer.name?.let { customerNames.add(it) }
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
                customerName = customerNAmes?.get(position).toString()
                Log.d(TAG, "onItemSelected customerName " + customerName)
                binding.editTextSearchCustomerName.setText(customerName)
            }


            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })

        delay(3000)
        dismissProgressDialog()
        spinner.adapter = adapter
    }

}



