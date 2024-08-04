package com.shohieb.pigeon.ui.dialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.shohieb.pigeon.model.MoneyItem

@Composable
fun UpdateMoneyItemDialog(
    onDismiss: () -> Unit,
    item: MoneyItem,
    onUpdate: (String, Double, String) -> Unit
) {
    var description by remember { mutableStateOf(item.moneyItemTitle) }
    var notes by remember { mutableStateOf(item.notes) }
    var amount by remember { mutableStateOf(item.amount.toString()) }

    val keyboard = LocalSoftwareKeyboardController.current

    val descriptionFocusRequester = remember { FocusRequester() }
    val amountFocusRequester = remember { FocusRequester() }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    var amountError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title =   { Text(text = "Edit Money Item: ${MoneyItem.getCategoryLabel(item.category)}") },
        text = {
            Column {

                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                        descriptionError = null
                    },
                    label = { Text("Description") },
                    isError = descriptionError != null,
                    trailingIcon = {
                        if (descriptionError != null) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    modifier = Modifier.focusRequester(descriptionFocusRequester)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it
                        amountError = null
                    },
                    label = { Text("Amount") },
                    isError = amountError != null,
                    trailingIcon = {
                        if (amountError != null) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    modifier = Modifier.focusRequester(amountFocusRequester),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {

                    if (description.isBlank()) {
                        descriptionError = "Description cannot be empty"
                        return@Button
                    }

                    val amountValue = amount.toDoubleOrNull()

                    if (amountValue == null) {
                        amountError = "Amount must be a valid number"
                        amountFocusRequester.requestFocus()
                        return@Button
                    }

                    keyboard?.hide()
                    onUpdate(description, amountValue, notes)
                    onDismiss()
                }
            ) {
                Text("Update")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}