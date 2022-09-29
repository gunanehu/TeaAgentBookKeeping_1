package com.teaagent.utility.exportfile

sealed class Exports {
    data class CSV(val csvConfig: CsvConfig) : Exports()
}