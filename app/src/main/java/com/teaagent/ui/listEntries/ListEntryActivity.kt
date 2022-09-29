package com.teaagent.ui.listEntries

import android.app.DatePickerDialog
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teaagent.R
import com.teaagent.TrackingApplication
import com.teaagent.databinding.ActivityListBinding
import com.teaagent.domain.firemasedbEntities.CollectionEntry
import com.teaagent.domain.firemasedbEntities.Customer
import com.teaagent.ui.report.ReportActivity
import com.teaagent.ui.saveentry.SaveEntryViewModel
import com.teaagent.ui.saveentry.SaveEntryViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class ListEntryActivity : AppCompatActivity() {
    private lateinit var customerName: String
    private var kg: Double = 0.0
    private var amount: Double = 0.0
    val TAG: String = "ListEntryActivity"
    private lateinit var binding: ActivityListBinding

    // Repository
    private fun getTrackingApplicationInstance() = application as TrackingApplication
    private fun getTrackingRepository() = getTrackingApplicationInstance().trackingRepository

    var adapter: ItemAdapter? = null

    // ViewModel
    private val listEntryActivityyViewModel: ListEntryViewModel by viewModels {
        ListEntryViewModelFactory(getTrackingRepository())
    }

    // ViewModel
    private val saveEntryViewModel: SaveEntryViewModel by viewModels {
        SaveEntryViewModelFactory(getTrackingRepository())
    }

    var dateTime = Calendar.getInstance()
    var data = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Switch to AppTheme fordisplaying the activity
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        lifecycleScope.launch {
            getALLCustomer()
        }

        val recyclerview = findViewById<RecyclerView>(R.id.list)
        recyclerview.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            adapter = ItemAdapter(data)
            // Setting the Adapter with the recyclerview
            recyclerview.adapter = adapter
        }


        binding.buttonSearchCustomerName.setOnClickListener {
            customerName = binding.editTextSearchCustomerName.text.toString()

            lifecycleScope.launch {
//                data = mapsActivityViewModel.getTListByCustomerName(customerName)
                data = listEntryActivityyViewModel.getByNameAndDateFromFirebaseDb(customerName, dateTime.timeInMillis)
                delay(5000)
                adapter = ItemAdapter(data)
                recyclerview.adapter = adapter

                lifecycleScope.launch {
//                    val total = mapsActivityViewModel.getTotalAmountByCustomerName(customerName)
                    val total = listEntryActivityyViewModel.newAllExpensesFromTo(
                        customerName,
                        dateTime.timeInMillis
                    )
                    Log.d(
                        TAG,
                        "total : " + total + " dateTime.timeInMillis :" + dateTime.timeInMillis
                    );
                    binding.totalAmount.text = "Total amount : " + total.toString()
                }
            }
        }


        binding.buttonfromDate.setOnClickListener {
            // Get Current Date
            // Get Current Date
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
        binding.buttonToDate.setOnClickListener {
            // Get Current Date
            // Get Current Date
            val c = Calendar.getInstance()
            var mYear = c[Calendar.YEAR]
            var mMonth = c[Calendar.MONTH]
            var mDay = c[Calendar.DAY_OF_MONTH]


            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    mYear = year
                    mMonth = monthOfYear
                    mDay = dayOfMonth
                    dateTime.set(year, monthOfYear, dayOfMonth)
                    binding.buttonfromDate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                },
                mYear,
                mMonth,
                mDay,
            )
            datePickerDialog.show()
        }


        sendClick()

    }

    public val ReportActivityBundleTag: String = "ReportActivityBundleTag"

    private fun sendClick() {
        binding.buttonShareScreen.setOnClickListener {
            if(customerName.length>0) {
                val listActiviTyIntent = Intent(this, ReportActivity::class.java)
                listActiviTyIntent.putExtra(ReportActivityBundleTag, customerName)
                startActivity(listActiviTyIntent)
            }else{
                Toast.makeText(
                    this, "Please select customer ",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private suspend fun getALLCustomer() {

//        var list: List<String> = listEntryActivityyViewModel.getAllCustomerName()
        var customerNames: ArrayList<String> = ArrayList()
        var list: ArrayList<Customer> =  saveEntryViewModel.getAllCustomerFirebaseDb()
        for (customer in list) {
            customer.name?.let { customerNames.add(it) }
        }

        val hashSet: HashSet<String> = HashSet()
        hashSet.addAll(customerNames!!)

        val customerNAmes: MutableList<String> = ArrayList()
        customerNAmes.add("Select pre selected name")
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
        spinner.adapter = adapter
    }

}



