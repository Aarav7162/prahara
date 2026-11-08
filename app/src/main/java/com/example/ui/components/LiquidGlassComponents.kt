package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*

/**
 * Pure Monochrome Liquid Glass Card with specular rim-light highlight and soft diffuse ambient shadow.
 */
@Composable
fun IOSCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 24.dp,
    elevation: Dp = 3.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val shape = RoundedCornerShape(cornerRadius)

    val clickMod = if (onClick != null) {
        Modifier.clip(shape).clickable(onClick = onClick)
    } else {
        Modifier.clip(shape)
    }

    Box(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                spotColor = if (isDark) Color(0x33000000) else Color(0x180A1020),
                ambientColor = if (isDark) Color(0x1F000000) else Color(0x0C0A1020)
            )
            .background(liquidGlassCardBrush(isDark), shape)
            .border(
                BorderStroke(
                    width = 1.dp,
                    brush = liquidGlassRimLightBrush(isDark)
                ),
                shape = shape
            )
            .then(clickMod)
    ) {
        content()
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    elevation: Dp = 2.5.dp,
    borderColor: Color? = null,
    backgroundColor: Color? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val shape = RoundedCornerShape(cornerRadius)

    val clickMod = if (onClick != null) {
        Modifier.clip(shape).clickable(onClick = onClick)
    } else {
        Modifier.clip(shape)
    }

    val bgModifier = if (backgroundColor != null) {
        Modifier.background(backgroundColor, shape)
    } else {
        Modifier.background(liquidGlassCardBrush(isDark), shape)
    }

    val borderModifier = if (borderColor != null) {
        Modifier.border(1.dp, borderColor, shape)
    } else {
        Modifier.border(BorderStroke(1.dp, liquidGlassRimLightBrush(isDark)), shape)
    }

    Box(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                spotColor = if (isDark) Color(0x30000000) else Color(0x140A1020),
                ambientColor = if (isDark) Color(0x15000000) else Color(0x080A1020)
            )
            .then(bgModifier)
            .then(borderModifier)
            .then(clickMod)
    ) {
        content()
    }
}

@Composable
fun ParvaBadge(
    text: String,
    modifier: Modifier = Modifier,
    isMajorParva: Boolean = true
) {
    val isDark = isSystemInDarkTheme()
    val bg = if (isDark) Color(0xFF222836) else Color(0xFFE9ECF2)
    val textColor = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(if (isMajorParva) LiquidTextPrimary else Color(0xFF8E8E93))
        )
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/**
 * Top Parva Notification Card matching the user's reference image exactly:
 * Soft squircle icon on left with sacred motif,
 * Title "CHAUDAS TODAY •", line 1 "Green vegetables restricted /", line 2 "Fasting observance".
 */
@Composable
fun IOSParvaBannerCard(
    title: String,
    line1: String,
    line2: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val isDark = isSystemInDarkTheme()

    IOSCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("parva_banner_card"),
        elevation = 3.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Frosted Squircle Icon Container with Prahara App Logo
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(if (isDark) Color(0xFF222938) else Color(0xFFE8ECF2))
                    .border(
                        1.dp,
                        if (isDark) Color(0xFF333D50) else Color(0xFFDCE1E9),
                        RoundedCornerShape(15.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_app_logo),
                    contentDescription = "Prahara Logo",
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(11.dp))
                )
            }

            // Right text column
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary,
                    letterSpacing = 0.3.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = line1,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (isDark) LiquidDarkTextSecondary else Color(0xFF334155)
                )
                Text(
                    text = line2,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (isDark) LiquidDarkTextSecondary else Color(0xFF334155)
                )
            }
        }
    }
}

/**
 * Split Hero Card matching reference:
 * Left side: "Navkarsi in / 24m 12s"
 * Center: Hairline vertical divider
 * Right side: "Local Tithi / Kartik Sud Chaudas / Sunrise-Sunset / 06:42 AM - 05:55 PM"
 */
@Composable
fun IOSHeroSplitCard(
    milestoneName: String,
    countdownFormatted: String,
    tithiFullName: String,
    sunriseSunsetRange: String,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()

    IOSCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("hero_split_card"),
        elevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Column: Countdown
            Column(
                modifier = Modifier.weight(1.18f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = milestoneName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = countdownFormatted,
                    fontSize = 29.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary,
                    letterSpacing = (-0.6).sp
                )
            }

            // Vertical subtle divider
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(54.dp)
                    .background(if (isDark) Color(0xFF262E3E) else Color(0xFFE2E6ED))
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Right Column: Local Tithi and Sunrise-Sunset
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Local Tithi",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    color = LiquidTextSecondary
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = tithiFullName,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Sunrise–Sunset",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    color = LiquidTextSecondary
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = sunriseSunsetRange,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                )
            }
        }
    }
}

