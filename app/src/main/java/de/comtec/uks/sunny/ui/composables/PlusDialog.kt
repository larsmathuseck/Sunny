package de.comtec.uks.sunny.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.comtec.uks.sunny.R


@Composable
fun PlusDialog(onDismiss: () -> Unit, onClickConfirm: (String) -> Unit) {
    var textInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_location)) },
        text = {
            Column {
                Text(stringResource(R.string.please_enter_the_location_name))
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    label = { Text(stringResource(R.string.location_name)) },
                    maxLines = 1,
                    singleLine = true,
                    placeholder = { Text(stringResource(R.string.e_g_london)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onClickConfirm(textInput)
                    onDismiss()
                }) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        })
}


@Preview
@Composable
fun PreviewPlusDialog() {
    PlusDialog(onDismiss = {}, onClickConfirm = {})
}

