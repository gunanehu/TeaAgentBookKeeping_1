package com.teaagent.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
     val id: Long,

    @ColumnInfo val timestamp: Long,
    @ColumnInfo val customerName: String,
    @ColumnInfo val quantity: Long,
    @ColumnInfo val amount: Long,
    @ColumnInfo val totalamount: Long,

    @ColumnInfo val labourAmount: Long,
    @ColumnInfo val AdvancedPaymentAmount: Long,

    ) : Serializable{

    override fun toString(): String {
        return "Name : " + customerName + " | " +
                " Quantity : " + quantity + " | " +
                " amount : " + amount + " | " +
                " total : " + totalamount + " | " +
                " labourAmount : " + labourAmount + " | " +
                " Advance Payment: " + AdvancedPaymentAmount + " | " +
                " Date :" + timestamp
    }


    fun ConvertDate(timestamp: Long): String {
        val timeD = Date(timestamp)
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val time: String = sdf.format(timeD)
        return time
    }
}