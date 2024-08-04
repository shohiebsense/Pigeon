package com.shohieb.pigeon.model

import java.time.LocalDate

data class MonthlyTransactionPageParameter(
    val date: LocalDate,
    val formattedDate: String,
)
