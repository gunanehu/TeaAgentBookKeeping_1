package com.teaagent.ui.saveentry

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.teaagent.R
import com.teaagent.TeaAgentApplication
import com.teaagent.data.FirebaseUtil
import com.teaagent.databinding.ActivitySaveCustomerBinding
import com.teaagent.domain.firemasedbEntities.Customer
import java.text.SimpleDateFormat
import java.util.*


/**
 * Main Screen
 */
class SaveCustomerActivity : AppCompatActivity() {

    val TAG: String = "SaveCustomerActivity"

    private lateinit var customerName: String
    private lateinit var additionalInfo: String
    var spinner: Spinner? = null
    var dateTime = Calendar.getInstance()

    // Repository
    private fun getTrackingApplicationInstance() = application as TeaAgentApplication
    private fun getTrackingRepository() = getTrackingApplicationInstance().trackingRepository

    private lateinit var binding: ActivitySaveCustomerBinding

    // ViewModel
    private val mapsActivityViewModel: SaveEntryViewModel by viewModels {
        SaveEntryViewModelFactory(getTrackingRepository())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = ActivitySaveCustomerBinding.inflate(layoutInflater)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)
        val view = binding.root
        setContentView(view)

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        binding.todaysDate.setText("currentDate : " + currentDate)

        // Set up button click events
        binding.saveCustomerButton.setOnClickListener {
            if (binding.editTextCustomerName.text.toString().length > 0) {
                val cuustomerEntry = getEditTextValues()
                addCustomer(cuustomerEntry);
            } else {
                showErrorMesage()
            }
        }

        binding.nextButton.setOnClickListener {
            val listActiviTyIntent = Intent(this, SaveCollectionEntryActivity::class.java)
            startActivity(listActiviTyIntent)
        }

        initialiseCalender()
    }

    private fun initialiseCalender() {
        binding.buttonsetDate.setOnClickListener {
            val c = Calendar.getInstance()
            val mYear = c[Calendar.YEAR]
            val mMonth = c[Calendar.MONTH]
            val mDay = c[Calendar.DAY_OF_MONTH]

//            dateTime = Calendar.getInstance()
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


    fun addCustomer(customerEntity: Customer?) {
        val customer = customerEntity?.let {
            Customer(
             /*   dateTime.timeInMillis.toString(),*/
                it.name,
                it.AdditionalInfo,
                it.phoneUserName
            )
        }
        mapsActivityViewModel.addCustomer(customer)
    }

    private fun clearEditTextValues() {
        binding.editTextCustomerName.setText("")
        binding.todaysDate.setText("")
        customerName = ""
        additionalInfo = ""
        dateTime.timeInMillis = 0
        spinner?.setSelection(0)
    }

    private fun getEditTextValues(): Customer {
        customerName = binding.editTextCustomerName.text.toString()
        additionalInfo = binding.editTextAdditionalInfo.text.toString()
        return Customer(
      /*      dateTime.timeInMillis.toString(),*/
            customerName,
            additionalInfo,
            FirebaseUtil.phoneUser.name
        )
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


}
