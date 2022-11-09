package com.teaagent.ui.saveentry

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.teaagent.domain.firemasedbEntities.BalanceTx
import com.teaagent.domain.firemasedbEntities.TimerLog
import com.teaagent.domain.firemasedbEntities.TradeAnalysis
import com.teaagent.domain.firemasedbEntities.enums.*
import com.teaagent.ui.listEntries.TradeListActivity
import java.text.SimpleDateFormat
import java.util.*


/**
 * Main Screen
 */
class SaveAccountDetailActivity : AppCompatActivity() {

    private var toUpdate: Boolean = false
    val TAG: String = "SaveInstitutionActivity"

    private lateinit var stockName: String
    private lateinit var institutionType: String  //

    private lateinit var phoneUserName: String  //= binding.editTextPhoneUserName.text.toString()
    private lateinit var entryPrice: String  //= binding.editTextInstituteCode.text.toString()
    private lateinit var slPrice: String  //= binding.editTextInstituteAddr.text.toString()

    private lateinit var exitPrice: String  //= binding.editTextAcNo.text.toString()
    private lateinit var note: String  //= binding.editTextNetBAnkingUserNAme.text.toString()
    private lateinit var password: String  //= binding.editTextNetBAnkingPwrd.text.toString()
    private lateinit var atmNo: String  //= binding.editTextAtmNo.text.toString()
    private lateinit var atmPin: String  //
    var balanceTx: TradeAnalysis? = null
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

        balanceTx = intent.getSerializableExtra("balanceTx") as TradeAnalysis?
        if (balanceTx != null) {
            toUpdate = true
        }
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        binding.todaysDate.setText("currentDate : " + currentDate)

        val phoneId: String? = TeaAgentsharedPreferenceUtil.getAppId()

        declareIncomeTypeSpinner()
        declareHigherTimeFarameLocationSpinner()
        declareHigherTimeFarameTrendSpinner()
        declareIntermediateTimeFarameTrendSpinner()
        declareExecutionTimeFarameSpinner()

        registerEditTextChangeListenerrs()


        declareEntryEmotionTypeSpinner()
        // Set up button click events
        binding.saveCustomerButton.setOnClickListener {
            if (binding.editTextStock.text.toString().length > 0) {
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

                if (toUpdate) {
                    cuustomerEntry.id = balanceTx!!?.id
                }
//                Log.d(TAG, "*****  cuustomerEntry.id " + cuustomerEntry.id)
                addAccountInfo(cuustomerEntry, toUpdate)

            } else {
                showErrorMesage()
            }
        }

