package com.example.nhom_5_quan_li_chi_tieu.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = GreenIncome,
    secondary = GreenIncome.copy(alpha = 0.7f),
    tertiary = OrangeUpdate,
    background = DarkBackground,
    surface = Color(0xFF1E1E1E),
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    onBackground = White,
    onSurface = White,
    error = RedExpense,
    surfaceVariant = DarkBackground.copy(alpha = 0.8f),
    onSurfaceVariant = Color.LightGray,
     // Thêm 2 dòng này cho phần được chọn
    secondaryContainer = Color(0xFF388E3C).copy(alpha = 0.5f), // <-- VIÊN THUỐC TỐI
    onSecondaryContainer = Color.White // <-- CHỮ/ICON ĐƯỢC CHỌN (TỐI)
)

private val LightColorScheme = lightColorScheme(
    primary = GreenIncome,
    secondary = GreenIncome.copy(alpha = 0.7f),
    tertiary = OrangeUpdate,
    background = LightBackground,
    surface = White,
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    onBackground = Black,
    onSurface = Black,
    error = RedExpense,
    surfaceVariant = LightBackground.copy(alpha = 0.8f),
    onSurfaceVariant = Color.Gray,
     // Thêm 2 dòng này cho phần được chọn
    secondaryContainer = Color(0xFFE8F5E9), // <-- VIÊN THUỐC SÁNG
    onSecondaryContainer = Color(0xFF2E7D32) // <-- CHỮ/ICON ĐƯỢC CHỌN (SÁNG)
)

@Composable
fun BudgetBuddyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
