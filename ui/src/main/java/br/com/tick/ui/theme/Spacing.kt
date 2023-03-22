package br.com.tick.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.tick.R

data class Spacing(
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 12.dp,
    val large: Dp = 16.dp,
    val extraLarge: Dp = 20.dp
)

val LocalSpacing = compositionLocalOf { Spacing() }

val MaterialTheme.spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current

data class TeiraTextStyle(
    val h1: TextStyle = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 3.sp
    ),
    val h2: TextStyle = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        letterSpacing = 0.5.sp
    ),
    val h2bold: TextStyle = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        letterSpacing = 0.5.sp
    ),
    val h2small: TextStyle = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.5.sp
    ),
    val h3: TextStyle = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp
    ),
    val h3extra: TextStyle = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        letterSpacing = 0.5.sp
    ),
    val h3small: TextStyle = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.5.sp
    ),
    val h4: TextStyle = TextStyle(
        fontFamily = Mulish,
        fontWeight = FontWeight.ExtraLight,
        fontSize = 14.sp,
        letterSpacing = 0.5.sp
    )
)

val Mulish = FontFamily(
    Font(R.font.mulish_medium, FontWeight.Medium),
    Font(R.font.mulish_regular, FontWeight.Normal),
    Font(R.font.mulish_extra_light, FontWeight.Light),
    Font(R.font.mulish_semi_bold, FontWeight.SemiBold),
    Font(R.font.mulish_bold, FontWeight.Bold)
)

val LocalTeiraTextStyle = compositionLocalOf { TeiraTextStyle() }

val MaterialTheme.textStyle: TeiraTextStyle
    @Composable
    @ReadOnlyComposable
    get() = LocalTeiraTextStyle.current