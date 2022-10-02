package com.teaagent.domain.firemasedbEntities.uimappingentities

import java.io.Serializable

open class SaveAccountInfo(
    var id: String,
    var type: String,
    var bankName: String?,

    open var phoneUserName: String?,
    var institutionCode: String?,
    var address: String?,

    var acNo: String?,
    var netBankingUserName: String?,
    var password: String?,
    var atmNo: String?,
    var atmPin: String?

) :
    Serializable {
    constructor() : this("", "", "", "", "", "", "", "", "", "", "")
}