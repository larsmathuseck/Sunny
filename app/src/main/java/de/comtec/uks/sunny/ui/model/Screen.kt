package de.comtec.uks.sunny.ui.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import de.comtec.uks.sunny.R

sealed class Screen(val route: String, @StringRes val name: Int, val icon: ImageVector) {
    data object Main: Screen("main", R.string.screen_name_home, Icons.Filled.Home)
    data object Settings: Screen("settings", R.string.screen_name_settings, Icons.Filled.Settings)

    companion object {
        val screens = listOf(Main, Settings)
    }

}