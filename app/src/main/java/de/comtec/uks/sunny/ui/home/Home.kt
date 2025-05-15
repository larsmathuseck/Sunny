package de.comtec.uks.sunny.ui.home

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import de.comtec.uks.sunny.R
import de.comtec.uks.sunny.ui.composables.Background
import de.comtec.uks.sunny.ui.composables.CustomCard
import de.comtec.uks.sunny.ui.composables.LoadingCard
import de.comtec.uks.sunny.ui.composables.PlusCard
import de.comtec.uks.sunny.ui.composables.PlusDialog
import de.comtec.uks.sunny.ui.composables.WeatherCard
import kotlinx.coroutines.delay

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Home(
    snackbarHostState: SnackbarHostState, viewModel: HomeViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.uiState.collectAsState()
    val currentLocationCity = state.cities.firstOrNull { it.isHomeLocation }
    val currentLocationWeather =
        state.weather.firstOrNull { it.location == currentLocationCity?.location }
    var showPlusDialog by remember { mutableStateOf(false) }
    var isShowingLoading by remember { mutableStateOf(false) }
    var loadingStartTime by remember { mutableLongStateOf(0L) }
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_COARSE_LOCATION)

    // TODO: Handle no internet, no location provider, ..

    LaunchedEffect(Unit) {
        viewModel.errorFlow.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.fetchWeather()
            }
        }
        val lifecycle = lifecycleOwner.lifecycle
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(state.loading) {
        if (state.loading) {
            isShowingLoading = true
            loadingStartTime = System.currentTimeMillis()
        } else {
            val elapsedTime = System.currentTimeMillis() - loadingStartTime
            if (elapsedTime < 2_000) {
                delay(2_000 - elapsedTime)
            }
            isShowingLoading = false
        }
    }

    LaunchedEffect(permissionState.status) {
        if (permissionState.status == PermissionStatus.Granted) {
            viewModel.fetchWeather()
        }
    }

    val backgroundTemperature =
        currentLocationWeather?.temperature ?: state.weather.firstOrNull()?.temperature

    Background(
        temperature = backgroundTemperature,
        unit = state.unit,
        content = {
            Spacer(modifier = Modifier.size(16.dp))
            AnimatedVisibility(isShowingLoading) {
                LoadingCard(stringResource(R.string.loading_data))
            }
            if (permissionState.status != PermissionStatus.Granted) {
                CustomCard {
                    Text(
                        text = stringResource(R.string.location_permission_required),
                    )
                    Button(onClick = {
                        permissionState.launchPermissionRequest()
                    }) {
                        Text(stringResource(R.string.request_location_permission))
                    }
                }
            } else {
                if (state.weather.isEmpty()) {
                    LoadingCard(stringResource(R.string.locating))
                }
            }
            if (currentLocationWeather != null) {
                WeatherCard(
                    state.unit,
                    currentLocationWeather.location,
                    currentLocationWeather.iconUrl,
                    currentLocationWeather.timestamp,
                    currentLocationWeather.temperature,
                    currentLocationWeather.feelsLike,
                    currentLocationWeather.windSpeed,
                    currentLocationWeather.windDeg,
                    showDelete = false,
                )
            }
            state.weather.filter { it.location != currentLocationCity?.location }
                .forEach { weather ->
                    WeatherCard(
                        state.unit,
                        weather.location,
                        weather.iconUrl,
                        weather.timestamp,
                        weather.temperature,
                        weather.feelsLike,
                        weather.windSpeed,
                        weather.windDeg,
                        onClickDelete = {
                            viewModel.removeLocation(weather.location)
                        })
                }
            PlusCard(
                onClick = { showPlusDialog = true },
            )
            if (showPlusDialog) {
                PlusDialog(onDismiss = { showPlusDialog = false }, onClickConfirm = { location ->
                    viewModel.addLocation(location)
                    showPlusDialog = false
                })
            }
            Spacer(modifier = Modifier.size(16.dp))
        },
    )
}


