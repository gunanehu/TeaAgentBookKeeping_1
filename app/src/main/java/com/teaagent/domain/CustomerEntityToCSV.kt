package com.teaagent.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.opencsv.bean.CsvBindByName

@Entity
data class CustomerEntityToCSV(
    @CsvBindByName(column = "timestamp")
    val timestamp: Long,

    @CsvBindByName(column = "customerName")
    @ColumnInfo val customerName: String,

    @CsvBindByName(column = "quantity")
    @ColumnInfo val quantity: Long,

    @CsvBindByName(column = "amount")
    @ColumnInfo val amount: Long,

    @CsvBindByName(column = "totalamount")
    @ColumnInfo val totalamount: Long,

    @CsvBindByName(column = "labourAmount")
    @ColumnInfo val labourAmount: Long,

    @CsvBindByName(column = "AdvancedPaymentAmount")
    @ColumnInfo val AdvancedPaymentAmount: Long,

    ) : Exportable

fun List<CustomerEntity>.toCsv(): List<CustomerEntityToCSV> = map {
    CustomerEntityToCSV(
        timestamp = it.timestamp,
        customerName = it.customerName,
        quantity = it.quantity,
        amount = it.amount,
        totalamount = it.totalamount,
        labourAmount = it.labourAmount,
        AdvancedPaymentAmount = it.AdvancedPaymentAmount
    )
}



