package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.IOSCard
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun TutorialScreen(
    onFinishTutorial: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 4 })

    val upiId = "8850783566@fam"

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(liquidGlassBackgroundBrush(isDark))
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .testTag("tutorial_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar with Step Indicators & Skip Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Step Indicator Pills
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(4) { index ->
                        val isCurrent = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .width(if (isCurrent) 22.dp else 8.dp)
                                .height(5.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    if (isCurrent) {
                                        if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                                    } else {
                                        if (isDark) Color(0xFF2E384D) else Color(0xFFD5DAE3)
                                    }
                                )
                        )
                    }
                }

                TextButton(
                    onClick = onFinishTutorial,
                    modifier = Modifier.testTag("tutorial_skip_button")
                ) {
                    Text(
                        text = if (pagerState.currentPage < 3) "Skip to Home" else "Done",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Pager of Tutorial Steps
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                when (page) {
                    0 -> TutorialStepView(
                        icon = Icons.Default.WbSunny,
                        badge = "STEP 1 OF 4",
                        title = "Accurate Solar Timings",
                        subtitle = "Surya Uday • Navkarsi • Prahars • Chauvihar",
                        description = "Timings are computed using real-time NOAA astronomical equations for your exact device GPS coordinates.\n\n• Navkarsi: 48 minutes (2 Ghadis) after Sunrise\n• Porsi, Sadh-Porsi & Purimaddh: Exact daylight divisions\n• Chauvihar: Sunset abstinence honoring Jain Ahimsa.",
                        highlightText = "Auto-detects device GPS location with nearest Jain center fallback."
                    )
                    1 -> TutorialStepView(
                        icon = Icons.Default.CalendarMonth,
                        badge = "STEP 2 OF 4",
                        title = "Jain Tithi & Parva Tracking",
                        subtitle = "Sud & Vad 1-15 • Parva Observances",
                        description = "Computes the prevailing Tithi at local sunrise, aligning with the Jain Panchang.\n\n• Parva Days: Atham (8th), Chaudas (14th), Poonam (Full Moon), and Amas (New Moon)\n• Green vegetable (Leelotri niyam) and root vegetable restrictions are prominently highlighted.",
                        highlightText = "Full monthly calendar with expandable day observance details."
                    )
                    2 -> TutorialStepView(
                        icon = Icons.Default.VolumeUp,
                        badge = "STEP 3 OF 4",
                        title = "Trilingual Vows & Audio Recital",
                        subtitle = "Gujarati • Hindi/Prakrit • English",
                        description = "Tap any Pachakkhan card on the home screen to recite the authentic Prakrit sutras in Gujarati, Devanagari, or English transliteration.\n\n• Built-in spoken voice recitation with adjustable speed (0.8x, 1.0x, 1.2x)\n• Daily notification alerts for Parva eve (8 PM) and morning Sunrise (6 AM).",
                        highlightText = "Clear guidelines on vow rules and exemptions (Aagars)."
                    )
                    3 -> DonationStepView(
                        upiId = upiId,
                        onCopyUpi = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("UPI ID", upiId)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "UPI ID copied: $upiId", Toast.LENGTH_SHORT).show()
                        },
                        onOpenUpi = {
                            val uri = Uri.parse("upi://pay?pa=$upiId&pn=Jain%20Pachakkhan%20Seva&cu=INR")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "No UPI app found on device. UPI ID copied!", Toast.LENGTH_LONG).show()
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bottom Navigation Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (pagerState.currentPage > 0) {
                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .height(46.dp)
                            .testTag("tutorial_back_button")
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "Back",
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(80.dp))
                }

                if (pagerState.currentPage < 3) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) Color(0xFF222938) else Color(0xFF0A0D14),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .height(46.dp)
                            .testTag("tutorial_next_button")
                    ) {
                        Text("Next", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                } else {
                    Button(
                        onClick = onFinishTutorial,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) Color(0xFF222938) else Color(0xFF0A0D14),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .height(46.dp)
                            .testTag("tutorial_start_app_button")
                    ) {
                        Text("Enter Application", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun TutorialStepView(
    icon: ImageVector,
    badge: String,
    title: String,
    subtitle: String,
    description: String,
    highlightText: String
) {
    val isDark = isSystemInDarkTheme()

    IOSCard(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        elevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(22.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Frosted Icon Pill
            Box(
                modifier = Modifier
                    .size(66.dp)
                    .clip(CircleShape)
                    .background(if (isDark) Color(0xFF222938) else Color(0xFFE8ECF2))
                    .border(1.dp, if (isDark) Color(0xFF333D50) else Color(0xFFDCE1E9), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isDark) Color(0xFFCBD5E1) else Color(0xFF475569),
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = badge,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = LiquidTextSecondary,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary,
                textAlign = TextAlign.Center
            )

            Text(
                text = subtitle,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = LiquidTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = description,
                fontSize = 13.5.sp,
                lineHeight = 21.sp,
                color = if (isDark) LiquidDarkTextSecondary else Color(0xFF334155),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isDark) Color(0xFF1E2433) else Color(0xFFF1F4F9))
                    .border(1.dp, if (isDark) Color(0xFF2D374B) else Color(0xFFDEE3EC), RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = highlightText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun DonationStepView(
    upiId: String,
    onCopyUpi: () -> Unit,
    onOpenUpi: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    IOSCard(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        elevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(if (isDark) Color(0xFF222938) else Color(0xFFE8ECF2))
                    .border(1.dp, if (isDark) Color(0xFF333D50) else Color(0xFFDCE1E9), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_app_logo),
                    contentDescription = "Prahara Logo",
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Support Prahara Seva",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "100% Free & Ad-Free Community Initiative",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = LiquidTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Jai Jinendra! If Prahara aids your daily Pachakkhan & Tithi observances, your voluntary support helps fund ongoing hosting, astronomy calculations, and audio services.",
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = if (isDark) LiquidDarkTextSecondary else Color(0xFF334155),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            // QR Code in pristine liquid glass card
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

            // UPI ID Chip with Copy Button
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isDark) Color(0xFF1E2433) else Color(0xFFF1F4F9))
                    .border(1.dp, if (isDark) Color(0xFF2D374B) else Color(0xFFDEE3EC), RoundedCornerShape(12.dp))
                    .clickable(onClick = onCopyUpi)
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .testTag("copy_upi_id_button"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.QrCode,
                    contentDescription = null,
                    tint = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = upiId,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                )
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy UPI ID",
                    tint = if (isDark) LiquidDarkTextSecondary else LiquidTextSecondary,
                    modifier = Modifier.size(15.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Open in UPI Button
            FilledTonalButton(
                onClick = onOpenUpi,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = if (isDark) Color(0xFF222938) else Color(0xFFE8ECF2),
                    contentColor = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .height(42.dp)
                    .testTag("open_upi_app_button")
            ) {
                Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Pay via GPay / PhonePe / Paytm", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
