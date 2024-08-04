package com.shohieb.pigeon.page.monthlytransaction

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shohieb.pigeon.cache.SharedPreferencesManager
import com.shohieb.pigeon.model.MoneyItem
import com.shohieb.pigeon.model.MoneyItemList
import com.shohieb.pigeon.model.MonthlyTransactionPageParameter
import com.shohieb.pigeon.ui.dialog.AddMoneyItemDialog
import com.shohieb.pigeon.ui.theme.ITEM_BACKGROUND_DEFAULT_PINK_CLICKED
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch

object MonthlyTransactionPage {
    const val NAME = "monthly_transaction_page"
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MonthlyTransactionPage(sharedPreferencesManager: SharedPreferencesManager, data: MonthlyTransactionPageParameter, onBackPressed: () -> Unit) {
    val viewModel: MonthlyTransactionViewModel = viewModel()

    var showDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }
    val lazyListState = rememberLazyListState()

    val shouldShowStickyRow by remember {
        derivedStateOf {

            val firstVisibleItemIndex = lazyListState.firstVisibleItemIndex
            val totalItemsCount = lazyListState.layoutInfo.totalItemsCount
            val lastVisibleItemIndex = firstVisibleItemIndex + lazyListState.layoutInfo.visibleItemsInfo.size - 1

            lastVisibleItemIndex < totalItemsCount - 1

        }
    }

    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    var isBackPressHandled by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    BackHandler {
        isBackPressHandled = true
        coroutineScope.launch {
            val moneyItemList = viewModel._items.toList()
            val objectMoneyitemList = MoneyItemList()
            objectMoneyitemList.addAll(moneyItemList)
            sharedPreferencesManager.saveTransactionList(data.date, objectMoneyitemList)
            awaitFrame()
            isBackPressHandled = false
            onBackPressed()
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .padding(it)
                    .padding(16.dp)
                    .fillMaxSize()) {

                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = data.formattedDate, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }

                item {
                    CategorySection(categoryCode = MoneyItem.CATEGORY_INCOME, viewModel = viewModel, sum = viewModel.incomeSum) {
                        selectedCategory = it
                        showDialog = true
                    }
                    CategorySection(categoryCode = MoneyItem.CATEGORY_EXPENSE, viewModel = viewModel, sum = viewModel.expensesSum)  {
                        selectedCategory =  it
                        showDialog = true
                    }
                    CategorySection(categoryCode = MoneyItem.CATEGORY_SAVING, viewModel = viewModel, viewModel.savingsSum)  {
                        selectedCategory = it
                        showDialog = true
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    TotalSection(balance = viewModel.balance)
                }
            }

            AnimatedVisibility(
                visible = shouldShowStickyRow,
                enter = expandIn(tween(durationMillis = 1000)),
                exit = shrinkOut(tween(durationMillis = 1000)),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(ITEM_BACKGROUND_DEFAULT_PINK_CLICKED)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)) {
                        TotalSection(balance = viewModel.balance)
                    }
                }
            }

            if (showDialog) {
                AddMoneyItemDialog(
                    onDismiss = { showDialog = false },
                    category = selectedCategory,
                    onAdd = { description, amount, category, notes ->
                        viewModel.addItem(description, amount, category, notes)
                    }
                )
            }

        }
    }

    LaunchedEffect(Unit) {
        viewModel.init(sharedPreferencesManager, data.date)
    }
}


@Composable
fun TotalSection(balance: Double) {
    Text(
        text = "Balance: MYR${"%.2f".format(balance)}",
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.SemiBold
    )
}
