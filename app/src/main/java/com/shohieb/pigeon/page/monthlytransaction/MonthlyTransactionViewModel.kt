package com.shohieb.pigeon.page.monthlytransaction

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shohieb.pigeon.cache.SharedPreferencesManager
import com.shohieb.pigeon.model.MoneyItem
import com.shohieb.pigeon.model.MonthlyMoneyItem
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

class MonthlyTransactionViewModel: ViewModel() {
    val _items = mutableStateListOf<MoneyItem>()

    private val _incomeSum = mutableStateOf(0.0)
    val incomeSum: Double get() = _incomeSum.value

    private val _expensesSum = mutableStateOf(0.0)
    val expensesSum: Double get() = _expensesSum.value

    private val _savingsSum = mutableStateOf(0.0)
    val savingsSum: Double get() = _savingsSum.value

    private val _balance = mutableStateOf(0.0)
    val balance: Double get() = _balance.value

    fun init(sharedPreferencesManager: SharedPreferencesManager, localDate: LocalDate) {
        val list = sharedPreferencesManager.getTransactionList(localDate)
        _items.addAll(list)
        updateSumsAndBalance()
    }

    fun addItem(item: MoneyItem) {
        _items.add(item)
    }

    fun addItem(description: String, amount: Double, category: String, notes: String) {
        _items.add(MoneyItem(id = generateId(), moneyItemTitle = description, amount = amount, category = category , notes = notes, isCheckMarked = true))
        updateSumsAndBalance()
    }

    private fun generateId(): String {
        return (_items.size + 1).toString()
    }

    fun updateItem(updatedItem: MoneyItem) {
        val index = _items.indexOfFirst { it.id == updatedItem.id }
        if (index != -1) {
            _items[index] = updatedItem
        }
        updateSumsAndBalance()
    }


    fun getCategorySum(category: String): Double {
        return _items.filter { it.category == category && it.isCheckMarked }.sumOf { it.amount }
    }

    private fun updateSumsAndBalance() {
        _incomeSum.value = getCategorySum(MoneyItem.CATEGORY_INCOME)
        _expensesSum.value = getCategorySum(MoneyItem.CATEGORY_EXPENSE)
        _savingsSum.value = getCategorySum(MoneyItem.CATEGORY_SAVING)
        _balance.value = incomeSum + savingsSum - expensesSum
    }

    override fun onCleared() {
        super.onCleared()
    }

}