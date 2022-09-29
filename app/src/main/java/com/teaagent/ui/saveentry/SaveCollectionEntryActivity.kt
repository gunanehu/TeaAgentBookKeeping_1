package com.teaagent.ui.saveentry

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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.teaagent.R
import com.teaagent.TrackingApplication
import com.teaagent.data.FirebaseUtil
import com.teaagent.databinding.ActivityMainBinding
import com.teaagent.databinding.ActivityMainBinding.inflate
import com.teaagent.domain.firemasedbEntities.CollectionEntry
import com.teaagent.domain.firemasedbEntities.Customer
import com.teaagent.domain.firemasedbEntities.PhoneUserAndCustomer
import com.teaagent.ui.listEntries.ListEntryActivity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


/**
 * Main Screen
 */
class SaveCollectionEntryActivity : AppCompatActivity() {
    private lateinit var customerName: String
    private var kg: Long = 0
    private var amount: Long = 0
    private var advancedPaymentAmount: Long = 0
    var labourAmount: Long = 0
    var netTotal: Long = 0
    var phoneUserAndCustomer: PhoneUserAndCustomer? = null
    private var totalKgAmount: Long = 0

    val TAG: String = "SaveCollectionEntryActivity"
    private lateinit var binding: ActivityMainBinding

    // ViewModel
    private val mapsActivityViewModel: SaveEntryViewModel by viewModels {
        SaveEntryViewModelFactory(getTrackingRepository())
    }


//        if (trackingEntity != null && customerName != null)
    //        {
//            Log.v(TAG, trackingEntity.toString())
//            lifecycleScope.launch {
//                val index: Long = mapsActivityViewModel.insert(trackingEntity)
//                if (index > 0) {
//                    Toast.makeText(
//                        applicationContext,
//                        "value added index" + index, Toast.LENGTH_SHORT
//                    ).show()
//                    clearEditTextValues()
//                } else {
//                    Toast.makeText(
//                        applicationContext,
//                        "value not added returned >> "+index, Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        }


    override fun onCreate(savedInstanceState: Bundle?) {
        // Switch to AppTheme for displaying the activity
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        lifecycleScope.launch {
//            getALLCustomerRoomDb()
            getALLCustomerNamesToSpinner()
        }

        val packageInfo = this.packageManager.getPackageInfo(packageName, 0)
        val versionCode = packageInfo.versionCode
        val version = packageInfo.versionName
        binding.version.setText(version)

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        binding.todaysDate.setText("currentDate : " + currentDate)

        // Set up button click events
        binding.saveButton.setOnClickListener {
            if (binding.editTextCustomerName.text.toString().length > 0 &&
                binding.editTextKG.text?.length!! > 0 &&
                binding.editTextAmount.text?.length!! > 0 &&
                binding.editTextLabourAmount.text?.length!! > 0 &&
                binding.todaysDate.text?.length!! > 0
            ) {
                val cuustomerEntry = createCollectionEntryFromEditText()
                mapsActivityViewModel.addTeaTransactionRecord(cuustomerEntry)
            } else {
                showErrorMesage()
            }
        }
        /* binding.buttonSearchCustomerName.setOnClickListener {
             val customerName = binding.editTextSearchCustomerName.text.toString()
             lifecycleScope.launch {
                 val total = mapsActivityViewModel.getAllEntitiesByCustomerName(customerName)
                 Log.d(TAG, "total : " + total);
                 binding.tViewSearchCustomerNameResult.text = total.toString()
             }
         }*/
        binding.endButton.setOnClickListener {
            val listActiviTyIntent = Intent(this, ListEntryActivity::class.java)
            startActivity(listActiviTyIntent)
        }
        // 1
        mapsActivityViewModel.allTrackingEntities.observe(this) { allTrackingEntities ->
            if (allTrackingEntities.isEmpty()) {
            }
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
    private fun getTrackingApplicationInstance() = application as TrackingApplication
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

    private fun createCollectionEntryFromEditText(): CollectionEntry {

        customerName = binding.editTextCustomerName.text.toString()
        kg = binding.editTextKG.text?.toString()?.toLong()!!
        amount = binding.editTextAmount.text?.toString()?.toLong()!!
        labourAmount = binding.editTextLabourAmount.text?.toString()?.toLong()!!

        // advancedPaymentAmount = binding.editTextAdvancedPaymentAmountt.text?.toString()?.toLong()!!

        netTotal = (kg * amount) - labourAmount
        Log.d(
            TAG,
            "totalKgAmount before insert : " + totalKgAmount + " timeInMillis " + dateTime.timeInMillis
        );
        phoneUserAndCustomer =
            PhoneUserAndCustomer(FirebaseUtil.getCurrentPhoneUser().phoneUserId, customerName)

        val tran = CollectionEntry(
            Calendar.getInstance().timeInMillis.toString(),
            kg,
            amount,
            labourAmount,
            netTotal,
            dateTime.timeInMillis,
            phoneUserAndCustomer
        )
        return tran
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


    private suspend fun getALLCustomerNamesToSpinner() {
        var customerNames: ArrayList<String> = ArrayList()
        var list: ArrayList<Customer> = mapsActivityViewModel.getAllCustomerFirebaseDb()
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

    private suspend fun getALLCustomerRoomDb() {

        var list: List<String> = mapsActivityViewModel.getAllCustomerNameRoomDb()

        val hashSet: HashSet<String> = HashSet()
        hashSet.addAll(list!!)

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


}
