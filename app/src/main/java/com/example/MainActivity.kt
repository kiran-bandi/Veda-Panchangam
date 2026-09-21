package com.example

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.engine.LocalizationEngine
import com.example.model.AppLanguage
import com.example.ui.*
import com.example.ui.components.AutoResizedText
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.PanchangaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: PanchangaViewModel = viewModel()
            val themeMode by viewModel.selectedThemeMode.collectAsState()
            
            MyApplicationTheme(themeMode = themeMode) {
                PanchangaApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanchangaApp(
    viewModel: PanchangaViewModel,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val activeTab by viewModel.activeTab.collectAsState()
    val language by viewModel.selectedLanguage.collectAsState()
    val showLanguageDialog by viewModel.showLanguageDialog.collectAsState()
    val showThemeDialog by viewModel.showThemeDialog.collectAsState()
    val selectedThemeMode by viewModel.selectedThemeMode.collectAsState()
    val selectedFestivalForDetail by viewModel.selectedFestivalForDetail.collectAsState()
    val shareCardFestival by viewModel.shareCardFestival.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val liveDate by viewModel.liveDate.collectAsState()
    val isToday = selectedDate == liveDate

    val notificationsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.scheduleDailyPanchangaNotification(context)
        }
    }

    // Load saved preferences, initialize database repository, schedule background notification worker, and preload interstitial ad on launch
    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.loadPreferences(context)
        viewModel.initializeDefaultLocation(context)
        viewModel.initializeRepository(context)
        viewModel.scheduleDailyPanchangaNotification(context)
        AdmobInterstitial.loadAd(context)

        // Request runtime permission on Android 13+ (API 33+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPerm = androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            if (!hasPerm) {
                notificationsLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = language,
            onLanguageSelected = { selectedLang ->
                viewModel.saveLanguage(context, selectedLang)
                viewModel.setShowLanguageDialog(false)
            },
            onDismiss = {
                viewModel.setShowLanguageDialog(false)
            }
        )
    }

    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentThemeMode = selectedThemeMode,
            language = language,
            onThemeSelected = { newTheme ->
                viewModel.saveThemeMode(context, newTheme)
            },
            onDismiss = {
                viewModel.setShowThemeDialog(false)
            }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Surface(
                color = Color(0xFF4A0E17), // Sacred Temple Maroon / గాఢ కుంకుమ వర్ణం (Matches Bottom Bar)
                shadowElevation = 6.dp,
                border = BorderStroke(1.dp, Color(0xFFD4AF37).copy(alpha = 0.35f)), // Radiant Temple Gold border
                modifier = Modifier.fillMaxWidth()
            ) {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFD4AF37).copy(alpha = 0.25f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "ॐ",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFFD54F)
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = LocalizationEngine.get("app_title", language),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFFFFD54F),
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "నిత్య వేద పంచాంగ దర్శిని",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFFFFE8E8).copy(alpha = 0.85f)
                                )
                            }
                        }
                    },
                    actions = {
                        // Theme Selector Icon
                        IconButton(
                            onClick = { viewModel.setShowThemeDialog(true) },
                            modifier = Modifier
                                .size(42.dp)
                                .testTag("action_theme_select")
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFD4AF37).copy(alpha = 0.22f),
                                modifier = Modifier.size(34.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Palette,
                                        contentDescription = "Theme",
                                        tint = Color(0xFFFFD54F),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        // Calendar / Today Action - Distinct and purposeful
                        if (!isToday || activeTab != 0) {
                            // User has browsed to a past or future date OR is on a different screen -> Show clear "ఈరోజు" badge to jump back to Today's Panchanga
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color(0xFFD4AF37),
                                modifier = Modifier
                                    .clickable {
                                        AdmobInterstitial.showAd(context) {
                                            viewModel.jumpToToday()
                                        }
                                    }
                                    .testTag("action_jump_today")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Today,
                                        contentDescription = "ఈరోజు",
                                        tint = Color(0xFF4A0E17),
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = LocalizationEngine.get("today", language),
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF4A0E17)
                                    )
                                }
                            }
                        } else {
                            // User is already on today -> Show Calendar Month button with clear visual cue
                            IconButton(
                                onClick = {
                                    AdmobInterstitial.showAd(context) {
                                        viewModel.setActiveTab(1)
                                    }
                                },
                                modifier = Modifier
                                    .size(42.dp)
                                    .testTag("action_jump_today")
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFFD4AF37).copy(alpha = 0.22f),
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.CalendarMonth,
                                            contentDescription = LocalizationEngine.get("nav_calendar", language),
                                            tint = Color(0xFFFFD54F),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        },
        bottomBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                AdmobBanner()
                Surface(
                    color = Color(0xFF7A1520), // Sacred Temple Crimson / ప్రకాశవంతమైన ఆలయ కుంకుమ వర్ణం
                    shadowElevation = 10.dp,
                    border = BorderStroke(1.dp, Color(0xFFFFD54F)), // Radiant Temple Gold top border
                    modifier = Modifier.fillMaxWidth()
                ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.widthIn(max = 680.dp)) {
                        NavigationBar(
                            containerColor = Color.Transparent,
                            tonalElevation = 0.dp
                        ) {
                    val activeGold = Color(0xFFFFD54F) // Radiant Temple Gold
                    val inactiveCream = Color(0xFFFFE8D6).copy(alpha = 0.88f) // Soft warm ivory/cream
                    val indicatorBg = Color(0xFF5C0D15) // Deep illuminated temple red pill

                    val navColors = NavigationBarItemDefaults.colors(
                        selectedIconColor = activeGold,
                        unselectedIconColor = inactiveCream,
                        selectedTextColor = activeGold,
                        unselectedTextColor = inactiveCream,
                        indicatorColor = indicatorBg
                    )

                    // Tab 0: Today
                    NavigationBarItem(
                        selected = activeTab == 0,
                        onClick = {
                            AdmobInterstitial.showAd(context) {
                                viewModel.setActiveTab(0)
                            }
                        },
                        alwaysShowLabel = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = "Today",
                                modifier = Modifier.size(20.dp),
                                tint = if (activeTab == 0) activeGold else inactiveCream
                            )
                        },
                        label = {
                            Text(
                                text = "Today",
                                fontSize = 9.5.sp,
                                fontWeight = if (activeTab == 0) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        colors = navColors,
                        modifier = Modifier.testTag("nav_tab_today")
                    )

                    // Tab 1: Calendar
                    NavigationBarItem(
                        selected = activeTab == 1,
                        onClick = {
                            AdmobInterstitial.showAd(context) {
                                viewModel.setActiveTab(1)
                            }
                        },
                        alwaysShowLabel = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Calendar",
                                modifier = Modifier.size(20.dp),
                                tint = if (activeTab == 1) activeGold else inactiveCream
                            )
                        },
                        label = {
                            Text(
                                text = "Calendar",
                                fontSize = 9.5.sp,
                                fontWeight = if (activeTab == 1) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        colors = navColors,
                        modifier = Modifier.testTag("nav_tab_calendar")
                    )

                    // Tab 2: Festivals
                    NavigationBarItem(
                        selected = activeTab == 2,
                        onClick = {
                            AdmobInterstitial.showAd(context) {
                                viewModel.setActiveTab(2)
                            }
                        },
                        alwaysShowLabel = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Celebration,
                                contentDescription = "Festivals",
                                modifier = Modifier.size(20.dp),
                                tint = if (activeTab == 2) activeGold else inactiveCream
                            )
                        },
                        label = {
                            Text(
                                text = "Festivals",
                                fontSize = 9.5.sp,
                                fontWeight = if (activeTab == 2) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        colors = navColors,
                        modifier = Modifier.testTag("nav_tab_festivals")
                    )

                    // Tab 3: Muhurtha
                    NavigationBarItem(
                        selected = activeTab == 3,
                        onClick = {
                            AdmobInterstitial.showAd(context) {
                                viewModel.setActiveTab(3)
                            }
                        },
                        alwaysShowLabel = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Muhurtha",
                                modifier = Modifier.size(20.dp),
                                tint = if (activeTab == 3) activeGold else inactiveCream
                            )
                        },
                        label = {
                            Text(
                                text = "Muhurtha",
                                fontSize = 9.5.sp,
                                fontWeight = if (activeTab == 3) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        colors = navColors,
                        modifier = Modifier.testTag("nav_tab_muhurtha")
                    )

                    // Tab 4: Explore
                    NavigationBarItem(
                        selected = activeTab == 4,
                        onClick = {
                            AdmobInterstitial.showAd(context) {
                                viewModel.setActiveTab(4)
                            }
                        },
                        alwaysShowLabel = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Explore,
                                contentDescription = "Explore",
                                modifier = Modifier.size(20.dp),
                                tint = if (activeTab == 4) activeGold else inactiveCream
                            )
                        },
                        label = {
                            Text(
                                text = "Explore",
                                fontSize = 9.5.sp,
                                fontWeight = if (activeTab == 4) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        colors = navColors,
                        modifier = Modifier.testTag("nav_tab_explore")
                    )

                    // Tab 5: Saved
                    NavigationBarItem(
                        selected = activeTab == 5,
                        onClick = {
                            AdmobInterstitial.showAd(context) {
                                viewModel.setActiveTab(5)
                            }
                        },
                        alwaysShowLabel = true,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = "Saved",
                                modifier = Modifier.size(20.dp),
                                tint = if (activeTab == 5) activeGold else inactiveCream
                            )
                        },
                        label = {
                            Text(
                                text = "Saved",
                                fontSize = 9.5.sp,
                                fontWeight = if (activeTab == 5) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        colors = navColors,
                        modifier = Modifier.testTag("nav_tab_saved")
                    )
                }
            }
        }
    }
}
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 680.dp)
            ) {
                when (activeTab) {
                    0 -> HomeScreen(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                    1 -> CalendarScreen(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                    2 -> FestivalsScreen(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                    3 -> MuhurthaScreen(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                    4 -> ExploreScreen(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                    5 -> SavedScreen(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                }
            }
        }

        // Global Dialogs
        selectedFestivalForDetail?.let { fest ->
            FestivalDetailDialog(
                festival = fest,
                language = language,
                onDismiss = { viewModel.selectFestivalForDetail(null) },
                onOpenShareCard = {
                    viewModel.openShareCard(fest)
                    viewModel.selectFestivalForDetail(null)
                }
            )
        }

        shareCardFestival?.let { fest ->
            FestiveShareCardDialog(
                festival = fest,
                language = language,
                onDismiss = { viewModel.openShareCard(null) }
            )
        }
    }
}
