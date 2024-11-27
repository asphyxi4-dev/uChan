package com.example.uchan.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.uchan.R

// Hey there! These are our font families :3
// We start with good ol' Roboto as our default
val RobotoFamily = FontFamily.Default

// Here's our cool MS-DOS style font! Perfect for that retro feel ^^
val DosFamily = FontFamily(
    Font(R.font.perfect_dos_vga, FontWeight.Normal)
)

/* Keeping these commented out until we add the font files!
   Will add more fancy fonts soon :)

val UbuntuFamily = FontFamily(
    Font(R.font.ubuntu_regular),
    Font(R.font.ubuntu_medium, FontWeight.Medium),
    Font(R.font.ubuntu_bold, FontWeight.Bold)
)

val FiraCodeFamily = FontFamily(
    Font(R.font.firacode_regular),
    Font(R.font.firacode_medium, FontWeight.Medium),
    Font(R.font.firacode_bold, FontWeight.Bold)
)

val ComicNeueFamily = FontFamily(
    Font(R.font.comicneue_regular),
    Font(R.font.comicneue_bold, FontWeight.Bold)
)
*/

// This is where we define all our font options! ^^
// Each option has its own font family and display name
object Type {
    enum class FontOption(val family: FontFamily) {
        ROBOTO(RobotoFamily),    // Clean and modern
        DOS(DosFamily),          // For that sweet retro feel :3
        // These will use Roboto for now until we add their files
        UBUNTU(RobotoFamily),    // Friendly and round
        FIRA_CODE(RobotoFamily), // Perfect for code
        COMIC_NEUE(RobotoFamily);// Fun and casual

        // This is what shows up in the font picker!
        fun displayName(): String = when (this) {
            ROBOTO -> "Roboto"
            DOS -> "MS-DOS"      // Bringing back the 90s ^^
            UBUNTU -> "Ubuntu"
            FIRA_CODE -> "Fira Code"
            COMIC_NEUE -> "Comic Neue"
        }
    }
}

// Here's where the magic happens! This function creates all our text styles
// with the chosen font family. It's like a font makeover for the whole app! :3
fun getTypography(fontFamily: FontFamily = FontFamily.Default): Typography = Typography(
    // Big headlines and titles
    displayLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp  // Negative spacing makes it look fancy ^^
    ),
    // Medium-sized display text
    displayMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    // ... other styles with their own personality ...

    // The smallest text - perfect for tiny details :3
    labelSmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)