package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.ScreenNav
import com.example.ui.components.CyberBackground
import com.example.ui.components.SplashLoadingScreen
import com.example.ui.components.UniversalThreatDialog
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.AntiTrackScreen
import com.example.ui.screens.AntivirusScreen
import com.example.ui.screens.CleanupScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.NetworkScreen
import com.example.ui.screens.PrivacyScreen
import com.example.ui.screens.ReportsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SystemScreen
import com.example.ui.screens.ToolsScreen
import com.example.ui.screens.VpnScreen
import com.example.ui.screens.WebShieldScreen
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.CyberVoid
import com.example.ui.theme.DTZShieldTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
            DTZShieldTheme(darkTheme = isDarkTheme) {
                DTZShieldApp(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DTZShieldApp(viewModel: MainViewModel) {
    val context = LocalContext.current
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
    val isSplashLoading by viewModel.isSplashLoading.collectAsStateWithLifecycle()
    val splashMessage by viewModel.splashMessage.collectAsStateWithLifecycle()
    val universalThreatNotice by viewModel.universalThreatNotice.collectAsStateWithLifecycle()

    var showMoreSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Handle Splash Loading state
    if (isSplashLoading) {
        SplashLoadingScreen(statusMessage = splashMessage)
        return
    }

    // Handle Android system back button to pop subscreen back to Dashboard
    BackHandler(enabled = currentScreen != ScreenNav.DASHBOARD) {
        viewModel.navigateTo(ScreenNav.DASHBOARD)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Dynamic Cyber / Lumina Canvas Background
        CyberBackground(performanceMode = settings.performanceMode)

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(NeonCyan.copy(alpha = 0.15f))
                                    .border(1.dp, NeonCyan, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = "DTZ Shield",
                                    tint = NeonCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "DTZ SHIELD",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Black,
                                        color = TextPrimary,
                                        letterSpacing = 0.8.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "v2.0.1",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NeonEmerald
                                    )
                                }
                                Text(
                                    text = currentScreen.title,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = NeonCyan
                                )
                            }
                        }
                    },
                    actions = {
                        // Game mode active badge
                        if (settings.gameMode) {
                            Box(
                                modifier = Modifier
                                    .padding(end = 4.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NeonEmerald.copy(alpha = 0.2f))
                                    .border(1.dp, NeonEmerald, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(text = "GAME MODE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = NeonEmerald)
                            }
                        }

                        // Theme Toggle (Dark / Light)
                        IconButton(
                            onClick = { viewModel.toggleTheme() },
                            modifier = Modifier.testTag("topbar_theme_toggle")
                        ) {
                            Icon(
                                imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle Light/Dark Theme",
                                tint = NeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Sound toggle
                        IconButton(
                            onClick = { viewModel.toggleSound(!settings.soundEffects) },
                            modifier = Modifier.testTag("topbar_sound_toggle")
                        ) {
                            Icon(
                                imageVector = if (settings.soundEffects) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                                contentDescription = "Toggle Audio",
                                tint = if (settings.soundEffects) NeonCyan else TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // WhatsApp Developer Contact Shortcut
                        IconButton(
                            onClick = {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://reference-url-citation.invalid/0"))
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            },
                            modifier = Modifier.testTag("topbar_whatsapp_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Chat,
                                contentDescription = "WhatsApp Developer",
                                tint = Color(0xFF25D366),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = CyberVoid.copy(alpha = 0.88f),
                        titleContentColor = TextPrimary
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    modifier = Modifier.navigationBarsPadding(),
                    containerColor = CyberVoid.copy(alpha = 0.94f),
                    tonalElevation = 8.dp
                ) {
                    val navItems = listOf(
                        Triple(ScreenNav.DASHBOARD, Icons.Default.Security, "Shield"),
                        Triple(ScreenNav.ANTIVIRUS, Icons.Default.Bolt, "Antivirus"),
                        Triple(ScreenNav.VPN, Icons.Default.VpnKey, "VPN"),
                        Triple(ScreenNav.WEB_SHIELD, Icons.Default.Language, "Web")
                    )

                    for ((screen, icon, label) in navItems) {
                        val selected = currentScreen == screen
                        NavigationBarItem(
                            selected = selected,
                            onClick = { viewModel.navigateTo(screen) },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = if (selected) NeonCyan else TextSecondary
                                )
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selected) NeonCyan else TextSecondary
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = NeonCyan.copy(alpha = 0.15f)
                            ),
                            modifier = Modifier.testTag("nav_item_${label.lowercase()}")
                        )
                    }

                    // More Navigation Item
                    val isMoreSelected = currentScreen in listOf(
                        ScreenNav.CLEANUP, ScreenNav.ANTITRACK, ScreenNav.TOOLS,
                        ScreenNav.NETWORK, ScreenNav.PRIVACY, ScreenNav.SYSTEM,
                        ScreenNav.REPORTS, ScreenNav.SETTINGS, ScreenNav.ABOUT
                    )
                    NavigationBarItem(
                        selected = isMoreSelected,
                        onClick = { showMoreSheet = true },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.MoreHoriz,
                                contentDescription = "More Suite Modules",
                                tint = if (isMoreSelected) NeonCyan else TextSecondary
                            )
                        },
                        label = {
                            Text(
                                text = "More",
                                fontSize = 10.sp,
                                fontWeight = if (isMoreSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isMoreSelected) NeonCyan else TextSecondary
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = NeonCyan.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_item_more")
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "screen_transition"
                ) { target ->
                    when (target) {
                        ScreenNav.DASHBOARD -> DashboardScreen(viewModel = viewModel)
                        ScreenNav.ANTIVIRUS -> AntivirusScreen(viewModel = viewModel)
                        ScreenNav.VPN -> VpnScreen(viewModel = viewModel)
                        ScreenNav.WEB_SHIELD -> WebShieldScreen(viewModel = viewModel)
                        ScreenNav.CLEANUP -> CleanupScreen(viewModel = viewModel)
                        ScreenNav.ANTITRACK -> AntiTrackScreen(viewModel = viewModel)
                        ScreenNav.TOOLS -> ToolsScreen(viewModel = viewModel)
                        ScreenNav.NETWORK -> NetworkScreen(viewModel = viewModel)
                        ScreenNav.PRIVACY -> PrivacyScreen(viewModel = viewModel)
                        ScreenNav.SYSTEM -> SystemScreen(viewModel = viewModel)
                        ScreenNav.REPORTS -> ReportsScreen(viewModel = viewModel)
                        ScreenNav.SETTINGS -> SettingsScreen(viewModel = viewModel)
                        ScreenNav.ABOUT -> AboutScreen()
                    }
                }
            }
        }

        // More Modules Bottom Sheet
        if (showMoreSheet) {
            ModalBottomSheet(
                onDismissRequest = { showMoreSheet = false },
                sheetState = sheetState,
                containerColor = CyberSurface,
                scrimColor = Color.Black.copy(alpha = 0.65f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .navigationBarsPadding()
                ) {
                    Text(
                        text = "DTZ SHIELD v2.0.1 SUITE MODULES",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = NeonCyan,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    MoreMenuRow("Cleanup Premium", "Junk-file removal, RAM booster & storage cleaner", Icons.Default.CleaningServices) {
                        showMoreSheet = false
                        viewModel.navigateTo(ScreenNav.CLEANUP)
                    }
                    MoreMenuRow("AntiTrack Premium", "Browser fingerprint cloaker & tracker radar", Icons.Default.Fingerprint) {
                        showMoreSheet = false
                        viewModel.navigateTo(ScreenNav.ANTITRACK)
                    }
                    MoreMenuRow("10 Next-Gen Tools", "CyberCapture, Scam Shield, Shredder & Deepfake guard", Icons.Default.Security) {
                        showMoreSheet = false
                        viewModel.navigateTo(ScreenNav.TOOLS)
                    }
                    MoreMenuRow("Network Shield & Firewall", "Wi-Fi vulnerability analysis, ARP & port inspector", Icons.Default.Router) {
                        showMoreSheet = false
                        viewModel.navigateTo(ScreenNav.NETWORK)
                    }
                    MoreMenuRow("Privacy Shield", "Ransomware folder lock, webcam & data audit", Icons.Default.Lock) {
                        showMoreSheet = false
                        viewModel.navigateTo(ScreenNav.PRIVACY)
                    }
                    MoreMenuRow("System Health", "Memory, CPU, storage & DTZ Game Mode", Icons.Default.Tune) {
                        showMoreSheet = false
                        viewModel.navigateTo(ScreenNav.SYSTEM)
                    }
                    MoreMenuRow("Security Reports", "Scan execution history & threat logs", Icons.Default.Assessment) {
                        showMoreSheet = false
                        viewModel.navigateTo(ScreenNav.REPORTS)
                    }
                    MoreMenuRow("Settings & Demo", "Shield toggles, audio & safe malware test", Icons.Default.Settings) {
                        showMoreSheet = false
                        viewModel.navigateTo(ScreenNav.SETTINGS)
                    }
                    MoreMenuRow("About DTZ SHIELD", "Developer Anuga Senithu • UltraDev • WhatsApp", Icons.Default.Info) {
                        showMoreSheet = false
                        viewModel.navigateTo(ScreenNav.ABOUT)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Universal Threat Warning Pop-Up Dialog
        if (universalThreatNotice != null) {
            UniversalThreatDialog(
                threat = universalThreatNotice!!,
                onDismiss = { viewModel.dismissUniversalThreat() },
                onBlockAndQuarantine = { viewModel.quarantineAndBlockCurrentThreat() }
            )
        }
    }
}

@Composable
private fun MoreMenuRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        color = Color.Transparent
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(CyberSurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = subtitle, fontSize = 11.sp, color = TextSecondary)
            }
        }
    }
}