        binding.nextButton.setOnClickListener {
//            val listActiviTyIntent = Intent(this, SaveAccountTxEntryActivity::class.java)
            val listActiviTyIntent = Intent(this, TradeListActivity::class.java)
            startActivity(listActiviTyIntent)
        }

    }

    private fun registerEditTextChangeListenerrs() {
        binding.etSLPrice.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val loss = calculateSLPercentage()
                binding.tvSLPercent.setText(loss.toString())
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                val loss = calculateSLPercentage()
                binding.tvSLPercent.setText(loss.toString())
            }
        })


        binding.etExitPrice.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                val profit = calculateProfitPercentage()
                binding.tvProfitPercent.setText(profit.toString() + " %")
            }


            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                val profit = calculateProfitPercentage()
                binding.tvProfitPercent.setText(profit.toString() + " %")
            }
        })


    }

    private fun calculateProfitPercentage(): Float {
        try {
            val exit = binding.etExitPrice.text?.toString()?.toFloat()
            val entry = binding.editTextEntryPrice.text?.toString()?.toFloat()
            val profit = exit!! - entry!!
            return profit / entry * 100
        } catch (e: NumberFormatException) {

        }
        return 0F
    }


    private fun calculateSLPercentage(): Float {
        try {
            val sl = binding.etSLPrice.text?.toString()?.toFloat()
            val entry = binding.editTextEntryPrice.text?.toString()?.toFloat()
            val loss = sl!! - entry!!
            return loss / entry * 100

        } catch (e: NumberFormatException) {

        }
        return 0F
    }

    var higherTimeFarameLocation: String? = null
    private fun declareHigherTimeFarameLocationSpinner() {
        val spinner: Spinner = findViewById(R.id.spinnerHTFLocation)
        val enumValues: List<Enum<*>> = ArrayList(
            EnumSet.allOf(
                HTFLocationType::class.java
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
                higherTimeFarameLocation = enumValues?.get(position).toString()
//                    institutionType = accountType as String
                Log.d(TAG, "onItemSelected higherTimeFarameLocation " + higherTimeFarameLocation)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                higherTimeFarameLocation = enumValues?.get(0).toString()
                Log.d(
                    TAG,
                    "default onItemSelected higherTimeFarameLocation " + higherTimeFarameLocation
                )
            }
        })
        spinner.adapter = adapter
    }


    var higherTimeFarameTrend: String? = null
    private fun declareHigherTimeFarameTrendSpinner() {
        val spinner: Spinner = findViewById(R.id.spinnerHTFTrend)
        val enumValues: List<Enum<*>> = ArrayList(
            EnumSet.allOf(
                TrendType::class.java
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
                higherTimeFarameTrend = enumValues?.get(position).toString()
//                    institutionType = accountType as String
                Log.d(TAG, "onItemSelected higherTimeFarameTrend " + higherTimeFarameTrend)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                higherTimeFarameTrend = enumValues?.get(0).toString()
                Log.d(TAG, "default onItemSelected higherTimeFarameTrend " + higherTimeFarameTrend)
            }
        })
        spinner.adapter = adapter
    }

    var excutionTimeTimeFarameLocation: String? = null
    private fun declareExecutionTimeFarameSpinner() {
        val spinner: Spinner = findViewById(R.id.spinnerExecutionZone)
        val enumValues: List<Enum<*>> = ArrayList(
            EnumSet.allOf(
                ExecutionZoneType::class.java
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
                excutionTimeTimeFarameLocation = enumValues?.get(position).toString()
//                    institutionType = accountType as String
                Log.d(
                    TAG,
                    "onItemSelected excutionTimeTimeFarameLocation " + excutionTimeTimeFarameLocation
                )
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                higherTimeFarameLocation = enumValues?.get(0).toString()
                Log.d(
                    TAG,
                    "default onItemSelected excutionTimeTimeFarameLocation " + excutionTimeTimeFarameLocation
                )
            }
        })
        spinner.adapter = adapter
    }

    var entryEmotion: String? = null

    private fun declareEntryEmotionTypeSpinner() {
        val spinner: Spinner = findViewById(R.id.spinnerEntryEmotion)
        val enumValues: List<Enum<*>> = ArrayList(
            EnumSet.allOf(
                EntryEmotionType::class.java
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
                entryEmotion = enumValues?.get(position).toString()
//                    institutionType = accountType as String
                Log.d(
                    TAG,
                    "onItemSelected entryEmotion " + entryEmotion
                )
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                higherTimeFarameLocation = enumValues?.get(0).toString()
                Log.d(
                    TAG,
                    "default onItemSelected entryEmotion " + entryEmotion
                )
            }
        })
        spinner.adapter = adapter
    }


    var intermediateTimeFarameTrend: String? = null
    private fun declareIntermediateTimeFarameTrendSpinner() {
        val spinner: Spinner = findViewById(R.id.spinnerITFTrend)
        val enumValues: List<Enum<*>> = ArrayList(
            EnumSet.allOf(
                TrendType::class.java
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
                intermediateTimeFarameTrend = enumValues?.get(position).toString()
//                    institutionType = accountType as String
                Log.d(
                    TAG,
                    "onItemSelected intermediateTimeFarameTrend " + intermediateTimeFarameTrend
                )
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                higherTimeFarameTrend = enumValues?.get(0).toString()
                Log.d(
                    TAG,
                    "default onItemSelected intermediateTimeFarameTrend " + intermediateTimeFarameTrend
                )
            }
        })
        spinner.adapter = adapter
    }


    var accountType: String? = null
    private fun declareIncomeTypeSpinner() {
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


    fun addAccountInfo(customerEntity: TradeAnalysis?, toUpdate: Boolean) {

        val customer = customerEntity?.let {
            TradeAnalysis(
                it.id,
                it.phoneUserName,

                it.tradeIncomeType,
                it.stockName,

                it.EntryPrice,
                it.SLPrice,
                it.ExitPrice,

                it.HTFLocation,
                it.HTFTrend,
                it.ITFTrend,
                it.ExecutionZone,
                it.entryEmotion,

                System.currentTimeMillis().toString(),
                it.note
            )
        }
        //            setEditTextValues(customer)
        //            toUpdate=false;
        if (toUpdate) {
            saveEntryViewModel.updateAccountDEtail(customer)
        } else {
            saveEntryViewModel.addAccountDEtail(customer)
        }
    }

    private fun clearEditTextValues() {
        binding.editTextStock.setText("")
        binding.todaysDate.setText("")
        stockName = ""
        institutionType = ""
        dateTime.timeInMillis = 0
        spinner?.setSelection(0)
    }


/*
    private fun setEditTextValues(balanceTx: BalanceTx) {
//        institutionType = binding.editTextInstituteType.text.toString()
        binding.editTextStock.setText(balanceTx.bankName)

//        binding.editTextPhoneUserName.setText(FirebaseUtil.getCurrentPhoneUser().toString() + "")

        binding.editTextInstituteCode.setText(balanceTx.accountType)
//        address = binding.editTextInstituteAddr.text.toString()
//
//        acNo = binding.editTextAcNo.text.toString()
//        netBankingUserName = binding.editTextNetBAnkingUserNAme.text.toString()
//        password = binding.editTextNetBAnkingPwrd.text.toString()
//        atmNo = binding.editTextAtmNo.text.toString()
//        atmPin = binding.editTextAtmPin.text.toString()


    }
*/

    private fun getEditTextValues(): TradeAnalysis {
        stockName = binding.editTextStock.text.toString()

        entryPrice = binding.editTextEntryPrice.text.toString()
        slPrice = binding.etSLPrice.text.toString()

        exitPrice = binding.etExitPrice.text.toString()
        note = binding.etNote.text.toString()

        /*  TradeAnalysis(
                it.id,
                it.phoneUserName,

                it.tradeIncomeType,
                it.stockName,

                it.EntryPrice,
                it.SLPrice,
                it.ExitPrice,

                it.HTFLocation,
                it.HTFTrend,
                it.ITFTrend,
                it.ExecutionZone,
                it.entryEmotion
                System.currentTimeMillis().toString(),
                it.note
            ) */
        return TradeAnalysis(
            "",
            FirebaseUtil.phoneUser.name,
            accountType!!,
            stockName,

            entryPrice,
            slPrice,
            exitPrice,

            higherTimeFarameLocation,
            higherTimeFarameTrend,
            intermediateTimeFarameTrend,
            excutionTimeTimeFarameLocation,
            entryEmotion,
            System.currentTimeMillis().toString(),
            note

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


    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Exiting " + this.getString(R.string.app_name))
            .setMessage("Are you sure you are done with your TRADING ?")
            .setPositiveButton(
                "Yes"
            ) { dialog, which ->

                val startStoredTime = TeaAgentsharedPreferenceUtil.getToPreferenceCurrentStartTime()
                val timediff = System.currentTimeMillis() - startStoredTime!!
                val timerlog = TimerLog("", startStoredTime, System.currentTimeMillis(), timediff)

                FirebaseUtil.addTimerLog(timerlog)
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }
}
