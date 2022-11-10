package com.teaagent.ui.saveentry

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.teaagent.R
import com.teaagent.data.FirebaseUtil
import com.teaagent.database.TeaAgentsharedPreferenceUtil
import com.teaagent.databinding.ActivitySaveCustomerBinding
import com.teaagent.domain.firemasedbEntities.TimerLog
import com.teaagent.domain.firemasedbEntities.TradeAnalysis
import com.teaagent.domain.firemasedbEntities.enums.*
import com.teaagent.domain.firemasedbEntities.enums.exit.*
import com.teaagent.ui.listEntries.TradeListActivity
import util.GeneralUtils
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Main Screen
 */
class SaveAccountDetailActivity : AppCompatActivity() {

    private var toUpdate: Boolean = false
    val TAG: String = "SaveInstitutionActivity"

    private lateinit var stockName: String
//    private lateinit var institutionType: String  //

    private lateinit var phoneUserName: String  //= binding.editTextPhoneUserName.text.toString()
    private lateinit var entryPrice: String  //= binding.editTextInstituteCode.text.toString()
    private lateinit var slPrice: String  //= binding.editTextInstituteAddr.text.toString()

    private lateinit var exitPrice: String  //= binding.editTextAcNo.text.toString()
    private lateinit var note: String  //= binding.editTextNetBAnkingUserNAme.text.toString()
    private lateinit var exitNote: String  //= binding.editTextNetBAnkingPwrd.text.toString()
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

