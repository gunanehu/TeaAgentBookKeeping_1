package com.teaagent.domain.firemasedbEntities

data class FirebaseResponse(
    var products: List<InstitutionEntity>? = null,
    var exception: Exception? = null
)