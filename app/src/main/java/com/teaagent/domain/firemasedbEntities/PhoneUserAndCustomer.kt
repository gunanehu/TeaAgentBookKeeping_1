package com.teaagent.domain.firemasedbEntities

import java.io.Serializable

data class PhoneUserAndCustomer(var phoneUserName: String, var customerName: String) :
    Serializable {

    constructor() : this("", "")
}
