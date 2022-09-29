package com.teaagent.domain.firemasedbEntities

import java.io.Serializable

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


}