    var togglebutton: ToggleButton? = null

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
            setIntentExtraTradeDetailsData(balanceTx)
        }

        binding.todaysDate.setText("currentDate : " + GeneralUtils.convertDisplayDate(System.currentTimeMillis()))

        togglebutton = binding.toggleBuySell
        handleEntryExitLayoutVisibility(toUpdate)

        val phoneId: String? = TeaAgentsharedPreferenceUtil.getAppId()

        declareIncomeTypeSpinner()
        declareHigherTimeFarameLocationSpinner()
        declareHigherTimeFarameTrendSpinner()
        declareIntermediateTimeFarameTrendSpinner()
        declareExecutionTimeFarameSpinner()
        declareEntryEmotionTypeSpinner()
        declareSpinnerSLLevel()
        declareSpinnerTargetLevel()

        declareConfidenceLevelSpinner()
        declareMentalStateSpinner()
        declareMissedTradeSpinner()
        declareTradeExitPostAnalysisTypeSpinner()
        declareTradeManagementTypeSpinner()

        registerEditTextChangeListenerrs()

        if (toUpdate) {
            setProfitPercentage()
            setSLPercentage()

        }
        // Set up button click events
        binding.saveCustomerButton.setOnClickListener {
            if (binding.editTextStock.text.toString().length > 0) {
                val cuustomerEntry = getEditTextValues()
                if (toUpdate) {
                    cuustomerEntry.id = balanceTx!!?.id
                }
                currentEntryTime = System.currentTimeMillis().toString()
                cuustomerEntry.timestampTradePlanned = currentEntryTime

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

    private fun handleEntryExitLayoutVisibility(toUpdate: Boolean) {
        if (toUpdate) {
            layoutExitStock = binding.layoutExitStock
            layoutExitStock?.visibility = View.VISIBLE

            layoutEntryAnalysis = binding.layoutEntryAnalysis
            layoutEntryAnalysis?.visibility = View.VISIBLE
        } else {
            layoutExitStock = binding.layoutExitStock
            layoutExitStock?.visibility = View.GONE

            layoutEntryAnalysis = binding.layoutEntryAnalysis
            layoutEntryAnalysis?.visibility = View.GONE
        }
    }

    var currentEntryTime: String? = null;
    var currentExitTime: String? = null;

    var isBuy = false
    fun onToggleClick(view: View?) {
        if (togglebutton!!.isChecked) {
            isBuy = true
        } else {
            isBuy = false
        }
    }

    var layoutExitStock: CardView? = null
    var layoutEntryAnalysis: CardView? = null

    var isExit = false
    fun onExitStockClick(view: View?) {
        isExit = true
        currentExitTime = System.currentTimeMillis().toString()
        layoutExitStock?.visibility = View.VISIBLE
    }

    fun onEntryAnalysisClicked(view: View?) {
        layoutEntryAnalysis?.visibility = View.VISIBLE
    }


    private fun setIntentExtraTradeDetailsData(balanceTx: TradeAnalysis?) {

        binding.editTextStock.setText(balanceTx?.stockName)

        binding.editTextEntryPrice.setText(balanceTx?.EntryPrice)
        binding.etSLPrice.setText(balanceTx?.SLPrice)
        binding.etExitPrice.setText(balanceTx?.ExitPrice)

        binding.etNote.setText(balanceTx?.note)
        binding.etExitNote.setText(balanceTx?.exitNote)

        binding.toggleBuySell.isChecked = balanceTx?.isBuy!!
        isBuy = balanceTx.isBuy
    }

    private fun registerEditTextChangeListenerrs() {
        binding.etSLPrice.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                setSLPercentage()
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
                setSLPercentage()

            }
        })


        binding.etExitPrice.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                setProfitPercentage()
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
                setProfitPercentage()
            }
        })


    }

    private fun setSLPercentage() {
        val loss = calculateSLPercentage()
        binding.tvSLPercent.setText(loss.toString())
    }

    private fun setProfitPercentage() {
        val profit = calculateProfitPercentage()
        binding.tvProfitPercent.setText(profit.toString() + " %")

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


    var targetLevel: String? = null
    private fun declareSpinnerTargetLevel() {
        val spinner: Spinner = findViewById(R.id.spinnerExitLevel)
        val enumValues: List<Enum<*>> = ArrayList(
            EnumSet.allOf(
                TargetLevel::class.java
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
                targetLevel = enumValues?.get(position).toString()
                Log.d(TAG, "onItemSelected targetLevel " + targetLevel)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                targetLevel = enumValues?.get(0).toString()
                Log.d(
                    TAG,
                    "default onItemSelected targetLevel " + targetLevel
                )
            }
        })
        spinner.adapter = adapter

        if (toUpdate && !balanceTx?.targetLevel .isNullOrEmpty()) {
            val index: Int = TargetLevel.valueOf(balanceTx?.targetLevel.toString()).ordinal
            spinner.setSelection(index)
        }
    }


    var sLLevel: String? = null
    private fun declareSpinnerSLLevel() {
        val spinner: Spinner = findViewById(R.id.spinnerSLLevel)
        val enumValues: List<Enum<*>> = ArrayList(
            EnumSet.allOf(
                SLLevel::class.java
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
                sLLevel = enumValues?.get(position).toString()
                Log.d(TAG, "onItemSelected higherTimeFarameLocation " + sLLevel)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                sLLevel = enumValues?.get(0).toString()
                Log.d(
                    TAG,
                    "default onItemSelected higherTimeFarameLocation " + sLLevel
                )
            }
        })
        spinner.adapter = adapter

        if (toUpdate && !balanceTx?.sLLevel .isNullOrEmpty()) {
            val index: Int = SLLevel.valueOf(balanceTx?.sLLevel.toString()).ordinal
            spinner.setSelection(index)
        }
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

        if (toUpdate && !balanceTx?.HTFLocation .isNullOrEmpty()) {
            val index: Int = HTFLocationType.valueOf(balanceTx?.HTFLocation.toString()).ordinal
            spinner.setSelection(index)
        }
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

        if (toUpdate && ! balanceTx?.HTFTrend .isNullOrEmpty()) {
            val index: Int = TrendType.valueOf(balanceTx?.HTFTrend.toString()).ordinal
            spinner.setSelection(index)
        }
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
                excutionTimeTimeFarameLocation = enumValues?.get(0).toString()
                Log.d(
                    TAG,
                    "default onItemSelected excutionTimeTimeFarameLocation " + excutionTimeTimeFarameLocation
                )
            }
        })
        spinner.adapter = adapter

        if (toUpdate && !balanceTx?.ExecutionZone .isNullOrEmpty()) {
            val index: Int = ExecutionZoneType.valueOf(balanceTx?.ExecutionZone.toString()).ordinal
            spinner.setSelection(index)
        }
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
                entryEmotion = enumValues?.get(0).toString()
                Log.d(
                    TAG,
                    "default onItemSelected entryEmotion " + entryEmotion
                )
            }
        })
        spinner.adapter = adapter

        if (toUpdate && !balanceTx?.entryEmotion .isNullOrEmpty()) {
            val index: Int = EntryEmotionType.valueOf(balanceTx?.entryEmotion.toString()).ordinal
            spinner.setSelection(index)
        }
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
                intermediateTimeFarameTrend = enumValues?.get(0).toString()
                Log.d(
                    TAG,
                    "default onItemSelected intermediateTimeFarameTrend " + intermediateTimeFarameTrend
                )
            }
        })
        spinner.adapter = adapter

        if (toUpdate && !balanceTx?.ITFTrend .isNullOrEmpty()) {
            val index: Int = TrendType.valueOf(balanceTx?.ITFTrend.toString()).ordinal
            spinner.setSelection(index)
        }
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
//                institutionType = accountType as String
                Log.d(TAG, "onItemSelected accountType " + accountType)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                accountType = enumValues?.get(0).toString()
                Log.d(TAG, "default onItemSelected accountType " + accountType)

            }
        })

        spinner.adapter = adapter

        if (toUpdate && !balanceTx?.tradeIncomeType .isNullOrEmpty()) {
            val index: Int = TradeIncomeType.valueOf(balanceTx?.tradeIncomeType.toString()).ordinal
            spinner.setSelection(index)
        }
    }

