package de.comtec.uks.sunny.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import de.comtec.uks.sunny.R
import de.comtec.uks.sunny.core.model.WeatherUnit
import de.comtec.uks.sunny.ui.composables.CustomCard

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Settings(viewModel: SettingsViewModel = hiltViewModel()) {

    val unit by viewModel.unit.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.size(16.dp))
        CustomCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.use_imperial_units))
                Switch(
                    checked = unit == WeatherUnit.IMPERIAL, onCheckedChange = {
                        viewModel.updateUnit(
                            if (it) WeatherUnit.IMPERIAL else WeatherUnit.METRIC
                        )
                    })
            }
        }
    }

}


