package ninja.bryansills.lunchtime.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// TODO: validate all of these...
private val BasicColorScheme = lightColorScheme(
    primary = Color(0xFF2B381F),
    secondary = Color(0xFF2B381F),
    tertiary = Color(0xFF656E5E),
    primaryContainer = Color(0xFFFFFFFF),
    background = Color(0xFFEFEFEC),
)

@Composable
fun LunchtimeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = BasicColorScheme,
        typography = Typography,
        content = content
    )
}