/**
 * 3-Column Timing Card matching the reference image.
 * Active card displays a prominent dark border outline (as seen on Navkarsi in the reference).
 */
@Composable
fun IOSTimingCard(
    title: String,
    timeFormatted: String,
    icon: ImageVector,
    progressFraction: Float,
    isCurrentActive: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val shape = RoundedCornerShape(22.dp)

    val borderStroke = if (isCurrentActive) {
        BorderStroke(1.5.dp, if (isDark) LiquidDarkBorderActive else LiquidGlassBorderActive)
    } else {
        BorderStroke(1.dp, liquidGlassRimLightBrush(isDark))
    }

    Box(
        modifier = modifier
            .aspectRatio(0.86f)
            .shadow(
                elevation = if (isCurrentActive) 4.5.dp else 2.5.dp,
                shape = shape,
                spotColor = if (isDark) Color(0x35000000) else Color(0x180A1020),
                ambientColor = if (isDark) Color(0x18000000) else Color(0x0C0A1020)
            )
            .background(liquidGlassCardBrush(isDark), shape)
            .border(borderStroke, shape)
            .clip(shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 10.dp)
            .testTag("timing_card_${title.lowercase().replace(" ", "_").replace("-", "_")}"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            // Frosted Circular Glyph Pill
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(if (isDark) Color(0xFF222938) else Color(0xFFE8ECF2))
                    .border(
                        0.8.dp,
                        if (isDark) Color(0xFF333D50) else Color(0xFFDCE1E9),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isDark) Color(0xFFCBD5E1) else Color(0xFF475569),
                    modifier = Modifier.size(19.dp)
                )
            }

            // Title
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary,
                textAlign = TextAlign.Center,
                maxLines = 1
            )

            // Large Time Digits (e.g. 06:42, 07:30, 17:55)
            Text(
                text = timeFormatted,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary,
                letterSpacing = (-0.4).sp,
                textAlign = TextAlign.Center
            )

            // Mini Capsule Progress Indicator
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.78f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (isDark) LiquidDarkProgressTrack else LiquidProgressTrack)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progressFraction.coerceIn(0f, 1f))
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (isDark) LiquidDarkProgressFill else LiquidProgressFill)
                )
            }
        }
    }
}

/**
 * Floating iOS-style bottom navigation bar matching the reference image.
 * Rounded translucent capsule pill floating above content with specular highlight rim.
 */
@Composable
fun IOSFloatingBottomBar(
    currentTab: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val shape = RoundedCornerShape(36.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 24.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 16.dp,
                    shape = shape,
                    spotColor = if (isDark) Color(0x44000000) else Color(0x280A1020),
                    ambientColor = if (isDark) Color(0x22000000) else Color(0x140A1020)
                )
                .clip(shape)
                .background(
                    if (isDark) Color(0xF2161B26) else Color(0xF5FFFFFF),
                    shape
                )
                .border(
                    BorderStroke(
                        1.dp,
                        if (isDark) {
                            Brush.verticalGradient(listOf(Color(0xFF384252), Color(0x22FFFFFF)))
                        } else {
                            Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.95f), Color(0x28000000)))
                        }
                    ),
                    shape = shape
                )
                .padding(vertical = 6.dp, horizontal = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IOSNavItem(
                    label = "Home",
                    icon = Icons.Filled.Home,
                    isSelected = currentTab == "home",
                    testTag = "bottom_nav_home",
                    onClick = { onTabSelected("home") }
                )

                IOSNavItem(
                    label = "Calendar",
                    icon = Icons.Filled.CalendarMonth,
                    isSelected = currentTab == "calendar",
                    testTag = "bottom_nav_calendar",
                    onClick = { onTabSelected("calendar") }
                )

                IOSNavItem(
                    label = "Settings",
                    icon = Icons.Filled.Settings,
                    isSelected = currentTab == "settings",
                    testTag = "bottom_nav_settings",
                    onClick = { onTabSelected("settings") }
                )
            }
        }
    }
}

@Composable
private fun IOSNavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val activeColor = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
    val inactiveColor = if (isDark) Color(0xFF64748B) else Color(0xFF8E8E93)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 4.dp)
            .testTag(testTag)
    ) {
        // Icon with dark circular halo backdrop when active (matching screenshot)
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) {
                        if (isDark) Color(0x33FFFFFF) else Color(0x14000000)
                    } else Color.Transparent
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) activeColor else inactiveColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(activeColor)
                )
            }
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) activeColor else inactiveColor
            )
        }
    }
}
