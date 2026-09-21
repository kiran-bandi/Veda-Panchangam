package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.NakshatraDetail
import com.example.engine.RashiDetail
import com.example.engine.RashiPhalaluData
import com.example.engine.ZodiacRepository
import com.example.ui.theme.TeluguRed
import com.example.ui.theme.TeluguRedDark
import com.example.ui.theme.TeluguRedLight

enum class RashiSection {
    HUB,
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY,
    ENGLISH_YEAR,
    ABOUT_RASHIS,
    RASHI_TRAITS,
    NAKSHATRA_TRAITS
}

@Composable
fun RashiPhalaluScreen(
    onNavigateBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var currentSection by remember { mutableStateOf(RashiSection.HUB) }
    var selectedRashiForDetail by remember { mutableStateOf<RashiDetail?>(null) }
    var selectedNakshatraForDetail by remember { mutableStateOf<NakshatraDetail?>(null) }
    var dailyIsTomorrow by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("rashi_phalalu_screen")
    ) {
        // Top Header Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectedRashiForDetail != null || selectedNakshatraForDetail != null || currentSection != RashiSection.HUB || onNavigateBack != null) {
                    IconButton(
                        onClick = {
                            if (selectedRashiForDetail != null) {
                                selectedRashiForDetail = null
                            } else if (selectedNakshatraForDetail != null) {
                                selectedNakshatraForDetail = null
                            } else if (currentSection != RashiSection.HUB) {
                                currentSection = RashiSection.HUB
                            } else {
                                onNavigateBack?.invoke()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "వెనుకకు",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                Text(
                    text = when {
                        selectedRashiForDetail != null -> "${selectedRashiForDetail?.teluguName} ఫలాలు"
                        selectedNakshatraForDetail != null -> "${selectedNakshatraForDetail?.teluguName} నక్షత్రం"
                        currentSection == RashiSection.HUB -> "రాశి ఫలాలు"
                        currentSection == RashiSection.DAILY -> "దిన ఫలాలు"
                        currentSection == RashiSection.WEEKLY -> "వార ఫలాలు"
                        currentSection == RashiSection.MONTHLY -> "మాస ఫలాలు"
                        currentSection == RashiSection.YEARLY -> "సంవత్సర ఫలాలు (2026-27)"
                        currentSection == RashiSection.ENGLISH_YEAR -> "ఆంగ్ల సంవత్సర రాశి ఫలాలు (2026)"
                        currentSection == RashiSection.ABOUT_RASHIS -> "రాశులు & నక్షత్రములు పరిచయం"
                        currentSection == RashiSection.RASHI_TRAITS -> "జన్మ రాశులు వాటి స్వభావాలు"
                        currentSection == RashiSection.NAKSHATRA_TRAITS -> "27 నక్షత్రాలు వాటి స్వభావాలు"
                        else -> "రాశి ఫలాలు"
                    },
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        // Section Content (Displays strictly within middle space)
        Box(modifier = Modifier.weight(1f)) {
            if (selectedRashiForDetail != null) {
                RashiDetailInlineView(
                    rashi = selectedRashiForDetail!!,
                    section = currentSection,
                    isTomorrowInitial = dailyIsTomorrow,
                    onDismiss = { selectedRashiForDetail = null }
                )
            } else if (selectedNakshatraForDetail != null) {
                NakshatraDetailInlineView(
                    nakshatra = selectedNakshatraForDetail!!,
                    onDismiss = { selectedNakshatraForDetail = null }
                )
            } else {
                when (currentSection) {
                    RashiSection.HUB -> RashiHubContent(onSelectSection = { currentSection = it })
                    RashiSection.DAILY -> DailyRashiContent(
                        isTomorrow = dailyIsTomorrow,
                        onToggleTomorrow = { dailyIsTomorrow = it },
                        onSelectRashi = { selectedRashiForDetail = it }
                    )
                    RashiSection.WEEKLY -> PeriodicRashiContent(
                        periodTitle = "వార ఫలాలు",
                        onSelectRashi = { selectedRashiForDetail = it }
                    )
                    RashiSection.MONTHLY -> PeriodicRashiContent(
                        periodTitle = "మాస ఫలాలు (సెప్టెంబర్ 2026)",
                        onSelectRashi = { selectedRashiForDetail = it }
                    )
                    RashiSection.YEARLY -> PeriodicRashiContent(
                        periodTitle = "సంవత్సర ఫలాలు (శ్రీ పరాభవ నామ సంవత్సరం)",
                        onSelectRashi = { selectedRashiForDetail = it }
                    )
                    RashiSection.ENGLISH_YEAR -> PeriodicRashiContent(
                        periodTitle = "2026 వార్షిక రాశి ఫలాలు",
                        onSelectRashi = { selectedRashiForDetail = it }
                    )
                    RashiSection.ABOUT_RASHIS -> AboutRashisContent()
                    RashiSection.RASHI_TRAITS -> RashiTraitsContent(onSelectRashi = { selectedRashiForDetail = it })
                    RashiSection.NAKSHATRA_TRAITS -> NakshatraTraitsContent(onSelectNakshatra = { selectedNakshatraForDetail = it })
                }
            }
        }
    }
}

/**
 * 8-Card Hub Menu
 */
@Composable
private fun RashiHubContent(onSelectSection: (RashiSection) -> Unit) {
    val items = listOf(
        RashiHubItem("దిన ఫలాలు", "ఈరోజు & రేపటి ఫలితాలు", "☀️", RashiSection.DAILY),
        RashiHubItem("వార ఫలాలు", "ఈ వారపు గ్రహస్థితి & ఫలాలు", "🗓️", RashiSection.WEEKLY),
        RashiHubItem("మాస ఫలాలు", "ఈ నెల రాశి ఫలితాలు", "🌙", RashiSection.MONTHLY),
        RashiHubItem("సంవత్సర ఫలాలు", "శ్రీ పరాభవ నామ సంవత్సర ఫలాలు", "🪐", RashiSection.YEARLY),
        RashiHubItem("ఆంగ్ల సంవత్సర రాశి ఫలాలు", "2026 వార్షిక ఫలితాలు", "✨", RashiSection.ENGLISH_YEAR),
        RashiHubItem("రాశులు & నక్షత్రములు అంటే ఏమిటి?", "జ్యోతిష్య పరిచయం & ప్రాముఖ్యత", "📖", RashiSection.ABOUT_RASHIS),
        RashiHubItem("జన్మ రాశులు వాటి స్వభావాలు", "12 రాశుల సమగ్ర లక్షణాలు", "♈", RashiSection.RASHI_TRAITS),
        RashiHubItem("జన్మ నక్షత్రములు వాటి స్వభావములు", "27 నక్షత్రాలు & అధిదేవతలు", "⭐", RashiSection.NAKSHATRA_TRAITS)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .clickable { onSelectSection(item.section) },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start
                ) {
                    Surface(
                        shape = CircleShape,
                        color = TeluguRedLight,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = item.icon, fontSize = 18.sp)
                        }
                    }
                    Column {
                        Text(
                            text = item.title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.subtitle,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

private data class RashiHubItem(
    val title: String,
    val subtitle: String,
    val icon: String,
    val section: RashiSection
)

/**
 * Daily Horoscope Screen with 12 Rashis Grid and Today/Tomorrow Toggle
 */
@Composable
private fun DailyRashiContent(
    isTomorrow: Boolean,
    onToggleTomorrow: (Boolean) -> Unit,
    onSelectRashi: (RashiDetail) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Today / Tomorrow Segmented Selector
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 6.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(if (!isTomorrow) TeluguRed else Color.Transparent)
                        .clickable { onToggleTomorrow(false) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ఈరోజు",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (!isTomorrow) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(if (isTomorrow) TeluguRed else Color.Transparent)
                        .clickable { onToggleTomorrow(true) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "రేపు",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isTomorrow) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 3-Column Grid of 12 Rashis
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(ZodiacRepository.RASHIS) { rashi ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(105.dp)
                        .clickable { onSelectRashi(rashi) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                    border = BorderStroke(1.dp, Color(0xFFFFE082)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFFFF3E0),
                            border = BorderStroke(1.dp, Color(0xFFFFCC80)),
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = rashi.symbol, fontSize = 22.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = rashi.teluguName.replace(" రాశి", ""),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF211A13)
                        )
                        Text(
                            text = rashi.teluguRuler,
                            fontSize = 9.sp,
                            color = Color(0xFF8D1808),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

/**
 * Periodic Horoscope (Weekly, Monthly, Yearly)
 */
@Composable
private fun PeriodicRashiContent(
    periodTitle: String,
    onSelectRashi: (RashiDetail) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
        ) {
            Text(
                text = "✨ $periodTitle - మీ రాశిని ఎంచుకోండి",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(10.dp),
                textAlign = TextAlign.Center
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(ZodiacRepository.RASHIS) { rashi ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(105.dp)
                        .clickable { onSelectRashi(rashi) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE2E4E8))
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = TeluguRedLight,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = rashi.symbol, fontSize = 22.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = rashi.teluguName.replace(" రాశి", ""),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212529)
                        )
                        Text(
                            text = rashi.teluguRuler,
                            fontSize = 9.sp,
                            color = Color(0xFF6C757D)
                        )
                    }
                }
            }
        }
    }
}

/**
 * 27 Nakshatras View
 */
@Composable
private fun NakshatraTraitsContent(onSelectNakshatra: (NakshatraDetail) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = TeluguRedLight,
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
        ) {
            Text(
                text = "⭐ 27 జన్మ నక్షత్రాలు - అధిదేవత & విశేష గుణాలు",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TeluguRed,
                modifier = Modifier.padding(10.dp),
                textAlign = TextAlign.Center
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(ZodiacRepository.NAKSHATRAS) { nak ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(95.dp)
                        .clickable { onSelectNakshatra(nak) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "${nak.number}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TeluguRed
                        )
                        Text(
                            text = nak.teluguName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = nak.teluguDeity,
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

/**
 * Rashi Traits Content
 */
@Composable
private fun RashiTraitsContent(onSelectRashi: (RashiDetail) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(ZodiacRepository.RASHIS) { rashi ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectRashi(rashi) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = rashi.symbol, fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = rashi.teluguName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "అధిపతి: ${rashi.teluguRuler}",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = rashi.teluguDescription,
                        fontSize = 10.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

/**
 * About Rashis Content
 */
@Composable
private fun AboutRashisContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Main Title Banner
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🕉️", fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "12 రాశులు – 27 నక్షత్రాలు – 108 పాదాలు",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "భారతీయ వేద జ్యోతిషంలో ఆకాశం 360 డిగ్రీల చక్రంగా విభజించబడింది. ఇందులో 12 రాశులు మరియు 27 నక్షత్రాలు ఉంటాయి. ప్రతి రాశి 30 డిగ్రీల విస్తీర్ణాన్ని కలిగి ఉంటుంది.\n\nప్రతి నక్షత్రానికి 4 పాదాలు ఉంటాయి. మొత్తం 27 నక్షత్రాల పాదాలు 108 (27 × 4 = 108). ఈ 108 పాదాలు 12 రాశులలో సమానంగా (ఒక్కో రాశికి 9 పాదాలు) పంపిణీ చేయబడ్డాయి.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 18.sp
                )
            }
        }

        // 12 Janma Rashis Swabhavalu Section
        Text(
            text = "🕉️ 12 జన్మ రాశులు — వాటి స్వభావాలు & లక్షణాలు",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TeluguRed,
            modifier = Modifier.padding(top = 4.dp)
        )

        com.example.engine.RashiNakshatra108Data.JANMA_RASHI_SWABHAVALU_LIST.forEach { rashiSwabhava ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Title Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = rashiSwabhava.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TeluguRed
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                        ) {
                            Text(
                                text = "అధిపతి: ${rashiSwabhava.adhipati}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Key Attributes Grid/Chips
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("• తత్త్వం: ${rashiSwabhava.tattvam}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                Text("• స్వభావం: ${rashiSwabhava.svabhavam}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                Text("• లింగం: ${rashiSwabhava.lingam}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("• ప్రతీకం: ${rashiSwabhava.pratikam}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                if (rashiSwabhava.disha.isNotBlank()) {
                                    Text("• దిశ: ${rashiSwabhava.disha}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                }
                                Text("• శరీర భాగం: ${rashiSwabhava.kalapurushaBhagam}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                            }
                            if (rashiSwabhava.prakriti.isNotBlank()) {
                                Text("• ప్రకృతి: ${rashiSwabhava.prakriti}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text("• నక్షత్ర పాదాలు: ${rashiSwabhava.nakshatraPadalu} (మొత్తం: ${rashiSwabhava.totalPadalu})", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Pradhana Svabhavam
                    Text("ప్రధాన స్వభావం:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TeluguRed)
                    Text(rashiSwabhava.pradhanaSvabhavam, fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurface, lineHeight = 17.sp)

                    Spacer(modifier = Modifier.height(6.dp))

                    // Sadharana Lakshanalu
                    Text("సాధారణంగా చెప్పే లక్షణాలు:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2B8A3E))
                    Text(rashiSwabhava.sadharanaLakshanalu, fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurface, lineHeight = 17.sp)

                    if (rashiSwabhava.manchiVaipu.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("మంచి వైపు (ధనాత్మక లక్షణాలు):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text(rashiSwabhava.manchiVaipu, fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurface, lineHeight = 17.sp)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Savallu
                    Text("జాగ్రత్తగా చూడవలసిన వైపు / సవాళ్లు:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TeluguRedDark)
                    Text(rashiSwabhava.savallu, fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 17.sp)

                    Spacer(modifier = Modifier.height(6.dp))

                    // Vrutti Rangalu
                    Text("సంప్రదాయ వృత్తి ఆసక్తులు / రంగాలు:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text(rashiSwabhava.vruttiRangalu, fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurface, lineHeight = 17.sp)
                }
            }
        }

        // 12 Rashis Detailed 9-Pada Breakdown
        Text(
            text = "12 రాశుల సమగ్ర నక్షత్ర & పాద వివరణ",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TeluguRed,
            modifier = Modifier.padding(top = 8.dp)
        )

        com.example.engine.RashiNakshatra108Data.RASHIS_108_PADAS.forEach { rashiInfo ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = rashiInfo.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TeluguRed
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                        ) {
                            Text(
                                text = rashiInfo.degrees,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = rashiInfo.summaryPadas,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2B8A3E),
                        lineHeight = 17.sp
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 10.dp),
                        thickness = 0.8.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    )

                    // Individual Padas List
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        rashiInfo.padas.forEach { pada ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = pada.name,
                                            fontSize = 12.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = pada.degreesInRashi,
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = pada.description,
                                        fontSize = 11.5.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 27 Nakshatras Summary List
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "📌 27 నక్షత్రాల పూర్తి జాబితా",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    com.example.engine.RashiNakshatra108Data.NAKSHATRAS_27_LIST.forEach { item ->
                        Text(
                            text = "• $item",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 10.dp),
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                )
                Text(
                    text = "27 × 4 = 108 పాదాలు.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * Comprehensive Rashi Detail View with Authentic Section-Specific Vedic Data (Displayed Inline in Middle Space)
 */
@Composable
fun RashiDetailInlineView(
    rashi: RashiDetail,
    section: RashiSection = RashiSection.DAILY,
    isTomorrowInitial: Boolean = false,
    onDismiss: () -> Unit
) {
    var isTomorrowInModal by remember { mutableStateOf(isTomorrowInitial) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Top Header Card with Rashi Info and Close/Back Action
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = rashi.symbol, fontSize = 22.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = rashi.teluguName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "అధిపతి: ${rashi.teluguRuler}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "మూసివేయి",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
                when (section) {
                    RashiSection.DAILY -> {
                        val dailyData = remember(rashi.id, isTomorrowInModal) {
                            RashiPhalaluData.getDailyHoroscope(rashi.id, isTomorrowInModal)
                        }

                        // Today / Tomorrow toggle in modal
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(if (!isTomorrowInModal) MaterialTheme.colorScheme.primary else Color.Transparent)
                                        .clickable { isTomorrowInModal = false }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "ఈరోజు",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (!isTomorrowInModal) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(if (isTomorrowInModal) MaterialTheme.colorScheme.primary else Color.Transparent)
                                        .clickable { isTomorrowInModal = true }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "రేపు",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isTomorrowInModal) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        // Date Header
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                            border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${rashi.teluguName} ఫలాలు (${dailyData.dateStr})",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }

                        // Main Narrative
                        Text(
                            text = dailyData.mainNarrative,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 18.sp
                        )

                        // Lucky Attributes Row
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("• అదృష్ట సంఖ్య :- ${dailyData.luckyNumber}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                Text("• అదృష్ట రంగు :- ${dailyData.luckyColor}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                Text("• చికిత్స / పరిహారం :- ${dailyData.remedy}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                            }
                        }

                        // 6-Category Detailed Breakdown
                        Text(
                            text = "ఈరోజు ఫలితాలు:",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                CategoryResultItem(icon = "🌿", title = "ఆరోగ్యం", desc = dailyData.health)
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 0.8.dp)
                                CategoryResultItem(icon = "💰", title = "సంపద & ఆర్థికం", desc = dailyData.wealth)
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 0.8.dp)
                                CategoryResultItem(icon = "👨‍👩‍👧", title = "కుటుంబం", desc = dailyData.family)
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 0.8.dp)
                                CategoryResultItem(icon = "❤️", title = "ప్రేమ సంబంధిత విషయాలు", desc = dailyData.love)
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 0.8.dp)
                                CategoryResultItem(icon = "💼", title = "వృత్తి & ఉద్యోగం", desc = dailyData.career)
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 0.8.dp)
                                CategoryResultItem(icon = "💍", title = "వివాహితుల జీవితం", desc = dailyData.marriedLife)
                            }
                        }
                    }

                    RashiSection.WEEKLY -> {
                        val weeklyData = remember(rashi.id) {
                            RashiPhalaluData.getWeeklyHoroscope(rashi.id)
                        }

                        // Week Header
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                            border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🗓️ ${rashi.teluguName} - ${weeklyData.weekPeriodStr}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }

                        // Weekly Planetary Transit Forecast Narrative
                        Text(
                            text = weeklyData.planetaryTransitForecast,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 18.sp
                        )

                        // Weekly Highlights Card
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("• అదృష్ట దినాలు :- ${weeklyData.luckyDays}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                Text("• అదృష్ట రంగు :- ${weeklyData.luckyColor}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                Text("• అదృష్ట సంఖ్య :- ${weeklyData.luckyNumber}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                Text("• వారం పరిహారం :- ${weeklyData.remedy}", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = TeluguRedDark)
                            }
                        }

                        // Guidance Items
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                CategoryResultItem(icon = "💼", title = "వృత్తి సూచనలు", desc = weeklyData.careerGuidance)
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 0.8.dp)
                                CategoryResultItem(icon = "🩺", title = "ఆరోగ్య సంరక్షణ", desc = weeklyData.healthGuidance)
                            }
                        }
                    }

                    RashiSection.MONTHLY -> {
                        val monthlyData = remember(rashi.id) {
                            RashiPhalaluData.getMonthlyHoroscope(rashi.id)
                        }

                        // Month Header
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                            border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🌙 ${rashi.teluguName} - ${monthlyData.monthYearStr}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }

                        // Overview
                        Text(
                            text = monthlyData.overviewForecast,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 18.sp
                        )

                        // Monthly Detail Cards
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                CategoryResultItem(icon = "💼", title = "వృత్తి & వ్యాపారం", desc = monthlyData.careerBusiness)
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 0.8.dp)
                                CategoryResultItem(icon = "💰", title = "ఆర్థిక స్థితి & ఖర్చులు", desc = monthlyData.financeWealth)
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 0.8.dp)
                                CategoryResultItem(icon = "📚", title = "విద్యార్థుల చదువు", desc = monthlyData.educationStudents)
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 0.8.dp)
                                CategoryResultItem(icon = "👨‍👩‍👧", title = "కుటుంబం, ప్రేమ & వివాహం", desc = monthlyData.familyLoveMarriage)
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 0.8.dp)
                                CategoryResultItem(icon = "🩺", title = "ఆరోగ్య సూచనలు", desc = monthlyData.healthWellness)
                            }
                        }

                        // Monthly Remedy
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                            border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🙏 మాస పరిహారం: ${monthlyData.monthlyRemedy}",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                lineHeight = 16.sp,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    RashiSection.YEARLY, RashiSection.ENGLISH_YEAR -> {
                        val yearlyData = remember(rashi.id) {
                            RashiPhalaluData.getYearlyHoroscope(rashi.id)
                        }

                        // Samvatsaram Header
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                            border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🪐 ${rashi.teluguName} - ${yearlyData.yearSamvatsaraStr}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }

                        // Adaya-Vyaya & Rajapoojya Grid
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFFF8E1), // Warm Sandalwood Gold
                            border = BorderStroke(1.dp, Color(0xFFFFE082)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                // Adayam Pill
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("ఆదాయం", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF5A4D3D))
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFFE8F5E9),
                                        border = BorderStroke(0.8.dp, Color(0xFFA5D6A7))
                                    ) {
                                        Text("${yearlyData.adayam}", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1B5E20), modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                                    }
                                }
                                // Vyayam Pill
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("వ్యయం", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF5A4D3D))
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFFFFEBEE),
                                        border = BorderStroke(0.8.dp, Color(0xFFEF9A9A))
                                    ) {
                                        Text("${yearlyData.vyayam}", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFB71C1C), modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                                    }
                                }
                                // Rajapoojyam Pill
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("రాజపూజ్యం", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF5A4D3D))
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFFE3F2FD),
                                        border = BorderStroke(0.8.dp, Color(0xFF90CAF9))
                                    ) {
                                        Text("${yearlyData.rajapoojyam}", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0D47A1), modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                                    }
                                }
                                // Avamanam Pill
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("అవమానం", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF5A4D3D))
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFFFFF3E0),
                                        border = BorderStroke(0.8.dp, Color(0xFFFFCC80))
                                    ) {
                                        Text("${yearlyData.avamanam}", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFE65100), modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                                    }
                                }
                            }
                        }

                        // General Overview
                        Text(
                            text = yearlyData.generalOverview,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 18.sp
                        )

                        // In-depth Annual Sections
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                CategoryResultItem(icon = "💼", title = "ఉద్యోగం, వృత్తి & వ్యాపారం", desc = yearlyData.careerAndProfession)
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 0.8.dp)
                                CategoryResultItem(icon = "💰", title = "ఆర్థిక స్థితి & ఆదాయ పెరుగుదల", desc = yearlyData.financialOutlook)
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 0.8.dp)
                                CategoryResultItem(icon = "🏠", title = "ఆస్తి, రియల్ ఎస్టేట్ & వాహనాలు", desc = yearlyData.propertyAndVehicles)
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 0.8.dp)
                                CategoryResultItem(icon = "🎓", title = "విద్యార్థులు & చదువు", desc = yearlyData.studentsAndEducation)
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 0.8.dp)
                                CategoryResultItem(icon = "💍", title = "ప్రేమ, వివాహం & వైవాహిక జీవితం", desc = yearlyData.loveAndMarriage)
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 0.8.dp)
                                CategoryResultItem(icon = "👨‍👩‍👧", title = "కుటుంబ జీవనం & సామరస్యం", desc = yearlyData.familyHarmony)
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 0.8.dp)
                                CategoryResultItem(icon = "🩺", title = "ఆరోగ్యం & జాగ్రత్తలు", desc = yearlyData.healthAndLongevity)
                            }
                        }

                        // Annual Remedy
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                            border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🙏 వార్షిక విశేష పరిహారం: ${yearlyData.annualPariharam}",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                lineHeight = 16.sp,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    else -> {
                        // Traits & General
                        val swabhavaData = remember(rashi.id) {
                            com.example.engine.RashiNakshatra108Data.JANMA_RASHI_SWABHAVALU_LIST.firstOrNull { it.rashiId == rashi.id }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("• రాశ్యాధిపతి: ${rashi.teluguRuler}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                Text("• పంచభూత తత్త్వం: ${rashi.teluguElement}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                if (swabhavaData != null) {
                                    Text("• స్వభావం: ${swabhavaData.svabhavam} | లింగం: ${swabhavaData.lingam}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                    Text("• ప్రతీకం: ${swabhavaData.pratikam} | దిశ: ${swabhavaData.disha}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                    Text("• కాళపురుష భాగం: ${swabhavaData.kalapurushaBhagam}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                }
                                Text("• అదృష్ట సంఖ్య: ${rashi.luckyNumber} | రంగు: ${rashi.teluguColor}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                Text("• అదృష్ట రత్నం: ${rashi.teluguGemstone}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                Text("• నక్షత్రాలు: ${rashi.teluguNakshatras}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                Text("• నామకరణ అక్షరాలు: ${rashi.teluguNamingLetters}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }

                        if (swabhavaData != null) {
                            Text(
                                text = "ప్రధాన స్వభావం:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = swabhavaData.pradhanaSvabhavam,
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 17.sp
                            )

                            Text(
                                text = "సాధారణంగా చెప్పే లక్షణాలు:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2B8A3E)
                            )
                            Text(
                                text = swabhavaData.sadharanaLakshanalu,
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 17.sp
                            )

                            if (swabhavaData.manchiVaipu.isNotBlank()) {
                                Text(
                                    text = "మంచి వైపు (ధనాత్మక లక్షణాలు):",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1971C2)
                                )
                                Text(
                                    text = swabhavaData.manchiVaipu,
                                    fontSize = 11.5.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 17.sp
                                )
                            }

                            Text(
                                text = "జాగ్రత్తగా చూడవలసిన వైపు / సవాళ్లు:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD9480F)
                            )
                            Text(
                                text = swabhavaData.savallu,
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 17.sp
                            )

                            Text(
                                text = "సంప్రదాయ వృత్తి ఆసక్తులు / రంగాలు:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF5C7CFA)
                            )
                            Text(
                                text = swabhavaData.vruttiRangalu,
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 17.sp
                            )
                        } else {
                            Text(
                                text = "రాశి స్వభావం & విశేష లక్షణాలు:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = rashi.teluguDescription,
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 17.sp
                            )
                        }

                        /* Source note hidden */
                    }
                }

        Spacer(modifier = Modifier.height(4.dp))

        // Return / Back Button
        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("← వెనుకకు", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun CategoryResultItem(icon: String, title: String, desc: String) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 12.sp)
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "$title:",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = desc,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 15.sp,
            modifier = Modifier.padding(start = 17.dp)
        )
    }
}

