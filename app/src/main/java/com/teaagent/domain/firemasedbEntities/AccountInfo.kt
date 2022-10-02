package com.teaagent.domain.firemasedbEntities

import java.io.Serializable

open class AccountInfo(override var phoneUserName: String?, open var accountNo: String?) :InstitutionEntity(),
    Serializable {

    constructor() : this("", "")
}
