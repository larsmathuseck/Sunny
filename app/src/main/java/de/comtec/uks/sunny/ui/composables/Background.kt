package de.comtec.uks.sunny.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.tooling.preview.Preview
import de.comtec.uks.sunny.core.model.WeatherUnit

@Preview
@Composable
fun Background(
    modifier: Modifier = Modifier,
    temperature: Double? = null,
    content: @Composable () -> Unit = {},
    unit: WeatherUnit = WeatherUnit.METRIC,
) {
    val baseModifier = modifier.fillMaxSize()

    val gradientModifier = temperature?.let { tempRaw ->
        val tempInCelsius = when (unit) {
            WeatherUnit.IMPERIAL -> (tempRaw - 32) * 5.0 / 9.0
            else -> tempRaw
        }

        val startColor = when {
            tempInCelsius <= 0 -> Color(0xFF2196F3)
            tempInCelsius >= 30 -> Color(0xFFFF5722)
            else -> lerp(Color(0xFF2196F3), Color(0xFFFF5722), (tempInCelsius / 30f).toFloat())
        }

        val endColor = when {
            tempInCelsius <= 0 -> Color(0xFF03A9F4)
            tempInCelsius >= 30 -> Color(0xFFFF9800)
            else -> lerp(Color(0xFF03A9F4), Color(0xFFFF9800), (tempInCelsius / 30f).toFloat())
        }

        baseModifier.background(Brush.verticalGradient(listOf(startColor, endColor)))
    } ?: baseModifier // No background if temperature is null

    LazyColumn(
        modifier = gradientModifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            content()
        }
    }
}