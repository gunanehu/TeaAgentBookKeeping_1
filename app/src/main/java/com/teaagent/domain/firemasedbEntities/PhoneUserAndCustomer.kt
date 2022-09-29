package com.teaagent.domain.firemasedbEntities

import java.io.Serializable

data class PhoneUserAndCustomer(var phoneUserId: String, var customerId: String) :
    Serializable {

    constructor() : this("", "")
}
