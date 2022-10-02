package com.teaagent.ui.saveentry

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
import com.teaagent.R
import com.teaagent.data.FirebaseUtil
import com.teaagent.database.TeaAgentsharedPreferenceUtil
import com.teaagent.databinding.ActivitySaveCustomerBinding
import com.teaagent.domain.firemasedbEntities.AccountType
import com.teaagent.domain.firemasedbEntities.uimappingentities.SaveAccountInfo
import java.text.SimpleDateFormat
import java.util.*


/**
 * Main Screen
 */
class SaveAccountDetailActivity : AppCompatActivity() {

    val TAG: String = "SaveInstitutionActivity"

    private lateinit var name: String
    private lateinit var institutionType: String  //

    private lateinit var phoneUserName: String  //= binding.editTextPhoneUserName.text.toString()
    private lateinit var institutionCode: String  //= binding.editTextInstituteCode.text.toString()
    private lateinit var address: String  //= binding.editTextInstituteAddr.text.toString()

    private lateinit var acNo: String  //= binding.editTextAcNo.text.toString()
    private lateinit var netBankingUserName: String  //= binding.editTextNetBAnkingUserNAme.text.toString()
    private lateinit var password: String  //= binding.editTextNetBAnkingPwrd.text.toString()
    private lateinit var atmNo: String  //= binding.editTextAtmNo.text.toString()
    private lateinit var atmPin: String  //

    var spinner: Spinner? = null
    var dateTime = Calendar.getInstance()

    private lateinit var binding: ActivitySaveCustomerBinding

    // ViewModel
    private val saveEntryViewModel: SaveAccountViewModel by viewModels {
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

        declareTypeSpinner()

        // Set up button click events
        binding.saveCustomerButton.setOnClickListener {
            if (binding.editTextCustomerName.text.toString().length > 0) {
                val cuustomerEntry = getEditTextValues()
                /*  SaveAccountInfo(
                      it.type,
                      it.name,

                      it.phoneUserName,
                      it.institutionCode,
                      it.address,

                       it.acNo,
                      it.netBankingUserName,
                      it.password,
                      it.atmNo,
                      it.atmPin
                  )
              }*/
                addAccountInfo(cuustomerEntry);
            } else {
                showErrorMesage()
            }
        }

        binding.nextButton.setOnClickListener {
            val listActiviTyIntent = Intent(this, SaveAccountTxEntryActivity::class.java)
            startActivity(listActiviTyIntent)
        }

    }

    var accountType: String? = null
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
                accountType = enumValues?.get(position).toString()
                institutionType = accountType as String
                Log.d(TAG, "onItemSelected accountType " + accountType)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                accountType = enumValues?.get(0).toString()
                Log.d(TAG, "default onItemSelected accountType " + accountType)

            }
        })
        spinner.adapter = adapter
    }


    fun addAccountInfo(customerEntity: SaveAccountInfo?) {
        val customer = customerEntity?.let {
            SaveAccountInfo(
                it.id,
                it.type,
                it.name,

                it.phoneUserName,
                it.institutionCode,
                it.address,

                it.acNo,
                it.netBankingUserName,
                it.password,
                it.atmNo,
                it.atmPin
            )
        }
        saveEntryViewModel.addAccountDEtail(customer)
    }

    private fun clearEditTextValues() {
        binding.editTextCustomerName.setText("")
        binding.todaysDate.setText("")
        name = ""
        institutionType = ""
        dateTime.timeInMillis = 0
        spinner?.setSelection(0)
    }

    private fun getEditTextValues(): SaveAccountInfo {
//        institutionType = binding.editTextInstituteType.text.toString()
        name = binding.editTextCustomerName.text.toString()

        phoneUserName = binding.editTextPhoneUserName.text.toString()
        institutionCode = binding.editTextInstituteCode.text.toString()
        address = binding.editTextInstituteAddr.text.toString()

        acNo = binding.editTextAcNo.text.toString()
        netBankingUserName = binding.editTextNetBAnkingUserNAme.text.toString()
        password = binding.editTextNetBAnkingPwrd.text.toString()
        atmNo = binding.editTextAtmNo.text.toString()
        atmPin = binding.editTextAtmPin.text.toString()


        /*  SaveAccountInfo(
                    it.type,
                    it.name,

                    it.phoneUserName,
                    it.institutionCode,
                    it.address,

                     it.acNo
                    it.netBankingUserName,
                    it.password,
                    it.atmNo,
                    it.atmPin
                )
            }*/
        return SaveAccountInfo( "",
            institutionType,
            name,

            FirebaseUtil.phoneUser.name,
            institutionCode,
            address,

            acNo,
            netBankingUserName,
            password,
            atmNo,
            atmPin
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
