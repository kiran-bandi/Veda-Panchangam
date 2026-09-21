package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.engine.AstronomicalEngine
import com.example.engine.LocalizationEngine
import com.example.engine.MonthDataManager
import com.example.engine.MonthEventOccurrence
import com.example.model.AppLanguage
import com.example.model.CalendarTradition
import com.example.model.CityLocation
import com.example.ui.theme.TeluguGreen
import com.example.ui.theme.TeluguRed
import com.example.ui.theme.TeluguRedLight
import java.time.LocalDate
import java.time.YearMonth

enum class ImportantTithiType(val teluguName: String, val emoji: String) {
    AMAVASYA("అమావాస్య", "🌑"),
    POURNAMI("పౌర్ణమి", "🌕"),
    EKADASHI("ఏకాదశి", "🙏"),
    PRADOSHAM("ప్రదోషం", "🐂"),
    SANKATAHARA_CHATURTHI("సంకటహర చతుర్థి", "🐘"),
    MASA_SHIVARATRI("మాస శివరాత్రి", "🔱"),
    SASHTHI("షష్ఠి", "🦚"),
    ASHTAMI("అష్టమి", "🌺"),
    NAVAMI("నవమి", "🏹")
}

data class DynamicTithiOccurrence(
    val date: LocalDate,
    val tithiType: ImportantTithiType,
    val title: String,
    val hinduMasa: String,
    val paksha: String,
    val significance: String
)

@Composable
fun ImportantTithisScreen(
    onNavigateBack: (() -> Unit)? = null,
    onSelectDate: ((LocalDate) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var selectedYear by remember { mutableStateOf(2026) }
    var selectedTithi by remember { mutableStateOf(ImportantTithiType.EKADASHI) }
    var viewMode by remember { mutableStateOf(0) } // 0: Annual Tithi List, 1: Month Fasting List
    var selectedMonth by remember { mutableStateOf(9) } // Default September

    val location = remember { AstronomicalEngine.CITIES.first() }
    val tradition = remember { CalendarTradition.TELUGU }

    // Fully dynamic astronomical calculations for the entire year
    val annualOccurrences = remember(selectedYear, selectedTithi) {
        calculateDynamicYearTithis(selectedYear, selectedTithi, location, tradition)
    }

    // Fully dynamic month fasting list (strictly one row per event occurrence)
    val monthFastingOccurrences = remember(selectedYear, selectedMonth) {
        val ym = YearMonth.of(selectedYear, selectedMonth)
        val monthData = MonthDataManager.getMonthPanchanga(ym, location, tradition, AppLanguage.TE)
        monthData.fastingOccurrences
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("important_tithis_screen")
    ) {
        // Top Header Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF8D1808),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (onNavigateBack != null) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "వెనుకకు",
                            tint = Color(0xFFFFD54F)
                        )
                    }
                }
                Text(
                    text = "ముఖ్యమైన తిథులు & ఉపవాసాలు",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD54F),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        // View Mode Switcher: "తిథి వారిగా (వార్షిక)" vs "నెల ఉపవాసాలు"
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFFFFF8E1),
            border = BorderStroke(1.dp, Color(0xFFFFD54F)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (viewMode == 0) Color(0xFF8D1808) else Color.Transparent)
                        .clickable { viewMode = 0 }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "తిథి వారిగా (వార్షిక)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (viewMode == 0) Color(0xFFFFD54F) else Color(0xFF5A4D3D)
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (viewMode == 1) Color(0xFF8D1808) else Color.Transparent)
                        .clickable { viewMode = 1 }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "నెల ఉపవాసాలు",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (viewMode == 1) Color(0xFFFFD54F) else Color(0xFF5A4D3D)
                    )
                }
            }
        }

        if (viewMode == 0) {
            // Tithi Chips Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 14.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ImportantTithiType.values().forEach { tithi ->
                    val isSelected = tithi == selectedTithi
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedTithi = tithi },
                        label = {
                            Text(
                                text = "${tithi.emoji} ${tithi.teluguName}",
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF8D1808),
                            selectedLabelColor = Color(0xFFFFD54F),
                            containerColor = Color(0xFFFFFDF8),
                            labelColor = Color(0xFF5A4D3D)
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) Color(0xFFFFD54F) else Color(0xFFFFE082)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }

            // Year Selector Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$selectedYear ${selectedTithi.teluguName} దినములు (${annualOccurrences.size})",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8D1808)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(2025, 2026, 2027).forEach { yr ->
                        val isSelected = yr == selectedYear
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) Color(0xFF8D1808) else Color(0xFFFFFDF8),
                            border = BorderStroke(1.dp, if (isSelected) Color(0xFFFFD54F) else Color(0xFFFFE082)),
                            modifier = Modifier.clickable { selectedYear = yr }
                        ) {
                            Text(
                                text = "$yr",
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color(0xFFFFD54F) else Color(0xFF5A4D3D),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Annual Occurrences List (One occurrence per row)
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(annualOccurrences) { occ ->
                    DynamicTithiCard(occ = occ, onClick = { onSelectDate?.invoke(occ.date) })
                }
            }
        } else {
            // Month Selector Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 14.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                (1..12).forEach { m ->
                    val isSelected = m == selectedMonth
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) Color(0xFF8D1808) else Color(0xFFFFFDF8),
                        border = BorderStroke(1.dp, if (isSelected) Color(0xFFFFD54F) else Color(0xFFFFE082)),
                        modifier = Modifier.clickable { selectedMonth = m }
                    ) {
                        Text(
                            text = LocalizationEngine.getTeluguMonthName(m),
                            fontSize = 11.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color(0xFFFFD54F) else Color(0xFF5A4D3D),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Header for Month Fasting (One Row Per Event Occurrence)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${LocalizationEngine.getTeluguMonthName(selectedMonth)} $selectedYear ఉపవాస దినములు",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8D1808)
                )
                Text(
                    text = "మొత్తం ${monthFastingOccurrences.size} విశేషాలు",
                    fontSize = 11.sp,
                    color = Color(0xFF5A4D3D)
                )
            }

            // Month Occurrences List (Strictly one row per event occurrence)
            if (monthFastingOccurrences.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text("ఈ నెలలో ఉపవాస దినాలు లేవు", fontSize = 13.sp, color = Color(0xFF5A4D3D))
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(monthFastingOccurrences) { item ->
                        MonthFastingOccurrenceCard(item = item, onClick = { onSelectDate?.invoke(item.date) })
                    }
                }
            }
        }
    }
}

