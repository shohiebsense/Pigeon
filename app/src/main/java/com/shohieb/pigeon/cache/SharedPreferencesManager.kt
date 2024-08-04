package com.shohieb.pigeon.cache

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.shohieb.pigeon.model.MoneyItemList
import java.time.LocalDate

class SharedPreferencesManager(context: Context) {

    private val PREF_NAME = "AppPreferences"
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        const val KEY_MONTHLY_LIST = "MONTHLY_LIST"
        const val KEY_TRANSACTION_LIST = "TRANSACTION_LIST"
    }

    fun saveString(key: String, value: String) {
        editor.putString(key, value).commit()
    }

    fun removeTransaction(date: LocalDate) {
        editor.remove("${KEY_TRANSACTION_LIST}$date").commit()
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun <T> saveObject(key: String, obj: T) {
        val json = appGson.toJson(obj)
        Log.e("shohiebsense","json ${json}")
        saveString(key, json)
    }

    fun getTransactionList(date: LocalDate) : MoneyItemList {
        return getObject("${KEY_TRANSACTION_LIST}$date", MoneyItemList::class.java, MoneyItemList())
    }

    fun saveTransactionList(date: LocalDate, moneyItemList: MoneyItemList) {
        saveObject("${KEY_TRANSACTION_LIST}$date", moneyItemList)
    }

    fun <T> getObject(key: String, classOfT: Class<T>, defaultValue: T): T {
        val json = getString(key, "{}")
        return if (json != "{}") {
            appGson.fromJson(json, classOfT)
        } else {
            defaultValue
        }
    }


}
