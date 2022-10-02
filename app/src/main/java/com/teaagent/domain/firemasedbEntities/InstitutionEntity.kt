package com.teaagent.domain.firemasedbEntities

import java.io.Serializable

 open class InstitutionEntity(
//    var customerId: String?,
     var name: String?,
     var type:String,
     open var phoneUserName: String?,
     var institutionCode: String?,
     var address: String?

):
    Serializable{
    constructor() : this(/*"", */"", "","","","")
}