package com.teaagent.domain.firemasedbEntities

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class BalanceTx(
    var id: String,

    var accountType: String,
    var accountNo: String,
    var balanceAmount: String,
    var timestamp: String,

    var phoneUserName: String?,
    var bankName: String
) :
    Serializable {

    constructor() : this("", "", "", "", "", "", "")

    override fun toString(): String {
         /*" convertDate : " + convertDate(timestamp) +*/
        return         " bankName : " + bankName + " | " +
                "| accountType : " + accountType + " " +
                " accountNo : " + accountNo + " - " +
                " balanceAmount : " + balanceAmount + " "


    }

    companion object {
        fun convertDate(timestamp: Long): String {
            val timeD =
                Date(timestamp)
            val sdf = SimpleDateFormat("dd/M/yyyy")
            val time: String = sdf.format(timeD)
            return time
        }
    }
}