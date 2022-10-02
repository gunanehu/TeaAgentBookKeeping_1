package com.teaagent.domain.firemasedbEntities

import java.io.Serializable

open class AccountSensitiveInfo(override var netBankingUserName: String?, override var password: String?, override var atmNo: String?,override var atmPin: String?) :AccountInfo(),
    Serializable {

    constructor() : this("", "","", "")
}
