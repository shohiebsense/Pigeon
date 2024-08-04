package com.shohieb.pigeon.page.monthlytransaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.shohieb.pigeon.model.MoneyItem
import com.shohieb.pigeon.ui.dialog.UpdateMoneyItemDialog
import com.shohieb.pigeon.ui.theme.ITEM_BACKGROUND_DEFAULT_PINK
import com.shohieb.pigeon.ui.theme.ITEM_BACKGROUND_DEFAULT_PINK_CLICKED


@Composable
fun CategorySection(
    categoryCode: String,
    viewModel: MonthlyTransactionViewModel,
    sum: Double,
    onCategoryClicked: (String) -> Unit
) {
    val items = viewModel._items.filter { it.category == categoryCode }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = MoneyItem.getCategoryLabel(categoryCode),
            style = MaterialTheme.typography.bodyLarge
        )

        items.forEach {
            MoneyItemCard(it) { updatedItem ->
                viewModel.updateItem(updatedItem)
            }
        }

        IconButton(
            colors = IconButtonDefaults.filledIconButtonColors(),
            onClick = {onCategoryClicked(categoryCode) }) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Money Item")

        }

        Text(
            text = "Total: MYR${"%.2f".format(sum)}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.End,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
        )

    }
}


@Composable
fun MoneyItemCard(item: MoneyItem, onUpdate: (MoneyItem) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var isCheckMarked by remember { mutableStateOf(item.isCheckMarked) }
    var isDescriptionClicked by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(
            containerColor = if (isDescriptionClicked) ITEM_BACKGROUND_DEFAULT_PINK_CLICKED else ITEM_BACKGROUND_DEFAULT_PINK
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isCheckMarked,
                        onCheckedChange = {
                            isCheckMarked = it
                            onUpdate(item.copy(isCheckMarked = it))
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = item.moneyItemTitle,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isDescriptionClicked) Color.White else Color.Unspecified,
                        modifier = Modifier.clickable {
                            isDescriptionClicked = !isDescriptionClicked
                        })
                }
                Text(
                    text = "MYR${"%.2f".format(item.amount)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Notes: ${item.notes}", style = MaterialTheme.typography.bodySmall)
            }

            if (isDescriptionClicked) {
                UpdateMoneyItemDialog(
                    onDismiss = { isDescriptionClicked = false },
                    item = item,
                ) { title,amount,notes ->
                    onUpdate(item.copy(moneyItemTitle = title, amount = amount, notes = notes))
                }
            }
        }
    }
}

