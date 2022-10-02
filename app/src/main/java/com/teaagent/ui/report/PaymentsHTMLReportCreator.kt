package com.teaagent.ui.report

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.teaagent.domain.firemasedbEntities.BalanceTx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.util.*

class PaymentsHTMLReportCreator internal constructor(
    var context: Context,
    private val customerName: String?
)
//{
//    private lateinit var text: String
//    var PAYMENT_SUMMARY_HEADER = "Payment Summary:"
//    var KEY_FLIGHT_DATE: String = " Date:"
//    var KEY_FLIGHT_NUMBER = "Customer Name:"
//    private val sectors: HashMap<String, String>? = null
//    private val TAG = "PaymentsReportPresenterImpl"
//
//    fun prepareDataForEmail(textFile: String) {
//        var reportFile = context.getExternalFilesDir("/")!!.absolutePath + "/temp"
//        val file = File(reportFile)
//        if (!file.exists()) {
//            file.mkdirs()
//            try {
//                file.createNewFile()
//            } catch (e: IOException) {
//                Log.e(TAG, " prepareDataForEmail IOException $e")
//            }
//        }
//        val fileName = "Report" + Date().time + ".html"
//        reportFile = "$reportFile/$fileName"
//        val subject = "Tea collection Transactions Report"
//        createFile(reportFile, textFile)
//        sendReport(subject, reportFile)
//    }
//
//    fun sendReport(subject: String?, reportFile: String) {
//        val fileUri = Uri.parse("file://$reportFile")
//        val file = File(fileUri.path)
//        val isEmailSent = FileUtil.sendMail(
//            context,
//            file,
//            subject,
//            "Please find the attached transactions report file for the CUSTOMER :" + customerName
//        )
//        if (!isEmailSent) {
//            Toast.makeText(context, "no_email_client", Toast.LENGTH_LONG)
//                .show()
//            Log.e(TAG, " no_email_client")
//        }
//    }
//
//    /**
//     * Create a file with email data to strFile path.
//     *
//     * @param strFile
//     */
//    private fun createFile(strFile: String, textFile: String) {
//        try {
//
//            /* val paymentReportData: String?
//              get() {
//                 return *//*commonCustomerDetails + *//*textFile
//            }*/
//
//            CoroutineScope(IO).launch {
////                getALLByCustomers()
//
//                OutputStreamWriter(FileOutputStream(strFile), StandardCharsets.UTF_8).use { out ->
//                    val data = textFile
//                    out.write(data)
//                }
//            }
//        } catch (e: Exception) {
//            Toast.makeText(context, "show File Creation Error " + e.message, Toast.LENGTH_LONG)
//                .show()
//            Log.e(TAG, "show File Creation Error " + e.message)
//        }
//    }
//
////     val paymentReportData: String?
////        public get() {
////            return /*commonCustomerDetails + */textFile
////        }
//
//    var textFile: String? = null
//
//    @JvmName("setTextFile1")
//    public fun setTextFile(textFile: String) {
//        this.textFile = textFile;
//        Log.i(TAG, "text " + textFile)
//    }
//
//
//    fun getCommonCustomerDetail(customerEntity: BalanceTx): BalanceTx {
//        val collectionEntry = BalanceTx(
//            customerEntity.quantity,
//            customerEntity.amount,
//            customerEntity.labourAmount,
//            customerEntity.netTotal,
//            customerEntity.timestamp,
//            customerEntity.convertedTimestampDate,
//            customerEntity.phoneUserName,
//            customerEntity.customerName
//        )
//        return customerEntity;
//    }
//
//    fun getCommonCustomerDetailsText(fli: BalanceTx?): String {
//        val sb = StringBuilder()
//        val reportHeader = ("<h1 style=\"text-align: center;\">"
//                + PAYMENT_SUMMARY_HEADER +
//                "</h1>")
//        val flightInfoHeader = H3STYLE_START + FLIGHT_INFOR_HEADER + H3STYLE_END
//        val tabId = "<style=\"text-align: left;\">" + fli?.customerName + "</>"
//        val breakLineStyle = "<br style=\"text-align: left;\">"
//        val timestamp = breakLineStyle + getFormattedStringForKeyValuePairs(
//            KEY_FLIGHT_DATE.trim { it <= ' ' },
//            (fli?.convertedTimestampDate.toString())
//        ) + "</>"
//        val flightNumber = breakLineStyle + getFormattedStringForKeyValuePairs(
//            KEY_FLIGHT_NUMBER.trim { it <= ' ' },
//            fli?.customerName.toString()
//        ) + "</>"
//        sb.append(flightNumber)
//        sb.append(reportHeader)
//        sb.append(flightInfoHeader)
//        sb.append(tabId)
//        sb.append(timestamp)
//        return sb.toString()
//    }
//
//
//    val H3STYLE_START = "<h3 style=\"text-align: left;\">"
//    val H3STYLE_END = "</h3>"
//    val TABLE_OPEN = "<table style = \"height: 43px;\" width = \"560\" ><tbody >"
//    val TABLE_CLOSE = "</tbody ></table >"
//    val ROW_BEGINNING = "<tr> <td width=\"50%\">"
//    val ROW_END = " </td ></tr>"
//    val ROW_END_NEW_ROW_50 = "</td><td width=\"50%\">"
//    private val FLIGHT = "flight"
//    private val FLIGHT_INFOR_HEADER = "Customer Details "
//    private val SALES_SUMMARY_HEADER = "Transaction details"
//    private fun getFormattedStringForKeyValuePairs(
//        key: String,
//        value: String
//    ): String {
//        return String.format("%s \t %s", key, value)
//    }
//
//    fun setSalesSummaryDataForShare(customerEntities: List<BalanceTx>): String {
//
//        val sb = StringBuilder()
//        val salesSummaryHeader = H3STYLE_START + SALES_SUMMARY_HEADER + H3STYLE_END
//        val breakAndNewCell = "</b></td><td width=\"25%\"><b>";
//        val headetTable: String = "<tr> " +
//                "<td width=\"25%\"><b>" + " Date " +
//                breakAndNewCell + " Quantity " +
//                breakAndNewCell + " Amount " +
//                breakAndNewCell + " Labour Amount " +
//                breakAndNewCell + "Net Total " +
//                breakAndNewCell + " Advanced Payment Amount  "
//        "</b>" + ROW_END;
//
//        var salesSummaryDataBuilder = StringBuilder()
//        for (collection in customerEntities) {
//            val time = collection.convertedTimestampDate.toString()
//            val getQuantity = collection.quantity.toString() + ""
//            val amount = collection.amount.toString() + ""
//            val getTotalamount = collection.netTotal.toString() + ""
//            val getLabourAmount = collection.labourAmount.toString() + ""
//
//            var salesSummaryDataRow: String
//
//            salesSummaryDataRow = ROW_BEGINNING +
//                    time + ROW_END_NEW_ROW_50 +
//                    getQuantity + ROW_END_NEW_ROW_50 +
//                    amount + ROW_END_NEW_ROW_50 +
//                    getLabourAmount + ROW_END_NEW_ROW_50 +
//                    getTotalamount + ROW_END_NEW_ROW_50 + ROW_END
//            salesSummaryDataBuilder.append(salesSummaryDataRow)
//        }
//        sb.append(salesSummaryHeader)
//        sb.append(TABLE_OPEN)
//        sb.append(headetTable)
//        sb.append(salesSummaryDataBuilder.toString())
//        sb.append(TABLE_CLOSE)
//        return sb.toString()
//
//    }
//
//
//}