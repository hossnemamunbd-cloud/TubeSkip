package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SpaceDashboard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AddEditKeywordDialog
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.KeywordsScreen
import com.example.ui.screens.LogsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.MainViewModelFactory
import com.example.util.AppStrings

enum class AppScreen(val route: String) {
    DASHBOARD("dashboard"),
    KEYWORDS("keywords"),
    LOGS("logs"),
    SETTINGS("settings")
}

data class NavItem(
    val screen: AppScreen,
    val titleKey: String,
    val icon: ImageVector
)

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels {
        val app = application as TubeSkipApp
        MainViewModelFactory(app.keywordRepository, app.settingsRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val settings by viewModel.settings.collectAsStateWithLifecycle()
            val language = settings.language

            val lifecycleOwner = LocalLifecycleOwner.current
            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_RESUME) {
                        viewModel.checkServiceStatus(this@MainActivity)
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }

            MyApplicationTheme(themeMode = settings.themeMode) {
                var currentScreen by remember { mutableStateOf(AppScreen.DASHBOARD) }
                var showQuickAddDialog by remember { mutableStateOf(false) }

                val navItems = listOf(
                    NavItem(AppScreen.DASHBOARD, "dashboard", Icons.Rounded.SpaceDashboard),
                    NavItem(AppScreen.KEYWORDS, "keywords", Icons.Rounded.Key),
                    NavItem(AppScreen.LOGS, "history", Icons.Rounded.History),
                    NavItem(AppScreen.SETTINGS, "settings", Icons.Rounded.Settings)
                )

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.safeDrawing),
                    bottomBar = {
                        NavigationBar(
                            modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 4.dp
                        ) {
                            navItems.forEach { item ->
                                val isSelected = currentScreen == item.screen
                                NavigationBarItem(
                                    selected = isSelected,
                                    onClick = { currentScreen = item.screen },
                                    icon = {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = AppStrings.get(item.titleKey, language)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = AppStrings.get(item.titleKey, language),
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedIconColor = MaterialTheme.colorScheme.primary,
                                        selectedTextColor = MaterialTheme.colorScheme.primary
                                    )
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentScreen) {
                            AppScreen.DASHBOARD -> DashboardScreen(
                                viewModel = viewModel,
                                onNavigateToKeywords = { currentScreen = AppScreen.KEYWORDS },
                                onNavigateToLogs = { currentScreen = AppScreen.LOGS },
                                onOpenAddDialog = { showQuickAddDialog = true }
                            )

                            AppScreen.KEYWORDS -> KeywordsScreen(viewModel = viewModel)

                            AppScreen.LOGS -> LogsScreen(viewModel = viewModel)

                            AppScreen.SETTINGS -> SettingsScreen(viewModel = viewModel)
                        }

                        if (showQuickAddDialog) {
                            AddEditKeywordDialog(
                                initialKeyword = null,
                                language = language,
                                onDismiss = { showQuickAddDialog = false },
                                onSave = { text, cat, matchType, targetArea, isCaseSensitive ->
                                    viewModel.addKeyword(text, cat, matchType, targetArea, isCaseSensitive)
                                    showQuickAddDialog = false
                                    Toast.makeText(
                                        this@MainActivity,
                                        if (language == "bn") "কীওয়ার্ড যুক্ত হয়েছে" else "Keyword added",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

