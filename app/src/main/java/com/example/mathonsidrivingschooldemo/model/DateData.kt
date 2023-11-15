package com.example.mathonsidrivingschooldemo.model

import java.io.Serializable
import java.text.SimpleDateFormat

data class DateData (
    var dateId: String? = "",
    var dateString: String? = "",
    var dateForm: String? = ""
): Serializable