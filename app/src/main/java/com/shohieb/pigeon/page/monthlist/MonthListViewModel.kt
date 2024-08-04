package com.shohieb.pigeon.page.monthlist

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shohieb.pigeon.cache.SharedPreferencesManager
import com.shohieb.pigeon.model.MoneyItem
import com.shohieb.pigeon.model.MonthlyMoneyItem
import com.shohieb.pigeon.model.MonthlyMoneyItemList
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class MonthListViewModel : ViewModel() {
    val _items = mutableStateListOf<MonthlyMoneyItem>()

    fun getItems(sharedPreferencesManager: SharedPreferencesManager): SnapshotStateList<MonthlyMoneyItem> {
        if (_items.isNotEmpty()) {
            return _items
        }

        val monthlymoneyItem = getMonthlyMoneyItemFromCurrentMonth()

        val monthlyMoneyItemList = MonthlyMoneyItemList()
        monthlyMoneyItemList.add(monthlymoneyItem)

        val list = sharedPreferencesManager.getObject(
            SharedPreferencesManager.KEY_MONTHLY_LIST,
            MonthlyMoneyItemList::class.java,
            monthlyMoneyItemList
        )

        if (list.size == 1) {
            sharedPreferencesManager.saveObject(SharedPreferencesManager.KEY_MONTHLY_LIST, list)
        }
        _items.clear()
        _items.addAll(list)
        return _items
    }

    fun add(sharedPreferencesManager: SharedPreferencesManager) {
        val list = sharedPreferencesManager.getObject(
            SharedPreferencesManager.KEY_MONTHLY_LIST,
            MonthlyMoneyItemList::class.java,
            MonthlyMoneyItemList()
        )

        if (list.isNotEmpty()) {
            val lastItem = list.last()
            val nextMonth = lastItem.date.plusMonths(1)
            val formattedDate = getDefaultFormattedDate(nextMonth)

            val newItem = MonthlyMoneyItem(date = nextMonth, formattedDate = formattedDate)
            list.add(newItem)
        } else {
            val newItem = getMonthlyMoneyItemFromCurrentMonth()
            list.add(newItem)
        }

        _items.clear()
        _items.addAll(list)
        sharedPreferencesManager.saveObject(SharedPreferencesManager.KEY_MONTHLY_LIST, list)
    }

    fun duplicate(
        sharedPreferencesManager: SharedPreferencesManager,
        monthlyMoneyItem: MonthlyMoneyItem
    ) {
        val lastItem = monthlyMoneyItem
        val nextMonth = lastItem.date.plusMonths(1)
        val formattedDate = getDefaultFormattedDate(nextMonth)

        val newItem = MonthlyMoneyItem(date = nextMonth, formattedDate = formattedDate)
        val lastItemTransactionList =
            sharedPreferencesManager.getTransactionList(lastItem.date)
        sharedPreferencesManager.saveTransactionList(newItem.date, lastItemTransactionList)

        _items.add(newItem)

        val monthlyMoneyItemList = MonthlyMoneyItemList()
        monthlyMoneyItemList.addAll(_items)
        sharedPreferencesManager.saveObject(
            SharedPreferencesManager.KEY_MONTHLY_LIST,
            monthlyMoneyItemList
        )
    }

    fun delete(
        sharedPreferencesManager: SharedPreferencesManager,
        monthlyMoneyItem: MonthlyMoneyItem
    ) {
        _items.remove(monthlyMoneyItem)
        val monthlyMoneyItemList = MonthlyMoneyItemList()
        monthlyMoneyItemList.addAll(_items)

        sharedPreferencesManager.removeTransaction(monthlyMoneyItem.date)
        sharedPreferencesManager.saveObject(
            SharedPreferencesManager.KEY_MONTHLY_LIST,
            _items
        )
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun getDefaultFormattedDate(date: LocalDate): String {
        val dateFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        val formattedDate = date.format(dateFormatter)
        return formattedDate
    }

    fun getMonthlyMoneyItemFromCurrentMonth(): MonthlyMoneyItem {
        val currentMonth = LocalDate.now().withDayOfMonth(1)
        val formattedDate = getDefaultFormattedDate(currentMonth)

        return MonthlyMoneyItem(date = currentMonth, formattedDate = formattedDate)
    }

}