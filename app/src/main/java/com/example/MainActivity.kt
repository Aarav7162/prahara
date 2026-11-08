package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ui.components.IOSFloatingBottomBar
import com.example.ui.screens.CalendarScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PachakkhanModal
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TutorialScreen
import com.example.ui.theme.*
import com.example.viewmodel.JainViewModel

enum class ScreenTab(val tabId: String) {
    TODAY("home"),
    CALENDAR("calendar"),
    SETTINGS("settings")
}

class MainActivity : ComponentActivity() {
    private val viewModel: JainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            JainTheme {
                MainApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainApp(viewModel: JainViewModel) {
    val showTutorial by viewModel.showTutorial.collectAsState()
    var currentTab by remember { mutableStateOf(ScreenTab.TODAY) }
    val selectedPachakkhan by viewModel.selectedPachakkhan.collectAsState()
    val isDark = isSystemInDarkTheme()

    if (showTutorial) {
        TutorialScreen(
            onFinishTutorial = { viewModel.finishTutorial() }
        )
    } else {
        // Handle back button: if not on Home, return to Home
        BackHandler(enabled = currentTab != ScreenTab.TODAY) {
            currentTab = ScreenTab.TODAY
        }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(liquidGlassBackgroundBrush(isDark)),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets.safeDrawing,
            bottomBar = {
                IOSFloatingBottomBar(
                    currentTab = currentTab.tabId,
                    onTabSelected = { tabId ->
                        currentTab = when (tabId) {
                            "calendar" -> ScreenTab.CALENDAR
                            "settings" -> ScreenTab.SETTINGS
                            else -> ScreenTab.TODAY
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentTab) {
                    ScreenTab.TODAY -> HomeScreen(viewModel = viewModel)
                    ScreenTab.CALENDAR -> CalendarScreen(
                        viewModel = viewModel,
                        onNavigateToHome = { currentTab = ScreenTab.TODAY }
                    )
                    ScreenTab.SETTINGS -> SettingsScreen(viewModel = viewModel)
                }

                // Pachakkhan modal dialog
                selectedPachakkhan?.let { item ->
                    PachakkhanModal(
                        item = item,
                        viewModel = viewModel,
                        onDismiss = { viewModel.closePachakkhan() }
                    )
                }
            }
        }
    }
}
