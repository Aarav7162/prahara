package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.JainTithiEngine
import com.example.engine.SolarAstronomyEngine
import com.example.ui.components.GlassCard
import com.example.ui.components.ParvaBadge
import com.example.ui.theme.*
import com.example.viewmodel.JainViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: JainViewModel,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val currentMonth by viewModel.currentMonth.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()

    var activeDetailDate by remember { mutableStateOf<LocalDate?>(selectedDate) }
    var showDetailSheet by remember { mutableStateOf(false) }

    val monthFormatter = remember { DateTimeFormatter.ofPattern("MMMM yyyy") }
    val today = remember { LocalDate.now(JainViewModel.IST_ZONE) }

    // Generate days for grid
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfMonth = currentMonth.atDay(1)
    val startDayOfWeek = (firstDayOfMonth.dayOfWeek.value % 7) // 0 for Sunday

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(liquidGlassBackgroundBrush(isDark))
            .testTag("calendar_screen")
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Top Header: Month title & Nav buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Jain Tithi Calendar",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                )
                Text(
                    text = currentMonth.format(monthFormatter),
                    fontSize = 14.sp,
                    color = LiquidTextSecondary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { viewModel.previousMonth() },
                    modifier = Modifier.testTag("prev_month_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Previous Month",
                        tint = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                    )
                }

                TextButton(
                    onClick = {
                        val nowMonth = YearMonth.now(JainViewModel.IST_ZONE)
                        viewModel.setCalendarMonth(nowMonth)
                        viewModel.selectDate(today)
                        activeDetailDate = today
                    }
                ) {
                    Text(
                        "Today",
                        color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = { viewModel.nextMonth() },
                    modifier = Modifier.testTag("next_month_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next Month",
                        tint = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Weekday Headers (Sun to Sat)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = LiquidTextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Calendar Day Cells Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Empty placeholder days before first day of month
            items(startDayOfWeek) {
                Spacer(modifier = Modifier.aspectRatio(0.85f))
            }

            // Actual days of the month
            items(daysInMonth) { dayIndex ->
                val dayNum = dayIndex + 1
                val cellDate = currentMonth.atDay(dayNum)
                val tithi = remember(cellDate) { JainTithiEngine.calculateTithi(cellDate) }
                val isToday = cellDate == today
                val isSelected = cellDate == activeDetailDate

                DayCell(
                    dayNum = dayNum,
                    tithi = tithi,
                    isToday = isToday,
                    isSelected = isSelected,
                    onClick = {
                        activeDetailDate = cellDate
                        viewModel.selectDate(cellDate)
                        showDetailSheet = true
                    }
                )
            }
        }

        // Inline quick summary bar for active selected date
        activeDetailDate?.let { date ->
            val activeTithi = remember(date) { JainTithiEngine.calculateTithi(date) }
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 80.dp)
                    .clickable { showDetailSheet = true },
                elevation = 3.dp,
                borderColor = if (isDark) Color(0xFF384357) else Color(0xFF475569)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${date.format(DateTimeFormatter.ofPattern("EEE, d MMM"))} • ${activeTithi.fullName}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                        )
                        if (activeTithi.isParva) {
                            Text(
                                text = activeTithi.observanceTitle,
                                fontSize = 11.sp,
                                color = LiquidTextSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                        } else {
                            Text(
                                text = "Standard Tithi • Tap for sunrise & timings",
                                fontSize = 11.sp,
                                color = LiquidTextSecondary
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Details",
                        tint = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                    )
                }
            }
        }
    }

    // Expandable Drawer / Modal Bottom Sheet on clicking any date
    if (showDetailSheet && activeDetailDate != null) {
        val date = activeDetailDate!!
        val tithi = remember(date) { JainTithiEngine.calculateTithi(date) }
        val dateSolarTimes = remember(date, currentLocation) {
            SolarAstronomyEngine.calculateSolarTimes(
                date = date,
                latitude = currentLocation.latitude,
                longitude = currentLocation.longitude
            )
        }
        val timeFormatter = remember { DateTimeFormatter.ofPattern("hh:mm a") }

        ModalBottomSheet(
            onDismissRequest = { showDetailSheet = false },
            containerColor = if (isDark) LiquidDarkSurface else LiquidGlassSurface,
            dragHandle = { BottomSheetDefaults.DragHandle() },
            modifier = Modifier.testTag("calendar_detail_sheet")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = date.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy")),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = LiquidTextSecondary
                        )
                        Text(
                            text = tithi.fullName,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                        )
                    }

                    IconButton(onClick = { showDetailSheet = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (tithi.isParva) {
                    ParvaBadge(text = tithi.parvaBadgeText)
                    Spacer(modifier = Modifier.height(12.dp))
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = if (isDark) Color(0xFF1E2433) else Color(0xFFF1F4F9),
                        borderColor = if (isDark) Color(0xFF2E384D) else Color(0xFFDCE1E9)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text = "Observance & Dietary Disciplines",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = tithi.dietaryGuidelines,
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                color = if (isDark) LiquidDarkTextSecondary else Color(0xFF334155)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = tithi.recommendedVows,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LiquidTextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Astronomical Solar Timings (${currentLocation.name} • IST)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TimingPill(
                        label = "Sunrise",
                        time = dateSolarTimes.sunrise.format(timeFormatter),
                        modifier = Modifier.weight(1f)
                    )
                    TimingPill(
                        label = "Navkarsi",
                        time = dateSolarTimes.navkarsi.format(timeFormatter),
                        modifier = Modifier.weight(1f)
                    )
                    TimingPill(
                        label = "Sunset",
                        time = dateSolarTimes.sunset.format(timeFormatter),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Action: View In Daily Tracker
                Button(
                    onClick = {
                        viewModel.selectDate(date)
                        showDetailSheet = false
                        onNavigateToHome()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("view_in_daily_tracker_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) Color(0xFF222938) else Color(0xFF0A0D14),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("View Full Timings in Daily Tracker", fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}

@Composable
private fun DayCell(
    dayNum: Int,
    tithi: JainTithiEngine.TithiInfo,
    isToday: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    val bgColor = when {
        isSelected -> if (isDark) Color(0xFF2E384D) else Color(0xFFE2E6EE)
        isToday -> if (isDark) Color(0xFF222836) else Color(0xFFECEFF4)
        tithi.isParva -> if (isDark) Color(0xFF1E2330) else Color(0xFFF3F5F9)
        else -> if (isDark) LiquidDarkSurface else LiquidGlassSurface
    }

    val borderColor = when {
        isSelected -> if (isDark) Color(0xFF94A3B8) else Color(0xFF475569)
        isToday -> if (isDark) Color(0xFF64748B) else Color(0xFF64748B)
        tithi.isParva -> if (isDark) Color(0xFF333E52) else Color(0xFFD6DBE4)
        else -> if (isDark) LiquidDarkBorder else LiquidGlassBorder
    }

    Box(
        modifier = Modifier
            .aspectRatio(0.85f)
            .clip(RoundedCornerShape(11.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(11.dp))
            .clickable(onClick = onClick)
            .padding(4.dp)
            .testTag("day_cell_$dayNum"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$dayNum",
                fontSize = 14.sp,
                fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = tithi.shortName,
                fontSize = 9.sp,
                fontWeight = if (tithi.isParva) FontWeight.Bold else FontWeight.Normal,
                color = if (tithi.isParva) (if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary) else LiquidTextSecondary,
                maxLines = 1
            )

            if (tithi.isParva) {
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary)
                )
            }
        }
    }
}

@Composable
private fun TimingPill(
    label: String,
    time: String,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()

    GlassCard(modifier = modifier, elevation = 1.dp) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                color = LiquidTextSecondary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = time,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
            )
        }
    }
}
