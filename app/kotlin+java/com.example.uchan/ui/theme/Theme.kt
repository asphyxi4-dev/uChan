package com.example.uchan.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Hey there! Welcome to our theme wonderland! ^^
// This is where all the cool color schemes live :3

// Vaporwave theme - For that sweet aesthetic feel!
private val VaporwaveColorScheme = darkColorScheme(
    primary = Color(0xFFFF71CE),      // Hot pink! So retro ^^
    secondary = Color(0xFF01CDFE),    // Cyber blue~
    tertiary = Color(0xFF05FFA1),     // Matrix green :3
    
    // Deep purple background - very aesthetic!
    background = Color(0xFF1A0933),
    
    // Different shades of purple for depth
    surface = Color(0xFF2D1B69),      // Not too dark, not too light ^^
    surfaceVariant = Color(0xFF4B0082),  // Royal purple~
    
    // Making sure text is readable and pretty :3
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color(0xFFFF71CE),  // Pink text on dark bg looks amazing!
    onSurface = Color(0xFF01CDFE),     // Cyber blue text ^^
    onSurfaceVariant = Color(0xFFDED5FF), // Light Purple text on variant surfaces
    
    // Container colors for interactive elements
    primaryContainer = Color(0xFF4B0082),  // Indigo for containers
    onPrimaryContainer = Color(0xFF01CDFE), // Cyan text on containers
    secondaryContainer = Color(0xFF2D1B69), // Mid Purple for secondary containers
    onSecondaryContainer = Color(0xFFFF71CE), // Pink text on secondary containers
    tertiaryContainer = Color(0xFF1A0933),  // Deep Blue-Purple for tertiary containers
    onTertiaryContainer = Color(0xFF05FFA1), // Neon Green text on tertiary containers,
    
    // Additional surface colors
    surfaceTint = Color(0xFF01CDFE),  // Cyan tint
    inverseSurface = Color(0xFF4B0082), // Indigo for inverse surfaces
    inverseOnSurface = Color(0xFFFF71CE), // Pink for text on inverse surfaces
    
    // Error colors with vaporwave theme
    error = Color(0xFFFF71CE),        // Pink for errors
    onError = Color.Black,
    errorContainer = Color(0xFF4B0082), // Indigo container for errors
    onErrorContainer = Color(0xFFFF71CE) // Pink text on error containers
)

// Tech theme - Inspired by Serial Experiments Lain! ^^
private val TechColorScheme = darkColorScheme(
    primary = Color(0xFF00FF00),      // Classic Matrix green :3
    secondary = Color(0xFF007ACC),    // Electric blue~
    tertiary = Color(0xFFFF3366),     // Neon pink
    
    // Main background with dark tech feel
    background = Color(0xFF0A0A0A),   // Near black
    
    // Surface colors with different tech-inspired hues
    surface = Color(0xFF121212),      // Dark gray
    surfaceVariant = Color(0xFF1A1A1A),  // Slightly lighter gray
    
    // Text colors
    onPrimary = Color(0xFF000000),    // Black text on primary
    onSecondary = Color(0xFF000000),  // Black text on secondary
    onTertiary = Color(0xFF000000),   // Black text on tertiary
    onBackground = Color(0xFF00FF00),  // Matrix green text on background
    onSurface = Color(0xFF00CC00),     // Darker green text on surface
    onSurfaceVariant = Color(0xFF009900), // Even darker green for variant surfaces
    
    // Container colors for interactive elements
    primaryContainer = Color(0xFF1A1A1A),  // Dark gray containers
    onPrimaryContainer = Color(0xFF00FF00), // Matrix green text on containers
    secondaryContainer = Color(0xFF121212), // Darker containers
    onSecondaryContainer = Color(0xFF007ACC), // Electric blue text
    tertiaryContainer = Color(0xFF0A0A0A),  // Near black containers
    onTertiaryContainer = Color(0xFFFF3366), // Neon pink text
    
    // Additional surface colors
    surfaceTint = Color(0xFF00FF00),  // Matrix green tint
    inverseSurface = Color(0xFF1A1A1A), // Dark gray for inverse
    inverseOnSurface = Color(0xFF00FF00), // Matrix green for inverse text
    
    // Error colors
    error = Color(0xFFFF3366),        // Neon pink for errors
    onError = Color(0xFF000000),      // Black text on error
    errorContainer = Color(0xFF1A1A1A), // Dark container for errors
    onErrorContainer = Color(0xFFFF3366) // Neon pink text on error containers
)

