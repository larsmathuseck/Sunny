package de.comtec.uks.sunny.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import de.comtec.uks.sunny.R
import de.comtec.uks.sunny.core.model.WeatherUnit
import de.comtec.uks.sunny.ui.theme.AppTypography
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

@Preview(showBackground = true)
@Composable
fun WeatherCard(
    unit: WeatherUnit = WeatherUnit.METRIC,
    location: String? = null,
    iconUrl: String? = null,
    timestamp: Long? = null,
    temperature: Double? = null,
    feelsLike: Double? = null,
    windSpeed: Double? = null,
    windDeg: Int? = null,
    showDelete: Boolean = true,
    onClickDelete: () -> Unit = {},
) {
    CustomCard {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = location ?: "?",
                fontSize = AppTypography.titleLarge.fontSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            if (showDelete) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.remove),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable { onClickDelete() })
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
        ) {
            // Weather icon
            iconUrl?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = stringResource(R.string.weather_icon),
                    modifier = Modifier.size(80.dp)
                )
            }

            // Temperature
            Text(
                text = "${temperature?.roundToInt() ?: "?"}°${if (unit == WeatherUnit.IMPERIAL) "F" else "C"}",
                fontSize = AppTypography.displayMedium.fontSize,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }

        // Feels like
        feelsLike?.let {
            Text(
                text = stringResource(R.string.feels_like_c, it.roundToInt()),
                fontSize = AppTypography.titleLarge.fontSize,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = stringResource(R.string.wind),
            fontSize = AppTypography.titleLarge.fontSize,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )

        // Wind speed
        Text(text = stringResource(R.string.speed_m_s, windSpeed?.let {
            String.format(Locale.getDefault(), "%.1f", it)
        } ?: "?"),
            fontSize = AppTypography.bodyLarge.fontSize,
            color = MaterialTheme.colorScheme.onSecondaryContainer)

        // Wind direction
        windDeg?.let { deg ->
            Text(
                text = stringResource(R.string.direction, deg, degToCompass(deg)),
                fontSize = AppTypography.bodyLarge.fontSize,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

        // Timestamp
        timestamp?.let {
            val formatted = remember(it) {
                SimpleDateFormat("HH:mm:ss", Locale.getDefault()).apply {
                    timeZone = TimeZone.getDefault()
                }.format(Date(it * 1000))
            }
            Text(
                modifier = Modifier.alpha(0.7f),
                text = stringResource(R.string.last_updated, formatted),
                fontSize = AppTypography.bodyMedium.fontSize,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
    }
}

private fun degToCompass(deg: Int): String {
    val directions = listOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
    val index = ((deg % 360) / 45.0).roundToInt() % 8
    return directions[index]
}