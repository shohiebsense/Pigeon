package com.shohieb.pigeon.ui.textfield

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp

@Composable
fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    errorMessage: String? = "this one cannot be empty",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {

    val focusRequester = remember { FocusRequester() }
    var descriptionError by remember { mutableStateOf<String?>(null) }

    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        label = { Text("Description") },
        isError = isError,
        trailingIcon = {
            if (isError) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        },
        modifier = Modifier.focusRequester(focusRequester)
    )
}