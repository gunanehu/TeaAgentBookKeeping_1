package com.teaagent.domain.firemasedbEntities

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class BalanceTx(
    var id: String,

    var accountType: String,
    var accountNo: String,
    var balanceAmount: Long,
    var timestamp: Long,

    var phoneUserName: String?,
    var bankName: String
) :
    Serializable {

    constructor() : this("", "", "", 0, 0, "", "")

    override fun toString(): String {
        return " convertDate : " + convertDate(timestamp) +
                " bankName : " + bankName + " | " +
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