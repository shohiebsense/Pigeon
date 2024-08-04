package com.shohieb.pigeon.page.monthlist

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shohieb.pigeon.R
import com.shohieb.pigeon.cache.SharedPreferencesManager
import com.shohieb.pigeon.model.MonthlyMoneyItem
import com.shohieb.pigeon.model.MonthlyTransactionPageParameter
import com.shohieb.pigeon.ui.theme.ITEM_BACKGROUND_DEFAULT_PINK
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object MonthListPage {
    const val NAME = "month_list_page"
}

@Composable
fun MonthListPage(sharedPreferencesManager: SharedPreferencesManager, onDetailClick: (MonthlyTransactionPageParameter) -> Unit) {

    val viewmodel = MonthListViewModel()

    Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
        FloatingActionButton(onClick = {
            viewmodel.add(sharedPreferencesManager)
        }) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add",
            )
        }
    }) { itemPadding ->
        Column(modifier = Modifier.padding(itemPadding)) {
            Header(image = painterResource(id = R.drawable.moonflower))
            LazyColumn {
                items(viewmodel.getItems(sharedPreferencesManager)) { item ->
                    MonthYearListItem(
                        item = item,
                        onDetailClick = {

                            val parameter = MonthlyTransactionPageParameter(
                                date = item.date,
                                formattedDate = item.formattedDate
                            )
                            onDetailClick(parameter)
                        },
                        onDeleteClick = {
                            viewmodel.delete(sharedPreferencesManager, item)
                        },
                        onCopyClick = {
                            viewmodel.duplicate(sharedPreferencesManager, item)
                        }
                    )
                }
            }
        }
    }


}


@Composable
fun Header(image: Painter) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = image,
            contentDescription = "Header Image",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(Color.Gray)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Chendy's",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
fun MonthYearListItem(
    item: MonthlyMoneyItem,
    onDetailClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCopyClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(ITEM_BACKGROUND_DEFAULT_PINK)
            .clickable(onClick = onDetailClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.formattedDate,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.width(8.dp))
        Row {
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
            IconButton(onClick = onCopyClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_content_copy),
                    contentDescription = "Copy"
                )
            }
        }
    }
}