package com.shohieb.pigeon.model

import java.time.LocalDate

data class MonthlyMoneyItem (
    val date: LocalDate,
    val formattedDate: String,
    val moneyItemList: MutableList<MoneyItem> = arrayListOf()
){
}

class MonthlyMoneyItemList : ArrayList<MonthlyMoneyItem>() {

}