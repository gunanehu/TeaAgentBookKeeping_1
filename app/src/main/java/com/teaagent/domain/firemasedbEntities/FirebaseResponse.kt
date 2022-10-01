package com.teaagent.domain.firemasedbEntities

data class FirebaseResponse(
    var products: List<Customer>? = null,
    var exception: Exception? = null
)