/**
 * Annual Tithi Occurrence Card
 */
@Composable
private fun DynamicTithiCard(occ: DynamicTithiOccurrence, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
        border = BorderStroke(1.dp, Color(0xFFFFE082)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Top Date
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${occ.date.dayOfMonth}/${occ.date.monthValue}/${occ.date.year}",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF8D1808)
                )
            }

            // Yellow / Amber Strip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF8E1))
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${LocalizationEngine.getTeluguMonthName(occ.date.monthValue)} - ${LocalizationEngine.getTeluguDayOfWeek(occ.date.dayOfWeek)}",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8D1808)
                )
            }

            // Split Bottom: Masa on left (Green), Tithi / Title on right (Orange)
            Row(modifier = Modifier.fillMaxWidth().height(42.dp)) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFFE8F5E9)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${occ.hinduMasa} - ${occ.date.dayOfMonth}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFFFFF3E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${occ.tithiType.emoji} ${occ.title}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE65100)
                    )
                }
            }
        }
    }
}

/**
 * Month Fasting Item Card (One row per event occurrence)
 */
@Composable
private fun MonthFastingOccurrenceCard(item: MonthEventOccurrence, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
        border = BorderStroke(1.dp, Color(0xFFFFE082)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = CircleShape,
                    color = item.color.copy(alpha = 0.12f),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = item.iconEmoji, fontSize = 20.sp)
                    }
                }
                Column {
                    Text(
                        text = item.title,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF211A13)
                    )
                    Text(
                        text = "${item.weekdayTelugu} · ${item.significance}",
                        fontSize = 11.sp,
                        color = Color(0xFF5A4D3D)
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF8D1808)
            ) {
                Text(
                    text = "${item.dayOfMonth}వ తేదీ",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFFFD54F),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

private val dynamicTithisCache = java.util.concurrent.ConcurrentHashMap<String, List<DynamicTithiOccurrence>>()

/**
 * Computes exact annual occurrences for any tithi type across 365/366 days using AstronomicalEngine
 */
private fun calculateDynamicYearTithis(
    year: Int,
    tithiType: ImportantTithiType,
    location: CityLocation,
    tradition: CalendarTradition
): List<DynamicTithiOccurrence> {
    val cacheKey = "${year}_${tithiType.name}_${location.id}_${tradition.name}"
    dynamicTithisCache[cacheKey]?.let { return it }

    val list = mutableListOf<DynamicTithiOccurrence>()
    val daysInYear = if (java.time.Year.of(year).isLeap) 366 else 365
    var currentDate = LocalDate.of(year, 1, 1)

    for (i in 0 until daysInYear) {
        val fast = AstronomicalEngine.calculateFastTithiMasa(currentDate, location, tradition)
        val tithiNum = fast.tithiNumber
        val tithiName = fast.tithiName
        val paksha = fast.paksha
        val isShukla = fast.isShukla
        val pakshaStr = if (isShukla) "శుక్ల పక్షం" else "కృష్ణ పక్షం"
        val masaStr = LocalizationEngine.translateMasa(fast.hinduMasa, AppLanguage.TE)
        val cleanMasaName = masaStr.replace("మాసం", "").trim()

        val matches = when (tithiType) {
            ImportantTithiType.AMAVASYA -> tithiNum == 30 || tithiName.contains("Amavasya", ignoreCase = true)
            ImportantTithiType.POURNAMI -> tithiNum == 15 || tithiName.contains("Purnima", ignoreCase = true) || tithiName.contains("Pournami", ignoreCase = true)
            ImportantTithiType.EKADASHI -> tithiNum == 11 || tithiNum == 26 || tithiName.contains("Ekadashi", ignoreCase = true)
            ImportantTithiType.PRADOSHAM -> tithiNum == 13 || tithiNum == 28 || tithiName.contains("Trayodashi", ignoreCase = true)
            ImportantTithiType.SANKATAHARA_CHATURTHI -> !isShukla && (tithiNum == 19 || tithiNum % 15 == 4)
            ImportantTithiType.MASA_SHIVARATRI -> !isShukla && (tithiNum == 29 || tithiNum % 15 == 14)
            ImportantTithiType.SASHTHI -> tithiNum == 6 || tithiNum == 21 || tithiName.contains("Shashti", ignoreCase = true) || tithiName.contains("Sashti", ignoreCase = true)
            ImportantTithiType.ASHTAMI -> tithiNum == 8 || tithiNum == 23 || tithiName.contains("Ashtami", ignoreCase = true)
            ImportantTithiType.NAVAMI -> tithiNum == 9 || tithiNum == 24 || tithiName.contains("Navami", ignoreCase = true)
        }

        if (matches) {
            val title = when (tithiType) {
                ImportantTithiType.AMAVASYA -> "$cleanMasaName అమావాస్య"
                ImportantTithiType.POURNAMI -> "$cleanMasaName పౌర్ణమి"
                ImportantTithiType.EKADASHI -> if (isShukla) "శుక్ల ఏకాదశి" else "కృష్ణ ఏకాదశి"
                ImportantTithiType.PRADOSHAM -> if (isShukla) "శుక్ల ప్రదోషం" else "కృష్ణ ప్రదోషం"
                ImportantTithiType.SANKATAHARA_CHATURTHI -> "సంకటహర చతుర్థి"
                ImportantTithiType.MASA_SHIVARATRI -> "మాస శివరాత్రి"
                ImportantTithiType.SASHTHI -> if (isShukla) "శుక్ల షష్ఠి" else "కృష్ణ షష్ఠి"
                ImportantTithiType.ASHTAMI -> if (isShukla) {
                    if (cleanMasaName.contains("ఆశ్వయుజ")) "దుర్గాష్టమి (మహాష్టమి)"
                    else "శుక్ల అష్టమి (దుర్గాష్టమి)"
                } else {
                    if (cleanMasaName.contains("శ్రావణ")) "శ్రీ కృష్ణాష్టమి (గోకులాష్టమి)"
                    else "కృష్ణ అష్టమి (కాలాష్టమి)"
                }
                ImportantTithiType.NAVAMI -> if (isShukla) {
                    if (cleanMasaName.contains("చైత్ర")) "శ్రీరామనవమి (శుక్ల నవమి)"
                    else if (cleanMasaName.contains("ఆశ్వయుజ")) "మహార్నవమి (శుక్ల నవమి)"
                    else "శుక్ల నవమి"
                } else "కృష్ణ నవమి"
            }
            val significance = when (tithiType) {
                ImportantTithiType.AMAVASYA -> "పితృ తర్పణం & పూర్వీకుల ఆరాధన"
                ImportantTithiType.POURNAMI -> "సత్యనారాయణ స్వామి వ్రతం & చంద్ర పూజ"
                ImportantTithiType.EKADASHI -> "శ్రీ మహావిష్ణువు పూజ & ఉపవాసం"
                ImportantTithiType.PRADOSHAM -> "శివ పార్వతుల ప్రదోషకాల ఆరాధన"
                ImportantTithiType.SANKATAHARA_CHATURTHI -> "గణపతి పూజ & చంద్ర దర్శనం"
                ImportantTithiType.MASA_SHIVARATRI -> "పరమేశ్వర లింగోద్భవ పూజ & జాగరణ"
                ImportantTithiType.SASHTHI -> "శ్రీ సుబ్రహ్మణ్యేశ్వర స్వామి పూజ"
                ImportantTithiType.ASHTAMI -> if (isShukla) "శ్రీ దుర్గాదేవి విశేష ఆరాధన & నవరాత్రి పూజ" else "శ్రీ కాలభైరవ స్వామి పూజ / శ్రీకృష్ణ జన్మోత్సవం"
                ImportantTithiType.NAVAMI -> if (isShukla) "శ్రీ సీతారామ కళ్యాణ మహోత్సవం & దేవి పూజ" else "శ్రీరామ స్మరణ & మాతృశక్తి ఆరాధన"
            }

            list.add(
                DynamicTithiOccurrence(
                    date = currentDate,
                    tithiType = tithiType,
                    title = title,
                    hinduMasa = "${masaStr}మాసం",
                    paksha = pakshaStr,
                    significance = significance
                )
            )
        }
        currentDate = currentDate.plusDays(1)
    }
    dynamicTithisCache[cacheKey] = list
    return list
}
