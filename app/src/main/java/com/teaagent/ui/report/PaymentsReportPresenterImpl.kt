package com.teaagent.ui.report

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.teaagent.domain.CustomerEntity
import com.teaagent.ui.listEntries.ListTransactionsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

class PaymentsReportPresenterImpl internal constructor(
    var context: Context,
    private val customerName: String?,
    private val mapsActivityViewModel: ListTransactionsViewModel
) {
    private lateinit var text: String
    var PAYMENT_SUMMARY_HEADER = "Payment Summary:"
    var KEY_FLIGHT_DATE = " Date:"
    var KEY_FLIGHT_NUMBER = "Customer Name:"
    private val sectors: HashMap<String, String>? = null
//    var data = mapsActivityViewModel.getALLByCustomerName("knj")

    fun prepareDataForEmail() {
        var reportFile = context.getExternalFilesDir("/")!!.absolutePath + "/temp"
        val file = File(reportFile)
        if (!file.exists()) {
            file.mkdirs()
            try {
                file.createNewFile()
            } catch (e: IOException) {
                Log.e(TAG, " prepareDataForEmail IOException $e")
            }
        }
        val fileName = "Report" + Date().time + ".html"
        reportFile = "$reportFile/$fileName"
        val subject = "Tea collection Transactions Report"
        createFile(reportFile)
        sendReport(subject, reportFile)
    }

    fun sendReport(subject: String?, reportFile: String) {
        val fileUri = Uri.parse("file://$reportFile")
        val file = File(fileUri.path)
        val isEmailSent = FileUtil.sendMail(context, file, subject, "Please find the attached transactions report file for the CUSTOMER :"+customerName)
        if (!isEmailSent) {
            Toast.makeText(context, "no_email_client", Toast.LENGTH_LONG)
                .show()
            Log.e(TAG, " no_email_client")
        }
    }

    /**
     * Create a file with email data to strFile path.
     *
     * @param strFile
     */
    private fun createFile(strFile: String) {
        try {
            CoroutineScope(IO).launch {
                OutputStreamWriter(FileOutputStream(strFile), StandardCharsets.UTF_8).use { out ->
                    val data = paymentReportData
                    out.write(data)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, "show File Creation Error " + e.message, Toast.LENGTH_LONG)
                .show()
            Log.e(TAG, "show File Creation Error " + e.message)
        }
    }

    //        return setFlightInfoDetailsForShare();
    public val paymentReportData: String
        public get() {
            return commonCustomerDetails + textFile
        }

    lateinit var textFile: String
    lateinit var commonCustomerDetails: String



    private fun getCommonCustomerDetail(customerEntity: CustomerEntity): CustomerEntity {
        val customerEntity = CustomerEntity(
            System.currentTimeMillis(),
            customerEntity.timestamp,
            customerEntity.customerName,
            customerEntity.quantity,
            customerEntity.amount,
            customerEntity.totalamount,
            customerEntity.labourAmount,
            customerEntity.AdvancedPaymentAmount
        )
        return customerEntity;
    }



    /**
     * Sets the flight details to the payment report based on fli
     *
     * @param fli
     * @return HTML string for flight details
    //     */
    private fun getCommonCustomerDetailsText(fli: CustomerEntity): String {
        val sb = StringBuilder()
        val reportHeader = ("<h1 style=\"text-align: center;\">"
                + PAYMENT_SUMMARY_HEADER +
                "</h1>")
        val flightInfoHeader = H3STYLE_START + FLIGHT_INFOR_HEADER + H3STYLE_END
        val tabId = "<style=\"text-align: left;\">" + fli.customerName + "</>"
        val breakLineStyle = "<br style=\"text-align: left;\">"
        val timestamp = breakLineStyle + getFormattedStringForKeyValuePairs(
            KEY_FLIGHT_DATE.trim { it <= ' ' },
            fli.ConvertDate(fli.timestamp)
        ) + "</>"
        val flightNumber = breakLineStyle + getFormattedStringForKeyValuePairs(
            KEY_FLIGHT_NUMBER.trim { it <= ' ' },
            fli.customerName
        ) + "</>"
        sb.append(flightNumber)
        sb.append(reportHeader)
        sb.append(flightInfoHeader)
        sb.append(tabId)
        sb.append(timestamp)
        return sb.toString()
    }

    fun convertDate(timestamp: Long): String {
        val timeD = Date(timestamp)
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val time: String = sdf.format(timeD)
        return time
    }

    /**
     * Sets the sales summary details to the payment report.
     *
     * @return HTML string for sales summary
     */
    private fun setSalesSummaryDataForShare(customerEntities: List<CustomerEntity>): String {
        val sb = StringBuilder()
        val salesSummaryHeader = H3STYLE_START + SALES_SUMMARY_HEADER + H3STYLE_END
//        val salesSummaryHeaderRow = ROW_BEGINNING +
//                "<b>" + " Date " +
//                "<b>" + " Quantity " +
//                "<b>" + " Amount " +
//                "<b>" + " Labour Amount " +
//                "<b>" + " Net Total " +
//                "<b>" + " Advanced Payment Amount  " +
//                "</b></td><td width=\"10%\"> <b>" + "revenue " + "</b>" + ROW_END

        val breakAndNewCell = "</b></td><td width=\"25%\"><b>";
        val headetTable: String = "<tr> " +
                "<td width=\"25%\"><b>" + " Date " +
                breakAndNewCell + " Quantity " +
                breakAndNewCell + " Amount " +
                breakAndNewCell + " Labour Amount " +
                breakAndNewCell + "Net Total " +
                breakAndNewCell + " Advanced Payment Amount  "
        "</b>"+ROW_END;


        val salesSummaryDataBuilder = StringBuilder()
        for ((_, time,name, quantity, amount, totalamount, labourAmount, AdvancedPaymentAmount) in customerEntities) {

            val time = convertDate(time) + ""
            val getQuantity = quantity.toString() + ""
            val amount = amount.toString() + ""
            val getTotalamount = totalamount.toString() + ""
            val getLabourAmount = labourAmount.toString() + ""
            val getAdvancedPaymentAmount = AdvancedPaymentAmount.toString() + ""
            var salesSummaryDataRow: String
            //            if (salesType.equalsIgnoreCase(paymentsReportView.getTotalString())) {
//                salesSummaryDataRow = ROW_BEGINNING + "<b>" + salesSummaryData.getSalesType()
//                        + "</b></td><td width=\"50%\"><b>" +
//                        salesSummaryData.getRevenue() + "</b>" + ROW_END;
//            } else {
            salesSummaryDataRow = ROW_BEGINNING +
                    time + ROW_END_NEW_ROW_50 +
                    getQuantity + ROW_END_NEW_ROW_50 +
                    amount + ROW_END_NEW_ROW_50 +
                    getLabourAmount + ROW_END_NEW_ROW_50 +
                    getTotalamount + ROW_END_NEW_ROW_50 +
                    getAdvancedPaymentAmount + ROW_END
            //            }
            salesSummaryDataBuilder.append(salesSummaryDataRow)
        }
        sb.append(salesSummaryHeader)
        sb.append(TABLE_OPEN)
        sb.append(headetTable)
        sb.append(salesSummaryDataBuilder.toString())
        sb.append(TABLE_CLOSE)
        return sb.toString()
    }
    /**
     * Sets the payment summary data details to the payment report.
     *
     * @return HTML string for payment summary data
     */
//    private String setPaymentSummaryDataForShare()
//    {
//
//        StringBuilder sb = new StringBuilder();
//
//        String paymentSummaryHeader = H3STYLE_START +PAYMENT_SUMMARY_HEADER + H3STYLE_END;
//        String cashPaymentHeader = "<h4 style=\"text-align: left;\"><u>"+CASH_TRANSACTIONS_HEADER+"</u></h4>";
//
//        val salesSummaryHeaderRow = ROW_BEGINNING +
//                "<b>" + " Date " +
//                "<b>" + " Quantity " +
//                "<b>" + " Amount " +
//                "<b>" + " Labour Amount " +
//                "<b>" + " Net Total " +
//                "<b>" + " Advanced Payment Amount  " +
//                "</b></td><td width=\"10%\"> <b>" + "revenue " + "</b>" + ROW_END
//        val salesSummaryDataBuilder = StringBuilder()
//
//        String currencyHeader = AppHelper . getInstance ().getContext()
//            .getString(R.string.currency_header);
//        String tenderHeader = AppHelper . getInstance ().getContext().getString(R.string.tender);
//        String changeHeader = AppHelper . getInstance ().getContext().getString(R.string.change);
//        String netHeader = AppHelper . getInstance ().getContext().getString(R.string.nt_amount);
//        String baseHeader = airlineProfile . getExchangeRates ().getBase().getCurrencyCode();
//        String breakAndNewCell = "</b></td><td width=\"25%\"><b>";
//
//        String paymentSummaryCashHeaderRow = "<tr> "
//        +"<td width=\"25%\"><b>" + currencyHeader
//        +breakAndNewCell + tenderHeader
//        +breakAndNewCell + changeHeader
//        +breakAndNewCell + netHeader + "</b>"
//        +breakAndNewCell + baseHeader + "</b>"
//        +ROW_END;
//
//        StringBuilder paymentStringBuilder = new StringBuilder();
//        for (PaymentData paymentData : getPaymentDataForReport()) {
//        String newCell = "</td><td width=\"25%\">";
//        String paymentDataRow = "<tr> <td width=\"25%\">"+paymentData.getCurrencyCode()
//        +newCell + paymentData.getAmountInTransactionCurrency()
//        +newCell + paymentData.getChangeInBaseCurrency()
//        +newCell + paymentData.getNetAmount()
//        +newCell + paymentData.getAmountInBaseCurrency()
//        +ROW_END;
//        paymentStringBuilder.append(paymentDataRow);
//    }
//
//
//        String cardPaymentHeader = "<h4 style=\"text-align: left;\"><u>"+CARD_TRANSACTIONS_HEADER+"</u></h4>";
//
//        String noOfCardPayments = AppHelper . getInstance ().getContext()
//            .getString(R.string.no_of_card_payments);
//        String totalAmnt = AppHelper . getInstance ().getContext().getString(R.string.total_amt);
//
//        String paymentSummaryCardHeaderRow = ROW_BEGINNING +"<b>" + noOfCardPayments +
//                "</b></td><td width=\"50%\"><b>" + totalAmnt + "</b>" + ROW_END;
//        String cardSummaryRow = ROW_BEGINNING +getCardSummary().getQuantity() +
//                ROW_END_NEW_ROW_50 + getCardSummary().getRevenue() + ROW_END;
//        sb.append(paymentSummaryHeader);
//        sb.append(cashPaymentHeader);
//        sb.append(TABLE_OPEN);
//        sb.append(paymentSummaryCashHeaderRow);
//        sb.append(paymentStringBuilder.toString());
//        sb.append(TABLE_CLOSE);
//
//
//        sb.append(cardPaymentHeader);
//        sb.append(TABLE_OPEN);
//        sb.append(paymentSummaryCardHeaderRow);
//        sb.append(cardSummaryRow);
//        sb.append(TABLE_CLOSE);
//
//        return sb.toString();
//
//    }

    /**
     * Sets the transaction summary data details to the payment report.
     *
     * @return HTML string for transaction summary data
     */
    //    private String setTransactionSummaryDataForShare() {
    //
    //        StringBuilder sb = new StringBuilder();
    //
    //        String transactionSummaryHeader = H3STYLE_START + TRANSATION_SUMMARY_HEADER + H3STYLE_END;
    //
    //        sb.append(transactionSummaryHeader);
    //        sb.append(TABLE_OPEN);
    //
    //        for (SalesSummaryData data : getTransactionSummary()) {
    //            sb.append(ROW_BEGINNING).append(data.getSalesType()).append(ROW_END_NEW_ROW_50).append(data.getQuantity()).append(ROW_END);
    //        }
    //
    //
    //        sb.append(TABLE_CLOSE);
    //        return sb.toString();
    //    }
    companion object {
        const val H3STYLE_START = "<h3 style=\"text-align: left;\">"
        const val H3STYLE_END = "</h3>"
        const val TABLE_OPEN = "<table style = \"height: 43px;\" width = \"560\" ><tbody >"
        const val TABLE_CLOSE = "</tbody ></table >"
        const val ROW_BEGINNING = "<tr> <td width=\"50%\">"
        const val ROW_END = " </td ></tr>"
        const val ROW_END_NEW_ROW_50 = "</td><td width=\"50%\">"
        private const val FLIGHT = "flight"
        private const val FLIGHT_INFOR_HEADER = "Customer Details "
        private const val SALES_SUMMARY_HEADER = "Transaction details"
        private const val TAG = "PaymentsReportPresenterImpl"
        private fun getFormattedStringForKeyValuePairs(
            key: String,
            value: String
        ): String {
            return String.format("%s \t %s", key, value)
        }
    }
}