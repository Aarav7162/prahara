package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.R
import com.example.model.DefaultLocations
import com.example.ui.components.GlassCard
import com.example.ui.components.IOSCard
import com.example.ui.theme.*
import com.example.viewmodel.JainViewModel

@Composable
fun SettingsScreen(
    viewModel: JainViewModel,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val context = LocalContext.current

    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val eveningAlertEnabled by viewModel.eveningAlertEnabled.collectAsState()
    val morningAlertEnabled by viewModel.morningAlertEnabled.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    val preferredLanguage by viewModel.preferredLanguage.collectAsState()
    val speechRate by viewModel.audioEngine.speechRate.collectAsState()

    val upiId = "8850783566@fam"

    // Android 13+ Notification Permission Launcher
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.toggleNotifications(isGranted)
    }

    // Location Permission Launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            viewModel.autoDetectLocation()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(liquidGlassBackgroundBrush(isDark))
            .testTag("settings_screen")
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Settings & Preferences",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Prahara App Brand Card
        IOSCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF222328))
                        .border(1.dp, if (isDark) Color(0xFF333E52) else Color(0xFFD6DBE4), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_app_logo),
                        contentDescription = "Prahara Logo",
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Prahara",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                    )
                    Text(
                        text = "Jain Daily Pachakkhan & Tithi Tracker",
                        fontSize = 12.sp,
                        color = LiquidTextSecondary
                    )
                    Text(
                        text = "Version 1.0 • Liquid Glass Edition",
                        fontSize = 11.sp,
                        color = LiquidTextTertiary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // NOTIFICATIONS SYSTEM SECTION
        Text(
            text = "Notifications & Tithi Alerts",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = LiquidTextSecondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        IOSCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Master Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Enable Tithi Alerts",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                        )
                        Text(
                            text = "Daily reminders for Parva Tithis & Sunrise timings",
                            fontSize = 12.sp,
                            color = LiquidTextSecondary
                        )
                    }

                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { checked ->
                            if (checked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                val hasPermission = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.POST_NOTIFICATIONS
                                ) == PackageManager.PERMISSION_GRANTED
                                if (!hasPermission) {
                                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    return@Switch
                                }
                            }
                            viewModel.toggleNotifications(checked)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = if (isDark) Color(0xFF475569) else Color(0xFF1E293B)
                        ),
                        modifier = Modifier.testTag("enable_notifications_toggle")
                    )
                }

                if (notificationsEnabled) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = if (isDark) LiquidDarkBorder else LiquidGlassBorder
                    )

                    // Evening Alert Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Evening Alert (8:00 PM)",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                            )
                            Text(
                                text = "Notifies about tomorrow's Tithi and Parva fasting preparation",
                                fontSize = 11.sp,
                                color = LiquidTextSecondary
                            )
                        }

                        Switch(
                            checked = eveningAlertEnabled,
                            onCheckedChange = { viewModel.toggleEveningAlert(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = if (isDark) Color(0xFF475569) else Color(0xFF1E293B)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Morning Alert Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Morning Alert (6:00 AM)",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                            )
                            Text(
                                text = "Daily Sunrise and Navkarsi deadline alarm",
                                fontSize = 11.sp,
                                color = LiquidTextSecondary
                            )
                        }

                        Switch(
                            checked = morningAlertEnabled,
                            onCheckedChange = { viewModel.toggleMorningAlert(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = if (isDark) Color(0xFF475569) else Color(0xFF1E293B)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Test Notification Button
                    OutlinedButton(
                        onClick = { viewModel.triggerTestNotification() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("send_test_notification_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                        )
                    ) {
                        Icon(Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Send Test Notification Now", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // LOCATION & ASTRONOMY COORDINATES SECTION
        Text(
            text = "Location & Solar Position",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = LiquidTextSecondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        IOSCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${currentLocation.name}${if (currentLocation.isAutoDetected) " (GPS Auto)" else ""}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                        )
                        Text(
                            text = "${currentLocation.stateOrCountry} (${String.format("%.3f", currentLocation.latitude)}°, ${String.format("%.3f", currentLocation.longitude)}°)",
                            fontSize = 12.sp,
                            color = LiquidTextSecondary
                        )
                    }

                    FilledTonalButton(
                        onClick = {
                            locationPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = if (isDark) Color(0xFF222938) else Color(0xFFE8ECF2),
                            contentColor = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                        ),
                        modifier = Modifier.testTag("auto_detect_location_button")
                    ) {
                        Icon(Icons.Default.MyLocation, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("GPS Auto", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Quick Select Major Jain Center:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = LiquidTextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Predefined city chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Ahmedabad", "Palitana", "Mumbai", "Surat").forEach { cityName ->
                        val city = DefaultLocations.predefinedCities.find { it.name == cityName }
                        if (city != null) {
                            val isSelected = currentLocation.name == city.name
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isSelected) {
                                            if (isDark) Color(0xFF2E384D) else Color(0xFF0A0D14)
                                        } else {
                                            if (isDark) Color(0xFF1E2433) else Color(0xFFF1F4F9)
                                        }
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) (if (isDark) Color(0xFF94A3B8) else Color(0xFF0A0D14)) else Color.Transparent,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { viewModel.setLocation(city) }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cityName,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else (if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // PACHAKKHAN AUDIO & LANGUAGE PREFERENCES
        Text(
            text = "Recitation & Voice Settings",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = LiquidTextSecondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        IOSCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Default Vow Script & Phonetics",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val langs = listOf(
                        Triple("hi", "हिन्दी (Prakrit)", "Prakrit phonetics"),
                        Triple("gu", "ગુજરાતી", "Traditional"),
                        Triple("en", "English", "Transliteration")
                    )

                    langs.forEach { (code, title, _) ->
                        val isSelected = preferredLanguage == code
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isSelected) {
                                        if (isDark) Color(0xFF2E384D) else Color(0xFF0A0D14)
                                    } else {
                                        if (isDark) Color(0xFF1E2433) else Color(0xFFF1F4F9)
                                    }
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) (if (isDark) Color(0xFF94A3B8) else Color(0xFF0A0D14)) else Color.Transparent,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { viewModel.setPreferredLanguage(code) }
                                .padding(vertical = 10.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else (if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Recitation Speed: ${String.format("%.1f", speechRate)}x",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(0.8f, 1.0f, 1.2f).forEach { rate ->
                        val isSelected = kotlin.math.abs(speechRate - rate) < 0.05f
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) {
                                        if (isDark) Color(0xFF2E384D) else Color(0xFF0A0D14)
                                    } else {
                                        if (isDark) Color(0xFF1E2433) else Color(0xFFF1F4F9)
                                    }
                                )
                                .clickable { viewModel.audioEngine.setSpeed(rate) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${rate}x Speed",
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else (if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // TUTORIAL & APP GUIDE
        Text(
            text = "Tutorial & App Guide",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = LiquidTextSecondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        IOSCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Interactive Onboarding Tutorial",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                    )
                    Text(
                        text = "Review how solar calculations, Tithis, and vows work",
                        fontSize = 12.sp,
                        color = LiquidTextSecondary
                    )
                }

                Button(
                    onClick = { viewModel.replayTutorial() },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) Color(0xFF222938) else Color(0xFF0A0D14),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.testTag("replay_tutorial_button")
                ) {
                    Text("Replay", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // SUPPORT & VOLUNTARY SEVA (DONATION)
        Text(
            text = "Support & Seva (Voluntary Donation)",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = LiquidTextSecondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        IOSCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Support This Free Jain Seva App",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "100% Free & Ad-Free Community Initiative",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LiquidTextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Voluntary contributions help cover server hosting, astronomical algorithms, and continued development seva.",
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = if (isDark) LiquidDarkTextSecondary else Color(0xFF334155),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .size(165.dp)
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(18.dp))
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFDEE3EB), RoundedCornerShape(18.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.qr_upi_donation),
                        contentDescription = "Scan to Donate via UPI",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isDark) Color(0xFF1E2433) else Color(0xFFF1F4F9))
                        .border(1.dp, if (isDark) Color(0xFF2E384D) else Color(0xFFDEE3EC), RoundedCornerShape(12.dp))
                        .clickable {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                            val clip = android.content.ClipData.newPlainText("UPI ID", upiId)
                            clipboard.setPrimaryClip(clip)
                            android.widget.Toast.makeText(context, "UPI ID copied: $upiId", android.widget.Toast.LENGTH_SHORT).show()
                        }
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.QrCode, contentDescription = null, tint = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary, modifier = Modifier.size(16.dp))
                    Text(
                        text = upiId,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                    )
                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = LiquidTextSecondary, modifier = Modifier.size(15.dp))
                }

                Spacer(modifier = Modifier.height(10.dp))

                FilledTonalButton(
                    onClick = {
                        val uri = android.net.Uri.parse("upi://pay?pa=$upiId&pn=Jain%20Pachakkhan%20Seva&cu=INR")
                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, uri)
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            android.widget.Toast.makeText(context, "No UPI app found. UPI ID copied!", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = if (isDark) Color(0xFF222938) else Color(0xFFE8ECF2),
                        contentColor = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.88f)
                        .height(40.dp)
                ) {
                    Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Pay via GPay / PhonePe / Paytm", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}
