package com.shohieb.pigeon.model

class MoneyItemList : ArrayList<MoneyItem>() {

}

data class MoneyItem(
    val id: String,
    var moneyItemTitle: String,
    var amount: Double,
    val category: String,
    var notes: String,
    var isCheckMarked : Boolean = false
) {
    companion object {
        const val CATEGORY_INCOME = "CATEOGRY_INCOME"
        const val CATEGORY_EXPENSE = "CATEOGRY_EXPENSE"
        const val CATEGORY_SAVING = "CATEOGRY_SAVING"

        fun getCategoryList() : List<String> = listOf("Income", "Expense", "Saving")

        fun getCategoryLabel(categoryCode : String) : String {
            return when(categoryCode) {
                CATEGORY_INCOME -> "Income"
                CATEGORY_EXPENSE -> "Expense"
                else -> "Saving"
            }
        }

        fun getCategoryByLabel(categoryLabel : String) : String {
            return when(categoryLabel) {
                "Income" -> CATEGORY_INCOME
                "Expense" -> CATEGORY_EXPENSE
                else -> CATEGORY_SAVING
            }
        }
    }
}
