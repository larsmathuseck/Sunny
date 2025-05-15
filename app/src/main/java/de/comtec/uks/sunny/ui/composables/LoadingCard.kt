package de.comtec.uks.sunny.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun LoadingCard(text: String = "") {
    CustomCard {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                16.dp, Alignment.CenterHorizontally
            )
        ) {
            Text(text, color = MaterialTheme.colorScheme.secondary)
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                trackColor = MaterialTheme.colorScheme.tertiary,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}
