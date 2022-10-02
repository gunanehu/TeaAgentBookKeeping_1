package com.teaagent.domain.firemasedbEntities

import java.io.Serializable

open class AccountSensitiveInfo( var netBankingUserName: String?,  var password: String?,var atmNo: String?,  var atmPin: String?) :AccountInfo(),
    Serializable {

    constructor() : this("", "","", "")
}
