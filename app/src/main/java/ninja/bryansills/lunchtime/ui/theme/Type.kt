package ninja.bryansills.lunchtime.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ninja.bryansills.lunchtime.R

private val Manrope = FontFamily(
    Font(R.font.manrope_bold),
    Font(R.font.manrope_extrabold),
    Font(R.font.manrope_extralight),
    Font(R.font.manrope_light),
    Font(R.font.manrope_medium),
    Font(R.font.manrope_regular),
    Font(R.font.manrope_semibold),
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    // Used for list item title, pill button text, etc.
    bodyMedium = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.W700,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    // Used for list item subtitle, etc.
    labelSmall = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.W500,
        fontSize = 13.sp,
        lineHeight = 19.5.sp,
    ),
)