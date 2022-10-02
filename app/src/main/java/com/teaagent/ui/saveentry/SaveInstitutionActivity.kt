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
import com.teaagent.data.FirebaseUtil
import com.teaagent.database.TeaAgentsharedPreferenceUtil
import com.teaagent.databinding.ActivitySaveCustomerBinding
import com.teaagent.domain.firemasedbEntities.InstitutionEntity
import java.text.SimpleDateFormat
import java.util.*


/**
 * Main Screen
 */
class SaveInstitutionActivity : AppCompatActivity() {

    val TAG: String = "SaveInstitutionActivity"

    private lateinit var name: String
    private lateinit var institutionType: String
    var spinner: Spinner? = null
    var dateTime = Calendar.getInstance()

    private lateinit var binding: ActivitySaveCustomerBinding

    // ViewModel
    private val saveEntryViewModel: SaveEntryViewModel by viewModels {
        SaveEntryViewModelFactory()
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

        val phoneId: String? = TeaAgentsharedPreferenceUtil.getAppId()
        binding.version.setText("Logged in user : " + phoneId)

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


    fun addCustomer(customerEntity: InstitutionEntity?) {
        val customer = customerEntity?.let {
            /*    data class InstitutionEntity(
          var name: String?,
          var type:String,
          var phoneUserName: String?,
          var institutionCode: String?,
          var address: String?

      ):*/
            InstitutionEntity(
                it.name,
                it.type, it.phoneUserName,
                it.institutionCode,
                ""
            )
        }
        saveEntryViewModel.addCustomer(customer)
    }

    private fun clearEditTextValues() {
        binding.editTextCustomerName.setText("")
        binding.todaysDate.setText("")
        name = ""
        institutionType = ""
        dateTime.timeInMillis = 0
        spinner?.setSelection(0)
    }

    private fun getEditTextValues(): InstitutionEntity {
        name = binding.editTextCustomerName.text.toString()
        institutionType = binding.editTextAdditionalInfo.text.toString()
        /*    data class InstitutionEntity(
                var name: String?,
                var type:String,
                var phoneUserName: String?,
                var institutionCode: String?,
                var address: String?

            ):*/
        return InstitutionEntity(
            name,
            institutionType,
            FirebaseUtil.phoneUser.name,
            ",", ""//todo empty for now
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
