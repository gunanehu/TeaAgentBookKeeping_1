package com.teaagent.domain.firemasedbEntities

import java.io.Serializable

open class InstitutionEntity(
    var name: String?,
    var type: String,

    open var phoneUserName: String?,
    var institutionName: String?,
    var address: String?,

    open var netBankingUserName: String?,
    open var password: String?,
    open var atmNo: String?,
    open var atmPin: String?

) :
    Serializable {
    constructor() : this("", "", "", "", "", "", "", "", "")
}