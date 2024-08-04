package com.shohieb.pigeon.ui.dialog

import android.icu.text.CaseMap.Title
import androidx.compose.runtime.Composable

@Composable
fun DialogValidationCommon(
    title: @Composable (() -> Unit)? = null,
    onDismiss: () -> Unit,
    column: @Composable () -> Unit,
) {

}