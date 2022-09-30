package com.teaagent.domain.firemasedbEntities

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class CollectionEntry(
//    var collectionEntryId: String,
    var quantity: Long,
    var amount: Long,
    var labourAmount: Long,
    var netTotal: Long,
    var timestamp: Long,
    var phoneUserName: String?,
    var customerName: String
) :
    Serializable{

    constructor() : this(/*"",*/ 0,0,0,0,0,"","")

    override fun toString(): String {
        return "Name : " + customerName + " | " +
//                " phoneUserName : " + phoneUserName + " | " +
                " Quantity : " + quantity + " | " +
                " amount : " + amount + " | " +
                " total : " + netTotal + " | " +
                " labourAmount : " + labourAmount + " | " +
                " Date :" + convertDate(timestamp)
    }


    fun convertDate(timestamp: Long): String {
        val timeD = Date(timestamp*(1000 * 60 * 60 * 24)) //as we were dividing with the same (1000 * 60 * 60 * 24), so now multiply
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val time: String = sdf.format(timeD)
        return time
    }
}