// 4chan theme - Keeping it classic! 
private val FourChanColorScheme = lightColorScheme(
    // The iconic orange and green combo ^^
    primary = Color(0xFFF04000),         // 4chan Orange
    secondary = Color(0xFF789922),       // 4chan Quote Green
    tertiary = Color(0xFF34345C),        // 4chan Link Blue
    
    // Main background with classic beige
    background = Color(0xFFF0E0D6),      // 4chan Classic Beige
    
    // Surface colors with subtle shadows
    surface = Color(0xFFFFFFFF),         // Pure White for posts
    surfaceVariant = Color(0xFFE8D6CC),  // Slightly darker beige for depth
    surfaceTint = Color(0x0D000000),     // Very subtle black tint for depth
    
    // Text colors
    onPrimary = Color(0xFFFFFFFF),       // White text on primary
    onSecondary = Color(0xFFFFFFFF),     // White text on secondary
    onTertiary = Color(0xFFFFFFFF),      // White text on tertiary
    onBackground = Color(0xFF800000),     // Classic Maroon text
    onSurface = Color(0xFF800000),        // Classic Maroon text
    onSurfaceVariant = Color(0xFF34345C), // Link blue for variant surfaces
    
    // Container colors with enhanced shadows
    primaryContainer = Color(0xFFF04000), // Orange containers
    onPrimaryContainer = Color(0xFFFFFFFF), // White text on orange
    secondaryContainer = Color(0xFFFAFAFA), // Slightly off-white for better shadow visibility
    onSecondaryContainer = Color(0xFF800000), // Maroon text on containers
    tertiaryContainer = Color(0xFFE8D6CC),   // Darker beige for depth
    onTertiaryContainer = Color(0xFF800000),  // Maroon text
    
    // Additional surface colors for depth
    inverseSurface = Color(0xFF800000),   // Maroon for inverse
    inverseOnSurface = Color(0xFFFFFFFF), // White text on inverse
    
    // Error colors
    error = Color(0xFFFF0000),           // Pure red for errors
    onError = Color(0xFFFFFFFF),         // White text on error
    errorContainer = Color(0xFFF0E0D6),   // Beige container
    onErrorContainer = Color(0xFFFF0000)  // Red text on error container
)

// Fallout theme - Watch out for radiation! ☢️
private val FalloutColorScheme = darkColorScheme(
    // Nuclear wasteland colors~
    primary = Color(0xFFFFD700),        // Vault-Tec gold ^^
    secondary = Color(0xFF7FFF00),      // Radioactive green!
    tertiary = Color(0xFFFF8C00),       // Dark Orange (radiation warning)
    
    // Main background with nuclear bunker feel
    background = Color(0xFF1A1A0E),     // Dark Gold-Tinted Black
    
    // Surface colors with radiation warning aesthetics
    surface = Color(0xFF2A2A15),        // Slightly lighter dark gold
    surfaceVariant = Color(0xFF3A3A20), // Gold variant
    
    // Text colors
    onPrimary = Color(0xFF000000),      // Black text on primary
    onSecondary = Color(0xFF000000),    // Black text on secondary
    onTertiary = Color(0xFF000000),     // Black text on tertiary
    onBackground = Color(0xFFFFD700),    // Gold text
    onSurface = Color(0xFFFFD700),       // Gold text
    onSurfaceVariant = Color(0xFF7FFF00), // Radioactive green for emphasis
    
    // Container colors for interactive elements
    primaryContainer = Color(0xFF2A2A15),    // Dark gold containers
    onPrimaryContainer = Color(0xFFFFD700),  // Gold text
    secondaryContainer = Color(0xFF1A1A0E),  // Darker containers
    onSecondaryContainer = Color(0xFF7FFF00),// Radioactive green text
    tertiaryContainer = Color(0xFF0E0E08),   // Darkest containers
    onTertiaryContainer = Color(0xFFFF8C00), // Warning orange text
    
    // Additional surface colors
    surfaceTint = Color(0xFFFFD700),     // Gold tint
    inverseSurface = Color(0xFF3A3A20),   // Dark gold for inverse
    inverseOnSurface = Color(0xFF7FFF00), // Radioactive green for inverse text
    
    // Error colors
    error = Color(0xFFFF6B00),           // Nuclear orange for errors
    onError = Color(0xFF000000),         // Black text on error
    errorContainer = Color(0xFF2A1500),   // Dark orange container
    onErrorContainer = Color(0xFFFF6B00)  // Nuclear orange text on error container
)

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

// Our theme wrapper - where the magic happens! ^^
@Composable
fun UChanTheme(
    theme: Theme = Theme.LIGHT,
    typography: androidx.compose.material3.Typography = androidx.compose.material3.Typography(),
    content: @Composable () -> Unit
) {
    // Pick the right color scheme based on the theme :3
    val colorScheme = when (theme) {
        Theme.DARK -> DarkColorScheme
        Theme.VAPORWAVE -> VaporwaveColorScheme  // A E S T H E T I C
        Theme.TECH -> TechColorScheme            // Present Day, Present Time... Ha ha ha!
        Theme.FOURCHAN -> FourChanColorScheme    // Classic vibes~
        Theme.FALLOUT -> FalloutColorScheme      // War... War never changes ^^
        Theme.LIGHT -> LightColorScheme
    }

    // Wrap everything in Material Theme goodness!
    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}

// Theme options enum - pick your favorite! :3
enum class Theme {
    LIGHT,      // Clean and bright
    DARK,       // Easy on the eyes ^^
    VAPORWAVE,  // Aesthetic vibes~
    TECH,       // Cyberpunk feels
    FOURCHAN,   // Classic style
    FALLOUT;    // Post-apocalyptic mood :3

    fun displayName(): String = when (this) {
        LIGHT -> "Light"
        DARK -> "Dark"
        VAPORWAVE -> "Vaporwave"  // A E S T H E T I C ^^
        TECH -> "Tech"
        FOURCHAN -> "4chan"
        FALLOUT -> "Fallout"      // War never changes~
    }
}