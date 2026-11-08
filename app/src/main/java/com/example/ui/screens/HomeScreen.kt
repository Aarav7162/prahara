package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.engine.JainTithiEngine
import com.example.model.CityLocation
import com.example.model.DefaultLocations
import com.example.model.PachakkhanItem
import com.example.repository.PachakkhanRepository
import com.example.model.PachakkhanType
import com.example.ui.components.IOSCard
import com.example.ui.components.IOSHeroSplitCard
import com.example.ui.components.IOSParvaBannerCard
import com.example.ui.components.IOSTimingCard
import com.example.ui.theme.*
import com.example.viewmodel.JainViewModel
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: JainViewModel,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val solarTimes by viewModel.solarTimes.collectAsState()
    val tithiInfo by viewModel.tithiInfo.collectAsState()
    val countdownState by viewModel.countdownState.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    val locationStatus by viewModel.locationStatus.collectAsState()

    var showLocationDialog by remember { mutableStateOf(false) }
    var showObservanceDialog by remember { mutableStateOf(false) }

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

    // Auto-request location permission on launch if not granted
    LaunchedEffect(Unit) {
        val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (!hasFine && !hasCoarse) {
            locationPermissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        } else {
            viewModel.autoDetectLocation()
        }
    }

    val time24Formatter = remember { DateTimeFormatter.ofPattern("HH:mm") }
    val timeRangeFormatter = remember { DateTimeFormatter.ofPattern("hh:mm a") }
    val sunriseSunsetStr = remember(solarTimes) {
        "${solarTimes.sunrise.format(timeRangeFormatter)} - ${solarTimes.sunset.format(timeRangeFormatter)} IST"
    }

    val pagerState = rememberPagerState(pageCount = { 2 })

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(liquidGlassBackgroundBrush(isDark))
            .testTag("home_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Sleek Minimalist Liquid Glass Location Pill
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { showLocationDialog = true }
                        .background(if (isDark) Color(0x33FFFFFF) else Color(0x18000000))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "${currentLocation.name}${if (currentLocation.isAutoDetected) " • GPS Auto" else ""}",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = if (isDark) LiquidDarkTextSecondary else LiquidTextSecondary,
                        modifier = Modifier.size(13.dp)
                    )
                }

                IconButton(
                    onClick = { viewModel.autoDetectLocation() },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh GPS Location",
                        tint = if (isDark) LiquidDarkTextSecondary else LiquidTextSecondary,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }

        // Top Card 1: Parva Notification Banner (e.g. CHAUDAS TODAY •)
        item {
            val (bannerTitle, line1, line2) = if (tithiInfo.isParva) {
                Triple(
                    "${tithiInfo.tithiName.uppercase()} TODAY •",
                    "Green vegetables restricted /",
                    "Fasting observance"
                )
            } else {
                Triple(
                    "${tithiInfo.tithiName.uppercase()} TODAY •",
                    "Standard Satvik observances /",
                    "Chauvihar recommended"
                )
            }

            IOSParvaBannerCard(
                title = bannerTitle,
                line1 = line1,
                line2 = line2,
                onClick = { showObservanceDialog = true }
            )
        }

        // Top Card 2: Split Hero Card (Countdown on Left | Local Tithi & Sunrise-Sunset on Right)
        item {
            IOSHeroSplitCard(
                milestoneName = countdownState.nextMilestoneName,
                countdownFormatted = countdownState.timeRemainingFormatted,
                tithiFullName = tithiInfo.fullName,
                sunriseSunsetRange = sunriseSunsetStr
            )
        }

        // Section Title: "Pachakkhan Timings (IST)"
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pachakkhan Timings",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LiquidTextSecondary
                )
                Text(
                    text = "IST (UTC+5:30)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = LiquidTextTertiary
                )
            }
        }

        // Pager of 3-Column Timing Grids
        item {
            val now = ZonedDateTime.now(JainViewModel.IST_ZONE)

            fun calculateProgress(time: ZonedDateTime): Float {
                val elapsed = Duration.between(solarTimes.sunrise, now).seconds
                val target = Duration.between(solarTimes.sunrise, time).seconds.coerceAtLeast(1)
                return (elapsed.toFloat() / target.toFloat()).coerceIn(0f, 1f)
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                if (page == 0) {
                    // Page 1: 6 Primary Timings matching reference screenshot exactly
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Row 1: Surya Uday (06:42) | Navkarsi (07:30) | Porsi (09:15)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            IOSTimingCard(
                                title = "Surya Uday",
                                timeFormatted = solarTimes.sunrise.format(time24Formatter),
                                icon = Icons.Default.WbSunny,
                                progressFraction = if (now.isAfter(solarTimes.sunrise)) 1f else 0.55f,
                                onClick = {
                                    PachakkhanRepository.getPachakkhan(PachakkhanType.NAVKARSI)?.let {
                                        viewModel.openPachakkhan(it)
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )

                            IOSTimingCard(
                                title = "Navkarsi",
                                timeFormatted = solarTimes.navkarsi.format(time24Formatter),
                                icon = Icons.Default.LightMode,
                                progressFraction = calculateProgress(solarTimes.navkarsi).coerceAtLeast(0.45f),
                                isCurrentActive = true, // Highlighted border focus as shown in screenshot
                                onClick = {
                                    PachakkhanRepository.getPachakkhan(PachakkhanType.NAVKARSI)?.let {
                                        viewModel.openPachakkhan(it)
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )

                            IOSTimingCard(
                                title = "Porsi",
                                timeFormatted = solarTimes.porsi.format(time24Formatter),
                                icon = Icons.Default.Spa,
                                progressFraction = calculateProgress(solarTimes.porsi).coerceAtLeast(0.30f),
                                onClick = {
                                    PachakkhanRepository.getPachakkhan(PachakkhanType.PORSI)?.let {
                                        viewModel.openPachakkhan(it)
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Row 2: Purimaddh (12:15) | Sadh-Porsi (10:30) | Chauvihar (17:55)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            IOSTimingCard(
                                title = "Purimaddh",
                                timeFormatted = solarTimes.purimaddh.format(time24Formatter),
                                icon = Icons.Default.Brightness5,
                                progressFraction = calculateProgress(solarTimes.purimaddh).coerceAtLeast(0.35f),
                                onClick = {
                                    PachakkhanRepository.getPachakkhan(PachakkhanType.PURIMADDH)?.let {
                                        viewModel.openPachakkhan(it)
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )

                            IOSTimingCard(
                                title = "Sadh-Porsi",
                                timeFormatted = solarTimes.sadhPorsi.format(time24Formatter),
                                icon = Icons.Default.SelfImprovement,
                                progressFraction = calculateProgress(solarTimes.sadhPorsi).coerceAtLeast(0.25f),
                                onClick = {
                                    PachakkhanRepository.getPachakkhan(PachakkhanType.SADH_PORSI)?.let {
                                        viewModel.openPachakkhan(it)
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )

                            IOSTimingCard(
                                title = "Chauvihar",
                                timeFormatted = solarTimes.chauvihar.format(time24Formatter),
                                icon = Icons.Default.WbTwilight,
                                progressFraction = calculateProgress(solarTimes.chauvihar).coerceAtLeast(0.60f),
                                onClick = {
                                    PachakkhanRepository.getPachakkhan(PachakkhanType.CHAUVIHAR)?.let {
                                        viewModel.openPachakkhan(it)
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                } else {
                    // Page 2: Extended Penance Vows (Avaddh, Ekashna, Biyashna, Ayambil, Upvas, Tivihar)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            IOSTimingCard(
                                title = "Avaddh",
                                timeFormatted = solarTimes.avaddh.format(time24Formatter),
                                icon = Icons.Default.Brightness4,
                                progressFraction = calculateProgress(solarTimes.avaddh),
                                onClick = {
                                    PachakkhanRepository.getPachakkhan(PachakkhanType.AVADDH)?.let {
                                        viewModel.openPachakkhan(it)
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )

                            IOSTimingCard(
                                title = "Ekashna",
                                timeFormatted = "Once",
                                icon = Icons.Default.SoupKitchen,
                                progressFraction = 0.5f,
                                onClick = {
                                    PachakkhanRepository.getPachakkhan(PachakkhanType.EKASHNA)?.let {
                                        viewModel.openPachakkhan(it)
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )

                            IOSTimingCard(
                                title = "Biyashna",
                                timeFormatted = "Twice",
                                icon = Icons.Default.Restaurant,
                                progressFraction = 0.5f,
                                onClick = {
                                    PachakkhanRepository.getPachakkhan(PachakkhanType.BIYASHNA)?.let {
                                        viewModel.openPachakkhan(it)
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            IOSTimingCard(
                                title = "Ayambil",
                                timeFormatted = "No Vigai",
                                icon = Icons.Default.Grain,
                                progressFraction = 0.5f,
                                onClick = {
                                    PachakkhanRepository.getPachakkhan(PachakkhanType.AYAMBIL)?.let {
                                        viewModel.openPachakkhan(it)
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )

                            IOSTimingCard(
                                title = "Upvas",
                                timeFormatted = "Fast",
                                icon = Icons.Default.NightsStay,
                                progressFraction = 0.8f,
                                onClick = {
                                    PachakkhanRepository.getPachakkhan(PachakkhanType.UPVAS)?.let {
                                        viewModel.openPachakkhan(it)
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )

                            IOSTimingCard(
                                title = "Tivihar",
                                timeFormatted = "Water Only",
                                icon = Icons.Default.WaterDrop,
                                progressFraction = 0.5f,
                                onClick = {
                                    PachakkhanRepository.getPachakkhan(PachakkhanType.CHAUVIHAR)?.let {
                                        viewModel.openPachakkhan(it)
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // Pagination Dots Indicator: ● ○
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf(0, 1).forEach { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (isSelected) 7.dp else 6.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) {
                                    if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                                } else {
                                    if (isDark) Color(0xFF333D50) else Color(0xFFC7CBD4)
                                }
                            )
                            .clickable {
                                coroutineScope.launch { pagerState.animateScrollToPage(index) }
                            }
                    )
                }
            }
        }
    }

    // Location Selection Dialog
    if (showLocationDialog) {
        AlertDialog(
            onDismissRequest = { showLocationDialog = false },
            title = {
                Text(
                    text = "Location & Solar Position",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp)
                ) {
                    FilledTonalButton(
                        onClick = {
                            showLocationDialog = false
                            locationPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = if (isDark) Color(0xFF222938) else Color(0xFFE8ECF2),
                            contentColor = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                        )
                    ) {
                        Icon(Icons.Default.MyLocation, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Auto-Detect Device GPS Location")
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Or choose major Jain center:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = LiquidTextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(DefaultLocations.predefinedCities) { city ->
                            val isSelected = currentLocation.name == city.name
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) (if (isDark) Color(0xFF2A3448) else Color(0xFFE8ECF2)) else Color.Transparent)
                                    .clickable {
                                        viewModel.setLocation(city)
                                        showLocationDialog = false
                                    }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = city.name,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 14.sp,
                                        color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                                    )
                                    Text(
                                        text = city.stateOrCountry,
                                        fontSize = 11.sp,
                                        color = LiquidTextSecondary
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = null,
                                        tint = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLocationDialog = false }) {
                    Text("Close", color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Observance Rules Dialog
    if (showObservanceDialog) {
        AlertDialog(
            onDismissRequest = { showObservanceDialog = false },
            title = {
                Text(
                    text = tithiInfo.observanceTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Dietary Disciplines:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                    )
                    Text(
                        text = tithiInfo.dietaryGuidelines,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = if (isDark) LiquidDarkTextSecondary else Color(0xFF334155)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Recommended Observances:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                    )
                    Text(
                        text = tithiInfo.recommendedVows,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = if (isDark) LiquidDarkTextSecondary else Color(0xFF334155)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showObservanceDialog = false }) {
                    Text("Got It", fontWeight = FontWeight.Bold, color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary)
                }
            }
        )
    }
}
