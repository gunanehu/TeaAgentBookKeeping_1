package com.teaagent.domain.firemasedbEntities

import java.io.Serializable

data class TimerLog(
    var id: String,
    var startInMilli: Long?,
    var stopInMilli: Long?,
    val timediff: Long?
): Serializable