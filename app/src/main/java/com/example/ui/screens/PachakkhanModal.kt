package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
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
import com.example.model.PachakkhanItem
import com.example.ui.components.GlassCard
import com.example.ui.components.IOSCard
import com.example.ui.theme.*
import com.example.viewmodel.JainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PachakkhanModal(
    item: PachakkhanItem,
    viewModel: JainViewModel,
    onDismiss: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val isPlaying by viewModel.audioEngine.isPlaying.collectAsState()
    val currentPlayingId by viewModel.audioEngine.currentlyPlayingId.collectAsState()
    val speechRate by viewModel.audioEngine.speechRate.collectAsState()
    val preferredLanguage by viewModel.preferredLanguage.collectAsState()

    var selectedLangTab by remember { mutableStateOf(preferredLanguage) }
    val isThisPlaying = isPlaying && currentPlayingId == item.id

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = if (isDark) LiquidDarkSurface else LiquidGlassSurface,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = Modifier.testTag("pachakkhan_modal")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = item.titleGujarati,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                    )
                    Text(
                        text = item.durationDescription,
                        fontSize = 13.sp,
                        color = LiquidTextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_pachakkhan_modal")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Language Selector Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isDark) Color(0xFF1E2433) else Color(0xFFF1F4F9))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LanguageTabButton(
                    label = "ગુજરાતી",
                    isSelected = selectedLangTab == "gu",
                    onClick = {
                        selectedLangTab = "gu"
                        viewModel.setPreferredLanguage("gu")
                    }
                )
                LanguageTabButton(
                    label = "हिन्दी / प्राकृत",
                    isSelected = selectedLangTab == "hi",
                    onClick = {
                        selectedLangTab = "hi"
                        viewModel.setPreferredLanguage("hi")
                    }
                )
                LanguageTabButton(
                    label = "English",
                    isSelected = selectedLangTab == "en",
                    onClick = {
                        selectedLangTab = "en"
                        viewModel.setPreferredLanguage("en")
                    }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Sacred Sutra Card
            IOSCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 3.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "॥ પચ્ચક્ખાણ સૂત્ર ॥",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = LiquidTextSecondary,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val activeSutra = when (selectedLangTab) {
                        "gu" -> item.sutraGujarati
                        "en" -> item.sutraEnglishTransliteration
                        else -> item.sutraHindi
                    }

                    Text(
                        text = activeSutra,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 28.sp,
                        textAlign = TextAlign.Center,
                        color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Audio Recitation Player Bar
            IOSCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FilledIconButton(
                            onClick = {
                                if (isThisPlaying) {
                                    viewModel.audioEngine.stop()
                                } else {
                                    viewModel.playSutra(item, selectedLangTab)
                                }
                            },
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = if (isDark) Color(0xFF2E384D) else Color(0xFF0A0D14),
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("play_sutra_audio_button")
                        ) {
                            Icon(
                                imageVector = if (isThisPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isThisPlaying) "Pause Recital" else "Play Recital",
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Column {
                            Text(
                                text = if (isThisPlaying) "Reciting Sutra..." else "Audio Recital",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
                            )
                            Text(
                                text = "Spoken Prakrit / Devotional recitation",
                                fontSize = 11.sp,
                                color = LiquidTextSecondary
                            )
                        }
                    }

                    // Speed Selector
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf(0.8f, 1.0f, 1.2f).forEach { rate ->
                            val isSelectedRate = kotlin.math.abs(speechRate - rate) < 0.05f
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelectedRate) {
                                            if (isDark) Color(0xFF2E384D) else Color(0xFF0A0D14)
                                        } else Color.Transparent
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelectedRate) Color.Transparent else (if (isDark) Color(0xFF333E52) else Color(0xFFD6DBE4)),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { viewModel.audioEngine.setSpeed(rate) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${rate}x",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isSelectedRate) Color.White else (if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // English Meaning Section
            Text(
                text = "Meaning & Exceptions (અર્થ અને આગાર)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = item.englishMeaning,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = if (isDark) LiquidDarkTextSecondary else Color(0xFF334155)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Spiritual Significance / Benefits
            Text(
                text = "Spiritual Significance",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) LiquidDarkTextPrimary else LiquidTextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = item.benefits,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = if (isDark) LiquidDarkTextSecondary else Color(0xFF334155)
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun LanguageTabButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) {
                    if (isDark) Color(0xFF2E384D) else Color(0xFF0A0D14)
                } else Color.Transparent
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else LiquidTextSecondary
        )
    }
}