/**
 * Nakshatra Detail Inline View (Middle space only)
 */
@Composable
fun NakshatraDetailInlineView(
    nakshatra: NakshatraDetail,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Header
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${nakshatra.number}. ${nakshatra.teluguName}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "అధిదేవత: ${nakshatra.teluguDeity}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "మూసివేయి",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("• అధిపతి గ్రహం: ${nakshatra.teluguRuler}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                Text("• రాశి & పాదాలు: ${nakshatra.teluguRashi} (${nakshatra.padasRange} పాదాలు)", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                if (nakshatra.degreeRange.isNotBlank()) {
                    Text("• రాశి పరిధి: ${nakshatra.degreeRange}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                }
                Text("• గణం: ${nakshatra.teluguGana} | యోని: ${nakshatra.teluguYoni}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                Text("• నామకరణ అక్షరాలు: ${nakshatra.teluguNamingLetters}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
            }
        }

        Text(
            text = "నక్షత్ర ప్రధాన లక్షణాలు:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = nakshatra.teluguTraits,
            fontSize = 11.5.sp,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 17.sp
        )

        if (nakshatra.classicalDescription.isNotBlank()) {
            Text(
                text = "బృహజ్జాతక / బృహత్సంహిత ప్రాచీన వివరణ:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = nakshatra.classicalDescription,
                fontSize = 11.5.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 17.sp
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Return / Back Button
        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("← వెనుకకు", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
