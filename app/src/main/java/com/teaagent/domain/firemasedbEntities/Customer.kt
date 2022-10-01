package com.teaagent.domain.firemasedbEntities

import java.io.Serializable

data class Customer(
//    var customerId: String?,
    var name: String?,
    var phoneUserName: String?,
    var AdditionalInfo: String?
):
    Serializable{
    constructor() : this(/*"", */"", "","")
}