/////spinner for exit


    var tradeManagementType: String? = null
    private fun declareTradeManagementTypeSpinner() {
        val spinner: Spinner = findViewById(R.id.spinnerTradeManagement)
        val enumValues: List<Enum<*>> = ArrayList(
            EnumSet.allOf(
                TradeManagementType::class.java
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
                tradeManagementType = enumValues?.get(position).toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                tradeManagementType = enumValues?.get(0).toString()
            }
        })
        spinner.adapter = adapter

        if (toUpdate && ! balanceTx?.tradeManagementType .isNullOrEmpty()) {
            val index: Int =
                TradeManagementType.valueOf(balanceTx?.tradeManagementType.toString()).ordinal
            spinner.setSelection(index)
        }
    }


    var tradeExitPostAnalysisTypeType: String? = null
    private fun declareTradeExitPostAnalysisTypeSpinner() {
        val spinner: Spinner = findViewById(R.id.spinnerTradeExitPostAnalysisType)
        val enumValues: List<Enum<*>> = ArrayList(
            EnumSet.allOf(
                TradeExitPostAnalysisType::class.java
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
                tradeExitPostAnalysisTypeType = enumValues?.get(position).toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                tradeExitPostAnalysisTypeType = enumValues?.get(0).toString()
            }
        })
        spinner.adapter = adapter

        if (toUpdate && !balanceTx?.tradeExitPostAnalysisTypeType .isNullOrEmpty()) {
            val index: Int =
                TradeExitPostAnalysisType.valueOf(balanceTx?.tradeExitPostAnalysisTypeType.toString()).ordinal
            spinner.setSelection(index)
        }
    }


    var missedTradeType: String? = null
    private fun declareMissedTradeSpinner() {
        val spinner: Spinner = findViewById(R.id.spinnerMissedTradeType)
        val enumValues: List<Enum<*>> = ArrayList(
            EnumSet.allOf(
                MissedTrade::class.java
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
                missedTradeType = enumValues?.get(position).toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                missedTradeType = enumValues?.get(0).toString()
            }
        })
        spinner.adapter = adapter

        if (toUpdate && !balanceTx?.missedTradeType.isNullOrEmpty()) {
            val index: Int = MissedTrade.valueOf(balanceTx?.missedTradeType.toString()).ordinal
            spinner.setSelection(index)
        }
    }


    var mentalState: String? = null
    private fun declareMentalStateSpinner() {
        val spinner: Spinner = findViewById(R.id.spinnerMentalState)
        val enumValues: List<Enum<*>> = ArrayList(
            EnumSet.allOf(
                MentalState::class.java
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
                mentalState = enumValues?.get(position).toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                mentalState = enumValues?.get(0).toString()
            }
        })
        spinner.adapter = adapter

        if (toUpdate && !balanceTx?.mentalState.isNullOrEmpty()) {
            val index: Int = MentalState.valueOf(balanceTx?.mentalState.toString()).ordinal
            spinner.setSelection(index)
        }
    }


    var confidenceLevel: String? = null
    private fun declareConfidenceLevelSpinner() {
        val spinner: Spinner = findViewById(R.id.spinnerConfidenceLevel)
        val enumValues: List<Enum<*>> = ArrayList(
            EnumSet.allOf(
                ConfidenceLevel::class.java
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
                confidenceLevel = enumValues?.get(position).toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                confidenceLevel = enumValues?.get(0).toString()
            }
        })
        spinner.adapter = adapter

        if (toUpdate && !balanceTx?.confidenceLevel.isNullOrEmpty() ) {
            val index: Int = ConfidenceLevel.valueOf(balanceTx?.confidenceLevel.toString()).ordinal
            spinner.setSelection(index)
        }
    }


    /*//spinnser for exit -end

*/
    fun addAccountInfo(customerEntity: TradeAnalysis?, toUpdate: Boolean) {

        val customer = customerEntity?.let {
            TradeAnalysis(
                it.id,
                it.phoneUserName,

                it.tradeIncomeType,
                it.stockName,
                it.isBuy,
                it.EntryPrice,
                it.SLPrice,
                it.ExitPrice,

                it.sLLevel,
                it.targetLevel,

                it.HTFLocation,
                it.HTFTrend,
                it.ITFTrend,
                it.ExecutionZone,
                it.entryEmotion,

                it.tradeManagementType,
                it.tradeExitPostAnalysisTypeType,
                it.missedTradeType,
                it.mentalState,
                it.confidenceLevel,
                it.exitNote,

                it.timestampTradePlanned,
                it.timestampTradeExited,
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

    /* todo
    private fun clearEditTextValues() {
         binding.editTextStock.setText("")
         binding.todaysDate.setText("")
         stockName = ""
         institutionType = ""
         dateTime.timeInMillis = 0
         spinner?.setSelection(0)
     }*/


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
        exitNote = binding.etExitNote.text.toString()
//spinnser for exit -end

        return TradeAnalysis(
            "",
            FirebaseUtil.getCurrentPhoneUser(
                TeaAgentsharedPreferenceUtil.getAppId().toString()
            ).name,
            accountType!!,
            stockName,
            isBuy,

            entryPrice,
            slPrice,
            exitPrice,

            sLLevel,
            targetLevel,

            higherTimeFarameLocation,
            higherTimeFarameTrend,
            intermediateTimeFarameTrend,
            excutionTimeTimeFarameLocation,
            entryEmotion,

            tradeManagementType,
            tradeExitPostAnalysisTypeType,
            missedTradeType,
            mentalState,
            confidenceLevel,
            exitNote,

            currentEntryTime,
            currentExitTime,
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


                val timediffVal = String.format(
                    "%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes(timediff),
                    TimeUnit.MILLISECONDS.toSeconds(timediff) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timediff))
                )
                Toast.makeText(this, "TIME SPENT " + timediffVal, Toast.LENGTH_LONG).show()
                FirebaseUtil.addTimerLog(timerlog)
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }
}
